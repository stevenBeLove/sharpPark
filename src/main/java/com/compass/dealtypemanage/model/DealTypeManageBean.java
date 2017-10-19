/**
 * 
 */
package com.compass.dealtypemanage.model;

/**
 * @author wanglong
 * 交易类型实体类
 */
public class DealTypeManageBean {

	private String dealTypeId; // 交易类型编码
	private String systemId;//来源系统编号
	private String systemName;//来源系统名称
	private String serverCode;//服务编码
	private String tradeCode;//渠道编码
	private String dealTypeName;// 交易类型名称
	private String dealTypeStatus;// 交易类型状态
	private String dealTypeStatusStr;
	private String dealTypeDesc;// 交易类型描述
	private String createrId;// 创建人编号
	private String createDate;// 创建时间
	
	public String getDealTypeId() {
		return dealTypeId;
	}
	public void setDealTypeId(String dealTypeId) {
		this.dealTypeId = dealTypeId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemName() {
		return systemName;
	}
	public String getServerCode() {
		return serverCode;
	}
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getDealTypeName() {
		return dealTypeName;
	}
	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}
	public String getDealTypeStatus() {
		return dealTypeStatus;
	}
	public void setDealTypeStatus(String dealTypeStatus) {
		this.dealTypeStatus = dealTypeStatus;
	}
	public String getDealTypeStatusStr() {
		return dealTypeStatusStr;
	}
	public void setDealTypeStatusStr(String dealTypeStatusStr) {
		this.dealTypeStatusStr = dealTypeStatusStr;
	}
	public String getDealTypeDesc() {
		return dealTypeDesc;
	}
	public void setDealTypeDesc(String dealTypeDesc) {
		this.dealTypeDesc = dealTypeDesc;
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
