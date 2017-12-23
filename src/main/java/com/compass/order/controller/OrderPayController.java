package com.compass.order.controller;

import java.util.ArrayList;
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
import com.compass.utils.ConstantUtils;
import com.compass.utils.DateUtil;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/orderPay/orderPay.do")
public class OrderPayController {
	
	private static final Logger log = LoggerFactory
			.getLogger(OrderPayController.class);

	@Autowired
	@Qualifier("orderPayService")
	private OrderPayService orderPayService;
	
	@RequestMapping(params = "method=getOrderPay")
	@ResponseBody
	public Map<String, Object> getOrderPay(
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		log.info("carNumber:"+carNumber);
		String changeParkId = (String) req.getSession().getAttribute("changeParkId");
		String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		OrderPayBean orderPayBean = new OrderPayBean();
		if(StringUtils.isNotBlank(carNumber)){
			orderPayBean.setCarNumber(carNumber);
		}
		if(!ConstantUtils.CENTERCODE.equals(outParkingId)){
			orderPayBean.setOutParkingId(outParkingId);
		}else{
			orderPayBean.setOutParkingId(changeParkId);
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
	
	@RequestMapping(params = "method=getOrderPayEntry")
	@ResponseBody
	public Map<String, Object> getOrderPayEntry(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "inStatus") String inStatus,
			@RequestParam(value = "billingTyper") String billingTyper,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			HttpServletRequest req) {
		log.info("carNumber:"+carNumber);
		String bDate = " 00:00:00";
		String eDate = " 23:59:59";
		String format = "yyyy-MM-dd HH:mm:ss";
		Integer count = 0;
		List<OrderPayBean> list = new ArrayList<OrderPayBean>();
		try {
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			OrderPayBean orderPayBean = new OrderPayBean();
			if(!ConstantUtils.CENTERCODE.equals(outParkingId)){
				orderPayBean.setOutParkingId(outParkingId);
			}else{
				orderPayBean.setOutParkingId(changeParkId);
			}
			if(StringUtils.isNotBlank(carNumber)){
				orderPayBean.setCarNumber(carNumber);
			}
			if (StringUtils.isNotBlank(inStatus)) {
				orderPayBean.setInStatus(inStatus);
			}
			if (StringUtils.isNotBlank(billingTyper)) {
				orderPayBean.setBillingTyper(billingTyper);
			}
			if(StringUtils.isNotBlank(startDate)){
				orderPayBean.setStartDate(DateUtil.fromatDate(startDate+bDate, format));
			}
			if(StringUtils.isNotBlank(endDate)){
				orderPayBean.setEndDate(DateUtil.fromatDate(endDate+eDate, format));
			}
			String rows = req.getParameter("rows");
			String page = req.getParameter("page");
			count = orderPayService.getOrderPayEntryCount(orderPayBean);
			int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
					: page);
			int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
					: rows);
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > count ? count : start + rownumber;
			orderPayBean.setStart(start);
			orderPayBean.setEnd(end);
			list = orderPayService.getOrderPayEntryAll(orderPayBean);
		} catch (Exception e) {
			log.error("getOrderPayEntry----error",e);
		}
		return AjaxReturnInfo.setTable(count, list);
	}
}
