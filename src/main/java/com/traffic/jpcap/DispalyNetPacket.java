package com.traffic.jpcap;

import java.io.UnsupportedEncodingException;

import com.traffic.winInfo.LocalWinInfoUtils;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

/**
 * 本机ip：//10.0.166.232
 * @Description 获取设备的一些信息 	使用jpcap显示网络上的各种数据包 
 * 采用线程的方式持续的获取包信息
 * 2019年3月24日  上午11:07:01
 * @Author Huangxiaocong
 */
public class DispalyNetPacket {
	
	public static void main(String[] args) {
		final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		for(int i = 0; i < devices.length; i++) {
			System.out.println("Device "+ i);
			NetworkInterface nc = devices[i];
			/**
			 * 获取设备的一些信息
			 */
			StringBuffer sbinfo = new StringBuffer();
			sbinfo.append("设备" + i + "信息 ===");
			sbinfo.append("\n");
			String ipv4 = "";
			for(int j = 0; j < nc.addresses.length; j++) {
				if(j == 0) sbinfo.append("Ipv6地址：" + nc.addresses[j].address);
				else if(j == 1) {
					sbinfo.append("\n");
					ipv4 = nc.addresses[j].address.getHostAddress();
					System.out.println(ipv4);
					sbinfo.append("Ipv4地址：" + nc.addresses[j].address);
					sbinfo.append("\n");
					sbinfo.append("广播地址为：" + nc.addresses[j].broadcast);
					sbinfo.append(" 子网掩码为：" + nc.addresses[j].subnet);
				}
			}
			//创建某个卡口上的抓取对象，最大为2000个
			if(LocalWinInfoUtils.getIpAddress().equals(ipv4)) {
//				String condition = "dst host 192.168.1.1 and src host " + ipv4 + "";
				String condition = "dst host 192.168.1.1 ";
				//String condition = null;
				//服务器端作为dst 目标  客户端作为源src
				startCapThread(nc, condition);
			} else {
				sbinfo = new StringBuffer();
				continue;
			}
			sbinfo.append("\n");
			sbinfo.append("网卡名	：" + nc.name);
			sbinfo.append("\n");
			sbinfo.append("网卡描述： " + nc.description);
			sbinfo.append("\n");
			sbinfo.append(" 数据链路名称： " + nc.datalink_name);
			sbinfo.append(" 数据链路描述： " + nc.datalink_description);
			sbinfo.append("\n");
			
			byte[] bs = nc.mac_address;
			StringBuffer sbMac = new StringBuffer();
			int count = 1;
			for(byte b : bs) {
				sbMac.append(Integer.toHexString(b & 0xff));
				if(count++ != bs.length) sbMac.append(":");
			}
			sbinfo.append("Mac地址为：" + sbMac.toString());
			sbinfo.append("\n");
			System.out.println(sbinfo.toString());
			System.out.println();
		}
	}
	
	//将每个Captor放到独立显存中运行
	public static void startCapThread(NetworkInterface deviceName, String condition) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				//使用接包处理器循环抓包
				//jpcap.loopPacket(-1, new TestPacketReceiver());
				//jpcap.loopPacket(20, new CatchDataPkg());
				CatchDataPkg.init(deviceName, condition);
			}
		};
		new Thread(runnable).start();
	}
}
/**
 * @Description 抓包监听器 ,实现PackReceiver中的方法打印出数据包说明
 * 2019年3月24日  上午11:16:03
 * @Author Huangxiaocong
 */
class TestPacketReceiver implements PacketReceiver {

	@Override
	public void receivePacket(Packet packet) {
		//Tcp包，在java Socket中只能得到负载数据 有些包的协议是多种叠加在一起0
		StringBuffer sb ;
		if(packet instanceof TCPPacket) {
			TCPPacket tcpPacket = (TCPPacket) packet;
			sb = new StringBuffer();
			sb.append(" TCPPacket: --> 目的Ip地址为 ： " + tcpPacket.dst_ip.getHostAddress() + ":" + tcpPacket.dst_port);
			sb.append("== 源Ip地址为：" + tcpPacket.src_ip.getHostAddress() + ":" + tcpPacket.src_port);
			sb.append("== 大小为：" + tcpPacket.len);
			System.out.println(sb.toString());
		} else if(packet instanceof UDPPacket) {
			UDPPacket udpPacket = (UDPPacket) packet;
			sb = new StringBuffer();
			sb.append(" UDPPacket: --> 目的Ip地址为 ： " + udpPacket.dst_ip.getHostAddress() + ":" + udpPacket.dst_port);
			sb.append("== 源Ip地址为：" + udpPacket.src_ip.getHostAddress() + ":" + udpPacket.src_port);
			sb.append("== 大小为：" + udpPacket.len);
			System.out.println(sb.toString());
			//如果你要在程序中构造一个ping报文，就要构造ICMPPacket包
		} else if(packet instanceof ICMPPacket) {
			ICMPPacket icmpPacket = (ICMPPacket) packet;
			String router_ip = "";
			for(int i = 0; i < icmpPacket.length; i++) {
				router_ip += " " + icmpPacket.router_ip[i].getHostAddress();
			}
			sb = new StringBuffer();
			sb.append("@ @ @ ICMPPacket:| router_ip --> ： " + router_ip);
			sb.append("== redir_ip为：" + icmpPacket.redir_ip);
			sb.append("== mtu为：" + icmpPacket.mtu);
			sb.append("== 大小为：" + icmpPacket.len);
			System.out.println(sb.toString());
			//是否地址转换为协议请求包
		} else if(packet instanceof ARPPacket) {
			ARPPacket arpPacket = (ARPPacket) packet;
			sb = new StringBuffer();
			sb.append("***ARPPacket: --> SenderHardwareAddress 为 ： " + arpPacket.getSenderHardwareAddress());
			sb.append("== SenderProtocolAddress为：" + arpPacket.getSenderProtocolAddress());
			sb.append(" UDPPacket: --> 目的Ip地址为 ： " + arpPacket.PROTOTYPE_IP);
			sb.append("== 源Ip地址为：" + arpPacket.datalink);
			sb.append("== 大小为：" + arpPacket.len);
			System.out.println(sb.toString());
		} 
		//取得链路层数据头： 如果你想局域网抓包或伪造数据包，
		DatalinkPacket datalink = packet.datalink;
		//如果是以太网包：
		if(datalink instanceof EthernetPacket) {
			EthernetPacket ePacket = (EthernetPacket) datalink;
			sb = new StringBuffer();
			sb.append("***datalink layer package  ===> DestinationAddress 为 ： " + ePacket.getDestinationAddress());
			sb.append("== SenderProtocolAddress为：" + ePacket.getSourceAddress());
			System.out.println(sb.toString());
		}
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
			System.out.print(m);
		}
		System.out.println();
		System.out.println(str);
		try {
			//if(k != null && k.length > 0)
			System.out.println( new String(k, "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println();
	}
	
	
}
