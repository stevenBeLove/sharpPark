package com.compass.dealmanager.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.compass.agency.model.SpecSystemBean;
import com.compass.authority.service.AuthorityService;
import com.compass.dealmanager.model.DealBean;
import com.compass.dealmanager.model.DealCountBean;
import com.compass.dealmanager.model.DealDeductBean;
import com.compass.dealmanager.model.DeductTypeBean;
import com.compass.dealmanager.model.OtherDeductBean;
import com.compass.dealmanager.model.ProfitBean;
import com.compass.dealmanager.model.RealDeductBean;
import com.compass.dealmanager.service.DealManageService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.terminalmanage.service.TerminalManageService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.TxtEncodingUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.google.gson.Gson;

/**
 * @author gaoyang 交易管理Controller
 */

@Controller
@RequestMapping("/deal/deal.do")
public class DealManageController {

	@Autowired
	@Qualifier("dealManageService")
	private DealManageService dealManageService;
	@Autowired
	@Qualifier("terminalManageService")
	public TerminalManageService terminalManageService;
	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;

	// 系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;

	@Autowired
	@Qualifier("authorityService")
	private AuthorityService authorityService;

	private final Log log = LogFactory.getLog(getClass());

	/**
	 * 获得交易列表
	 */
	@RequestMapping(params = "method=getDealList")
	@ResponseBody
	public Map<String, Object> getDealList(HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();

		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		Integer totalCount = dealManageService.getDealListCount(systemId);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemId", systemId);
		map.put("start", start);
		map.put("end", end);
		List<DealBean> list = dealManageService.getDealList(map);
		map = null;
		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 获取文件的title(文件格式：excle、txt)
	 * 
	 * @param multipartRequest
	 * @param response
	 * @param req
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "method=upload")
	@ResponseBody
	public void upload(MultipartHttpServletRequest multipartRequest, HttpServletResponse response, HttpServletRequest req) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		response.setContentType("text/html;charset=UTF-8");
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("upload_file");
		String dealrule = req.getParameter("dealrule");
		Gson gosn = null;
		File ff = null;
		try {
			PrintWriter print = response.getWriter();
			InputStream in = file.getInputStream();
			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String realPath = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + file.getName();
			req.getSession().setAttribute("file", realPath);

			File rootFile = new File(realPath);
			if (!rootFile.exists()) {
				rootFile.mkdirs();
			}
			ff = new File(realPath, file.getOriginalFilename());
			OutputStream out = new FileOutputStream(ff);
			FileCopyUtils.copy(in, out);
			req.getSession().setAttribute("upload_file", ff);
			String fileName = file.getOriginalFilename();
			String pathFileName = realPath + "/" + fileName;
			if (fileName.endsWith("txt")) {
				if (ff.isFile() && ff.exists()) {
					req.getSession().setAttribute("namePath", pathFileName);
					String encodinng = TxtEncodingUtils.codeString(pathFileName);
					InputStreamReader read = new InputStreamReader(new FileInputStream(ff), encodinng);// 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("id", ConstantUtils.COMBOXONEID);
					map1.put("text", "---请选择---");
					map1.put("selected", true);
					list.add(map1);
					map1 = null;
					String lineTxt = null;
					int flag = 0;
					while ((lineTxt = bufferedReader.readLine()) != null) {
						String temp[] = lineTxt.split(dealrule);
						if (flag == 0) {// 只获取第一行数据
							for (int i = 0; i < temp.length; i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", i);
								map.put("text", temp[i]);
								list.add(map);
								map = null;
							}
						}
						flag++;
					}
					read.close();
					bufferedReader.close();
					read = null;
				}
			} else {
				Workbook work = Workbook.getWorkbook(new FileInputStream(ff));
				Sheet sheet = work.getSheet(0);
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("id", ConstantUtils.COMBOXONEID);
				map1.put("text", "---请选择---");
				map1.put("selected", true);
				list.add(map1);
				map1 = null;
				for (int i = 0; i < sheet.getRows(); i++) {
					for (int j = 0; j < sheet.getColumns(); j++) {
						Map<String, Object> map = null;
						try {
							map = new HashMap<String, Object>();
							map.put("id", j);
							map.put("text", sheet.getCell(j, i).getContents());
							list.add(map);
						} catch (Exception e) {
							throw e;
						} finally {
							map = null;
						}
					}
					break;
				}
			}

			gosn = new Gson();
			print.write(gosn.toJson(list));
			out.close();
			in.close();
			print.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			list = null;
			gosn = null;
			ff = null;
		}
	}

	/**
	 * 将文件数据导入到数据库(文件格式：excle,txt)
	 * 
	 * @param terminalTypeId
	 * @param terminalCode
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws BiffException
	 */
	@RequestMapping(params = "method=saveExcelData")
	@ResponseBody
	public String save(@RequestParam(value = "sysSource") String sysSource, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "dealrule") String dealrule, @RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "transacount") String transacount,
			@RequestParam(value = "terminalId") String terminalId, @RequestParam(value = "deal_data") String deal_data,
			@RequestParam(value = "deal_time") String deal_time, @RequestParam(value = "deal_status") String deal_status,
			@RequestParam(value = "dealtype_id") String dealtype_id, @RequestParam(value = "dealdesc") String dealdesc,
			@RequestParam(value = "dealrebackcode") String dealrebackcode, @RequestParam(value = "charge") String charge,
			@RequestParam(value = "transcost") String transcost, @RequestParam(value = "bankcardNumber") String bankcardNumber,
			HttpServletRequest req, HttpServletResponse res) throws BiffException, FileNotFoundException, IOException {

		Integer dealidD = Integer.valueOf(dealId);
		Integer merchantCodeD = Integer.valueOf(merchantCode);
		Integer serialNumberD = Integer.valueOf(serialNumber);
		Integer transacountD = Integer.valueOf(transacount);
		Integer terminalIdD = Integer.valueOf(terminalId);
		Integer deal_dataD = Integer.valueOf(deal_data);
		Integer deal_timeD = Integer.valueOf(deal_time);
		Integer deal_statusD = Integer.valueOf(deal_status);
		Integer dealtype_idD = Integer.valueOf(dealtype_id);
		Integer dealdescD = Integer.valueOf(dealdesc);
		Integer dealrebackcodeD = Integer.valueOf(dealrebackcode);
		Integer chargeD = Integer.valueOf(charge);
		Integer transcostD = Integer.valueOf(transcost);
		Integer bankcardNumberD = Integer.valueOf(bankcardNumber);

		File file = (File) req.getSession().getAttribute("upload_file");
		// AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.failed("导入失败");
		int fail = 0;
		int flag = 0;
		int k = 0;
		String fileName = file.getName();
		String failTxt = "";
		String dealFailDesc = "";
		String createDt = CommonDate.getDate();
		if (fileName.endsWith("txt")) {
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				String pathFileName = (String) req.getSession().getAttribute("namePath");
				String encodinng = "utf-8";
				try {
					encodinng = TxtEncodingUtils.codeString(pathFileName);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encodinng);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				int index = 0;
				while ((lineTxt = bufferedReader.readLine()) != null && !"".equals(lineTxt = bufferedReader.readLine())) {
					String temp[] = lineTxt.split(dealrule);
					if (index != 0) { // 获取除了第一行数据以外的数据
						DealBean dealBean = SetDealBean(sysSource, temp[dealidD], temp[serialNumberD], temp[deal_dataD], temp[deal_timeD],
								temp[merchantCodeD], temp[terminalIdD], temp[transacountD], temp[deal_statusD], temp[dealtype_idD], temp[dealdescD],
								temp[dealrebackcodeD], temp[chargeD], temp[transcostD], temp[bankcardNumberD], createDt);
						try {
							k = dealManageService.addDeal(dealBean);
							if (k > 0) {
								flag++;
							}
						} catch (Exception e) {
							fail++;
							if (e.getCause() == null) {
								dealFailDesc = e.getMessage();
							} else {
								dealFailDesc = e.getCause().getCause() == null ? e.getMessage() : e.getCause().getCause().toString();
							}
							failTxt = sysSource + "," + temp[dealidD] + "," + temp[merchantCodeD] + "," + temp[serialNumberD] + ","
									+ temp[transacountD] + "," + temp[terminalIdD] + "," + temp[deal_timeD] + "," + temp[dealidD] + ","
									+ temp[deal_statusD] + "," + temp[dealtype_idD] + "," + temp[dealdescD] + "," + temp[dealrebackcodeD] + ","
									+ temp[chargeD] + "," + temp[transcostD] + "," + bankcardNumber + "," + createDt;
							String dealFailId = String.valueOf(dealManageService.getDealFailId());
							dealManageService.addDealFail(dealFailId, failTxt, dealFailDesc, createDt);
						} finally {
							read.close();
							bufferedReader.close();
						}
					}
					index++;
				}
			}
		} else {

			Workbook work = Workbook.getWorkbook(new FileInputStream(file));
			Sheet sheet = work.getSheet(0);
			for (int i = 1; i < sheet.getRows(); i++) {
				if (sheet.getCell(serialNumberD, i).getContents() != null && !"".equals(sheet.getCell(serialNumberD, i).getContents())) {
					DealBean dealBean = SetDealBean(sysSource, sheet.getCell(dealidD, i).getContents(),
							sheet.getCell(serialNumberD, i).getContents(), sheet.getCell(deal_dataD, i).getContents(), sheet.getCell(deal_timeD, i)
									.getContents(), sheet.getCell(merchantCodeD, i).getContents(), sheet.getCell(terminalIdD, i).getContents(), sheet
									.getCell(transacountD, i).getContents(), sheet.getCell(deal_statusD, i).getContents(),
							sheet.getCell(dealtype_idD, i).getContents(), sheet.getCell(dealdescD, i).getContents(), sheet
									.getCell(dealrebackcodeD, i).getContents(), sheet.getCell(chargeD, i).getContents(), sheet.getCell(transcostD, i)
									.getContents(), sheet.getCell(bankcardNumberD, i).getContents(), createDt);
					try {
						k = dealManageService.addDeal(dealBean);
						if (k > 0) {
							flag++;
						}
					} catch (Exception e) {
						fail++;
						if (e.getCause() == null) {
							dealFailDesc = e.getMessage();
						} else {
							dealFailDesc = e.getCause().getCause() == null ? e.getMessage() : e.getCause().getCause().toString();
						}
						failTxt = sysSource + "," + sheet.getCell(dealidD, i).getContents() + "," + sheet.getCell(merchantCodeD, i).getContents()
								+ "," + sheet.getCell(serialNumberD, i).getContents() + "," + sheet.getCell(transacountD, i).getContents() + ","
								+ sheet.getCell(transacountD, i).getContents() + "," + sheet.getCell(transcostD, i).getContents() + ","
								+ sheet.getCell(terminalIdD, i).getContents() + "," + sheet.getCell(deal_dataD, i).getContents() + ","
								+ sheet.getCell(deal_timeD, i).getContents() + "," + sheet.getCell(deal_statusD, i).getContents() + ","
								+ sheet.getCell(dealtype_idD, i).getContents() + "," + sheet.getCell(dealdescD, i).getContents() + ","
								+ sheet.getCell(dealrebackcodeD, i).getContents() + "," + sheet.getCell(chargeD, i).getContents() + ","
								+ sheet.getCell(bankcardNumberD, i).getContents() + "," + createDt;

						String dealFailId = String.valueOf(dealManageService.getDealFailId());
						dealManageService.addDealFail(dealFailId, failTxt, dealFailDesc, createDt);
					}
				}
			}
		}
		terminalManageService.teminalActivating(createDt);
		// ajaxReturnInfo =
		// AjaxReturnInfo.success("成功导入："+flag+"条; 失败：	"+fail+"条");
		Gson gson = new Gson();
		String temps = gson.toJson("成功：" + flag + "条，失败：" + fail + "条");
		res.setContentType("text/xml;charset=UTF-8");
		res.setHeader("Cache-Control", "no-cache");
		res.getWriter().println(temps);

		return null;
	}

	@RequestMapping(params = "method=getProfit")
	@ResponseBody
	public Map<String, Object> getProfit(@RequestParam(value = "yearmonth") String yearmonth, HttpServletRequest request) {
		String parentagencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		;
		List<ProfitBean> list = dealManageService.getProfit(yearmonth, agencyId);
		List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				ProfitBean p = list.get(i);
				String parent = p.getParentagencyId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("agencyId", p.getAgencyId());
				map.put("agencyName", p.getAgnecyName());
				map.put("transCount", p.getTransCount());
				map.put("transCountagency", p.getTransCountagency());
				map.put("dealCount", p.getDealCount());
				map.put("dealCountagency", p.getDealCountagency());
				map.put("amount", p.getAmount());
				map.put("amountagency", p.getAmountagency());
				map.put("amounted", p.getAmounted());
				map.put("yearmonth", p.getYearmonth());

				if (parent != null && !parent.equals(parentagencyId)) {
					map.put("_parentId", parent);
				}
				resultlist.add(map);

			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", resultlist.size());
		map.put("rows", resultlist);
		return map;
	}

	/**
	 * 得到交易类型明细
	 */
	@RequestMapping(params = "method=getDealByType")
	@ResponseBody
	public Map<String, Object> getDealByType(@RequestParam(value = "yearmonth") String yearmonth, @RequestParam(value = "agencyId") String agencyId,
			HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		List<ProfitBean> list = dealManageService.getDealByType(yearmonth, agencyId);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<ProfitBean> beanList = new ArrayList<ProfitBean>();
		if (list != null && list.size() > 0) {
			for (int i = start; i < end; i++) {
				beanList.add(list.get(i));
			}
		}
		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), beanList);
	}

	/**
	 * 获取当前机构下的交易明细
	 * 
	 * @param yearmonth
	 * @param agencyId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getDealdetail")
	@ResponseBody
	public Map<String, Object> getDealdetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "terminalcode") String terminalcode, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "moblieNo") String moblieNo,
			@RequestParam(value = "customerName") String customerName, HttpServletRequest request) {
		String sysdate = CommonDate.getNowDate();
		// 获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		if (dealType == null || ConstantUtils.COMBOXONEID.equals(dealType)) {
			dealType = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}

		String topAgencyId = agencyId;
		String upperAgencyid = agencyId; // 不为空则进行归并，二级机构开始
		if (StringUtils.isNotEmpty(agencySelect)) {
			topAgencyId = agencySelect;
		}

		String agecyFlag = ""; // 若为不空只能看到直属机构 ，一级机构开始

		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
		if (null != spec) {
			parentAgencyid = "";
			if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
				upperAgencyid = agencySelect;
			}
			if ("0".equals(spec.getLevelControl())) {
				upperAgencyid = "";
				agecyFlag = "";
				upperAgencyid = agencyId;
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else if (parentAgencyids.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
				parentAgencyid = parentAgencyids;
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
				parentAgencyid = parentAgencyids;
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else {
				agecyFlag = topAgencyId;
				upperAgencyid = "";
				if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
			if ("2".equals(queryFlag)) {
				topAgencyId = agencySelect;
				agencySelect = "";
			}
		}

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		DealCountBean totalCount = dealManageService.getDealdetailCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
		Double receDealSum = 0.0d;
		Double receFeeSum = 0.0d;
		Double t0DealSum = 0.0d;
		if (StringUtils.isEmpty(dealType)) {
			dealType = authorityService.getParamObject("QUERYDEALTYPE");
			DealCountBean receCount = dealManageService.getDealdetailReceCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId,
					agencySelect, serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);

			DealCountBean t0Count = dealManageService.getDealdetailT0Count(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
			receDealSum = receCount.getAccountCount();
			receFeeSum = receCount.getFeeAmtCount();
			t0DealSum = t0Count.getAccountCount();
			dealType = "";
		} else {
			receDealSum = totalCount.getAccountCount();
			receFeeSum = totalCount.getFeeAmtCount();
		}

		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount.getCounts() ? totalCount.getCounts() : start + rownumber;
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + yearmonthdatestart + yearmonthdateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDET, ConstantUtils.OPERTYPESER, operateDetail);

		List<DealBean> list = dealManageService.getDealdetail(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect, serialNumber,
				dealType, merchantCode, start, end, terminalcode, upperAgencyid, parentAgencyid, agecyFlag, topAgencyId, moblieNo, customerName,
				typeMergeFlag);
		return AjaxReturnInfo.setTableDeal(totalCount.getCounts(), list, receFeeSum, receDealSum, t0DealSum);
	}

	/**
	 * 获取当前机构下的历史交易明细
	 * 
	 * @param yearmonth
	 * @param agencyId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getOldDealdetail")
	@ResponseBody
	public Map<String, Object> getOldDealdetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "terminalcode") String terminalcode, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "moblieNo") String moblieNo,
			@RequestParam(value = "customerName") String customerName, HttpServletRequest request) {
		String sysdate = CommonDate.getNowDate();
		// 获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		if (dealType == null || ConstantUtils.COMBOXONEID.equals(dealType)) {
			dealType = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		if (ConstantUtils.CENTERCODE.equals(agencyId) || ConstantUtils.CENTERCODE.equals(parentAgencyids)) {
			typeMergeFlag = "";
		}
		String topAgencyId = agencyId;
		String upperAgencyid = agencyId; // 不为空则进行归并，二级机构开始
		if (StringUtils.isNotEmpty(agencySelect)) {
			topAgencyId = agencySelect;
		}

		String agecyFlag = ""; // 若为不空只能看到直属机构 ，一级机构开始

		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
		if (null != spec) {
			parentAgencyid = "";
			if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
				upperAgencyid = agencySelect;
			}
			if ("0".equals(spec.getLevelControl())) {
				upperAgencyid = "";
				agecyFlag = "";
				upperAgencyid = agencyId;
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else if (parentAgencyids.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
				parentAgencyid = parentAgencyids;
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
				parentAgencyid = parentAgencyids;
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			} else {
				agecyFlag = topAgencyId;
				upperAgencyid = "";
				if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
			if ("2".equals(queryFlag)) {
				topAgencyId = agencySelect;
				agencySelect = "";
			}
		}

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		DealCountBean totalCount = dealManageService.getOldDealdetailCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
		Double receDealSum = 0.0d;
		Double receFeeSum = 0.0d;
		Double t0DealSum = 0.0d;
		if (StringUtils.isEmpty(dealType)) {
			dealType = authorityService.getParamObject("QUERYDEALTYPE");
			DealCountBean receCount = dealManageService.getOldDealdetailReceCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId,
					agencySelect, serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);

			DealCountBean t0Count = dealManageService.getOldDealdetailT0Count(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
			receDealSum = receCount.getAccountCount();
			receFeeSum = receCount.getFeeAmtCount();
			t0DealSum = t0Count.getAccountCount();
			dealType = "";
		} else {
			receDealSum = totalCount.getAccountCount();
			receFeeSum = totalCount.getFeeAmtCount();
		}

		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount.getCounts() ? totalCount.getCounts() : start + rownumber;
		
//		  if (page == 1) {
//	            map.put("start", 0);
//	            map.put("end", rows);
//	        } else {
//	            map.put("start", rows * page -rows);
//	            map.put("end", rows * page);
//	        }
//		
		
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + yearmonthdatestart + yearmonthdateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOLDDEALDET, ConstantUtils.OPERTYPESER, operateDetail);

		List<DealBean> list = dealManageService.getOldDealdetail(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				serialNumber, dealType, merchantCode, start, end, terminalcode, upperAgencyid, parentAgencyid, agecyFlag, topAgencyId, moblieNo,
				customerName, typeMergeFlag);
		return AjaxReturnInfo.setTableDeal(totalCount.getCounts(), list,receFeeSum, receDealSum,  t0DealSum);
	}

	/**
	 * 查看当前机构的交易明细（限制条件:1 年月 2 机构 3 交易类型）
	 * 
	 * @param yearmonth
	 * @param agencyId
	 * @param dealtype
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getDealtypedetail")
	@ResponseBody
	public Map<String, Object> getDealtypedetail(@RequestParam(value = "yearmonth") String yearmonth,
			@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "dealtype") String dealtype, HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer totalCount = dealManageService.getDealtypedetailCount(yearmonth, agencyId, dealtype);
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		List<DealBean> list = dealManageService.getDealtypedetail(yearmonth, agencyId, dealtype, start, end);
		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 导出交易按机构汇总的数据
	 * 
	 * @param yearmonth
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=agencydealExport")
	@ResponseBody
	public Map<String, Object> agencydealExport(@RequestParam(value = "yearmonth") String yearmonth, HttpServletRequest request) throws IOException,
			RowsExceededException, WriteException {
		Map<String, Object> map = new HashMap<String, Object>();
		String urls = request.getSession().getServletContext().getRealPath("/") + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}
		String url = request.getSession().getServletContext().getRealPath("/") + "excel\\机构交易汇总.xls";
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);

		WritableSheet ws = wwb.createSheet("agencydeal", 0);
		for (int i = 0; i < ConstantUtils.AGENCYDEAL.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.AGENCYDEAL[i]));
		}
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		;
		List<ProfitBean> list = dealManageService.getProfit(yearmonth, agencyId);
		for (int i = 0; i < list.size(); i++) {
			int col = i + 1;
			ws.addCell(new Label(0, col, list.get(i).getAgencyId()));
			ws.addCell(new Label(1, col, list.get(i).getAgnecyName()));
			ws.addCell(new Label(2, col, list.get(i).getDealCount()));
			ws.addCell(new Label(3, col, list.get(i).getDealCountagency()));
			ws.addCell(new Label(4, col, list.get(i).getTransCount().toString()));
			ws.addCell(new Label(5, col, list.get(i).getTransCountagency().toString()));
			ws.addCell(new Label(6, col, list.get(i).getAmount().toString()));
			ws.addCell(new Label(7, col, list.get(i).getAmountagency().toString()));
			ws.addCell(new Label(8, col, list.get(i).getAmounted().toString()));
		}
		wwb.write();
		wwb.close();
		map.put("url", url);
		return map;
	}

	/**
	 * 导出当前机构下所有交易明细
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportDealDetail")
	public void exportDealDetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "terminalcode") String terminalcode, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "moblieNo") String moblieNo,
			@RequestParam(value = "customerName") String customerName, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		String parentAgencyids = parentAgencyid;
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		if (dealType == null || ConstantUtils.COMBOXONEID.equals(dealType)) {
			dealType = "";
		}

		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}

		String topAgencyId = agencyId;

		if (StringUtils.isNotEmpty(agencySelect)) {
			topAgencyId = agencySelect;
		}

		String upperAgencyid = agencyId;

		String agecyFlag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
		try {
			if (null != spec) {
				parentAgencyid = "";
				if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
					upperAgencyid = agencySelect;
				}
				if ("0".equals(spec.getLevelControl())) {
					upperAgencyid = "";
					agecyFlag = "";
					upperAgencyid = agencyId;
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (parentAgencyids.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else {
					agecyFlag = topAgencyId;
					upperAgencyid = "";
					if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				}
			} else {
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			}

			DealCountBean totalCount = dealManageService.getDealdetailCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
			List<DealBean> list = dealManageService.getDealdetail(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, 0, totalCount.getCounts(), terminalcode, upperAgencyid, parentAgencyid, agecyFlag,
					topAgencyId, moblieNo, customerName, typeMergeFlag);

			long startTime = System.currentTimeMillis();
			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
			File f = new File(urls);
			if (!f.exists()) {
				f.mkdirs();
			}
			String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTDEAL;

			File wj = new File(url);
			int sheetNum = 0;
			WritableWorkbook wwb = Workbook.createWorkbook(wj);
			WritableSheet ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
			for (int i = 0; i < ConstantUtils.DEALDETAIL.length; i++) {
				ws.addCell(new Label(i, 0, ConstantUtils.DEALDETAIL[i]));
			}
			for (int i = 0, j = 0; i < list.size(); i++, j++) {
				int col = j + 1;
				ws.addCell(new Label(0, col, list.get(i).getAgencyName()));
				ws.addCell(new Label(1, col, list.get(i).getTerminal_OnlyCode()));
				ws.addCell(new Label(2, col, list.get(i).getDealTypeName()));
				ws.addCell(new Label(3, col, list.get(i).getSerialNumber()));
				ws.addCell(new Label(4, col, list.get(i).getDeal_data()));
				ws.addCell(new Label(5, col, list.get(i).getDeal_time()));
				String transacount = "";
				transacount = (list.get(i).getTransacount() == null) ? transacount : list.get(i).getTransacount().toString();
				ws.addCell(new Label(6, col, transacount));

				String feeAmt = "";
				feeAmt = (list.get(i).getFeeAmt() == null) ? "" : list.get(i).getFeeAmt().toString();
				ws.addCell(new Label(7, col, feeAmt));
				ws.addCell(new Label(8, col, list.get(i).getBankcardNumber()));
				ws.addCell(new Label(9, col, list.get(i).getMoblieNo()));
				ws.addCell(new Label(10, col, list.get(i).getCustomerName()));
				if (i / 60000 == sheetNum + 1) {
					sheetNum++;
					j = 0;
					ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
				}
			}
			wwb.write();
			wwb.close();
			try {
				// 添加进系统日志表 20141203
				systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEDEALDET, ConstantUtils.OPERTYPEEXPO, null);
				download(request, response, url, ConstantUtils.EXPORTDEAL);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("交易明细导出异常", e);
			}

		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.println("<script> alert('123');</script>");
			log.error("交易明细导出异常", e);
		}

	}

	/**
	 * 导出当前机构下所有交易明细
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportDealDetailCSV")
	public void exportDealDetailSCV(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "terminalcode") String terminalcode, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "moblieNo") String moblieNo,
			@RequestParam(value = "customerName") String customerName, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		String parentAgencyids = parentAgencyid;
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		if (dealType == null || ConstantUtils.COMBOXONEID.equals(dealType)) {
			dealType = "";
		}

		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}

		String topAgencyId = agencyId;

		if (StringUtils.isNotEmpty(agencySelect)) {
			topAgencyId = agencySelect;
		}
		String upperAgencyid = agencyId;
		String agecyFlag = "";
		BufferedOutputStream out = null;
		try {
			SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
			Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
			if (null != spec) {
				parentAgencyid = "";
				if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
					upperAgencyid = agencySelect;
				}
				if ("0".equals(spec.getLevelControl())) {
					upperAgencyid = "";
					agecyFlag = "";
					upperAgencyid = agencyId;
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (parentAgencyids.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else {
					agecyFlag = topAgencyId;
					upperAgencyid = "";
					if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				}
			} else {
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			}

			DealCountBean totalCount = dealManageService.getDealdetailCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
			int allowedMaxNum = dealManageService.getAllowedMaxNum();
			if (totalCount.getCounts() > allowedMaxNum) {
				log.info(totalCount.getCounts() + "超出允许的最大数据量");
				throw new Exception("超出允许的最大数据量");
			}
			List<DealBean> list = dealManageService.getDealdetail(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, 0, totalCount.getCounts(), terminalcode, upperAgencyid, parentAgencyid, agecyFlag,
					topAgencyId, moblieNo, customerName, typeMergeFlag);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ConstantUtils.DEALDETAIL.length; i++) {
				sb.append(ConstantUtils.DEALDETAIL[i] + ",");
			}
			sb.append("\n");
			for (int i = 0, j = 0; i < list.size(); i++, j++) {
				sb.append(list.get(i).getAgencyName() + ",");
				sb.append(list.get(i).getTerminal_OnlyCode() + ",");
				sb.append(list.get(i).getDealTypeName() + ",");
				sb.append(list.get(i).getSerialNumber() + ",");
				sb.append(list.get(i).getDeal_data() + ",");
				sb.append(list.get(i).getDeal_time() + ",");
				String transacount = "";
				transacount = (list.get(i).getTransacount() == null) ? transacount : list.get(i).getTransacount().toString();
				sb.append(transacount + ",");

				String feeAmt = "";
				feeAmt = (list.get(i).getFeeAmt() == null) ? "" : list.get(i).getFeeAmt().toString();
				sb.append(feeAmt + ",");
				sb.append(list.get(i).getBankcardNumber() + ",");
				sb.append(list.get(i).getMoblieNo() + ",");
				sb.append(list.get(i).getCustomerName() + ",");
				sb.append("\n");
			}
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEDEALDET, ConstantUtils.OPERTYPEEXPO, null);

			response.setHeader("Content-Disposition", "attachment; filename=JYMX_" + CommonDate.currentStr() + ".csv");
			response.setCharacterEncoding("GBK");
			response.setContentType("application/vnd.ms-excel");
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			out.write(bom);
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			response.setCharacterEncoding("GBK");
			out.write("<script> alert('超出允许的最大数据量20万');</script>".getBytes());
			log.error("交易明细导出异常" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			out = null;
		}
	}

	/**
	 * 导出当前机构下所有历史交易明细
	 * 
	 * @param yearmonthdate
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=exportOldDealDetail")
	public void exportOldDealDetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			@RequestParam(value = "serialNumber") String serialNumber, @RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "terminalcode") String terminalcode, @RequestParam(value = "merchantCode") String merchantCode,
			@RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "moblieNo") String moblieNo,
			@RequestParam(value = "customerName") String customerName, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		String parentAgencyids = parentAgencyid;
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
			systemId = "";
		}
		if (dealType == null || ConstantUtils.COMBOXONEID.equals(dealType)) {
			dealType = "";
		}

		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}

		String topAgencyId = agencyId;

		if (StringUtils.isNotEmpty(agencySelect)) {
			topAgencyId = agencySelect;
		}
		String upperAgencyid = agencyId;
		String agecyFlag = "";
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
		try {
			if (null != spec) {
				parentAgencyid = "";
				if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
					upperAgencyid = agencySelect;
				}
				if ("0".equals(spec.getLevelControl())) {
					upperAgencyid = "";
					agecyFlag = "";
					upperAgencyid = agencyId;
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (parentAgencyids.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
					parentAgencyid = parentAgencyids;
					upperAgencyid = "";
					if ("2".equals(queryFlag)) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				} else {
					agecyFlag = topAgencyId;
					upperAgencyid = "";
					if (StringUtils.isNotEmpty(agencySelect) && (!agencyId.equals(agencySelect))) {
						topAgencyId = agencySelect;
						agencySelect = "";
					}
				}
			} else {
				upperAgencyid = "";
				if ("2".equals(queryFlag)) {
					topAgencyId = agencySelect;
					agencySelect = "";
				}
			}

			DealCountBean totalCount = dealManageService.getOldDealdetailCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId,
					agencySelect, serialNumber, dealType, merchantCode, terminalcode, topAgencyId, moblieNo, customerName, typeMergeFlag);
			int allowedMaxNum = dealManageService.getAllowedMaxNum();
			if (totalCount.getCounts() > allowedMaxNum) {
				log.info(totalCount.getCounts() + "超出允许的最大数据量");
				throw new Exception("超出允许的最大数据量");
			}
			List<DealBean> list = dealManageService.getOldDealdetail(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
					serialNumber, dealType, merchantCode, 0, totalCount.getCounts(), terminalcode, upperAgencyid, parentAgencyid, agecyFlag,
					topAgencyId, moblieNo, customerName, typeMergeFlag);
			String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
			File f = new File(urls);
			if (!f.exists()) {
				f.mkdirs();
			}
			String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTOLDDEAL;

			File wj = new File(url);
			int sheetNum = 0;
			WritableWorkbook wwb = Workbook.createWorkbook(wj);
			WritableSheet ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
			for (int i = 0; i < ConstantUtils.DEALDETAIL.length; i++) {
				ws.addCell(new Label(i, 0, ConstantUtils.DEALDETAIL[i]));
			}
			for (int i = 0, j = 0; i < list.size(); i++, j++) {
				int col = j + 1;
				ws.addCell(new Label(0, col, list.get(i).getAgencyName()));
				ws.addCell(new Label(1, col, list.get(i).getTerminal_OnlyCode()));
				ws.addCell(new Label(2, col, list.get(i).getDealTypeName()));
				ws.addCell(new Label(3, col, list.get(i).getSerialNumber()));
				ws.addCell(new Label(4, col, list.get(i).getDeal_data()));
				ws.addCell(new Label(5, col, list.get(i).getDeal_time()));
				String transacount = "";
				transacount = (list.get(i).getTransacount() == null) ? transacount : list.get(i).getTransacount().toString();
				ws.addCell(new Label(6, col, transacount));

				String feeAmt = "";
				feeAmt = (list.get(i).getFeeAmt() == null) ? "" : list.get(i).getFeeAmt().toString();
				ws.addCell(new Label(7, col, feeAmt));
				ws.addCell(new Label(8, col, list.get(i).getBankcardNumber()));
				ws.addCell(new Label(9, col, list.get(i).getMoblieNo()));
				ws.addCell(new Label(10, col, list.get(i).getCustomerName()));
				if (i / 60000 == sheetNum + 1) {
					sheetNum++;
					j = 0;
					ws = wwb.createSheet("dealDetail" + sheetNum, sheetNum);
				}
			}
			wwb.write();
			wwb.close();
			try {
				// 添加进系统日志表 20141203
				systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOLDDEALDET, ConstantUtils.OPERTYPEEXPO, null);
				download(request, response, url, ConstantUtils.EXPORTOLDDEAL);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("交易明细导出异常", e);
			}

		} catch (Exception e) {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('超出允许的最大数据量20万');</script>");
			log.error("交易明细导出异常", e);
		}

	}

	/**
	 * 按交易类型统计本机构及其下属机构指定日期范围内的 总交易笔数和总交易金额
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencySelect
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=dealStat")
	@ResponseBody
	public Map<String, Object> dealStat(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roleTypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		// String parentAgencyId =
		// request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roleTypeId.trim())) {
			systemId = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}
		DealCountBean totalCount = dealManageService.getDealStatCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				typeMergeFlag);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount.getCounts() ? totalCount.getCounts() : start + rownumber;

		List<DealBean> list = dealManageService.getDealStat(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect, start, end,
				typeMergeFlag);
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + yearmonthdatestart + yearmonthdateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMETERMDEALSTAT, ConstantUtils.OPERTYPESER, operateDetail);
		return AjaxReturnInfo.setTableDeal(totalCount.getCounts(), list, totalCount.getFeeAmtCount(), totalCount.getAccountCount(), 0d);
	}

	/**
	 * 按交易类型统计本机构及其下属机构指定日期范围内的 历史总交易笔数和总交易金额
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencySelect
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=oldDealStat")
	@ResponseBody
	public Map<String, Object> oldDealStat(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roleTypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		// String parentAgencyId =
		// request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roleTypeId.trim())) {
			systemId = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}

		DealCountBean totalCount = dealManageService.getOldDealStatCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				typeMergeFlag);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount.getCounts() ? totalCount.getCounts() : start + rownumber;

		List<DealBean> list = dealManageService.getOldDealStat(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect, start, end,
				typeMergeFlag);
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + yearmonthdatestart + yearmonthdateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMETERMOLDDEALSTAT, ConstantUtils.OPERTYPESER, operateDetail);
		return AjaxReturnInfo.setTableDeal(totalCount.getCounts(), list, totalCount.getFeeAmtCount(), totalCount.getAccountCount(), 0d);
	}

	/**
	 * 导出交易统计报表
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencySelect
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */

	@RequestMapping(params = "method=dealStatExport")
	@ResponseBody
	public void dealStatExport(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roleTypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roleTypeId.trim())) {
			systemId = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}
		DealCountBean totalCount = dealManageService.getDealStatCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				typeMergeFlag);
		List<DealBean> list = dealManageService.getDealStat(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect, 0,
				totalCount.getCounts(), typeMergeFlag);

		// 根据查询的交易统计列表list写EXCEL文件 20141102
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}

		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTDEALSTAT;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);

		WritableSheet ws = wwb.createSheet("dealstat", 0);
		for (int i = 0; i < ConstantUtils.DEALSTAT.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.DEALSTAT[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int row = i + 1;
			int col = 0;
			ws.addCell(new Label(col++, row, list.get(i).getDealTypeName()));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getTranscost().longValue())));
			ws.addCell(new Label(col++, row, list.get(i).getTransacount().toString()));
			ws.addCell(new Label(col++, row, yearmonthdatestart));
			ws.addCell(new Label(col++, row, yearmonthdateend));
		}
		wwb.write();
		wwb.close();

		try {
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMETERMDEALSTAT, ConstantUtils.OPERTYPEEXPO, null);
			download(request, response, url, ConstantUtils.EXPORTDEALSTAT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 导出历史交易统计报表
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencySelect
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@RequestMapping(params = "method=dealOldStatExport")
	@ResponseBody
	public void dealOldStatExport(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart,
			@RequestParam(value = "yearmonthdateend") String yearmonthdateend, @RequestParam(value = "agencySelect") String agencySelect,
			HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roleTypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyids = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		// String parentAgencyId =
		// request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roleTypeId.trim())) {
			systemId = "";
		}
		String typeMergeFlag = agencyId; // 机构按分润模板交易类型进行关系查询，
		// 若为不空则按模板中交易类型进行查询，否则查询所有
		// 若为不空则按模板中交易类型进行查询，否则查询所有

		if (ConstantUtils.CENTERCODE.equals(agencyId)) {
			typeMergeFlag = agencySelect;
		}
		DealCountBean totalCount = dealManageService.getOldDealStatCount(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect,
				typeMergeFlag);
		List<DealBean> list = dealManageService.getOldDealStat(yearmonthdatestart, yearmonthdateend, agencyId, systemId, agencySelect, 0,
				totalCount.getCounts(), typeMergeFlag);
		// 根据查询的交易统计列表list写EXCEL文件 20141102
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}

		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTDOLDEALSTAT;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);

		WritableSheet ws = wwb.createSheet("dealstat", 0);
		for (int i = 0; i < ConstantUtils.DEALSTAT.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.DEALSTAT[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int row = i + 1;
			int col = 0;
			ws.addCell(new Label(col++, row, list.get(i).getDealTypeName()));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getTranscost().longValue())));
			ws.addCell(new Label(col++, row, list.get(i).getTransacount().toString()));
			ws.addCell(new Label(col++, row, yearmonthdatestart));
			ws.addCell(new Label(col++, row, yearmonthdateend));
		}
		wwb.write();
		wwb.close();

		try {
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMETERMOLDDEALSTAT, ConstantUtils.OPERTYPEEXPO, null);
			download(request, response, url, ConstantUtils.EXPORTDOLDEALSTAT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取交易扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param serialNumber
	 * @param dealType
	 * @param terminalCode
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getDealDeduct")
	@ResponseBody
	public Map<String, Object> getDealDeduct(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, @RequestParam(value = "serialNumber") String serialNumber,
			@RequestParam(value = "dealType") String dealType, @RequestParam(value = "terminalCode") String terminalCode, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}

		String upperAgencyid = agencyId;
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		if (null != spec && !parentAgencyid.equals(ConstantUtils.CENTERCODE)) {
			parentAgencyid = "";
			if (StringUtils.isNotBlank(agencySelect)) {
				if (!agencySelect.equals(agencyId)) {
					agencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
		}

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer totalCount = dealManageService.getDealDeductCount(datestart, dateend, null, agencyId, systemId, agencySelect, serialNumber, dealType,
				terminalCode, upperAgencyid, parentAgencyid);

		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		List<DealDeductBean> list = dealManageService.getDealDeduct(datestart, dateend, null, agencyId, systemId, agencySelect, serialNumber,
				dealType, start, end, terminalCode, upperAgencyid, parentAgencyid);
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + datestart + dateend + serialNumber + dealType + terminalCode + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDEDUCT, ConstantUtils.OPERTYPEDEDUCTSER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 获得所有有效的扣款类型（下拉框使用）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getComboDeductTypes")
	@ResponseBody
	public List<Map<String, Object>> getComboDeductTypes(HttpServletRequest request) {
		String deductClass = request.getParameter("flag");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ConstantUtils.COMBOXONEID);
		map.put("text", "请选择扣款类型");
		map.put("selected", true);
		list.add(map);

		List<DeductTypeBean> dList = dealManageService.getDeductType(deductClass, "", "0");
		for (int i = 0; i < dList.size(); i++) {
			DeductTypeBean bean = dList.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", bean.getDeductType());
			map1.put("text", bean.getDeductName());
			list.add(map1);
		}

		return list;
	}

	/**
	 * 获取扣款类型
	 * 
	 * @param deductClass
	 * @param deductTypeName
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getDeductType")
	@ResponseBody
	public Map<String, Object> getDeductType(@RequestParam(value = "deductClass") String deductClass,
			@RequestParam(value = "deductTypeName") String deductTypeName, @RequestParam(value = "status") String status, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		/*
		 * String rows = request.getParameter("rows"); String page =
		 * request.getParameter("page"); Integer totalCount =
		 * dealManageService.getDeductTypeCount(deductClass,status);
		 * 
		 * int pagenumber = Integer.parseInt((page == null || page == "0") ?
		 * "1": page); int rownumber = Integer.parseInt((rows == null || rows ==
		 * "0") ? "20": rows); int start = (pagenumber - 1) * rownumber; int end
		 * = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		 */
		List<DeductTypeBean> list = dealManageService.getDeductType(deductClass, deductTypeName, status);
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + deductClass + deductTypeName + status;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEDUCTTYPE, ConstantUtils.OPERTYPESER, operateDetail);
		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), list);
	}

	/**
	 * 获取其他扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getOtherDeduct")
	@ResponseBody
	public Map<String, Object> getOtherDeduct(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}

		String upperAgencyid = agencyId;
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		if (null != spec && !parentAgencyid.equals(ConstantUtils.CENTERCODE)) {
			parentAgencyid = "";
			if (StringUtils.isNotBlank(agencySelect)) {
				if (!agencySelect.equals(agencyId)) {
					agencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
		}

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer totalCount = dealManageService.getOtherDeductCount(datestart, dateend, null, agencyId, systemId, agencySelect, upperAgencyid,
				parentAgencyid);

		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
		List<OtherDeductBean> list = dealManageService.getOtherDeduct(datestart, dateend, null, agencyId, systemId, agencySelect, start, end,
				upperAgencyid, parentAgencyid);

		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + datestart + dateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOTHERDEDUCT, ConstantUtils.OPERTYPESER, operateDetail);

		return AjaxReturnInfo.setTable(totalCount, list);
	}

	/**
	 * 按照机构和扣款类型统计交易扣款和其他扣款
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=deductStat")
	@ResponseBody
	public Map<String, Object> deductStat(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, @RequestParam(value = "dealFlag") String dealFlag, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}
		if (dealFlag == null || "-1".equals(dealFlag)) {
			dealFlag = "";
		}

		String upperAgencyid = agencyId;
		// 统计全部（包括中心）按归属机构统计 20141121
		// if (!agencyId.equals(ConstantUtils.CENTERCODE)){
		parentAgencyid = "";
		if (StringUtils.isNotBlank(agencySelect)) {
			if (!agencySelect.equals(agencyId)) {
				agencyId = agencySelect;
				agencySelect = "";
			}
		}
		// }else{
		// upperAgencyid="";
		// }

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		List<RealDeductBean> list = dealManageService.getDeductStat(datestart, dateend, dealFlag, agencyId, systemId, agencySelect, upperAgencyid,
				parentAgencyid, "thisAgency");
		List<RealDeductBean> menulist = new ArrayList<RealDeductBean>();
		if (list != null && list.size() > 0) {
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
			for (int i = start; i < end; i++) {
				menulist.add(list.get(i));
			}
		}
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + datestart + dateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEREALDEDUCT, ConstantUtils.OPERTYPEDEDUCTSTAT, operateDetail);

		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), menulist);
	}

	/**
	 * 获取实际扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getRealDeduct")
	@ResponseBody
	public Map<String, Object> getRealDeduct(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, @RequestParam(value = "dealFlag") String dealFlag, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}
		if (dealFlag == null || "-1".equals(dealFlag)) {
			dealFlag = "";
		}

		String upperAgencyid = agencyId;
		// 统计全部（包括中心）按归属机构统计 20141121
		// if (!agencyId.equals(ConstantUtils.CENTERCODE)){
		parentAgencyid = "";
		if (StringUtils.isNotBlank(agencySelect)) {
			if (!agencySelect.equals(agencyId)) {
				agencyId = agencySelect;
				agencySelect = "";
			}
		}
		// }else{
		// upperAgencyid="";
		// }

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
		int rownumber = Integer.parseInt((rows == null || rows == "0") ? "20" : rows);
		List<RealDeductBean> list = dealManageService.getRealDeduct(datestart, dateend, dealFlag, null, agencyId, systemId, agencySelect,
				upperAgencyid, parentAgencyid, "thisAgency");
		List<RealDeductBean> menulist = new ArrayList<RealDeductBean>();
		if (list != null && list.size() > 0) {
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
			for (int i = start; i < end; i++) {
				menulist.add(list.get(i));
			}
		}
		// 添加操作详情 20141203
		String operateDetail = "查询条件为" + datestart + dateend + agencySelect;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEREALDEDUCT, ConstantUtils.OPERTYPEREALDEDUCTSER, operateDetail);
		return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), menulist);
	}

	/**
	 * 增加扣款类型
	 * 
	 * @param deductClass
	 * @param deductName
	 * @param deductType
	 * @param status
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addDeductType")
	@ResponseBody
	public AjaxReturnInfo addDeductType(@RequestParam(value = "deductClass") String deductClass,
			@RequestParam(value = "deductName") String deductName, @RequestParam(value = "status") String status,
			@RequestParam(value = "comments") String comments, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String createId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String createDt = CommonDate.getDate();
		String deductType = dealManageService.getNextDeductType();

		DeductTypeBean deductTypeBean = new DeductTypeBean();
		deductTypeBean.setDeductClass(deductClass);
		deductTypeBean.setDeductName(deductName);
		deductTypeBean.setDeductType(deductType);
		deductTypeBean.setStatus(status);
		deductTypeBean.setCreateId(createId);
		deductTypeBean.setCreateDt(createDt);
		deductTypeBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");
		int resule = dealManageService.addDeductType(deductTypeBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "添加扣款名称为" + deductName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEDUCTTYPE, ConstantUtils.OPERTYPEADD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 增加交易扣款
	 * 
	 * @param dealId
	 * @param serialNumber
	 * @param transacount
	 * @param terminalId
	 * @param dealDate
	 * @param dealTime
	 * @param dealTypeId
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addDealDeduct")
	@ResponseBody
	public AjaxReturnInfo addDealDeduct(@RequestParam(value = "dealId") String dealId, @RequestParam(value = "serialNumber") String serialNumber,
			@RequestParam(value = "transacount") String transacount, @RequestParam(value = "terminalId") String terminalId,
			@RequestParam(value = "dealDate") String dealDate, @RequestParam(value = "dealTime") String dealTime,
			@RequestParam(value = "onlyCode") String onlyCode, @RequestParam(value = "terminalOnlyCode") String terminalOnlyCode,
			@RequestParam(value = "bankCardNumber") String bankCardNumber, @RequestParam(value = "dealTypeId") String dealTypeId,
			@RequestParam(value = "deductType") String deductType, @RequestParam(value = "deductMoney") String deductMoney,
			@RequestParam(value = "dealFlag") String dealFlag, @RequestParam(value = "comments") String comments, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		int check = dealManageService.checkDealDeductAdd(dealDate, dealTime, serialNumber);
		if (check != 0) {
			return AjaxReturnInfo.failed("该交易已添加扣款!");
		}

		String createId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String createDt = CommonDate.getDate();
		String deductDate = CommonDate.getNowDate();

		DealDeductBean dealDeductBean = new DealDeductBean();
		dealDeductBean.setDealId(dealId);
		dealDeductBean.setSerialNumber(serialNumber);
		dealDeductBean.setTransacount(Double.valueOf(transacount));
		dealDeductBean.setTerminalId(terminalId);
		dealDeductBean.setDealDate(dealDate);
		dealDeductBean.setDealTime(dealTime);
		dealDeductBean.setOnlyCode(onlyCode);
		dealDeductBean.setTerminalOnlyCode(terminalOnlyCode);
		dealDeductBean.setBankCardNumber(bankCardNumber);
		dealDeductBean.setDealTypeId(dealTypeId);
		dealDeductBean.setDeductType(deductType);
		dealDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		dealDeductBean.setDeductDate(deductDate);
		dealDeductBean.setDealFlag(dealFlag);
		dealDeductBean.setCreateId(createId);
		dealDeductBean.setCreateDt(createDt);
		dealDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");
		int resule = dealManageService.addDealDeduct(dealDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "添加交易的流水号为" + serialNumber;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDEDUCT, ConstantUtils.OPERTYPEADD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 增加其他扣款
	 * 
	 * @param agencyId
	 * @param transacount
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addOtherDeduct")
	@ResponseBody
	public AjaxReturnInfo addOtherDeduct(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "transacount") String transacount,
			@RequestParam(value = "deductType") String deductType, @RequestParam(value = "deductMoney") String deductMoney,
			@RequestParam(value = "dealFlag") String dealFlag, @RequestParam(value = "comments") String comments, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String createId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String createDt = CommonDate.getDate();
		String deductDate = CommonDate.getNowDate();
		Integer id = dealManageService.getOtherDeductId();

		OtherDeductBean otherDeductBean = new OtherDeductBean();
		otherDeductBean.setId(id);
		otherDeductBean.setAgencyId(agencyId);
		otherDeductBean.setTransacount(Double.valueOf(transacount));
		otherDeductBean.setDeductType(deductType);
		otherDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		otherDeductBean.setDeductDate(deductDate);
		otherDeductBean.setDealFlag(dealFlag);
		otherDeductBean.setCreateId(createId);
		otherDeductBean.setCreateDt(createDt);
		otherDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");
		int resule = dealManageService.addOtherDeduct(otherDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "添加其他扣款的机构为" + agencyId;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOTHERDEDUCT, ConstantUtils.OPERTYPEADD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 增加实际扣款
	 * 
	 * @param agencyId
	 * @param transacount
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param deductMonth
	 * @param realDeductMonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addRealDeduct")
	@ResponseBody
	public AjaxReturnInfo addRealDeduct(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "transacount") String transacount,
			@RequestParam(value = "deductType") String deductType, @RequestParam(value = "deductMoney") String deductMoney,
			@RequestParam(value = "dealFlag") String dealFlag, @RequestParam(value = "comments") String comments,
			@RequestParam(value = "deductMonth") String deductMonth, @RequestParam(value = "realDeductMonth") String realDeductMonth,
			HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String createId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String createDt = CommonDate.getDate();
		Integer id = dealManageService.getRealDeductId();

		RealDeductBean realDeductBean = new RealDeductBean();
		realDeductBean.setId(id);
		realDeductBean.setAgencyId(agencyId);
		realDeductBean.setDeductType(deductType);
		realDeductBean.setTransacount(Double.valueOf(transacount));
		realDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		realDeductBean.setDeductMonth(deductMonth);
		realDeductBean.setRealDeductMonth(realDeductMonth);
		realDeductBean.setDealFlag(dealFlag);
		realDeductBean.setCreateId(createId);
		realDeductBean.setCreateDt(createDt);
		realDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");
		int resule = dealManageService.addRealDeduct(realDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "添加扣款的机构ID为" + agencyId;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEREALDEDUCT, ConstantUtils.OPERTYPEADD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 修改扣款类型
	 * 
	 * @param deductClass
	 * @param deductType
	 * @param deductName
	 * @param status
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateDeductType")
	@ResponseBody
	public AjaxReturnInfo updateDeductType(@RequestParam(value = "deductClass") String deductClass,
			@RequestParam(value = "deductType") String deductType, @RequestParam(value = "deductName") String deductName,
			@RequestParam(value = "status") String status, @RequestParam(value = "comments") String comments, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		DeductTypeBean deductTypeBean = new DeductTypeBean();
		deductTypeBean.setDeductClass(deductClass);
		deductTypeBean.setDeductName(deductName);
		deductTypeBean.setDeductType(deductType);
		deductTypeBean.setStatus(status);
		deductTypeBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("修改成功!");
		int resule = dealManageService.updateDeductType(deductTypeBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("修改失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "修改扣款名为" + deductName;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEDUCTTYPE, ConstantUtils.OPERTYPEUPD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 修改交易扣款
	 * 
	 * @param dealId
	 * @param serialNumber
	 * @param transacount
	 * @param terminalId
	 * @param dealDate
	 * @param dealTime
	 * @param onlyCode
	 * @param dealTypeId
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateDealDeduct")
	@ResponseBody
	public AjaxReturnInfo updateDealDeduct(@RequestParam(value = "dealId") String dealId, @RequestParam(value = "serialNumber") String serialNumber,
			@RequestParam(value = "transacount") String transacount, @RequestParam(value = "terminalId") String terminalId,
			@RequestParam(value = "dealDate") String dealDate, @RequestParam(value = "dealTime") String dealTime,
			@RequestParam(value = "onlyCode") String onlyCode, @RequestParam(value = "terminalOnlyCode") String terminalOnlyCode,
			@RequestParam(value = "bankCardNumber") String bankCardNumber, @RequestParam(value = "dealTypeId") String dealTypeId,
			@RequestParam(value = "deductType") String deductType, @RequestParam(value = "deductMoney") String deductMoney,
			@RequestParam(value = "dealFlag") String dealFlag, @RequestParam(value = "comments") String comments, HttpServletRequest request) {
		/*
		 * String createId =
		 * request.getSession().getAttribute(ConstantUtils.USERID).toString();
		 * String createDt = CommonDate.getDate();
		 */
		String deductDate = CommonDate.getNowDate();
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		DealDeductBean dealDeductBean = new DealDeductBean();
		dealDeductBean.setDealId(dealId);
		dealDeductBean.setSerialNumber(serialNumber);
		dealDeductBean.setTransacount(Double.valueOf(transacount));
		dealDeductBean.setTerminalId(terminalId);
		dealDeductBean.setDealDate(dealDate);
		dealDeductBean.setDealTime(dealTime);
		dealDeductBean.setOnlyCode(onlyCode);
		dealDeductBean.setTerminalOnlyCode(terminalOnlyCode);
		dealDeductBean.setBankCardNumber(bankCardNumber);
		dealDeductBean.setDealTypeId(dealTypeId);
		dealDeductBean.setDeductType(deductType);
		dealDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		dealDeductBean.setDeductDate(deductDate);
		dealDeductBean.setDealFlag(dealFlag);
		// dealDeductBean.setCreateId(createId);
		// dealDeductBean.setCreateDt(createDt);
		dealDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("修改成功!");
		int resule = dealManageService.updateDealDeduct(dealDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("修改失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "修改的流水号为" + serialNumber;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDEDUCT, ConstantUtils.OPERTYPEUPD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 修改其他扣款
	 * 
	 * @param agencyId
	 * @param transacount
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateOtherDeduct")
	@ResponseBody
	public AjaxReturnInfo updateOtherDeduct(@RequestParam(value = "id") Integer id, @RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "transacount") String transacount, @RequestParam(value = "deductType") String deductType,
			@RequestParam(value = "deductMoney") String deductMoney, @RequestParam(value = "dealFlag") String dealFlag,
			@RequestParam(value = "comments") String comments, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String deductDate = CommonDate.getNowDate();

		OtherDeductBean otherDeductBean = new OtherDeductBean();
		otherDeductBean.setId(id);
		otherDeductBean.setAgencyId(agencyId);
		otherDeductBean.setTransacount(Double.valueOf(transacount));
		otherDeductBean.setDeductType(deductType);
		otherDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		otherDeductBean.setDeductDate(deductDate);
		otherDeductBean.setDealFlag(dealFlag);
		otherDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("修改成功!");
		int resule = dealManageService.updateOtherDeduct(otherDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("修改失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "修改其他扣款的机构为" + agencyId;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEOTHERDEDUCT, ConstantUtils.OPERTYPEUPD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 修改实际扣款
	 * 
	 * @param id
	 * @param agencyId
	 * @param transacount
	 * @param deductType
	 * @param deductMoney
	 * @param dealFlag
	 * @param comments
	 * @param deductMonth
	 * @param realDeductMonth
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateRealDeduct")
	@ResponseBody
	public AjaxReturnInfo updateRealDeduct(@RequestParam(value = "id") Integer id, @RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "transacount") String transacount, @RequestParam(value = "deductType") String deductType,
			@RequestParam(value = "deductMoney") String deductMoney, @RequestParam(value = "dealFlag") String dealFlag,
			@RequestParam(value = "comments") String comments, @RequestParam(value = "deductMonth") String deductMonth,
			@RequestParam(value = "realDeductMonth") String realDeductMonth, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		RealDeductBean realDeductBean = new RealDeductBean();
		realDeductBean.setId(id);
		realDeductBean.setAgencyId(agencyId);
		realDeductBean.setDeductType(deductType);
		realDeductBean.setTransacount(Double.valueOf(transacount));
		realDeductBean.setDeductMoney(Double.valueOf(deductMoney));
		realDeductBean.setDeductMonth(deductMonth);
		realDeductBean.setRealDeductMonth(realDeductMonth);
		realDeductBean.setDealFlag(dealFlag);
		realDeductBean.setComments(comments);

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("修改成功!");
		int resule = dealManageService.updateRealDeduct(realDeductBean);
		;
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("修改失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "修改实际扣款的机构ID为" + agencyId;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEREALDEDUCT, ConstantUtils.OPERTYPEUPD, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 删除扣款类型一条记录
	 * 
	 * @param deductClass
	 * @param deductType
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=deleteDeductType")
	@ResponseBody
	public AjaxReturnInfo deleteDeductType(@RequestParam(value = "deductType") String deductType, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String deductClass = request.getParameter("deductClass");
		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("删除成功!");
		int resule = dealManageService.deleteDeductType(deductClass, deductType);
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("删除失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "删除的扣款类型为" + deductType;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEDUCTTYPE, ConstantUtils.OPERTYPEDEL, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 删除交易扣款一条记录
	 * 
	 * @param dealDate
	 * @param dealTime
	 * @param serialNumber
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=deleteDealDeduct")
	@ResponseBody
	public AjaxReturnInfo deleteDealDeduct(@RequestParam(value = "dealDate") String dealDate, @RequestParam(value = "dealTime") String dealTime,
			@RequestParam(value = "serialNumber") String serialNumber, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("删除成功!");
		int resule = dealManageService.deleteDealDeduct(dealDate, dealTime, serialNumber);
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("删除失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "删除记录的流水号为" + serialNumber;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDEDUCT, ConstantUtils.OPERTYPEDEL, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 删除其他扣款记录（可能多条）
	 * 
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=deleteOtherDeducts")
	@ResponseBody
	public AjaxReturnInfo deleteOtherDeducts(@RequestParam(value = "Ids") String ids, HttpServletRequest request) {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("删除成功!");
		int resule = dealManageService.deleteOtherDeducts(ids);
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("删除失败!");
		} else {
			// 添加操作详情 20141203
			String operateDetail = "删除的其他扣款ID为" + ids;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOTHERDEDUCT, ConstantUtils.OPERTYPEDEL, operateDetail);
		}

		return ajaxReturnInfo;
	}

	/**
	 * 删除实际扣款记录（可能多条）
	 * 
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=deleteRealDeducts")
	@ResponseBody
	public AjaxReturnInfo deleteRealDeducts(@RequestParam(value = "Ids") String ids, HttpServletRequest request) {
		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("删除成功!");
		int resule = dealManageService.deleteRealDeducts(ids);
		if (resule == 0) {
			ajaxReturnInfo = AjaxReturnInfo.failed("删除失败!");
		}

		return ajaxReturnInfo;
	}

	/**
	 * 导出交易扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param serialNumber
	 * @param dealType
	 * @param terminalCode
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	@RequestMapping(params = "method=dealDeductExport")
	@ResponseBody
	public void dealDeductExport(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, @RequestParam(value = "serialNumber") String serialNumber,
			@RequestParam(value = "dealType") String dealType, @RequestParam(value = "terminalCode") String terminalCode, HttpServletRequest request,
			HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}

		String upperAgencyid = agencyId;
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		if (null != spec && !parentAgencyid.equals(ConstantUtils.CENTERCODE)) {
			parentAgencyid = "";
			if (StringUtils.isNotBlank(agencySelect)) {
				if (!agencySelect.equals(agencyId)) {
					agencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
		}

		Integer totalCount = dealManageService.getDealDeductCount(datestart, dateend, null, agencyId, systemId, agencySelect, serialNumber, dealType,
				terminalCode, upperAgencyid, parentAgencyid);
		List<DealDeductBean> list = dealManageService.getDealDeduct(datestart, dateend, null, agencyId, systemId, agencySelect, serialNumber,
				dealType, 0, totalCount, terminalCode, upperAgencyid, parentAgencyid);

		// 根据查询的交易扣款列表list写EXCEL文件 20141119
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}

		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTDEALDEDUCT;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);

		WritableSheet ws = wwb.createSheet("交易扣款", 0);
		for (int i = 0; i < ConstantUtils.DEALDEDUCT.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.DEALDEDUCT[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int row = i + 1;
			int col = 0;
			ws.addCell(new Label(col++, row, list.get(i).getAgencyName()));
			ws.addCell(new Label(col++, row, list.get(i).getTerminalId()));
			ws.addCell(new Label(col++, row, list.get(i).getDealTypeName()));
			ws.addCell(new Label(col++, row, list.get(i).getSerialNumber()));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getTransacount())));
			ws.addCell(new Label(col++, row, list.get(i).getDealDate()));
			ws.addCell(new Label(col++, row, list.get(i).getDealTime()));
			ws.addCell(new Label(col++, row, list.get(i).getDeductTypeName()));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getDeductMoney())));
			ws.addCell(new Label(col++, row, list.get(i).getDeductDate()));
			ws.addCell(new Label(col++, row, list.get(i).getDealFlagStr()));
			ws.addCell(new Label(col++, row, list.get(i).getComments()));
		}
		wwb.write();
		wwb.close();

		try {
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEDEALDEDUCT, ConstantUtils.OPERTYPEEXPO, null);
			download(request, response, url, ConstantUtils.EXPORTDEALDEDUCT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 导出其他扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencySelect
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	@RequestMapping(params = "method=otherDeductExport")
	@ResponseBody
	public void otherDeductExport(@RequestParam(value = "datestart") String datestart, @RequestParam(value = "dateend") String dateend,
			@RequestParam(value = "agencySelect") String agencySelect, HttpServletRequest request, HttpServletResponse response) throws IOException,
			RowsExceededException, WriteException {
		// 获得ip
		String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

		String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
		String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
		String parentAgencyid = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
		if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeId.trim())) {
			systemId = "";
		}

		String upperAgencyid = agencyId;
		SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
		if (null != spec && !parentAgencyid.equals(ConstantUtils.CENTERCODE)) {
			parentAgencyid = "";
			if (StringUtils.isNotBlank(agencySelect)) {
				if (!agencySelect.equals(agencyId)) {
					agencyId = agencySelect;
					agencySelect = "";
				}
			}
		} else {
			upperAgencyid = "";
		}

		Integer totalCount = dealManageService.getOtherDeductCount(datestart, dateend, null, agencyId, systemId, agencySelect, upperAgencyid,
				parentAgencyid);
		List<OtherDeductBean> list = dealManageService.getOtherDeduct(datestart, dateend, null, agencyId, systemId, agencySelect, 0, totalCount,
				upperAgencyid, parentAgencyid);

		// 根据查询的其他扣款列表list写EXCEL文件 20141119
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
		File f = new File(urls);
		if (!f.exists()) {
			f.mkdirs();
		}

		String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTOTHERDEDUCT;
		File wj = new File(url);
		WritableWorkbook wwb = Workbook.createWorkbook(wj);

		WritableSheet ws = wwb.createSheet("其他扣款", 0);
		for (int i = 0; i < ConstantUtils.OTHERDEDUCT.length; i++) {
			ws.addCell(new Label(i, 0, ConstantUtils.OTHERDEDUCT[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			int row = i + 1;
			int col = 0;
			ws.addCell(new Label(col++, row, list.get(i).getAgencyName()));
			ws.addCell(new Label(col++, row, list.get(i).getDeductTypeName()));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getTransacount())));
			ws.addCell(new Label(col++, row, String.valueOf(list.get(i).getDeductMoney())));
			ws.addCell(new Label(col++, row, list.get(i).getDeductDate()));
			ws.addCell(new Label(col++, row, list.get(i).getDealFlagStr()));
			ws.addCell(new Label(col++, row, list.get(i).getComments()));
		}
		wwb.write();
		wwb.close();

		try {
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEOTHERDEDUCT, ConstantUtils.OPERTYPEEXPO, null);
			download(request, response, url, ConstantUtils.EXPORTOTHERDEDUCT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * @RequestMapping(params = "method=exportDealType")
	 * 
	 * @ResponseBody public Map<String,Object>
	 * exportDealType(@RequestParam(value = "yearmonth") String yearmonth,
	 * 
	 * @RequestParam(value = "agencyId") String agencyId,
	 * 
	 * @RequestParam(value = "dealtype") String dealtype, HttpServletRequest
	 * request ) throws IOException, RowsExceededException, WriteException{
	 * List<DealBean> list= dealManageService.getDealtypedetailAll(yearmonth,
	 * agencyId, dealtype); String urls =
	 * request.getSession().getServletContext().getRealPath("/")+ "excel"; File
	 * f = new File(urls); if(!f.exists()){ f.mkdirs(); } String url =
	 * request.getSession().getServletContext().getRealPath("/")+
	 * "excel\\机构交易类型信息表.xls"; File wj = new File(url); WritableWorkbook wwb =
	 * Workbook.createWorkbook(wj); WritableSheet ws =
	 * wwb.createSheet("dealDetail", 0); for(int
	 * i=0;i<ConstantUtils.DEALDETAIL.length;i++){ ws.addCell(new
	 * Label(i,0,ConstantUtils.DEALDETAIL[i])); } for(int
	 * i=0;i<list.size();i++){ int col=i+1; ws.addCell(new
	 * Label(0,col,list.get(i).getAgencyName())); ws.addCell(new
	 * Label(1,col,list.get(i).getDealtype_id())); ws.addCell(new
	 * Label(2,col,list.get(i).getSerialNumber())); ws.addCell(new
	 * Label(3,col,list.get(i).getDeal_data())); ws.addCell(new
	 * Label(4,col,list.get(i).getTransacount().toString())); ws.addCell(new
	 * Label(5,col,list.get(i).getCost().toString())); ws.addCell(new
	 * Label(6,col,list.get(i).getDealrebackcode())); ws.addCell(new
	 * Label(7,col,list.get(i).getCharge().toString())); ws.addCell(new
	 * Label(8,col,list.get(i).getDealdesc())); ws.addCell(new
	 * Label(9,col,list.get(i).getDeal_status())); ws.addCell(new
	 * Label(10,col,list.get(i).getOnlyCode())); ws.addCell(new
	 * Label(11,col,list.get(i).getSysSource())); } Map<String,Object> map=new
	 * HashMap<String,Object>(); wwb.write(); wwb.close(); map.put("url", url);
	 * return map; }
	 */
	/*
	 * @RequestMapping(params = "method=exportDealtypeprofit")
	 * 
	 * @ResponseBody public Map<String,Object> exportDealtypeprofit(
	 * 
	 * @RequestParam(value = "yearmonth") String yearmonth,
	 * 
	 * @RequestParam(value = "agencyId") String agencyId, HttpServletRequest
	 * request) throws IOException, RowsExceededException, WriteException{
	 * String urls = request.getSession().getServletContext().getRealPath("/")+
	 * "excel"; File f = new File(urls); if(!f.exists()){ f.mkdirs(); } String
	 * url = request.getSession().getServletContext().getRealPath("/")+
	 * "excel\\交易类型汇总表.xls"; File wj = new File(url); WritableWorkbook wwb =
	 * Workbook.createWorkbook(wj); WritableSheet ws =
	 * wwb.createSheet("dealtypeprofit", 0); for(int
	 * i=0;i<ConstantUtils.DEALTYPE.length;i++){ ws.addCell(new
	 * Label(i,0,ConstantUtils.DEALTYPE[i])); } List<ProfitBean>
	 * list=dealManageService.getDealByType(yearmonth, agencyId); for(int
	 * i=0;i<list.size();i++){ int col=i+1; ws.addCell(new
	 * Label(0,col,list.get(i).getAgnecyName())); ws.addCell(new
	 * Label(1,col,list.get(i).getDealtype())); ws.addCell(new
	 * Label(2,col,list.get(i).getYearmonth())); ws.addCell(new
	 * Label(3,col,list.get(i).getDealCount())); ws.addCell(new
	 * Label(4,col,list.get(i).getTransCount().toString())); ws.addCell(new
	 * Label(5,col,list.get(i).getAmount().toString())); ws.addCell(new
	 * Label(6,col,list.get(i).getAmounted().toString())); ws.addCell(new
	 * Label(7,col,list.get(i).getFee().toString())); } Map<String,Object>
	 * map=new HashMap<String,Object>(); wwb.write(); wwb.close();
	 * map.put("url", url); return map; }
	 */

	private DealBean SetDealBean(String sysSource, String dealId, String serialNumber, String dealData, String dealTime, String merchantCode,
			String terminalCode, String transAcount, String dealStatus, String dealTypeId, String dealDesc, String dealRebackCode, String charge,
			String transCost, String bankCardNumber, String createDt) {

		if ("".equals(transAcount) || transAcount == null) {
			transAcount = "0";
		}
		if ("".equals(charge) || charge == null) {
			charge = "0";
		}
		if ("".equals(transCost) || transCost == null) {
			transCost = "0";
		}
		DealBean dealBean = new DealBean();
		String onlyCode = sysSource + dealId + serialNumber + dealData;
		String terminalOnlyCode = sysSource + terminalCode + merchantCode;
		dealBean.setSysSource(sysSource);
		dealBean.setDealId(dealId);
		dealBean.setMerchantCode(merchantCode);
		dealBean.setSerialNumber(serialNumber);
		dealBean.setTransacount(Double.valueOf(transAcount));
		dealBean.setTerminalId(terminalCode);
		dealBean.setDeal_data(dealData);
		dealBean.setDeal_time(dealTime);
		dealBean.setDeal_status(dealStatus);
		dealBean.setDealtype_id(dealTypeId);
		dealBean.setDealdesc(dealDesc);
		dealBean.setDealrebackcode(dealRebackCode);
		dealBean.setCharge(Double.valueOf(charge));
		dealBean.setTranscost(Double.valueOf(transCost));
		dealBean.setBankcardNumber(bankCardNumber);
		dealBean.setCreateDt(createDt);
		dealBean.setOnlyCode(onlyCode);
		dealBean.setTerminal_OnlyCode(terminalOnlyCode);
		return dealBean;
	}

	/**
	 * 文件下载
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
			fileName = null;
		}

	}
}
