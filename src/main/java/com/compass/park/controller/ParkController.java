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
import com.compass.utils.ConstantUtils;
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
	
	@RequestMapping(params = "method=addPark")
	@ResponseBody
	public AjaxReturnInfo addPark(
			@RequestParam(value = "merchantName") String merchantName,
			@RequestParam(value = "merchantServicePhone") String merchantServicePhone,
			@RequestParam(value = "accountNo") String accountNo,
			@RequestParam(value = "cityId") String cityId,
			@RequestParam(value = "equipmentName") String equipmentName,
			@RequestParam(value = "parkingAddress") String parkingAddress,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "parkingStartTime") String parkingStartTime,
			@RequestParam(value = "parkingEndTime") String parkingEndTime,
			@RequestParam(value = "parkingNumber") String parkingNumber,
			@RequestParam(value = "parkingLotType") String parkingLotType,
			@RequestParam(value = "parkingType") String parkingType,
			@RequestParam(value = "paymentMode") String paymentMode,
			@RequestParam(value = "payType") String payType,
			@RequestParam(value = "shopingmallId") String shopingmallId,
			@RequestParam(value = "parkingFeeDescription") String parkingFeeDescription,
			@RequestParam(value = "contactName") String contactName,
			@RequestParam(value = "contactMobile") String contactMobile,
			@RequestParam(value = "contactTel") String contactTel,
			@RequestParam(value = "contactEmali") String contactEmali,
			@RequestParam(value = "contactWeixin") String contactWeixin,
			@RequestParam(value = "contactAlipay") String contactAlipay,
			@RequestParam(value = "parkingName") String parkingName,
			@RequestParam(value = "timeOut") String timeOut,
			@RequestParam(value = "status") String status,
			HttpServletRequest req) {
		try {
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			ParkBean parkBean = new ParkBean();
			parkBean.setMerchantName(merchantName);
			parkBean.setMerchantServicePhone(merchantServicePhone);
			parkBean.setAccountNo(accountNo);
			parkBean.setCityId(cityId);
			parkBean.setEquipmentName(equipmentName);
			parkBean.setParkingAddress(parkingAddress);
			parkBean.setLongitude(longitude);
			parkBean.setLatitude(latitude);
			parkBean.setParkingStartTime(parkingStartTime);
			parkBean.setParkingEndTime(parkingEndTime);
			parkBean.setParkingNumber(parkingNumber);
			parkBean.setParkingLotType(parkingLotType);
			parkBean.setParkingType(parkingType);
			parkBean.setPaymentMode(paymentMode);
			parkBean.setPayType(payType);
			parkBean.setShopingmallId(shopingmallId);
			parkBean.setParkingFeeDescription(parkingFeeDescription);
			parkBean.setContactName(contactName);
			parkBean.setContactMobile(contactMobile);
			parkBean.setContactTel(contactTel);
			parkBean.setContactEmali(contactEmali);
			parkBean.setContactWeixin(contactWeixin);
			parkBean.setContactAlipay(contactAlipay);
			parkBean.setParkingName(parkingName);
			parkBean.setTimeOut(timeOut);
			parkBean.setStatus(status);
			parkBean.setCreateUserId(userId);
			boolean flag = this.parkService.addPark(parkBean);
			if(flag){
				return AjaxReturnInfo.success("保存成功");
			}else{
				return AjaxReturnInfo.failed("保存失败");
			}
		} catch (Exception e) {
			LogPay.error("addPark---error",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	
	@RequestMapping(params = "method=editPark")
	@ResponseBody
	public AjaxReturnInfo editPark(
			@RequestParam(value = "merchantName") String merchantName,
			@RequestParam(value = "merchantServicePhone") String merchantServicePhone,
			@RequestParam(value = "accountNo") String accountNo,
			@RequestParam(value = "cityId") String cityId,
			@RequestParam(value = "equipmentName") String equipmentName,
			@RequestParam(value = "parkingAddress") String parkingAddress,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "parkingStartTime") String parkingStartTime,
			@RequestParam(value = "parkingEndTime") String parkingEndTime,
			@RequestParam(value = "parkingNumber") String parkingNumber,
			@RequestParam(value = "parkingLotType") String parkingLotType,
			@RequestParam(value = "parkingType") String parkingType,
			@RequestParam(value = "paymentMode") String paymentMode,
			@RequestParam(value = "payType") String payType,
			@RequestParam(value = "shopingmallId") String shopingmallId,
			@RequestParam(value = "parkingFeeDescription") String parkingFeeDescription,
			@RequestParam(value = "contactName") String contactName,
			@RequestParam(value = "contactMobile") String contactMobile,
			@RequestParam(value = "contactTel") String contactTel,
			@RequestParam(value = "contactEmali") String contactEmali,
			@RequestParam(value = "contactWeixin") String contactWeixin,
			@RequestParam(value = "contactAlipay") String contactAlipay,
			@RequestParam(value = "parkingName") String parkingName,
			@RequestParam(value = "timeOut") String timeOut,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "outParkingId") String outParkingId,
			HttpServletRequest req) {
		try {
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			ParkBean parkBean = new ParkBean();
			parkBean.setMerchantName(merchantName);
			parkBean.setMerchantServicePhone(merchantServicePhone);
			parkBean.setAccountNo(accountNo);
			parkBean.setCityId(cityId);
			parkBean.setEquipmentName(equipmentName);
			parkBean.setParkingAddress(parkingAddress);
			parkBean.setLongitude(longitude);
			parkBean.setLatitude(latitude);
			parkBean.setParkingStartTime(parkingStartTime);
			parkBean.setParkingEndTime(parkingEndTime);
			parkBean.setParkingNumber(parkingNumber);
			parkBean.setParkingLotType(parkingLotType);
			parkBean.setParkingType(parkingType);
			parkBean.setPaymentMode(paymentMode);
			parkBean.setPayType(payType);
			parkBean.setShopingmallId(shopingmallId);
			parkBean.setParkingFeeDescription(parkingFeeDescription);
			parkBean.setContactName(contactName);
			parkBean.setContactMobile(contactMobile);
			parkBean.setContactTel(contactTel);
			parkBean.setContactEmali(contactEmali);
			parkBean.setContactWeixin(contactWeixin);
			parkBean.setContactAlipay(contactAlipay);
			parkBean.setParkingName(parkingName);
			parkBean.setTimeOut(timeOut);
			parkBean.setStatus(status);
			parkBean.setOutParkingId(outParkingId);
			parkBean.setModifyUserId(userId);
			boolean flag = this.parkService.updateParkById(parkBean);
			if(flag){
				return AjaxReturnInfo.success("更新成功");
			}else{
				return AjaxReturnInfo.failed("更新失败");
			}
		} catch (Exception e) {
			LogPay.error("updatePark---error",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
}
