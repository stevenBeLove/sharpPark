package com.compass.dealmanager.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.authority.service.AuthorityService;
import com.compass.dealmanager.model.TerminalDealBean;
import com.compass.dealmanager.service.TerminalDealService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.CommonParam;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * 类名: 终端交易管理 TerminalDealController <br/>
 * 创建者: zhang jun <br/>
 * 添加时间: 2016年4月12日 下午1:33:51 <br/>
 */
@Controller
@RequestMapping("/terminaldeal/terminaldeal.do")
public class TerminalDealController {
	@Autowired
	@Qualifier("terminalDealService")
	public TerminalDealService terminalDealService;
	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;
	@Autowired
	@Qualifier("authorityService")
	private AuthorityService authorityService;

	// 系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;

	private final Log log = LogFactory.getLog(getClass());

	/**
	 * 方法名： getActivateStandard.<br/>
	 * 适用条件:参数配置激活标准下拉列表.<br/>
	 * 方法作用:获取激活标准下拉框.<br/>
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:39:27.<br/>
	 * 参数者异常：@param request 参数者异常：@return .<br/>
	 * 返回值： @return 返回结果：List<Map<String,Object>>.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	@RequestMapping(params = "method=getActivateStandard")
	@ResponseBody
	public List<Map<String, Object>> getActivateStandard(HttpServletRequest request) {
		String flag = request.getParameter("flag");
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		String paramvalue = authorityService.getParamObject("ACTIVATESTANDARD");
		String array[] = paramvalue.split("\\|");
		Map<String, Object> map = null;
		// 如果参数为1 添加 ”请选择记录“
		if ("1".equals(flag)) {
			map = new HashMap<String, Object>();
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择激活标准");
			map.put("selected", true);
			listmap.add(map);
			map = null;
		}
		for (int i = 0; i < array.length; i++) {
			String idtext[] = array[i].split(",");
			map = new HashMap<String, Object>();
			map.put("id", idtext[0]);
			map.put("text", idtext[1]);
			listmap.add(map);
			idtext = null;
			map = null;
		}
		return listmap;
	}

	/**
	 * 
	 * 方法名： getViewTerminalDeal.<br/>
	 * 方法作用:获取终端交易查询列表.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:37:08.<br/>
	 * 
	 * @param startDate
	 * @param endDate
	 * @param activateStandard
	 * @param dealTypeId
	 * @param startCode
	 * @param endCode
	 * @param agencyId
	 * @param agencyCode
	 * @param flagStatus
	 * @param request
	 * @return .<br/>
	 *         返回值： @return 返回结果：Map<String,Object>.<br/>
	 *         其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	@RequestMapping(params = { "method=getViewTerminalDeal" })
	@ResponseBody
	public Map<String, Object> getViewTerminalDeal(TerminalDealBean terminalDeal, HttpServletRequest request) {
		String curAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.AGENCYID);
		if (StringUtils.isNotEmpty(terminalDeal.getStartCode()) && terminalDeal.getStartCode().trim().length() < 16)
			terminalDeal.setStartCode(terminalDeal.getStartCode().trim() + "0");
		if (StringUtils.isNotEmpty(terminalDeal.getEndCode()) && terminalDeal.getEndCode().trim().length() < 16) {
			terminalDeal.setEndCode(terminalDeal.getEndCode().trim() + "9");
		}
		terminalDeal.setCurAgencyId(curAgencyId);
		if (curAgencyId.equals(terminalDeal.getAgencyId())) {
			terminalDeal.setAgencyId("");
		}
		// 查询记录条数
		Integer totalCount = terminalDealService.getViewTerminalDealCount(terminalDeal);
		List<TerminalDealBean> listModel = new ArrayList<TerminalDealBean>();
		// 查询
		if (0 < totalCount) {
			terminalDeal.setStart((terminalDeal.getPagenumber() - 1) * terminalDeal.getRownumber());
			terminalDeal.setEnd(terminalDeal.getStart() + terminalDeal.getRownumber() > totalCount ? totalCount : terminalDeal.getStart()
					+ terminalDeal.getRownumber());
			listModel = this.terminalDealService.getViewTermnalDealList(terminalDeal);
		}
		return AjaxReturnInfo.setTable(listModel == null ? 0 : totalCount, listModel);
	}

	/**
	 * 
	 * 方法名： batchFlagOPtTerminal.<br/>
	 * 方法作用:批量标记终端,批量取消标记终端记录.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:41:40.<br/>
	 * 参数者异常：@param idStr 参数者异常：@param optFlag 参数者异常：@param request
	 * 参数者异常：@return .<br/>
	 * 返回值： @return 返回结果：AjaxReturnInfo.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	@RequestMapping(params = { "method=batchFlagOPtTerminal" })
	@ResponseBody
	public AjaxReturnInfo batchFlagOPtTerminal(TerminalDealBean terminaldeal, HttpServletRequest request) {
		String flagOperator = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.USERID);
		String curAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.AGENCYID);
		terminaldeal.setFlagOperator(flagOperator);
		String ipAddress = request.getSession().getAttribute("ipAddress").toString();
		String agencyIdS = request.getSession().getAttribute("agencyId").toString();
		String userIdS = request.getSession().getAttribute("userId").toString();
		AjaxReturnInfo ajaxinfo = null;
		String operateDetail = null;
		if ("0".equals(terminaldeal.getOptFlag())) {
			try {
				terminaldeal.setCurAgencyId(curAgencyId);
				if (curAgencyId.equals(terminaldeal.getAgencyId())) {
					terminaldeal.setAgencyId("");
				}
				int result = this.terminalDealService.insertPayTermJnls(terminaldeal);
				if (0 < result) {
					operateDetail = "批量标记成功！";
					ajaxinfo = AjaxReturnInfo.success(operateDetail);
				} else {
					operateDetail = "批量标记失败！";
					ajaxinfo = AjaxReturnInfo.failed(operateDetail);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				operateDetail = "批量标记失败！";
				ajaxinfo = AjaxReturnInfo.failed(operateDetail);
			}
		}
		if ("1".equals(terminaldeal.getOptFlag())) {
			try {
				int result = this.terminalDealService.delPayTermJnls(terminaldeal);
				operateDetail = "批量取消标记成功！";
				ajaxinfo = AjaxReturnInfo.success(operateDetail);
				if (0 < result) {
					operateDetail = "批量取消标记成功！";
					ajaxinfo = AjaxReturnInfo.success(operateDetail);
				} else {
					operateDetail = "批量取消标记失败！";
					ajaxinfo = AjaxReturnInfo.failed(operateDetail);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				operateDetail = "批量取消标记失败!";
				ajaxinfo = AjaxReturnInfo.failed(operateDetail);
			}
		}
		this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMETERMINALDEAL, ConstantUtils.OPERTYPEUPD, operateDetail);
		operateDetail += terminaldeal.getTerminalCodeStr();
		log.info(operateDetail);
		return ajaxinfo;
	}

	/**
	 * 
	 * 方法名： batchFlagDistribute.<br/>
	 * 方法作用:批量将已标记的终端标记分配.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:44:02.<br/>
	 * 参数者异常：@param idStr 参数者异常：@param request 参数者异常：@return .<br/>
	 * 返回值： @return 返回结果：AjaxReturnInfo.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	@RequestMapping(params = { "method=batchFlagDistribute" })
	@ResponseBody
	public AjaxReturnInfo batchFlagDistribute(TerminalDealBean terminaldeal, HttpServletRequest request) {
		String flagOperator = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.USERID);
		terminaldeal.setFlagOperator(flagOperator);
		String ipAddress = request.getSession().getAttribute("ipAddress").toString();
		String agencyIdS = request.getSession().getAttribute("agencyId").toString();
		String userIdS = request.getSession().getAttribute("userId").toString();
		String operateDetail = null;
		AjaxReturnInfo ajaxinfo = null;
		try {
			terminaldeal.setFlagStatus("2");
			int result = this.terminalDealService.updateFlagTerminalDeal(terminaldeal);
			if (result > 0) {
				operateDetail = "批量标记已分配成功";
				ajaxinfo = AjaxReturnInfo.success(operateDetail);
			} else {
				operateDetail = "批量标记已分配失败";
				ajaxinfo = AjaxReturnInfo.failed(operateDetail);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			operateDetail = "批量标记已分配失败";
			ajaxinfo = AjaxReturnInfo.failed(operateDetail);
			log.error(operateDetail);
		}
		this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMETERMINALDEAL, ConstantUtils.OPERTYPEUPD, operateDetail);
		log.info(operateDetail);
		return ajaxinfo;
	}

	/**
	 * 
	 * 方法名： exportDealDetailSCV.<br/>
	 * 方法作用:导出 .<br/>
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月26日.<br/>
	 * 创建时间：下午4:05:37.<br/>
	 * 参数者异常：@param terminalDeal 参数者异常：@param request 参数者异常：@param response
	 * 参数者异常：@throws IOException 参数者异常：@throws RowsExceededException
	 * 参数者异常：@throws WriteException .<br/>
	 * 返回值： @return 返回结果：void.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	@RequestMapping(params = "method=exportDealDetailCSV")
	public void exportDealDetailSCV(TerminalDealBean terminalDeal, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		String curAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.AGENCYID);
		BufferedOutputStream out = null;
		try {
			if (StringUtils.isNotEmpty(terminalDeal.getStartCode()) && terminalDeal.getStartCode().trim().length() < 16)
				terminalDeal.setStartCode(terminalDeal.getStartCode().trim() + "0");
			if (StringUtils.isNotEmpty(terminalDeal.getEndCode()) && terminalDeal.getEndCode().trim().length() < 16) {
				terminalDeal.setEndCode(terminalDeal.getEndCode().trim() + "9");
			}
			terminalDeal.setCurAgencyId(curAgencyId);
			if (curAgencyId.equals(terminalDeal.getAgencyId())) {
				terminalDeal.setAgencyId("");
			}
			// 查询记录条数
			Integer totalCount = terminalDealService.getViewTerminalDealCount(terminalDeal);
			List<TerminalDealBean> listModel = new ArrayList<TerminalDealBean>();
			String allowedMaxNum = authorityService.getParamObject("ALLOWEDMAXNUM");
			int maxRow = StringUtils.isNotEmpty(allowedMaxNum) ? Integer.valueOf(allowedMaxNum) : 200000;
			if (totalCount > maxRow) {
				log.info(totalCount + "超出允许的最大数据量");
				throw new Exception("超出允许的最大数据量");
			}
			terminalDeal.setStart(0);
			terminalDeal.setEnd(totalCount);
			listModel = this.terminalDealService.getViewTermnalDealList(terminalDeal);

			String CurrentSystemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		    /////// 创新金融平台 ('00800000','00640000','00110000','00530000','00630000')     来源系统TKMY,RJDZ,AIYA
			String paramvalue = authorityService.getParamObject("TemnldealExportCusIdSys");
			String[] title = null;
			if (paramvalue == null || paramvalue == "") {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "客户编码", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
			} else {
				String[] bArray = paramvalue.split(","); // String array[] =
															// paramvalue.split(",");
				Boolean isNoIn = CommonParam.isStrInArray(CurrentSystemId, bArray);
				if (isNoIn) {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "客户编码", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
				} else {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
				}
			}
			    
			  
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < title.length; i++) {
				sb.append(title[i] + ",");
			}
			sb.append("\n");
			for (int i = 0; i < listModel.size(); i++) {
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getStartDate()) ? listModel.get(i).getStartDate() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getEndDate()) ? listModel.get(i).getEndDate() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getAgencyName()) ? listModel.get(i).getAgencyName() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getTerminalCode()) ? listModel.get(i).getTerminalCode() : "") + ",");
				
				if (paramvalue == null || paramvalue == "") {
					sb.append((StringUtils.isNotEmpty(listModel.get(i).getCustomerId()) ? listModel.get(i).getCustomerId() : "") + ",");
				} else {
					String[] bArray = paramvalue.split(",");
					Boolean isNoIn = CommonParam.isStrInArray(CurrentSystemId, bArray);
					if (isNoIn) {
						sb.append((StringUtils.isNotEmpty(listModel.get(i).getCustomerId()) ? listModel.get(i).getCustomerId() : "") + ",");
					}
				}
				
				sb.append((listModel.get(i).getTransAmount() != null ? listModel.get(i).getTransAmount() : "") + ",");
				if ("1".equals(listModel.get(i).getFlag())) {
					sb.append(",");
				} else {
					sb.append("用户重复,");
				}
				if ("0".equals(listModel.get(i).getFlagStatus())) {
					sb.append("未标记,");
				} else if ("1".equals(listModel.get(i).getFlagStatus())) {
					sb.append("已标记,");
				} else if ("2".equals(listModel.get(i).getFlagStatus())) {
					sb.append("已分配,");
				} else {
					sb.append("未标记,");
				}
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getFlagOperator()) ? listModel.get(i).getFlagOperator() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getFlagOperTime()) ? listModel.get(i).getFlagOperTime() : "") + ",");
				sb.append("\n");
			}
			response.setHeader("Content-Disposition", "attachment; filename=TERDEAL_" + CommonDate.currentStr() + ".csv");
			response.setCharacterEncoding("GBK");
			response.setContentType("application/vnd.ms-excel");
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			out.write(bom);
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			response.setCharacterEncoding("GBK");
			out = new BufferedOutputStream(response.getOutputStream());
			out.write("<script> alert('超出允许的最大数据量20万');</script>".getBytes());
			log.error("交易明细导出异常" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			out = null;
		}
	}

	

	
	
	
	
	@RequestMapping(params = { "method=getOldTerminalDeal" })
	@ResponseBody
	public Map<String, Object> getOldTerminalDeal(TerminalDealBean terminalDeal, HttpServletRequest request) {
		String curAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.AGENCYID);
		if (StringUtils.isNotEmpty(terminalDeal.getStartCode()) && terminalDeal.getStartCode().trim().length() < 16)
			terminalDeal.setStartCode(terminalDeal.getStartCode().trim() + "0");
		if (StringUtils.isNotEmpty(terminalDeal.getEndCode()) && terminalDeal.getEndCode().trim().length() < 16) {
			terminalDeal.setEndCode(terminalDeal.getEndCode().trim() + "9");
		}
		terminalDeal.setCurAgencyId(curAgencyId);
		if (curAgencyId.equals(terminalDeal.getAgencyId())) {
			terminalDeal.setAgencyId("");
		}
		// 查询记录条数
		Integer totalCount = terminalDealService.getOldTerminalDealCount(terminalDeal);
		List<TerminalDealBean> listModel = new ArrayList<TerminalDealBean>();
		// 查询
		if (0 < totalCount) {
			terminalDeal.setStart((terminalDeal.getPagenumber() - 1) * terminalDeal.getRownumber());
			terminalDeal.setEnd(terminalDeal.getStart() + terminalDeal.getRownumber() > totalCount ? totalCount : terminalDeal.getStart()
					+ terminalDeal.getRownumber());
			listModel = this.terminalDealService.getOldTermnalDealList(terminalDeal);
		}
		return AjaxReturnInfo.setTable(listModel == null ? 0 : totalCount, listModel);
	}

	
	
	@RequestMapping(params = "method=exportOldDealDetailCSV")
	public void exportOldDealDetailSCV(TerminalDealBean terminalDeal, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		String curAgencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(
				ConstantUtils.AGENCYID);
		BufferedOutputStream out = null;
		try {
			if (StringUtils.isNotEmpty(terminalDeal.getStartCode()) && terminalDeal.getStartCode().trim().length() < 16)
				terminalDeal.setStartCode(terminalDeal.getStartCode().trim() + "0");
			if (StringUtils.isNotEmpty(terminalDeal.getEndCode()) && terminalDeal.getEndCode().trim().length() < 16) {
				terminalDeal.setEndCode(terminalDeal.getEndCode().trim() + "9");
			}
			terminalDeal.setCurAgencyId(curAgencyId);
			if (curAgencyId.equals(terminalDeal.getAgencyId())) {
				terminalDeal.setAgencyId("");
			}
			// 查询记录条数
			Integer totalCount = terminalDealService.getOldTerminalDealCount(terminalDeal);
			List<TerminalDealBean> listModel = new ArrayList<TerminalDealBean>();
			String allowedMaxNum = authorityService.getParamObject("ALLOWEDMAXNUM");
			int maxRow = StringUtils.isNotEmpty(allowedMaxNum) ? Integer.valueOf(allowedMaxNum) : 200000;
			if (totalCount > maxRow) {
				log.info(totalCount + "超出允许的最大数据量");
				throw new Exception("超出允许的最大数据量");
			}
			terminalDeal.setStart(0);
			terminalDeal.setEnd(totalCount);
			listModel = this.terminalDealService.getOldTermnalDealList(terminalDeal);
			
			String CurrentSystemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
			String paramvalue = authorityService.getParamObject("TemnldealExportCusIdSys");
			String[] title = null;
			if (paramvalue == null || paramvalue == "") {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "客户编码", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
			} else {
				String[] bArray = paramvalue.split(","); // String array[] = paramvalue.split(",");
				Boolean isNoIn = CommonParam.isStrInArray(CurrentSystemId, bArray);
				if (isNoIn) {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "客户编码", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
				} else {
					title = new String[] { "开始时间", "截止时间", "机构名称", "终端编号", "交易金额", "不可选原因", "标记状态", "标记操作人", "标记时间" };
				}
			}

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < title.length; i++) {
				sb.append(title[i] + ",");
			}
			sb.append("\n");
			for (int i = 0; i < listModel.size(); i++) {
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getStartDate()) ? listModel.get(i).getStartDate() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getEndDate()) ? listModel.get(i).getEndDate() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getAgencyName()) ? listModel.get(i).getAgencyName() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getTerminalCode()) ? listModel.get(i).getTerminalCode() : "") + ",");
				
				if (paramvalue == null || paramvalue == "") {
					sb.append((StringUtils.isNotEmpty(listModel.get(i).getCustomerId()) ? listModel.get(i).getCustomerId() : "") + ",");
				} else {
					String[] bArray = paramvalue.split(",");
					Boolean isNoIn = CommonParam.isStrInArray(CurrentSystemId, bArray);
					if (isNoIn) {
						sb.append((StringUtils.isNotEmpty(listModel.get(i).getCustomerId()) ? listModel.get(i).getCustomerId() : "") + ",");
					}
				}
				
				sb.append((listModel.get(i).getTransAmount() != null ? listModel.get(i).getTransAmount() : "") + ",");
				if ("1".equals(listModel.get(i).getFlag())) {
					sb.append(",");
				} else {
					sb.append("用户重复,");
				}
				if ("0".equals(listModel.get(i).getFlagStatus())) {
					sb.append("未标记,");
				} else if ("1".equals(listModel.get(i).getFlagStatus())) {
					sb.append("已标记,");
				} else if ("2".equals(listModel.get(i).getFlagStatus())) {
					sb.append("已分配,");
				} else {
					sb.append("未标记,");
				}
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getFlagOperator()) ? listModel.get(i).getFlagOperator() : "") + ",");
				sb.append((StringUtils.isNotEmpty(listModel.get(i).getFlagOperTime()) ? listModel.get(i).getFlagOperTime() : "") + ",");
				sb.append("\n");
			}
			response.setHeader("Content-Disposition", "attachment; filename=TERDEAL_" + CommonDate.currentStr() + ".csv");
			response.setCharacterEncoding("GBK");
			response.setContentType("application/vnd.ms-excel");
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			out.write(bom);
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			response.setCharacterEncoding("GBK");
			out = new BufferedOutputStream(response.getOutputStream());
			out.write("<script> alert('超出允许的最大数据量20万');</script>".getBytes());
			log.error("交易明细导出异常" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			out = null;
		}
	}

}
