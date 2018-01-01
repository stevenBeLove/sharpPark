package com.imobpay.park.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.compass.park.model.ParkRuleSetBean;
import com.imobpay.park.service.AbstractCostPriceService;

/**
 * 免费计算规则
 * @author lenovo
 *
 */
public class FreeRuleServiceImpl extends AbstractCostPriceService{

	@Override
	public Map<String, Object> execute(ParkRuleSetBean parkRuleSetBean, Map<String, Object> reqParam) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("totalPrice", "0");
		retMap.put("payType", "M");
		retMap.put("retCode", "00");
		retMap.put("retMessage", "success");
		return retMap;
	}
}
