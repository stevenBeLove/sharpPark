package com.compass.common;

/**
 * <pre>
 * 【类型】: Constants <br/> 
 * 【作用】: 常量池. <br/>  
 * 【时间】：2017年1月13日 下午8:01:49 <br/> 
 * 【作者】：yinghui zhang <br/>
 * </pre>
 */
public class FrConstants {

    /**
     * 按金额
     */
    public static final String splittingModel        = "2";

    /**
     * 代理商成本费率最小值
     */
    public static final String costRateMin           = "AGENCY001";

    /**
     * 代理商成本费率最大值
     */
    public static final String costRateMax           = "AGENCY002";

    /**
     * 代理商成本固定值最小值（元）
     */
    public static final String costFixMin            = "AGENCY003";

    /**
     * 代理商成本固定值最大值（元）
     */
    public static final String costFixMax            = "AGENCY004";

    /**
     * 待生效
     */
    public static final String waitUse               = "0";

    /**
     * 生效中
     */
    public static final String isUse                 = "1";

    /** 响应码 */
    public static final String RESP_CODE_15_ERR_HOST = "0015";               // 主机异常

    /**
     * 闪付交易类型
     */
    public static final String BUSINESSTYPE_3001     = "3001";

    /**
     * 普通到账类型
     */
    public static final String BUSINESSTYPE_3002     = "3002";

    /**
     * 二维码微信类型
     */
    public static final String BUSINESSTYPE_3003     = "3003";

    /**
     * 二维码支付宝类型
     */
    public static final String BUSINESSTYPE_3004     = "3004";
    
    /**
     * 云闪付类型
     */
    public static final String BUSINESSTYPE_3006     = "3006";

    /**
     * 瑞通宝二期上线时间
     */
    public static final String ONLINETIME            = "2017-04-13 01:00:00";
    /** 短信模板配置开始 **/
    /**
     * 短信模板key
     */
    public static final String SMSPARAM              = "SMSPARAM";
    /**
     * 短信模板value1
     */
    public static final String RTBPLATFORM_01        = "RTBPLATFORM_01";
    /**
     * 短信模板value1(终端费率修改信息模板)
     */
    public static final String RTBPLATFORM_02        = "RTBPLATFORM_RATE_UPDATE";
    /**
     * 当前时间
     */
    public static final String NOWDATE               = "NOWDATE";
    /**
     * APPUSER
     */
    public static final String APPUSER               = "APPUSER";
    /**
     * 手机号码
     */
    public static final String MOBILENO              = "MOBILENO";
    /**
     * 客户号
     */
    public static final String CUSTOMERID            = "CUSTOMERID";
    /**
     * 发送类型
     */
    public static final String TIMETYPE              = "TIMETYPE";
    /**
     * 实时发送
     */
    public static final String ONTIME                = "1";
    /**
     * 来源系统
     */
    public static final String SOURCECHANNAL         = "SOURCECHANNAL";
    /**
     * 上级机构编号
     */
    public static final String ONLINECHANNEL         = "onlineChannel";
    /**
     * 当前机构编号
     */
    public static final String AGENCYID              = "agencyId";
    /**
     * 瑞通宝app
     */
    public static final String RUITONGBAO            = "ruitongbao";
    /**
     * 当前系统名
     */
    public static final String RTBPLATFORM           = "RtbPlatForm";
    /**
     * branchid
     */
    public static final String BRANCHID              = "BRANCHID";
    /**
     * 机构号
     */
    public static final String BRANCHID_NUM          = "00800653";
    /**
     * 空字符串
     */
    public static final String NULL                  = "";
    /**
     * 回拨终端数量
     */
    public static final String BACK_COUNT            = "backCount";
    
    public static final String PSAMID               ="PSAMID";
}
