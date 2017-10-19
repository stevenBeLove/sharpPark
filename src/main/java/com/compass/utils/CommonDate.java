/**
 * 
 */
package com.compass.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhoubiao
 * 
 */
public class CommonDate {

    /** 年月日时分秒 */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    
    /***日期***/
    public static final String YYYYMMDD = "yyyyMMdd";
    
    /***时间***/
    public static final String HHMMSS = "HHmmss";
    
    /***日期时间类型***/
    public static final String DATETIME  = "yyyy-MM-dd HH:mm:ss";

	public static String getDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public static String getDateminite() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
		return sdf.format(d);
	}

	public static String getDateStr() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		return sdf.format(d);
	}

	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static String getNowDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(d);
	}

	public static String getCntDtStr(Date date, String format) {
		SimpleDateFormat dtFmt = new SimpleDateFormat(format);
		String nowDtStr = dtFmt.format(date);
		return nowDtStr;
	}

	public static String currentStr() {
		return getCntDtStr(new Date(), "yyyyMMddHHmmssSSS");
	}

    /**
     * 【方法名】 : (根据格式化字符串获取当前时间). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 上午11:54:20 .<br/>
     * 【参数】： .<br/>
     * @param format
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public static String currentDateWithFormatStr(String format) {
        return getCntDtStr(new Date(), format);
    }
	
    /**
     * 【方法名】    : (比较日期). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月19日 下午3:06:17 .<br/>
     * 【参数】： .<br/>
     * @param DATE1
     * @param DATE2
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                // System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 【方法名】    : (这里用一句话描述这个方法的作用). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月19日 下午3:10:58 .<br/>
     * 【参数】： .<br/>
     * @param dateTime1 日期1
     * @param dateTime2 日期2
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static int compareDateTime(String dateTime1, String dateTime2) {
        DateFormat df = new SimpleDateFormat(DATETIME);
        try {
            Date dt1 = df.parse(dateTime1);
            Date dt2 = df.parse(dateTime2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                // System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

	// 获取上个月的第一天
	public static String lastMonthFirstDate() {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 格式化对象
		Calendar calendar = Calendar.getInstance(); // 日历对象
		calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置当前月的日期
		calendar.add(Calendar.MONTH, -1); // 当前月份减一个月
		return sdf.format(calendar.getTime());

	}

	// 获取上个月的最后一天 
	public static String lastMonthLastDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 格式化对象
		Calendar calendar = Calendar.getInstance(); // 日历对象
		int month = calendar.get(Calendar.MONTH); // 获得当前月份
		calendar.set(Calendar.MONTH, month - 1); // 设置上一个月的月份
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 得到上个月的最后一天
		Date strDateTo = calendar.getTime();
		return sdf.format(strDateTo);
	}
	/**
	 * 
	 * 【方法名】    : (根据格式化字符串获取当前时间). <br/> 
	 * 【作者】: yinghui zhang .<br/>
	 * 【时间】： 2017年2月9日 上午10:20:56 .<br/>
	 * 【参数】： .<br/>
	 * @param formatStr 格式化字符串
	 * @return .<br/>
	 * <p>
	 * 修改记录.<br/>
	 * 修改人:  yinghui zhang 修改描述： .<br/>
	 * <p/>
	 */
	 public static String currentTimeWithFormat(String formatStr) {
	     return getCntDtStr(new Date(), formatStr);
	   }
	 
    /**
     * 【方法名】 : (是否是当月日期). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月23日 上午11:57:28 .<br/>
     * 【参数】： .<br/>
     * 
     * @param dateTime
     *            String
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     * @throws ParseException 
     */
    public static boolean isCurrentMonth(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        sdf.setLenient(true);
        Date date = sdf.parse(dateTime);
        SimpleDateFormat monthsdf = new SimpleDateFormat("yyyyMM");
        String month = monthsdf.format(date);
        Date nowDate = new Date();
        String nowMonth = monthsdf.format(nowDate);
        return month.equals(nowMonth);
    }
    
    public static void main(String[] args) {
       System.out.println(compareDateTime("2017-04-19 14:15:15","2017-04-13 01:00:00")); 
    }

}
