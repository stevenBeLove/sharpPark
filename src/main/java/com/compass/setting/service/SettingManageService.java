package com.compass.setting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.setting.model.SettingManageBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author wangLong
 * 读取文件设置管理
 *
 */
public class SettingManageService extends AbstractService {

	
	/**
	 * 获得读取文件设置的Service方法
	 * @param settingName
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SettingManageBean> getSettings(String systemId,String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemCode",systemId);
		map.put("status", status);
		return dao.queryForList("setting.getSetting", map);
	}

	/**
	 * 获得文件路径（下拉框使用）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SettingManageBean> getFilePath() {
		// TODO Auto-generated method stub
		return dao.queryForList("setting.getFilePath");
	}
	/**
	 * 添加读取文件设置
	 * @param settingBean
	 * @return
	 */
	public int addSetting(SettingManageBean settingBean) {
		// TODO Auto-generated method stub
		return dao.insert("setting.addSetting", settingBean);
	}

	/**
	 * 编辑读取文件设置
	 * @param settingBean
	 * @return
	 */
	public int updateSetting(SettingManageBean settingBean) {
		// TODO Auto-generated method stub
		return dao.update("setting.updateSetting", settingBean);
	}

	/**
	 * 删除读取文件设置
	 * @param settingBean
	 * @return
	 */
	public int deleteSetting(String settingIds) {
		// TODO Auto-generated method stub
		return dao.update("setting.deleteSetting", settingIds);
	}

	/**
	 * 获取导入数据设置编号
	 * @return
	 */
	public Integer getSettingId(){
		return (Integer) dao.queryForObject("SEQUENCE.getSettingId");
	}
}
