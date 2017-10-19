package com.compass.role.controller;
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

import com.compass.role.model.RoleBean;
import com.compass.role.service.RoleService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangyuchao
 * 
 */
@Controller
@RequestMapping("/role/role.do")
public class RoleController {
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;
	//系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService; 

	/**
	 * 获取角色生成下拉框
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getRoles")
	@ResponseBody
	public List<Map<String, Object>> getRoles(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String roletypeId=(request.getSession().getAttribute(ConstantUtils.ROLETYPEID)==null)?"":request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString();
		List<RoleBean> rolelist = roleService.getRoles(roletypeId);
		String flag = request.getParameter("flag");
		Map<String, Object> map = null;
		//如果参数为1 添加   ”请选择记录“
		if ("1".equals(flag)) {
			 map = new HashMap<String, Object>();
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择角色");
			map.put("selected", true);
			list.add(map);
			map=null;
		}
		for (int i = 0; i < rolelist.size(); i++) {
			 map = new HashMap<String, Object>();
			map.put("id", rolelist.get(i).getRoleId());
			map.put("text", rolelist.get(i).getRoleName());
			list.add(map);
			map=null;
		}
		return list;
	}
	/**
	 * 获取角色树
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=gettreeRole")
	@ResponseBody
	public List<Map<String,Object>> gettreeRole(HttpServletRequest request){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
		String roletypeId=(request.getSession().getAttribute(ConstantUtils.ROLETYPEID)==null)?"":request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString();
		List<RoleBean> rolelist = roleService.getRoles(roletypeId);
		for(int i=0;i<rolelist.size();i++){
			Map<String, Object> map1 = new HashMap<String, Object>();
			if(i==0){
				map1.put("id", rolelist.get(i).getRoleId());
				map1.put("text", rolelist.get(i).getRoleName());
				map1.put("checked", true);
			}else{
				map1.put("id", rolelist.get(i).getRoleId());
				map1.put("text", rolelist.get(i).getRoleName());
			}
		
			list.add(map1);
			map1=null;
		}		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id",1);
		map.put("text", "根节点");
		map.put("children", list);
		clist.add(map);
		map=null;
		return clist;
	}

	/**
	 * 获取角色信息
	 * @param roleName
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getRole")
	@ResponseBody
	public Map<String, Object> getRole(
			@RequestParam(value = "roleName") String roleName,
			@RequestParam(value = "status") String status,
			HttpServletRequest request) {
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		
		List<RoleBean> list = roleService.getRole(roleName, status);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<RoleBean> menulist=new ArrayList<RoleBean>();
		if (list != null && list.size() > 0) {
			for(int i=start;i<end;i++){
				menulist.add(list.get(i));
			}
		}
		menulist=null;
		//添加操作详情 20141203
		String operateDetail = "查询条件为" + roleName + status;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEROLECONTROL,ConstantUtils.OPERTYPESER,
		operateDetail);
		return AjaxReturnInfo.setTable(list==null?0:list.size(), list);
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(params = "method=getRolecom")
	@ResponseBody
	public Map<String, Object> getRolecom() {
	
		
		List<RoleBean> list = roleService.getRole("", "1");
		return AjaxReturnInfo.setTable(list==null?0:list.size(), list);
	}

	/**
	 * 添加角色信息
	 * @param roleName
	 * @param Status
	 * @param roleDesc
	 * @param roletypeId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addRole")
	@ResponseBody
	public AjaxReturnInfo addRole(
			@RequestParam(value = "roleName") String roleName,
			@RequestParam(value = "Status") String Status,
			@RequestParam(value = "roleDesc") String roleDesc,
			@RequestParam(value = "roletypeId") String roletypeId,
			HttpServletRequest request) {
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String createId=request.getSession().getAttribute(ConstantUtils.USERID)+"";
		RoleBean rb = new RoleBean();
		rb.setRoleName(roleName);
		rb.setStatus(Status);
		rb.setRoletypeId(roletypeId);
		rb.setRoleDesc(roleDesc);
		rb.setCreateId(createId);
		//String roleId=CommonTable.getTableId("r");
		String roleId=roleService.getseqRoleId().toString();
		String createDt=CommonDate.getDate();
		rb.setRoleId(roleId);
		rb.setCreateDt(createDt);
		int result = roleService.add(rb);
		rb=null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("保存失败");
		} else {
			//添加操作详情 20141203
			String operateDetail = "添加角色名为" + roleName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEROLECONTROL,ConstantUtils.OPERTYPEADD,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}

	/**
	 * 修改角色信息
	 * @param roleName
	 * @param Status
	 * @param roleDesc
	 * @param roleId
	 * @param roletypeId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateRole")
	@ResponseBody
	public AjaxReturnInfo updateRole(
			@RequestParam(value = "roleName") String roleName,
			@RequestParam(value = "Status") String Status,
			@RequestParam(value = "roleDesc") String roleDesc,
			@RequestParam(value = "roleId") String roleId,
			@RequestParam(value = "roletypeId") String roletypeId,
			HttpServletRequest request) {
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String createId=request.getSession().getAttribute(ConstantUtils.USERID)+"";
		String createDt=CommonDate.getDate();
		RoleBean rb = new RoleBean();
		rb.setRoleId(roleId);
		rb.setRoleName(roleName);
		rb.setStatus(Status);
		rb.setRoletypeId(roletypeId);
		rb.setRoleDesc(roleDesc);
		rb.setModifyid(createId);
		rb.setModifydt(createDt);
		int result = roleService.update(rb);
		rb=null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("修改失败");
		} else {
			//添加操作详情 20141203
			String operateDetail = "修改的角色名为" + roleName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEROLECONTROL,ConstantUtils.OPERTYPEUPD,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("修改成功");
		}
		return ajaxinfo;
	}

	/**
	 * 删除角色信息
	 * @param Ids
	 * @return
	 */
	@RequestMapping(params = "method=deleteRole")
	@ResponseBody
	public AjaxReturnInfo deleteRole(@RequestParam(value = "Ids") String Ids
			,HttpServletRequest request) {
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		
		AjaxReturnInfo ajaxinfo = null;
		int result = roleService.delete(Ids);
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("删除失败");
		} else {
			//添加操作详情 20141203
			String operateDetail = "删除的角色ID为" + Ids;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEROLECONTROL,ConstantUtils.OPERTYPEDEL,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("删除成功");
		}
		
		return ajaxinfo;
	}

	
}
