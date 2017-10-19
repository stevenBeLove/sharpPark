package com.compass.dealmanager.model;

/**
 * 
 * @author machizhi
 * 其他扣款实体类
 * 
 */
public class OtherDeductBean {
	private Integer id;							// 唯一编号
	private String agencyId;					// 机构编号
	private Double transacount;			// 金额
	private String deductType;				// 扣款类型
	private Double deductMoney;		// 扣款金额
	private String deductDate;				// 扣款日期
	private String dealFlag;					// 处理标志
	private String createId;					// 创建人编号
	private String createDt;					// 创建时间
	private String comments;				// 备注
	
	private String agencyName;  		// 机构名称
	private String deductTypeName;	// 扣款类型名称
	private String dealFlagStr;				// 处理标志描述
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public Double getTransacount() {
		return transacount;
	}
	public void setTransacount(Double transacount) {
		this.transacount = transacount;
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
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getDeductTypeName() {
		return deductTypeName;
	}
	public void setDeductTypeName(String deductTypeName) {
		this.deductTypeName = deductTypeName;
	}
	public String getDealFlagStr() {
		return dealFlagStr;
	}
	public void setDealFlagStr(String dealFlagStr) {
		this.dealFlagStr = dealFlagStr;
	}

}
