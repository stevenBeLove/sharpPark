package com.compass.systemlog.model;

/**
 * 
 * @author LiCheng
 * 系统日志实体类
 *
 */
public class SystemLogBean {
	private String logId;//日志编号
	private String ipAddress;//操作者 IP地址
	private String operateTime; //操作时间
	private String operateAgency; // 操作人所在机构名
	private String operateAgencyId; // 操作人所在机构Id
	private String operateMan; // 操作人名称
	private String operateId; // 操作人ID
	private String operateName; // 操作名称
	private String operateType; // 操作类型 1-增，2-删，3-改，4-查
	private String operateDetail; // 操作详情
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperateAgency() {
		return operateAgency;
	}
	public void setOperateAgency(String operateAgency) {
		this.operateAgency = operateAgency;
	}
	public String getOperateMan() {
		return operateMan;
	}
	public void setOperateMan(String operateMan) {
		this.operateMan = operateMan;
	}
	public String getOperateId() {
		return operateId;
	}
	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateDetail() {
		return operateDetail;
	}
	public void setOperateDetail(String operateDetail) {
		this.operateDetail = operateDetail;
	}
	public String getOperateAgencyId() {
		return operateAgencyId;
	}
	public void setOperateAgencyId(String operateAgencyId) {
		this.operateAgencyId = operateAgencyId;
	}
	
}
