/** 
 * 包名: package com.compass.agency.model; <br/> 
 * 添加时间: 2017年4月12日 下午9:23:26 <br/> 
 */
package com.compass.agency.model;
/** 
 * 类名: AgencySetup <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class AgencySetup {
    /**
     * 代理商Id
     */
    private String agencyId;

    /**
     * 注册日期
     */
    private String setDate;

    /**
     * 注册时间
     */
    private String setTime;

    /**
     * 注册URL
     */
    private String registerUrl;

    /**
     * 激活状态：1已使用0未使用
     */
    private String status;

    /**
     * 机构号唯一加密串
     */
    private String agencymac;

    /**
     * 是否发送短信：0未激活未发送1未激活已发送
     */
    private String isNote;

    /**
     * 激活时间
     */
    private String actTime;

    /**
     * 无成本发送短信:0未设成本未发短信1未设成本已发短信
     */
    private String isNoteParent;

    /**
     * 是否发送提醒登陆短信：0没登陆未发送1没登陆已发送
     */
    private String isNoteLogin;

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgencymac() {
        return agencymac;
    }

    public void setAgencymac(String agencymac) {
        this.agencymac = agencymac;
    }

    public String getIsNote() {
        return isNote;
    }

    public void setIsNote(String isNote) {
        this.isNote = isNote;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getIsNoteParent() {
        return isNoteParent;
    }

    public void setIsNoteParent(String isNoteParent) {
        this.isNoteParent = isNoteParent;
    }

    public String getIsNoteLogin() {
        return isNoteLogin;
    }

    public void setIsNoteLogin(String isNoteLogin) {
        this.isNoteLogin = isNoteLogin;
    }
    
    
    
}

