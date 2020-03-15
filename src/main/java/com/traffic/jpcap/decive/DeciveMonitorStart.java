package com.traffic.jpcap.decive;

import com.traffic.entity.NetworkInterfaceEntity;
import com.traffic.winInfo.LocalWinInfoUtils;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

/**
 * @Description 设备接口信息
 * 2019年3月26日  下午3:53:01
 * @Author Huangxiaocong
 */
public class DeciveMonitorStart {
	//测试
	public static void main(String[] args) {
		DeciveMonitorStart deciveMonitorStart = new DeciveMonitorStart();
		String cond = " (dst host 10.255.0.13 and dst port 12388)";
		deciveMonitorStart.monitorByDeciveIp("");
	}
	/**
	 * 通过设备ip监控接口信息
	 * 。。@param ipList 设备ip
	 * @param cond 过滤条件 可为空
	 * @Author Huangxiaocong 2019年3月26日 下午3:40:25
	 */
	public void monitorByDeciveIp(String cond) {
		long begin = System.currentTimeMillis();
		// 获取设备列表
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		NetworkInterfaceEntity nICEntity = null;
		for(int i = 0; i < devices.length; i++) {
			NetworkInterface netInterface = devices[i];
			nICEntity = getServerDeciverInfo(netInterface);
			String ipv4 = nICEntity.getIpv4Addr();
			String mac = nICEntity.getMacAddr().replace(':', '-');
			//与当前网卡比较mac地址和ipv4地址
			if(LocalWinInfoUtils.getIpAddress().equals(ipv4) //
					&& LocalWinInfoUtils.getMacAddress().equalsIgnoreCase(mac)) {
				//服务器端作为dst 目标  客户端作为源src
				String filterCond = cond == null || cond.equals("") ? "( dst host " + ipv4 + ")" : cond + " and ( dst host " + ipv4 + ")";
				startCapThread(netInterface, filterCond);
			} else {
				continue;
			}
		}
		System.out.println(System.currentTimeMillis() - begin);
	}
	
	/**
	 * 启动一个线程独立运行抓包
	 * @param deviceName
	 * @param filterCond
	 * @Author Huangxiaocong 2019年3月26日 下午2:35:51
	 */
	public void startCapThread(NetworkInterface deviceName, String filterCond) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				CatchDataPacketInfo catchDataPacketInfo = new CatchDataPacketInfo();
				catchDataPacketInfo.init(deviceName, filterCond);
			}
		};
		new Thread(runnable).start();
	}
	
	/**
	 * 获取网卡的信息
	 * @param netInterface
	 * @return
	 * @Author Huangxiaocong 2019年3月26日 下午2:22:36
	 */
	public NetworkInterfaceEntity getServerDeciverInfo(NetworkInterface netInterface) {
		NetworkInterfaceEntity nICEntity = new NetworkInterfaceEntity();
		nICEntity.setNICName(netInterface.name);
		nICEntity.setNICDesc(netInterface.description);
		nICEntity.setDataLinkName(netInterface.datalink_name);
		nICEntity.setDataLinkDesc(netInterface.datalink_description);
		//计算mac地址
		byte[] bs = netInterface.mac_address;
		StringBuilder sBuilder = new StringBuilder();
		int count = 1;
		for(byte b : bs) {
			sBuilder.append(Integer.toHexString(b & 0xff));
			if(count++ != bs.length) sBuilder.append(":");
		}
		nICEntity.setMacAddr(sBuilder.toString());
		//查找ip地址
		NetworkInterfaceAddress[] netInterAddresses = netInterface.addresses;
		for(int i = 0; i < netInterAddresses.length; i++) {
			if(i == 0) {
				nICEntity.setIpv6Addr(netInterAddresses[i].address.getHostAddress());
			} else if(i == 1) {
				nICEntity.setIpv4Addr(netInterAddresses[i].address.getHostAddress());
				nICEntity.setBroadcase(netInterAddresses[i].broadcast.getHostAddress());
				nICEntity.setSubnet(netInterAddresses[i].subnet.getHostAddress());
			}
		}
		return nICEntity;
	}
}
