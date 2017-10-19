package com.compass.splitfee.model;

/**
 * 
 * @author wangLong
 * 
 *         分润明细实体类
 * 
 */
public class SplitFeeDealBean {
	private String agencyId; // 机构编号
	private String terminalCode; // 机构编号
	private String serialNumber;// 流水号
	private String agencyName;
	private String onlyCode; // 唯一编号
	private String dealTypeId;// 交易类型编号
	private String dealTypeStr;
	private String dealData;// 交易日期
	private String transAcount;// 交易金额
	private String feeAcount;// 可分润金额
	private String feeAmt;  //交易费率金额
	private String splitFeeAcount;// 分润金额
	private String oneSplitFeeAcount; // 一级分润金额
	private String twoSplitFeeAcount; // 二级分润金额
	private String threeSplitFeeAcount;// 三级分润金额
	private String fourSplitFeeAcount;// 四级分润金额
	private String fiveSplitFeeAcount;// //五级分润金额
	private String moneyLevel;  //当前级别的分润金额
	private String moneyLevelNext; //下级的分润金额
	private String profit;		// 当前机构利润

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getOnlyCode() {
		return onlyCode;
	}

	public void setOnlyCode(String onlyCode) {
		this.onlyCode = onlyCode;
	}

	public String getDealTypeId() {
		return dealTypeId;
	}

	public void setDealTypeId(String dealTypeId) {
		this.dealTypeId = dealTypeId;
	}

	public String getDealData() {
		return dealData;
	}

	public void setDealData(String dealData) {
		this.dealData = dealData;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getDealTypeStr() {
		return dealTypeStr;
	}

	public void setDealTypeStr(String dealTypeStr) {
		this.dealTypeStr = dealTypeStr;
	}

	public String getTransAcount() {
		return transAcount;
	}

	public void setTransAcount(String transAcount) {
		this.transAcount = transAcount;
	}

	public String getFeeAcount() {
		return feeAcount;
	}

	public void setFeeAcount(String feeAcount) {
		this.feeAcount = feeAcount;
	}

	public String getSplitFeeAcount() {
		return splitFeeAcount;
	}

	public void setSplitFeeAcount(String splitFeeAcount) {
		this.splitFeeAcount = splitFeeAcount;
	}

	public String getOneSplitFeeAcount() {
		return oneSplitFeeAcount;
	}

	public void setOneSplitFeeAcount(String oneSplitFeeAcount) {
		this.oneSplitFeeAcount = oneSplitFeeAcount;
	}

	public String getTwoSplitFeeAcount() {
		return twoSplitFeeAcount;
	}

	public void setTwoSplitFeeAcount(String twoSplitFeeAcount) {
		this.twoSplitFeeAcount = twoSplitFeeAcount;
	}

	public String getThreeSplitFeeAcount() {
		return threeSplitFeeAcount;
	}

	public void setThreeSplitFeeAcount(String threeSplitFeeAcount) {
		this.threeSplitFeeAcount = threeSplitFeeAcount;
	}

	public String getFourSplitFeeAcount() {
		return fourSplitFeeAcount;
	}

	public void setFourSplitFeeAcount(String fourSplitFeeAcount) {
		this.fourSplitFeeAcount = fourSplitFeeAcount;
	}

	public String getFiveSplitFeeAcount() {
		return fiveSplitFeeAcount;
	}

	public void setFiveSplitFeeAcount(String fiveSplitFeeAcount) {
		this.fiveSplitFeeAcount = fiveSplitFeeAcount;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getMoneyLevel() {
		return moneyLevel;
	}

	public void setMoneyLevel(String moneyLevel) {
		this.moneyLevel = moneyLevel;
	}

	public String getMoneyLevelNext() {
		return moneyLevelNext;
	}

	public void setMoneyLevelNext(String moneyLevelNext) {
		this.moneyLevelNext = moneyLevelNext;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

}
