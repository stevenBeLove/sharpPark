package com.compass.agencyCost.model;

import java.math.BigDecimal;

public class AgencyCostLog {
    /**
     * 序号
     */
    private String fid;

    /**
     * 机构成本表序号
     */
    private String id;

    /**
     * 机构编号
     */
    private String agencyId;

    /**
     * 机构成本费率
     */
    private BigDecimal costRate;

    /**
     * 机构成本固定值
     */
    private BigDecimal costFix;

    /**
     * 生效时间
     */
    private String createdt;

    /**
     * 入库时间
     */
    private String indate;

    /**
     * 产品类型编码
     */
    private String businessType;

    /**
     * 创建时间
     */
    private String operdt;

    /**
     * 创建人
     */
    private String oper;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid == null ? null : fid.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId == null ? null : agencyId.trim();
    }

    public BigDecimal getCostRate() {
        return costRate;
    }

    public void setCostRate(BigDecimal costRate) {
        this.costRate = costRate;
    }

    public BigDecimal getCostFix() {
        return costFix;
    }

    public void setCostFix(BigDecimal costFix) {
        this.costFix = costFix;
    }

    public String getCreatedt() {
        return createdt;
    }

    public void setCreatedt(String createdt) {
        this.createdt = createdt == null ? null : createdt.trim();
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate == null ? null : indate.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getOperdt() {
        return operdt;
    }

    public void setOperdt(String operdt) {
        this.operdt = operdt == null ? null : operdt.trim();
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper == null ? null : oper.trim();
    }
}