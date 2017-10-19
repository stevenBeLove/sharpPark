package com.compass.utils;

/**
 * 供后台使用的WebRoot工具类
 */
public class ContextRootUtils {
  private static String contextroot;

  public static String getContextRoot() {
    return contextroot;
  }

  public static void setContextRoot(String root) {
    contextroot = root;
  }
}
