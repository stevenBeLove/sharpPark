package com.imobpay.base.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.park.model.ParkRuleSetBean;
import com.compass.park.service.ParkRuleSetService;
import com.compass.vehicle.model.FreeVehicleBrandBean;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.compass.vehicle.service.FreeVehicleBrandService;
import com.compass.vehicle.service.MonthVehicleBrandService;

public class QueryParkPriceServiceImpl implements QueryParkPriceService {

	private static final Logger log = LoggerFactory
			.getLogger(QueryParkPriceServiceImpl.class);

	public List<Integer> weekList = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
	public List<Integer> weekDayList = Arrays.asList(1, 2, 3, 4, 5);
	public List<Integer> noWeekDayList = Arrays.asList(6, 7);
	public String RULE_TYPE = "01"; //01：模糊计费 (30/60分钟) 02：精确计费(分钟) 

	public Map<String, Object> execute(Map<String, Object> paramMap) {
		long begin = System.currentTimeMillis();
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retCode = "99";
		String retMessage = "系统异常";
		String payType = "L";
		try {
			BigDecimal totalPrice = BigDecimal.ZERO;
			List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dayDate = new SimpleDateFormat("yyyy-MM-dd");
			String outParkingId = (String) paramMap.get("outParkingId");
			String inTime = (String) paramMap.get("inTime");
			String outTime = (String) paramMap.get("outTime");
			String carNumber = (String) paramMap.get("carNumber");
			String vehicleType = (String) paramMap.get("vehicleType");
			Date in_time = date.parse(inTime);
			Date out_time = date.parse(outTime);
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			monthVehicleBrandBean.setCarNumber(carNumber);
			FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			freeVehicleBrandBean.setCarNumber(carNumber);
			if(freeVehicleBrandService.isFreeVehicleFlag(freeVehicleBrandBean)){
				log.info(carNumber+"该车为免费车");
				long end = System.currentTimeMillis();
				log.info("QueryParkPriceService---耗时："+(end-begin)+"ms");
				retMap.put("totalPrice", totalPrice.toString());
				retMap.put("payType", "F");
				retMap.put("retCode", "00");
				retMap.put("retMessage", "success");
				return retMap;
			}
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
				if(monthVehicleBrandService.isMonthVehicleFlag(monthVehicleBrandBean)){
					listPrice.add(BigDecimal.ZERO);
					payType="M";
					log.info(carNumber+"日期"+dayDate.format(in)+"该天为月卡时间");
					retCode = "00";
					retMessage = "success";
				}else{
					ParkRuleSetBean reqParkRuleSetBean = new ParkRuleSetBean();
					reqParkRuleSetBean.setOutParkingId(outParkingId);
					reqParkRuleSetBean.setVehicleType(vehicleType);
					reqParkRuleSetBean.setStart(1);
					int inTimeWeekDay = dayForWeek(in);
					List<String> dateSetList = new ArrayList<String>();
					if (weekList.contains(inTimeWeekDay))
						dateSetList.add("1");
					if (weekDayList.contains(inTimeWeekDay))
						dateSetList.add("2");
					if (noWeekDayList.contains(inTimeWeekDay))
						dateSetList.add("3");
					dateSetList.add("1" + inTimeWeekDay);
					reqParkRuleSetBean.setDateSetList(dateSetList);
					List<ParkRuleSetBean> parkRuleSetList = parkRuleSetService
							.queryParkRuleSetByParkId(reqParkRuleSetBean);
					if (parkRuleSetList == null || parkRuleSetList.size() <= 0) {
						log.info("指定规则不存在，查询默认规则");
						reqParkRuleSetBean.setVehicleType("0");
						parkRuleSetList = parkRuleSetService
								.queryParkRuleSetByParkId(reqParkRuleSetBean);
					}
					if (parkRuleSetList != null && parkRuleSetList.size() > 0) {
						ParkRuleSetBean parkRuleSetBean = getParkRule(
								parkRuleSetList, in);
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
								log.info("全白天" + carNumber + "停车时长:" + stopMin
										+ "金额：" + price);
								price = dayTotalPrice(dayLimit, price);
							} else if (!inFlag && !outFlag) {
								if (in.compareTo(startHour) == -1
										&& out.compareTo(endHour) > -1) {
									stopMin = queryMin(in, startHour);
									BigDecimal firstPrice = totalPrice(stopMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, true);
									log.info("全晚上：晚上-白天：" + carNumber + "停车时长:"
											+ stopMin + "金额：" + firstPrice);
									stopMin = queryMin(startHour, endHour);
									BigDecimal twoPrice = totalPrice(stopMin,
											freeTime, subFreeTime, startChargeTime,
											startChargePrice, chargeTime,
											chargePrice, timeSlotLimit, chargeType,
											subCharge, true);
									log.info("全晚上：白天-白天结束：" + carNumber + "停车时长:"
											+ stopMin + "金额：" + twoPrice);
									stopMin = queryMin(endHour, out);
									BigDecimal threePrice = totalPrice(stopMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, false);
									log.info("全晚上：白天结束-出：" + carNumber + "停车时长:"
											+ stopMin + "金额：" + threePrice);
									price = price.add(firstPrice).add(twoPrice)
											.add(threePrice);
									price = dayTotalPrice(dayLimit, price);
								} else {
									price = totalPrice(stopMin, nfreeTime,
											nsubFreeTime, nstartChargeTime,
											nstartChargePrice, nchargeTime,
											nchargePrice, ntimeSlotLimit,
											nchargeType, nsubCharge, true);
									log.info("全晚上" + carNumber + "停车时长:" + stopMin
											+ "金额：" + price);
									price = dayTotalPrice(dayLimit, price);
								}
							} else {
								int inMin = 0;
								int outMin = 0;
								if (inFlag && !outFlag) {
									// 早进/晚出
									inMin = queryMin(
											in,
											date.parse(dayDate.format(in) + " "
													+ parkRuleSetBean.getEndHour()
													+ ":00"));
									BigDecimal inPrice = totalPrice(inMin,
											freeTime, subFreeTime, startChargeTime,
											startChargePrice, chargeTime,
											chargePrice, timeSlotLimit, chargeType,
											subCharge, true);
									log.info("早进晚出：进-白天结束：" + carNumber + "停车时长："
											+ inMin + "金额：" + inPrice);
									Date nightStartTime = date.parse(dayDate
											.format(in)
											+ " "
											+ parkRuleSetBean.getnStartHour()
											+ ":00");
									outMin = queryMin(nightStartTime, out);
									BigDecimal outPrice = totalPrice(outMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, true);
									log.info("早进晚出：白天结束-出：" + carNumber + "停车时长："
											+ outMin + "金额：" + outPrice);
									price = inPrice.add(outPrice);
									price = dayTotalPrice(dayLimit, price);
								} else if (!inFlag && outFlag) {
									// 同一天
									inMin = queryMin(
											in,
											date.parse(dayDate.format(in) + " "
													+ parkRuleSetBean.getnEndHour()
													+ ":00"));
									BigDecimal inPrice = totalPrice(inMin,
											nfreeTime, nsubFreeTime,
											nstartChargeTime, nstartChargePrice,
											nchargeTime, nchargePrice,
											ntimeSlotLimit, nchargeType,
											nsubCharge, true);
									log.info("晚进早出：晚上：" + carNumber + "停车时长："
											+ inMin + "金额：" + inPrice);
									outMin = queryMin(date.parse(dayDate
											.format(out)
											+ " "
											+ parkRuleSetBean.getStartHour()
											+ ":00"), out_time);
									BigDecimal outPrice = totalPrice(outMin,
											freeTime, subFreeTime, startChargeTime,
											startChargePrice, chargeTime,
											chargePrice, timeSlotLimit, chargeType,
											subCharge, true);
									log.info("晚进早出：白天：" + carNumber + "停车时长："
											+ outMin + "金额：" + outPrice);
									price = inPrice.add(outPrice);
									price = dayTotalPrice(dayLimit, price);
								}
							}
							listPrice.add(price);
					} else {
						log.info("未找到收费策略");
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
			log.error("查询收费金额异常", e);
			retCode = "99";
			retMessage = "系统异常";
		}
		long end = System.currentTimeMillis();
		log.info("QueryParkPriceService---耗时："+(end-begin)+"ms");
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}

	public ParkRuleSetBean getParkRule(List<ParkRuleSetBean> parkRuleSetList,
			Date inDate) throws Exception {
		ParkRuleSetBean parkRuleSet = new ParkRuleSetBean();
		// 今天星期几
		int i = dayForWeek(inDate);
		String dateSet = "";
		boolean customDayFlag = false;
		boolean weekDayOrNoWeekDayFalg = false;
		for (ParkRuleSetBean parkRuleSetBean : parkRuleSetList) {
			String f_dateSet = parkRuleSetBean.getDateSet();
			if(("1"+i).equals(f_dateSet)){
				customDayFlag = true;
			}else if(weekDayList.contains(i)&&"2".equals(f_dateSet)||(noWeekDayList.contains(i)&&"3".equals(f_dateSet))){
				weekDayOrNoWeekDayFalg = true;
			}
		}
		if(customDayFlag){
			dateSet = "1" + i;
		}else if(weekDayOrNoWeekDayFalg){
			dateSet = weekDayList.contains(i) ? "2" : "3";
		}else{
			dateSet = "1";
		}
		for (ParkRuleSetBean parkRuleSetBean : parkRuleSetList) {
			if (dateSet.equals(parkRuleSetBean.getDateSet())){
				parkRuleSet = parkRuleSetBean;
				return parkRuleSet;
			}
		}
		return parkRuleSet;
	}

	private int dayForWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	private boolean isDayFlag(Date time, Date from, Date to) {
		Calendar c_time = Calendar.getInstance();
		c_time.setTime(time);
		Calendar c_from = Calendar.getInstance();
		c_from.setTime(from);
		Calendar c_to = Calendar.getInstance();
		c_to.setTime(to);
		if(c_to.after(c_time)&&c_from.after(c_to)){
			return true;
		} else {
			return false;
		}
	}

	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	private BigDecimal dayTotalPrice(BigDecimal dayLimit, BigDecimal price) {
		if (dayLimit.compareTo(BigDecimal.ZERO) > 0) {
			if (price.compareTo(dayLimit) > -1) {
				price = dayLimit;
			}
		}
		return price;
	}

	private boolean isSameDate(Date in_time, Date out_time) {
		Calendar in = Calendar.getInstance();
		in.setTime(in_time);
		Calendar out = Calendar.getInstance();
		out.setTime(out_time);
		boolean isSameYear = in.get(Calendar.YEAR) == out.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& in.get(Calendar.MONTH) == out.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& in.get(Calendar.DAY_OF_MONTH) == out
						.get(Calendar.DAY_OF_MONTH);
		return isSameDate;
	}

	private BigDecimal totalPrice(int stopMin, long freeTime, long subFreeTime,
			long startChargeTime, BigDecimal startChargePrice, long chargeTime,
			BigDecimal chargePrice, BigDecimal timeSlotLimit,
			String chargeType, BigDecimal subCharge, boolean flag) {
		BigDecimal price = BigDecimal.ZERO;
		if (flag) {
			if ("1".equals(chargeType)) {
				if (stopMin > freeTime) {
					stopMin = (int) (stopMin - freeTime);
					if (stopMin > startChargeTime) {
						stopMin = (int) (stopMin - startChargeTime);
						price = price.add(startChargePrice);
						if("01".equals(RULE_TYPE)){
							price = price.add(chargePrice.multiply(new BigDecimal(queryChargeCount(stopMin, (int)chargeTime))));
						}else if("02".equals(RULE_TYPE)){
							price = price.add(chargePrice.multiply(
									new BigDecimal(stopMin)).divide(
									new BigDecimal(chargeTime), 2,
									BigDecimal.ROUND_HALF_UP));
						}
					} else {
						if("01".equals(RULE_TYPE)){
							price = startChargePrice;
						}else if("02".equals(RULE_TYPE)){
							price = startChargePrice.multiply(
									new BigDecimal(stopMin)).divide(
									new BigDecimal(startChargeTime), 2,
									BigDecimal.ROUND_HALF_UP);
						}
					}
				}
				if (timeSlotLimit.compareTo(BigDecimal.ZERO) > 0) {
					if (price.compareTo(timeSlotLimit) > -1) {
						price = timeSlotLimit;
					}
				}
			} else if ("2".equals(chargeType)) {
				if (stopMin > subFreeTime) {
					price = subCharge;
				}
			} else if ("3".equals(chargeType)) {
				log.info("计费方式为免费");
			} else {
				price = new BigDecimal(9999999);
				log.warn("未配置的规则类型");
			}
		} else {
			if ("1".equals(chargeType)) {
				if("01".equals(RULE_TYPE)){
					price = startChargePrice.multiply(new BigDecimal(queryChargeCount(stopMin, (int)startChargeTime)));
				}else if("02".equals(RULE_TYPE)){
					price = startChargePrice.multiply(new BigDecimal(stopMin))
							.divide(new BigDecimal(startChargeTime), 2,
									BigDecimal.ROUND_HALF_UP);
				}
			} else if ("2".equals(chargeType)) {
				if (flag) {
					price = subCharge;
				}
			} else if ("3".equals(chargeType)) {
				log.info("计费方式为免费");
			} else {
				price = new BigDecimal(9999999);
				log.warn("未配置的规则类型");
			}
		}
		return price;
	}
	
	private int queryChargeCount(int stopMin,int chargeTime){
		int c = 0;
		if(stopMin%chargeTime==0){
			c = stopMin/chargeTime;
		}else{
			c = stopMin/chargeTime+1;
		}
		return c;
	}

	private int queryMin(Date start, Date end) throws Exception {
		long statTime = start.getTime();
		long endTime = end.getTime();
		int minutes = (int) ((endTime - statTime) / (1000 * 60));
		return minutes;
	}

	private int queryDay(Date start, Date end) throws Exception {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(start);
		int startTime = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(end);
		int endTime = aCalendar.get(Calendar.DAY_OF_YEAR);
		return endTime - startTime;
	}

	public ParkRuleSetService parkRuleSetService;
	
	public FreeVehicleBrandService freeVehicleBrandService;
	
	public MonthVehicleBrandService monthVehicleBrandService;

	public ParkRuleSetService getParkRuleSetService() {
		return parkRuleSetService;
	}

	public void setParkRuleSetService(ParkRuleSetService parkRuleSetService) {
		this.parkRuleSetService = parkRuleSetService;
	}

	public FreeVehicleBrandService getFreeVehicleBrandService() {
		return freeVehicleBrandService;
	}

	public void setFreeVehicleBrandService(
			FreeVehicleBrandService freeVehicleBrandService) {
		this.freeVehicleBrandService = freeVehicleBrandService;
	}

	public MonthVehicleBrandService getMonthVehicleBrandService() {
		return monthVehicleBrandService;
	}

	public void setMonthVehicleBrandService(
			MonthVehicleBrandService monthVehicleBrandService) {
		this.monthVehicleBrandService = monthVehicleBrandService;
	}

}
