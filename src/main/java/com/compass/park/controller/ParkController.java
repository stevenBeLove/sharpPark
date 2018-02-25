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

import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyBean;
import com.compass.agency.service.AgencyService;
import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.role.service.RoleService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.userrole.model.UserRoleBean;
import com.compass.userrole.service.UserRoleService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.Md5Util;
import com.compass.utils.CommonEnums.AgencyStatus;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/park/park.do")
public class ParkController {

	private static final Logger log = LoggerFactory
			.getLogger(ParkController.class);

	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;

	@Autowired
	@Qualifier("userRoleService")
    private UserRoleService userRoleService;
	
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;
	
	@Autowired
    @Qualifier("agencyService")
    private AgencyService        agencyService;
	
	@Autowired
    @Qualifier("systemLogService")
    private SystemLogService     systemLogService;
	
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
		log.info("***********************getPark*************outParkingId:"
				+ outParkingId + ",merchantName:" + merchantName);
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		ParkBean parkBean = new ParkBean();
		String loginOutParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		if(!ConstantUtils.CENTERCODE.equals(loginOutParkingId)){
			parkBean.setOutParkingId(loginOutParkingId);
			parkBean.setIsSubPark("1");
		}else{
			if(StringUtils.isNotBlank(outParkingId)){
				parkBean.setOutParkingId(outParkingId);
			}
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
	
	@RequestMapping(params="/queryAllPark")
	@ResponseBody
	public List<ParkBean> queryAllPark(){
		ParkBean parkBean = new ParkBean();
		parkBean.setStart(0);
		parkBean.setEnd(1000);
		List<ParkBean> list = parkService.getParkAll(parkBean);
		return list;
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
			String loginOutParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			String createId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
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
			parkBean.setCreateUserId(createId);
			String loutParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			if(ConstantUtils.CENTERCODE.equals(loutParkingId)){
				String lcount = req.getParameter("licenseCount");
				if(StringUtils.isNotBlank(lcount)){
					parkBean.setLicenseCount(Integer.valueOf(lcount));
				}
			}else{
				parkBean.setLicenseCount(0);
			}
			String outParkingId = parkService.getParkSeq();
			parkBean.setOutParkingId(outParkingId);
			log.info("addPark："+JSONObject.toJSONString(parkBean));
			boolean flag = this.parkService.addPark(parkBean);
			if(flag){
				String createDt = CommonDate.getDate();
				AgencyBean agency = new AgencyBean();
                agency.setSystemId(outParkingId);
                agency.setAgency_id(outParkingId);
                agency.setCompanyName(merchantName);
                agency.setUpperAgencyid(loginOutParkingId);
                agency.setVestagencyId(outParkingId);
                agency.setContactsName(contactName);
                agency.setCompanyPhone(contactMobile);
                agency.setCompanyEmail(contactEmali);
                agency.setCompanyAddr(parkingAddress);
                agency.setCityId(cityId);
                agency.setAgency_status(AgencyStatus.auditPass.getVal());
                agency.setCreateId(createId);
                agency.setCreateDt(createDt);
                agency.setVestBrandId(outParkingId);
				agencyService.AddAgency(agency);
				String userId=userRoleService.getUserId().toString();
				UserRoleBean userRoleBean = new UserRoleBean();
				userRoleBean.setUserId(userId);
				userRoleBean.setUserName(outParkingId);
				userRoleBean.setLoginName(outParkingId);
				userRoleBean.setPassword(Md5Util.getMd5(outParkingId));
				userRoleBean.setEmail(contactEmali);
				userRoleBean.setPhone(contactMobile);
				userRoleBean.setStatus(status);
				userRoleBean.setAgencyId(outParkingId);
				userRoleBean.setCreateId(createId);
				userRoleBean.setCreateDt(createDt);
				//默认添加管理员
				List<Map<String,Object>> maplist=roleService.getRoleId(ConstantUtils.ROLETYPEADMIN);
				//检查用户的登录名称是否存在 
				int checkuser=userRoleService.checkUserAdd(outParkingId, outParkingId);
				if(checkuser==0){
					String roleId=maplist.get(0).get("ROLEID").toString();
					int resule = userRoleService.getInsert(userRoleBean);
					int resule1= userRoleService.addUserRole(userId,roleId,createId,createDt);
					if(resule*resule1==0){
						log.info(outParkingId+"保存失败");
					}
					log.info(outParkingId+"保存完成");
				}else{
					log.info(outParkingId+"登录名已经被占用!");
				}
				return AjaxReturnInfo.success("保存成功");
			}else{
				return AjaxReturnInfo.failed("保存失败");
			}
		} catch (Exception e) {
			log.error("addPark---error",e);
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
			String loutParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			if(ConstantUtils.CENTERCODE.equals(loutParkingId)){
				String lcount = req.getParameter("licenseCount");
				if(StringUtils.isNotBlank(lcount)){
					parkBean.setLicenseCount(Integer.valueOf(lcount));
				}
			}
			log.info("editPark"+JSONObject.toJSONString(parkBean));
			boolean flag = this.parkService.updateParkById(parkBean);
			if(flag){
				return AjaxReturnInfo.success("更新成功");
			}else{
				return AjaxReturnInfo.failed("更新失败");
			}
		} catch (Exception e) {
			log.error("updatePark---error",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=changePark")
	@ResponseBody
	public AjaxReturnInfo changePark(@RequestParam(value = "changeParkId") String changeParkId,
			HttpServletRequest req){
		String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		if(ConstantUtils.CENTERCODE.equals(outParkingId)){
			if(StringUtils.isNotBlank(changeParkId)){
				req.getSession().setAttribute("changeParkId", changeParkId);
				return AjaxReturnInfo.failed("切换成功");
			}else{
				return AjaxReturnInfo.failed("切换停车场不能为空");
			}
		}
		return AjaxReturnInfo.failed("无操作权限");
	}
}
