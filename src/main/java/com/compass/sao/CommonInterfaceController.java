package com.compass.sao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.DateUtil;
import com.compass.utils.IpUtils;
import com.compass.utils.Md5Util;
import com.compass.utils.PropertyPlaceholderConfigurerExt;
import com.compass.vehicle.model.FreeVehicleBrandBean;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.compass.vehicle.service.FreeVehicleBrandService;
import com.compass.vehicle.service.MonthVehicleBrandService;

@Controller
public class CommonInterfaceController {

	private static final Logger log = LoggerFactory.getLogger(CommonInterfaceController.class);
	
	private final String SIGN_KEY = (String) PropertyPlaceholderConfigurerExt.getProperties().get("parkPrice.sign");
	
	@Autowired
	@Qualifier("freeVehicleBrandService")
	public FreeVehicleBrandService freeVehicleBrandService;

	@Autowired
	@Qualifier("monthVehicleBrandService")
	public MonthVehicleBrandService monthVehicleBrandService;

	@RequestMapping(value = "/queryCarNumberType.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> queryCarNumberType(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber, HttpServletRequest req) {
		String sign = req.getParameter("sign");
		String retCode = "99";
		String retMessage = "系统异常";
		String carNumberType = "";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)) {
				retCode = "01";
				retMessage = "请求参数为空";
			} else {
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					String signValue = Md5Util.sortMapByKey(param);
					log.info("queryCarNumberType---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("queryCarNumberType---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
				monthVehicleBrandBean.setOutParkingId(outParkingId);
				monthVehicleBrandBean.setCarNumber(carNumber);
				monthVehicleBrandBean.setStopDate(Calendar.getInstance().getTime());
				FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
				freeVehicleBrandBean.setOutParkingId(outParkingId);
				freeVehicleBrandBean.setCarNumber(carNumber);
				if (freeVehicleBrandService.isFreeVehicleFlag(freeVehicleBrandBean)) {
					carNumberType = "F";
				} else if (monthVehicleBrandService.isMonthVehicleFlag(monthVehicleBrandBean)) {
					carNumberType = "M";
				} else {
					carNumberType = "L";
				}
				retCode = "00";
				retMessage = "success";
				retMap.put("carNumberType", carNumberType);
			}
		} catch (Exception e) {
			log.error("queryCarNumberType--error", e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}

	@RequestMapping(value = "/monthCardRecharge.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> monthCardRecharge(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "vehicleBrandType") String vehicleBrandType,
			@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "monthPayAmount") String monthPayAmount, HttpServletRequest req) {
		String sign = req.getParameter("sign");
		String retCode = "99";
		String retMessage = "系统异常";
		String format = "yyyy-MM-dd";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			String carOwnerName = req.getParameter("carOwnerName");
			String carOwnerPhone = req.getParameter("carOwnerPhone");
			String remark = req.getParameter("remark");
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)
					|| StringUtils.isBlank(vehicleBrandType) || StringUtils.isBlank(startDate)
					|| StringUtils.isBlank(endDate) || StringUtils.isBlank(monthPayAmount)) {
				retCode = "01";
				retMessage = "请求参数为空";
			}else{
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					param.put("vehicleBrandType", vehicleBrandType);
					param.put("startDate", startDate);
					param.put("endDate", endDate);
					param.put("monthPayAmount", monthPayAmount);
					param.put("carOwnerName",carOwnerName);
					param.put("carOwnerPhone",carOwnerPhone);
					param.put("remark", remark);
					String signValue = Md5Util.sortMapByKey(param);
					log.info("monthCardRecharge---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("monthCardRecharge---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				try {
					monthVehicleBrandBean.setStartDate(DateUtil.fromatDate(startDate, format));
					monthVehicleBrandBean.setEndDate(DateUtil.fromatDate(endDate, format));
					monthVehicleBrandBean.setMonthPayAmount(new BigDecimal(monthPayAmount));
				} catch (Exception e) {
					log.error("monthCardRecharge--format--error",e);
					retMap.put("retCode", "02");
					retMap.put("retMessage", "数据格式不正确");
					return retMap;
				}
				monthVehicleBrandBean.setCarNumber(carNumber);
				monthVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
				monthVehicleBrandBean.setCarOwnerName(carOwnerName);
				monthVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
				monthVehicleBrandBean.setRemark(remark);
				monthVehicleBrandBean.setCreateUserid("api");
				monthVehicleBrandBean.setOutParkingId(outParkingId);
				this.addLog(req, "月卡充值", ConstantUtils.OPERTYPEADD, JSONObject.toJSONString(monthVehicleBrandBean));
				boolean flag = monthVehicleBrandService.addMonthVehicleBrand(monthVehicleBrandBean);
				if(flag){
					retCode = "00";
					retMessage = "充值成功";
				}else{
					retCode = "03";
					retMessage = "充值失败";
				}
			}
		} catch (Exception e) {
			log.error("monthCardRecharge---error", e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	@RequestMapping(value = "/monthCardRefund.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> monthCardRefund(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "refundMoney") String refundMoney,
			@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
			HttpServletRequest req) {
		String sign = req.getParameter("sign");
		String retCode = "99";
		String retMessage = "系统异常";
		String format = "yyyy-MM-dd";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)
					|| StringUtils.isBlank(refundMoney) || StringUtils.isBlank(startDate)
					|| StringUtils.isBlank(endDate)) {
				retCode = "01";
				retMessage = "请求参数为空";
			}else{
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					param.put("refundMoney", refundMoney);
					param.put("startDate", startDate);
					param.put("endDate", endDate);
					String signValue = Md5Util.sortMapByKey(param);
					log.info("monthCardRefund---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("monthCardRefund---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
				try {
					monthVehicleBrandBean.setEndDate(DateUtil.fromatDate(endDate, format));
					monthVehicleBrandBean.setStartDate(DateUtil.fromatDate(startDate, format));
					monthVehicleBrandBean.setMonthPayAmount(new BigDecimal(refundMoney));
					monthVehicleBrandBean.setModifyUserid("api");
				} catch (Exception e) {
					log.error("monthCardRefund--format--error",e);
					retMap.put("retCode", "02");
					retMap.put("retMessage", "数据格式不正确");
					return retMap;
				}
				monthVehicleBrandBean.setOutParkingId(outParkingId);
				monthVehicleBrandBean.setCarNumber(carNumber);
				this.addLog(req, "月卡退款", ConstantUtils.OPERTYPERET, JSONObject.toJSONString(monthVehicleBrandBean));
				if(monthVehicleBrandService.isMonthCardRefund(monthVehicleBrandBean)){
					retCode = "00";
					retMessage = "success";
				}else{
					retCode = "03";
					retMessage = "退费失败或记录不存在";
				}
			}
		} catch (Exception e) {
			log.error("monthCardRefund---error",e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	
	@RequestMapping(value = "/addFreeCard.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addFreeCard(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		String retCode = "99";
		String retMessage = "系统异常";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)) {
				retCode = "01";
				retMessage = "请求参数为空";
			}else{
				String vehicleBrandType = req.getParameter("vehicleBrandType");
				String carOwnerName = req.getParameter("carOwnerName");
				String carOwnerPhone = req.getParameter("carOwnerPhone");
				String remark = req.getParameter("remark");
				String sign = req.getParameter("sign");
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					param.put("vehicleBrandType", vehicleBrandType);
					param.put("carOwnerName", carOwnerName);
					param.put("carOwnerPhone", carOwnerPhone);
					param.put("remark", remark);
					String signValue = Md5Util.sortMapByKey(param);
					log.info("addFreeCard---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("addFreeCard---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
				freeVehicleBrandBean.setCarNumber(carNumber);
				freeVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
				freeVehicleBrandBean.setCarOwnerName(carOwnerName);
				freeVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
				freeVehicleBrandBean.setRemark(remark);
				freeVehicleBrandBean.setStatus("1");
				freeVehicleBrandBean.setCreateUserid("api");
				freeVehicleBrandBean.setModifyDatetime(Calendar.getInstance().getTime());
				freeVehicleBrandBean.setOutParkingId(outParkingId);
				this.addLog(req, "新增免费车", ConstantUtils.OPERTYPEADD, JSONObject.toJSONString(freeVehicleBrandBean));
				boolean flag = freeVehicleBrandService.addFreeVehicleBrand(freeVehicleBrandBean);
				if(flag){
					retCode = "00";
					retMessage = "success";
				}else{
					retCode = "02";
					retMessage = "新增失败";
				}
			}
		} catch (Exception e) {
			log.error("addFreeCard---error",e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	@RequestMapping(value = "/delFreeCard.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delFreeCard(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		String retCode = "99";
		String retMessage = "系统异常";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)) {
				retCode = "01";
				retMessage = "请求参数为空";
			}else{
				String sign = req.getParameter("sign");
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					String signValue = Md5Util.sortMapByKey(param);
					log.info("delFreeCard---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("delFreeCard---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
				freeVehicleBrandBean.setCarNumber(carNumber);
				freeVehicleBrandBean.setOutParkingId(outParkingId);
				freeVehicleBrandBean.setModifyUserid("api");
				freeVehicleBrandBean.setStatus("0");
				this.addLog(req, "删除免费车", ConstantUtils.OPERTYPEDEL, JSONObject.toJSONString(freeVehicleBrandBean));
				boolean flag = freeVehicleBrandService.updateFreeByBean(freeVehicleBrandBean);
				if(flag){
					retCode = "00";
					retMessage = "success";
				}else{
					retCode = "02";
					retMessage = "删除失败";
				}
			}
		} catch (Exception e) {
			log.error("delFreeCard---error",e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	@RequestMapping(value = "/updateFreeCard.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateFreeCard(@RequestParam(value = "outParkingId") String outParkingId,
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req) {
		String retCode = "99";
		String retMessage = "系统异常";
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			if (StringUtils.isBlank(outParkingId) || StringUtils.isBlank(carNumber)) {
				retCode = "01";
				retMessage = "请求参数为空";
			}else{
				String vehicleBrandType = req.getParameter("vehicleBrandType");
				String carOwnerName = req.getParameter("carOwnerName");
				String carOwnerPhone = req.getParameter("carOwnerPhone");
				String remark = req.getParameter("remark");
				String sign = req.getParameter("sign");
				if(StringUtils.isNotBlank(sign)){
					Map<String, String> param = new HashMap<String, String>();
					param.put("outParkingId", outParkingId);
					param.put("carNumber", new String(carNumber.getBytes("utf-8")));
					param.put("vehicleBrandType", vehicleBrandType);
					param.put("carOwnerName", carOwnerName);
					param.put("carOwnerPhone", carOwnerPhone);
					param.put("remark", remark);
					String signValue = Md5Util.sortMapByKey(param);
					log.info("updateFreeCard---signValue:"+signValue);
					String nSign = Md5Util.getMd5(signValue+SIGN_KEY);
					log.info("updateFreeCard---nSign:"+nSign+",sign:"+sign);
					if(!sign.equals(nSign)){
						retMap.put("retCode", "101");
						retMap.put("retMessage", "验签失败");
						return retMap;
					}
				}
				FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
				freeVehicleBrandBean.setCarNumber(carNumber);
				freeVehicleBrandBean.setOutParkingId(outParkingId);
				freeVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
				freeVehicleBrandBean.setCarOwnerName(carOwnerName);
				freeVehicleBrandBean.setRemark(remark);
				freeVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
				freeVehicleBrandBean.setModifyUserid("api");
				this.addLog(req, "更新免费车", ConstantUtils.OPERTYPEUPD, JSONObject.toJSONString(freeVehicleBrandBean));
				boolean flag = freeVehicleBrandService.updateFreeByBean(freeVehicleBrandBean);
				if(flag){
					retCode = "00";
					retMessage = "success";
				}else{
					retCode = "02";
					retMessage = "更新失败";
				}
			}
		} catch (Exception e) {
			log.error("updateFreeCard---error",e);
		}
		retMap.put("retCode", retCode);
		retMap.put("retMessage", retMessage);
		return retMap;
	}
	
	@Autowired
    @Qualifier("systemLogService")
    private SystemLogService     systemLogService;
	
	public void addLog(HttpServletRequest req,String operName,String operType,String operateDetail){
		try {
			String ipAddress = IpUtils.getRemoteHost(req);
			systemLogService.addLog(ipAddress, null, "api", operName, operType, operateDetail);
		} catch (Exception e) {
			log.error("insert--log---error",e);
		}
	}
}
