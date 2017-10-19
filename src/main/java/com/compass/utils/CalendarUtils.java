package com.compass.utils;

import java.util.Calendar;

/**
 * @author wangLong
 * 时间片的毫秒数常量类
 */
public final class CalendarUtils {
  public static final int MILLISECOND = 1;
  public static final int SECOND = 1000 * MILLISECOND;
  public static final int MINUTE = 60 * SECOND;
  public static final int HOUR = 60 * MINUTE;
  public static final int DAY = 24 * HOUR;
  public static final int WEEK = 7 * DAY;

  /**
   * 获取当前日期的Calendar对象，时间设置为00:00:00
   */
  public static Calendar getCurrDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 获取最大时间
   * @return
   */
  public static Calendar getMaxDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2999);
    calendar.set(Calendar.MONTH, 11);
    calendar.set(Calendar.DAY_OF_MONTH, 31);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar;
  }

  /**
   * 转换为每天的00:00:00.000，源对象不变，创建新对象
   */
  public static Calendar convToDayStart(final Calendar day) {
    if(day == null) return null;
    Calendar calendar = (Calendar) day.clone();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 转换为每天的23:59:59.0，源对象不变，创建新对象
   */
  public static Calendar convToDayEnd(final Calendar day) {
    if(day == null) return null;
    Calendar calendar = (Calendar) day.clone();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;

  }

  /**
   * 转换为小时的00:00.000，源对象不变，创建新对象
   */
  public static Calendar convToHourStart(final Calendar hour) {
    if(hour == null) return null;
    Calendar calendar = (Calendar) hour.clone();
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 转换为每小时的59:59.0，源对象不变，创建新对象
   */
  public static Calendar convToHourEnd(final Calendar hour) {
    if(hour == null) return null;
    Calendar calendar = (Calendar) hour.clone();
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 转换为当月第一天的00:00:00.000，源对象不变，创建新对象
   */
  public static Calendar convToMonthStart(final Calendar month) {
    if(month == null) return null;
    Calendar calendar = (Calendar) month.clone();
    calendar.set(Calendar.DAY_OF_MONTH, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 转换为当月最后一天的23:59:59.0，源对象不变，创建新对象
   */
  public static Calendar convToMonthEnd(final Calendar month) {
    if(month == null) return null;
    Calendar calendar = (Calendar) month.clone();
    calendar.set(Calendar.DAY_OF_MONTH, getDaysInMonth(month));
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * 获取输入的月份中包含的天数
   * @param date
   */
  public static int getDaysInMonth(Calendar date) {
    int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    if(date == null) return 0;
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);

    if(month == Calendar.FEBRUARY) {
      if(year % 4 != 0) return 28;
      if(year % 100 != 0) return 29;
      if(year % 400 == 0) return 29;
      return 28;
    } else {
      return days[month];
    }
  }

  /**
   * 返回输入的时间加一小时
   * @param hour 输入时间
   * @return 加一小时后的时间
   */
  public static Calendar incHour(Calendar hour) {
    if(hour == null) return null;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(hour.getTimeInMillis() + HOUR);
    return calendar;
  }

  /**
   * 返回输入的时间加一天
   * @param hour 输入时间
   * @return 加一天的时间
   */
  public static Calendar incDay(Calendar hour) {
    if(hour == null) return null;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(hour.getTimeInMillis() + DAY);
    return calendar;
  }
}
