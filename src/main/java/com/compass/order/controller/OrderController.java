package com.compass.order.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.order.model.OrderBean;
import com.compass.order.service.OrderService;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/order/order.do")
public class OrderController {
	
	private static final Logger LogPay = LoggerFactory
			.getLogger(OrderController.class);

	@Autowired
	@Qualifier("orderService")
	private OrderService orderService;
	
	@RequestMapping(params = "method=getOrder")
	@ResponseBody
	public Map<String, Object> getPark(HttpServletRequest req) {
		OrderBean orderBean = new OrderBean();
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		Integer count = orderService.getOrderCount(orderBean);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		orderBean.setStart(start);
		orderBean.setEnd(end);
		List<OrderBean> list = orderService.getOrderAll(orderBean);
		return AjaxReturnInfo.setTable(count, list);
	}
}
