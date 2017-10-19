package com.compass.dataManage.model;

public class FrunFeeBean {
    /** 代理商机构号 */
    private String prepbranch;
    /** 来源系统 */
    private String systemid;
    /** APP机构号 */
    private String branchid;
    /** 交易类型,3：支付方式A，4：支付方式B,5：支付方式C，6:支付方式D，7：T+0提现 */
    private String dealtype;
    /** 交易类型名 */
    private String feename;
    /** 费率编号 */
    private String shopno;
    /** 费率成本 */
    private String fee;
    /** 0：点数;1：封不;2:封封 */
    private String feestatus;
    /** 0:停用,1:启用 */
    private String status;

    private String systemname;

    private String dtname;

    private String feeststusstr;

    private String statusstr;  
    
    private String  branchname;

    public String getPrepbranch() {
        return prepbranch;
    }

    public void setPrepbranch(String prepbranch) {
        this.prepbranch = prepbranch;
    }

    public String getSystemid() {
        return systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getDealtype() {
        return dealtype;
    }

    public void setDealtype(String dealtype) {
        this.dealtype = dealtype;
    }

    public String getFeename() {
        return feename;
    }

    public void setFeename(String feename) {
        this.feename = feename;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFeestatus() {
        return feestatus;
    }

    public void setFeestatus(String feestatus) {
        this.feestatus = feestatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }

    public String getDtname() {
        return dtname;
    }

    public void setDtname(String dtname) {
        this.dtname = dtname;
    }

    public String getFeeststusstr() {
        return feeststusstr;
    }

    public void setFeeststusstr(String feeststusstr) {
        this.feeststusstr = feeststusstr;
    }

    public String getStatusstr() {
        return statusstr;
    }

    public void setStatusstr(String statusstr) {
        this.statusstr = statusstr;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }
    
}
