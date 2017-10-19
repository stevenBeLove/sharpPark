package com.compass.agencyCost.model;

import java.math.BigDecimal;

/**
 * <pre>
 * 【类型】: AgencyCost <br/> 
 * 【作用】: 机构成本表. <br/>  
 * 【时间】：2017年2月14日 下午4:21:23 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class AgencyCost {
    /**
     * 主键
     */
    private String id;

    /**
     * 机构id
     */
    private String agencyId;
    
    /**
     * 机构名称
     */
    private String agencyName;

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
     * 产品类型编码
     */
    private String businessType;
    
    /**
     * 产品类型编码名称
     */
    private String businessTypeName;

    /**
     * 创建时间
     */
    private String operdt;

    /**
     * 创建人
     */
    private String oper;
    
    /**
     * 创建人名字
     */
    private String operName;
    
    /**
     * 数据状态
     */
    private String status;
    
    /**
     * 更新次数
     */
    private Integer updateTimes;

    
    
    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(Integer updateTimes) {
        this.updateTimes = updateTimes;
    }
    
    
    
    
}