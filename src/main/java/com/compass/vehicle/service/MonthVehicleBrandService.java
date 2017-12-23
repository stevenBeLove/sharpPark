package com.compass.vehicle.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.compass.utils.AbstractService;
import com.compass.vehicle.model.FreeVehicleBrandBean;
import com.compass.vehicle.model.FreeVehicleBrandExportBean;
import com.compass.vehicle.model.MonthVehicleBrandBean;
import com.compass.vehicle.model.MonthVehicleBrandExportBean;

public class MonthVehicleBrandService extends AbstractService{
	public Integer getMonthVehicleBrandCount(MonthVehicleBrandBean monthVehicleBrandBean) {
        return (Integer) dao.queryForObject("TB_MONTH_VEHICLE_BRAND.getMonthVehicleBrandCount", monthVehicleBrandBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<MonthVehicleBrandBean> getMonthVehicleBrandAll(MonthVehicleBrandBean monthVehicleBrandBean) {
	        return dao.queryForList("TB_MONTH_VEHICLE_BRAND.getMonthVehicleBrandAll", monthVehicleBrandBean);
	}
	
	public boolean addMonthVehicleBrand(MonthVehicleBrandBean monthVehicleBrandBean){
		return dao.insert("TB_MONTH_VEHICLE_BRAND.addMonthVehicleBrand", monthVehicleBrandBean)>0?true:false;
	}
	
	public boolean updateMonthVehicleBrandById(MonthVehicleBrandBean monthVehicleBrandBean){
		return dao.update("TB_MONTH_VEHICLE_BRAND.updateMonthVehicleBrandById", monthVehicleBrandBean)>0?true:false;
	}
	
	public boolean returnMonthVehicleBrandById(MonthVehicleBrandBean monthVehicleBrandBean){
		return dao.update("TB_MONTH_VEHICLE_BRAND.returnMonthVehicleBrandById", monthVehicleBrandBean)>0?true:false;
	}
	
	public boolean isMonthVehicleFlag(MonthVehicleBrandBean monthVehicleBrandBean){
		return (Integer)dao.queryForObject("TB_MONTH_VEHICLE_BRAND.isMonthVehicleFlag",monthVehicleBrandBean)>0?true:false;
	}
	
	public boolean isMonthCardRefund(MonthVehicleBrandBean monthVehicleBrandBean){
		return dao.update("TB_MONTH_VEHICLE_BRAND.isMonthCardRefund", monthVehicleBrandBean)>0?true:false;
	}
	
	@SuppressWarnings("unchecked")
	public List<MonthVehicleBrandExportBean> getMonthVehicleBrandExport(MonthVehicleBrandBean monthVehicleBrandBean) {
	    return dao.queryForList("TB_MONTH_VEHICLE_BRAND.getMonthVehicleBrandExport", monthVehicleBrandBean);
	}
	
	public Map<String,Object> addBatchMonthVehicleBrand(List list,String outParkingId) throws Exception{
		List<MonthVehicleBrandBean> addList = new ArrayList<MonthVehicleBrandBean>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> retMap = new HashMap<String,Object>();
		for (Object object : list) {
			MonthVehicleBrandBean monthVehicleBrandBean = new MonthVehicleBrandBean();
			MonthVehicleBrandExportBean monthVehicleBrandExportBean = (MonthVehicleBrandExportBean) object;
			String startDate = monthVehicleBrandExportBean.getStartDate();
			String endDate = monthVehicleBrandExportBean.getEndDate();
			String monthPayAmount = monthVehicleBrandExportBean.getMonthPayAmount();
			monthVehicleBrandExportBean.setStartDate(null);
			monthVehicleBrandExportBean.setEndDate(null);
			monthVehicleBrandExportBean.setMonthPayAmount(null);
			PropertyUtils.copyProperties(monthVehicleBrandBean, monthVehicleBrandExportBean);
			monthVehicleBrandBean.setOutParkingId(outParkingId);
			monthVehicleBrandBean.setVehicleBrandType(formatVehicleBrandType(monthVehicleBrandExportBean.getVehicleBrandType()));
			try {
				if(StringUtils.isNotBlank(startDate)){
					monthVehicleBrandBean.setStartDate(dateFormat.parse(startDate));
				}
				if(StringUtils.isNotBlank(endDate)){
					monthVehicleBrandBean.setEndDate(dateFormat.parse(endDate));
				}
				if(StringUtils.isNotBlank(monthPayAmount)){
					monthVehicleBrandBean.setMonthPayAmount(new BigDecimal(monthPayAmount));
				}
			} catch (Exception e) {
				logger.error("addBatchMonthVehicleBrand--format--error",e);
				retMap.put("retCode", "99");
				retMap.put("retMsg", "数据格式转换错误");
				return retMap;
			}
			addList.add(monthVehicleBrandBean);
		}
		addBatchMonthVehicleBrand(addList);
		retMap.put("retCode", "00");
		retMap.put("retMsg", "success");
		return retMap;
	}
	
	public void addBatchMonthVehicleBrand(List<MonthVehicleBrandBean> list){
		dao.insert("TB_MONTH_VEHICLE_BRAND.addBatchMonthVehicleBrand", list);
	}
	
	public String formatVehicleBrandType(String vehicleBrandType){
		if("公安".equals(vehicleBrandType))return "1";
		if("武警".equals(vehicleBrandType))return "2";
		if("军队".equals(vehicleBrandType))return "3";
		return "4";
	}
}
