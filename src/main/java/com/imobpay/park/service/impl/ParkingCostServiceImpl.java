package com.imobpay.park.service.impl;

import java.math.BigDecimal;
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
import com.imobpay.park.service.ParkingCostService;

/**
 * 停车费用
 * @author lenovo
 *
 */

public class ParkingCostServiceImpl implements ParkingCostService{
	private static final Logger log = LoggerFactory
			.getLogger(ParkingCostServiceImpl.class);
	
	
	public Map<String, Object> execute(Map<String, Object> paramMap) {
		long begin = System.currentTimeMillis();
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retCode = "99";
		String retMessage = "系统异常";
		try {
			BigDecimal totalPrice = BigDecimal.ZERO;
			String outParkingId = (String) paramMap.get("outParkingId");
			String carNumber = (String) paramMap.get("carNumber");
			String vehicleType = (String) paramMap.get("vehicleType");
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
			ParkRuleSetBean parkRuleSetBean = new ParkRuleSetBean();
			parkRuleSetBean.setOutParkingId(outParkingId);
			parkRuleSetBean.setVehicleType(vehicleType);
			List<ParkRuleSetBean> ruleList = parkRuleSetService.queryParkRuleSetByType(parkRuleSetBean);
			if(ruleList.isEmpty()||ruleList.size()==0){
				retCode = "01";
				retMessage = "未配置策略";
			}else if(ruleList.size()>1){
				retCode = "01";
				retMessage = "规则设置错误";
			}else{
				parkRuleSetBean = ruleList.get(0);
				Map<String,Object> costRetMap = costPriceByType(parkRuleSetBean, paramMap);
				long end = System.currentTimeMillis();
				log.info("ParkingCostServiceImpl---耗时："+(end-begin)+"ms");
				return costRetMap;
			}
		} catch (Exception e) {
			log.error("查询收费金额异常", e);
			retCode = "99";
			retMessage = "系统异常";
		}
		long end = System.currentTimeMillis();
		log.info("ParkingCostServiceImpl---耗时："+(end-begin)+"ms");
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	/**
	 * 计算费用by类型
	 * @param type
	 * @return
	 */
	public Map<String, Object> costPriceByType(ParkRuleSetBean parkRuleSetBean,Map<String,Object> reqParam){
		//计费类型
		String type = parkRuleSetBean.getType();
		Map<String,Object> retMap = new HashMap<String, Object>();
		int t = Integer.valueOf(type);
		switch (t) {
		case 0:
			//免费
			FreeRuleServiceImpl freeServiceImpl = new FreeRuleServiceImpl();
			retMap = freeServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 1:
			//时长
			DurationRuleServiceImpl durationRuleServiceImpl = new DurationRuleServiceImpl();
			retMap = durationRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 2:
			//次数
			TimeCountRuleServiceImpl countRuleServiceImpl = new TimeCountRuleServiceImpl();
			retMap = countRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 3:
			//24小时轮询
			PollingRuleServiceImpl pollingRuleServiceImpl = new PollingRuleServiceImpl();
			retMap = pollingRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 4:
			//跨日时段
			IntervalDayRuleServiceImpl intervalDayRuleServiceImpl = new IntervalDayRuleServiceImpl();
			retMap = intervalDayRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 5:
			//当日时段
			NowDayRuleServiceImpl nowDayRuleServiceImpl = new NowDayRuleServiceImpl();
			retMap = nowDayRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		case 6:
			//工作日非工作日
			WorkDayRuleServiceImpl workDayRuleServiceImpl = new WorkDayRuleServiceImpl();
			retMap = workDayRuleServiceImpl.execute(parkRuleSetBean, reqParam);
			break;
		default:
			break;
		}
		return retMap;
	}
	
	public FreeVehicleBrandService freeVehicleBrandService;
	
	public ParkRuleSetService parkRuleSetService;


	public FreeVehicleBrandService getFreeVehicleBrandService() {
		return freeVehicleBrandService;
	}

	public void setFreeVehicleBrandService(FreeVehicleBrandService freeVehicleBrandService) {
		this.freeVehicleBrandService = freeVehicleBrandService;
	}

	public ParkRuleSetService getParkRuleSetService() {
		return parkRuleSetService;
	}

	public void setParkRuleSetService(ParkRuleSetService parkRuleSetService) {
		this.parkRuleSetService = parkRuleSetService;
	}
}
