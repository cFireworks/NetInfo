package com.traffic.jpcap.test;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.traffic.jpcap.JpcapTipDemo;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class MainTest_1 {
	
	public static void main(String[] args) {
		Properties properties = System.getProperties();
		Set<Entry<Object, Object>> setEntry = properties.entrySet();
		for(Entry<Object, Object> se: setEntry) {
			System.out.println(se.getKey() + " --> " + se.getValue());
		}
		//System.exit(0);
		//JpcapCaptor代表了系统中的一个网络接口卡
		//getDeviceList可以获取机器上网络接口卡对象的数组，数组中的每个NetworkInterface代表一个网络接口。
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();	
		/*for(int i = 0; i < devices.length; i++) {
			int a = 0;
			//本地网络信息
			byte[] b = devices[i].mac_address;	//网卡物理地址
			System.out.print("网卡MAC： 00");
			for(int j = 0; j < b.length; j++) {
				//a = a << 8;
				a = b[j];
				a = a << 24;
				a = a >>> 24;
				System.out.print(Integer.toHexString(a));
			}
			System.out.println();
			NetworkInterfaceAddress[] kAddresses = devices[i].addresses;
			System.out.println("网卡MAC" + Integer.toHexString(a));
			for(int n = 0; n < kAddresses.length; n++) {
				System.out.println("本机IP地址：" + kAddresses[n].address);
				System.out.println("子网掩码为：" + kAddresses[n].subnet);
			}
			System.out.println("网络连接类型：" + devices[i].datalink_description);
			NetworkInterface deviceName = devices[i];
			System.out.println("设备类型为 ：" + deviceName);
			System.out.println();
			System.out.println();
			System.out.println();
		}
		System.exit(0);*/
		NetworkInterface deviceName = devices[2];
		//将网卡设为混杂模式下用网络设备deviceName
		try {
			/*openDevice(NetworkInterface intrface, int snaplen, boolean promisc, int to_ms)
				取得在指定网卡上的Jpcapcator对象，Interface:所返回的某个网卡对象snaplen；snaplen:一次性要抓取数据包的最大长度
				promisc：设置是否混杂模式，处于混杂模式将接收所有数据包，如果设置为混杂模式后，调用了包过滤函数setFilter()将不起任何作用；
				to_ms : 这个参数主要用于processPacket()方法，指定超时的时间。
			*/
			JpcapCaptor jpcapCaptor = JpcapCaptor.openDevice(deviceName, 2000, false, 1);
			/*int loopPacket(int count, PacketReceiver handler) ：通过openDevice方法取得每个网络接口上的JpcapCaptor对象，就可通过这个方法抓包了。
				count：表示要抓的包的数目，如果设置为-1表示永远抓下去；handler：第二个参数必须是实现了PacketReceiver接口的一个对象，抓到的包将调用这个对象的
				receivePacket方法处理，这个方法调用会阻塞等待 与该方法相对应的是breakLoop()，在JacapCaptor对象上的阻塞等待的方法将强制终止
			 */
			jpcapCaptor.loopPacket(-1, new JpcapTipDemo());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
