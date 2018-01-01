package com.compass.park.service;

import java.util.List;

import com.compass.park.model.ParkRuleSetBean;
import com.compass.utils.AbstractService;

public class ParkRuleSetService extends AbstractService{
	
	public Integer getParkRuleSetCount(ParkRuleSetBean parkRuleSetBean) {
        return (Integer) dao.queryForObject("TB_PARK_RULE_SET.getParkRuleSetCount", parkRuleSetBean);
    }
	
	@SuppressWarnings("unchecked")
	public List<ParkRuleSetBean> getParkRuleSetAll(ParkRuleSetBean parkRuleSetBean) {
	        return dao.queryForList("TB_PARK_RULE_SET.getParkRuleSetAll", parkRuleSetBean);
	}
	
	public boolean addParkRuleSet(ParkRuleSetBean parkRuleSetBean){
		return dao.insert("TB_PARK_RULE_SET.addParkRuleSet", parkRuleSetBean)>0?true:false;
	}
	
	public boolean updateParkRuleSetById(ParkRuleSetBean parkRuleSetBean){
		return dao.update("TB_PARK_RULE_SET.updateParkRuleSetById", parkRuleSetBean)>0?true:false;
	}
	
	public List<ParkRuleSetBean> queryParkRuleSetByParkId(ParkRuleSetBean parkRuleSetBean){
		return (List<ParkRuleSetBean>)dao.queryForList("TB_PARK_RULE_SET.queryParkRuleSetByParkId", parkRuleSetBean);
	}
	
	public List<ParkRuleSetBean> queryParkRuleSetByType(ParkRuleSetBean parkRuleSetBean){
		return (List<ParkRuleSetBean>)dao.queryForList("TB_PARK_RULE_SET.queryParkRuleSetByType", parkRuleSetBean);
	}
}
