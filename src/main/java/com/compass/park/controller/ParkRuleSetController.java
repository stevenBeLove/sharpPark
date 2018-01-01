package com.compass.park.controller;

import java.math.BigDecimal;
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

import com.compass.park.model.ParkRuleSetBean;
import com.compass.park.service.ParkRuleSetService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/parkRuleSet/parkRuleSet.do")
public class ParkRuleSetController {
	
	private static final Logger log = LoggerFactory
			.getLogger(ParkRuleSetController.class);
	
	@Autowired
	@Qualifier("parkRuleSetService")
	private ParkRuleSetService parkRuleSetService;
	
	@RequestMapping(params = "method=getParkRuleSet")
	@ResponseBody
	public Map<String, Object> getParkRuleSet(
			HttpServletRequest req) {
		Integer count = 0;
		String changeParkId = (String) req.getSession().getAttribute("changeParkId");
		String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		//测试用
		outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
		List<ParkRuleSetBean> list = new ArrayList<ParkRuleSetBean>();
		try {
			ParkRuleSetBean parkRuleSetBean = new ParkRuleSetBean();
			String rows = req.getParameter("rows");
			String page = req.getParameter("page");
			parkRuleSetBean.setOutParkingId(outParkingId);
			count = parkRuleSetService.getParkRuleSetCount(parkRuleSetBean);
			int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
					: page);
			int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
					: rows);
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > count ? count : start + rownumber;
			parkRuleSetBean.setStart(start);
			parkRuleSetBean.setEnd(end);
			list = parkRuleSetService.getParkRuleSetAll(parkRuleSetBean);
		} catch (Exception e) {
			log.error("查询规则列表异常",e);
		}
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=editParkRule")
	@ResponseBody
	public AjaxReturnInfo editParkRule(
			@RequestParam(value = "parkRuleSetId") String parkRuleSetId,
			HttpServletRequest req) {
		try {
			String ruleName = req.getParameter("ruleName");
			String vehicleType = req.getParameter("vehicleType");
			String dayLimit = req.getParameter("dayLimit");
			String startHour = req.getParameter("startHour");
			String endHour = req.getParameter("endHour");
			String dateSet = req.getParameter("dateSet");
			String chargeType = req.getParameter("chargeType");
			String subCharge = req.getParameter("subCharge");
			String timeSlotLimit = req.getParameter("timeSlotLimit");
			String startChargeTime = req.getParameter("startChargeTime");
			String startChargePrice = req.getParameter("startChargePrice");
			String chargeTime = req.getParameter("chargeTime");
			String chargePrice = req.getParameter("chargePrice");
			String nChargeType = req.getParameter("nChargeType");
			String nFreeTime = req.getParameter("nFreeTime");
			String nSubCharge = req.getParameter("nSubCharge");
			String nTimeSlotLimit = req.getParameter("nTimeSlotLimit");
			String nStartChargeTime = req.getParameter("nStartChargeTime");
			String nStartChargePrice = req.getParameter("nStartChargePrice");
			String nChargeTime = req.getParameter("nChargeTime");
			String nChargePrice = req.getParameter("nChargePrice");
			String nStartHour = req.getParameter("nStartHour");
			String nEndHour = req.getParameter("nEndHour");
			String type = req.getParameter("type");
			String subFreeTime = req.getParameter("subFreeTime");
			String nSubFreeTime = req.getParameter("nSubFreeTime");
			String status = req.getParameter("status");
			String freeTime = req.getParameter("freeTime");
			
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			//测试用
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			ParkRuleSetBean parkRuleSetBean = new ParkRuleSetBean();
			parkRuleSetBean.setRuleName(ruleName);
			parkRuleSetBean.setOutParkingId(outParkingId);
			parkRuleSetBean.setVehicleType(vehicleType);
			parkRuleSetBean.setDayLimit(new BigDecimal(StringUtils.isEmpty(dayLimit)?"0":dayLimit));
			parkRuleSetBean.setStartHour(startHour);
			parkRuleSetBean.setEndHour(endHour);
			parkRuleSetBean.setDateSet(dateSet);
			parkRuleSetBean.setParkRuleSetId(parkRuleSetId);
			parkRuleSetBean.setChargeType(chargeType);
			parkRuleSetBean.setSubCharge(new BigDecimal(StringUtils.isEmpty(subCharge)?"0":subCharge));
			parkRuleSetBean.setTimeSlotLimit(new BigDecimal(StringUtils.isEmpty(timeSlotLimit)?"0":timeSlotLimit));
			parkRuleSetBean.setStartChargeTime(Long.valueOf(StringUtils.isEmpty(startChargeTime)?"0":startChargeTime));
			parkRuleSetBean.setStartChargePrice(new BigDecimal(StringUtils.isEmpty(startChargePrice)?"0":startChargePrice));
			parkRuleSetBean.setChargeTime(Long.valueOf(StringUtils.isEmpty(chargeTime)?"0":chargeTime));
			parkRuleSetBean.setChargePrice(new BigDecimal(StringUtils.isEmpty(chargePrice)?"0":chargePrice));
			parkRuleSetBean.setnChargeType(nChargeType);
			parkRuleSetBean.setnFreeTime(Long.valueOf(StringUtils.isEmpty(nFreeTime)?"0":nFreeTime));
			parkRuleSetBean.setnSubCharge(new BigDecimal(StringUtils.isEmpty(nSubCharge)?"0":nSubCharge));
			parkRuleSetBean.setnTimeSlotLimit(new BigDecimal(StringUtils.isEmpty(nTimeSlotLimit)?"0":nTimeSlotLimit));
			parkRuleSetBean.setnStartChargeTime(Long.valueOf(StringUtils.isEmpty(nStartChargeTime)?"0":nStartChargeTime));
			parkRuleSetBean.setnStartChargePrice(new BigDecimal(StringUtils.isEmpty(nStartChargePrice)?"0":nStartChargePrice));
			parkRuleSetBean.setnChargeTime(Long.valueOf(StringUtils.isEmpty(nChargeTime)?"0":nChargeTime));
			parkRuleSetBean.setnChargePrice(new BigDecimal(StringUtils.isEmpty(nChargePrice)?"0":nChargePrice));
			parkRuleSetBean.setFreeTime(Long.valueOf(StringUtils.isEmpty(freeTime)?"0":freeTime));
			parkRuleSetBean.setnStartHour(nStartHour);
			parkRuleSetBean.setnEndHour(nEndHour);
			parkRuleSetBean.setType(type);
			parkRuleSetBean.setSubFreeTime(Long.valueOf(StringUtils.isEmpty(subFreeTime)?"0":subFreeTime));
			parkRuleSetBean.setnSubFreeTime(Long.valueOf(StringUtils.isEmpty(nSubFreeTime)?"0":nSubFreeTime));
			parkRuleSetBean.setStatus(status);
			parkRuleSetBean.setModifyUserid(userId);
			boolean flag = parkRuleSetService.updateParkRuleSetById(parkRuleSetBean);
			if(flag){
				return AjaxReturnInfo.success("更新成功");
			}else{
				return AjaxReturnInfo.failed("更新失败");
			}
		} catch (Exception e) {
			log.error("规则修改异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=addParkRule")
	@ResponseBody
	public AjaxReturnInfo addParkRule(
			HttpServletRequest req) {
		try {
			String ruleName = req.getParameter("ruleName");
			String vehicleType = req.getParameter("vehicleType");
			String dayLimit = req.getParameter("dayLimit");
			String startHour = req.getParameter("startHour");
			String endHour = req.getParameter("endHour");
			String dateSet = req.getParameter("dateSet");
			String chargeType = req.getParameter("chargeType");
			String subCharge = req.getParameter("subCharge");
			String timeSlotLimit = req.getParameter("timeSlotLimit");
			String startChargeTime = req.getParameter("startChargeTime");
			String startChargePrice = req.getParameter("startChargePrice");
			String chargeTime = req.getParameter("chargeTime");
			String chargePrice = req.getParameter("chargePrice");
			String nChargeType = req.getParameter("nChargeType");
			String nFreeTime = req.getParameter("nFreeTime");
			String nSubCharge = req.getParameter("nSubCharge");
			String nTimeSlotLimit = req.getParameter("nTimeSlotLimit");
			String nStartChargeTime = req.getParameter("nStartChargeTime");
			String nStartChargePrice = req.getParameter("nStartChargePrice");
			String nChargeTime = req.getParameter("nChargeTime");
			String nChargePrice = req.getParameter("nChargePrice");
			String nStartHour = req.getParameter("nStartHour");
			String nEndHour = req.getParameter("nEndHour");
			String type = req.getParameter("type");
			String subFreeTime = req.getParameter("subFreeTime");
			String nSubFreeTime = req.getParameter("nSubFreeTime");
			String status = req.getParameter("status");
			String freeTime = req.getParameter("freeTime");
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			//测试用
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			ParkRuleSetBean parkRuleSetBean = new ParkRuleSetBean();
			parkRuleSetBean.setOutParkingId(outParkingId);
			parkRuleSetBean.setRuleName(ruleName);
			parkRuleSetBean.setVehicleType(vehicleType);
			parkRuleSetBean.setDayLimit(new BigDecimal(StringUtils.isEmpty(dayLimit)?"0":dayLimit));
			parkRuleSetBean.setStartHour(startHour);
			parkRuleSetBean.setEndHour(endHour);
			parkRuleSetBean.setDateSet(dateSet);
			parkRuleSetBean.setChargeType(chargeType);
			parkRuleSetBean.setFreeTime(Long.valueOf(StringUtils.isEmpty(freeTime)?"0":freeTime));
			parkRuleSetBean.setSubCharge(new BigDecimal(StringUtils.isEmpty(subCharge)?"0":subCharge));
			parkRuleSetBean.setTimeSlotLimit(new BigDecimal(StringUtils.isEmpty(timeSlotLimit)?"0":timeSlotLimit));
			parkRuleSetBean.setStartChargeTime(Long.valueOf(StringUtils.isEmpty(startChargeTime)?"0":startChargeTime));
			parkRuleSetBean.setStartChargePrice(new BigDecimal(StringUtils.isEmpty(startChargePrice)?"0":startChargePrice));
			parkRuleSetBean.setChargeTime(Long.valueOf(StringUtils.isEmpty(chargeTime)?"0":chargeTime));
			parkRuleSetBean.setChargePrice(new BigDecimal(StringUtils.isEmpty(chargePrice)?"0":chargePrice));
			parkRuleSetBean.setnChargeType(nChargeType);
			parkRuleSetBean.setnFreeTime(Long.valueOf(StringUtils.isEmpty(nFreeTime)?"0":nFreeTime));
			parkRuleSetBean.setnSubCharge(new BigDecimal(StringUtils.isEmpty(nSubCharge)?"0":nSubCharge));
			parkRuleSetBean.setnTimeSlotLimit(new BigDecimal(StringUtils.isEmpty(nTimeSlotLimit)?"0":nTimeSlotLimit));
			parkRuleSetBean.setnStartChargeTime(Long.valueOf(StringUtils.isEmpty(nStartChargeTime)?"0":nStartChargeTime));
			parkRuleSetBean.setnStartChargePrice(new BigDecimal(StringUtils.isEmpty(nStartChargePrice)?"0":nStartChargePrice));
			parkRuleSetBean.setnChargeTime(Long.valueOf(StringUtils.isEmpty(nChargeTime)?"0":nChargeTime));
			parkRuleSetBean.setnChargePrice(new BigDecimal(StringUtils.isEmpty(nChargePrice)?"0":nChargePrice));
			parkRuleSetBean.setnStartHour(nStartHour);
			parkRuleSetBean.setnEndHour(nEndHour);
			parkRuleSetBean.setType(type);
			parkRuleSetBean.setSubFreeTime(Long.valueOf(StringUtils.isEmpty(subFreeTime)?"0":subFreeTime));
			parkRuleSetBean.setnSubFreeTime(Long.valueOf(StringUtils.isEmpty(nSubFreeTime)?"0":nSubFreeTime));
			parkRuleSetBean.setStatus(status);
			parkRuleSetBean.setCreateUserid(userId);
			boolean flag = parkRuleSetService.addParkRuleSet(parkRuleSetBean);
			if(flag){
				return AjaxReturnInfo.success("成功");
			}else{
				return AjaxReturnInfo.failed("失败");
			}
		} catch (Exception e) {
			log.error("规则新增异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	
	
}
