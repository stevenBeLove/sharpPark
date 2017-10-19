package com.compass.dealmanager.model;

/**
 * 
 * @author machizhi
 * 交易扣款实体类
 * 
 */
public class DealDeductBean {
	private String dealId;						// 交易编号
	private String serialNumber;			// 流水号
	private Double transacount;			// 交易金额
	private String terminalId;				// 终端编号
	private String dealDate;					// 交易日期
	private String dealTime;					// 交易时间
	private String dealTypeId;				// 交易类型编号
	private String onlyCode;				// 唯一编号
	private String bankCardNumber;	// 银行卡号
	private String terminalOnlyCode;	// 终端唯一号
	private String mobilePhone; 			// 手机号
	private String deductType;				// 扣款类型
	private Double deductMoney;		// 扣款金额
	private String deductDate;				// 扣款日期
	private String dealFlag;					// 处理标志
	private String createId;					// 创建人编号
	private String createDt;					// 创建时间
	private String comments;				// 备注
	
	private String agencyId;					// 机构编号
	private String agencyName;  		// 机构名称
	private String dealTypeName;		// 交易类型名称
	private String deductTypeName;	// 扣款类型名称
	private String dealFlagStr;				// 处理标志描述
	
	public String getDealId() {
		return dealId;
	}
	public void setDealId(String dealId) {
		this.dealId = dealId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Double getTransacount() {
		return transacount;
	}
	public void setTransacount(Double transacount) {
		this.transacount = transacount;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getDealTime() {
		return dealTime;
	}
	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}
	public String getDealTypeId() {
		return dealTypeId;
	}
	public void setDealTypeId(String dealTypeId) {
		this.dealTypeId = dealTypeId;
	}
	public String getOnlyCode() {
		return onlyCode;
	}
	public void setOnlyCode(String onlyCode) {
		this.onlyCode = onlyCode;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getTerminalOnlyCode() {
		return terminalOnlyCode;
	}
	public void setTerminalOnlyCode(String terminalOnlyCode) {
		this.terminalOnlyCode = terminalOnlyCode;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getDeductType() {
		return deductType;
	}
	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}
	public Double getDeductMoney() {
		return deductMoney;
	}
	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}
	public String getDeductDate() {
		return deductDate;
	}
	public void setDeductDate(String deductDate) {
		this.deductDate = deductDate;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDealTypeName() {
		return dealTypeName;
	}
	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getDealFlagStr() {
		return dealFlagStr;
	}
	public void setDealFlagStr(String dealFlagStr) {
		this.dealFlagStr = dealFlagStr;
	}
	public String getDeductTypeName() {
		return deductTypeName;
	}
	public void setDeductTypeName(String deductTypeName) {
		this.deductTypeName = deductTypeName;
	}

}
