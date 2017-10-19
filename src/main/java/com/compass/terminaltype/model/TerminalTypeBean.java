package com.compass.terminaltype.model;

/**
 * 
 * @author gaoyang
 * 终端类型实体类
 */
public class TerminalTypeBean {

	private String systemId;//来源系统编号
	private String systemName;//来源系统
	private String terminalTypeId;// 终端类型编号
	private String terminalTypeName;// 终端类型名称
	private String terminalTypeStatus;// 终端类型状态 0.无效 1.有效
	private String terminalCount;//当前终端类型个数
	private String terminalTypeStatusStr;
	private String terminalTypeDesc;//终端类型描述
	private String createrId;// 创建人编号
	private String createDate;// 创建时间

	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}	
	public String getTerminalTypeId() {
		return terminalTypeId;
	}
	public void setTerminalTypeId(String terminalTypeId) {
		this.terminalTypeId = terminalTypeId;
	}
	public String getTerminalTypeName() {
		return terminalTypeName;
	}
	public void setTerminalTypeName(String terminalTypeName) {
		this.terminalTypeName = terminalTypeName;
	}
	public String getTerminalTypeStatus() {
		return terminalTypeStatus;
	}
	public void setTerminalCount(String terminalCount) {
		this.terminalCount = terminalCount;
	}
	public String getTerminalCount() {
		return terminalCount;
	}
	public void setTerminalTypeStatus(String terminalTypeStatus) {
		this.terminalTypeStatus = terminalTypeStatus;
	}
	public String getTerminalTypeStatusStr() {
		return terminalTypeStatusStr;
	}
	public void setTerminalTypeStatusStr(String terminalTypeStatusStr) {
		this.terminalTypeStatusStr = terminalTypeStatusStr;
	}
	public String getTerminalTypeDesc() {
		return terminalTypeDesc;
	}
	public void setTerminalTypeDesc(String terminalTypeDesc) {
		this.terminalTypeDesc = terminalTypeDesc;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
}
