package com.cogniter;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;



import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Connection {
	
	 Socket sock = null;
	  boolean connectionStatus;
	Connection(){
		
		
		   try{
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
		    
		    
		    if(connectionStatus == true) {
		    	System.out.println("Successfully Connected!!!!!!!!!!!!!!!");
		    }else {
		    	
		    }
		    
		    } catch(URISyntaxException e){
		      e.printStackTrace(); 
		    }
		
		
	}

}
