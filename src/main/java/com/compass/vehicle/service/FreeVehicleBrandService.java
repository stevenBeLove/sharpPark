package com.compass.vehicle.service;

import java.util.List;

import com.compass.utils.AbstractService;
import com.compass.vehicle.model.FreeVehicleBrandBean;
import com.compass.vehicle.model.FreeVehicleBrandExportBean;

public class FreeVehicleBrandService extends AbstractService{

	public Integer getFreeVehicleBrandCount(FreeVehicleBrandBean freeVehicleBrandBean) {
        return (Integer) dao.queryForObject("TB_FREE_VEHICLE_BRAND.getFreeVehicleBrandCount", freeVehicleBrandBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<FreeVehicleBrandBean> getFreeVehicleBrandAll(FreeVehicleBrandBean freeVehicleBrandBean) {
	        return dao.queryForList("TB_FREE_VEHICLE_BRAND.getFreeVehicleBrandAll", freeVehicleBrandBean);
	}
	
	public boolean addFreeVehicleBrand(FreeVehicleBrandBean freeVehicleBrandBean){
		return dao.insert("TB_FREE_VEHICLE_BRAND.addFreeVehicleBrand", freeVehicleBrandBean)>0?true:false;
	}
	
	public boolean updateFreeVehicleBrandById(FreeVehicleBrandBean freeVehicleBrandBean){
		return dao.update("TB_FREE_VEHICLE_BRAND.updateFreeVehicleBrandById", freeVehicleBrandBean)>0?true:false;
	}
	
	@SuppressWarnings("unchecked")
	public List<FreeVehicleBrandExportBean> getFreeVehicleBrandExport(FreeVehicleBrandBean freeVehicleBrandBean) {
	    return dao.queryForList("TB_FREE_VEHICLE_BRAND.getFreeVehicleBrandExport", freeVehicleBrandBean);
	}
	
	public boolean isFreeVehicleFlag(FreeVehicleBrandBean freeVehicleBrandBean){
		return (Integer)dao.queryForObject("TB_FREE_VEHICLE_BRAND.isFreeVehicleFlag",freeVehicleBrandBean)>0?true:false;
	}
}
