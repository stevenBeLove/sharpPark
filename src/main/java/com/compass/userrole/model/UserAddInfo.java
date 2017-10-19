/** 
 * 包名: package com.compass.userrole.model; <br/> 
 * 添加时间: 2017年4月5日 下午3:59:13 <br/> 
 */
package com.compass.userrole.model;

import java.math.BigDecimal;

/** 
 * 类名: UserAddInfo <br/> 
 * 作用：用户附加信息<br/> 
 * 创建者: zhangyinghui. <br/> 
 * 添加时间: 2017年4月5日 下午3:59:13 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class UserAddInfo {
    

    /**
     * 用户id
     */
    private String homeId;

    /**
     * 首页登录类型id，关联RTB_CLIENT_HOMEPAGE
     */
    private String userid;
    
    /**
     * 验证码时间
     */
    private String usercodeTime;

    /**
     * 是否认证成功：1成功0失败
     */
    private String isApprove;
    
    /**
     * 天数
     */
    private Integer days;

    
    /**
     * 是否通过认证
     */
    public static final String APPROVE = "1";
    
    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsercodeTime() {
        return usercodeTime;
    }

    public void setUsercodeTime(String usercodeTime) {
        this.usercodeTime = usercodeTime;
    }

    public String getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(String isApprove) {
        this.isApprove = isApprove;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
    
    
}

