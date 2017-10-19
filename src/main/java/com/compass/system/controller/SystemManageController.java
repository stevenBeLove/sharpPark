/**
 * 
 */
package com.compass.system.controller;

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

import com.compass.system.model.SystemManageBean;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangLong
 * 文件提供系统
 */


@Controller
@RequestMapping("/system/system.do")
public class SystemManageController {

	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;

	//系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService; 
	
	/**
	 * 获得文件提供系统列表
	 */
	@RequestMapping(params = "method=getSystems")
	@ResponseBody
	public Map<String, Object> getSystems(
			@RequestParam(value = "systemName") String systemName,
			@RequestParam(value = "status") String status,
			HttpServletRequest request) {
		//获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		List<SystemManageBean> list = systemManageService.getSystems(systemName, status);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<SystemManageBean> systemList=new ArrayList<SystemManageBean>();
		if (list != null && list.size() > 0) {
			for(int i=start;i<end;i++){
				systemList.add(list.get(i));
			}
		}
		//添加操作详情 20141203
		String operateDetail = "查询条件为" + systemName + status;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMESYSTEM,ConstantUtils.OPERTYPESER,
		operateDetail);
		return AjaxReturnInfo.setTable(list==null?0:list.size(), systemList);
	}
	
	
	/**
	 * 获得所有有效的文件提供系统（下拉框使用）
	 */
	@RequestMapping(params = "method=getCombSystems")
	@ResponseBody
	public List<Map<String,Object>>  getCombSystems() {
	
	  List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择系统");
		map.put("selected", true);
		list.add(map);
		map=null;
		List<SystemManageBean> dtypeList = systemManageService.getCombSystems();
		for(int i=0;i<dtypeList.size();i++){
			SystemManageBean bean = dtypeList.get(i);
			Map<String,Object> map1=new HashMap<String,Object>();
			map1.put("id", bean.getSystemId());
			map1.put("text", bean.getSystemName());
			list.add(map1);
			map1=null;
		}
		return list;
	}
	@RequestMapping(params = "method=getCombSystemsSigle")
	@ResponseBody
	public List<Map<String,Object>>  getCombSystemsSigle(HttpServletRequest request) {
		String systemId=request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String depagencyId=request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		if(ConstantUtils.CENTERCODE.equals(depagencyId)){
			systemId="";
		}
		List<SystemManageBean> dtypeList = systemManageService.getCombSystems(systemId);
		Map<String,Object> map=new HashMap<String,Object>();
		if(ConstantUtils.CENTERCODE.equals(depagencyId)){
			map.put("id","-1");
			map.put("text", "请选择");
		 	map.put("selected", true);
			list.add(map);
			map=null;
		}
		
		for(int i=0;i<dtypeList.size();i++){
			SystemManageBean bean = dtypeList.get(i);
			map=new HashMap<String,Object>();
			map.put("id", bean.getSystemId());
			map.put("text", bean.getSystemName());
			if(!ConstantUtils.CENTERCODE.equals(depagencyId)&&i==0){
			 	map.put("selected", true);
			}
			list.add(map);
			map=null;
		}
		return list;
	}
	
	
	@RequestMapping(params = "method=addSystem")
	@ResponseBody
	/**
	 * 添加文件提供系统管理
	 */
	public AjaxReturnInfo addSystem(
			@RequestParam(value = "systemName") String systemName,
			@RequestParam(value = "systemCode") String systemCode,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "systemDesc") String systemDesc,
			HttpServletRequest request) {
		String createrId=request.getSession().getAttribute(ConstantUtils.USERID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.USERID);
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		int flag = systemManageService.checkSysCode(systemCode);
		int result  = 0;
		if(flag ==0){
			SystemManageBean systemBean = new SystemManageBean();
			
			systemBean.setSystemName(systemName);
			systemBean.setSystemCode(systemCode);
			systemBean.setSystemStatus(status);
			systemBean.setSystemDesc(systemDesc);
			systemBean.setCreaterId(createrId);
			//String systemId=CommonTable.getTableId("r");
//			String systemId=String.valueOf(systemManageService.getSystemId());
			String createDate=CommonDate.getDate();
			systemBean.setSystemId(systemCode);
			systemBean.setCreateDate(createDate);
			result= systemManageService.addSystem(systemBean);
			systemBean=null;
		}else{
			result = -1;
		}
		
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("保存失败");
		}else if(result ==-1){
			ajaxinfo = AjaxReturnInfo.failed("系统编号已经存在,不能重复!");
		} else {
			//添加操作详情 20141203
			String operateDetail = "添加系统名称为"+ systemName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMESYSTEM,ConstantUtils.OPERTYPEADD,
			operateDetail);
			
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}

	@RequestMapping(params = "method=updateSystem")
	@ResponseBody
	/**
	 * 修改文件提供系统管理
	 */
	public AjaxReturnInfo updateSystem(
			@RequestParam(value = "systemName") String systemName,
			@RequestParam(value = "systemCode") String systemCode,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "systemDesc") String systemDesc,
			@RequestParam(value = "systemId") String systemId,
			HttpServletRequest request) {
		SystemManageBean systemBean = new SystemManageBean();
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		systemBean.setSystemId(systemId);
		systemBean.setSystemName(systemName);
		systemBean.setSystemCode(systemCode);
		systemBean.setSystemStatus(status);
		systemBean.setSystemDesc(systemDesc);
		int result = systemManageService.updateSystem(systemBean);
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("修改失败");
		} else {
			//添加操作详情 20141203
			String operateDetail = "修改系统名称为" + systemName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMESYSTEM,ConstantUtils.OPERTYPEUPD,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("修改成功");
		}
		return ajaxinfo;
	}
	
	@RequestMapping(params = "method=deleteSystem")
	@ResponseBody
	public AjaxReturnInfo deleteRole(@RequestParam(value = "Ids") String Ids,
			HttpServletRequest request) {
		AjaxReturnInfo ajaxinfo = null;
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
			int result = systemManageService.deleteSystem(Ids);
			if (result == 0) {
				ajaxinfo = AjaxReturnInfo.failed("删除失败");
			} else {
				//添加操作详情 20141203
				String operateDetail = "删除系统ID为" + Ids;
				// 添加进系统日志表 20141203
				systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMESYSTEM,ConstantUtils.OPERTYPEDEL,
				operateDetail);
				ajaxinfo = AjaxReturnInfo.success("删除成功");
			}
		
		return ajaxinfo;
	}
}
