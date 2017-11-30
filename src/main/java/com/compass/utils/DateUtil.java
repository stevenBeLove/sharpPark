package com.compass.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	public static Date fromatDate(String date,String format) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(date);
	}
	
	public static String toDay(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(Calendar.getInstance().getTime());
	}
	
	//昨天
	public static String yesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(5, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal
				.getTime());
		return yesterday;
	}

	//上周一
	public static String lastWeekFirst() {
		int weeks = -1;
		int mondayPlus = lastWeekDay();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(5, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	private static int lastWeekDay() {
		Calendar cd = Calendar.getInstance();
		int dayOfWeek = cd.get(7) - 1;
		if (dayOfWeek == 1) {
			return 0;
		}
		return (1 - dayOfWeek);
	}

	//上周周日
	public static String lastWeekEnd() {
		int weeks = -1;
		int mondayPlus = lastWeekDay();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(5, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	//上月初
	public static String lastMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(5, 1);
		lastDate.add(2, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	//上月末
	public static String lastMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(2, -1);
		lastDate.set(5, 1);
		lastDate.roll(5, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}
}
