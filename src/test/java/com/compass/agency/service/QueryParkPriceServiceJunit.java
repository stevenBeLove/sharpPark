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
			//入：2017-11-23 09:30:00    出  2017-11-24 16:30:00
			//2017-11-24 16:30:00
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("outParkingId", "10230");//停车场Id
			paramMap.put("inTime", "2017-11-23 09:30:59");//进场时间
			paramMap.put("outTime", "2017-11-24 16:33:12");//出场时间
			paramMap.put("carNumber", "沪A6B522");//车牌
			paramMap.put("vehicleType", "1");//车类型 车辆类型0.全部 1.小型车2.中型车 3.大型车 4.摩托车 5.其他
			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
			QueryParkPriceService queryParkPriceService = (QueryParkPriceService) ctx.getBean("queryParkPriceService");
			System.out.println(queryParkPriceService.execute(paramMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
