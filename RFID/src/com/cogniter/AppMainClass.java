package com.cogniter;

import java.io.IOException;

import org.json.JSONException;

public class AppMainClass {
	
	
	public static void main(String[] args) throws InterruptedException, JSONException, IOException {
		
		
		String serialNumber = SystemMotherBoardNumber.getSystemMotherBoard_SerialNumber();
		System.out.println("Serial Number "+serialNumber);
		MacAdd macAdd = new MacAdd();
		String mac = macAdd.getMac();
		Connection conn = new Connection(serialNumber, mac);
		
	}

}
