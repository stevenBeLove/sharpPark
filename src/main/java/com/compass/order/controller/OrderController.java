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

import com.compass.order.model.OrderBean;
import com.compass.order.service.OrderService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.compass.vehicle.service.MonthVehicleBrandService;

@Controller
@RequestMapping("/order/order.do")
public class OrderController {
	
	private static final Logger LogPay = LoggerFactory
			.getLogger(OrderController.class);

	@Autowired
	@Qualifier("orderService")
	private OrderService orderService;
	
	@Autowired
	@Qualifier("monthVehicleBrandService")
	private MonthVehicleBrandService monthVehicleBrandService;
	
	@RequestMapping(params = "method=getOrder")
	@ResponseBody
	public Map<String, Object> getOrder(
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		LogPay.info("carNumber:"+carNumber);
		String changeParkId = (String) req.getSession().getAttribute("changeParkId");
		String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		OrderBean orderBean = new OrderBean();
		if(StringUtils.isNotBlank(carNumber)){
			orderBean.setCarNumber(carNumber);
		}
		if(!ConstantUtils.CENTERCODE.equals(outParkingId)){
			orderBean.setOutParkingId(outParkingId);
		}else{
			orderBean.setOutParkingId(changeParkId);
		}
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		orderBean.setStatus("0");
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
		for (OrderBean bean : list) {
			MonthVehicleBrandBean brandBean = monthVehicleBrandService.queryMonthVehicleByCarNumber(bean.getOutParkingId(),bean.getCarNumber());
			if(brandBean!=null){
				bean.setCarOwnerPhone(brandBean.getCarOwnerPhone());
				bean.setCarOwnerName(brandBean.getCarOwnerName());
				bean.setRemark(brandBean.getRemark());
			}
		}
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=getExcepOrder")
	@ResponseBody
	public Map<String, Object> getExcepOrder(
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		LogPay.info("carNumber:"+carNumber);
		String changeParkId = (String) req.getSession().getAttribute("changeParkId");
		String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		OrderBean orderBean = new OrderBean();
		if(StringUtils.isNotBlank(carNumber)){
			orderBean.setCarNumber(carNumber);
		}
		if(!ConstantUtils.CENTERCODE.equals(outParkingId)){
			orderBean.setOutParkingId(outParkingId);
		}else{
			orderBean.setOutParkingId(changeParkId);
		}
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		orderBean.setStatus("1");
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
