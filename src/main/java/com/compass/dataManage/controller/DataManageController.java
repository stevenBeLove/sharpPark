package com.compass.dataManage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.dataManage.model.FrunFeeBean;
import com.compass.dataManage.model.ProcLogBean;
import com.compass.dataManage.service.DataService;
import com.compass.splitfee.model.ResetBean;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/data/data.do")
public class DataManageController {

	private final Log log = LogFactory.getLog(getClass());

	// 系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;
	@Autowired
	@Qualifier("dataService")
	private DataService dataService;

	/**
	 * 查询出存储过程执行记录
	 * 
	 * @param agencyId
	 * @param execDate
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getProcLog")
	@ResponseBody
	public Map<String, Object> getProcLog(
			@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "execDate") String execDate,
			HttpServletRequest request) {
		log.info("查询出存储过程执行记录");
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer count = dataService.getProcLogCount(agencyId, execDate);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		List<ProcLogBean> list = dataService.getProcLog(agencyId, execDate,
				start, end);
		return AjaxReturnInfo.setTable(list == null ? 0 : count, list);
	}

	/**
	 * 代理商分润计算
	 * 
	 * @param startDate
	 *            开始计算 日期
	 * @param endDat
	 *            结束计算日期
	 * @param request
	 *            结果返回
	 * @return
	 */
	@RequestMapping(params = "method=agencyFrSave")
	@ResponseBody
	public AjaxReturnInfo agencyFrSave(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			HttpServletRequest request) {
		log.info("代理商分润计算,时间" + startDate + "-" + endDate);
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("计算失败");
		if (StringUtils.isEmpty(startDate)) {
			log.info("代理商分润计算,开始时间为空");
			ajaxinfo = AjaxReturnInfo.failed("开始时间不能为 空");
			return ajaxinfo;
		}
		if (StringUtils.isEmpty(endDate)) {
			log.info("代理商分润计算,结束时间为空");
			ajaxinfo = AjaxReturnInfo.failed("结束时间不能为 空");
			return ajaxinfo;
		}
		dataService.agencyFrCal(startDate, endDate);
		ajaxinfo = AjaxReturnInfo.success("正在计算，请稍后查询日志。");
		return ajaxinfo;
	}

	/**
	 * 申请重算记录查询
	 * 
	 * @param agencyId
	 *            申请机构
	 * @param applyDate
	 *            申请日期
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getResetAll")
	@ResponseBody
	public Map<String, Object> getResetAll(
			@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "applyDate") String applyDate,
			HttpServletRequest request) {
		log.info("申请重算记录查询,申请机构  " + agencyId + ",时间" + applyDate);
		String loginAgencyId = request.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer count = dataService.getResetCount(agencyId, applyDate,
				loginAgencyId);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		List<ResetBean> list = dataService.getResetAll(agencyId, applyDate,
				loginAgencyId, start, end);
		return AjaxReturnInfo.setTable(list == null ? 0 : count, list);
	}

	/**
	 * 计算重算申请的机构的的分润
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=againCalFrSave")
	@ResponseBody
	public AjaxReturnInfo againCalFrSave(HttpServletRequest request) {
		log.info("计算重算申请的机构的的分润");
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("计算失败");
		dataService.againCalReset();
		ajaxinfo = AjaxReturnInfo.success("正在计算，请稍后查询日志。");
		return ajaxinfo;
	}

	/**
	 * 导入某一天的交易记录
	 * 
	 * @param execDate
	 *            导入日期
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=dealImp")
	@ResponseBody
	public AjaxReturnInfo dealImp(
			@RequestParam(value = "execDate") String execDate,
			HttpServletRequest request) {
		log.info("导入某一天的交易记录,执行日期：" + execDate);
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("计算失败");
		if (StringUtils.isEmpty(execDate)) {
			log.info("导入某一天的交易记录,执行日期不能为空");
			ajaxinfo = AjaxReturnInfo.failed("执行日期不能为空");
			return ajaxinfo;
		}
		String result = dataService.dealImp(execDate);
		log.info("导入某一天的交易记录,执行日期：" + execDate + "," + result);
		ajaxinfo = AjaxReturnInfo.success("正在计算，请稍后查询日志。");
		return ajaxinfo;
	}

	/**
	 * 一级代理商分润计算
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=fristCal")
	@ResponseBody
	public AjaxReturnInfo fristCal(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "prepbranchid") String prepbranchid,
			@RequestParam(value = "branchid") String branchid,
			HttpServletRequest request) {
		log.info("一级代理商分润计算,时间" + startDate + "-" + endDate);
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("计算失败");
		if (StringUtils.isEmpty(startDate)) {
			log.info("一级代理商分润计算,开始时间为空");
			ajaxinfo = AjaxReturnInfo.failed("开始时间不能为 空");
			return ajaxinfo;
		}
		if (StringUtils.isEmpty(endDate)) {
			log.info("一级代理商分润计算,结束时间为空");
			ajaxinfo = AjaxReturnInfo.failed("结束时间不能为 空");
			return ajaxinfo;
		}

		if (StringUtils.isEmpty(prepbranchid)) {
			prepbranchid = "QT";
		}
		if (StringUtils.isEmpty(branchid)) {
			branchid = "QT";
		}
		
		if("-1".equals(prepbranchid)){
			prepbranchid = "QT";
		}
		if ("-1".equals(branchid)) {
			branchid = "QT";
		}
		String result = dataService.agencyFristCal(startDate, endDate,
				prepbranchid, branchid);
		log.info("一级代理商分润计算,时间" + startDate + "-" + endDate + "," + result);
		ajaxinfo = AjaxReturnInfo.success("正在计算，请稍后查询日志。");
		return ajaxinfo;
	}

	/**
	 * 终端激活
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=terminalActive")
	@ResponseBody
	public AjaxReturnInfo terminalActive(HttpServletRequest request) {
		log.info("终端激活");
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("计算失败");
		int result = dataService.updateTerminalActice();
		log.info("终端激活处理成功");
		ajaxinfo = AjaxReturnInfo.success("终端激活处理的数量：" + result);
		return ajaxinfo;
	}

	/**
	 * 查询费率成本
	 * 
	 * @param systemid
	 * @param status
	 * @param ealtype
	 * @param request
	 * @return
	 * 
	 * @return Map<String,Object>
	 */
	@RequestMapping(params = "method=getfrunfee")
	@ResponseBody
	public Map<String, Object> getFrunfee(
			@RequestParam(value = "systemid") String systemid,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "dealtype") String dealtype,
			@RequestParam(value = "branchid") String branchid,
			HttpServletRequest request) {
		log.info("一级代理商费查询");
		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("查询失败");
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer count = dataService.getFeeCount(systemid, status, dealtype,
				branchid);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		List<FrunFeeBean> feeList = dataService.getFrunFee(systemid, status,
				dealtype, branchid, start, end);
		return AjaxReturnInfo.setTable(feeList == null ? 0 : count, feeList);
	}

	/**
	 * 修改费率成本
	 *
	 * @param systemid
	 * @param status
	 * @param prepbranch
	 * @param branchid
	 * @param fee
	 * @param feestatus
	 * @param dealtype
	 * @param request
	 * @return
	 * 
	 * @return AjaxReturnInfo
	 */
	@RequestMapping(params = "method=updateFrunfee")
	@ResponseBody
	public AjaxReturnInfo updateFrunfee(
			@RequestParam(value = "systemid") String systemid,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "prepbranch") String prepbranch,
			@RequestParam(value = "branchid") String branchid,
			@RequestParam(value = "fee") String fee,
			@RequestParam(value = "feestatus") String feestatus,
			@RequestParam(value = "dealtype") String dealtype,
			@RequestParam(value = "shopno") String shopno,
			HttpServletRequest request) {
		log.info("费率修改：" + systemid + ",prepbranch:" + prepbranch
				+ ",branchid:" + branchid);
		String ipAddress = request.getSession()
				.getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID)
				.toString();
		String agencyId = request.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();

		AjaxReturnInfo ajaxinfo = AjaxReturnInfo.success("修改成功!");
		int result = dataService.updateFunFee(fee, systemid, status,
				prepbranch, branchid, feestatus, dealtype, shopno);
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("修改失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "费率修改：" + systemid + ",prepbranch:"
					+ prepbranch + ",branchid:" + branchid;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, "数据管理",
					ConstantUtils.OPERTYPEUPD, operateDetail);
		}
		return ajaxinfo;
	}

	@RequestMapping(params = "method=getbranch")
	@ResponseBody
	public List<Map<String, Object>> getCombBranch(HttpServletRequest request) {
		String systemid = request.getParameter(ConstantUtils.SYSTEMID);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择机构");
		map.put("selected", true);
		list.add(map);
		map = null;
		List<FrunFeeBean> fList = dataService.getbranch(systemid);
		for (int i = 0; i < fList.size(); i++) {
			FrunFeeBean bean = fList.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", bean.getBranchid());
			map1.put("text", bean.getBranchname());
			list.add(map1);
		}
		return list;
	}

	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getPrepBranchid")
	@ResponseBody
	public List<Map<String, Object>> getPrepBranchid(HttpServletRequest request) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择");
		map.put("selected", true);
		list.add(map);
		List<ResetBean> agencylist = dataService.getPrepBranchid();
		for (int i = 0; i < agencylist.size(); i++) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", agencylist.get(i).getParentAgencyId());
			map1.put("text", agencylist.get(i).getParentAgencyName());
			list.add(map1);
		}
		return list;
	}

	/**
	 * 根据省编号获得所有的市
	 * 
	 * @param provinceId
	 * @return
	 */
	@RequestMapping(params = "method=getBranchid")
	@ResponseBody
	public List<Map<String, Object>> getBranchid(
			@RequestParam(value = "prepBranchid") String prepBranchid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择");
		map.put("selected", true);
		list.add(map);
		List<ResetBean> agencylist = dataService.getBranchid(prepBranchid);
		for (int i = 0; i < agencylist.size(); i++) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", agencylist.get(i).getAgencyId());
			map1.put("text", agencylist.get(i).getAgencyName());
			list.add(map1);
		}
		return list;
	}

}
