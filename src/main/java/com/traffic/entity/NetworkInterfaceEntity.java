package com.traffic.entity;

import java.io.Serializable;

/**
 * @Description 网卡信息 实体
 * 2019年3月26日  上午11:29:30
 * @Author Huangxiaocong
 */
public class NetworkInterfaceEntity implements Serializable {

	private static final long serialVersionUID = 1314L;
	/** 网卡名称 （NIC为NetworkInterface的简称） */
	private String NICName;
	/** 网卡描述（NIC为NetworkInterface的简称）*/
	private String NICDesc;
	/** ipv6地址 */
	private String ipv6Addr;
	/** IPv4地址 */
	private String ipv4Addr;
	/** mac地址  */
	private String macAddr;
	/** 广播地址  */
	private String broadcase;
	/** 子网掩码 */
	private String subnet;
	/** 数据链路名称 */
	private String dataLinkName;
	/** 数据链路描述  */
	private String dataLinkDesc;
	
	//以下为get/set方法
	public String getNICName() {
		return NICName;
	}
	public void setNICName(String nICName) {
		NICName = nICName;
	}
	public String getNICDesc() {
		return NICDesc;
	}
	public void setNICDesc(String nICDesc) {
		NICDesc = nICDesc;
	}
	public String getIpv6Addr() {
		return ipv6Addr;
	}
	public void setIpv6Addr(String ipv6Addr) {
		this.ipv6Addr = ipv6Addr;
	}
	public String getIpv4Addr() {
		return ipv4Addr;
	}
	public void setIpv4Addr(String ipv4Addr) {
		this.ipv4Addr = ipv4Addr;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getBroadcase() {
		return broadcase;
	}
	public void setBroadcase(String broadcase) {
		this.broadcase = broadcase;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
	public String getDataLinkName() {
		return dataLinkName;
	}
	public void setDataLinkName(String dataLinkName) {
		this.dataLinkName = dataLinkName;
	}
	public String getDataLinkDesc() {
		return dataLinkDesc;
	}
	public void setDataLinkDesc(String dataLinkDesc) {
		this.dataLinkDesc = dataLinkDesc;
	}
	
}
