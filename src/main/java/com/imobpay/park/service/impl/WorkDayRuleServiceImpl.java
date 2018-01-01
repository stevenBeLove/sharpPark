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
 * 工作日/非工作日
 * @author lenovo
 *
 */
public class WorkDayRuleServiceImpl extends AbstractCostPriceService{

	@Override
	public Map<String, Object> execute(ParkRuleSetBean parkRuleSetBean, Map<String, Object> reqParam) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retCode = "99";
		String retMessage = "系统异常";
		String payType = "L";
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
					BigDecimal dayLimit = parkRuleSetBean.getDayLimit();
					// 日间
					long freeTime = parkRuleSetBean.getFreeTime();
					long startChargeTime = parkRuleSetBean.getStartChargeTime();
					BigDecimal startChargePrice = parkRuleSetBean
							.getStartChargePrice();
					long chargeTime = parkRuleSetBean.getChargeTime();
					BigDecimal chargePrice = parkRuleSetBean.getChargePrice();
					BigDecimal timeSlotLimit = parkRuleSetBean
							.getTimeSlotLimit();
					String chargeType = parkRuleSetBean.getChargeType();// 1.按时长计费
																		// 2按次数计费
																		// 3免费
					BigDecimal subCharge = parkRuleSetBean.getSubCharge();
					long subFreeTime = parkRuleSetBean.getSubFreeTime();
					// 夜间
					long nfreeTime = parkRuleSetBean.getnFreeTime();
					long nstartChargeTime = parkRuleSetBean
							.getnStartChargeTime();
					BigDecimal nstartChargePrice = parkRuleSetBean
							.getnStartChargePrice();
					long nchargeTime = parkRuleSetBean.getnChargeTime();
					BigDecimal nchargePrice = parkRuleSetBean.getnChargePrice();
					BigDecimal ntimeSlotLimit = parkRuleSetBean
							.getnTimeSlotLimit();
					String nchargeType = parkRuleSetBean.getnChargeType();
					BigDecimal nsubCharge = parkRuleSetBean.getnSubCharge();
					long nsubFreeTime = parkRuleSetBean.getnSubFreeTime();
					log.info("in:" + date.format(in) + ",out:"
							+ date.format(out));
					int i = dayForWeek(in);
					int stopMin = queryMin(in, out);
					if(i==6||i==7){
						//非工作日
						price = totalPrice(stopMin,
								nfreeTime, nsubFreeTime,
								nstartChargeTime, nstartChargePrice,
								nchargeTime, nchargePrice,
								ntimeSlotLimit, nchargeType,
								nsubCharge, true);
						price = dayTotalPrice(dayLimit, price);
						log.info("非工作日" + carNumber + "停车时长:" + stopMin
								+ "金额：" + price);
					}else{
						//工作日
						price = totalPrice(stopMin, freeTime, subFreeTime,
								startChargeTime, startChargePrice,
								chargeTime, chargePrice, timeSlotLimit,
								chargeType, subCharge, true);
						price = dayTotalPrice(dayLimit, price);
						log.info("工作日" + carNumber + "停车时长:" + stopMin
								+ "金额：" + price);
					}
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
			log.error("查询收费金额异常", e);
			retCode = "99";
			retMessage = "系统异常";
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
}
