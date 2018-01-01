package com.imobpay.park.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.park.model.ParkRuleSetBean;
import com.compass.utils.ServiceFactory;
import com.compass.vehicle.service.MonthVehicleBrandService;

public abstract class AbstractCostPriceService {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String RULE_TYPE = "01"; //01：模糊计费 (30/60分钟) 02：精确计费(分钟) 
	
	public MonthVehicleBrandService getMonthVehicleBrandService() {
		return (MonthVehicleBrandService) ServiceFactory.getInstance().getBean("monthVehicleBrandService");
	}

	/**
	 * 计算费用入口
	 * @return
	 */
	public Map<String,Object> execute(ParkRuleSetBean parkRuleSetBean,Map<String,Object> reqParam){
		return null;
	}
	
	/**
	 * 判断是否在区间内
	 * @param time 
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean isDayFlag(Date time, Date from, Date to) {
		Calendar c_time = Calendar.getInstance();
		c_time.setTime(time);
		Calendar c_from = Calendar.getInstance();
		c_from.setTime(from);
		Calendar c_to = Calendar.getInstance();
		c_to.setTime(to);
		if(!c_time.after(c_to)&&!c_to.after(c_from)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否是同一天
	 * @param in_time
	 * @param out_time
	 * @return
	 */
	public boolean isSameDate(Date in_time, Date out_time) {
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
	
	/**
	 * 判断总天数
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int queryDay(Date start, Date end) throws Exception {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(start);
		int startTime = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(end);
		int endTime = aCalendar.get(Calendar.DAY_OF_YEAR);
		return endTime - startTime;
	}
	
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
	
	public int queryChargeCount(int stopMin,int chargeTime){
		int c = 0;
		if(stopMin%chargeTime==0){
			c = stopMin/chargeTime;
		}else{
			c = stopMin/chargeTime+1;
		}
		return c;
	}
	
	public int queryMin(Date start, Date end) throws Exception {
		long statTime = start.getTime();
		long endTime = end.getTime();
		int minutes = (int) ((endTime - statTime) / (1000 * 60));
		return minutes;
	}
	
	/**
	 * 存在多少个24，轮询
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int queryDayHour(Date start,Date end) throws Exception{
		int minutes = queryMin(start, end);
		int c = 0;
		if(minutes%1440==0){
			c = minutes/1440;
		}else{
			c = minutes/1440+1;
		}
		return c;
	}
	
	public int dayForWeek(Date date) throws Exception {
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
	
	public int query24Time(Date start,Date end) throws Exception{
		int minutes = queryMin(start, end);
		return minutes/1440;
	}
	
	public BigDecimal dayTotalPrice(BigDecimal dayLimit, BigDecimal price) {
		if (dayLimit.compareTo(BigDecimal.ZERO) > 0) {
			if (price.compareTo(dayLimit) > -1) {
				price = dayLimit;
			}
		}
		return price;
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
