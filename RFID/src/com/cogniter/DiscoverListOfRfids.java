package com.cogniter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.apple.dnssd.BrowseListener;
import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;

public class DiscoverListOfRfids implements BrowseListener {
	// private DNSSDService service = null;
	static String ipAdd;
	static String readerName;
	static String fullRFIDReaderDetail;
	int flags;
	int ifIndex;
	String serviceName;
	String regType;
	String domain;
	List<String> list;

	private DNSSDService browser = null;
	// private DNSSDService resolver = null;
	BrowseListener browr;
	String serviceType = "_llrp._tcp";
	InetAddress theAddress;
	List<String> servicesList = new ArrayList<String>();
	
	public List<String> discov() {
		try {
			list = new ArrayList<>();
			browser = DNSSD.browse(serviceType, this);
			Thread.sleep(2000);
			DNSSD.resolve(flags, ifIndex, serviceName, regType, domain, new ResolveListener() {

				@Override
				public void operationFailed(DNSSDService arg0, int arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void serviceResolved(DNSSDService arg0, int arg1, int arg2, String arg3, String arg4, int arg5,
						TXTRecord arg6) {
						//System.out.println("??"+arg0);
						//System.out.println("}}"+arg1);
						//System.out.println(">>"+arg2);
						//System.out.println(",,"+arg3);
						System.out.println("*"+arg4);
						//System.out.println("&"+arg5);
						//System.out.println("#"+arg6.getValueAsString(arg1));
					/*
					 * System.out.println("Flags :" + flags); System.out.println("ifIndex :" +
					 * ifIndex); System.out.println("serviceName :" + serviceName);
					 * System.out.println("regType :" + regType); System.out.println("Domain :" +
					 * domain);
					 */

					try {
						theAddress = InetAddress.getByName(arg4);
						// System.out.println("IpAddress:"+theAddress.toString().substring(theAddress.toString().lastIndexOf("/")+1));
						ipAdd = theAddress.toString().substring(theAddress.toString().lastIndexOf("/") + 1);
						System.out.println("Discover Final Reader is "+arg4+" on Ip Address  " + ipAdd+" at "+arg5);
						ipAdd = arg4+" on "+ipAdd+" at "+arg5;
						list.add(ipAdd);
						list.add("Sargas-1 on 192.168.0.53 at "+arg5);
						list.add("Sargas-2 on 192.168.0.54 at "+arg5);
						list.add("Sargas-3 on 192.168.0.55 at "+arg5);
						list.add("Sargas-4 on 192.168.0.56 at "+arg5);
					} catch (UnknownHostException e) {
						// ouch..
						e.printStackTrace();
					}

				}
			});

			// }
			//System.out.println("dsfds" + browser.toString());

		} catch (Exception e) {
			e.printStackTrace();
			//return "Exception in founding Rfid Reader";
			
		}

		return list;
	}

	@Override
	public void operationFailed(DNSSDService arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serviceFound(DNSSDService browser, int arg1, int arg2, String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub
		
		/*
		 * System.out.println("service found" + browser); System.out.println(">" +
		 * arg1); System.out.println(">>" + arg2); System.out.println(">>>" + arg3);
		 * 
		 * System.out.println(">>>>" + arg4); System.out.println(">>>>>" + arg5);
		 * 
		 */
		flags = arg1;
		ifIndex = arg2;
		serviceName = arg3;
		regType = arg4;
		domain = arg5;

		readerName = arg3;

	}

	@Override
	public void serviceLost(DNSSDService arg0, int arg1, int arg2, String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		DiscoverListOfRfids dis = new DiscoverListOfRfids();
		dis.discov();
	}

}
