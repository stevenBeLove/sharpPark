package com.compass.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.order.model.AccountBean;
import com.compass.order.model.ManagementAnalysisBean;
import com.compass.order.model.OrderPayBean;
import com.compass.utils.AbstractService;

public class OrderPayService extends AbstractService{
	
	public Integer getOrderPayCount(OrderPayBean orderPayBean) {
        return (Integer) dao.queryForObject("TB_ORDER_PAY.getOrderPayCount", orderPayBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<OrderPayBean> getOrderPayAll(OrderPayBean orderPayBean) {
	        return dao.queryForList("TB_ORDER_PAY.getOrderPayAll", orderPayBean);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderPayBean> getOrderPayListByType(OrderPayBean orderPayBean) {
	        return dao.queryForList("TB_ORDER_PAY.getOrderPayListByType", orderPayBean);
	}
	
	@SuppressWarnings("unchecked")
	public List<ManagementAnalysisBean> getManagementAnalysisList(String outParkingId,String startDate,String endDate,Integer start,Integer end) {
		Map<String,Object> reqParam = new HashMap<String,Object>();
		reqParam.put("startDate", startDate);
		reqParam.put("endDate", endDate);
		reqParam.put("start", start);
		reqParam.put("end", end);
		reqParam.put("outParkingId", outParkingId);
        return dao.queryForList("TB_ORDER_PAY.getManagementAnalysisList", reqParam);
	}
	
	public Integer getManagementAnalysisCount(String outParkingId,String startDate,String endDate) {
		Map<String,Object> reqParam = new HashMap<String,Object>();
		reqParam.put("startDate", startDate);
		reqParam.put("endDate", endDate);
		reqParam.put("outParkingId", outParkingId);
        return (Integer) dao.queryForObject("TB_ORDER_PAY.getManagementAnalysisCount", reqParam);
    }
	
	@SuppressWarnings("unchecked")
	public List<AccountBean> getAccountList(String outParkingId,String payType,String startDate,String endDate){
		Map<String,Object> reqParam = new HashMap<String,Object>();
		reqParam.put("startDate", startDate);
		reqParam.put("endDate", endDate);
		reqParam.put("outParkingId", outParkingId);
		reqParam.put("payType", "0".equals(payType)?null:payType);
		return dao.queryForList("TB_ORDER_PAY.getAccountList", reqParam);
	}
	
	public Integer getOrderPayEntryCount(OrderPayBean orderPayBean) {
        return (Integer) dao.queryForObject("TB_ORDER_PAY.getOrderPayEntryCount", orderPayBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<OrderPayBean> getOrderPayEntryAll(OrderPayBean orderPayBean) {
	        return dao.queryForList("TB_ORDER_PAY.getOrderPayEntryAll", orderPayBean);
	}
}
