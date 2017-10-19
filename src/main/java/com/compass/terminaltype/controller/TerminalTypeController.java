package com.compass.terminaltype.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.systemlog.service.SystemLogService;
import com.compass.terminaltype.model.TerminalTypeBean;
import com.compass.terminaltype.service.TerminalTypeService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * 
 * @author gaoyang
 * 终端类型管理Controller
 */
@Controller
@RequestMapping("/terminaltype/terminaltype.do")
public class TerminalTypeController {
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier("terminalTypeService")
	private TerminalTypeService  terminalTypeService;
	
	//系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService; 
	/**
	 * 获取终端类型列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getTerminalTypes")
	@ResponseBody
	public Map<String, Object> getTerminalTypes(
			@RequestParam(value = "terminalTypeName") String terminalTypeName,
			@RequestParam(value = "status") String status,
			HttpServletRequest request){
		log.info("获取终端类型列表");
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		String systemId=request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
		String roletypeid=request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		if("1".equals(roletypeid.trim())){
			systemId="";
		}
		List<TerminalTypeBean> list = terminalTypeService.getTerminalType(terminalTypeName,status,systemId);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1": page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20": rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		
		List<TerminalTypeBean> terminal_typelist = new ArrayList<TerminalTypeBean>();
		if(list != null && list.size() > 0){
			for(int i = start; i < end; i++){
				terminal_typelist.add(list.get(i));
			}
		}
		//添加操作详情 20141203
		String operateDetail = "查询的终端类型为"+ terminalTypeName;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMETERMTYPE,ConstantUtils.OPERTYPESER,
		operateDetail);
		
		return AjaxReturnInfo.setTable(list==null?0:list.size(), terminal_typelist);
	}
	
	/**
	 * 添加终端类型管理
	 * @param terminalTypeName
	 * @param status
	 * @param terminalTypeDesc
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addTerminalType")
	@ResponseBody
	public AjaxReturnInfo addTerminalType(
			@RequestParam(value = "systemId") String systemId,
			@RequestParam(value = "terminalTypeName") String terminalTypeName,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "terminalTypeDesc") String terminalTypeDesc,
			HttpServletRequest request){
		log.info("添加终端类型管理");
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		
		String createrId = request.getSession().getAttribute(ConstantUtils.USERID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.USERID);
		//String systemId=request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
		TerminalTypeBean terminalTypeBean = new TerminalTypeBean();
		terminalTypeBean.setTerminalTypeName(terminalTypeName);
		terminalTypeBean.setTerminalTypeStatus(status);
		terminalTypeBean.setTerminalTypeDesc(terminalTypeDesc);
		terminalTypeBean.setCreaterId(createrId);
		terminalTypeBean.setSystemId(systemId);
		//String terminalTypeId = CommonTable.getTableId("r");
		String terminalTypeId = String.valueOf(terminalTypeService.getTerminaltypeId());
		String createDate = CommonDate.getDate();
		terminalTypeBean.setTerminalTypeId(terminalTypeId);
		terminalTypeBean.setCreateDate(createDate);
		int result = terminalTypeService.addTerminalType(terminalTypeBean);
		AjaxReturnInfo ajaxinfo = null;
		if(result == 0){
			log.info("添加终端类型失败");
			ajaxinfo = AjaxReturnInfo.failed("保存失败");
		}else{
			log.info("添加终端类型成功");
			//添加操作详情 20141203
			String operateDetail = "添加的终端为"+ terminalTypeName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMETERMTYPE,ConstantUtils.OPERTYPEADD,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}
	
	/**
	 * 修改终端类型管理
	 * @param terminalTypeName
	 * @param status
	 * @param terminalTypeDesc
	 * @param terminalTypeId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateTerminalType")
	@ResponseBody
	public AjaxReturnInfo updateTerminalType(
			@RequestParam(value = "systemId") String systemId,
			@RequestParam(value = "terminalTypeName") String terminalTypeName,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "terminalTypeDesc") String terminalTypeDesc,
			@RequestParam(value = "terminalTypeId") String terminalTypeId,
			HttpServletRequest request){
		log.info("修改终端类型，终端类型："+terminalTypeId+terminalTypeDesc);
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
	
		TerminalTypeBean terminalTypeBean = new TerminalTypeBean();
		terminalTypeBean.setSystemId(systemId);
		terminalTypeBean.setTerminalTypeId(terminalTypeId);
		terminalTypeBean.setTerminalTypeName(terminalTypeName);
		terminalTypeBean.setTerminalTypeStatus(status);
		terminalTypeBean.setTerminalTypeDesc(terminalTypeDesc);
		int result = terminalTypeService.updateTerminalType(terminalTypeBean);
		AjaxReturnInfo ajaxinfo = null;
		if(result == 0){
			log.info("修改终端类型修改失败，终端类型："+terminalTypeId+terminalTypeDesc);
			ajaxinfo = AjaxReturnInfo.failed("修改失败");
		}else{
			log.info("修改终端类型修改成功，终端类型："+terminalTypeId+terminalTypeDesc);
			//添加操作详情 20141203
			String operateDetail = "修改的终端为"+ terminalTypeName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMETERMTYPE,ConstantUtils.OPERTYPEUPD,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("修改成功");
		}
		return ajaxinfo;
	}
	
	/**
	 * 删除终端类型
	 * @param Ids
	 * @return
	 */
	@RequestMapping(params = "method=deleteTerminalType")
	@ResponseBody
	public AjaxReturnInfo deleteTerminalType(
			@RequestParam(value = "Ids") String Ids,
			HttpServletRequest request){
		log.info("删除终端类型,"+Ids);
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
			
		AjaxReturnInfo ajaxinfo = null;
		int result = terminalTypeService.deleteTerminalType(Ids);
		if(result == 0){
			log.info("删除终端类型失败,"+Ids);
			ajaxinfo = AjaxReturnInfo.failed("删除失败");
		}else{
			log.info("删除终端类型成功,"+Ids);
			//添加操作详情 20141203
			String operateDetail = "删除的终端为"+ Ids;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMETERMTYPE,ConstantUtils.OPERTYPEDEL,
			operateDetail);
			ajaxinfo = AjaxReturnInfo.success("删除成功");
		}
		return ajaxinfo;
	}
	
	/**
	 * 获得终端类型名称（下拉列表）
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getTerminalTypeName")
	@ResponseBody
	public List<Map<String, Object>> getTerminalManageName(HttpServletRequest request) {
		log.info("获得终端类型名称（下拉列表）");
		String systemId=request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
		String roletypeid=request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		if("1".equals(roletypeid.trim())){
			systemId="";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<TerminalTypeBean> relist = terminalTypeService.getTerminalTypeName(systemId);
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择终端类型");
			map.put("selected", true);
			list.add(map);
		}
		for (int i = 0; i < relist.size(); i++) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", relist.get(i).getTerminalTypeId());
			map1.put("text", relist.get(i).getTerminalTypeName());
			list.add(map1);
		}
		
		return list;
	}
}
