package com.compass.terminaltype.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.terminaltype.model.TerminalTypeBean;
import com.compass.utils.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author gaoyang
 * 终端类型管理
 */
public class TerminalTypeService extends AbstractService{
	
	private final Log log=LogFactory.getLog(getClass());

	/**
	 * 获得终端类型的Service方法
	 * @param terminalTypeName
	 * @param status
	 * @param systemId 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalTypeBean> getTerminalType(String terminalTypeName,String status, String systemId){
		log.info("获得终端类型List");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("terminalTypeName", terminalTypeName);
		map.put("status", status);
		map.put("systemId", systemId);
		return dao.queryForList("terminalType.getTerminalType",map);
	}
	
	/**
	 * 添加终端类型
	 * @param terminalTypeBean
	 * @return
	 */
	public int addTerminalType(TerminalTypeBean terminalTypeBean){
		log.info("添加终端类型Service方法");
		return dao.insert("terminalType.addTerminalType", terminalTypeBean);
	}
	
	/**
	 * 编辑终端类型
	 * @param terminalTypeBean
	 * @return
	 */
	public int updateTerminalType(TerminalTypeBean terminalTypeBean){
		log.info("修改终端类型Service方法");
		return dao.update("terminalType.updateTerminalType", terminalTypeBean);
	}
	
	/**
	 * 删除终端类型
	 * @param terminalTypeIds
	 * @return
	 */
	public int deleteTerminalType(String terminalTypeIds){
		return dao.update("terminalType.deleteTerminalType", terminalTypeIds);
	}
	
	/**
	 * 获取终端类型编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalTypeBean> getTerminalTypeName(String systemId){
		log.info("删除终端类型Service方法");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemId", systemId);
		return dao.queryForList("terminalType.getTerminalTypeName",map);
	}
	
	/**
	 * 获取终端类型序列号
	 * @return
	 */
	public Integer getTerminaltypeId(){
		log.info("获取终端类型序列号");
		return (Integer) dao.queryForObject("SEQUENCE.getTerminaltypeId");
	}
}
