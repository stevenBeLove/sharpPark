package com.imobpay.park.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.park.model.ParkRuleSetBean;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.imobpay.park.service.AbstractCostPriceService;

/**
 * 跨日策略计算规则
 * @author lenovo
 *
 */
public class IntervalDayRuleServiceImpl extends AbstractCostPriceService{
	
	private static final Logger log = LoggerFactory
			.getLogger(IntervalDayRuleServiceImpl.class);
	
	public String RULE_TYPE = "01"; //01：模糊计费 (30/60分钟) 02：精确计费(分钟) 
	
	@Override
	public Map<String, Object> execute(ParkRuleSetBean parkRuleSetBean,Map<String,Object> reqParam) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retCode = "99";
		String retMessage = "系统异常";
		String payType = "L";
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
			Date startHour = date.parse(dayDate.format(in_time) + " "
					+ parkRuleSetBean.getStartHour() + ":00");
			Date endHour = date.parse(dayDate.format(in_time) + " "
					+ parkRuleSetBean.getEndHour() + ":00");
			Date nendHour = date.parse(dayDate.format(in_time) + " "
					+ parkRuleSetBean.getnEndHour() + ":00");
			boolean inFlag = this.isDayFlag(startHour, endHour, in_time);
			List<Map<String, Date>> list = new ArrayList<Map<String, Date>>();
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			monthVehicleBrandBean.setCarNumber(carNumber);
			if("24:00".equals(parkRuleSetBean.getnEndHour())){
				nendHour=addDate(nendHour, -1);
				if(in_time.compareTo(nendHour)>-1){
					nendHour = addDate(nendHour, 1);
				}
			}
			list = queryDateList(in_time, out_time, startHour, nendHour);
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
					startHour = date.parse(dayDate.format(in) + " "
							+ parkRuleSetBean.getStartHour() + ":00");
					endHour = date.parse(dayDate.format(in) + " "
							+ parkRuleSetBean.getEndHour() + ":00");
					log.info("in:" + date.format(in) + ",out:"
							+ date.format(out));
					inFlag = this.isDayFlag(startHour, endHour, in);
					boolean outFlag = this
							.isDayFlag(startHour, endHour, out);
					int stopMin = queryMin(in, out);
					if(inFlag && outFlag){
						price = totalPrice(stopMin, freeTime, subFreeTime,
								startChargeTime, startChargePrice,
								chargeTime, chargePrice, timeSlotLimit,
								chargeType, subCharge, true);
						log.info("全白天" + carNumber + "停车时长:" + stopMin
								+ "金额：" + price);
						price = dayTotalPrice(dayLimit, price);
					}else if(!inFlag){
						//如果进场是晚上
						price = totalPrice(stopMin, nfreeTime, nsubFreeTime,
								nstartChargeTime, nstartChargePrice,
								nchargeTime, nchargePrice, ntimeSlotLimit,
								nchargeType, nsubCharge, true);
						log.info("全晚上" + carNumber + "停车时长:" + stopMin
								+ "金额：" + price);
						price = dayTotalPrice(dayLimit, price);
					}else if(inFlag){
						//如果进场是白天
						int inMin = queryMin(
								in,
								date.parse(dayDate.format(in) + " "
										+ parkRuleSetBean.getEndHour()
										+ ":00"));
						BigDecimal inPrice = totalPrice(inMin,
								freeTime, subFreeTime, startChargeTime,
								startChargePrice, chargeTime,
								chargePrice, timeSlotLimit, chargeType,
								subCharge, true);
						int outMin = queryMin(date.parse(dayDate.format(in) + " "
										+ parkRuleSetBean.getEndHour()
										+ ":00"), out);
						log.info("白天：" + carNumber + "停车时长："
								+ outMin + "金额：" + inPrice);
						BigDecimal outPrice = totalPrice(outMin,
								nfreeTime, nsubFreeTime,
								nstartChargeTime, nstartChargePrice,
								nchargeTime, nchargePrice,
								ntimeSlotLimit, nchargeType,
								nsubCharge, true);
						log.info("晚上：" + carNumber + "停车时长："
								+ outMin + "金额：" + outPrice);
						price = inPrice.add(outPrice);
						price = dayTotalPrice(dayLimit, price);
					}
					if(isMonthFlag){
						//月卡已过期
						startHour = date.parse(dayDate.format(in) + " 00:00:00");
						endHour = date.parse(dayDate.format(in) + " "
								+ parkRuleSetBean.getStartHour() + ":00");
						BigDecimal mendPrice = totalPrice(queryMin(startHour, endHour), nfreeTime, nsubFreeTime,
								nstartChargeTime, nstartChargePrice,
								nchargeTime, nchargePrice, ntimeSlotLimit,
								nchargeType, nsubCharge, true);
						log.info("月卡过期，补夜晚：" + carNumber + "停车时长:" + queryMin(startHour, endHour)
								+ "金额：" + mendPrice);
						price = price.add(mendPrice);
						isMonthFlag = false;
					}
				}
				listPrice.add(price);
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
	
	
	public List<Map<String,Date>> queryDateList(Date in,Date out,Date startHour,Date endHour){
		List<Map<String,Date>> list = new ArrayList<Map<String,Date>>();
		Map<String,Date> dateMap = null;
		int i =0;
		while(true){
			dateMap = new HashMap<String, Date>();
			boolean flag = dateSplit(in, out, startHour, endHour, list,dateMap,i);
			in = dateMap.get("out");
			if(!flag){
				break;
			}
			i++;
		}
		return list;
	}
	
	public boolean dateSplit(Date in,Date out,Date startHour,Date endHour,List<Map<String,Date>> list,Map<String,Date> dateMap,int i){
		boolean flag = false;
		endHour = addDate(endHour,i);
		if(out.compareTo(endHour)!=-1){
			dateMap.put("in", in);
			dateMap.put("out", endHour);
			list.add(dateMap);
			flag = true;
		}else{
			dateMap.put("in", in);
			dateMap.put("out", out);
			list.add(dateMap);
		}	
		return flag;
	}
	
	
	public BigDecimal totalPrice(int stopMin, long freeTime, long subFreeTime,
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
}
