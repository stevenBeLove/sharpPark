package com.compass.vehicle.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

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
	
	public void updateOrInsertFreeVehicle(List list,String outParkingId) throws Exception{
		List<FreeVehicleBrandBean> addList = new ArrayList<FreeVehicleBrandBean>();
		List<FreeVehicleBrandBean> updateList = new ArrayList<FreeVehicleBrandBean>();
		for (Object object : list) {
			FreeVehicleBrandBean freeVehicleBrandBean = new FreeVehicleBrandBean();
			FreeVehicleBrandExportBean freeVehicleBrandExportBean = (FreeVehicleBrandExportBean) object;
			PropertyUtils.copyProperties(freeVehicleBrandBean, freeVehicleBrandExportBean);
			freeVehicleBrandBean.setOutParkingId(outParkingId);
			freeVehicleBrandBean.setVehicleBrandType(formatVehicleBrandType(freeVehicleBrandBean.getVehicleBrandType()));
			if(getFreeVehicleByOutPardingIdAndCarNumber(freeVehicleBrandBean)>0){
				updateList.add(freeVehicleBrandBean);
			}else{
				addList.add(freeVehicleBrandBean);
			}
		}
		if(updateList.size()>0){
			updateBatchFreeVehicle(updateList);
		}
		if(addList.size()>0){
			addBatchFreeVehicle(addList);
		}
	}
	
	public int getFreeVehicleByOutPardingIdAndCarNumber(FreeVehicleBrandBean FreeVehicleBrandBean){
		return (Integer) dao.queryForObject("TB_FREE_VEHICLE_BRAND.getFreeVehicleByOutPardingIdAndCarNumber",FreeVehicleBrandBean);
	}
	
	public void updateBatchFreeVehicle(List<FreeVehicleBrandBean> list){
		dao.update("TB_FREE_VEHICLE_BRAND.updateBatchFreeVehicle", list);
	}
	
	public void addBatchFreeVehicle(List<FreeVehicleBrandBean> list){
		dao.insert("TB_FREE_VEHICLE_BRAND.addBatchFreeVehicle", list);
	}
	
	public String formatVehicleBrandType(String vehicleBrandType){
		if("公安".equals(vehicleBrandType))return "1";
		if("武警".equals(vehicleBrandType))return "2";
		if("军队".equals(vehicleBrandType))return "3";
		return "4";
	}
}
