package com.compass.vehicle.service;

import java.util.List;

import com.compass.utils.AbstractService;
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
	
	public boolean isMonthVehicleFlag(MonthVehicleBrandBean monthVehicleBrandBean){
		return (Integer)dao.queryForObject("TB_MONTH_VEHICLE_BRAND.isMonthVehicleFlag",monthVehicleBrandBean)>0?true:false;
	}
	
	@SuppressWarnings("unchecked")
	public List<MonthVehicleBrandExportBean> getMonthVehicleBrandExport(MonthVehicleBrandBean monthVehicleBrandBean) {
	    return dao.queryForList("TB_MONTH_VEHICLE_BRAND.getMonthVehicleBrandExport", monthVehicleBrandBean);
	}
}
