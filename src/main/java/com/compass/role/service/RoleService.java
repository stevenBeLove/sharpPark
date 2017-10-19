/**
 * 
 */
package com.compass.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.role.model.RoleBean;
import com.compass.utils.AbstractService;

/**
 * @author wangYuChao
 * 
 */
public class RoleService extends AbstractService {
	/**
	 * 获取角色序列号
	 * @return
	 */
	public Integer getseqRoleId(){
		return (Integer) dao.queryForObject("SEQUENCE.getRoleId");
	}
	/**
	 * 获取角色
	 * @param roleName
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoleBean> getRole(String roleName, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleName", roleName);
		map.put("status", status);
		return dao.queryForList("role.getRole", map);
	}
	/**
	 * 添加角色
	 * @param roleBean
	 * @return
	 */
	public int add(RoleBean roleBean) {
		return dao.insert("role.addRole", roleBean);
	}
	/**
	 * 修改角色
	 * @param roleBean
	 * @return
	 */
	public int update(RoleBean roleBean) {
		return dao.update("role.updateRole", roleBean);
	}
	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
	public int delete(String roleId) {
		return dao.update("role.deleteRole", roleId);
	}
	/**
	 * 获取角色的总条数
	 * @param roleName
	 * @param status
	 * @return
	 */
	public int getCount(String roleName, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleName", roleName);
		map.put("status", status);
		return (Integer) dao.queryForObject("role.getCount", map);
	}
		
	/**
	 * 获取角色
	 * @param roletypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation= Propagation.REQUIRED)
	public List<RoleBean> getRoles(String roletypeId){
		return dao.queryForList("role.getRoleList",roletypeId);
	}

	/**
	 * 获取管理员权限的角色编号
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getRoleId(String roletypeId){
		return dao.queryForList("role.getRoleId",roletypeId);
	}
	
	/**
	 * 获取角色类型编号
	 * @param loginname
	 * @param agencyId
	 * @return
	 */
	public String getRoleTypeId(String loginname,String agencyId){
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginname", loginname);
		map.put("agencyId", agencyId);
		return (String) dao.queryForObject("role.getRoleTypeId",map);
	}
}
