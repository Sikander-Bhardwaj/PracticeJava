package com.cogniter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thingmagic.Reader;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Connection {
	private String osName;
	private Socket sock = null;
	private boolean connectionStatus;
	private List<String> listOfReader;
	JSONObject readerJObj;
	String ip;
	String name;
	JSONArray jsonArrName;
	JSONArray jsonArrrIp;
	JSONObject user;
	String command;
	String companyId;
	String userName;
	String userEmail;
	String deviceId;
	String macAdd;
	String portNo;
	String userId;
	String option;
	JSONObject jObj;
	RFIDServices rfidServices;
	Reader r;
	Timer timer;
	String port;
	String apiUrl = "https://stageattendancemgmtapi.talygen.com/api/SaveDeviceFromApi";
	String serialNumber;
	
	public Connection(String serialNumber, String macAdd) throws InterruptedException, JSONException, IOException {
		this.serialNumber = serialNumber;
		this.macAdd = macAdd;
		try {
			sock = IO.socket("https://stagechat.talygen.com");

			sock.connect();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connectionStatus = sock.connected();
			System.out.println(connectionStatus);

			if (connectionStatus == true) {
				System.out.println("Successfully Connected!!!!!!!!!!!!!!!");
				UserDetails ud = new UserDetails();
				user = ud.UserDetailss();
				System.out.println("User " + user);
				companyId = user.getString("companyId");
				userEmail = user.getString("Email");
				userId = user.getString("userId");
				osName = System.getProperty("os.name");
				
				
				
				if (osName.contains("Windows")) {
					System.out.println("Windows");

					DiscoverListOfRfids discover = new DiscoverListOfRfids();
					listOfReader = new ArrayList<>();
					listOfReader = discover.discov();
					Thread.sleep(5000);
					readerJObj = new JSONObject();
					jsonArrName = new JSONArray();
					jsonArrrIp = new JSONArray();
					for (String reader : listOfReader) {
						JSONObject obj = new JSONObject();
						System.out.println(reader);
						int iend = reader.indexOf("on");

						String subString;
						if (iend != -1) {
							subString = reader.substring(0, iend);
							System.out.println("Reader Name :" + subString);
							name = subString;
							obj.put("name", name);

						}
						port = reader.substring(reader.indexOf("at")+2, reader.length());
						obj.put("port", port.trim());
						ip = reader.substring(reader.indexOf("on") + 2, reader.length()).replaceAll("at", " ").replaceAll(port, " ");
						System.out.println("IP is :" + ip);
						obj.put("ip", ip.trim());
						obj.put("serialNumber", this.serialNumber);
						obj.put("macAdd", this.macAdd);
						obj.put("RaspberryPiId", "Pi1");
						readerJObj.append("RFID", obj);
						

					}

					System.out.println("JSON is " + readerJObj);
					URL url = new URL(apiUrl);
			        System.out.println("URL " + url);
			        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			        conn.setRequestMethod("POST");
			        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			        conn.setRequestProperty("Accept", "application/json");

			        conn.setDoOutput(true);
			        
			        try (OutputStream os = conn.getOutputStream()) {
			        byte[] input = readerJObj.toString().getBytes("utf-8");
			        os.write(input, 0, input.length);
			        System.out.println("Data saved...............................");
			        System.out.println("Response Message:" + conn.getResponseMessage());
			        System.out.println("Response Code:" + conn.getResponseCode());
			        System.out.println("jsonObj" + readerJObj);
			        if(conn.getResponseCode()==200) {
			        	sock.emit("commandFromWebApp", "success");
			        }else {
			        	sock.emit("commandFromWebApp", "Fail");
			        }
				}catch(Exception e) {
					e.printStackTrace();
				}
			        

					if (connectionStatus) {
						System.out.println("Connection created");
						sock.emit("add user", this.user);

						
						/*sock.on("sendCommandToPi", new Emitter.Listener() {
							
							@Override
							public void call(Object... args) {
								String com = args.toString();
								System.out.println("CommandCommand "+com);
								
							}
						});*/
						sock.on("sendCommandToPi", new Emitter.Listener() {

							public void call(Object... arg0) {
								// TODO Auto-generated method stub
								System.out.println("69");
								JSONObject jsonObj = (JSONObject) arg0[0];
								System.out.println("?" + arg0[0].toString());
								try {
									System.out.println(jsonObj.getString("ip"));
									System.out.println(jsonObj.getString("name"));
									System.out.println(jsonObj.getString("command"));
									ip = jsonObj.getString("ip");
									name = jsonObj.getString("name");
									command = jsonObj.getString("command");
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								if(command.equalsIgnoreCase("getReaders")) {
									sock.emit("commandFromWebApp", readerJObj);
									System.out.println("Command is "+command+" and readers list sent "+readerJObj);
								}
								if (command.equalsIgnoreCase("connect")) {
									System.out.println(
											"Command is " + command + " IP is " + ip + " Name of Reader is " + name);
									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									sock.emit("commandFromWebApp", jObj);
									rfidServices = new RFIDServices();
									r = rfidServices.connectToReader(ip, userEmail, companyId,"5084", userId, "DeviceID", "MacAdd");
									System.out.println("Reader is "+r);
								/*	String connectionStatus = rfidServices.connectToReader(ip, userEmail, companyId,
											"5084", userId, "DeviceID", "MacAdd");
									if (connectionStatus.equalsIgnoreCase("Connected")) {
										jObj = new JSONObject();
										try {
											jObj.put("name", name);
										} catch (JSONException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										try {
											jObj.put("command", command);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										sock.emit("commandFromWebApp", jObj);
										System.out.println("Reader Connected!!!!!!!!!!!!!!!!!!!");
									} else if (connectionStatus.equalsIgnoreCase("Not Connected")) {
										sock.emit("commandFromWebApp", "Connection not established");
										System.out.println("Connection not established!!!!!!!!!!!!!!!");
									} else if (connectionStatus
											.equalsIgnoreCase("Reader is already connected to device")) {
										sock.emit("commandFromWebApp", "Reader is already connected");
										System.out.println("Reader is already connected!!!!!!!!!!!!!!!!!!!!");
									}*/

								}
								if (command.equalsIgnoreCase("start")) {
									
									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
									System.out.println("Command is " + command);
									rfidServices = new RFIDServices();
									timer = new Timer();
									timer = rfidServices.startReadingTags(r,sock);
									

									// service.startReadingTags();
								}if (command.equalsIgnoreCase("stop")) {

									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
									System.out.println("Command is " + command);
									rfidServices = new RFIDServices();
									rfidServices.stopReadingTags(r,timer);
								}
								if(command.equalsIgnoreCase("checkIn")) {
									option = command;
									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
									rfidServices = new RFIDServices();
									rfidServices.checkInOut(option);
									System.out.println("Selected option is "+option);
								}
								if(command.equalsIgnoreCase("checkOut")) {
									option = command;
									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
									rfidServices = new RFIDServices();
									rfidServices.checkInOut(option);
									System.out.println("Selected option is "+option);
								}
								if(command.equalsIgnoreCase("Audit")) {
									option = command;
									jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
									rfidServices = new RFIDServices();
									rfidServices.checkInOut(option);
									System.out.println("Selected option is "+option);
								}
							    if(command.equalsIgnoreCase("disconnect")) {
							    	jObj = new JSONObject();
									try {
										jObj.put("name", name);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										jObj.put("command", command);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									sock.emit("commandFromWebApp", jObj);
							    	System.out.println("Reader disconnected "+name);
							    	rfidServices = new RFIDServices();
									rfidServices.disconnect(r, timer);
							    	
							    }

							}
						});

					}

				} else {
					System.out.println("Linux");
					GetReaderIp getIp = new GetReaderIp();
					// ipAddOfReader = getIp.getIp();
				}

			} else {
				System.out.println("  not connected!!!!!!!!!!!!");
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}
	

}
