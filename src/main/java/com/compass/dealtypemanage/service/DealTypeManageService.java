package com.compass.dealtypemanage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.dealtypemanage.model.DealTypeManageBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author wangLong
 * 交易类型管理
 *
 */
public class DealTypeManageService extends AbstractService {

	
	/**
	 * 获得交易类型的Service方法
	 * @param dealTypeName
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealTypeManageBean> getDealTypes(String dealTypeName,String status,String systemId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dealTypeName", dealTypeName);
		map.put("status", status);
		map.put("systemId", systemId);
		return dao.queryForList("dealtypeM.getDealType", map);
	}

	/**
	 * 添加交易类型
	 * @param dealTypeBean
	 * @return
	 */
	public int addDealType(DealTypeManageBean dealTypeBean) {
		// TODO Auto-generated method stub
		return dao.insert("dealtypeM.addDealType", dealTypeBean);
	}

	/**
	 * 编辑交易类型
	 * @param dealTypeBean
	 * @return
	 */
	public int updateDealType(DealTypeManageBean dealTypeBean) {
		// TODO Auto-generated method stub
		return dao.update("dealtypeM.updateDealType", dealTypeBean);
	}

	/**
	 * 删除交易类型
	 * @param dealTypeBean
	 * @return
	 */
	public int deleteDealType(String dealTypeIds) {
		// TODO Auto-generated method stub
		return dao.update("dealtypeM.deleteDealType", dealTypeIds);
	}
	
	/**
	 * 获得所有有效的交易类型（在某一个agencyid下面，下拉框使用）
	 * @param systemId 
	 */
	@SuppressWarnings("unchecked")
	public List<DealTypeManageBean> getCombDealTypesByagencyId(String agencyId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		return dao.queryForList("dealtypeM.getCombDealTypesByagencyId",map);
		
	}
	
	/**
	 * 获得所有有效的交易类型（下拉框使用）
	 * @param systemId 
	 */
	@SuppressWarnings("unchecked")
	public List<DealTypeManageBean> getCombDealTypesAll() {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", null);
		return dao.queryForList("dealtypeM.getCombDealTypesAll",map);
	}
	
	@SuppressWarnings("unchecked")
	public List<DealTypeManageBean> getCombDealTypes(String systemId) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("systemId", systemId);
		return dao.queryForList("dealtypeM.getCombDealTypes",map);
	}

	/**
	 * 获取交易类型序列号
	 */
	public Integer getDealtypeId(){
		return (Integer) dao.queryForObject("SEQUENCE.getDealtypeId");
	}
	
	public List<DealTypeManageBean> getCombDealTypeByAgencyId(String agencyId) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		return dao.queryForList("dealtypeM.getCombDealTypeByAgencyId",map);
	}
}
