package com.imobpay.park.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.park.model.ParkRuleSetBean;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.imobpay.park.service.AbstractCostPriceService;

/*
 * 轮询规则
 */
public class PollingRuleServiceImpl extends AbstractCostPriceService{

	@Override
	public Map<String, Object> execute(ParkRuleSetBean parkRuleSetBean, Map<String, Object> reqParam) {
		String retCode = "99";
		String retMessage = "系统异常";
		String payType = "L";
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			//月卡临界状态
			boolean isMonthFlag = false;
			BigDecimal totalPrice = BigDecimal.ZERO;
			List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dayDate = new SimpleDateFormat("yyyy-MM-dd");
			String outParkingId = (String) reqParam.get("outParkingId");
			String inTime = (String) reqParam.get("inTime");
			String outTime = (String) reqParam.get("outTime");
			String carNumber = (String) reqParam.get("carNumber");
			Date in_time = date.parse(inTime);
			Date out_time = date.parse(outTime);
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			monthVehicleBrandBean.setCarNumber(carNumber);
			List<Map<String, Date>> list = new ArrayList<Map<String, Date>>();
			if (isSameDate(in_time, out_time)) {
				Map<String, Date> firstMap = new HashMap<String, Date>();
				firstMap.put("in", in_time);
				firstMap.put("out", out_time);
				list.add(firstMap);
			} else {
				int totalDay = queryDayHour(in_time, out_time);
				for (int i = 1; i < totalDay; i++) {
					Map<String, Date> c = new HashMap<String, Date>();
					c.put("in", addDate(in_time, i-1));
					c.put("out",addDate(in_time, i));
					list.add(c);
				}
				Map<String, Date> lastMap = new HashMap<String, Date>();
				lastMap.put("in",addDate(in_time, totalDay-1));
				lastMap.put("out", out_time);
				list.add(lastMap);
			}
			//日限额
			BigDecimal dayLimit = parkRuleSetBean.getDayLimit();
			//免费停车时长
			long freeTime = parkRuleSetBean.getFreeTime();
			//前x分钟
			long startChargeTime = parkRuleSetBean.getStartChargeTime();
			BigDecimal startChargePrice = parkRuleSetBean.getStartChargePrice();
			long chargeTime = parkRuleSetBean.getChargeTime();
			BigDecimal chargePrice = parkRuleSetBean.getChargePrice();
			for (Map<String, Date> dateMap : list) {
				BigDecimal price = BigDecimal.ZERO;
				Date in = dateMap.get("in");
				Date out = dateMap.get("out");
				monthVehicleBrandBean.setStopDate(in);
				if(getMonthVehicleBrandService().isMonthVehicleFlag(monthVehicleBrandBean)){
					listPrice.add(BigDecimal.ZERO);
					payType="M";
					log.info(carNumber+"日期"+dayDate.format(in)+"该天为月卡时间");
					retCode = "00";
					retMessage = "success";
					isMonthFlag = true;
				}else{
					int stopMin = queryMin(in, out);
					price = totalPrice(stopMin, dayLimit, freeTime, startChargeTime, startChargePrice, chargeTime, chargePrice);
					log.info("需要：" + carNumber + "停车时长:" + stopMin+"，金额："+price);
					listPrice.add(price);
					if(isMonthFlag){
						Date startHour = date.parse(dayDate.format(in) + " 00:00:00");
						stopMin = queryMin(startHour, in);
						BigDecimal mendPrice = totalPrice(stopMin, dayLimit, freeTime, startChargeTime, startChargePrice, chargeTime, chargePrice);
						listPrice.add(mendPrice);
						isMonthFlag = false;
						log.info("月卡过期，补夜晚：" + carNumber + "停车时长:" + stopMin+"，金额："+mendPrice);
					}
				}
			}
			for (BigDecimal pr : listPrice) {
				totalPrice = totalPrice.add(pr);
			}
			retCode = "00";
			retMessage = "success";
			log.info(carNumber + "需付费" + totalPrice + "元");
			retMap.put("totalPrice", totalPrice.toString());
			retMap.put("payType", payType);
		} catch (Exception e) {
			log.error("按轮询规则计算异常", e);
			retCode = "99";
			retMessage = "系统异常";
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	public BigDecimal totalPrice(int stopMin,BigDecimal dayLimit,long freeTime,
			long startChargeTime,BigDecimal startChargePrice,long chargeTime,BigDecimal chargePrice){
		BigDecimal price = BigDecimal.ZERO;
		if (stopMin > freeTime) {
			stopMin = (int) (stopMin - freeTime);
			if (stopMin > startChargeTime) {
				stopMin = (int) (stopMin - startChargeTime);
				price = price.add(startChargePrice);
				price = price.add(chargePrice.multiply(new BigDecimal(queryChargeCount(stopMin, (int)chargeTime))));
			}else{
				price = startChargePrice;
			}
		}
		if (dayLimit.compareTo(BigDecimal.ZERO) > 0) {
			if (price.compareTo(dayLimit) > -1) {
				price = dayLimit;
			}
		}
		return price;
	}
}
