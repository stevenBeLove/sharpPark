package com.compass.pasmFee.model;

import java.math.BigDecimal;

import com.compass.utils.NumberFormat;

public class TradeProfit {
    /**
     * 
     */
    private BigDecimal frid;

    /**
     * 服务编码
     */
    private String servcode;

    /**
     * 交易日期
     */
    private String localdate;

    /**
     * 交易时间
     */
    private String localtime;

    /**
     * 交易号
     */
    private String locallogno;

    /**
     * 商户类型
     */
    private String merchantid;

    /**
     * 产品ID
     */
    private String productid;

    /**
     * 订单号
     */
    private String orderid;

    /**
     * 产品类型编码
     */
    private String businessType;

    /**
     * 产品类型名称
     */
    private String businessName;
    /**
     * 交易终端号
     */
    private String termid;

    /**
     * 用户PSAM卡号
     */
    private String psamid;

    /**
     * 交易金额
     */
    private String amount;

    /**
     * 交易金额所属分销商ID
     */
    private String agencyId;
    
    /**
     * 交易金额所属分销商名称
     */
    private String agencyName;

    /**
     * 代理商ID
     */
    private String superAgencyId;
    
    /**
     * 代理商名称
     */
    private String superAgencyName;

    /**
     * 终端费率
     */
    private String shopno;

    /**
     * 刷卡费率
     */
    private String feerate;

    /**
     * 终端固定手续费
     */
    private String fixFee;

    /**
     * 代理商成本费率
     */
    private String costRate;

    /**
     * 代理商成本固定值
     */
    private String costFix;

    /**
     * 与上级机构相差的费率
     */
    private String differRate;

    /**
     * 与上级代理商相差固定值
     */
    private String differCostFix;
    
    /**
     * 分润差金额
     */
    private String differAmount;

    /**
     * 分润差
     */
    private String frAmount;

    /**
     * 手续费
     */
    private String rtbfee;

    /**
     * 交易状态00 成功   BB退款   AA失败
     */
    private String status;
    
    /**
     * 直属下级分润比例
     */
    private String childCostRate;
    /**
     * 直属下级分润固定值
     */
    private String childCostFix;
    
    /**
     * 直属下级分润金额
     */
    private String childAmount;
    
    /**
     * 分润总金额
     */
    private String sumAmount;
    
    /**
     * 下级机构号
     */
    private String childAgencyId;
    
    /**
     * 手机号
     */
    private String mobileNo;
    
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BigDecimal getFrid() {
        return frid;
    }

    public void setFrid(BigDecimal frid) {
        this.frid = frid;
    }

    public String getServcode() {
        return servcode;
    }

    public void setServcode(String servcode) {
        this.servcode = servcode;
    }

    public String getLocaldate() {
        return localdate;
    }

    public void setLocaldate(String localdate) {
        this.localdate = localdate;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getLocallogno() {
        return locallogno;
    }

    public void setLocallogno(String locallogno) {
        this.locallogno = locallogno;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTermid() {
        return termid;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public String getPsamid() {
        return psamid;
    }

    public void setPsamid(String psamid) {
        this.psamid = psamid;
    }

    public String getAmount() {
        return NumberFormat.parse2NumberFormat(amount, NumberFormat.DEVIDE100);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getSuperAgencyId() {
        return superAgencyId;
    }

    public void setSuperAgencyId(String superAgencyId) {
        this.superAgencyId = superAgencyId;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public String getFeerate() {
        return NumberFormat.parseNumberFormat(feerate, NumberFormat.MULTIPLY100);
    }

    public void setFeerate(String feerate) {
        this.feerate = feerate;
    }

    public String getFixFee() {
        return NumberFormat.parse2NumberFormat(fixFee, NumberFormat.DEVIDE100);
    }

    public void setFixFee(String fixFee) {
        this.fixFee = fixFee;
    }

    public String getCostRate() {
        return NumberFormat.parseNumberFormat(costRate, NumberFormat.MULTIPLY100);
    }

    public void setCostRate(String costRate) {
        this.costRate = costRate;
    }

    public String getCostFix() {
        return NumberFormat.parse2NumberFormat(costFix, NumberFormat.DEVIDE100);
    }

    public void setCostFix(String costFix) {
        this.costFix = costFix;
    }

    public String getDifferRate() {
        return NumberFormat.parseNumberFormat(differRate, NumberFormat.MULTIPLY100);
    }

    public void setDifferRate(String differRate) {
        this.differRate = differRate;
    }

    public String getDifferCostFix() {
        return NumberFormat.parseNumberFormat(differCostFix, NumberFormat.DEVIDE100);
    }

    public void setDifferCostFix(String differCostFix) {
        this.differCostFix = differCostFix;
    }

    public String getFrAmount() {
        return NumberFormat.parse2NumberFormat(frAmount, NumberFormat.DEVIDE100);
    }

    public void setFrAmount(String frAmount) {
        this.frAmount = frAmount;
    }

    public String getRtbfee() {
        return NumberFormat.parse2NumberFormat(rtbfee, NumberFormat.DEVIDE100);
    }

    public void setRtbfee(String rtbfee) {
        this.rtbfee = rtbfee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getSuperAgencyName() {
        return superAgencyName;
    }

    public void setSuperAgencyName(String superAgencyName) {
        this.superAgencyName = superAgencyName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDifferAmount() {
        return NumberFormat.parse2NumberFormat(differAmount, NumberFormat.DEVIDE100);
    }

    public void setDifferAmount(String differAmount) {
        this.differAmount = differAmount;
    }

    public String getChildCostRate() {
        return NumberFormat.parseNumberFormat(childCostRate,NumberFormat.MULTIPLY100);
    }

    public void setChildCostRate(String childCostRate) {
        this.childCostRate = childCostRate;
    }

    public String getChildCostFix() {
        return NumberFormat.parse2NumberFormat(childCostFix,NumberFormat.DEVIDE100);
    }

    public void setChildCostFix(String childCostFix) {
        this.childCostFix = childCostFix;
    }

    public String getChildAmount() {
        return NumberFormat.parse2NumberFormat(childAmount,NumberFormat.DEVIDE100);
    }

    public void setChildAmount(String childAmount) {
        this.childAmount = childAmount;
    }

    public String getSumAmount() {
        return NumberFormat.parse2NumberFormat(sumAmount,NumberFormat.DEVIDE100);
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getChildAgencyId() {
        return childAgencyId;
    }

    public void setChildAgencyId(String childAgencyId) {
        this.childAgencyId = childAgencyId;
    }
    
}
