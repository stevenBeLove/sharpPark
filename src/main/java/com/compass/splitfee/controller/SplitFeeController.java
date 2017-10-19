/**
 * 
 */
package com.compass.splitfee.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Range;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.SpecSystemBean;
import com.compass.agency.service.AgencyService;
import com.compass.dealmanager.model.RealDeductBean;
import com.compass.dealmanager.service.DealManageService;
import com.compass.splitfee.model.SplitFeeBean;
import com.compass.splitfee.model.SplitFeeDealBean;
import com.compass.splitfee.model.SplitFeeDealType;
import com.compass.splitfee.service.SplitFeeService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.terminalmanage.model.TerminalCountBean;
import com.compass.terminalmanage.service.TerminalManageService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangLong 分润管理Controller
 */

@Controller
@RequestMapping("/splitfee/splitfee.do")
public class SplitFeeController {
	private static Logger log = LoggerFactory.getLogger(SplitFeeController.class);

	@Autowired
	@Qualifier("splitFeeService")
	private SplitFeeService splitFeeService;
	@Autowired
	@Qualifier("agencyService")
	private AgencyService agencyService;
	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;
	@Autowired
	@Qualifier("dealManageService")
	private DealManageService dealManageService;
	// 系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;
	@Autowired
	@Qualifier("terminalManageService")
	public TerminalManageService terminalManageService;

	// private String Flag="0";

	/**
	 * 获取分润执行日志
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSplitFeeLog")
	@ResponseBody
	public Map<String, Object> getSplitFeeLog(HttpServletRequest request) {

		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : request.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();
		Integer totalCount = splitFeeService.getSplitFeeCount(agencyId);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		List<SplitFeeBean> splitFeelist = splitFeeService.getSplitFeeLog(agencyId, start, end);
		return AjaxReturnInfo.setTable(totalCount, splitFeelist);
	}

	/**
	 * 获得分润信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSplitFeeByAgencyDealType")
	@ResponseBody
	public Map<String, Object> getSplitFeeByAgencyDealType(@RequestParam(value = "reportData") String reportData, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		List<SplitFeeDealType> list = splitFeeService.splitFeeByAgencyDealType(agencyId, reportData);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<SplitFeeDealType> resultlist = new ArrayList<SplitFeeDealType>();
		if (list != null && list.size() > 0) {
			for (int i = start; i < end; i++) {
				resultlist.add(list.get(i));
			}
		}
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + reportData;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEE, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), resultlist);
	}

	/**
	 * 根据机构号和类型分润计算
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=splitFeeCalc")
	@ResponseBody
	public AjaxReturnInfo splitFeeCalc(@RequestParam(value = "dealTypeId") String dealTypeId, @RequestParam(value = "reportData") String reportData,
			@RequestParam(value = "type") String type, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String parentagencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString(); // 计算当前登录机构的某个类型的分润(计算下级，下级需确认)
		AjaxReturnInfo ajaxReturnInfo = null;

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			ajaxReturnInfo = AjaxReturnInfo.success("中心不参与分润计算");
		} else if (ConstantUtils.CENTERCODE.equals(parentagencyId)) {
			Integer count = splitFeeService.getFlag(agencyId);
			if (count > 0) {
				ajaxReturnInfo = AjaxReturnInfo.success("正在计算中，请耐心等待");
				ajaxReturnInfo.add("flag", count);
			} else {
				splitFeeService.addLog(agencyId, dealTypeId);
				splitFeeService.splitFee(reportData, agencyId, dealTypeId);
				splitFeeService.delFlag(agencyId, dealTypeId);
				// 添加操作详情 20141203
				String operateDetail = "计算条件为" + reportData + agencyId + dealTypeId;
				// 添加进系统日志表 20141203
				systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEE, ConstantUtils.OPERTYPESPLITFEE, operateDetail);
				ajaxReturnInfo = AjaxReturnInfo.success("计算完成");
			}
		} else {
			Integer exits = 0;
			if ("".equals(type)) {
				exits = splitFeeService.splitFeeByAgencyDealTypeCount(agencyId, reportData);
			}
			if (exits > 0) {
				ajaxReturnInfo = AjaxReturnInfo.success("分润没有确认完,不能全部计算");
			} else {
				Integer count = splitFeeService.getFlag(agencyId);
				if (count > 0) {
					ajaxReturnInfo = AjaxReturnInfo.success("正在计算中，请耐心等待");
					ajaxReturnInfo.add("flag", count);
				} else {
					splitFeeService.addLog(agencyId, dealTypeId);
					splitFeeService.splitFee(reportData, agencyId, dealTypeId);
					splitFeeService.delFlag(agencyId, dealTypeId);
					// 添加操作详情 20141203
					String operateDetail = "计算条件为" + reportData + agencyId + dealTypeId;
					// 添加进系统日志表 20141203
					systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEE, ConstantUtils.OPERTYPESPLITFEE,
							operateDetail);
					ajaxReturnInfo = AjaxReturnInfo.success("计算完成");
				}
			}
		}

		return ajaxReturnInfo;
	}

	/**
	 * 设置Bean数据
	 * 
	 * @param agencyId
	 * @param dealTypeId
	 * @param reportData
	 * @param parentagencyId
	 * @return
	 */
	private SplitFeeDealType SetBean(String agencyId, String dealTypeId, String reportData) {
		SplitFeeDealType bean = new SplitFeeDealType();
		bean.setAgencyId(agencyId);
		bean.setDealtype(dealTypeId);
		bean.setYearmonth(reportData);
		return bean;
	}

	/**
	 * 对分润金额确认
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=splitFeeAffirm")
	@ResponseBody
	public AjaxReturnInfo splitFeeAffirm(@RequestParam(value = "dealTypeId") String dealTypeId,
			@RequestParam(value = "reportData") String reportData, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();// 确认当前登录机构的某个类型的分润(上级计算)
		SplitFeeDealType bean = SetBean(agencyId, dealTypeId, reportData);
		int flag = splitFeeService.splitFeeAffirm(bean);
		AjaxReturnInfo ajaxReturnInfo = null;
		if (flag > 0) {
			// 添加操作详情 20141203
			String operateDetail = "确认的交易类型为" + dealTypeId + reportData;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEE, ConstantUtils.OPERTYPESPLITCONFIRM, operateDetail);
			ajaxReturnInfo = AjaxReturnInfo.success("分润已确认!");
		} else {
			ajaxReturnInfo = AjaxReturnInfo.failed("确认失败!");
		}
		return ajaxReturnInfo;
	}

	/**
	 * 查询当前机构对下级的分润
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getChildSplitFee")
	@ResponseBody
	public Map<String, Object> getChildSplitFee(
	// @RequestParam(value = "yearmonth") String yearmonth,
			@RequestParam(value = "agencyIdf") String agencyIdf,
			// @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		if ("1".equals(roletypeid.trim())) {
			systemId = "QT";
		}

		if (agencyIdf == null || "".equals(agencyIdf) || "-1".equals(agencyIdf)) {
			// agencyIdf = "QT";
			agencyIdf = agencyId;
		}

		String dealType = "QT";

		// Integer totalCount=splitFeeService.getChildSplitFeeCount(yearmonth,
		// agencyId, agencyIdf, dealType, systemId);
		Integer totalCount = splitFeeService.getChildSplitFeeCount(datestart, dateend, agencyId, agencyIdf, dealType, systemId);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");

		String dealCount = "0";// 总笔数
		Double transCount = 0d;// 总 交易金额
		Double amount = 0d;// 总分润到金额
		List<SplitFeeDealType> list = new ArrayList<SplitFeeDealType>();
		if (0 < totalCount) {
			int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
			int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
			list = splitFeeService.getChildSplitFee(datestart, dateend, agencyId, agencyIdf, dealType, systemId, start, end);
			for (int i = 0; i < list.size(); i++) {
				SplitFeeDealType sfd = list.get(i);
				Long dealCount1 = Long.valueOf(dealCount) + Long.valueOf(sfd.getDealCount() == null ? "0" : sfd.getDealCount());
				dealCount = dealCount1.toString();
				transCount += sfd.getTransCount();
				amount += sfd.getAmount();
			}
		}
		SplitFeeDealType sfd = new SplitFeeDealType();
		sfd.setDealtypeStr("合计");
		sfd.setDealCount(dealCount);
		sfd.setTransCount(Double.parseDouble(String.format("%.2f", transCount)));
		sfd.setAmount(Double.parseDouble(String.format("%.2f", amount)));
		list.add(sfd);
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + agencyIdf + datestart + dateend;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEESER, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 查询当前机构对下级的分润的历史数据
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getChildHisSplitFee")
	@ResponseBody
	public Map<String, Object> getChildHisSplitFee(
	// @RequestParam(value = "yearmonth") String yearmonth,
			@RequestParam(value = "agencyIdf") String agencyIdf,
			// @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		if ("1".equals(roletypeid.trim())) {
			systemId = "QT";
		}

		if (agencyIdf == null || "".equals(agencyIdf) || "-1".equals(agencyIdf)) {
			// agencyIdf = "QT";
			agencyIdf = agencyId;
		}
		/*
		 * if(dealType ==null||"".equals(dealType)||"-1".equals(dealType)){
		 * dealType ="QT"; }
		 */
		String dealType = "QT";

		// Integer totalCount=splitFeeService.getChildSplitFeeCount(yearmonth,
		// agencyId, agencyIdf, dealType, systemId);
		Integer totalCount = splitFeeService.getChildHisSplitFeeCount(datestart, dateend, agencyId, agencyIdf, dealType, systemId);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		// List<SplitFeeBean> splitFeelist =
		// splitFeeService.getSplitFeeLog(agencyId,start,end);
		// List<SplitFeeDealType> list =
		// splitFeeService.getChildSplitFee(yearmonth,agencyId,agencyIdf,dealType,systemId,start,end);
		List<SplitFeeDealType> list = splitFeeService.getChildHisSplitFee(datestart, dateend, agencyId, agencyIdf, dealType, systemId, start, end);
		String dealCount = "0";// 总笔数
		Double transCount = 0d;// 总 交易金额
		Double amount = 0d;// 总分润到金额
		for (int i = 0; i < list.size(); i++) {
			SplitFeeDealType sfd = list.get(i);
			Long dealCount1 = Long.valueOf(dealCount) + Long.valueOf(sfd.getDealCount() == null ? "0" : sfd.getDealCount());
			dealCount = dealCount1.toString();
			transCount += sfd.getTransCount();
			amount += sfd.getAmount();
		}
		SplitFeeDealType sfd = new SplitFeeDealType();
		sfd.setDealtypeStr("合计");
		sfd.setDealCount(dealCount);
		sfd.setTransCount(Double.parseDouble(String.format("%.2f", transCount)));
		sfd.setAmount(Double.parseDouble(String.format("%.2f", amount)));
		list.add(sfd);
		/*
		 * List<SplitFeeDealType> beanList=new ArrayList<SplitFeeDealType>(); if
		 * (list != null && list.size() > 0) { for(int i=start;i<end;i++){
		 * beanList.add(list.get(i)); } }
		 */

		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + agencyIdf + datestart + dateend;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOLDSPLITFEESER, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 导出当前机构对下级的分润
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=exportChildSplitFee")
	public void exportChildSplitFee(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		// String yearmonth=request.getParameter("yearmonth");
		// String agencyIdf=request.getParameter("agencyIdf");
		// String dealType=request.getParameter("dealType");
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		if ("1".equals(roletypeid.trim())) {
			systemId = "QT";
		}
		String agencyIdf = "QT";
		String dealType = "QT";
		try {
			Integer totalCount = splitFeeService.getChildSplitFeeCount(datestart, dateend, agencyId, agencyIdf, dealType, systemId);
			List<SplitFeeDealType> list = splitFeeService
					.getChildSplitFee(datestart, dateend, agencyId, agencyIdf, dealType, systemId, 0, totalCount);
			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
			File f = new File(urls);
			if (!f.exists()) {
				f.mkdirs();
			}

			String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTSPLITFEEFILENAME;

			File wj = new File(url);
			WritableWorkbook wwb = Workbook.createWorkbook(wj);
			WritableSheet ws = wwb.createSheet("dealDetail", 0);
			for (int i = 0; i < ConstantUtils.CHILDSPLITFEE.length; i++) {
				ws.addCell(new Label(i, 0, ConstantUtils.CHILDSPLITFEE[i]));
			}
			for (int i = 0; i < list.size(); i++) {
				int col = i + 1;
				ws.addCell(new Label(0, col, list.get(i).getAgnecyName()));
				ws.addCell(new Label(1, col, list.get(i).getDealtypeStr()));
				ws.addCell(new Label(2, col, list.get(i).getYearmonth()));
				ws.addCell(new Label(3, col, list.get(i).getTransCount().toString()));
				ws.addCell(new Label(4, col, list.get(i).getDealCount()));
				ws.addCell(new Label(5, col, list.get(i).getAmount().toString()));
			}

			wwb.write();
			wwb.close();

			download(request, response, url, ConstantUtils.EXPORTSPLITFEEFILENAME);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导出平台结算款确认函文件 20141107
	 * 
	 * @param datestart
	 * @param dateend
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws BiffException
	 */
	/*
	 * @RequestMapping(params = "method=exportConfirmationFile")
	 * 
	 * @ResponseBody public AjaxReturnInfo exportConfirmationFile(
	 * 
	 * @RequestParam(value = "agencyIdf") String agencyIdf,
	 * 
	 * @RequestParam(value = "datestart") String datestart,
	 * 
	 * @RequestParam(value = "dateend") String dateend, HttpServletRequest
	 * request, HttpServletResponse response ) throws IOException,
	 * RowsExceededException, WriteException, BiffException{ //获得ip String
	 * ipAddress =
	 * request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
	 * String userId =
	 * request.getSession().getAttribute(ConstantUtils.USERID).toString();
	 * 
	 * String
	 * agencyId=request.getSession().getAttribute(ConstantUtils.AGENCYID).
	 * toString().trim(); // String
	 * roletypeid=request.getSession().getAttribute(
	 * ConstantUtils.ROLETYPEID).toString().trim(); String
	 * systemId=request.getSession
	 * ().getAttribute(ConstantUtils.SYSTEMID).toString().trim(); AjaxReturnInfo
	 * ajaxReturnInfo =null;
	 * 
	 * // 获取本机构交易分润信息 // if("1".equals(roletypeid.trim())){ // systemId="QT"; //
	 * } if(agencyIdf==null||"".equals(agencyIdf)||"-1".equals(agencyIdf)){ //
	 * agencyIdf = "QT"; agencyIdf = agencyId; } String dealType ="QT";
	 * List<SplitFeeDealType> splitList = null; try { Integer
	 * totalCount=splitFeeService.getChildSplitFeeCount(datestart, dateend,
	 * agencyId, agencyIdf, dealType, systemId); if (totalCount > 0) {
	 * splitList=
	 * splitFeeService.getChildSplitFee(datestart,dateend,agencyId,agencyIdf
	 * ,dealType,systemId,0,totalCount); }
	 * 
	 * // 获取所选机构信息 AgencyBean agency = new AgencyBean();
	 * agency.setAgency_id(agencyIdf); List<AgencyBean> agencyList =
	 * agencyService.getAgency(agency); if (agencyList == null ||
	 * agencyList.size() == 0) { return AjaxReturnInfo.failed("获取机构信息失败"); }
	 * agency = agencyList.get(0);
	 * 
	 * // 获取所选机构（下级机构归属到所选机构）强扣信息 String upperAgencyid = agencyId; String
	 * dealFlag = "0"; // 0-未处理；1-已处理 String agencySelect = ""; // 认为未选取机构
	 * agencyId = agencyIdf; // 将选取机构作为开始统计机构 String parentAgencyid = ""; //
	 * 不按实际机构区分 systemId = ""; // 不区分来源系统
	 * 
	 * List<RealDeductBean> statList =
	 * dealManageService.getDeductStat(datestart,
	 * dateend,dealFlag,agencyId,systemId,agencySelect,
	 * upperAgencyid,parentAgencyid,"thisAgency"); List<RealDeductBean> realList
	 * =
	 * dealManageService.getRealDeduct(datestart,dateend,dealFlag,"real",agencyId
	 * ,systemId,agencySelect, upperAgencyid,parentAgencyid,"thisAgency");
	 * TerminalCountBean
	 * tcb=terminalManageService.getTerminalCount(agencyIdf,datestart,dateend);
	 * 
	 * // 导出到确认函文件 String url =
	 * doExportConfirmationFile(agency,splitList,statList,realList,dateend,tcb);
	 * System.out.println(url);
	 * download(request,response,url,url.substring(url.lastIndexOf("/")+1)); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return AjaxReturnInfo.failed("导出确认函文件失败"); }
	 * 
	 * // 添加进系统日志表 20141203
	 * systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils
	 * .OPERNAMESPLITFEESER,ConstantUtils.OPERTYPEEXPO, null); ajaxReturnInfo =
	 * AjaxReturnInfo.success("导出成功"); return ajaxReturnInfo; }
	 */
	/**
	 * 导出平台结算款确认函文件 20141107
	 * 
	 * @param datestart
	 * @param dateend
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws BiffException
	 */
	@RequestMapping(params = "method=exportConfirmationFile")
	@ResponseBody
	public AjaxReturnInfo exportConfirmationFile(@RequestParam(value = "agencyIdf") String agencyIdf,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend, HttpServletRequest request,
			HttpServletResponse response) throws IOException, RowsExceededException, WriteException, BiffException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		// String
		// roletypeid=request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		AjaxReturnInfo ajaxReturnInfo = null;

		// 获取本机构交易分润信息
		// if("1".equals(roletypeid.trim())){
		// systemId="QT";
		// }
		if (agencyIdf == null || "".equals(agencyIdf) || "-1".equals(agencyIdf)) {
			// agencyIdf = "QT";
			agencyIdf = agencyId;
		}
		String dealType = "QT";
		List<SplitFeeDealType> splitList = null;
		try {
			Integer totalCount = splitFeeService.getChildSplitFeeCount(datestart, dateend, agencyId, agencyIdf, dealType, systemId);
			if (totalCount > 0) {
				splitList = splitFeeService.getChildSplitFee(datestart, dateend, agencyId, agencyIdf, dealType, systemId, 0, totalCount);
			}

			// 获取所选机构信息
			AgencyBean agency = new AgencyBean();
			agency.setAgency_id(agencyIdf);
			List<AgencyBean> agencyList = agencyService.getAgency(agency);
			if (agencyList == null || agencyList.size() == 0) {
				return AjaxReturnInfo.failed("获取机构信息失败");
			}
			agency = agencyList.get(0);

			// 获取所选机构（下级机构归属到所选机构）强扣信息
			String upperAgencyid = agencyId;
			String dealFlag = "0"; // 0-未处理；1-已处理
			String agencySelect = ""; // 认为未选取机构
			agencyId = agencyIdf; // 将选取机构作为开始统计机构
			String parentAgencyid = ""; // 不按实际机构区分
			systemId = ""; // 不区分来源系统

			List<RealDeductBean> statList = dealManageService.getDeductStat(datestart, dateend, dealFlag, agencyId, systemId, agencySelect,
					upperAgencyid, parentAgencyid, "thisAgency");
			List<RealDeductBean> realList = dealManageService.getRealDeduct(datestart, dateend, dealFlag, "real", agencyId, systemId, agencySelect,
					upperAgencyid, parentAgencyid, "thisAgency");
			TerminalCountBean tcb = terminalManageService.getTerminalCount(agencyIdf, datestart, dateend);

			// 导出到确认函文件
			String url = doExportConfirmationFile(agency, splitList, statList, realList, dateend, tcb);
			System.out.println(url);
			download(request, response, url, url.substring(url.lastIndexOf("/") + 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxReturnInfo.failed("导出确认函文件失败");
		}

		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITFEESER, ConstantUtils.OPERTYPEEXPO, null);
		ajaxReturnInfo = AjaxReturnInfo.success("导出成功");
		return ajaxReturnInfo;
	}

	/**
	 * 导出平台历史结算款确认函文件 20141107
	 * 
	 * @param datestart
	 * @param dateend
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws BiffException
	 */
	@RequestMapping(params = "method=exportHisConfirmationFile")
	@ResponseBody
	public AjaxReturnInfo exportHisConfirmationFile(@RequestParam(value = "agencyIdf") String agencyIdf,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend, HttpServletRequest request,
			HttpServletResponse response) throws IOException, RowsExceededException, WriteException, BiffException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		// String
		// roletypeid=request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		AjaxReturnInfo ajaxReturnInfo = null;

		// 获取本机构交易分润信息
		// if("1".equals(roletypeid.trim())){
		// systemId="QT";
		// }
		if (agencyIdf == null || "".equals(agencyIdf) || "-1".equals(agencyIdf)) {
			// agencyIdf = "QT";
			agencyIdf = agencyId;
		}
		String dealType = "QT";
		List<SplitFeeDealType> splitList = null;
		try {
			Integer totalCount = splitFeeService.getChildHisSplitFeeCount(datestart, dateend, agencyId, agencyIdf, dealType, systemId);
			if (totalCount > 0) {
				splitList = splitFeeService.getChildHisSplitFee(datestart, dateend, agencyId, agencyIdf, dealType, systemId, 0, totalCount);
			}

			// 获取所选机构信息
			AgencyBean agency = new AgencyBean();
			agency.setAgency_id(agencyIdf);
			List<AgencyBean> agencyList = agencyService.getAgency(agency);
			if (agencyList == null || agencyList.size() == 0) {
				return AjaxReturnInfo.failed("获取机构信息失败");
			}
			agency = agencyList.get(0);

			// 获取所选机构（下级机构归属到所选机构）强扣信息
			String upperAgencyid = agencyId;
			String dealFlag = "0"; // 0-未处理；1-已处理
			String agencySelect = ""; // 认为未选取机构
			agencyId = agencyIdf; // 将选取机构作为开始统计机构
			String parentAgencyid = ""; // 不按实际机构区分
			systemId = ""; // 不区分来源系统

			List<RealDeductBean> statList = dealManageService.getDeductStat(datestart, dateend, dealFlag, agencyId, systemId, agencySelect,
					upperAgencyid, parentAgencyid, "thisAgency");
			List<RealDeductBean> realList = dealManageService.getRealDeduct(datestart, dateend, dealFlag, "real", agencyId, systemId, agencySelect,
					upperAgencyid, parentAgencyid, "thisAgency");
			TerminalCountBean tcb = terminalManageService.getTerminalCount(agencyIdf, datestart, dateend);

			// 导出到确认函文件
			String url = doExportConfirmationFile(agency, splitList, statList, realList, dateend, tcb);
			System.out.println(url);
			download(request, response, url, url.substring(url.lastIndexOf("/") + 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxReturnInfo.failed("导出确认函文件失败");
		}

		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOLDSPLITFEESER, ConstantUtils.OPERTYPEEXPO, null);
		ajaxReturnInfo = AjaxReturnInfo.success("导出成功");
		return ajaxReturnInfo;
	}

	/**
	 * 根据提供的数据导出确认函文件
	 * 
	 * @param agency
	 * @param splitList
	 * @param dealDeductList
	 * @param otherDeductList
	 * @throws IOException
	 * @throws BiffException
	 * @throws WriteException
	 */
	private String doExportConfirmationFile(AgencyBean agency, List<SplitFeeDealType> splitList, List<RealDeductBean> statList,
			List<RealDeductBean> realList, String exportDate, TerminalCountBean tcb) throws BiffException, IOException, WriteException {
		String exportYYYY = exportDate.substring(0, 4); // 导出数据所属年份
		String exportMM = exportDate.substring(4, 6); // 导出数据所属月份
		String exportYearMonth = exportYYYY + "年" + exportMM + "月";
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}
		// 导出确认函文件名
		String fileName = agency.getCompanyName() + "_" + agency.getContactsName() + "_" + exportYearMonth + "分润.xls";
		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + fileName;
		String template = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "WEB-INF/config/template/" + ConstantUtils.EXPORTCONFIRMATIONTEMPLATE;

		Workbook workbook = Workbook.getWorkbook(new File(template));
		WritableWorkbook copy = Workbook.createWorkbook(new File(url), workbook);
		WritableSheet ws = copy.getSheet(0);
		// 设置sheet名称，例如：2014年10月分润确认函
		ws.setName(exportYearMonth + "分润确认函");

		// 当日日期
		String today = CommonDate.getNowDate();
		String yyyy = today.substring(0, 4);
		String MM = today.substring(4, 6);
		String dd = today.substring(6, 8);

		// 填写代理商信息
		findCellSetString(ws, "brand", agency.getCompanyName()); // 品牌名称
		findCellSetString(ws, "agencyName", agency.getContactsName()); // 代理商姓名
		findCellSetString(ws, "accountName", agency.getAccountName()); // 账户名
		findCellSetString(ws, "openBank", agency.getAccountBank()); // 开户行
		findCellSetString(ws, "accountNo", agency.getBankcode()); // 账号
		findCellSetString(ws, "psamcount", tcb.getPsamCount() == null ? "0" : tcb.getPsamCount()); // 账号
		findCellSetString(ws, "addcount", tcb.getAddCount() == null ? "0" : tcb.getAddCount()); // 账号
		findCellSetString(ws, "activecount", tcb.getActiveCount() == null ? "0" : tcb.getActiveCount()); // 账号
		findCellSetString(ws, "label1", exportYearMonth + "应结算总金额"); // 年
																		// 月应结算总金额

		// 正常交易汇总数据
		Integer dealTotalNo = 0; // 交易总笔数
		Double dealTotalAmount = 0.00; // 交易总金额
		Double dealTotalProfit = 0.00; // 本月结算款

		Range[] range = ws.getMergedCells();
		// 填写正常交易数据列表
		Cell dealList = ws.findCell("dealList");
		findCellSetString(ws, "label2", exportYearMonth + " 平台业务结算汇总表");
		if (splitList != null) {
			int row = dealList.getRow();
			int rowNo = 1;
			for (int i = 0; i < splitList.size(); i++) {
				Integer totalNo = Integer.parseInt(splitList.get(i).getDealCount());
				Double totalAmount = splitList.get(i).getTransCount();
				Double profit = splitList.get(i).getAmount();

				// 交易总笔数、交易总金额、总结算金额
				dealTotalNo += totalNo;
				dealTotalAmount += totalAmount;
				dealTotalProfit += profit;

				// 复制当前行到当前行
				if (i == 0) {
					((Label) dealList).setString("");
				}
				copyCells(ws, range, 0, row + 1, ws.getColumns(), row + 1, 0, row);

				Cell cell = null;
				int col = dealList.getColumn();
				String totalAmountStr = String.format("%.2f", totalAmount);
				String profitStr = String.format("%.2f", profit);

				// 序号
				((Label) ws.getCell(col, row)).setString(String.valueOf(rowNo++));
				col++;
				// 交易类型（合并单元格）
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, splitList.get(i).getDealtypeStr(), cell.getCellFormat()));
				col += 3;
				// 总笔数
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, splitList.get(i).getDealCount(), cell.getCellFormat()));
				col++;
				// 总金额（合并单元格）
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, totalAmountStr, cell.getCellFormat()));
				col += 2;
				// 总结算金额（合并单元格）
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col++, row, profitStr, cell.getCellFormat()));

				row++;
			}

		}
		// 数据为空删除标记
		if (splitList == null || splitList.size() == 0) {
			((Label) dealList).setString("");
		}
		// 正常交易汇总信息
		String str = null;
		findCellSetString(ws, "dealTotalNo", String.valueOf(dealTotalNo));
		str = String.format("%.2f", dealTotalAmount);
		findCellSetString(ws, "dealTotalAmount", str);
		str = String.format("%.2f", dealTotalProfit);
		findCellSetString(ws, "dealTotalProfit", str);
		findCellSetString(ws, "dealTotalProfit2", "￥" + str);

		// 强扣汇总数据
		Double totalAmount = 0.00; // 本月强扣汇总总金额
		Double deductTotalAmount = 0.00; // 本月强扣汇总扣款总金额
		Double totalAmount2 = 0.00; // 本月实际应扣总金额
		Double deductTotalAmount2 = 0.00; // 本月实际实扣总金额
		Double finalAmount = 0.00; // 实际应结算总金额

		// 填写强扣数据汇总列表
		Cell deductList = ws.findCell("deductList");
		if (statList != null) {
			int row = deductList.getRow();
			for (int i = 0; i < statList.size(); i++) {
				Double transacount = statList.get(i).getTransacount();
				Double deductMoney = statList.get(i).getDeductMoney();

				// 汇总总金额|汇总扣款总金额
				totalAmount += transacount;
				deductTotalAmount += deductMoney;

				// 复制当前行到当前行
				if (i == 0) {
					((Label) deductList).setString("");
				}
				copyCells(ws, range, 0, row + 1, ws.getColumns(), row + 1, 0, row);

				Cell cell = null;
				int col = dealList.getColumn();
				String transacountStr = String.format("%.2f", transacount);
				String deductMoneyStr = String.format("%.2f", deductMoney);

				// 扣款类型|汇总总金额|汇总扣款总金额
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, statList.get(i).getDeductTypeName(), cell.getCellFormat()));
				col += 3;
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, transacountStr, cell.getCellFormat()));
				col += 3;
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, deductMoneyStr, cell.getCellFormat()));

				row++;
			}
		}
		// 数据为空删除标记
		if (statList == null || statList.size() == 0) {
			((Label) deductList).setString("");
		}

		// 填写实际扣款列表
		Cell deductList2 = ws.findCell("deductList2");
		if (realList != null) {
			int row = deductList2.getRow();
			for (int i = 0; i < realList.size(); i++) {
				Double transacount = realList.get(i).getTransacount();
				Double deductMoney = realList.get(i).getDeductMoney();

				// 应扣总金额|实扣总金额
				totalAmount2 += transacount;
				deductTotalAmount2 += deductMoney;

				// 复制当前行到当前行
				if (i == 0) {
					((Label) deductList2).setString("");
				}
				copyCells(ws, range, 0, row + 1, ws.getColumns(), row + 1, 0, row);

				Cell cell = null;
				int col = deductList2.getColumn();
				String transacountStr = String.format("%.2f", transacount);
				String deductMoneyStr = String.format("%.2f", deductMoney);

				// 扣款类型|应扣金额|实扣金额|应扣月份|实扣月份|说明
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, realList.get(i).getDeductTypeName(), cell.getCellFormat()));
				col += 2;
				((Label) ws.getCell(col++, row)).setString(transacountStr);
				((Label) ws.getCell(col++, row)).setString(deductMoneyStr);
				((Label) ws.getCell(col++, row)).setString(realList.get(i).getDeductMonth());
				((Label) ws.getCell(col++, row)).setString(realList.get(i).getRealDeductMonth());
				cell = ws.getCell(col, row);
				ws.addCell(new Label(col, row, realList.get(i).getComments(), cell.getCellFormat()));

				row++;
			}
		}
		// 数据为空删除标记
		if (realList == null || realList.size() == 0) {
			((Label) deductList2).setString("");
		}

		// 强扣数据汇总信息
		str = String.format("%.2f", totalAmount);
		findCellSetString(ws, "totalAmount", str);
		str = String.format("%.2f", deductTotalAmount);
		findCellSetString(ws, "deductTotalAmount", str);
		str = String.format("%.2f", totalAmount2);
		findCellSetString(ws, "totalAmount2", str);
		str = String.format("%.2f", deductTotalAmount2);
		findCellSetString(ws, "deductTotalAmount2", str);
		findCellSetString(ws, "deductTotalAmount3", "￥" + str);

		// 填写汇总信息
		finalAmount = dealTotalProfit - deductTotalAmount2;
		str = String.format("%.2f", finalAmount);
		findCellSetString(ws, "finalAmount", "￥" + str + " 元");
		findCellSetString(ws, "RMBNumber", "￥" + str);
		findCellSetString(ws, "date", yyyy + " 年 " + MM + " 月 " + dd + " 日");

		// 人民币大写
		String RMBCapital = "";
		if (finalAmount < 0) {
			RMBCapital = num2Chinese(-finalAmount);
			RMBCapital = "负" + RMBCapital;
		} else {
			RMBCapital = num2Chinese(finalAmount);
		}
		findCellSetString(ws, "RMBCapital", RMBCapital);

		// 文件写入及关闭
		copy.write();
		copy.close();

		return url;
	}

	/**
	 * 复制指定范围的表格到指定位置
	 * 
	 * @param sheet
	 *            操作对象
	 * @param mergedCell
	 *            所有合并单元格范围 考虑到该方法可能被循环调用多次，而且是复制同一个表格到不同地方，因此本参数记录的原始
	 *            被拷贝表格中的单元格，避免在多次循环后，本参数数据量不断增加，导致遍历时间太长。
	 *            即本参数值需在调用本方法的循环外就已经获得。
	 * @param from1Cols
	 *            被复制表格开始列
	 * @param from1Row
	 *            被复制表格开始行
	 * @param to1Col
	 *            被复制表格结束列
	 * @param to1Row
	 *            被复制表格结束行
	 * @param from2Col
	 *            复制到表格开始列
	 * @param from2Row
	 *            复制到表格开始行
	 * @return boolean 是否完整
	 * @throws IOException
	 */
	public static boolean copyCells(WritableSheet sheet, Range[] mergedCell, int from1Col, int from1Row, int to1Col, int to1Row, int from2Col,
			int from2Row) throws IOException {

		// 复制表格的高和长
		// int tabHigh = to1Row - from1Row + 1;

		try {
			// 制作表格，先合并单元格
			for (int i = 0; i < (to1Row - from1Row + 1); i++) {
				// 选中区域下一行
				sheet.insertRow(from2Row + i);
				sheet.setRowView(from2Row + i, sheet.getRowView(from1Row + i));
				// 对插入行的列进行处理，即单元格
				for (int j = 0; j < (to1Col - from1Col); j++) {
					CellFormat cf = sheet.getWritableCell(from1Col + j, from1Row + i).getCellFormat();
					String content = sheet.getCell(from1Col + j, from1Row + i).getContents();

					if (cf == null) {
						sheet.addCell(new Label(from1Col + j, from2Row + i, content));
					} else {
						sheet.addCell(new Label(from1Col + j, from2Row + i, content, cf));
					}
				}
			}

			// 合并单元格
			for (int i = 0; i < mergedCell.length; i++) {
				int fromRow = mergedCell[i].getTopLeft().getRow();
				int fromCol = mergedCell[i].getTopLeft().getColumn();
				int toRow = mergedCell[i].getBottomRight().getRow();
				int toCol = mergedCell[i].getBottomRight().getColumn();

				// 如果检测到的合并单元格，在复制表格内，则将对应粘贴表的单元格合并。列数=原列数+表高，列数=原列数
				if (fromRow >= from1Row && fromCol >= from1Col && toRow <= to1Row && toCol <= to1Col) {
					// sheet.mergeCells(fromCol, fromRow + from2Row, toCol,
					// toRow + from2Row);
					sheet.mergeCells(fromCol, from2Row, toCol, from2Row);
				}
			}

		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 从sheet中找到值为key的cell，然后设置为新值value
	 * 
	 * @param ws
	 * @param key
	 * @param value
	 */
	private void findCellSetString(WritableSheet ws, String key, String value) {
		Cell cell = ws.findCell(key);
		if (cell != null) {
			Label label = (Label) cell;
			label.setString(value);
		}
	}

	/**
	 * 查看分润明细
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSplitFeeDetail")
	@ResponseBody
	public Map<String, Object> getSplitFeeDetail(@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "dealTypeId") String dealTypeId, @RequestParam(value = "serialnumber") String serialnumber,
			@RequestParam(value = "terminalId") String terminalId, @RequestParam(value = "startValue") String startValue,
			@RequestParam(value = "endValue") String endValue, @RequestParam(value = "queryFlag") String queryFlag, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();

		/*
		 * String agencyOnly = "QT"; String agencyFlag="QT";
		 * if("-1".equals(agencyId)){ agencyId=""; } if (spec ==
		 * null||!"2".equals(spec.getLevelControl())){ agencyOnly = "YES"; }
		 * 
		 * if(null!=spec&&"2".equals(spec.getLevelControl())){ agencyFlag="YES";
		 * }else{
		 * if("2".equals(queryFlag)&&StringUtils.isNotEmpty(agencyId)&&(!agencyIdS
		 * .equals(agencyId))){ loginAgencyId=agencyId; agencyId=""; } }
		 */

		String flag = "";

		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		System.out.println(ConstantUtils.CENTERCODE.equals(agencyIdS));
		System.out.println(StringUtils.isEmpty(agencyId));
		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, agencyId);
		Integer totalCount = splitFeeService.getSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber.trim(), terminalId.trim(),
				startValue, endValue, level, flag);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;

		List<SplitFeeDealBean> list = splitFeeService.getSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber.trim(),
				terminalId.trim(), startValue, endValue, level, flag, start, end);
		// 设置本机构利润 20141106
		for (SplitFeeDealBean bean : list) {
			Double moneyLevel = Double.valueOf(bean.getMoneyLevel());
			Double moneyLevelNext = Double.valueOf(bean.getMoneyLevelNext());
			bean.setProfit(String.format("%.2f", moneyLevel - moneyLevelNext));
		}

		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + agencyId + datestart + dateend + dealTypeId + serialnumber + terminalId + startValue + endValue;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMESPLITFEEDETAIL, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 查看历史分润明细
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getOldSplitFeeDetail")
	@ResponseBody
	public Map<String, Object> getOldSplitFeeDetail(@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "dealTypeId") String dealTypeId, @RequestParam(value = "serialnumber") String serialnumber,
			@RequestParam(value = "terminalId") String terminalId, @RequestParam(value = "startValue") String startValue,
			@RequestParam(value = "endValue") String endValue, @RequestParam(value = "queryFlag") String queryFlag, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();

		/*
		 * String agencyOnly = "QT"; String agencyFlag="QT";
		 * if("-1".equals(agencyId)){ agencyId=""; } if (spec ==
		 * null||!"2".equals(spec.getLevelControl())){ agencyOnly = "YES"; }
		 * 
		 * if(null!=spec&&"2".equals(spec.getLevelControl())){ agencyFlag="YES";
		 * }else{
		 * if("2".equals(queryFlag)&&StringUtils.isNotEmpty(agencyId)&&(!agencyIdS
		 * .equals(agencyId))){ loginAgencyId=agencyId; agencyId=""; } }
		 */

		String flag = "";

		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, agencyId);
		Integer totalCount = splitFeeService.getHisSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber.trim(),
				terminalId.trim(), startValue, endValue, level, flag);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;

		List<SplitFeeDealBean> list = splitFeeService.getHisSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber.trim(),
				terminalId.trim(), startValue, endValue, level, flag, start, end);
		// 设置本机构利润 20141106
		for (SplitFeeDealBean bean : list) {
			Double moneyLevel = Double.valueOf(bean.getMoneyLevel());
			Double moneyLevelNext = Double.valueOf(bean.getMoneyLevelNext());
			bean.setProfit(String.format("%.2f", moneyLevel - moneyLevelNext));
		}

		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + agencyId + datestart + dateend + dealTypeId + serialnumber + terminalId + startValue + endValue;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOLDSPLITFEEDETAIL, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/***
	 * 查看交易明细
	 * 
	 * @param agencyId
	 * @param yearmonth
	 * @param dealTypeId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=splitFeeDetail")
	@ResponseBody
	public Map<String, Object> splitFeeDetail(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "yearmonth") String yearmonth,
			@RequestParam(value = "dealTypeId") String dealTypeId, HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		Integer count = splitFeeService.splitFeeDetailCount(yearmonth, agencyId, dealTypeId);
		int end = (start + rownumber) > count ? count : start + rownumber;
		List<SplitFeeDealBean> list = splitFeeService.splitFeeDetail(yearmonth, agencyId, dealTypeId, start, end);
		return AjaxReturnInfo.setTable(count, list);
	}

	/**
	 * 导出 交易明细，单笔交易在各级的分润情况
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */

	@RequestMapping(params = "method=exportGetSplitFeeDetail")
	// @ResponseBody
	public void exportGetSplitFeeDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
		String agencyId = request.getParameter("agencyId");
		String yearmonth = request.getParameter("yearmonth");
		String dealTypeId = request.getParameter("dealTypeId");
		String serialnumber = request.getParameter("serialnumber");
		String terminalId = request.getParameter("terminalId");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String queryFlag = request.getParameter("queryFlag");

		String flag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		System.out.println(ConstantUtils.CENTERCODE.equals(agencyIdS));
		System.out.println(StringUtils.isEmpty(agencyId));
		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		WritableWorkbook wwb = null;
		try {
			String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
			Integer count = splitFeeService.getSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId, startValue,
					endValue, level, flag);
			List<SplitFeeDealBean> list = splitFeeService.getSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
					startValue, endValue, level, flag, 0, count);
			// 设置本机构利润 20141106
			for (SplitFeeDealBean bean : list) {
				Double moneyLevel = Double.valueOf(bean.getMoneyLevel());
				Double moneyLevelNext = Double.valueOf(bean.getMoneyLevelNext());
				bean.setProfit(String.format("%.2f", moneyLevel - moneyLevelNext));
			}

			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
			File f = new File(urls);
			if (!f.exists()) {
				f.mkdirs();
			}
			String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTSPLITFEEDEALFILENAME;
			File wj = new File(url);
			wwb = Workbook.createWorkbook(wj);
			int sheetNum = 0;
			WritableSheet ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
			for (int i = 0; i < ConstantUtils.SPLITFEEDEALDETAIL.length; i++) {
				ws.addCell(new Label(i, 0, ConstantUtils.SPLITFEEDEALDETAIL[i]));
			}
			for (int i = 0, j = 0; i < list.size(); i++, j++) {
				int col = j + 1;
				ws.addCell(new Label(0, col, list.get(i).getAgencyName() == null ? "0" : list.get(i).getAgencyName().toString()));
				ws.addCell(new Label(1, col, list.get(i).getOnlyCode() == null ? "0" : list.get(i).getOnlyCode().toString()));
				ws.addCell(new Label(2, col, list.get(i).getTerminalCode() == null ? "0" : list.get(i).getTerminalCode().toString()));
				ws.addCell(new Label(3, col, list.get(i).getSerialNumber() == null ? "0" : list.get(i).getSerialNumber().toString()));
				ws.addCell(new Label(4, col, list.get(i).getDealTypeStr() == null ? "0" : list.get(i).getDealTypeStr().toString()));
				ws.addCell(new Label(5, col, list.get(i).getDealData() == null ? "0" : list.get(i).getDealData().toString()));
				ws.addCell(new Label(6, col, list.get(i).getTransAcount() == null ? "0" : list.get(i).getTransAcount().toString()));
				ws.addCell(new Label(7, col, list.get(i).getFeeAmt() == null ? "0" : list.get(i).getFeeAmt().toString()));
				ws.addCell(new Label(8, col, list.get(i).getMoneyLevel() == null ? "0" : list.get(i).getMoneyLevel().toString()));
				ws.addCell(new Label(9, col, list.get(i).getMoneyLevelNext() == null ? "0" : list.get(i).getMoneyLevelNext().toString()));
				// 新增金额差 20141106
				ws.addCell(new Label(10, col, list.get(i).getProfit() == null ? "0" : list.get(i).getProfit().toString()));
				if (i / 60000 == sheetNum + 1) {
					sheetNum++;
					j = 0;
					ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
				}
			}
			// Map<String,Object> map=new HashMap<String,Object>();
			wwb.write();
			wwb.close();

			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMESPLITFEEDETAIL, ConstantUtils.OPERTYPEEXPO, null);

			download(request, response, url, ConstantUtils.EXPORTSPLITFEEDEALFILENAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wwb = null;
		}
		// map.put("url", url);
		// return map;
	}

	/**
	 * 导出历史交易明细，单笔交易在各级的分润情况
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportGetHisSplitFeeDetail")
	// @ResponseBody
	public void exportGetHisSplitFeeDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
		String agencyId = request.getParameter("agencyId");
		String yearmonth = request.getParameter("yearmonth");
		String dealTypeId = request.getParameter("dealTypeId");
		String serialnumber = request.getParameter("serialnumber");
		String terminalId = request.getParameter("terminalId");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String queryFlag = request.getParameter("queryFlag");

		String flag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		System.out.println(ConstantUtils.CENTERCODE.equals(agencyIdS));
		System.out.println(StringUtils.isEmpty(agencyId));
		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		WritableWorkbook wwb = null;
		try {
			String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
			Integer count = splitFeeService.getHisSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId, startValue,
					endValue, level, flag);
			List<SplitFeeDealBean> list = splitFeeService.getHisSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
					startValue, endValue, level, flag, 0, count);
			// 设置本机构利润 20141106
			for (SplitFeeDealBean bean : list) {
				Double moneyLevel = Double.valueOf(bean.getMoneyLevel());
				Double moneyLevelNext = Double.valueOf(bean.getMoneyLevelNext());
				bean.setProfit(String.format("%.2f", moneyLevel - moneyLevelNext));
			}

			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
			File f = new File(urls);
			if (!f.exists()) {
				f.mkdirs();
			}
			String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTHISSPLITFEEDEALFILENAME;
			File wj = new File(url);
			wwb = Workbook.createWorkbook(wj);
			int sheetNum = 0;
			WritableSheet ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
			for (int i = 0; i < ConstantUtils.SPLITFEEDEALDETAIL.length; i++) {
				ws.addCell(new Label(i, 0, ConstantUtils.SPLITFEEDEALDETAIL[i]));
			}
			for (int i = 0, j = 0; i < list.size(); i++, j++) {
				int col = j + 1;
				ws.addCell(new Label(0, col, list.get(i).getAgencyName() == null ? "0" : list.get(i).getAgencyName().toString()));
				ws.addCell(new Label(1, col, list.get(i).getOnlyCode() == null ? "0" : list.get(i).getOnlyCode().toString()));
				ws.addCell(new Label(2, col, list.get(i).getTerminalCode() == null ? "0" : list.get(i).getTerminalCode().toString()));
				ws.addCell(new Label(3, col, list.get(i).getSerialNumber() == null ? "0" : list.get(i).getSerialNumber().toString()));
				ws.addCell(new Label(4, col, list.get(i).getDealTypeStr() == null ? "0" : list.get(i).getDealTypeStr().toString()));
				ws.addCell(new Label(5, col, list.get(i).getDealData() == null ? "0" : list.get(i).getDealData().toString()));
				ws.addCell(new Label(6, col, list.get(i).getTransAcount() == null ? "0" : list.get(i).getTransAcount().toString()));
				ws.addCell(new Label(7, col, list.get(i).getFeeAmt() == null ? "0" : list.get(i).getFeeAmt().toString()));
				ws.addCell(new Label(8, col, list.get(i).getMoneyLevel() == null ? "0" : list.get(i).getMoneyLevel().toString()));
				ws.addCell(new Label(9, col, list.get(i).getMoneyLevelNext() == null ? "0" : list.get(i).getMoneyLevelNext().toString()));
				// 新增金额差 20141106
				ws.addCell(new Label(10, col, list.get(i).getProfit() == null ? "0" : list.get(i).getProfit().toString()));
				if (i / 60000 == sheetNum + 1) {
					sheetNum++;
					j = 0;
					ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
				}
			}
			// Map<String,Object> map=new HashMap<String,Object>();
			wwb.write();
			wwb.close();

			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOLDSPLITFEEDETAIL, ConstantUtils.OPERTYPEEXPO, null);

			download(request, response, url, ConstantUtils.EXPORTHISSPLITFEEDEALFILENAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wwb = null;
		}
		// map.put("url", url);
		// return map;
	}

	/**
	 * 导出某个机构交易明细
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=exportSplitFeeDetail")
	public void exportSplitFeeDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		String agencyId = request.getParameter("agencyId");
		String yearmonth = request.getParameter("yearmonth");
		String dealTypeId = request.getParameter("dealTypeId");
		Integer count = splitFeeService.splitFeeDetailCount(yearmonth, agencyId, dealTypeId);
		List<SplitFeeDealBean> list = splitFeeService.splitFeeDetail(yearmonth, agencyId, dealTypeId, 0, count);
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}
		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTSPLITFEEDEALBYAGENCYFILENAME;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);
		WritableSheet ws = wwb.createSheet("dealDetail", 0);
		for (int i = 0; i < ConstantUtils.SPLITFEEDEALBYAGENCY.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.SPLITFEEDEALBYAGENCY[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int col = i + 1;
			ws.addCell(new Label(0, col, list.get(i).getAgencyName()));
			ws.addCell(new Label(1, col, list.get(i).getSerialNumber()));
			ws.addCell(new Label(2, col, list.get(i).getOnlyCode()));
			ws.addCell(new Label(3, col, list.get(i).getDealTypeStr()));
			ws.addCell(new Label(4, col, list.get(i).getDealData()));
			ws.addCell(new Label(5, col, list.get(i).getTransAcount() == null ? "0" : list.get(i).getTransAcount().toString()));
			ws.addCell(new Label(6, col, list.get(i).getFeeAmt() == null ? "0" : list.get(i).getFeeAmt().toString()));
			ws.addCell(new Label(7, col, list.get(i).getSplitFeeAcount() == null ? "0" : list.get(i).getSplitFeeAcount().toString()));
		}

		wwb.write();
		wwb.close();
		try {
			download(request, response, url, ConstantUtils.EXPORTSPLITFEEDEALBYAGENCYFILENAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 文件导出
	 * 
	 * @param request
	 * @param response
	 * @param path
	 * @param fileName
	 * @throws Exception
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, String path, String fileName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		request.setCharacterEncoding("UTF-8");
		fileName = new String(fileName.getBytes(), "iso_8859_1");
		try {
			File f = new File(path);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			in = new BufferedInputStream(new FileInputStream(f));
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] data = new byte[1024];
			int len = 0;
			while (-1 != (len = in.read(data, 0, data.length))) {
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 查看交易明细
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSplitFeeDetailAll")
	@ResponseBody
	public Map<String, Object> getSplitFeeDetailAll(@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "dealTypeId") String dealTypeId, @RequestParam(value = "serialnumber") String serialnumber,
			@RequestParam(value = "terminalId") String terminalId, @RequestParam(value = "startValue") String startValue,
			@RequestParam(value = "endValue") String endValue, HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = "QT";
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
		Integer totalCount = splitFeeService.getSplitFeeDetailAllCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
				startValue, endValue, level, loginAgencyId);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;

		List<SplitFeeDealBean> list = splitFeeService.getSplitFeeDetailAll(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
				startValue, endValue, level, loginAgencyId, start, end);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 导出 交易明细，单笔交易在各级的分润情况
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportGetSplitFeeDetailAll")
	// @ResponseBody
	public void exportGetSplitFeeDetailAll(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		String agencyId = request.getParameter("agencyId");
		String yearmonth = request.getParameter("yearmonth");
		String dealTypeId = request.getParameter("dealTypeId");
		String serialnumber = request.getParameter("serialnumber");
		String terminalId = request.getParameter("terminalId");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = "QT";
		}
		if (yearmonth == null || "".equals(yearmonth)) {
			yearmonth = "QT";
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}

		String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
		Integer count = splitFeeService.getSplitFeeDetailAllCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId, startValue,
				endValue, level, loginAgencyId);
		List<SplitFeeDealBean> list = splitFeeService.getSplitFeeDetailAll(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
				startValue, endValue, level, loginAgencyId, 0, count);

		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}
		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTSPLITFEEDEALFILENAME;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);
		WritableSheet ws = wwb.createSheet("dealDetail", 0);
		for (int i = 0; i < ConstantUtils.SPLITFEEDEALDETAILALL.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.SPLITFEEDEALDETAILALL[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int col = i + 1;
			ws.addCell(new Label(0, col, list.get(i).getAgencyName() == null ? "0" : list.get(i).getAgencyName().toString()));
			ws.addCell(new Label(1, col, list.get(i).getOnlyCode() == null ? "0" : list.get(i).getOnlyCode().toString()));
			ws.addCell(new Label(2, col, list.get(i).getTerminalCode() == null ? "0" : list.get(i).getTerminalCode().toString()));
			ws.addCell(new Label(3, col, list.get(i).getSerialNumber() == null ? "0" : list.get(i).getSerialNumber().toString()));
			ws.addCell(new Label(4, col, list.get(i).getDealTypeStr() == null ? "0" : list.get(i).getDealTypeStr().toString()));
			ws.addCell(new Label(5, col, list.get(i).getDealData() == null ? "0" : list.get(i).getDealData().toString()));
			ws.addCell(new Label(6, col, list.get(i).getTransAcount() == null ? "0" : list.get(i).getTransAcount().toString()));
			ws.addCell(new Label(7, col, list.get(i).getFeeAmt() == null ? "0" : list.get(i).getFeeAmt().toString()));
			ws.addCell(new Label(8, col, list.get(i).getOneSplitFeeAcount() == null ? "0" : list.get(i).getOneSplitFeeAcount().toString()));
			ws.addCell(new Label(9, col, list.get(i).getTwoSplitFeeAcount() == null ? "0" : list.get(i).getTwoSplitFeeAcount().toString()));
			ws.addCell(new Label(10, col, list.get(i).getThreeSplitFeeAcount() == null ? "0" : list.get(i).getThreeSplitFeeAcount().toString()));
			ws.addCell(new Label(11, col, list.get(i).getFourSplitFeeAcount() == null ? "0" : list.get(i).getFourSplitFeeAcount().toString()));
			ws.addCell(new Label(12, col, list.get(i).getFiveSplitFeeAcount() == null ? "0" : list.get(i).getFiveSplitFeeAcount().toString()));

		}
		// Map<String,Object> map=new HashMap<String,Object>();
		wwb.write();
		wwb.close();
		try {
			download(request, response, url, ConstantUtils.EXPORTSPLITFEEDEALFILENAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// map.put("url", url);
		// return map;
	}

	/**
	 * 人民币金额小写转大写
	 * 
	 * @param input
	 * @return
	 */
	public static String num2Chinese(double fSum) {
		String[] cap_num = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String[] cap_unit = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万" };

		int len;
		String numStr = "";
		String resultStr = "";

		if (fSum > -0.01 && fSum < 0.01) {
			return "零元整";
		}

		numStr = String.format("%15.0f", 100 * fSum);
		len = numStr.length();
		if (len > 15 || len == 0 || fSum < 0) {
			return "";
		}

		int iNum, iZero = 0;
		for (int i = 0; i < len; i++) {
			/* jump the blank */
			if (numStr.charAt(i) > '9' || numStr.charAt(i) < '0')
				continue;

			/* capital num */
			iNum = numStr.charAt(i) - '0';
			if (iNum == 0) {
				iZero++;
			} else {
				if (iZero > 0) {
					resultStr += "零";
				}

				resultStr += cap_num[iNum];
				iZero = 0;
			}

			/* add unit */
			if (iNum != 0 || len - i == 3 || len - i == 11 || ((len - i + 1) % 8 == 0 && iZero < 4)) {
				resultStr += cap_unit[len - i - 1];
			}
		}

		if (numStr.endsWith("00")) {
			resultStr += "整";
		}

		return resultStr;
	}

	/**
	 * 根据机构号和类型分润计算
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=splitFeeSum")
	@ResponseBody
	public AjaxReturnInfo splitFeeSum(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "parentAgencyId") String parentAgencyId,
			HttpServletRequest request) {
		// 获得ip
		String agency_id = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		AjaxReturnInfo ajaxReturnInfo = null;
		if (StringUtils.isEmpty(datestart)) {
			ajaxReturnInfo = AjaxReturnInfo.success("请选择开始时间!");
			return ajaxReturnInfo;
		}
		if (StringUtils.isEmpty(dateend)) {
			ajaxReturnInfo = AjaxReturnInfo.success("请选择结束时间!");
			return ajaxReturnInfo;
		}
		if (StringUtils.isEmpty(agencyId) && StringUtils.isEmpty(parentAgencyId)) {
			ajaxReturnInfo = AjaxReturnInfo.success("请选择机构!");
			return ajaxReturnInfo;
		}
		int result = splitFeeService.insertSplitFeeSum(datestart, dateend, agencyId, parentAgencyId, agency_id, CommonDate.getNowDate());
		ajaxReturnInfo = AjaxReturnInfo.success("申请成功!");
		return ajaxReturnInfo;
	}

	/**
	 * 导出历史交易明细，单笔交易在各级的分润情况
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportGetHisSplitFeeDetailCSV")
	// @ResponseBody
	public void exportGetHisSplitFeeDetailCSV(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
		String agencyId = request.getParameter("agencyId");
		String dealTypeId = request.getParameter("dealTypeId");
		String serialnumber = request.getParameter("serialnumber");
		String terminalId = request.getParameter("terminalId");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String queryFlag = request.getParameter("queryFlag");

		String flag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		BufferedOutputStream out = null;
		StringBuffer sb = null;
		try {
			out = new BufferedOutputStream(response.getOutputStream());
			String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
			Integer count = splitFeeService.getHisSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId, startValue,
					endValue, level, flag);
			List<SplitFeeDealBean> list = splitFeeService.getHisSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
					startValue, endValue, level, flag, 0, count);

			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOLDSPLITFEEDETAIL, ConstantUtils.OPERTYPEEXPO, null);

			sb = new StringBuffer();
			for (int i = 0; i < ConstantUtils.SPLITFEEDEALDETAIL.length; i++) {
				sb.append(ConstantUtils.SPLITFEEDEALDETAIL[i] + ",");
			}
			sb.append("\n");
			for (int i = 0; i < list.size(); i++) {
				sb.append((StringUtils.isNotEmpty(list.get(i).getAgencyName()) ? list.get(i).getAgencyName() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getOnlyCode()) ? list.get(i).getOnlyCode() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getTerminalCode()) ? list.get(i).getTerminalCode() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getSerialNumber()) ? list.get(i).getSerialNumber() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getDealTypeStr()) ? list.get(i).getDealTypeStr() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getDealData()) ? list.get(i).getDealData() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getTransAcount()) ? list.get(i).getTransAcount() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getFeeAmt()) ? list.get(i).getFeeAmt() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getMoneyLevel()) ? list.get(i).getMoneyLevel() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getMoneyLevelNext()) ? list.get(i).getMoneyLevelNext() : "") + ",");
				Double moneyLevel = Double.valueOf(list.get(i).getMoneyLevel());
				Double moneyLevelNext = Double.valueOf(list.get(i).getMoneyLevelNext());
				String profit = String.format("%.2f", moneyLevel - moneyLevelNext);
				sb.append(profit + ",");
				sb.append("\n");
			}
			String filename = new String(ConstantUtils.EXPORTHISSPLITFEEDEALFILENAME.substring(0,
					ConstantUtils.EXPORTHISSPLITFEEDEALFILENAME.indexOf(".")).getBytes("UTF-8"), "ISO-8859-1");
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEDEALDET, ConstantUtils.OPERTYPEEXPO, null);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");

			byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			out.write(bom);
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setCharacterEncoding("UTF-8");
			out.write("<script> alert('导出异常');</script>".getBytes());
			log.error("导出异常" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
			out = null;
			sb = null;
		}
	}

	/**
	 * 导出 交易明细，单笔交易在各级的分润情况
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */

	@RequestMapping(params = "method=exportGetSplitFeeDetailCVS")
	// @ResponseBody
	public void exportGetSplitFeeDetailCVS(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException,
			WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String parentAgencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
		String agencyId = request.getParameter("agencyId");
		String dealTypeId = request.getParameter("dealTypeId");
		String serialnumber = request.getParameter("serialnumber");
		String terminalId = request.getParameter("terminalId");
		String startValue = request.getParameter("startValue");
		String endValue = request.getParameter("endValue");
		String loginAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		// 特别系统处理，如果为特别系统则只能看到直属下级机构，直属下级以下的所有交易视为直属下级 20141106
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String queryFlag = request.getParameter("queryFlag");

		String flag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		SpecSystemBean split = null;
		try {
			split = systemManageService.checkSplit(systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != spec) {
			// 指定机构
			if (agencyIdS.equals(agencyId)) {
				flag = "1";
			}
			if ("1".equals(spec.getLevelControl()) && ConstantUtils.CENTERCODE.equals(parentAgencyId)) {
				// 归属不归并
				flag = "2";
			} else {
				// 归属归并
				flag = "3";
			}
		} else {
			// 指定机构
			if ("1".equals(queryFlag)) {
				flag = "1";
			}
			// 归属不归并
			if ("2".equals(queryFlag)) {
				flag = "3";
			}
			if (ConstantUtils.CENTERCODE.equals(agencyIdS)) {
				if ("1".equals(queryFlag)) {
					flag = "1";
				}
				// 归属不归并
				if ("2".equals(queryFlag)) {
					flag = "2";
				}
			}
		}
		// 归属归并
		if (null != split && "1".equals(split.getSplitcontrol())) {
			flag = "3";
		}

		System.out.println(ConstantUtils.CENTERCODE.equals(agencyIdS));
		System.out.println(StringUtils.isEmpty(agencyId));
		if (ConstantUtils.CENTERCODE.equals(agencyIdS) && StringUtils.isEmpty(agencyId)) {
			flag = "2";
		}

		if (agencyId == null || "".equals(agencyId) || "-1".equals(agencyId)) {
			agencyId = agencyIdS;
		}
		if (datestart == null || "".equals(datestart)) {
			datestart = CommonDate.getNowDate();
		}
		if (dateend == null || "".equals(dateend)) {
			dateend = CommonDate.getNowDate();
		}
		if (dealTypeId == null || "".equals(dealTypeId) || "-1".equals(dealTypeId)) {
			dealTypeId = "QT";
		}
		if (serialnumber == null || "".equals(serialnumber)) {
			serialnumber = "QT";
		}
		if (terminalId == null || "".equals(terminalId)) {
			terminalId = "QT";
		}
		if (startValue == null || "".equals(startValue)) {
			startValue = "QT";
		}
		if (endValue == null || "".equals(endValue)) {
			endValue = "QT";
		}
		StringBuffer sb = new StringBuffer();
		BufferedOutputStream out = null;
		try {
			String level = agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, loginAgencyId);
			Integer count = splitFeeService.getSplitFeeDetailCount(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId, startValue,
					endValue, level, flag);
			List<SplitFeeDealBean> list = splitFeeService.getSplitFeeDetail(agencyId, datestart, dateend, dealTypeId, serialnumber, terminalId,
					startValue, endValue, level, flag, 0, count);

			for (int i = 0; i < ConstantUtils.SPLITFEEDEALDETAIL.length; i++) {
				sb.append(ConstantUtils.SPLITFEEDEALDETAIL[i] + ",");
			}
			sb.append("\n");
			for (int i = 0; i < list.size(); i++) {
				sb.append((StringUtils.isNotEmpty(list.get(i).getAgencyName()) ? list.get(i).getAgencyName() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getOnlyCode()) ? list.get(i).getOnlyCode() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getTerminalCode()) ? list.get(i).getTerminalCode() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getSerialNumber()) ? list.get(i).getSerialNumber() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getDealTypeStr()) ? list.get(i).getDealTypeStr() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getDealData()) ? list.get(i).getDealData() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getTransAcount()) ? list.get(i).getTransAcount() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getFeeAmt()) ? list.get(i).getFeeAmt() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getMoneyLevel()) ? list.get(i).getMoneyLevel() : "") + ",");
				sb.append((StringUtils.isNotEmpty(list.get(i).getMoneyLevelNext()) ? list.get(i).getMoneyLevelNext() : "") + ",");
				Double moneyLevel = Double.valueOf(list.get(i).getMoneyLevel());
				Double moneyLevelNext = Double.valueOf(list.get(i).getMoneyLevelNext());
				String profit = String.format("%.2f", moneyLevel - moneyLevelNext);
				sb.append(profit + ",");
				sb.append("\n");
			}
			String filename = new String(ConstantUtils.EXPORTSPLITFEEDEALFILENAME.substring(0,
					ConstantUtils.EXPORTSPLITFEEDEALFILENAME.indexOf(".")).getBytes("UTF-8"), "ISO-8859-1");
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEDEALDET, ConstantUtils.OPERTYPEEXPO, null);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			out.write(bom);
			out.write(sb.toString().getBytes("UTF-8"));

			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMESPLITFEEDETAIL, ConstantUtils.OPERTYPEEXPO, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setCharacterEncoding("UTF-8");
			out.write("<script> alert('导出异常');</script>".getBytes());
			log.error("导出异常" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
			out = null;
			sb = null;
		}
	}

}
