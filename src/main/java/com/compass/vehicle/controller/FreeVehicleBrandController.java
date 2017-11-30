package com.compass.vehicle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
			@RequestParam(value = "status") String status,
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
		if (StringUtils.isNotBlank(inStatus)) {
			freeVehicleBrandBean.setInStatus(inStatus);
		}
		if (StringUtils.isNotBlank(status)) {
			freeVehicleBrandBean.setStatus(status);
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
			String outParkingId = "10180";//后续从session中获取
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
			String outParkingId = "10180";
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
			@RequestParam(value = "inStatus") String inStatus,
			@RequestParam(value = "status") String status,
			HttpServletRequest req,HttpServletResponse response) {
		try {
			String outParkingId = "10180";
			FreeVehicleBrandBean freeVehicleBrandBean= new FreeVehicleBrandBean();
			if (StringUtils.isNotBlank(carNumber)) {
				freeVehicleBrandBean.setCarNumber(carNumber);
			}
			if (StringUtils.isNotBlank(carOwnerName)) {
				freeVehicleBrandBean.setCarOwnerName(carOwnerName);
			}
			if (StringUtils.isNotBlank(inStatus)) {
				freeVehicleBrandBean.setInStatus(inStatus);
			}
			if (StringUtils.isNotBlank(status)) {
				freeVehicleBrandBean.setStatus(status);
			}
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			List<FreeVehicleBrandExportBean> list = freeVehicleBrandService.getFreeVehicleBrandExport(freeVehicleBrandBean);
			ParkBean parkBean = parkService.getParkByOutParkingId(outParkingId);
			ExcelUtils.exportDataToExcel(response, list, new String[]{"车牌号","车牌性质","车辆在场状态","车主姓名","联系方式","备注"}, "免费车信息", ".xls",parkBean);
		} catch (Exception e) {
			log.error("freeVehicleBrandExport导出异常",e);
		}
	}
	
	@RequestMapping(params = "method=freeVehicleBrandImport")
	@ResponseBody
	public AjaxReturnInfo freeVehicleBrandImport(HttpServletRequest req,HttpServletResponse response){
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
	        	Map<String, Object> retMap = ExcelUtils.importDataFromExcel(new FreeVehicleBrandExportBean(), fileInputStream, file.getOriginalFilename(),"车牌号车牌性质车辆在场状态车主姓名联系方式备注",NCELL);
	        	if(retMap!=null&&!retMap.isEmpty()&&"00".equals(retMap.get("retCode"))){
	        		List list = (List) retMap.get("list");
	        		if(list!=null&&!list.isEmpty()&&list.size()>0){
	        			log.info("上传的记录："+list.size());
	        			this.freeVehicleBrandService.updateOrInsertFreeVehicle(list,outParkingId);
		        		msg = "上传成功";
		        		return AjaxReturnInfo.success(msg);
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
