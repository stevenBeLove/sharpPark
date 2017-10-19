package com.compass.pasmFee.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <pre>
 * 【类型】: PsmFee <br/> 
 * 【作用】: . <br/>  
 * 【时间】：2017年2月17日 上午10:22:18 <br/> 
 * 【作者】：chenbin yang <br/> 
 * </pre>
 */
public class QueryTradeProfit {
    /*
     *    机构名称
     */
    private String agency_Name;
    
    /*
     *    手机号
     */
    private String mobileNo;
    /*
     *    交易流水号
     */
    private String logNo;
    
    /*
     *    终端号
     */
    private String psamId;
    
    /*
     *    交易类型
    */
    private String business_type;
    
    /*
     *    交易名称
     */
    private String business_Name;
    
    /*
     *    交易日期
     */
    private String localDate;
    
    /*
     *    交易时间
     */
    private String localTime;
        
    /*
     *    交易金额
     */
    private String amount;
    
    /*
     *    手续费
     */
    private String  rtbFee;
    
    /*
     *    终端费率百分比
     */
    private String  feeRate;
    
    /*
     *    终端费率单笔
     */
    private String  fix_Fee;
    
    /*
     *    本级分润成本百分比
     */
    private String  cost_rate;
    
    /*
     *    本级分润成本单笔
     */
    private String cost_fix;
    
    /*
     *    下级分润成本百分比
     */
    private String next_FeeRate;
    
    /*
     *    下级分润成本单笔
     */
    private String next_FixFee;
    
    /*
     *    分润总金额
     */
    private String  fee_Amount;
    
    /*
     *    下级分润金额
     */
    private String  next_feeAmount;

    /*
     *    分润差
     */
    private String  difference_feeAmount;

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return agency_Name .<br/>
     */
    public String getAgency_Name() {
        return agency_Name;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  agency_Name 设置值.  <br/>
     */
    public void setAgency_Name(String agency_Name) {
        this.agency_Name = agency_Name;
    }
    
    /**
     * 
     * 方法名： getMobileNo.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月28日.<br/>
     * 创建时间：下午2:50:30.<br/>
     * 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：String.<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getMobileNo() {
        return mobileNo;
    }
    /**
     * 
     * 方法名： setMobileNo.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月28日.<br/>
     * 创建时间：下午2:50:46.<br/>
     * 参数者异常：@param mobileNo .<br/>
     * 返回值： @return 返回结果：void.<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return logNo .<br/>
     */
    public String getLogNo() {
        return logNo;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  logNo 设置值.  <br/>
     */
    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return psamId .<br/>
     */
    public String getPsamId() {
        return psamId;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  psamId 设置值.  <br/>
     */
    public void setPsamId(String psamId) {
        this.psamId = psamId;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return business_type .<br/>
     */
    public String getBusiness_type() {
        return business_type;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  business_type 设置值.  <br/>
     */
    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return business_Name .<br/>
     */
    public String getBusiness_Name() {
        return business_Name;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  business_Name 设置值.  <br/>
     */
    public void setBusiness_Name(String business_Name) {
        this.business_Name = business_Name;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return localDate .<br/>
     */
    public String getLocalDate() {
        return localDate;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  localDate 设置值.  <br/>
     */
    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return localTime .<br/>
     */
    public String getLocalTime() {
        return localTime;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  localTime 设置值.  <br/>
     */
    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return amount .<br/>
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  amount 设置值.  <br/>
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return rtbFee .<br/>
     */
    public String getRtbFee() {
        return rtbFee;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  rtbFee 设置值.  <br/>
     */
    public void setRtbFee(String rtbFee) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        this.rtbFee = rtbFee == null ? null : decimalFormat.format(Double.parseDouble(rtbFee));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return feeRate .<br/>
     */
    public String getFeeRate() {
        return feeRate;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  feeRate 设置值.  <br/>
     */
    public void setFeeRate(String feeRate) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.00000");
        this.feeRate = feeRate == null ? null : decimalFormat.format(Double.parseDouble(feeRate));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return fix_Fee .<br/>
     */
    public String getFix_Fee() {
        return fix_Fee;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  fix_Fee 设置值.  <br/>
     */
    public void setFix_Fee(String fix_Fee) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        this.fix_Fee = fix_Fee == null ? null : decimalFormat.format(Double.parseDouble(fix_Fee));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return cost_rate .<br/>
     */
    public String getCost_rate() {
        return cost_rate;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  cost_rate 设置值.  <br/>
     */
    public void setCost_rate(String cost_rate) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.0000");
        this.cost_rate = cost_rate == null ? null : decimalFormat.format(Double.parseDouble(cost_rate));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return cost_fix .<br/>
     */
    public String getCost_fix() {
        return cost_fix;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  cost_fix 设置值.  <br/>
     */
    public void setCost_fix(String cost_fix) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        this.cost_fix = cost_fix == null ? null : decimalFormat.format(Double.parseDouble(cost_fix));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return next_FeeRate .<br/>
     */
    public String getNext_FeeRate() {
        return next_FeeRate;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  next_FeeRate 设置值.  <br/>
     */
    public void setNext_FeeRate(String next_FeeRate) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.0000");
        this.next_FeeRate = next_FeeRate == null ? null : decimalFormat.format(Double.parseDouble(next_FeeRate));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return next_FixFee .<br/>
     */
    public String getNext_FixFee() {
        return next_FixFee;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  next_FixFee 设置值.  <br/>
     */
    public void setNext_FixFee(String next_FixFee) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        this.next_FixFee = next_FixFee == null ? null : decimalFormat.format(Double.parseDouble(next_FixFee));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return fee_Amount .<br/>
     */
    public String getFee_Amount() {
        return fee_Amount;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  fee_Amount 设置值.  <br/>
     */
    public void setFee_Amount(String fee_Amount) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.000");
        this.fee_Amount = fee_Amount == null ? null : decimalFormat.format(Double.parseDouble(fee_Amount));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return next_feeAmount .<br/>
     */
    public String getNext_feeAmount() {
        return next_feeAmount;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  next_feeAmount 设置值.  <br/>
     */
    public void setNext_feeAmount(String next_feeAmount) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.000");
        this.next_feeAmount = next_feeAmount == null ? null : decimalFormat.format(Double.parseDouble(next_feeAmount));;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：zhangyinghui <br/>
     * 返回类型：@return difference_feeAmount .<br/>
     */
    public String getDifference_feeAmount() {
        return difference_feeAmount;
    }

    /**
     * 创建人：yinghui zhang <br/>
     * 创建时间：2017年2月20日 下午7:16:46 <br/>
     * 参数: @param  difference_feeAmount 设置值.  <br/>
     */
    public void setDifference_feeAmount(String difference_feeAmount) {
        /***
         * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
         */
        DecimalFormat decimalFormat = new DecimalFormat("######0.000");
        this.difference_feeAmount = difference_feeAmount == null ? null : decimalFormat.format(Double.parseDouble(difference_feeAmount));;
    }

  
    
}
