package com.compass.vehicle.model;

public class MonthVehicleBrandExportBean {
	private String carNumber;
	private String vehicleBrandType;
	private String startDate;
	private String endDate;
	private String monthPayAmount;
	private String isExpire;
	private String inStatus;
	private String carOwnerName;
	private String carOwnerPhone;
	private String remark;
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getVehicleBrandType() {
		return vehicleBrandType;
	}
	public void setVehicleBrandType(String vehicleBrandType) {
		this.vehicleBrandType = vehicleBrandType;
	}
	public String getCarOwnerName() {
		return carOwnerName;
	}
	public void setCarOwnerName(String carOwnerName) {
		this.carOwnerName = carOwnerName;
	}
	public String getCarOwnerPhone() {
		return carOwnerPhone;
	}
	public void setCarOwnerPhone(String carOwnerPhone) {
		this.carOwnerPhone = carOwnerPhone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMonthPayAmount() {
		return monthPayAmount;
	}
	public void setMonthPayAmount(String monthPayAmount) {
		this.monthPayAmount = monthPayAmount;
	}
	public String getIsExpire() {
		return isExpire;
	}
	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
	}
	public String getInStatus() {
		return inStatus;
	}
	public void setInStatus(String inStatus) {
		this.inStatus = inStatus;
	}
}