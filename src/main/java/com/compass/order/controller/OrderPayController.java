package com.compass.order.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.order.model.OrderPayBean;
import com.compass.order.service.OrderPayService;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/orderPay/orderPay.do")
public class OrderPayController {
	
	private static final Logger LogPay = LoggerFactory
			.getLogger(OrderPayController.class);

	@Autowired
	@Qualifier("orderPayService")
	private OrderPayService orderPayService;
	
	@RequestMapping(params = "method=getOrderPay")
	@ResponseBody
	public Map<String, Object> getOrderPay(
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		LogPay.info("carNumber:"+carNumber);
		OrderPayBean orderPayBean = new OrderPayBean();
		if(StringUtils.isNotBlank(carNumber)){
			orderPayBean.setCarNumber(carNumber);
		}
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		Integer count = orderPayService.getOrderPayCount(orderPayBean);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		orderPayBean.setStart(start);
		orderPayBean.setEnd(end);
		List<OrderPayBean> list = orderPayService.getOrderPayAll(orderPayBean);
		return AjaxReturnInfo.setTable(count, list);
	}
}
