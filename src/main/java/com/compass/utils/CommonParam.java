package com.compass.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 获取配置资源文件 [公共参数] 信息
 * 
 * @author wangLong
 */

public class CommonParam {

	private static String propertyFileName = "com/compass/utils/SysConfig";

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(propertyFileName);

	/**
	 * 根据名称获得值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {

		if (key == null || key.equals("") || key.equals("null")) {
			return "";
		}

		String result = "";
		try {
			result = resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return result;

	}

	public static boolean isStrInArray(String substring, String[] source) {
		if (source == null || source.length == 0) {
			return false;
		}
		for (int i = 0; i < source.length; i++) {
			String aSource = source[i];
			if (aSource.equals(substring)) {
				return true;
			}
		}
		return false;
	}

}
