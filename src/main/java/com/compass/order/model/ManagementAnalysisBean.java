package com.compass.order.model;

public class ManagementAnalysisBean {
	
	private String dateStr;
	private String tempTotalAmount;
	private String tempAmount;
	private String tempAlipayAmount;
	private String tempWeiXinAmount;
	private Integer inTimeCount;
	private Integer inTimeStayCount; //在场
	private Integer outTimeCount;
	private Integer expectedVehicleCount;
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getTempTotalAmount() {
		return tempTotalAmount;
	}
	public void setTempTotalAmount(String tempTotalAmount) {
		this.tempTotalAmount = tempTotalAmount;
	}
	public String getTempAmount() {
		return tempAmount;
	}
	public void setTempAmount(String tempAmount) {
		this.tempAmount = tempAmount;
	}
	public String getTempAlipayAmount() {
		return tempAlipayAmount;
	}
	public void setTempAlipayAmount(String tempAlipayAmount) {
		this.tempAlipayAmount = tempAlipayAmount;
	}
	public String getTempWeiXinAmount() {
		return tempWeiXinAmount;
	}
	public void setTempWeiXinAmount(String tempWeiXinAmount) {
		this.tempWeiXinAmount = tempWeiXinAmount;
	}
	public Integer getInTimeCount() {
		return this.inTimeCount+this.inTimeStayCount;
	}
	public void setInTimeCount(Integer inTimeCount) {
		this.inTimeCount = inTimeCount;
	}
	public Integer getInTimeStayCount() {
		return inTimeStayCount;
	}
	public void setInTimeStayCount(Integer inTimeStayCount) {
		this.inTimeStayCount = inTimeStayCount;
	}
	public Integer getOutTimeCount() {
		return outTimeCount;
	}
	public void setOutTimeCount(Integer outTimeCount) {
		this.outTimeCount = outTimeCount;
	}
	public Integer getExpectedVehicleCount() {
		return expectedVehicleCount;
	}
	public void setExpectedVehicleCount(Integer expectedVehicleCount) {
		this.expectedVehicleCount = expectedVehicleCount;
	}
}
