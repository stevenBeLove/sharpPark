package com.compass.dataManage.model;

public class ProcLogBean {
	
	private String  procName;    //存储过程名
	private String  procRemark;  //存储过程备注
	private String stime;        // 开始时间
	private String etime;        //结束时间 
	private String procTime;	 //执行时间 
	private String qty;		     //处理数据量
	private String runFlag;		 //执行状态
	private String retCode;      //处理状态码
	private String retMsg;       //处理消息
    private String note1;		 //备注1
    private String note2;        //备注2
    private String execDate;     //执行日期
    private String agencyid;     //执行机构
	public String getProcName() {
		return procName;
	}
	public void setProcName(String procName) {
		this.procName = procName;
	}
	public String getProcRemark() {
		return procRemark;
	}
	public void setProcRemark(String procRemark) {
		this.procRemark = procRemark;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getProcTime() {
		return procTime;
	}
	public void setProcTime(String procTime) {
		this.procTime = procTime;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getNote1() {
		return note1;
	}
	public void setNote1(String note1) {
		this.note1 = note1;
	}
	public String getNote2() {
		return note2;
	}
	public void setNote2(String note2) {
		this.note2 = note2;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public String getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(String agencyid) {
		this.agencyid = agencyid;
	}
}
