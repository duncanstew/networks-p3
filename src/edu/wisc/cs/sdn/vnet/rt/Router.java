package edu.wisc.cs.sdn.vnet.rt;

import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;

import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;

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

		System.out.println("Loaded static route table");
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
	
	private void handleIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
		if (etherPacket.getEtherType() != Ethernet.TYPE_IPv4)
		{ 	
			System.out.println("Packet is not IPv4, drop the packet");
			return; 
		}

		// Get IP header
		IPv4 ipPacket = (IPv4) etherPacket.getPayload();
		System.out.println("Handle IP packet");

		// Verify checksum
		short origCksum = ipPacket.ge:tChecksum();
		ipPacket.resetChecksum();
		byte[] serialized = ipPacket.serialize();
		ipPacket.deserialize(serialized, 0, serialized.length);
		short calcCksum = ipPacket.getChecksum();
		if (origCksum != calcCksum)
		{ 	System.out.println("The checksum is incorrect, drop the packet");
			return; }

		// Check TTL
		ipPacket.setTtl((byte)(ipPacket.getTtl()-1));
		if (0 == ipPacket.getTtl())
		{ 
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

			// Source MAC: set to the MAC address of the out interface obtained by 
			// performing a lookup in the route table (invariably this will be the 
			// interface on which the original packet arrived)
			//TODO
			//
			IcmpMessage(etherPacket, inIface, (byte)11, (byte)0);
			return;
			/**
			int srcAddr = ipPacket.getSourceAddress();
			RouteEntry src = this.routeTable.lookup(srcAddr);
			int n1 = src.getGatewayAddress();
			ArpEntry arpSrc = this.arpCache.lookup(n1);
			ether.setSourceMACAddress(arpSrc.getMac());


			// Destination MAC: set to the MAC address of the next hop, determined by 
			// performing a lookup in the route table followed by a lookup in the ARP cache
			// TODO
			int destAddr = ipPacket.getDestinationAddress();
			RouteEntry dest = this.routeTable.lookup(destAddr);
			int n2 = dest.getGatewayAddress();
			ArpEntry arpDest = this.arpCache.lookup(n2);
			ether.setDestinationMACAddress(arpDest.getMac());
			return;
	      		*/ 
	
		}

		// Reset checksum now that TTL is decremented
		ipPacket.resetChecksum();

		// Check if packet is destined for one of router's interfaces
		for (Iface iface : this.interfaces.values())
		{
			if (ipPacket.getDestinationAddress() == iface.getIpAddress())
			{ return; }
		}

		// Do route lookup and forward
		this.forwardIpPacket(etherPacket, inIface);
	}

	private void IcmpMessage(Ethernet etherPacket, Iface inIface, byte type, byte code){
		IPv4 packet = (IPv4) etherPacket.getPayload();
		Ethernet ether = new Ethernet();
		ether.setEtherType(Ethernet.TYPE_IPv4);
		ether.setSourceMACAddress(inIface.getMacAddress().toBytes());
		ether.setDestinationMACAddress(etherPacket.getSourceMACAddress());

		IPv4 ip = new IPv4();
		ip.setTtl((byte)64);
		ip.setProtocol(IPv4.PROTOCOL_ICMP);
		ip.setSourceAddress(inIface.getIpAddress());
		ip.setDestinationAddress(packet.getSourceAddress());

		ICMP icmp = new ICMP();
		icmp.setIcmpType(type);
		icmp.setIcmpCode(code);

		Data data = new Data();

		if(type!=0){
			int packetLength = packet.getHeaderLength() * 4;
			byte[] ICMPPayload = new byte[12 + packetLength];
			byte[] packetSerialize = packet.serialize();

			for(int i = 0; i < (packetLength + 8);i++){
				ICMPPayload[i+4]=packetSerialize[i];
			}
			data.setData(ICMPPayload);
		}
		else{
			ICMP icmpPacket = (ICMP)packet.getPayload();
			ip.setSourceAddress(packet.getDestinationAddress());
			data.setData(icmpPacket.getPayload().serialize());
		}

		ether.setPayload(ip);
		ip.setPayload(icmp);
		icmp.setPayload(data);

		this.sendPacket(ether, inIface);
	
	}

	private void forwardIpPacket(Ethernet etherPacket, Iface inIface)
	{
		// Make sure it's an IP packet
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
			// TODO	
			return; 
		}

		// Make sure we don't sent a packet back out the interface it came in
		Iface outIface = bestMatch.getInterface();
		if (outIface == inIface)
		{ 	System.out.println("Sending packet back to interface from which it came");
			return; }

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



			return; 
		}
		etherPacket.setDestinationMACAddress(arpEntry.getMac().toBytes());

		this.sendPacket(etherPacket, outIface);
	}
}
