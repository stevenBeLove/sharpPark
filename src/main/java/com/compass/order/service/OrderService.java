package com.compass.order.service;

import java.util.List;

import com.compass.order.model.OrderBean;
import com.compass.utils.AbstractService;

public class OrderService extends AbstractService{
	
	public Integer getOrderCount(OrderBean orderBean) {
        return (Integer) dao.queryForObject("TB_ORDER.getOrderCount", orderBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<OrderBean> getOrderAll(OrderBean orderBean) {
	        return dao.queryForList("TB_ORDER.getOrderAll", orderBean);
	}
}
