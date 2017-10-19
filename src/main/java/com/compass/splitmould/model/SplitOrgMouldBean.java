/**
 * 
 */
package com.compass.splitmould.model;

/**
 * @author liCheng 
 * 机构模板表实体Bean
 * 
 */
public class SplitOrgMouldBean {

	private String ruleId;// 规则编号
	private String ruleNum;//模板中的某一行
	private String dealType; // 业务名称(交易类型)
	private String dealTypeName;
	private String splittingMode; // 分润方式 1:按总笔数分润 ，2：按总金额分润
	private String splittingModeStr;
	private String splittingRegionMode;//分润规则（分润区间类型）1-月交易笔数2-月交易金额3-单笔交易金额  
	private String splittingRegionModeStr;
	private int ruleBegin;//规则起点（开区间）
	private int ruleEnd;//规则终点（闭区间）
	private double ruleValue;//规则值
	private String validityDate;//生效日期
	private String createId;//创建人
	private String createDate;//创建时间
	private String ruleRem;//备注
	private String agencyId;//机构编号
	private String agencyName;//机构名称
	
	private String mouldName;//增加值之后的模板名称
	
	private String invalidiayDate;//失效日期
	
	
	
	public String getInvalidiayDate() {
		return invalidiayDate;
	}
	public void setInvalidiayDate(String invalidiayDate) {
		this.invalidiayDate = invalidiayDate;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleNum() {
		return ruleNum;
	}
	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
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
	public String getSplittingRegionMode() {
		return splittingRegionMode;
	}
	public void setSplittingRegionMode(String splittingRegionMode) {
		this.splittingRegionMode = splittingRegionMode;
	}
	public String getSplittingRegionModeStr() {
		return splittingRegionModeStr;
	}
	public void setSplittingRegionModeStr(String splittingRegionModeStr) {
		this.splittingRegionModeStr = splittingRegionModeStr;
	}
	public int getRuleBegin() {
		return ruleBegin;
	}
	public void setRuleBegin(int ruleBegin) {
		this.ruleBegin = ruleBegin;
	}
	public int getRuleEnd() {
		return ruleEnd;
	}
	public void setRuleEnd(int ruleEnd) {
		this.ruleEnd = ruleEnd;
	}
	
	public double getRuleValue() {
		return ruleValue;
	}
	public void setRuleValue(double ruleValue) {
		this.ruleValue = ruleValue;
	}
	public String getValidityDate() {
		return validityDate;
	}
	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getRuleRem() {
		return ruleRem;
	}
	public void setRuleRem(String ruleRem) {
		this.ruleRem = ruleRem;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getMouldName() {
		return mouldName;
	}
	public void setMouldName(String mouldName) {
		this.mouldName = mouldName;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	
	
	
}
