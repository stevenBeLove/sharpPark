package com.compass.dealmanager.model;

public class ProfitBean {
	private String agencyId;// 机构编号
	private String parentagencyId;// 父机构编号
	private String agnecyName;// 机构名称
	private String dealtype;// 交易类型
	private String yearmonth;// 交易年月
	private String dealCount;// 总笔数
	private Double transCount;// 交易金额
	private Double amount;// 可分润金额
	private Double amounted;  //分润到的金额
	private Double fee;//手续费
	private String dealCountagency;// 当前机构总笔数
	private Double transCountagency;// 当前机构交易金额
	private Double amountagency;// 当前机构可分润金额
	private Double feeagency;//当前机构手续费
	public String getDealCountagency() {
		return dealCountagency;
	}

	public void setDealCountagency(String dealCountagency) {
		this.dealCountagency = dealCountagency;
	}

	public Double getTransCountagency() {
		return transCountagency;
	}

	public void setTransCountagency(Double transCountagency) {
		this.transCountagency = transCountagency;
	}

	public Double getAmountagency() {
		return amountagency;
	}

	public void setAmountagency(Double amountagency) {
		this.amountagency = amountagency;
	}

	public Double getFeeagency() {
		return feeagency;
	}

	public void setFeeagency(Double feeagency) {
		this.feeagency = feeagency;
	}

	
	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	
	public Double getAmounted() {
		return amounted;
	}

	public void setAmounted(Double amounted) {
		this.amounted = amounted;
	}
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getParentagencyId() {
		return parentagencyId;
	}

	public void setParentagencyId(String parentagencyId) {
		this.parentagencyId = parentagencyId;
	}

	public String getAgnecyName() {
		return agnecyName;
	}

	public void setAgnecyName(String agnecyName) {
		this.agnecyName = agnecyName;
	}

	public String getDealtype() {
		return dealtype;
	}

	public void setDealtype(String dealtype) {
		this.dealtype = dealtype;
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

	

	

}
