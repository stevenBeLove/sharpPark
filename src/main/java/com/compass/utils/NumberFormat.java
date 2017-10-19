/** 
 * 包名: package com.qt.sales.common; <br/> 
 * 添加时间: 2016年11月10日 下午7:19:43 <br/> 
 */
package com.compass.utils;

import java.text.DecimalFormat;

import org.springframework.util.StringUtils;



/**
 * 类名: NumberFormat <br/>
 * 作用：TODO(数字格式化)<br/>
 * 创建者: zhangyinghui. <br/>
 * 添加时间: 2016年11月10日 下午7:19:43 <br/>
 * 版本： JDK 1.6 SalesPlatform 1.0
 */
public class NumberFormat {

    /**
     * 除100
     */
    public static double DEVIDE100 = 0.01;
    
    /**
     * 乘100
     */
    public static double MULTIPLY100 = 100;
    
    /**
     * 
     * 【方法名】    : (数字Format). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年11月10日 下午7:20:47 .<br/>
     * 【参数】： .<br/>
     * @param number 数字
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String parseNumberFormat(String number, double n){
        if (StringUtils.isEmpty(number)){
            return "0.00";
        }
        double x = Double.parseDouble(number) * n;
        DecimalFormat df;
        if (x > 0){
            df = new DecimalFormat("###,###.000");
        } else {
            df = new DecimalFormat("###,##0.000");
        }
        String xs = df.format(x);
        if (xs.startsWith(".")) {
            xs = "0" + xs;
        }
        return xs;
    }
    
    /**
     * 
     * 【方法名】    : (数字Format). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年11月10日 下午7:20:47 .<br/>
     * 【参数】： .<br/>
     * @param number 数字
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String parse2NumberFormat(String number, double n){
        if (StringUtils.isEmpty(number)){
            return "0.00";
        }
        double x = Double.parseDouble(number) * n;
        DecimalFormat df;
        if (x > 0){
            df = new DecimalFormat("###,###.00");
        } else {
            df = new DecimalFormat("###,##0.00");
        }
        String xs = df.format(x);
        if (xs.startsWith(".")) {
            xs = "0" + xs;
        }
        return xs;
    }
    /**
     * 
     * 【方法名】    : (元Format). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年11月10日 下午7:20:47 .<br/>
     * 【参数】： .<br/>
     * @param number 数字
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String parseYuanFormat(String number) {
        if (StringUtils.isEmpty(number)){
            return "0.00";
        }
        double x = Double.parseDouble(number);
        DecimalFormat df = new DecimalFormat("###,###.00");
        String xs = df.format(x);
        if (xs.startsWith(".")) {
            xs = "0" + xs;
        }
        return xs;
    }
    /**
     * 
     * 【方法名】    : (数字Format). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年11月10日 下午7:23:06 .<br/>
     * 【参数】： .<br/>
     * @param number Long 
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String parseNumberFormat(Long number) {
        double x = Double.parseDouble(number.toString()) * 0.01;
        DecimalFormat df = new DecimalFormat("###,###.00");
        String xs = df.format(x);
        if (xs.startsWith(".")) {
            xs = "0" + xs;
        }
        return xs;
    }
}
