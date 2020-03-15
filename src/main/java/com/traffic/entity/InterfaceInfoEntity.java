package com.traffic.entity;

import java.io.Serializable;

/**
 * @Description 监控的设备的接口信息 
 * 2019年3月26日  下午2:59:32
 * @Author Huangxiaocong
 */
public class InterfaceInfoEntity implements Serializable {

	private static final long serialVersionUID = 13141314L;
	/** 协议类型 */
	private String contractType;
	/** 数据包长度 */
	private Integer caplen;
	/** 时间戳  */
	private Long secTime;
	/** 源Mac地址 */
	private String sourceMacAddr;
	/** 源ip地址 */
	private String sourceIp;
	/** 源端口号 */
	private Integer sourcePort;
	/** 目标mac地址 */
	private String targetMacAddr;
	/** 目标ip地址 */
	private String targetIp;
	/** 目标端口号 */
	private Integer targetPort;
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getSourceMacAddr() {
		return sourceMacAddr;
	}
	public void setSourceMacAddr(String sourceMacAddr) {
		this.sourceMacAddr = sourceMacAddr;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public String getTargetMacAddr() {
		return targetMacAddr;
	}
	public void setTargetMacAddr(String targetMacAddr) {
		this.targetMacAddr = targetMacAddr;
	}
	public String getTargetIp() {
		return targetIp;
	}
	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getCaplen() {
		return caplen;
	}
	public void setCaplen(Integer caplen) {
		this.caplen = caplen;
	}
	public Long getSecTime() {
		return secTime;
	}
	public void setSecTime(Long secTime) {
		this.secTime = secTime;
	}
	public Integer getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}
	public Integer getTargetPort() {
		return targetPort;
	}
	public void setTargetPort(Integer targetPort) {
		this.targetPort = targetPort;
	}
	@Override
	public String toString() {
		return "InterfaceInfoEntity [contractType=" + contractType + ", caplen=" + caplen + ", secTime=" + secTime
				+ ", sourceMacAddr=" + sourceMacAddr + ", sourceIp=" + sourceIp + ", sourcePort=" + sourcePort
				+ ", targetMacAddr=" + targetMacAddr + ", targetIp=" + targetIp + ", targetPort=" + targetPort + "]";
	}
	
}
