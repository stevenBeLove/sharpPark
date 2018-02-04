package com.compass.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Md5Util {
	public static String getMd5(String sb) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(sb.getBytes());
		byte b[] = md5.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	/**
	 * map排序后拼接
	 * @param map
	 * @return
	 */
	public static String sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 降序排序
				return obj2.compareTo(obj1);
			}
		});
		sortMap.putAll(map);
		StringBuffer sb = new StringBuffer();
		for (String key : sortMap.keySet()) {
			sb.append(key+sortMap.get(key));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getMd5("123456"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
