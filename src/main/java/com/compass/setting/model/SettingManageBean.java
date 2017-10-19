/**
 * 
 */
package com.compass.setting.model;

/**
 * @author wangLong 导入数据设置实体类
 */
public class SettingManageBean {

	private String settingId; // 设置编号
	private String systemCode;// 系统编号
	private String systemName;// 系统编号
	private String settingStatus;// 设置状态
	private String settingStatusStr;
	private String startFlagStr;
	private String path; // 文件路径
	private String splitStr; // 分隔符
	private String fileName;// 文件名称
	private String startFlag; // 文件读取起始位置
	private String dealId;// 交易编号
	private String serialNumber;// 流水号
	private String transaCount;// 交易金额
	private String terminalCode;// 终端编号
	private String dealData;// 交易日期
	private String dealTime;// 交易时间
	private String dealStatus;// 交易状态
	private String dealtypeId;// 交易类型编号
	private String dealDesc;// 交易描述
	private String dealRebackCode;// 交易返回码
	private String bankCardnumber;//银行编号
	public String getBankCardnumber() {
		return bankCardnumber;
	}

	public void setBankCardnumber(String bankCardnumber) {
		this.bankCardnumber = bankCardnumber;
	}

	private String merchantCode;// 商户号
	private String cost;// 成本金额
	private String settingDesc;// 设置描述
	private String createrId;// 创建人编号
	private String createDate;// 创建时间

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getSettingStatus() {
		return settingStatus;
	}

	public void setSettingStatus(String settingStatus) {
		this.settingStatus = settingStatus;
	}

	public String getSettingStatusStr() {
		return settingStatusStr;
	}

	public void setStartFlagStr(String startFlagStr) {
		this.startFlagStr = startFlagStr;
	}

	public String getStartFlagStr() {
		return startFlagStr;
	}

	public void setSettingStatusStr(String settingStatusStr) {
		this.settingStatusStr = settingStatusStr;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSplitStr() {
		return splitStr;
	}

	public void setSplitStr(String splitStr) {
		this.splitStr = splitStr;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStartFlag() {
		return startFlag;
	}

	public void setStartFlag(String startFlag) {
		this.startFlag = startFlag;
	}

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

	public String getTransaCount() {
		return transaCount;
	}

	public void setTransaCount(String transaCount) {
		this.transaCount = transaCount;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getDealData() {
		return dealData;
	}

	public void setDealData(String dealData) {
		this.dealData = dealData;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getDealtypeId() {
		return dealtypeId;
	}

	public void setDealtypeId(String dealtypeId) {
		this.dealtypeId = dealtypeId;
	}

	public String getDealDesc() {
		return dealDesc;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}

	public String getDealRebackCode() {
		return dealRebackCode;
	}

	public void setDealRebackCode(String dealRebackCode) {
		this.dealRebackCode = dealRebackCode;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getSettingDesc() {
		return settingDesc;
	}

	public void setSettingDesc(String settingDesc) {
		this.settingDesc = settingDesc;
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

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

}
