package com.compass.park.controller;

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

import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/park/park.do")
public class ParkController {

	private static final Logger LogPay = LoggerFactory
			.getLogger(ParkController.class);

	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;

	/**
	 * 获取机构信息
	 * 
	 * @param agencyId
	 * @param companyName
	 * @param agencyStatus
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "method=getPark")
	@ResponseBody
	public Map<String, Object> getPark(
			@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "merchantName") String merchantName,
			HttpServletRequest req) {
		LogPay.info("***********************getPark*************outParkingId:"
				+ outParkingId + ",merchantName:" + merchantName);
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		ParkBean parkBean = new ParkBean();
		if (StringUtils.isNotBlank(outParkingId)) {
			parkBean.setOutParkingId(outParkingId);
		}
		if (StringUtils.isNotBlank(merchantName)) {
			parkBean.setMerchantName(merchantName);
		}
		// 待添加查询条件
		Integer count = parkService.getParkCount(parkBean);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		parkBean.setStart(start);
		parkBean.setEnd(end);
		List<ParkBean> list = parkService.getParkAll(parkBean);
		return AjaxReturnInfo.setTable(count, list);
	}
}
