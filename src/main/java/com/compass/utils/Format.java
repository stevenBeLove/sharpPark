package com.compass.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Format {

	/**
	 * string类型当前日期
	 * 
	 * @return
	 */
	public static String formatDate() {
		String sdate = time();
		return sdate.substring(0, 8);
	}

	/**
	 * String类型当前时间6位
	 * 
	 * @return
	 */
	public static String formatTime() {
		String sdate = time();
		return sdate.substring(8, 14);
	}

	/**
	 * String类型当前时间14位
	 * 
	 * @return
	 */
	public static String time() {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		String sdate = sd.format(date);
		return sdate;
	}
	
	public static byte[] base642Bytes(String base64String) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(base64String);
	}

	public static String bytes2Base64(byte[] bytes) throws IOException{
		 BASE64Encoder encoder = new BASE64Encoder();
		return  encoder.encode(bytes);
	}
	 
	/**
	 * String类型 6位随机码
	 * @return
	 */
	public static String getRandom(){		
		StringBuffer random=new StringBuffer();
		Random r=new Random(); 
		for(int i=0; i<6; i++){
			random.append(r.nextInt(9));
		}
		return random.toString();
	}
	/**
	 * String类型 8位随机码
	 * @return
	 */
	public static String getRandom8(){		
		StringBuffer random=new StringBuffer();
		Random r=new Random(); 
		for(int i=0; i<8; i++){
			random.append(r.nextInt(9));
		}
		return random.toString();
	}
	/**
	 * String 元转分
	 * @param amount
	 * @return
	 */
	public static String ytf(String amount){
		
		DecimalFormat df = new DecimalFormat("0");
		double temp = Double.valueOf(amount);
		temp = temp*100;
		return df.format(temp);		
	}
	
	/**
	 * String 分转元
	 * @param amount
	 * @return
	 */
	public static String fty(String amount){
		
		DecimalFormat df = new DecimalFormat("0.00");
		double temp = Double.valueOf(amount);
		temp = temp/100;
		return df.format(temp);		
	}
	
	/**
	 * Long 分转元
	 * @param amount
	 * @return
	 */
	public static Long fty(Long amount){
		
		amount = amount/100;
		return Long.valueOf(String.valueOf(amount));		
	}
	/**
	 * 左填0
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String addLeftZero(String s, int length) {
		// StringBuilder sb=new StringBuilder();
		int old = s.length();
		if (length > old) {
			char[] c = new char[length];
			char[] x = s.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: " + s
								+ " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		return s.substring(0, length);

	}
	
	
	 
 
	/**
	 * 分转换为元
	 * 
	 * @param fen
	 * 
	 * @return
	 */
	public static String fromFenToYuan(String fen) {
		String yuan = "";
		// 左去0
		int MULTIPLIER = 100;
		Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
		Matcher matcher = pattern.matcher(fen);
		if (matcher.matches()) {
			yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
		}else if("0".equals(fen)){
			yuan="0.00";
		} else {

		}
		return yuan;
	}
	
	/**
	 * 分转换为元
	 * 
	 * @param fen
	 * 
	 * @return
	 */
	public static Double fromFenToYuanDouble(String fen) {
		Double yuan =  0d ;
		// 左去0
		int MULTIPLIER = 100;
		Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
		Matcher matcher = pattern.matcher(fen);
		if (matcher.matches()) {
			BigDecimal bd= new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2);
			yuan =bd.doubleValue();
		}  else {

		}
		return yuan;
	}

	public static String cardIdFormat(String cardId){
		StringBuffer sb=new StringBuffer();
		sb.append(cardId.substring(0, 4));
		sb.append(" ");
		sb.append(cardId.substring(4, 6));
		sb.append("**");
		sb.append(" ");
		sb.append("**** ");
		sb.append(cardId.substring(cardId.length()-4, cardId.length()));
		return sb.toString();
	}
	
	public static String dateFormat(String date){
		StringBuffer sb=new StringBuffer();
		sb.append(date.substring(0, 4));
		sb.append("/");
		sb.append(date.substring(4, 6));
		sb.append("/");
		sb.append(date.substring(6, 8));
		return sb.toString();
	}
	
	public static String timeFormat(String time) {
		StringBuffer sb=new StringBuffer();
		sb.append(time.substring(0, 2));
		sb.append(":");
		sb.append(time.substring(2, 4));
		sb.append(":");
		sb.append(time.substring(4, 6));
		return sb.toString();
	}

}
