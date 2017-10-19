/** 
 * 包名: package com.compass.paramater.model; <br/> 
 * 添加时间: 2017年4月5日 下午5:09:18 <br/> 
 */
package com.compass.paramater.model;

import java.math.BigDecimal;

/** 
 * 类名: RtbParamter <br/> 
 * 创建者: zhangyinghui. <br/> 
 * 添加时间: 2017年4月5日 下午5:09:18 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class RtbParamter {

    /**
     * 参数编号
     */
    private String pmNo;
    
    /**
     * 参数值
     */
    private BigDecimal pmValue;

    /**
     * 参数含义
     */
    private String pmComment;

    /**
     * 大类编号
     */
    private String classno;

    /**
     * 瑞源宝-瑞通宝交易类型显示开关：1开0关
     */
    private String onOff;
    
    /**
     * 状态：1有效0无效
     */
    private String status;

    public String getPmNo() {
        return pmNo;
    }

    public void setPmNo(String pmNo) {
        this.pmNo = pmNo;
    }

    public BigDecimal getPmValue() {
        return pmValue;
    }

    public void setPmValue(BigDecimal pmValue) {
        this.pmValue = pmValue;
    }

    public String getPmComment() {
        return pmComment;
    }

    public void setPmComment(String pmComment) {
        this.pmComment = pmComment;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    public String getOnOff() {
        return onOff;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

