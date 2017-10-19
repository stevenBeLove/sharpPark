/**
 * 
 */
package com.compass.splitmould.model;

/**
 * @author liCheng 
 * 顶级分润模板实体Bean
 * 
 */
public class SplitTopMouldBean {

	private String ruleId;// 模板编码
	private String agencyId;//机构编码
	private String agencyNameD; // 由他分配模板的机构名称
	private String agencyName;//机构编码
	private String mouldName; // 模板名称
	private String ruleRem;//备注
	private int status;//备注
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
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
	public String getRuleRem() {
		return ruleRem;
	}
	public void setRuleRem(String ruleRem) {
		this.ruleRem = ruleRem;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAgencyNameD() {
		return agencyNameD;
	}
	public void setAgencyNameD(String agencyNameD) {
		this.agencyNameD = agencyNameD;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
