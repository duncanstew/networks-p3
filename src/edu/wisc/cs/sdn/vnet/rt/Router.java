package edu.wisc.cs.sdn.vnet.rt;

import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;

import net.floodlightcontroller.packet.Ethernet;
<<<<<<< HEAD
import net.floodlightcontroller.packet.IPv4;
=======
import net.floodlightcontroller.packet.*;
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f

/**
 * @author Aaron Gember-Jacobson and Anubhavnidhi Abhashkumar
 */
public class Router extends Device
{	
	/** Routing table for the router */
	private RouteTable routeTable;

	/** ARP cache for the router */
	private ArpCache arpCache;

	/**
	 * Creates a router for a specific host.
	 * @param host hostname for the router
	 */
	public Router(String host, DumpFile logfile)
	{
		super(host,logfile);
		this.routeTable = new RouteTable();
		this.arpCache = new ArpCache();
	}

	/**
	 * @return routing table for the router
	 */
	public RouteTable getRouteTable()
	{ return this.routeTable; }

	/**
	 * Load a new routing table from a file.
	 * @param routeTableFile the name of the file containing the routing table
	 */
	public void loadRouteTable(String routeTableFile)
	{
		if (!routeTable.load(routeTableFile, this))
		{
			System.err.println("Error setting up routing table from file "
					+ routeTableFile);
			System.exit(1);
		}

<<<<<<< HEAD
		System.out.println("Loaded static route tabsdle!");
=======
		System.out.println("Loaded static route table");
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
		System.out.println("-------------------------------------------------");
		System.out.print(this.routeTable.toString());
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Load a new ARP cache from a file.
	 * @param arpCacheFile the name of the file containing the ARP cache
	 */
	public void loadArpCache(String arpCacheFile)
	{
		if (!arpCache.load(arpCacheFile))
		{
			System.err.println("Error setting up ARP cache from file "
					+ arpCacheFile);
			System.exit(1);
		}

		System.out.println("Loaded static ARP cache");
		System.out.println("----------------------------------");
		System.out.print(this.arpCache.toString());
		System.out.println("----------------------------------");
	}

	/**
	 * Handle an Ethernet packet received on a specific interface.
	 * @param etherPacket the Ethernet packet that was received
	 * @param inIface the interface on which the packet was received
	 */
	public void handlePacket(Ethernet etherPacket, Iface inIface)
	{
		System.out.println("*** -> Received packet: " +
				etherPacket.toString().replace("\n", "\n\t"));

		/********************************************************************/
		/* TODO: Handle packets                                             */

		switch(etherPacket.getEtherType())
		{
		case Ethernet.TYPE_IPv4:
			this.handleIpPacket(etherPacket, inIface);
			break;
		// Ignore all other packet types, for now
		}

		/********************************************************************/
	}
<<<<<<< HEAD

	private void handleIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
		if (etherPacket.getEtherType() != Ethernet.TYPE_IPv4)
		{ return; }

		// Get IP header
		IPv4 ipPacket = (IPv4)etherPacket.getPayload();
=======
	
	private void handleIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
		System.out.println("HandleIPPacket");
		if (etherPacket.getEtherType() != Ethernet.TYPE_IPv4)
		{ 	
			System.out.println("Packet is not IPv4, drop the packet");
			return; 
		}

		// Get IP header
		IPv4 ipPacket = (IPv4) etherPacket.getPayload();
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
		System.out.println("Handle IP packet");

		// Verify checksum
		short origCksum = ipPacket.getChecksum();
		ipPacket.resetChecksum();
		byte[] serialized = ipPacket.serialize();
		ipPacket.deserialize(serialized, 0, serialized.length);
		short calcCksum = ipPacket.getChecksum();
		if (origCksum != calcCksum)
<<<<<<< HEAD
		{ return; }
=======
		{ 	System.out.println("The checksum is incorrect, drop the packet");
			return; }
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f

		// Check TTL
		ipPacket.setTtl((byte)(ipPacket.getTtl()-1));
		if (0 == ipPacket.getTtl())
		{ 
<<<<<<< HEAD
			
			// 2.1 Time exceeded
			
			//TODO here ttl exceeded, should send ICMP code before
			//dropping packet
			
			// TODO this code is given in the assignment description
			// When the router generates ICMP messages for time exceeded, the packet must 
			// contain an Ethernet header, IPv4 header, ICMP header, and ICMP payload	

			Ethernet ether = new Ethernet();
			IPv4 ip = new IPv4();
			ICMP icmp = new ICMP();
			Data data = new Data();
			ether.setPayload(icmp);
			icmp.setPayload(data);		
			
			//EtherType: set to Ethernet.TYPE_IPV4
			ether.setEtherType(Ethernet.TYPE_IPV4);
=======
			System.out.println("TTL is 0, drop the packet.");
			// 2.1 Time exceeded
			
						
			// TODO this code is given in the assignment description
			// When the router generates ICMP messages for time exceeded, the packet must 
			// contain an Ethernet header, IPv4 header, ICMP header, and ICMP payload	
			//Ethernet ether = new Ethernet();
			//IPv4 ip = new IPv4();
			//ICMP icmp = new ICMP();
			//Data data = new Data();
			//ether.setPayload(icmp);
			//icmp.setPayload(data);		
			
			//EtherType: set to Ethernet.TYPE_IPV4
			//ether.setEtherType(Ethernet.TYPE_IPV4);
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f

			// Source MAC: set to the MAC address of the out interface obtained by 
			// performing a lookup in the route table (invariably this will be the 
			// interface on which the original packet arrived)
			//TODO
<<<<<<< HEAD
=======
			//
			sendICMP(etherPacket, inIface, (byte)11, (byte)0);
			return;
			/**
			int srcAddr = ipPacket.getSourceAddress();
			RouteEntry src = this.routeTable.lookup(srcAddr);
			int n1 = src.getGatewayAddress();
			ArpEntry arpSrc = this.arpCache.lookup(n1);
			ether.setSourceMACAddress(arpSrc.getMac());
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f


			// Destination MAC: set to the MAC address of the next hop, determined by 
			// performing a lookup in the route table followed by a lookup in the ARP cache
			// TODO
<<<<<<< HEAD
			
			// IP 
			ip.setTtl(64);
			ip.setProtocol(IPv4.PROTOCOL_ICMP);


			// Source IP: set to the IP address of the interface on which the original packet arrived
			// TODO

			// Destination IP — set to the source IP of the original packet
			// TODO
			
			// ICMP
			icmp.setIcmpType(11);
			icmp.setIcmpCode(11);

			// set the payload 
			// TODO
			byte [] bytes = {};
			data.setData(bytes);

			
			// send to interface obtained by longest prefix match for source IP	
			// TODO


			return;
	       
=======
			int destAddr = ipPacket.getDestinationAddress();
			RouteEntry dest = this.routeTable.lookup(destAddr);
			int n2 = dest.getGatewayAddress();
			ArpEntry arpDest = this.arpCache.lookup(n2);
			ether.setDestinationMACAddress(arpDest.getMac());
			return;
	      		*/ 
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
	
		}

		// Reset checksum now that TTL is decremented
		ipPacket.resetChecksum();
<<<<<<< HEAD
=======
		System.out.println("TTL decremented");
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f

		// Check if packet is destined for one of router's interfaces
		for (Iface iface : this.interfaces.values())
		{
			if (ipPacket.getDestinationAddress() == iface.getIpAddress())
			{ return; }
		}

		// Do route lookup and forward
<<<<<<< HEAD
		this.forwardIpPacket(etherPacket, inIface);
	}

	private void forwardIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
=======
		System.out.println("forwardingpacket");
		this.forwardIpPacket(etherPacket, inIface);
	}

	private void sendICMP(Ethernet etherPacket, Iface inIface, byte type, byte code)
	{
		System.out.println("ICMPMessage function call");
		Ethernet ether = new Ethernet();
		IPv4 ip = new IPv4();
		ICMP icmp = new ICMP();
		IPv4 packet = (IPv4) etherPacket.getPayload();




		int srcAddr = packet.getSourceAddress();
		RouteEntry closeMatch = this.routeTable.lookup(srcAddr);
		
		if(closeMatch == null){
			System.out.println("no match found, icmp");
			return;
		}

		// TODO gateway?
		int hop = closeMatch.getGatewayAddress();
		if(hop == 0){
			hop = srcAddr;			
		}

		ArpEntry arpSrc = this.arpCache.lookup(hop);

		if(arpSrc == null){
			System.out.println("icmp miss");
			//handle arp miss here {args: etherPacket, hop, inIface);
		}


		//TODO check toBytes is correct
		ether.setDestinationMACAddress(arpSrc.getMac().toBytes());
		ether.setSourceMACAddress(inIface.getMacAddress().toBytes());
		ether.setEtherType(Ethernet.TYPE_IPv4);


		int destAddr = packet.getDestinationAddress();
		RouteEntry dest = this.routeTable.lookup(destAddr);
		int n2 = dest.getGatewayAddress();
		ArpEntry arpDest = this.arpCache.lookup(n2);

		if(arpDest == null){
			System.out.println("arp miss");
		}

		
		//ether.setSourceMACAddress(inIface.getMacAddress().toBytes());
		//ether.setDestinationMACAddress(etherPacket.getSourceMACAddress());

		ip.setTtl((byte)64);
		ip.setProtocol(IPv4.PROTOCOL_ICMP);
		ip.setDestinationAddress(packet.getSourceAddress());
		//ip.setSourceAddress(inIface.getIpAddress());
		//System.out.println(inIface.getIpAddress());

		icmp.setIcmpType(type);
		icmp.setIcmpCode(code);

		Data data = new Data();

		if(type!=0){
			ip.setSourceAddress(inIface.getIpAddress());
			int packetLength = packet.getHeaderLength() * 4;
			byte[] ICMPPayload = new byte[12 + packetLength];
			byte[] packetSerialize = packet.serialize();
			// TODO does this include the IPv4 Header?
			for(int i = 0; i < (packetLength + 8);i++){
				ICMPPayload[i+4]=packetSerialize[i];
			}
			data.setData(ICMPPayload);
		}
		else{
			// TODO does this include the IPv4 header that needs to also be added
			// or the 4 bytes of padding?
			ICMP icmpPacket = (ICMP)packet.getPayload();
			// TODO i think the Source IP is supposed to be this devices ip?
			// not necessarily the ip of where it was supposed to go
			// because it likely didn't reach there or the packet wouldnt
			// be dropped
			ip.setSourceAddress(packet.getDestinationAddress());
			data.setData(icmpPacket.getPayload().serialize());
		}

		ether.setPayload(ip);
		ip.setPayload(icmp);
		icmp.setPayload(data);

		// TODO description says send it to the  interface obtained from the longest
		// prefix match in the route table for the original source IP, i think this
		// can be done by using the forwardIpPacket function
		this.forwardIpPacket(ether, inIface);
		// this.sendPacket(ether, inIface);
	
	}

	private void forwardIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
		System.out.println("forwardingIpPacket function call");
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
		if (etherPacket.getEtherType() != Ethernet.TYPE_IPv4)
		{ return; }
		System.out.println("Forward IP packet");

		// Get IP header
		IPv4 ipPacket = (IPv4)etherPacket.getPayload();
		int dstAddr = ipPacket.getDestinationAddress();

		// Find matching route table entry 
		RouteEntry bestMatch = this.routeTable.lookup(dstAddr);

		// If no entry matched, do nothing
		if (null == bestMatch)
		{ 
			// 2.2 Destination network unreachable
<<<<<<< HEAD
			// TODO	
=======
			System.out.println("Destination network unreachable");
			sendICMP(etherPacket, inIface, (byte)3, (byte)0);	
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
			return; 
		}

		// Make sure we don't sent a packet back out the interface it came in
		Iface outIface = bestMatch.getInterface();
		if (outIface == inIface)
<<<<<<< HEAD
		{ return; }
=======
		{ 	System.out.println("Sending packet back to interface from which it came");
			return; }
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f

		// Set source MAC address in Ethernet header
		etherPacket.setSourceMACAddress(outIface.getMacAddress().toBytes());

		// If no gateway, then nextHop is IP destination
		int nextHop = bestMatch.getGatewayAddress();
		if (0 == nextHop)
		{ nextHop = dstAddr; }

		// Set destination MAC address in Ethernet header
		ArpEntry arpEntry = this.arpCache.lookup(nextHop);
		if (null == arpEntry)
		{ 
			// 2.3 Destination host unreachable
			// TODO
<<<<<<< HEAD



			return; 
		}
		etherPacket.setDestinationMACAddress(arpEntry.getMac().toBytes());

=======
			System.out.println("sending arp requests");
			return; 
		}
		etherPacket.setDestinationMACAddress(arpEntry.getMac().toBytes());
		System.out.println("sendPacket from forwardIPpacket");
>>>>>>> 61170deedc5ba3d03e41c59e26fb5f7ec635ad4f
		this.sendPacket(etherPacket, outIface);
	}
}
