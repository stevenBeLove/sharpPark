/**
 * 
 */
package com.compass.splitrule.model;

/**
 * @author wangLong 分润规则实体Bean
 * 
 */
public class SplitRuleBean {

	private String ruleId;// 规则编号
	private String singleRuleId;// 单笔区间规则编号
	private String agencyId; // 机构编号
	private String childAgencyId; // 子机构编号
	private String childAgencyName;// 子机构名称
	private String splittingMode; // 分润方式 1:按总笔数分润 ，2：按总金额分润 ，3：按收益分润
	private String splittingModeStr;
	private String dealType; // 交易类型
	private String dealTypeName;
	private String scale; // 交易规模 1：总笔数2：总金额
	private String scaleStr;// 交易规模
	private String scaleStartValue; // 规模区间起始值
	private String scaleEndValue; // 规模区间结束值
	private String singleDealStartValue; // 单笔交易金额区间起始值
	private String singleDealEndValue; // 单笔交易金额区间结束值
	private String rate; // 分润费率
	private String validityData; // 规则生效日期
	private String createrId;// 创建人编号
	private String createDate;// 创建时间

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getSingleRuleId() {
		return singleRuleId;
	}

	public void setSingleRuleId(String singleRuleId) {
		this.singleRuleId = singleRuleId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getChildAgencyId() {
		return childAgencyId;
	}

	public void setChildAgencyId(String childAgencyId) {
		this.childAgencyId = childAgencyId;
	}

	public String getChildAgencyName() {
		return childAgencyName;
	}

	public void setChildAgencyName(String childAgencyName) {
		this.childAgencyName = childAgencyName;
	}

	public String getSplittingMode() {
		return splittingMode;
	}

	public void setSplittingMode(String splittingMode) {
		this.splittingMode = splittingMode;
	}

	public String getSplittingModeStr() {
		return splittingModeStr;
	}

	public void setSplittingModeStr(String splittingModeStr) {
		this.splittingModeStr = splittingModeStr;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealTypeName() {
		return dealTypeName;
	}

	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getScaleStr() {
		return scaleStr;
	}

	public void setScaleStr(String scaleStr) {
		this.scaleStr = scaleStr;
	}

	public String getScaleStartValue() {
		return scaleStartValue;
	}

	public void setScaleStartValue(String scaleStartValue) {
		this.scaleStartValue = scaleStartValue;
	}

	public String getScaleEndValue() {
		return scaleEndValue;
	}

	public void setScaleEndValue(String scaleEndValue) {
		this.scaleEndValue = scaleEndValue;
	}

	public String getSingleDealStartValue() {
		return singleDealStartValue;
	}

	public void setSingleDealStartValue(String singleDealStartValue) {
		this.singleDealStartValue = singleDealStartValue;
	}

	public String getSingleDealEndValue() {
		return singleDealEndValue;
	}

	public void setSingleDealEndValue(String singleDealEndValue) {
		this.singleDealEndValue = singleDealEndValue;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getValidityData() {
		return validityData;
	}

	public void setValidityData(String validityData) {
		this.validityData = validityData;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
