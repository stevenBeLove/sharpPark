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
 * 当日时段
 * @author lenovo
 *
 */
public class NowDayRuleServiceImpl extends AbstractCostPriceService{

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
					Date startHour = date.parse(dayDate.format(in) + " "
							+ parkRuleSetBean.getStartHour() + ":00");
					Date endHour = date.parse(dayDate.format(in) + " "
							+ parkRuleSetBean.getEndHour() + ":00");
					log.info("in:" + date.format(in) + ",out:"
							+ date.format(out));
					boolean inFlag = this.isDayFlag(startHour, endHour, in);
					boolean outFlag = this
							.isDayFlag(startHour, endHour, out);
					int stopMin = queryMin(in, out);
					if (inFlag && outFlag) {
						price = totalPrice(stopMin, freeTime, subFreeTime,
								startChargeTime, startChargePrice,
								chargeTime, chargePrice, timeSlotLimit,
								chargeType, subCharge, true);
						log.info("全白天时段" + carNumber + "停车时长:" + stopMin
								+ "金额：" + price);
						price = dayTotalPrice(dayLimit, price);
					}else if(inFlag&&!outFlag){
						//进场白天、出场晚上
						stopMin = queryMin(in, endHour);
						BigDecimal dayPrice = totalPrice(stopMin, freeTime, subFreeTime,
								startChargeTime, startChargePrice,
								chargeTime, chargePrice, timeSlotLimit,
								chargeType, subCharge, true);
						log.info("1.白天时段：" + carNumber + "停车时长:" + stopMin
								+ "金额：" + dayPrice);
						stopMin = queryMin(endHour, out);
						BigDecimal nightPrice = totalPrice(stopMin,
								nfreeTime, nsubFreeTime,
								nstartChargeTime, nstartChargePrice,
								nchargeTime, nchargePrice,
								ntimeSlotLimit, nchargeType,
								nsubCharge, true);
						log.info("2.夜间时段：" + carNumber + "停车时长:" + stopMin
								+ "金额：" + nightPrice);
						price = dayPrice.add(nightPrice);
						price = dayTotalPrice(dayLimit, price);
					} else{
						//进场在日间起始之前
						if(startHour.compareTo(in)>-1){
							if(out.compareTo(startHour)>-1){
								if(out.compareTo(endHour)>-1){
									//出场>白天结束
									//进场在夜间起始时段
									int oneStopMin = queryMin(in, startHour);
									int twoStopMin = queryMin(endHour, out);
									stopMin = queryMin(startHour, endHour);
									BigDecimal dayPrice = totalPrice(stopMin, freeTime, subFreeTime,
											startChargeTime, startChargePrice,
											chargeTime, chargePrice, timeSlotLimit,
											chargeType, subCharge, true);
									log.info("多段之白天：" + carNumber + "停车时长:" + stopMin
											+ "金额：" + dayPrice);
									BigDecimal nightPrice = totalPrice(oneStopMin+twoStopMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, true);
									log.info("多段之夜间：" + carNumber + "停车时长总:" + (oneStopMin+twoStopMin)
											+ "，其中第一段时长："+oneStopMin+",第二段时长："+twoStopMin+"金额：" + nightPrice);
									price = dayPrice.add(nightPrice);
									price = dayTotalPrice(dayLimit, price);
								}else{
									stopMin = queryMin(in, startHour);
									BigDecimal nightPrice = totalPrice(stopMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, true);
									log.info("1.夜间时段：" + carNumber + "停车时长:" + stopMin
											+ "金额：" + nightPrice);
									stopMin = queryMin(startHour, out);
									BigDecimal dayPrice = totalPrice(stopMin, freeTime, subFreeTime,
											startChargeTime, startChargePrice,
											chargeTime, chargePrice, timeSlotLimit,
											chargeType, subCharge, true);
									log.info("2.白天时段：" + carNumber + "停车时长:" + stopMin
											+ "金额：" + dayPrice);
									price = dayPrice.add(nightPrice);
									price = dayTotalPrice(dayLimit, price);
								}
							}else{
								stopMin = queryMin(in, out);
								price = totalPrice(stopMin,
										nfreeTime, nsubFreeTime,
										nstartChargeTime, nstartChargePrice,
										nchargeTime, nchargePrice,
										ntimeSlotLimit, nchargeType,
										nsubCharge, true);
								price = dayTotalPrice(dayLimit, price);
								log.info("只有夜间前半段：" + carNumber + "停车时长:" + stopMin
										+ "金额：" + price);
							}
						}else{
							//进场在日间结束
							stopMin = queryMin(in, out);
							price = totalPrice(stopMin,
									nfreeTime, nsubFreeTime,
									nstartChargeTime, nstartChargePrice,
									nchargeTime, nchargePrice,
									ntimeSlotLimit, nchargeType,
									nsubCharge, true);
							price = dayTotalPrice(dayLimit, price);
							log.info("只有夜间后半段：" + carNumber + "停车时长:" + stopMin
									+ "金额：" + price);
						}
						
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
