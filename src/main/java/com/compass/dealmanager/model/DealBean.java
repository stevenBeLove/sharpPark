package com.compass.dealmanager.model;

/**
 * 
 * @author gaoyang 交易实体类
 * 
 */
public class DealBean {
	private String onlyCode; // 唯一编号
	private String sysSource; // 来源系统
	private String dealId;// 交易编号
	private String serialNumber;// 流水号
	private Double transacount;// 交易金额
	private Double transcost;//手续费
	private String terminalId;// 终端编号
	private String deal_data;// 交易日期
	private String deal_time;// 交易时间
	private String deal_status;// 交易状态
	private String deal_statusStr;
	private String dealtype_id;// 交易类型编号
	private String dealTypeName;// 交易类型名称
	private String dealdesc;// 交易描述
	private String dealrebackcode;// 交易返回码
	private Double charge;// 交易成本
	private Double cost;// 可分润金额
	private String bankcardNumber;//银行卡号
	private String createId;// 创建人编号
	private String createDt;// 创建时间
	private String agencyName;  //机构编号
	private String merchantCode;  //商户号
	private String terminal_OnlyCode; //终端唯一号
	private String splitAmont;
	private String feeAmt; //费率
	private String moblieNo; //手机号码
	private String customerName; //客户姓名 
	private String idcard;       //身份证号

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getOnlyCode() {
		return onlyCode;
	}

	public void setOnlyCode(String onlyCode) {
		this.onlyCode = onlyCode;
	}

	public String getSysSource() {
		return sysSource;
	}

	public void setSysSource(String sysSource) {
		this.sysSource = sysSource;
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

	public Double getTransacount() {
		return transacount;
	}

	public void setTransacount(Double transacount) {
		this.transacount = transacount;
	}

	public void setTranscost(Double transcost) {
		this.transcost = transcost;
	}

	public Double getTranscost() {
		return transcost;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getDeal_data() {
		return deal_data;
	}

	public void setDeal_data(String deal_data) {
		this.deal_data = deal_data;
	}

	public String getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}

	public String getDeal_status() {
		return deal_status;
	}

	public void setDeal_status(String deal_status) {
		this.deal_status = deal_status;
	}

	public String getDeal_statusStr() {
		return deal_statusStr;
	}

	public void setDeal_statusStr(String deal_statusStr) {
		this.deal_statusStr = deal_statusStr;
	}

	public String getDealtype_id() {
		return dealtype_id;
	}

	public void setDealtype_id(String dealtype_id) {
		this.dealtype_id = dealtype_id;
	}

	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}

	public String getDealTypeName() {
		return dealTypeName;
	}

	public String getDealdesc() {
		return dealdesc;
	}

	public void setDealdesc(String dealdesc) {
		this.dealdesc = dealdesc;
	}

	public String getDealrebackcode() {
		return dealrebackcode;
	}

	public void setDealrebackcode(String dealrebackcode) {
		this.dealrebackcode = dealrebackcode;
	}

	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public void setBankcardNumber(String bankcardNumber) {
		this.bankcardNumber = bankcardNumber;
	}

	public String getBankcardNumber() {
		return bankcardNumber;
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

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getTerminal_OnlyCode() {
		return terminal_OnlyCode;
	}

	public void setTerminal_OnlyCode(String terminal_OnlyCode) {
		this.terminal_OnlyCode = terminal_OnlyCode;
	}

	public String getSplitAmont() {
		return splitAmont;
	}

	public void setSplitAmont(String splitAmont) {
		this.splitAmont = splitAmont;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
 
	public String getMoblieNo() {
		return moblieNo;
	}

	public void setMoblieNo(String moblieNo) {
		this.moblieNo = moblieNo;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
}
