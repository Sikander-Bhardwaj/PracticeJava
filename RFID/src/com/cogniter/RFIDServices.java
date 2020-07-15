package com.cogniter;



import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import javax.swing.JFrame;


import org.json.*;

import com.cogniter.Discover;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TagProtocol;
import com.thingmagic.TagReadData;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class RFIDServices {
	private String ipAdd;
	private String email_Idd;
	private String company_Id;
	private	String device_Id;
	private	String port;
	private	String user_Id;
	private int checkInOut;
	private String mac_Add;
	String apiUrl="http://192.168.0.200:8095";
	JSONObject jObj;
	JSONObject jObj1;
	 JSONObject jObj2;
	Reader r;
	TagReadData[] tagReads;
	Timer timer;
	 List<String> l1;
	    List<String> l2;
	    List<String> removed;
	    List<String> added;
	    int listCounter = 0;
	    int saveCounter = 0;
	    JFrame jFrame;
	    String ipAddOfReader="";
	    int counter = 0;
	    
	    
	  
	    
	    
	/*This method accepts getReaders as command and fetch the available llrp reader*/
	
	
	public String fetchReader(String command) {
		if (command.equalsIgnoreCase("getReaders")) {
			Discover discover = new Discover();
			discover.discov();
//			GetReaderIp getIp = new GetReaderIp();
//			ipAddOfReader = getIp.getIp();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(">>>>>>>>>>>>>>>>>>>"+Discover.ipAdd);
			ipAdd = Discover.ipAdd;
			/*
			 * if(ipAdd!=null || ipAdd!="") { connectToReader(ipAdd); }
			 */
//			jFrame = new JFrame();
//			JOptionPane.showMessageDialog(jFrame, "Ip Address of Reader is "+ipAddOfReader);
			if(ipAdd.equalsIgnoreCase(null)) {
				return "No RFID Reader found!!!!!!!";
			}
			else if(ipAdd.equalsIgnoreCase("Exception in founding Rfid Reader"))  {
				return "Not Found";
			}else {
				return ipAdd;
			}
			
		} else {
			return "Incorrect command";
		}

	}
	
	/*This method will connect to reader if and only if the ip address of the RFID reader is available*/
	
	public Reader connectToReader(String ipAdd,String emailId, String companyId, String portNo, String userId,String deviceId,String macAdd) {
		
		email_Idd = emailId;
		company_Id = companyId;
		port = portNo;
		user_Id = userId;
		device_Id = deviceId;
		mac_Add = macAdd;
		//String createConn = "tmr:///"+ipAdd;
		String finalIp = ipAdd.trim();
		   try{
			   if(r!=null) {
				  // return "Reader is already connected to device";
			   }else {
				   System.out.println("Ip Add is "+finalIp);
				   
				   //System.out.println("Create connection with "+createConn);
				   r = Reader.create("tmr://"+finalIp);
		            r.connect();
		             if (Reader.Region.UNSPEC == (Reader.Region) r.paramGet("/reader/region/id")) {
		                r.paramSet("/reader/region/id", Reader.Region.NA);
		            }
		        
		        int[] antennaList = (int[]) r.paramGet("/reader/antenna/portList");
		        System.out.println("Antenna Count " + antennaList.length);
		        tagReads = r.read(500);
		        int a[] = new int[1];
		        a[0] = 1;
		        SimpleReadPlan plan = new SimpleReadPlan(a, TagProtocol.GEN2, null, null, 1000);

		        r.paramSet("/reader/read/plan", plan);
//		        jFrame = new JFrame();
//				JOptionPane.showMessageDialog(jFrame, "Connected");
		        return r;
		        
			   }
	           
	        }catch(ReaderException e){
	            e.printStackTrace();
	        }
		  /* if(r!=null) {
			   return "Connected";
		   }else {
			   return "Not Connected";
		   }*/
		   return r;
	}
	
	/*This method will start reading tags*/
	
	
	public Timer startReadingTags(Reader r, Socket sock) {
		
//		jFrame = new JFrame();
//		JOptionPane.showMessageDialog(jFrame, "Reader is reading Tags");
        
timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               // counter++;
                    

                try {
                    tagReads = r.read(500);
                } catch (ReaderException ex) {
                    ex.printStackTrace();
                }

        int a[] = new int[1];
        a[0] = 1;
        SimpleReadPlan plan = new SimpleReadPlan(a, TagProtocol.GEN2, null, null, 1000);

                try {
                    r.paramSet("/reader/read/plan", plan);
                } catch (ReaderException ex) {
                    
                }
                JSONObject jObjTags = new JSONObject();
for (TagReadData p : tagReads) {
            System.out.println("<>" + p.toString());
            try {
				jObjTags.put("tag", p.toString());
				jObjTags.put("name", "Sargas-93bb4f.local");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
sock.emit("commandFromWebApp", jObjTags);
            
//            if (counter == 1) {
//            try {
//                saveTagsFirstTime();
//                 saveAddedOrRemovedTags();
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//        } else {
//            try {
//                saveAddedOrRemovedTags();
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//        }

        }


            }
        }, 0, 1000);
		return timer;
		
	}
	
	
	
	public String checkInOut(String command) {
		if(command.equalsIgnoreCase("checkIn")) {
			checkInOut = 0;
			System.out.println("checkIn value is "+checkInOut);
//			jFrame = new JFrame();
//			JOptionPane.showMessageDialog(jFrame, "Check In");
			return "check In ";
		}else if(command.equalsIgnoreCase("checkOut")) {
			checkInOut = 1;
			System.out.println("Check Out Value is "+checkInOut);
//			jFrame = new JFrame();
//			JOptionPane.showMessageDialog(jFrame, "Check Out");
			return "check Out";
		}else if(command.equalsIgnoreCase("audit")){
			checkInOut = 2;
			System.out.println("In Audit value for ");
			return "audit";
		}else {
			System.out.println("Do nothing");
			return "nothing";
		}
		
	}
	
	
	
	/*This method will stop reading tags*/
	
	public void stopReadingTags(Reader r, Timer timer) {
		
		r.stopReading();
		timer.cancel();
		System.out.println("Stopped in service !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		jFrame = new JFrame();
//		JOptionPane.showMessageDialog(jFrame, "Reader is stopped ");
		
	}
	
	/*This method will destroy the connection from reader*/
	
	public String disconnect(Reader r, Timer timer) {
System.out.println("Disconnect in service!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(r!=null) {
			timer.cancel();
			r.destroy();
			return "Reader Disconnected";
		}else {
			return "Reader is already Disconnected !!!!!!!!!!!!!!";
		}
		
	}
	
	
	public void saveTagsCheckInOut() {
		jObj = new JSONObject();
		
		for(TagReadData tr : tagReads) {
			
			try {
				String epcString = tr.epcString();
				String emailId = email_Idd;
				int scanType = checkInOut;
				String ip_Address = ipAdd;
				int companyId = Integer.parseInt(company_Id);
				String portNo = port;
				int device_id = Integer.parseInt(device_Id);
	            int user_id = Integer.parseInt(user_Id);
	            String macAddress = mac_Add;
	            LocalDateTime now = LocalDateTime.now();  
	            jObj.put("EpcString", epcString);
		            jObj.put("Time", now);
		            jObj.put("Email_Id", emailId);
		            jObj.put("Code", "");
		            jObj.put("ScanType", checkInOut);
		            jObj.put("CompanyId", companyId);
		            jObj.put("Ip_Address", ip_Address);
		            jObj.put("device_id", device_id);
		            jObj.put("user_id", user_id);
		            jObj.put("macaddress", macAddress);
		            jObj.put("Port", portNo);
		            jObj.put("Tag_Type","RFID");
		            
		            
		            
		            URL url = new URL(apiUrl + "/api/SaveTags");
                System.out.println("URL " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                conn.setDoOutput(true);
                
                try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jObj.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
                System.out.println("Data saved...............................");
                System.out.println("Response Message:" + conn.getResponseMessage());
                System.out.println("Response Code:" + conn.getResponseCode());
                System.out.println("jObj" + jObj);
            }
                
                
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveTagsFirstTime() {
		jObj = new JSONObject();
       // l1 = new LinkedList<>();
        for (TagReadData p : tagReads) {
            try {
           // l1.add(p.epcString());
            String epcString = p.epcString();
            //long Time = p.getTime();
            String email_Id = email_Idd;
            String code = "";
            int scanType = checkInOut;
            String ip_Address = ipAdd;
            int companyId = Integer.parseInt(company_Id);
            String portt = port;
            int device_id = Integer.parseInt(device_Id);
            int user_id = Integer.parseInt(user_Id);
            LocalDateTime now = LocalDateTime.now();  
            String macAddress = mac_Add;

            jObj1.put("EpcString", epcString);
            jObj1.put("Time", now);
            jObj1.put("Email_Id", email_Id);
            jObj1.put("Code", "");
            jObj1.put("ScanType", scanType);
            jObj1.put("Ip_Address", ip_Address);
            jObj1.put("CompanyId", companyId);
            jObj1.put("Port", port);
            jObj1.put("device_id", device_id);
             //jObj1.put("macaddress", "0A-00-27-00-00-05");
            jObj1.put("user_id", user_id);
            // jObj1.put("Tag_Type","RFID");

            
                URL url = new URL(apiUrl + "/api/SaveTags");
                System.out.println("URL " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jObj.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                    System.out.println("Data saved...............................");
                    System.out.println("Response Message:" + conn.getResponseMessage());
                    System.out.println("Response Code:" + conn.getResponseCode());
                    System.out.println("jObj" + jObj1);
                }
            } catch(Exception e) {
				e.printStackTrace();
			}
            }
    }
	
	
	 public void saveAddedOrRemovedTags() throws  IOException {
		 ++listCounter;
		 String commaSeparated = "";
		 if(listCounter==1){
		     
		 }else{
		     System.out.println("elseElse<><>??????????::::::::::::::::"+listCounter);
		     l1 = new LinkedList<>();
		 }
		         String addedTag = "";
		         String removedTag = "";
		         int device_id;
		 l2 = new LinkedList<>();
		 removed = new LinkedList<String>();
		 added = new LinkedList<String>();
		        
		         
		         System.out.println(tagReads.length);
		         saveCounter=0;
		         for (TagReadData p : tagReads) {
		             
		        	 
		        	 try {
			        	 
			        	 saveCounter++;
			             jObj1 = new JSONObject();
			             if(listCounter==1){
			                  l2.add(p.epcString());
			                  added.add(p.epcString());
			                 
			  
			             }else{
			                  l1.add(p.epcString());
			             }
			            
			             //l2.add(p.epcString());

			             if (listCounter == 1) {

			             } else {
			                 for (String item : l1) {
			                     if (l2.contains(item)) {

			                     } else {
			                         removed.add(item);
			                         //jObj1.put("removedTag", removed);
			                     }
			                 }
			                 System.out.println("Removed " + removed);

			                 for (String item : l2) {
			                     if (l1.contains(item)) {

			                     } else {
			                         added.add(item);
			                         //jObj1.put("addedTag", added);
			                     }
			                 }
			             }
			             
			             System.out.println("Added " + added);

			             String Ip_Address = ipAdd;

			             device_id = Integer.parseInt(device_Id);

			             // int user_id = Integer.parseInt(Login.UserID);
			            
			             
			             System.out.println("TagReads:"+tagReads.length);
			             System.out.println("SaveCounter:"+saveCounter);
			             
			             if(tagReads.length==saveCounter){
			                 String[] arr = added.toArray(new String[0]); 
			                 System.out.println("Arrr:"+arr);
			                 String[] arrr = removed.toArray(new String[0]);
			                 
			                 
			                 
			                 String result = "";
			                 String removedResult = "";
			                 System.out.println("ARrr Length:"+arr.length);
			                 if (arr.length > 0) { 
			                     StringBuilder sb = new StringBuilder();
			                     for (String s : arr) { 
			                         sb.append(s).append(",");
			                     } 
			                     result = sb.deleteCharAt(sb.length() - 1).toString(); 
			                 }
			                 if (arrr.length > 0) { 
			                     StringBuilder sb1 = new StringBuilder();
			                     for (String s : arrr) { 
			                         sb1.append(s).append(",");
			                     } 
			                     removedResult = sb1.deleteCharAt(sb1.length() - 1).toString(); 
			                 }


			                 System.out.println("Final String is "+result);
			                 LocalDateTime now = LocalDateTime.now();  
			                 jObj1.put("removedTag", removedResult);
			             jObj1.put("addedTag", result);
			             jObj1.put("timeStamp", now);
			             jObj1.put("ipaddress", Ip_Address);
			             jObj1.put("deviceid", device_id);
			             
			             
			                 URL url = new URL(apiUrl + "/api/SaveChangedTags");
			                 System.out.println("URL " + url);
			                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			                 conn.setRequestMethod("POST");
			                 conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			                 conn.setRequestProperty("Accept", "application/json");

			                 conn.setDoOutput(true);

			                 try (OutputStream os = conn.getOutputStream()) {
			                     byte[] input = jObj1.toString().getBytes("utf-8");
			                     os.write(input, 0, input.length);
			                     System.out.println("Data saved...............................");
			                     System.out.println("Response Message:" + conn.getResponseMessage());
			                     System.out.println("Response Code:" + conn.getResponseCode());
			                     System.out.println("jObj" + jObj1);
			                     if(listCounter==1){
			                         l1 = new LinkedList<>();
			                     }else{
			                        l1.clear(); 
			                     }
			                     
			                     l1.addAll(l2);
			                     l2.clear();
			                 }
			             
			             }
			             		        		 
		        	 }catch(Exception e) {
		        		 
		        	 }
		        	 


		         }
		     }
	
	
	
		
	
	
	}


