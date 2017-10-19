package com.compass.role_auth.service;

/**
 * @author wangyuchao
 */

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.authority.model.AuthorityBean;
import com.compass.role_auth.model.Role_AuthBean;
import com.compass.utils.AbstractService;

public class Role_AuthService extends AbstractService{
	/**
	 * 保存
	 * @param role_AuthBean
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public int saveAuths(Role_AuthBean role_AuthBean){
		return dao.insert("ROLE_AUTH.saveAuths", role_AuthBean);
	}
	
	/**
	 * 删除角色权限
	 * @param roleId
	 * @return
	 */
	public int delAuths(String roleId){
		return dao.delete("ROLE_AUTH.delAuths", roleId);
	}
	
	/**
	 * 获取角色权限
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Role_AuthBean> getRoleAuths(String roleId){
		return dao.queryForList("ROLE_AUTH.getRoleAuthorityTree",roleId);
	}
	
	/**
	 * 获取权限生成的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityBean> getAuTree(){
		return dao.queryForList("authority.getAuthorityTree");
	}
	
}
