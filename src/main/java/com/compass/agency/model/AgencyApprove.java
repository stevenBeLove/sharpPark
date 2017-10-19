package com.compass.agency.model;

/**
 * <pre>
 * 【类型】: AgencyApprove <br/> 
 * 【作用】: 认证通过的平台用户. <br/>  
 * 【时间】：2017年2月21日 下午4:01:58 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class AgencyApprove {
    
    /**
     * 代理商Id
     */
    private String agencyId;

    /**
     * 手机号码
     */
    private String mobileno;

    /**
     * 姓名
     */
    private String username;

    /**
     * 身份证号
     */
    private String customerpid;

    /**
     * 银行卡号
     */
    private String accountno;

    /**
     * USERID,关联users表
     */
    private String userid;

    /**
     * 认证时间
     */
    private String approvedt;

    /**
     * 认证状态0未通过1通过认证
     */
    private String status;

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId == null ? null : agencyId.trim();
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno == null ? null : mobileno.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getCustomerpid() {
        return customerpid;
    }

    public void setCustomerpid(String customerpid) {
        this.customerpid = customerpid == null ? null : customerpid.trim();
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno == null ? null : accountno.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getApprovedt() {
        return approvedt;
    }

    public void setApprovedt(String approvedt) {
        this.approvedt = approvedt == null ? null : approvedt.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}