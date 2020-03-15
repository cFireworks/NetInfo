package com.traffic.jpcap.decive;

import java.io.IOException;

import com.traffic.entity.InterfaceInfoEntity;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
/**
 * @Description 抓包获取数据并分析信息  
 * 2019年3月26日  下午2:42:12
 * @Author Huangxiaocong
 */
public class CatchDataPacketInfo implements PacketReceiver {

	/**
	 * 初始化参数信息 取得在指定网卡上的Jpcapcator对象
	 * @param deviceName 网卡名称
	 * @param filterCond 过滤条件
	 * @Author Huangxiaocong 2019年3月26日 下午2:47:53
	 */
	public void init(NetworkInterface deviceName, String filterCond) {
		JpcapCaptor jpcap = null;
		try {
			jpcap = JpcapCaptor.openDevice(deviceName, 5000, true, 20);
			//过滤代码 可以是协议 端口 IP 组合
			if(filterCond != null && !"".equals(filterCond)) {
				jpcap.setFilter(filterCond, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		jpcap.loopPacket(20, new CatchDataPacketInfo());
	}
	/**
	 * 解析包信息
	 */
	@Override
	public void receivePacket(Packet packet) {
		//封装抓包获取数据
		InterfaceInfoEntity interInfoEntity = new InterfaceInfoEntity();
		//分析协议类型
		if(packet instanceof ARPPacket) { //该协议无端口号
			ARPPacket arpPacket = (ARPPacket) packet;
			interInfoEntity.setContractType("ARP协议");
			interInfoEntity.setCaplen(arpPacket.caplen);
			interInfoEntity.setSecTime(arpPacket.sec);
			interInfoEntity.setSourceIp(arpPacket.getSenderProtocolAddress().toString());
			interInfoEntity.setSourceMacAddr(arpPacket.getSenderHardwareAddress().toString());
			interInfoEntity.setTargetIp(arpPacket.getTargetProtocolAddress().toString());
			interInfoEntity.setTargetMacAddr(arpPacket.getTargetHardwareAddress().toString());
		} else if(packet instanceof UDPPacket) {
			//TODO 可获取头部信息
			UDPPacket udpPacket = (UDPPacket) packet;
			interInfoEntity.setContractType("UDP协议");
			interInfoEntity.setCaplen(udpPacket.caplen);
			interInfoEntity.setSecTime(udpPacket.sec);
			interInfoEntity.setSourceIp(udpPacket.src_ip.getHostAddress());
			interInfoEntity.setSourcePort(udpPacket.src_port);
			EthernetPacket datalink = (EthernetPacket) udpPacket.datalink;
			interInfoEntity.setSourceMacAddr(getMacInfo(datalink.src_mac));
			interInfoEntity.setTargetIp(udpPacket.dst_ip.getHostAddress());
			interInfoEntity.setTargetPort(udpPacket.dst_port);
			interInfoEntity.setTargetMacAddr(getMacInfo(datalink.dst_mac));
		} else if(packet instanceof TCPPacket) {
			TCPPacket tcpPacket = (TCPPacket) packet;
			//TODO 可获取头部信息
			interInfoEntity.setContractType("TCP协议");
			interInfoEntity.setCaplen(tcpPacket.caplen);
			interInfoEntity.setSecTime(tcpPacket.sec);
			interInfoEntity.setSourceIp(tcpPacket.src_ip.getHostAddress());
			interInfoEntity.setSourcePort(tcpPacket.src_port);
			EthernetPacket datalink = (EthernetPacket) tcpPacket.datalink;
			interInfoEntity.setSourceMacAddr(getMacInfo(datalink.src_mac));
			interInfoEntity.setTargetIp(tcpPacket.dst_ip.getHostAddress());
			interInfoEntity.setTargetPort(tcpPacket.dst_port);
			interInfoEntity.setTargetMacAddr(getMacInfo(datalink.dst_mac));
		} else if(packet instanceof ICMPPacket) { //该协议无端口号
			ICMPPacket icmpPacket = (ICMPPacket) packet;
			//TODO 可获取头部信息
			interInfoEntity.setContractType("ICMP协议");
			interInfoEntity.setCaplen(icmpPacket.caplen);
			interInfoEntity.setSecTime(icmpPacket.sec);
			interInfoEntity.setSourceIp(icmpPacket.src_ip.getHostAddress());
			EthernetPacket datalink = (EthernetPacket) icmpPacket.datalink;
			interInfoEntity.setSourceMacAddr(getMacInfo(datalink.src_mac));
			interInfoEntity.setTargetIp(icmpPacket.dst_ip.getHostAddress());
			interInfoEntity.setTargetMacAddr(getMacInfo(datalink.dst_mac));
		}
		System.out.println(interInfoEntity);
		
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
}
