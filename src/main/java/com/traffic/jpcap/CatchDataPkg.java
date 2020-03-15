package com.traffic.jpcap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class CatchDataPkg implements PacketReceiver {

//	private static String src_mac;
	//初始化抓包网卡
	public static void init(NetworkInterface deviceName, String filterCond) {
		JpcapCaptor jpcap = null;
		try {
			jpcap = JpcapCaptor.openDevice(deviceName, 2000, true, 20);
			//过滤代码 可以是协议 端口 IP 组合
			//jpcap.setFilter("ip and tcp and (dst host 10.255.0.13 and dst port 12388)", true);
			if(filterCond != null && !"".equals(filterCond)) {
				jpcap.setFilter(filterCond, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(jpcap.getPacket());
		jpcap.loopPacket(20, new CatchDataPkg());
		//jpcap.processPacket(-1, new CatchDataPkg());//它支持超时以及非阻塞模式
		System.out.println(jpcap.getPacket());
	}
	@Override
	public void receivePacket(Packet packet) {
		//封装抓包获取数据
		Map<String, Object> dataMap = new HashMap();
		//分析协议类型
		StringBuffer sbInfo = new StringBuffer();
		if(packet instanceof IPPacket) {
			IPPacket ip = (IPPacket) packet;
			sbInfo.append("\n");
			sbInfo.append("协议类型： IP协议 ==> ");
			System.out.println("版本：IPv4");
			System.out.println("优先权：" + ip.priority);
			System.out.println("区分服务：最大的吞吐量： " + ip.t_flag);
			System.out.println("区分服务：最高的可靠性：" + ip.r_flag);
			System.out.println("长度：" + ip.length);
			System.out.println("标识：" + ip.ident);
			System.out.println("DF:Don't Fragment: " + ip.dont_frag);
			System.out.println("NF:Nore Fragment: " + ip.more_frag);
			System.out.println("片偏移：" + ip.offset);
			System.out.println("生存时间：" + ip.hop_limit);
		} 
		if(packet instanceof ARPPacket) {
			ARPPacket arpPacket = (ARPPacket) packet;
			sbInfo.append("\n");
			sbInfo.append("协议类型： ARP协议==> ");
			sbInfo.append("数据包长度：" + arpPacket.caplen);
			sbInfo.append("长度len：" + arpPacket.len);
			sbInfo.append("长度：" + arpPacket.hlen + " == " + arpPacket.plen + " ==");
			sbInfo.append("时间戳：" + arpPacket.sec);
			sbInfo.append("\n");
			sbInfo.append("源网卡Mac地址：" + arpPacket.getSenderHardwareAddress());
			sbInfo.append(" 源IP地址：" + arpPacket.getSenderProtocolAddress());
			sbInfo.append("\n");
			sbInfo.append("目的网卡Mac地址：" + arpPacket.getTargetHardwareAddress());
			sbInfo.append(" 目的IP地址：" + arpPacket.getTargetProtocolAddress());
		} else if(packet instanceof UDPPacket) {
			//TODO 可获取头部信息
			UDPPacket udpPacket = (UDPPacket) packet;
			sbInfo.append("\n");
			sbInfo.append("协议类型： UDP协议==> ");
			sbInfo.append("数据包长度" + udpPacket.caplen);
			sbInfo.append("长度len：" + udpPacket.len);
			sbInfo.append("长度length：" + udpPacket.length + " == ");
			sbInfo.append(" 时间戳" + udpPacket.sec);
			sbInfo.append("\n");
			sbInfo.append("源IP地址和端口：" + udpPacket.src_ip + ":" + udpPacket.src_port); 
			sbInfo.append("\n");
			sbInfo.append("目的IP地址和端口：	" + udpPacket.dst_ip + ":" + udpPacket.dst_port);
			sbInfo.append("\n");
			sbInfo.append("生存时间：" + udpPacket.hop_limit);
			sbInfo.append("版本信息：" + udpPacket.version);
			sbInfo.append("\n");
			EthernetPacket datalink = (EthernetPacket) udpPacket.datalink;
			//获取源mac地址
			sbInfo.append("源Mac地址为：" + getMacInfo(datalink.src_mac));
			sbInfo.append("\n");
			//获取目的Mac地址
			sbInfo.append("目标Mac地址为：" + getMacInfo(datalink.dst_mac));
			//sbInfo.append("\n");
		} else if(packet instanceof TCPPacket) {
			TCPPacket tcpPacket = (TCPPacket) packet;
			//TODO 可获取头部信息
			sbInfo.append("\n");
			sbInfo.append("协议类型： TCP协议 ==> ");
			sbInfo.append("数据包长度" + tcpPacket.caplen);
			sbInfo.append("长度len：" + tcpPacket.len);
			sbInfo.append("长度length：" + tcpPacket.length + " ==");
			sbInfo.append(" 时间戳" + tcpPacket.sec);
			sbInfo.append("\n");
			sbInfo.append("源IP地址和端口：" + tcpPacket.src_ip + ":" + tcpPacket.src_port); 
			sbInfo.append("\n");
			sbInfo.append("目的IP地址和端口：	" + tcpPacket.dst_ip + ":" + tcpPacket.dst_port);
			sbInfo.append("\n");
			sbInfo.append("sequence ：" + tcpPacket.sequence);
			sbInfo.append(" 	window ：" + tcpPacket.window);
			sbInfo.append(" 	ack_num ：" + tcpPacket.ack_num);
			sbInfo.append("\n");
			sbInfo.append("生存时间time：" + tcpPacket.hop_limit);
			sbInfo.append("版本信息version：" + tcpPacket.version);
			sbInfo.append("\n");
			EthernetPacket datalink = (EthernetPacket) tcpPacket.datalink;
			//获取源mac地址
			sbInfo.append("源Mac地址为：" + getMacInfo(datalink.src_mac));
			sbInfo.append("\n");
			//获取目的Mac地址
			sbInfo.append("目标Mac地址为：" + getMacInfo(datalink.dst_mac));
			sbInfo.append("\n");
			sbInfo.append("其他信息：" + tcpPacket.fin  + " ** " + tcpPacket.syn  + " ** " + tcpPacket.rst  + " ** " + tcpPacket.ack  + " ** " + tcpPacket.urg  + " ** ");
		} else if(packet instanceof ICMPPacket) {
			ICMPPacket icmpPacket = (ICMPPacket) packet;
			//TODO 可获取头部信息
			sbInfo.append("\n");
			sbInfo.append("协议类型： TCP协议 ==> ");
			sbInfo.append("数据包长度" + icmpPacket.caplen);
			sbInfo.append(" 时间戳" + icmpPacket.sec);
			sbInfo.append("\n");
			sbInfo.append("源IP地址：" + icmpPacket.src_ip ); 
			sbInfo.append("\n");
			sbInfo.append("目的IP地址：	" + icmpPacket.dst_ip );
			sbInfo.append("\n");
			sbInfo.append("生存时间time：" + icmpPacket.hop_limit);
			sbInfo.append("版本信息version：" + icmpPacket.version);
			sbInfo.append("\n");
		}
		System.out.println(sbInfo);
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
		
	}
	/**
	 * 获取Mac信息
	 * @param macByte
	 * @return
	 * @Author Huangxiaocong 2019年3月24日 下午3:19:30
	 */
	protected String getMacInfo(byte[] macByte) {
        StringBuffer srcMacStr = new StringBuffer(); 
        int count = 1;
        for (byte b : macByte) {  
            srcMacStr.append(Integer.toHexString(b & 0xff));
            if(count++ != macByte.length) 
                srcMacStr.append(":");
        }
		return srcMacStr.toString();
	}
	
	/**
	 * 报文头
	 * @param packet
	 * @param dataMap
	 * @Author Huangxiaocong 2019年3月24日 下午4:02:29
	 */
	public void readHead(Packet packet, Map<String, Object> dataMap){  
        byte[] l = packet.header;  
        int d = l.length; 
        dataMap.put("headLen", (d * 8)); //首部长度
	} 
	
	/**
	 * 报文头数据
	 * @param packet
	 * @param dataMap
	 * @Author Huangxiaocong 2019年3月24日 下午4:03:15
	 */
	public void readData(Packet packet, Map<String, Object> dataMap){  
	    byte[] k = packet.data;  
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < k.length; i++) {  
	        try {  
	            sb.append(new String(k, "utf-8"));  
	        } catch (UnsupportedEncodingException e) {}  
	    }
	    String s = " " + packet.getClass();
	    dataMap.put("messageType", s.substring(s.lastIndexOf(".") + 1)); //报文类型
	}

	
	
	
	
	
	
}
