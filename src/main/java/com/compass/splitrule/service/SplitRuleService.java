package com.compass.splitrule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.splitrule.model.SplitRuleBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author wangLong
 * 分润规则设置
 *
 */
public class SplitRuleService extends AbstractService {

	
	/**
	 * 获得分润规则的Service方法
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitRuleBean> getRules(String agencyId,String childagencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("childagencyId", childagencyId);
		return dao.queryForList("rule.getRules", map);
	}

	/**
	 * 添加分润规则方法
	 * @param splitRuleBean
	 * @return
	 */
	public int addRule(SplitRuleBean splitRuleBean) {
		// TODO Auto-generated method stub
		return dao.insert("rule.addRule", splitRuleBean);
	}
	public int updateRule(SplitRuleBean splitRuleBean){
		return dao.update("rule.updateRule", splitRuleBean);
	}

	/**
	 * 获取分润规则编号
	 * @return
	 */
	public Integer getRuleId(){
		return (Integer) dao.queryForObject("SEQUENCE.getRuleId");
	}
	

	/**
	 * 删除分润规则
	 * @param ruleIds
	 * @return
	 */
	public int deleteRules(String agencyId,String childAgencyId,String dealType,String validityData) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("childAgencyId", childAgencyId);
		map.put("dealType", dealType);
		map.put("validityData", validityData);
		return dao.delete("rule.deleteRules", map);
	}

	/**
	 * 根据条件获取相应的规则编号
	 * @param agencyId
	 * @param childAgencyId
	 * @param dealType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitRuleBean> checkRule(String agencyId, String childAgencyId, String dealType,String validityData) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("childAgencyId", childAgencyId);
		map.put("dealType", dealType);
		map.put("validityData", validityData);
		return dao.queryForList("rule.checkrule", map);
	}
	
	
	/**
	 * 获取交易规模区间
	 * @param ruleIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitRuleBean> getRulesByIds(String agencyId, String childAgencyId, String dealType) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("childAgencyId", childAgencyId);
		map.put("dealType", dealType);
		return dao.queryForList("rule.getRulesByIds", map);
	}

	/**
	 * 获取单笔区间
	 * @param ruleIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitRuleBean> getSingleDealSection(String ruleId) {
		// TODO Auto-generated method stub
		return dao.queryForList("rule.getSingleDealSection", ruleId);
	}

	/**
	 * 根据交易规模区间删除单笔区间
	 * @param ruleId
	 */
	public int deleteSingleRules(String ruleId) {
		// TODO Auto-generated method stub
		return dao.delete("rule.deleteSingleRules", ruleId);
	}

	/**
	 * 添加单笔区间
	 * @param bean
	 * @return
	 */
	public int addSingleRule(SplitRuleBean bean) {
		// TODO Auto-generated method stub
		return dao.insert("rule.addSingleRule", bean);
	}

	public Integer getRulesCount(String agencyId, String childAgencyId,
			String dealType) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("childAgencyId", childAgencyId);
		map.put("dealType", dealType);
		
		// TODO Auto-generated method stub
		return (Integer) dao.queryForObject("rule.getRulesCount", map);
	}

	public Integer deleteRule(String ruleId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ruleId", ruleId);
		return dao.delete("rule.delRule", map);
	}
}
