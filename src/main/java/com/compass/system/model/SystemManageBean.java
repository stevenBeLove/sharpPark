/**
 * 
 */
package com.compass.system.model;

/**
 * @author wanglong
 * 数据来源系统实体类
 * 
 */
public class SystemManageBean {

	private String systemId; // 系统编号
	private String systemName;// 系统名称
	private String systemCode;//系统编号
	private String systemStatus;// 系统状态
	private String systemStatusStr;
	private String systemDesc;// 系统描述
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
	
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}

	public String getSystemStatusStr() {
		return systemStatusStr;
	}

	public void setSystemStatusStr(String systemStatusStr) {
		this.systemStatusStr = systemStatusStr;
	}

	public String getSystemDesc() {
		return systemDesc;
	}

	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
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
