package com.compass.vehicle.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.utils.ConstantUtils;
import com.compass.utils.ExcelUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.vehicle.model.FreeVehicleBrandBean;
import com.compass.vehicle.model.FreeVehicleBrandExportBean;
import com.compass.vehicle.service.FreeVehicleBrandService;

@Controller
@RequestMapping("/freeVehicleBrand/freeVehicleBrand.do")
public class FreeVehicleBrandController {

	private static final Logger log = LoggerFactory
			.getLogger(FreeVehicleBrandController.class);
	
	@Autowired
	@Qualifier("freeVehicleBrandService")
	private FreeVehicleBrandService freeVehicleBrandService;
	
	@RequestMapping(params = "method=getFreeVehicleBrand")
	@ResponseBody
	public Map<String, Object> getFreeVehicleBrand(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			HttpServletRequest req) {
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		FreeVehicleBrandBean freeVehicleBrandBean= new FreeVehicleBrandBean();
		if (StringUtils.isNotBlank(carNumber)) {
			freeVehicleBrandBean.setCarNumber(carNumber);
		}
		if (StringUtils.isNotBlank(carOwnerName)) {
			freeVehicleBrandBean.setCarOwnerName(carOwnerName);
		}
		// 待添加查询条件
		Integer count = freeVehicleBrandService.getFreeVehicleBrandCount(freeVehicleBrandBean);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		freeVehicleBrandBean.setStart(start);
		freeVehicleBrandBean.setEnd(end);
		List<FreeVehicleBrandBean> list = freeVehicleBrandService.getFreeVehicleBrandAll(freeVehicleBrandBean);
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=editFreeVehicleBrand")
	@ResponseBody
	public AjaxReturnInfo editFreeVehicleBrand(
			@RequestParam(value = "freeVehicleBrandId") String freeVehicleBrandId,
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "vehicleBrandType") String vehicleBrandType,
			@RequestParam(value = "vehiclePlace") String vehiclePlace,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			@RequestParam(value = "carOwnerAddres") String carOwnerAddres,
			@RequestParam(value = "carOwnerPhone") String carOwnerPhone,
			@RequestParam(value = "carOwnerEmail") String carOwnerEmail,
			@RequestParam(value = "remark") String remark,
			@RequestParam(value = "vehicleBrand") String vehicleBrand,
			@RequestParam(value = "status") String status,
			HttpServletRequest req) {
		try {
			String outParkingId = "100020";//后续从session中获取
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
			freeVehicleBrandBean.setFreeVehicleBrandId(freeVehicleBrandId);
			freeVehicleBrandBean.setCarNumber(carNumber);
			freeVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
			freeVehicleBrandBean.setVehiclePlace(vehiclePlace);
			freeVehicleBrandBean.setCarOwnerName(carOwnerName);
			freeVehicleBrandBean.setCarOwnerAddres(carOwnerAddres);
			freeVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
			freeVehicleBrandBean.setCarOwnerEmail(carOwnerEmail);
			freeVehicleBrandBean.setRemark(remark);
			freeVehicleBrandBean.setVehicleBrand(vehicleBrand);
			freeVehicleBrandBean.setStatus(status);
			freeVehicleBrandBean.setModifyUserid(userId);
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			boolean flag = freeVehicleBrandService.updateFreeVehicleBrandById(freeVehicleBrandBean);
			if(flag){
				return AjaxReturnInfo.success("更新成功");
			}else{
				return AjaxReturnInfo.failed("更新失败");
			}
		} catch (Exception e) {
			log.error("免费车修改异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=addFreeVehicleBrand")
	@ResponseBody
	public AjaxReturnInfo addFreeVehicleBrand(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "vehicleBrandType") String vehicleBrandType,
			@RequestParam(value = "vehiclePlace") String vehiclePlace,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			@RequestParam(value = "carOwnerAddres") String carOwnerAddres,
			@RequestParam(value = "carOwnerPhone") String carOwnerPhone,
			@RequestParam(value = "carOwnerEmail") String carOwnerEmail,
			@RequestParam(value = "remark") String remark,
			@RequestParam(value = "vehicleBrand") String vehicleBrand,
			@RequestParam(value = "status") String status,
			HttpServletRequest req) {
		try {
			String outParkingId = "100020";
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
			freeVehicleBrandBean.setCarNumber(carNumber);
			freeVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
			freeVehicleBrandBean.setVehiclePlace(vehiclePlace);
			freeVehicleBrandBean.setCarOwnerName(carOwnerName);
			freeVehicleBrandBean.setCarOwnerAddres(carOwnerAddres);
			freeVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
			freeVehicleBrandBean.setCarOwnerEmail(carOwnerEmail);
			freeVehicleBrandBean.setRemark(remark);
			freeVehicleBrandBean.setVehicleBrand(vehicleBrand);
			freeVehicleBrandBean.setStatus(status);
			freeVehicleBrandBean.setCreateUserid(userId);
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			boolean flag = freeVehicleBrandService.addFreeVehicleBrand(freeVehicleBrandBean);
			if(flag){
				return AjaxReturnInfo.success("新增成功");
			}else{
				return AjaxReturnInfo.failed("新增失败");
			}
		} catch (Exception e) {
			log.error("免费车新增异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=freeVehicleBrandExport")
	@ResponseBody
	public void freeVehicleBrandExport(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			HttpServletRequest req,HttpServletResponse response) {
		try {
			String outParkingId = "100020";
			FreeVehicleBrandBean freeVehicleBrandBean= new FreeVehicleBrandBean();
			if (StringUtils.isNotBlank(carNumber)) {
				freeVehicleBrandBean.setCarNumber(carNumber);
			}
			if (StringUtils.isNotBlank(carOwnerName)) {
				freeVehicleBrandBean.setCarOwnerName(carOwnerName);
			}
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			List<FreeVehicleBrandExportBean> list = freeVehicleBrandService.getFreeVehicleBrandExport(freeVehicleBrandBean);
			ExcelUtils.exportDataToExcel(response, list, new String[]{"车牌号","车牌类型","车主姓名","车主住址","联系电话","电子邮件","备注","状态","车辆品牌"}, "免费车信息", ".xls");
		} catch (Exception e) {
			log.error("freeVehicleBrandExport导出异常",e);
		}
		
	}
}
