package com.compass.vehicle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.ExcelUtils;
import com.compass.utils.PropertyPlaceholderConfigurerExt;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.compass.vehicle.model.MonthVehicleBrandExportBean;
import com.compass.vehicle.service.MonthVehicleBrandService;

@Controller
@RequestMapping("/monthVehicleBrand/monthVehicleBrand.do")
public class MonthVehicleBrandController {
	
	private static final Logger log = LoggerFactory
			.getLogger(MonthVehicleBrandController.class);
	
	@Autowired
	@Qualifier("monthVehicleBrandService")
	private MonthVehicleBrandService monthVehicleBrandService;
	
	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;
	
	private static final String FILE_UPLOAD= (String) PropertyPlaceholderConfigurerExt.getProperties().get("file.upload");
	
	public List<Integer> NCELL = Arrays.asList(0);
	
	@RequestMapping(params = "method=getFreeVehicleBrand")
	@ResponseBody
	public Map<String, Object> getFreeVehicleBrand(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			@RequestParam(value = "inStatus") String inStatus,
			@RequestParam(value = "isExpire") String isExpire,
			HttpServletRequest req) {
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		MonthVehicleBrandBean monthVehicleBrandBean= new MonthVehicleBrandBean();
		if (StringUtils.isNotBlank(carNumber)) {
			monthVehicleBrandBean.setCarNumber(carNumber);
		}
		if (StringUtils.isNotBlank(carOwnerName)) {
			monthVehicleBrandBean.setCarOwnerName(carOwnerName);
		}
		if (StringUtils.isNotBlank(inStatus)) {
			monthVehicleBrandBean.setInStatus(inStatus);
		}
		if (StringUtils.isNotBlank(isExpire)) {
			monthVehicleBrandBean.setIsExpire(isExpire);
		}
		// 待添加查询条件
		Integer count = monthVehicleBrandService.getMonthVehicleBrandCount(monthVehicleBrandBean);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		monthVehicleBrandBean.setStart(start);
		monthVehicleBrandBean.setEnd(end);
		List<MonthVehicleBrandBean> list = monthVehicleBrandService.getMonthVehicleBrandAll(monthVehicleBrandBean);
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=editMonthVehicleBrand")
	@ResponseBody
	public AjaxReturnInfo editFreeVehicleBrand(
			@RequestParam(value = "monthVehicleBrandId") String monthVehicleBrandId,
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "vehicleBrandType") String vehicleBrandType,
			@RequestParam(value = "vehiclePlace") String vehiclePlace,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			@RequestParam(value = "carOwnerAddres") String carOwnerAddres,
			@RequestParam(value = "carOwnerPhone") String carOwnerPhone,
			@RequestParam(value = "carOwnerEmail") String carOwnerEmail,
			@RequestParam(value = "remark") String remark,
			@RequestParam(value = "vehicleBrand") String vehicleBrand,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "monthPayAmount") String monthPayAmount,
			HttpServletRequest req) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String outParkingId = "10180";//后续从session中获取
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			monthVehicleBrandBean.setMonthVehicleBrandId(monthVehicleBrandId);
			monthVehicleBrandBean.setCarNumber(carNumber);
			monthVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
			monthVehicleBrandBean.setVehiclePlace(vehiclePlace);
			monthVehicleBrandBean.setCarOwnerName(carOwnerName);
			monthVehicleBrandBean.setCarOwnerAddres(carOwnerAddres);
			monthVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
			monthVehicleBrandBean.setCarOwnerEmail(carOwnerEmail);
			monthVehicleBrandBean.setRemark(remark);
			monthVehicleBrandBean.setVehicleBrand(vehicleBrand);
			monthVehicleBrandBean.setModifyUserid(userId);
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			if(StringUtils.isNotBlank(startDate)){
				monthVehicleBrandBean.setStartDate(dateFormat.parse(startDate));
			}
			if(StringUtils.isNotBlank(endDate)){
				monthVehicleBrandBean.setEndDate(dateFormat.parse(endDate));
			}
			if(StringUtils.isNotBlank(monthPayAmount)){
				monthVehicleBrandBean.setMonthPayAmount(new BigDecimal(monthPayAmount));
			}
			boolean flag = monthVehicleBrandService.updateMonthVehicleBrandById(monthVehicleBrandBean);
			if(flag){
				return AjaxReturnInfo.success("更新成功");
			}else{
				return AjaxReturnInfo.failed("更新失败");
			}
		} catch (Exception e) {
			log.error("月卡管理修改异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=addMonthVehicleBrand")
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
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "monthPayAmount") String monthPayAmount,
			HttpServletRequest req) {
		try {
			String outParkingId = "10180";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			monthVehicleBrandBean.setCarNumber(carNumber);
			monthVehicleBrandBean.setVehicleBrandType(vehicleBrandType);
			monthVehicleBrandBean.setVehiclePlace(vehiclePlace);
			monthVehicleBrandBean.setCarOwnerName(carOwnerName);
			monthVehicleBrandBean.setCarOwnerAddres(carOwnerAddres);
			monthVehicleBrandBean.setCarOwnerPhone(carOwnerPhone);
			monthVehicleBrandBean.setCarOwnerEmail(carOwnerEmail);
			monthVehicleBrandBean.setRemark(remark);
			monthVehicleBrandBean.setVehicleBrand(vehicleBrand);
			monthVehicleBrandBean.setCreateUserid(userId);
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			if(StringUtils.isNotBlank(startDate)){
				monthVehicleBrandBean.setStartDate(dateFormat.parse(startDate));
			}
			if(StringUtils.isNotBlank(endDate)){
				monthVehicleBrandBean.setEndDate(dateFormat.parse(endDate));
			}
			if(StringUtils.isNotBlank(monthPayAmount)){
				monthVehicleBrandBean.setMonthPayAmount(new BigDecimal(monthPayAmount));
			}
			boolean flag = monthVehicleBrandService.addMonthVehicleBrand(monthVehicleBrandBean);
			if(flag){
				return AjaxReturnInfo.success("新增成功");
			}else{
				return AjaxReturnInfo.failed("新增失败");
			}
		} catch (Exception e) {
			log.error("月卡新增异常",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=monthVehicleBrandExport")
	@ResponseBody
	public void monthVehicleBrandExport(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "carOwnerName") String carOwnerName,
			@RequestParam(value = "inStatus") String inStatus,
			@RequestParam(value = "isExpire") String isExpire,
			HttpServletRequest req,HttpServletResponse response) {
		try {
			String outParkingId = "10180";
			MonthVehicleBrandBean monthVehicleBrandBean= new MonthVehicleBrandBean();
			if (StringUtils.isNotBlank(carNumber)) {
				monthVehicleBrandBean.setCarNumber(carNumber);
			}
			if (StringUtils.isNotBlank(carOwnerName)) {
				monthVehicleBrandBean.setCarOwnerName(carOwnerName);
			}
			if (StringUtils.isNotBlank(inStatus)) {
				monthVehicleBrandBean.setInStatus(inStatus);
			}
			if (StringUtils.isNotBlank(isExpire)) {
				monthVehicleBrandBean.setIsExpire(isExpire);
			}
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			List<MonthVehicleBrandExportBean> list = monthVehicleBrandService.getMonthVehicleBrandExport(monthVehicleBrandBean);
			ParkBean parkBean = parkService.getParkByOutParkingId(outParkingId);
			ExcelUtils.exportDataToExcel(response, list, new String[]{"车牌号","车牌性质","有效期起始日期","有效期终止日期","当期缴费金额","是否逾期","车辆在场状态","车主姓名","联系方式","备注"}, "月卡车信息", ".xls",parkBean);
		} catch (Exception e) {
			log.error("monthVehicleBrandExport导出异常",e);
		}
	}
	
	@RequestMapping(params = "method=monthVehicleBrandImport")
	@ResponseBody
	public AjaxReturnInfo monthVehicleBrandImport(HttpServletRequest req,HttpServletResponse response){
		String msg = "系统异常";
		FileInputStream fileInputStream = null;
		try {
			String outParkingId = "10180";
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
			MultipartFile file = multipartRequest.getFile("upload_file");  
	        if(file.isEmpty()){  
	        	msg = "文件不存在";
	        }else{
	        	File newFile=new File(FILE_UPLOAD+new Date().getTime()+file.getOriginalFilename());
	        	file.transferTo(newFile);
	        	fileInputStream = new FileInputStream(newFile);
	        	Map<String, Object> retMap = ExcelUtils.importDataFromExcel(new MonthVehicleBrandExportBean(), fileInputStream, file.getOriginalFilename(),"车牌号车牌性质有效期起始日期有效期终止日期当期缴费金额是否逾期车辆在场状态车主姓名联系方式备注",NCELL);
	        	if(retMap!=null&&!retMap.isEmpty()&&"00".equals(retMap.get("retCode"))){
	        		List list = (List) retMap.get("list");
	        		if(list!=null&&!list.isEmpty()&&list.size()>0){
	        			log.info("上传的记录："+list.size());
	        			Map retAddMap = this.monthVehicleBrandService.addBatchMonthVehicleBrand(list,outParkingId);
	        			if(retAddMap!=null&&!retAddMap.isEmpty()&&"00".equals(retAddMap.get("retCode"))){
			        		return AjaxReturnInfo.success("上传成功");
	        			}else{
	        				msg = (String) retAddMap.get("retMsg");
	        			}
	        		}else{
	        			msg = "上传的文件无有效数据";
	        		}
	        	}else{
	        		msg = (String) retMap.get("retMsg");
	        	}
	        }
		} catch (Exception e) {
			log.error("freeVehicleBrandImport---error",e);
		}finally{
			if(fileInputStream!=null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					log.error("close---io--error",e);
				}
			}
		}
		return AjaxReturnInfo.failed(msg);
	}
}
