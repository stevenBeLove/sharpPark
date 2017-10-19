package com.compass.utils;
public  class SqlMatch {
	public static String getSqlMatch(String sql){
		String match="";
		sql=sql.trim();
		int z=sql.indexOf(" ");
		String title=sql.substring(0, z);
		if("select".equalsIgnoreCase(title)||
				"desc".equalsIgnoreCase(title)|| 
				"show".equalsIgnoreCase(title)){
			match="S";
		}else if("call".equals("title") ||"exec".equalsIgnoreCase(title)){
			match="C";
		}else{
			match="U";
		}
		return match;
	}
}
