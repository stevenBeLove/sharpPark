package com.compass.filter;

/**
 * <pre>
 * 【类型】: ThreadLocalContext <br/> 
 * 【作用】: 类说明 ThreadLocal工具类 用于各层之间传递参数. <br/>  
 * 【时间】：2017年3月2日 上午11:17:29 <br/> 
 * 【作者】：yinghui zhang <br/>
 * </pre>
 */

public class ThreadLocalContext {

    /**
     * 分页开始
     */
    private static ThreadLocal<Integer> startValue = new ThreadLocal<Integer>();

    /**
     * 分页结束
     */
    private static ThreadLocal<Integer> endValue = new ThreadLocal<Integer>();

    
    
    public static Integer getStartValue() {
        return startValue.get();
    }

    public static void setStartValue(Integer start) {
        startValue.set(start);
    }

    public static Integer getEndValue() {
        return endValue.get();
    }

    public static void setEndValue(Integer end) {
       endValue.set(end);
    }






}
