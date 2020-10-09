package com.cogniter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetReaderIp {
	
	public String getIp() {
//		String command = "avahi-browse -d local _llrp._tcp --resolve -t";
//		String comm = "ipconfig";
//		String search = "address";
//		String found="";
//		String finalIP="";
//		 
//		try {
//		    Process process = Runtime.getRuntime().exec(command);
//		 
//		    BufferedReader reader = new BufferedReader(
//		            new InputStreamReader(process.getInputStream()));
//		    String line;
//		    while ((line = reader.readLine()) != null) {
//		        System.out.println(line);
//		        if(line.contains("address = [1")) {
//		        	found = line;
//		        	
//		        }
//		    }
//		    
//		    System.out.println("Found is > "+found);
//		    
//		    finalIP = found.toString().substring(found.toString().lastIndexOf("[") + 1);
//			System.out.println("Discover Final Ip Address is " + finalIP);
//			finalIP = finalIP.replace(']', ' ');
//			System.out.println("??????????????????????? "+finalIP);
//		 
//		    reader.close();
//		 
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//		
//		return finalIP;
		
		
		String command = "avahi-browse -d local _llrp._tcp --resolve -t";
		String foundIp="";
		
			try{
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while((line = reader.readLine())!=null){
				System.out.println(line);
				if(line.contains("address")){
					String val = line.substring(line.indexOf('['));
					try{
						int i = Integer.parseInt(val.substring(val.indexOf('[')+1,val.indexOf('[')+2));
						foundIp = val.substring(val.indexOf('[')+1,val.lastIndexOf(']'));
						}catch(Exception e){
							System.out.println(e);
							}
					}
				
				
				}
				System.out.println("IP is "+foundIp);
			
			}catch(Exception e){
			e.printStackTrace();
			
			
			}
			
			return foundIp;
	}

}
