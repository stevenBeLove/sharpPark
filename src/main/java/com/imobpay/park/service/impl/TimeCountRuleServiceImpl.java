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

/**
 * 计次规则
 * @author lenovo
 *
 */
public class TimeCountRuleServiceImpl extends AbstractCostPriceService{
	
	@Override
	public Map<String, Object> execute(ParkRuleSetBean parkRuleSetBean, Map<String, Object> reqParam) {
		String retCode = "99";
		String retMessage = "系统异常";
		String payType = "L";
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
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
				// 总天数
				int totalDay = queryDay(in_time, out_time);
				Map<String, Date> firstMap = new HashMap<String, Date>();
				firstMap.put("in", in_time);
				firstMap.put(
						"out",
						date.parse(dayDate.format(addDate(in_time, 0))
								+ " 24:00:00"));
				list.add(firstMap);
				for (int i = 1; i < totalDay; i++) {
					Map<String, Date> c = new HashMap<String, Date>();
					c.put("in",
							date.parse(dayDate.format(addDate(in_time, i))
									+ " 00:00:00"));
					c.put("out",
							date.parse(dayDate.format(addDate(in_time, i))
									+ " 24:00:00"));
					list.add(c);
				}
				Map<String, Date> lastMap = new HashMap<String, Date>();
				lastMap.put(
						"in",
						date.parse(dayDate.format(addDate(out_time, 0))
								+ " 00:00:00"));
				lastMap.put("out", out_time);
				list.add(lastMap);
			}
			//日限额
			BigDecimal dayLimit = parkRuleSetBean.getDayLimit();
			//免费停车时长
			long freeTime = parkRuleSetBean.getFreeTime();
			//每次收费
			BigDecimal subCharge = parkRuleSetBean.getSubCharge();
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
				}else{
					int stopMin = queryMin(in, out);
					price = totalPrice(stopMin, dayLimit, freeTime,subCharge);
					listPrice.add(price);
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
			log.error("按次计费计算异常", e);
			retCode = "99";
			retMessage = "系统异常";
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	public BigDecimal totalPrice(int stopMin,BigDecimal dayLimit,long freeTime,BigDecimal subCharge){
		BigDecimal price = BigDecimal.ZERO;
		if (stopMin > freeTime) {
			price = subCharge;
		}
		if (dayLimit.compareTo(BigDecimal.ZERO) > 0) {
			if (price.compareTo(dayLimit) > -1) {
				price = dayLimit;
			}
		}
		return price;
	}
}
