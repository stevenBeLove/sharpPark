package com.compass.role_auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.authority.model.AuthorityBean;
import com.compass.role_auth.model.Role_AuthBean;
import com.compass.role_auth.service.Role_AuthService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
/**
 * 
 * @author wangyuchao
 *
 */

@Controller
@RequestMapping("/role_auth/role_auth.do")
public class Role_AuthController {

	@Autowired
	@Qualifier("role_AuthService")
	private Role_AuthService role_AuthService;
	//系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService; 
		
	/**
	 *  查看角色所对应的菜单，并生成树
	 * @param roleId
	 * @return
	 */
	@RequestMapping(params = "method=getRoleAuthsTree")
	@ResponseBody
	public List<Map<String, Object>> getRoleAuthTree(
			@RequestParam(value = "roleId") String roleId) {
		List<Role_AuthBean> list = role_AuthService.getRoleAuths(roleId);//角色的所有菜单
		List<AuthorityBean> auslist = role_AuthService.getAuTree();//所有菜单
		return createAuthTree(list, auslist, "-1");
	}
	/**
	 * 查看角色所对应的菜单，并生成树（提取方法便于递归查找）
	 * @param list
	 * @param auslist
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> createAuthTree(List<Role_AuthBean> list,
			List<AuthorityBean> auslist, String id) {
		List<Map<String, Object>> aulist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < auslist.size(); i++) {
			Map<String, Object> map = null;
			AuthorityBean at = auslist.get(i);
			if (id.equals(at.getParentNodeId())) {
				map = new HashMap<String, Object>();
				map.put("id", at.getMenuId());
				map.put("text", at.getMenuName());
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getAuthId()
							.equals(auslist.get(i).getMenuId())) {
						map.put("checked", true);
					}
				}
				if ("0".equals(at.getChildCode())) {
					map.put("state", "open");
				} else {
					map.put("state", "open");
					map.put("children",
							createAuthTree(list, auslist, at.getMenuId()));

				}

			}
			if (map != null)
				aulist.add(map);
		}
		return aulist;
	}
	/**
	 * 删除角色对应的菜单
	 * @param roleId
	 * @return
	 */
	public AjaxReturnInfo delPowers(
			@RequestParam(value = "roleId") String roleId) {
		int result = 0;
		result = role_AuthService.delAuths(roleId);
		AjaxReturnInfo ajaxInfo = AjaxReturnInfo.success("操作成功");
		if (result == 0) {
			ajaxInfo = AjaxReturnInfo.failed("操作失败");
		}
		return ajaxInfo;
	}
	/**
	 * 保存角色对应的菜单
	 * @param powerIds
	 * @param roleId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=saveAuths")
	@ResponseBody
	public AjaxReturnInfo savePowers(
			@RequestParam(value = "powerIds") String powerIds,
			@RequestParam(value = "roleId") String roleId,
			HttpServletRequest request) {
		int result = 0;
		delPowers(roleId);
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功");
		if(!"".equals(powerIds)){
			String ids[] = powerIds.split(",");
			for (String powerId : ids) {
				Role_AuthBean ra = new Role_AuthBean();
				String createId=request.getSession().getAttribute(ConstantUtils.USERID).toString();
				String createDt=CommonDate.getDate();
				ra.setRoleId(roleId);
				ra.setAuthId(powerId);
				ra.setCreateId(createId);
				ra.setCreateDt(createDt);
				
				result = role_AuthService.saveAuths(ra);
				ra=null;
			}
		
			if (result == 0) {
				ajaxReturnInfo = AjaxReturnInfo.failed("保存失败");
			}
		}
		//添加操作详情 20141203
		String operateDetail = "添加角色为" + roleId + ",添加菜单为" + powerIds;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEROLEAUTH,ConstantUtils.OPERTYPEADD,
		operateDetail);
		
		return ajaxReturnInfo;
	}

}
