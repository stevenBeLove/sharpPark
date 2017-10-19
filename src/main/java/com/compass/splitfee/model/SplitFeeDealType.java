package com.compass.splitfee.model;

public class SplitFeeDealType {

	private String parentagencyId;// 父机构编号
	private String agencyId;// 机构编号
	private String agnecyName;// 机构名称
	private String systemId; // 系统编号
	private String systemName; // 系统名称
	private String dealtype;// 交易类型
	private String dealtypeStr;// 交易类型
	private String yearmonth;// 交易年月
	private String dealCount;// 总笔数
	private Double transCount;// 总 交易金额
	private Double amount;// 总分润到金额
	private String isAffirm; // 状态 ： 0：未确认 1：已确认
	private String isAffirmStr; // 状态 ： 0：未确认 1：已确认
	private String isCalc; // 是否计算 0：未计算，1：已计算
	private String isCalcStr; // 是否计算 0：未计算，1：已计算
	private String parentIsCalc; // 上级是否计算

	public String getParentagencyId() {
		return parentagencyId;
	}

	public void setParentagencyId(String parentagencyId) {
		this.parentagencyId = parentagencyId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgnecyName() {
		return agnecyName;
	}

	public void setAgnecyName(String agnecyName) {
		this.agnecyName = agnecyName;
	}

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

	public String getDealtype() {
		return dealtype;
	}

	public void setDealtype(String dealtype) {
		this.dealtype = dealtype;
	}

	public String getDealtypeStr() {
		return dealtypeStr;
	}

	public void setDealtypeStr(String dealtypeStr) {
		this.dealtypeStr = dealtypeStr;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getDealCount() {
		return dealCount;
	}

	public void setDealCount(String dealCount) {
		this.dealCount = dealCount;
	}

	public Double getTransCount() {
		return transCount;
	}

	public void setTransCount(Double transCount) {
		this.transCount = transCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getIsAffirm() {
		return isAffirm;
	}

	public void setIsAffirm(String isAffirm) {
		this.isAffirm = isAffirm;
	}

	public String getIsCalc() {
		return isCalc;
	}

	public void setIsCalc(String isCalc) {
		this.isCalc = isCalc;
	}

	public String getParentIsCalc() {
		return parentIsCalc;
	}

	public void setParentIsCalc(String parentIsCalc) {
		this.parentIsCalc = parentIsCalc;
	}

	public String getIsAffirmStr() {
		return isAffirmStr;
	}

	public void setIsAffirmStr(String isAffirmStr) {
		this.isAffirmStr = isAffirmStr;
	}

	public String getIsCalcStr() {
		return isCalcStr;
	}

	public void setIsCalcStr(String isCalcStr) {
		this.isCalcStr = isCalcStr;
	}

}
