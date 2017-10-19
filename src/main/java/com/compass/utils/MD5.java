package com.compass.utils;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author wangLong
 *
 */
public class MD5 {
    
    private static final Logger LogPay = LoggerFactory.getLogger(MD5.class);
	// MD5加码。32位
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String KL(String inStr) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		return new String(a);
	}

	// 加密后解密
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		return new String(a);
	}

	// 测试主函数
	public static void main(String args[]) {
		String s = new String("2");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + MD5(s));
		System.out.println("MD5后再加密：" + KL(MD5(s)));
		System.out.println("解密为MD5后的：" + JM(KL(MD5(s))));
	}
	

  /**
   * 
   * @方法名称:md5
   * @方法作用：
   * @方法条件：
   * @方法流程：
   * @作者:madman MD5工具算法类
   * @param s
   *            参数
   * @return 返回结果
   */
  public static final String md5(String s) {
      char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
      try {
          byte[] strTemp = s.getBytes("UTF-8");
          MessageDigest mdTemp = MessageDigest.getInstance("MD5");
          mdTemp.update(strTemp);
          byte[] md = mdTemp.digest();
          int j = md.length;
          char[] str = new char[j * 2];
          int k = 0;
          for (int i = 0; i < j; i++) {
              byte byte0 = md[i];
              str[k++] = hexDigits[byte0 >>> 4 & 0xf];
              str[k++] = hexDigits[byte0 & 0xf];
          }
          return new String(str);
      } catch (Exception e) {
          return null;
      }
  }

  /**
   * 
   * @方法名称:md52
   * @方法作用：MD5算法2
   * @方法条件：
   * @方法流程：
   * @作者:madman
   * @param b
   *            参数
   * @return 返回结果
   */
  public static final String md52(byte[] b) {
      char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
      try {
          byte[] strTemp = b;
          MessageDigest mdTemp = MessageDigest.getInstance("MD5");
          mdTemp.update(strTemp);
          byte[] md = mdTemp.digest();
          int j = md.length;
          char[] str = new char[j * 2];
          int k = 0;
          for (int i = 0; i < j; i++) {
              byte byte0 = md[i];
              str[k++] = hexDigits[byte0 >>> 4 & 0xf];
              str[k++] = hexDigits[byte0 & 0xf];
          }
          return new String(str);
      } catch (Exception e) {
          return null;
      }
  }

  /**
   * @author eric 20140410 带指定密钥串的MD5加密,高阳捷迅游戏，Q币使用，其他渠道使用依据实际情况
   * @param strSrc strSrc
   * @param key key
   *            <pre>
   * 字符串形式的md5加密，以小写密文串
   * </pre>
   * @return String
   */
  public static String md5(String strSrc, String key) {
      String result = "";
      try {

          MessageDigest md5 = MessageDigest.getInstance("MD5");
          md5.update(strSrc.getBytes("UTF-8"));
          byte[] temp;
          temp = md5.digest(key.getBytes("UTF-8"));
          for (int i = 0; i < temp.length; i++) {
              result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
          }
      } catch (Exception e) {
          LogPay.error("Md5生成错误："+e.getMessage(), e);
      }
      return result;
  }
	
	
}
