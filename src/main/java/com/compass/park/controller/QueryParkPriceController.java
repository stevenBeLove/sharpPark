package com.compass.park.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imobpay.base.services.QueryParkPriceService;
import com.imobpay.park.service.ParkingCostService;

@Controller
public class QueryParkPriceController {
	
	private static final Logger log = LoggerFactory
			.getLogger(QueryParkPriceController.class);
	
	@Autowired
	@Qualifier("queryParkPriceService")
	private QueryParkPriceService queryParkPriceService;
	
	@Autowired
	@Qualifier("parkingCostService")
	private ParkingCostService parkingCostService;
	
	@RequestMapping(value="/qeryParkPrice.do",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> qeryParkPrice(
			@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "inTime") String inTime,
			@RequestParam(value = "outTime") String outTime,
			@RequestParam(value = "vehicleType") String vehicleType,
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		String retCode = "99";
		String retMessage = "系统异常";
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, Object>  paramMap = new HashMap<String, Object>();
		log.info("qeryParkPrice--start--:outParkingId:"+outParkingId+",inTime:"+inTime+",outTime:"+outTime+",vehicleType:"+vehicleType+",carNumber:"+carNumber);
		if(StringUtils.isBlank(outParkingId)||StringUtils.isBlank(inTime)||StringUtils.isBlank(outTime)||StringUtils.isBlank(vehicleType)||StringUtils.isBlank(carNumber)){
			retCode = "01";
			retMessage = "请求参数为空";
		}else{
			boolean flag = true;
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date.parse(inTime);
				date.parse(outTime);
			} catch (Exception e) {
				retCode = "02";
				retMessage = "日期格式不正确";
				flag = false;
			}
			if(flag){
				paramMap.put("outParkingId", outParkingId);
				paramMap.put("inTime", inTime);
				paramMap.put("outTime", outTime);
				paramMap.put("vehicleType", vehicleType);
				paramMap.put("carNumber", carNumber);
				//Map<String, Object> queryRetMap = queryParkPriceService.execute(paramMap);
				Map<String, Object> queryRetMap = parkingCostService.execute(paramMap);
				if(!queryRetMap.isEmpty()&&"00".equals(queryRetMap.get("retCode"))){
					retMap.put("totalPrice", (String)queryRetMap.get("totalPrice"));
					retMap.put("payType", (String)queryRetMap.get("payType"));
				}
				retCode = (String) queryRetMap.get("retCode");
				retMessage = (String) queryRetMap.get("retMessage");
			}
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
}
