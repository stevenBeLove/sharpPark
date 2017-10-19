package com.compass.agency.model;

public class SpecSystemBean {
	
	private String systemid; //来源系统
	private String levelControl; //控制显示级别
	private String onlineControl; //归属机构是否显示，0：不显示，1显示： 
	private String splitcontrol;  //分润明细下级机构归并显示,1显示 ，0不显示 
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getLevelControl() {
		return levelControl;
	}
	public void setLevelControl(String levelControl) {
		this.levelControl = levelControl;
	}
	public String getOnlineControl() {
		return onlineControl;
	}
	public void setOnlineControl(String onlineControl) {
		this.onlineControl = onlineControl;
	}
	public String getSplitcontrol() {
		return splitcontrol;
	}
	public void setSplitcontrol(String splitcontrol) {
		this.splitcontrol = splitcontrol;
	}
}
