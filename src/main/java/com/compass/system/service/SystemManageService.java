package com.compass.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.compass.agency.model.SpecSystemBean;
import com.compass.system.model.SystemManageBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author wangLong
 * 系统管理
 *
 */
public class SystemManageService extends AbstractService {

	private final Log log=LogFactory.getLog(getClass());
	
	/**
	 * 获得系统的Service方法
	 * @param systemName
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemManageBean> getSystems(String systemName,
			String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemName", systemName);
		map.put("status", status);
		return dao.queryForList("system.getSystem", map);
	}

	/**
	 * 添加系统
	 * @param systemBean
	 * @return
	 */
	public int addSystem(SystemManageBean systemBean) {
		// TODO Auto-generated method stub
		return dao.insert("system.addSystem", systemBean);
	}

	/**
	 * 编辑系统
	 * @param systemBean
	 * @return
	 */
	public int updateSystem(SystemManageBean systemBean) {
		// TODO Auto-generated method stub
		return dao.update("system.updateSystem", systemBean);
	}

	/**
	 * 删除系统
	 * @param systemBean
	 * @return
	 */
	public int deleteSystem(String systemIds) {
		// TODO Auto-generated method stub
		return dao.update("system.deleteSystem", systemIds);
	}
	
	/**
	 * 获得所有有效的系统（下拉框使用）
	 */
	@SuppressWarnings("unchecked")
	public List<SystemManageBean> getCombSystems() {
		// TODO Auto-generated method stub
		return dao.queryForList("system.getCombSystems");
	}

	/**
	 * 获取数据来源系统编号
	 * @return
	 */
	public Integer getSystemId(){
		return (Integer) dao.queryForObject("SEQUENCE.getSystemId");
	}

	/**
	 * 检查系统编号是否重复
	 * @param systemCode
	 * @return
	 */
	public int checkSysCode(String systemCode) {
		// TODO Auto-generated method stub
		return (Integer) dao.queryForObject("system.checkSysCode",systemCode);
	}
	
	/**
	 * 查看是否为特别系统（仅显示本机构及直属机构）
	 * 用于生成机构树和二级代理商及以下机构对于终端下发，交易明细查询，分润明细查询的特别处理
	 * @param systemId
	 * @return
	 */
	public SpecSystemBean checkSpecSystem(String systemId) {
		return (SpecSystemBean) dao.queryForObject("system.checkSpecSystem",systemId);
	}
	
	 

	@SuppressWarnings("unchecked")
	public List<SystemManageBean> getCombSystems(String systemId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("systemId", systemId);
		return dao.queryForList("system.getCombSystemsSin",map);
	}
	
	/**
	 * 查看是否为特别系统（仅显示本机构及直属机构）
	 * 用于生成机构树和二级代理商及以下机构对于终端下发，交易明细查询，分润明细查询的特别处理
	 * @param systemId
	 * @return
	 */
	public SpecSystemBean checkOnlineSystem(String systemId) {
		return (SpecSystemBean) dao.queryForObject("system.checkOnlineSystem",systemId);
	}
	
	/**
	 * 查看来源系统下的机构是否为特别系统
	 * @param systemId
	 * @param agencyId
	 * @return
	 */
	public Integer checkSpecAgency(String systemId,String agencyId) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("systemId", systemId);
		map.put("agencyId", agencyId);
		return (Integer) dao.queryForObject("system.checkSpecAgency",map);
	}
	
	/**
	 * 检查系统来源编号是否为分润明细下级机构归并显示
	 * @param systemId
	 * @return
	 * @throws Exception
	 */
	public SpecSystemBean  checkSplit(String systemId) throws Exception{
		log.info("检查系统来源编号是否为分润明细下级机构归并显示");
		SpecSystemBean spec=null;
		try{
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("systemId", systemId);
			spec= (SpecSystemBean) dao.queryForObject("system.checkSplitControl",map);
		}catch(Exception e){
			log.error("检查系统来源编号是否为分润明细下级机构归并显示异常"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return spec;
	}
}
