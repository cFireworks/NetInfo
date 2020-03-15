package com.traffic.jpcap;

import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class JpcapTipDemo implements PacketReceiver {

	@Override
	public void receivePacket(Packet packet) {//每个Packet表示从指定网络上抓取到的数据包
		System.out.println("**********************************");
		//IP数据包报文头
		byte[] ipHead = packet.header;
		for(int t = 0; t < 21; t++) {
			System.out.print(ipHead[t]);
		}
		System.out.println();
		String string = "";
		System.out.print("报头文");
		for(int i = 0; i < ipHead.length; i++) {
			int m = 0;
			m = ipHead[i];
			m = m << 24;
			m = m >>> 24;
			string = string + Integer.toHexString(m);
			System.out.print(ipHead[i]);
		}
		System.out.println();
		System.out.println(string);
		int d = ipHead.length;
		System.out.println("首部长度 ：" + (d * 8) + "bit");
		System.out.println();
		//分析源IP地址和目的IP地址 分析协议类型
		if(packet.getClass().equals(IPPacket.class)) {
			IPPacket ipPacket = (IPPacket) packet;
			byte[] iph = ipPacket.option;
			String iphstr = new String(iph);
			System.out.println(iphstr);
		}
		if(packet.getClass().equals(ARPPacket.class)) {
			System.out.println("协议类型为： ARP协议");
			try {
				ARPPacket arpPacket = (ARPPacket) packet;
				System.out.println("源网卡MAC地址为：" + arpPacket.getSenderHardwareAddress());
				System.out.println("源IP地址为：" + arpPacket.getSenderProtocolAddress());
				System.out.println("目的网卡MAC地址为：" + arpPacket.getTargetHardwareAddress());
				System.out.println("目的IP地址为：" + arpPacket.getTargetProtocolAddress());
			} catch (Exception e) {
				 
			}
			
		} else if(packet.getClass().equals(UDPPacket.class)) {
			System.out.println("协议类型： UDP协议");
			UDPPacket udpPacket = (UDPPacket) packet;
			System.out.println("源IP地址为：" + udpPacket.src_ip);
			int tport = udpPacket.src_port;
			System.out.println("源端口号为：" + tport);
			System.out.println("目的IP地址为：" + udpPacket.dst_ip);
			System.out.println("目的端口号为：" + udpPacket.dst_port);
		} else if(packet.getClass().equals(TCPPacket.class)) {
			System.out.println("协议类型为： TCP协议");
			
			TCPPacket tcpPacket = (TCPPacket) packet;
			int tport = tcpPacket.src_port;
			System.out.println("源IP地址为：" + tcpPacket.src_ip);
			System.out.println("源IP端口号为：" + tcpPacket.src_port);
			System.out.println("目的IP地址为：" + tcpPacket.dst_ip);
			System.out.println("目的端口号为：" + tcpPacket.dst_port);
		} else if(packet.getClass().equals(ICMPPacket.class)){
			System.out.println("协议类型为： ICMP协议");
		} else {
			System.out.println("协议类型为：GGP/EGP/JGP协议或ospf协议或ISO的第4类运输协议TP4");
		}
		//IP数据报文数据
		byte[] k = packet.data;
		String str = "";
		System.out.println("数据：");
		for(int i = 0; i < k.length; i++) {
			int m = 0;
			m = k[i];
			m = m << 24;
			m = m >>> 24;
			str = str + Integer.toHexString(m);
			str = new String(k);
			System.out.print(k[i]);
		}
		System.out.println();
		System.out.println(str);
		System.out.println("数据报类型：" + packet.getClass());
		System.out.println();
	}
	
	
	
	
	
	
	
	
	

}
