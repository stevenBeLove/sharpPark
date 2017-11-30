package com.compass.order.service;

import java.util.List;

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
}
