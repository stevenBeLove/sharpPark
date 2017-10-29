package com.compass.park.service;

import java.util.List;

import com.compass.park.model.ParkBean;
import com.compass.utils.AbstractService;

public class ParkService extends AbstractService{
	
	public Integer getParkCount(ParkBean parkBean) {
        return (Integer) dao.queryForObject("TB_PARK.getParkCount", parkBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<ParkBean> getParkAll(ParkBean parkBean) {
	        return dao.queryForList("TB_PARK.getParkAll", parkBean);
	}
}
