package com.compass.agency.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imobpay.base.services.QueryParkPriceService;

public class QueryParkPriceServiceJunit {
	
	@Test
	public void test(){
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("outParkingId", "100020");
			paramMap.put("inTime", "2017-11-11 21:00:00");
			paramMap.put("outTime", "2017-11-14 21:30:00");
			paramMap.put("carNumber", "沪A6B521");
			paramMap.put("vehicleType", "1");
			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
			QueryParkPriceService queryParkPriceService = (QueryParkPriceService) ctx.getBean("queryParkPriceService");
			System.out.println(queryParkPriceService.execute(paramMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
