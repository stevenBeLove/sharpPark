package com.compass.agency.model;

/**
 * <pre>
 * 【类型】: Payuser <br/> 
 * 【作用】:客户端用户. <br/>  
 * 【时间】：2017年2月22日 下午2:44:58 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class Payuser {

    /**
     * 用户id(手机号码)
     */
    private String userid;

    /**
     * 客户Id
     */
    private String customerid;

    /**
     * 信用等级
     */
    private String viplevel;
    
    /**
     * 审核状态
     */
    private String checkrange;
    
    /**
     * 冻结
     */
    private String memo;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getCheckrange() {
        return checkrange;
    }

    public void setCheckrange(String checkrange) {
        this.checkrange = checkrange;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    
}
