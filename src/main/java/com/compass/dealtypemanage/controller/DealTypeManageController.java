/**
 * 
 */
package com.compass.dealtypemanage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.authority.service.AuthorityService;
import com.compass.dealtypemanage.model.DealTypeManageBean;
import com.compass.dealtypemanage.service.DealTypeManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangLong 交易类型管理Controller
 */

@Controller
@RequestMapping("/dealtype/dealtype.do")
public class DealTypeManageController {

	@Autowired
	@Qualifier("dealTypeManageService")
	private DealTypeManageService dealTypeManageService;
	// 系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;

	@Autowired
	@Qualifier("authorityService")
	private AuthorityService authorityService;

	/**
	 * 获得交易类型列表
	 */
	@RequestMapping(params = "method=getDealTypes")
	@ResponseBody
	public Map<String, Object> getDealTypes(@RequestParam(value = "dealTypeName") String dealTypeName, @RequestParam(value = "status") String status,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		if ("1".equals(roletypeid.trim())) {
			systemId = "";
		}
		List<DealTypeManageBean> list = dealTypeManageService.getDealTypes(dealTypeName, status, systemId);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<DealTypeManageBean> dealTypeList = new ArrayList<DealTypeManageBean>();
		if (list != null && list.size() > 0) {
			for (int i = start; i < end; i++) {
				dealTypeList.add(list.get(i));
			}
		}
		dealTypeList = null;
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + dealTypeName + status;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALTYPE, ConstantUtils.OPERTYPESER, operateDetail);
		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), list);
	}

	/**
	 * 获得所有有效的交易类型（在某一个agencyid下面，下拉框使用）
	 */
	@RequestMapping(params = "method=getCombDealTypesByagencyId")
	@ResponseBody
	public List<Map<String, Object>> getCombDealTypesByagencyId(HttpServletRequest request) {
		String agencyId = request.getParameter("agencyId");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择交易类型");
		map.put("selected", true);
		list.add(map);
		map = null;
		List<DealTypeManageBean> dtypeList = dealTypeManageService.getCombDealTypesByagencyId(agencyId);
		for (int i = 0; i < dtypeList.size(); i++) {
			DealTypeManageBean bean = dtypeList.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", bean.getDealTypeId());
			map1.put("text", bean.getDealTypeName());
			list.add(map1);
		}
		return list;
	}

	/**
	 * 14.11.03 新增需求 获得所有有效的交易类型（下拉框使用）
	 */
	@RequestMapping(params = "method=getCombDealTypesAll")
	@ResponseBody
	public List<Map<String, Object>> getCombDealTypesAll(HttpServletRequest request) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择交易类型");
		map.put("selected", true);
		list.add(map);
		map = null;
		List<DealTypeManageBean> dtypeList = dealTypeManageService.getCombDealTypesAll();
		for (int i = 0; i < dtypeList.size(); i++) {
			DealTypeManageBean bean = dtypeList.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", bean.getDealTypeId());
			map1.put("text", bean.getDealTypeName());
			list.add(map1);
		}
		return list;
	}

	@RequestMapping(params = "method=getCombDealTypes")
	@ResponseBody
	public List<Map<String, Object>> getCombDealTypes(HttpServletRequest request) {
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String flag = request.getParameter("flag");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		List<DealTypeManageBean> dtypeList = dealTypeManageService.getCombDealTypes(systemId);
		if (StringUtils.isNotEmpty(flag) && "1".equals(flag.toString().trim())) {
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择交易类型");
			list.add(map);
			String dealtype = authorityService.getParamObject("DEALTYPE_ACTIVITY");
			for (int i = 0; i < dtypeList.size(); i++) {
				DealTypeManageBean bean = dtypeList.get(i);
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("id", bean.getDealTypeId());
				map1.put("text", bean.getDealTypeName());
				String[] dealTypeArr = dealtype.split(",");
				List<String> dealTypeList = Arrays.asList(dealTypeArr);
				if (dealTypeList.contains(bean.getDealTypeId())) {
					map1.put("selected", true);
				}
				list.add(map1);
				map1 = null;
				dealTypeArr = null;
				dealTypeList = null;
			}
		} else {
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择交易类型");
			map.put("selected", true);
			list.add(map);
			map = null;
			for (int i = 0; i < dtypeList.size(); i++) {
				DealTypeManageBean bean = dtypeList.get(i);
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("id", bean.getDealTypeId());
				map1.put("text", bean.getDealTypeName());
				list.add(map1);
			}
		}

		return list;
	}

	/**
	 * 添加交易类型管理
	 */
	@RequestMapping(params = "method=addDealType")
	@ResponseBody
	public AjaxReturnInfo addDealType(@RequestParam(value = "dealTypeName") String dealTypeName, @RequestParam(value = "status") String status,
			@RequestParam(value = "dealTypeDesc") String dealTypeDesc,
			// 14.10.31 新需求，交易类型新增时，加入服务编码和渠道编码
			@RequestParam(value = "dealTypeServCode") String dealTypeServCode, @RequestParam(value = "dealTypeTradCode") String dealTypeTradCode,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.USERID);
		DealTypeManageBean dealTypeBean = new DealTypeManageBean();

		dealTypeBean.setDealTypeName(dealTypeName);
		dealTypeBean.setDealTypeStatus(status);
		dealTypeBean.setDealTypeDesc(dealTypeDesc);
		dealTypeBean.setCreaterId(createrId);
		// String dealTypeId=CommonTable.getTableId("r");
		String dealTypeId = String.valueOf(dealTypeManageService.getDealtypeId());
		String createDate = CommonDate.getDate();
		dealTypeBean.setDealTypeId(dealTypeId);
		dealTypeBean.setCreateDate(createDate);
		// 14.10.31 新需求，交易类型新增时，加入服务编码和渠道编码
		dealTypeBean.setServerCode(dealTypeServCode);
		dealTypeBean.setTradeCode(dealTypeTradCode);

		int result = dealTypeManageService.addDealType(dealTypeBean);
		dealTypeBean = null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("保存失败");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "添加交易类型名称为" + dealTypeName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALTYPE, ConstantUtils.OPERTYPEADD, operateDetail);
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}

	/**
	 * 修改交易类型管理
	 */
	@RequestMapping(params = "method=updateDealType")
	@ResponseBody
	public AjaxReturnInfo updateDealType(@RequestParam(value = "dealTypeName") String dealTypeName, @RequestParam(value = "status") String status,
			@RequestParam(value = "dealTypeDesc") String dealTypeDesc, @RequestParam(value = "dealTypeId") String dealTypeId,
			// 14.10.31 新需求，交易类型更新时，加入服务编码和渠道编码
			@RequestParam(value = "dealTypeServCode") String dealTypeServCode, @RequestParam(value = "dealTypeTradCode") String dealTypeTradCode,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		DealTypeManageBean dealTypeBean = new DealTypeManageBean();
		dealTypeBean.setDealTypeId(dealTypeId);
		dealTypeBean.setDealTypeName(dealTypeName);
		dealTypeBean.setDealTypeStatus(status);
		dealTypeBean.setDealTypeDesc(dealTypeDesc);
		// 14.10.31 新需求，交易类型更新时，加入服务编码和渠道编码
		dealTypeBean.setServerCode(dealTypeServCode);
		dealTypeBean.setTradeCode(dealTypeTradCode);
		int result = dealTypeManageService.updateDealType(dealTypeBean);
		dealTypeBean = null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("修改失败");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "修改交易类型" + dealTypeName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALTYPE, ConstantUtils.OPERTYPEUPD, operateDetail);
			ajaxinfo = AjaxReturnInfo.success("修改成功");
		}
		return ajaxinfo;
	}

	/**
	 * 删除交易类型
	 * 
	 * @param Ids
	 * @return
	 */
	@RequestMapping(params = "method=deleteDealType")
	@ResponseBody
	public AjaxReturnInfo deleteRole(@RequestParam(value = "Ids") String Ids, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		AjaxReturnInfo ajaxinfo = null;

		int result = dealTypeManageService.deleteDealType(Ids);
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("删除失败");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "删除交易类型ID" + Ids;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALTYPE, ConstantUtils.OPERTYPEDEL, operateDetail);
			ajaxinfo = AjaxReturnInfo.success("删除成功");
		}

		return ajaxinfo;
	}

	@RequestMapping(params = "method=getCombDealTypeByAgencyId")
	@ResponseBody
	public List<Map<String, Object>> getCombDealTypeByAgencyId(HttpServletRequest request) {

		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			agencyId = "";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择交易类型");
		map.put("selected", true);
		list.add(map);
		map = null;
		List<DealTypeManageBean> dtypeList = dealTypeManageService.getCombDealTypeByAgencyId(agencyId);
		for (int i = 0; i < dtypeList.size(); i++) {
			DealTypeManageBean bean = dtypeList.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", bean.getDealTypeId());
			map1.put("text", bean.getDealTypeName());
			list.add(map1);
		}
		return list;
	}
}
