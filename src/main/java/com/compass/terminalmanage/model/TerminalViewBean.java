package com.compass.terminalmanage.model;

public class TerminalViewBean {
	private String  terminalId;  //终端编号
	private String agencyid;    //当时机构
	private String oldagencyid; //先前机构
	private String xfdate;      //下发时间 
	private String hbdate;      // 回拔时间 
	private String username;    //用户名称 
	

	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(String agencyid) {
		this.agencyid = agencyid;
	}
	public String getOldagencyid() {
		return oldagencyid;
	}
	public void setOldagencyid(String oldagencyid) {
		this.oldagencyid = oldagencyid;
	}
	public String getXfdate() {
		return xfdate;
	}
	public void setXfdate(String xfdate) {
		this.xfdate = xfdate;
	}
	public String getHbdate() {
		return hbdate;
	}
	public void setHbdate(String hbdate) {
		this.hbdate = hbdate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

	

}
