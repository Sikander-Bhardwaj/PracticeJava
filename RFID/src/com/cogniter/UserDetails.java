package com.cogniter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



class UserDetails{
	Scanner sc;
	private String result;
	JSONObject json;
	private final String TAG_ROOT="ValidateUserJsonResult";
	private String email;
	boolean validEmail = true;
	private String password;
	private String ipAdd;
	URL url;
    HttpURLConnection connection = null;
	public static String URL = " http://api.talygen.com/Service/V1.1/talygenrestservice.svc/";
	
	String userId;
	String userEmail;
	String userName;
	String companyId;
	String userImgPath;
	  
	
	  
	public JSONObject UserDetailss() throws IOException, JSONException{
		JSONObject newJson = new JSONObject();
		 InetAddress localhost = InetAddress.getLocalHost(); 
	        System.out.println("System IP Address : " + 
	                      (localhost.getHostAddress()).trim());
	        ipAdd = localhost.getHostAddress().trim();
		sc = new Scanner(System.in);
		
		do {
		if(validEmail) {
			System.out.println("Enter email : ");
			email = sc.nextLine();
			validEmail = validateEmail(email);
			if(validEmail) {
				System.out.println("Enter password:");
				password = sc.nextLine();
				if(validEmail && email!=null && password!=null && password!=" " && !password.isEmpty()) {
					System.out.println("Success");
					String urlStr = URL + "json/login?email=" + email + "&password=" + password + "" + "&ipaddress=" + ipAdd + "&trackagent=Linux";
					System.out.println("URLStr : "+urlStr);
					 URL url = null;
					try {
						url = new URL(urlStr);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				              try {
								connection = (HttpURLConnection)url.openConnection();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				              
				              byte responseData[] = null;
				              int len = (int) connection.getContentLength();
				              if (len > 0) {
				                  responseData = new byte[len];
				                  DataInputStream dis;
				                  dis = new DataInputStream(connection.getInputStream());
				                  dis.readFully(responseData);
				                  result = new String(responseData, "UTF-8");
				                  dis.close();
				                  try {
									json = new JSONObject(new JSONTokener(result));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				                  JSONObject c = json.getJSONObject(TAG_ROOT);
				                  c.put("password", password);
				                  json = json.put(TAG_ROOT, c);
				                  result = json.toString();

				              }
				              try {


				                    json = new JSONObject(new JSONTokener(result));
				                    System.out.println("json  login    " + json);
				                    JSONObject j = json.getJSONObject(TAG_ROOT);
				                    
				                   
				                    if(json!=null) {
				                    	userId = (String)j.getString("UserId");
				                    	userName = j.getString("UserName");
				                    	userEmail = j.getString("Email");
				                    	userImgPath = j.getString("Avatar");
				                    	companyId = j.getString("CompanyId");
				                    	
				                    	 newJson.put("type", "RaspberryPi");
				                    	 newJson.put("userId", userId);
				                    	 newJson.put("name", userName);
				                    	 newJson.put("userimgPath", userImgPath);
				                    	 newJson.put("companyId", companyId);
				                    	 newJson.put("Email", userEmail);
				                    	 newJson.put("deviceName", "RaspberryPiCogniter");
				                    	 
				                    	 
				                    	
				                    	
				                    	//System.out.println(userId);
				                    	//User user = new User(userId, userName, userImgPath, companyId, userEmail);
				                    	//System.out.println(user);
				                    	
				                    	//SocketIO sock = new SocketIO(user);
				                    	
				                    	
				                    	
				                    	
				                    }

				                } catch (JSONException e1) {
				                    // TODO Auto-generated catch block
				                    e1.printStackTrace();
				                }
					
				}
			}
		}else {
			System.out.println("Your email is wrong. Enter email again : ");
			email = sc.nextLine();
			validEmail = validateEmail(email);
		}
			
		}while(!validEmail);
		
		
		
		
		return newJson;
		
	}
	
	boolean validateEmail(String email) {
		boolean result = false;
		
		 //String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		 String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                 "[a-zA-Z0-9_+&*-]+)*@" + 
                 "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                 "A-Z]{2,7}$"; 
		result = email.matches(regex);
		
		return result;
	}
}