package com.compass.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonTable {

	/**
	 * @param wangyuchao
	 */
	
	public static String getTableId(String head){
		Date data=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
		String Id=head+sf.format(data);
		return Id;
	}

}
