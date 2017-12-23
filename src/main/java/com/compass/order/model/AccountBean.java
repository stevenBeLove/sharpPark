package com.compass.order.model;

/**
 * 对账
 * @author lenovo
 *
 */
public class AccountBean {
	private String payType; //1：现金 2：支付宝
	private String payTypeName;
	private Integer totalCount;//交易笔数
	private String transactionAmount;//交易金额
	private String refundCount;//退款笔数
	private String refundAmount;//退款金额
	private String merchantDiscount;//商户优惠
	private String cloudDiscount;//云停风行优惠
	private String actualAmount;//实收金额
	private String freeAmount;//手续费
	private String settAmount;//结算金额
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getRefundCount() {
		return refundCount;
	}
	public void setRefundCount(String refundCount) {
		this.refundCount = refundCount;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getMerchantDiscount() {
		return merchantDiscount;
	}
	public void setMerchantDiscount(String merchantDiscount) {
		this.merchantDiscount = merchantDiscount;
	}
	public String getCloudDiscount() {
		return cloudDiscount;
	}
	public void setCloudDiscount(String cloudDiscount) {
		this.cloudDiscount = cloudDiscount;
	}
	public String getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getFreeAmount() {
		return freeAmount;
	}
	public void setFreeAmount(String freeAmount) {
		this.freeAmount = freeAmount;
	}
	public String getSettAmount() {
		return settAmount;
	}
	public void setSettAmount(String settAmount) {
		this.settAmount = settAmount;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
}
