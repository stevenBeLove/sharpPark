package com.compass.terminalmanage.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.servlet.ModelAndView;

import cn.ardu.jms.ems.exception.QTException;

import com.compass.agency.controller.AgencyController;
import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.SpecSystemBean;
import com.compass.agency.service.AgencyService;
import com.compass.constans.InterfaceNameConstans;
import com.compass.pasmFee.service.PsmFeeService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.terminalmanage.model.TerminalBack;
import com.compass.terminalmanage.model.TerminalChange;
import com.compass.terminalmanage.model.TerminalCountBean;
import com.compass.terminalmanage.model.TerminalManageBean;
import com.compass.terminalmanage.model.TerminalMsgBean;
import com.compass.terminalmanage.model.TerminalSerilBean;
import com.compass.terminalmanage.service.TerminalManageService;
import com.compass.utils.CommonDate;
import com.compass.utils.CommonEnums.BatchState;
import com.compass.utils.CommonEnums.TerminalChangeType;
import com.compass.utils.ConstantUtils;
import com.compass.utils.Format;
import com.compass.utils.TxtEncodingUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.utils.mvc.I18nUtils;
import com.google.gson.Gson;

@Controller
@RequestMapping({ "/terminalmanage/terminalmanage.do" })
public class TerminalManageController {
    private final Log            log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("terminalManageService")
    public TerminalManageService terminalManageService;

    @Autowired
    @Qualifier("systemManageService")
    private SystemManageService  systemManageService;

    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService     systemLogService;

    @Autowired
    @Qualifier("agencyService")
    private AgencyService        agencyService;
    /**
     * 分润成本设置
     */
    @Autowired
    @Qualifier("psmFeeService")
    private PsmFeeService        psmFeeService;

    @RequestMapping(params = { "method=getTerminalManageTb" })
    @ResponseBody
    public Map<String, Object> getTerminalManageTb(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("status") String status,
            HttpServletRequest request) {
        log.info("终端管理 --终端导入");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        String systemId = request.getSession().getAttribute("systemid").toString();
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        String roletypeid = request.getSession().getAttribute("roletypeId").toString().trim();
        String parentAgencyid = request.getSession().getAttribute("parentagencyId").toString().trim();
        String upperAgencyid = parentAgencyid;
        SpecSystemBean spec = this.systemManageService.checkSpecSystem(systemId);
        if ("1".equals(roletypeid.trim())) {
            systemId = "";
        }
        if ((spec != null) && (parentAgencyid.equals(ConstantUtils.CENTERCODE))) {
            parentAgencyid = "";
        } else {
            upperAgencyid = "";
        }
        int totalCount = this.terminalManageService.getTerminalManageTbCount(terminalCode, terminalTypeId, status, agencyId, systemId).intValue();
        if ("-1".equals(status)) {
            status = null;
        }
        if ("-1".equals(terminalTypeId)) {
            terminalTypeId = null;
        }
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalManageTb(terminalCode, terminalTypeId, status, agencyId, systemId, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    @RequestMapping(params = { "method=getTerminalManagecheck" })
    @ResponseBody
    public Map<String, Object> getTerminalManagecheck(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("status") String status,
            HttpServletRequest request) {
        log.info("终端审核");
        String systemId = request.getSession().getAttribute("systemid").toString();
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        String roletypeid = request.getSession().getAttribute("roletypeId").toString().trim();
        if ("1".equals(roletypeid.trim())) {
            systemId = "";
        }
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        if ("-1".equals(status)) {
            status = null;
        }
        if ("-1".equals(terminalTypeId)) {
            terminalTypeId = null;
        }
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int totalCount = this.terminalManageService.getTerminalManageCheckCount(terminalCode, terminalTypeId, status, agencyId, systemId).intValue();

        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalManagecheck(terminalCode, terminalTypeId, status, agencyId, systemId, start, end);

        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    @RequestMapping(params = { "method=getTerminalManageback" })
    @ResponseBody
    public Map<String, Object> getTerminalManageback(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("status") String status,
            @RequestParam("terminalCodeEnd") String terminalCodeEnd, @RequestParam("agencytreeId") String agencytreeId, @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend, HttpServletRequest request) {
        log.info("终端回拔");
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String systemId = request.getSession().getAttribute("systemid").toString();
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        if ("-1".equals(agencytreeId)) {
            agencytreeId = "";
        }
        String terminalCodeStart = terminalCode;
        String flagCTP = ""; // 标记终端查询， 为空则查询单个，不为空则查询批次
        if (StringUtils.isEmpty(terminalCodeEnd)) {
            terminalCodeStart = "";
        } else {
            flagCTP = "1";
            terminalCode = "";
            /*
             * if (terminalCodeStart.length() > 12) terminalCodeStart =
             * terminalCodeStart + "0"; if (terminalCodeEnd.length() > 12) {
             * terminalCodeEnd = terminalCodeEnd + "9"; }
             */
        }
        if (StringUtils.isNotEmpty(agencytreeId)) {
            agencyId = "";
        }

        int totalCount = this.terminalManageService.getTerminalManageBackCount(terminalCode, terminalTypeId, status, agencyId, systemId, terminalCodeStart, terminalCodeEnd, flagCTP, agencytreeId,
                datestart, dateend).intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalManageback(terminalCode, terminalTypeId, status, agencyId, systemId, terminalCodeStart, terminalCodeEnd, flagCTP, start, end, agencytreeId,
                datestart, dateend);

        String operateDetail = "查询条件为" + terminalCode + terminalTypeId;

        this.systemLogService.addLog(ipAddress, agencyId, userId, "终端回拨", "4", operateDetail);

        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    @RequestMapping(params = { "method=getNewTerminalManageback" })
    @ResponseBody
    public Map<String, Object> getNewTerminalManageback(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalCodeEnd") String terminalCodeEnd, HttpServletRequest request) {
        log.info("终端回拔");
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String systemId = request.getSession().getAttribute("systemid").toString();
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        String terminalCodeStart = terminalCode;
        String flagCTP = ""; // 标记终端查询， 为空则查询单个，不为空则查询批次
        if (StringUtils.isEmpty(terminalCodeEnd)) {
            terminalCodeStart = "";
        } else {
            flagCTP = "1";
            terminalCode = "";
            if (terminalCodeStart.length() > 12) {
                terminalCodeStart = terminalCodeStart + "0";
            }
            if (terminalCodeEnd.length() > 12) {
                terminalCodeEnd = terminalCodeEnd + "9";
            }
        }
        int totalCount = this.terminalManageService.getTerminalManageBackCount(terminalCode, "", "0", agencyId, systemId, terminalCodeStart, terminalCodeEnd, flagCTP, "", "", "").intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalManageback(terminalCode, "", "0", agencyId, systemId, terminalCodeStart, terminalCodeEnd, flagCTP, start, end, "", "", "");
        String operateDetail = "查询条件为" + terminalCode + terminalCodeEnd;
        this.systemLogService.addLog(ipAddress, agencyId, userId, "终端回拨", "4", operateDetail);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    /**
     * 【方法名】 : (新终端回拨查询). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月7日 下午2:52:56 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalCode
     *            终端编号
     * @param terminalTypeId
     *            终端类型
     * @param status
     *            状态
     * @param terminalCodeEnd
     *            结束编号
     * @param agencytreeId
     *            机构树
     * @param datestart
     *            开始日期
     * @param dateend
     *            结束日期
     * @param request
     *            HttpServletRequest
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=saveTerminalChangeBatch" })
    @ResponseBody
    public AjaxReturnInfo saveTerminalChangeBatch(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("status") String status,
            @RequestParam("terminalCodeEnd") String terminalCodeEnd, @RequestParam("agencytreeId") String agencytreeId, @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend, HttpServletRequest request) throws Exception {
        log.info("新终端回拔");
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String systemId = request.getSession().getAttribute("systemid").toString();
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        if ("-1".equals(agencytreeId)) {
            agencytreeId = "";
        }
        String terminalCodeStart = terminalCode;
        String flagCTP = ""; // 标记终端查询， 为空则查询单个，不为空则查询批次
        if (StringUtils.isEmpty(terminalCodeEnd)) {
            terminalCodeStart = "";
        } else {
            flagCTP = "1";
            terminalCode = "";
            if (terminalCodeStart.length() > 12) {
                terminalCodeStart = terminalCodeStart + "0";
            }
            if (terminalCodeEnd.length() > 12) {
                terminalCodeEnd = terminalCodeEnd + "9";
            }
        }
        if (StringUtils.isNotEmpty(agencytreeId)) {
            agencyId = "";
        }

        TerminalChange change = new TerminalChange();
        change.setBatchno(generateRefNo(userId));
        change.setBeginTermid(terminalCode);
        change.setEndTermid(terminalCodeEnd);
        change.setChangeType(TerminalChangeType.goback.getVal());
        // 原机构编码
        change.setOldAgencyid(agencyId);
        AjaxReturnInfo ajaxInfo = AjaxReturnInfo.failed("操作失败!");
        AgencyBean agency = agencyService.getAgency(agencyId);
        if (!org.springframework.util.StringUtils.isEmpty(agency)) {
            // 新机构编码
            change.setNewAgencyid(agency.getOnlinechannel());
            // 申请时间
            change.setApplyTime(Format.time());
            // 申请人
            change.setApplyUserid(userId);
            // 批次状态
            change.setBatchState(BatchState.apply.getVal());
            int result = terminalManageService.insertTerminalChange(change);
            if (result > 0) {
                ajaxInfo = AjaxReturnInfo.failed("操作成功!");
            } else {
                ajaxInfo = AjaxReturnInfo.failed("操作失败!");
            }
            String operateDetail = "创建批次为：开始号段" + terminalCode + ",结束号段：" + terminalCodeEnd;
            this.systemLogService.addLog(ipAddress, agencyId, userId, "终端回拨批次", "4", operateDetail);
        }
        return ajaxInfo;
    }

    /**
     * 
     * 【方法名】 : (批次号). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年12月13日 下午12:08:11 .<br/>
     * 【参数】： .<br/>
     * 
     * @param userid
     *            String
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     * @throws QTException
     */
    private String generateRefNo(String userid) throws QTException {
        String dateTime = CommonDate.currentTimeWithFormat(CommonDate.YYYYMMDDHHMMSS);
        int userId = 0;
        try {
            userId = Integer.parseInt(userid);
        } catch (NumberFormatException e) {
            throw new QTException("生成批次号出错！");
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumIntegerDigits(7);
        nf.setMinimumIntegerDigits(7);
        return dateTime + nf.format(userId);
    }

    @RequestMapping(params = { "method=getTerminalManageAllTb" })
    @ResponseBody
    public Map<String, Object> getTerminalManageAllTb(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("status") String status,
            @RequestParam("agencyId") String agencyId, @RequestParam("systemId") String systemId, @RequestParam("agencyCode") String agencyCode, @RequestParam("startCode") String startCode,
            @RequestParam("endCode") String endCode, @RequestParam("queryFlag") String queryFlag, @RequestParam("datestart") String activeDtStart, @RequestParam("dateend") String activeDtEnd,
            @RequestParam("bindDtstart") String bindDtstart, @RequestParam("bindDtend") String bindDtend,
            String isJoincash,  String isPay, String havepsam, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute("ipAddress").toString();
        String userIdS = request.getSession().getAttribute("userId").toString();
        String agencyIdS = request.getSession().getAttribute("agencyId").toString();
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        String userSystemId = request.getSession().getAttribute("systemid").toString();
        String userAgencyId = request.getSession().getAttribute("agencyId").toString();
        String roletypeid = request.getSession().getAttribute("roletypeId").toString().trim();
        String parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();

        if ("1".equals(roletypeid.trim())) {
            userSystemId = "";
        }
        if ("-1".equals(status)) {
            status = null;
        }
        if ("-1".equals(terminalTypeId)) {
            terminalTypeId = null;
        }
        if ("-1".equals(agencyId)) {
            agencyId = "";
        }

        SpecSystemBean spec = this.systemManageService.checkSpecSystem(userSystemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyIdS);

        if (StringUtils.isEmpty(agencyId)) {
            queryFlag = "";
        }

        String agencytopId = userAgencyId;
        String upperAgencyid = userAgencyId;
        if (StringUtils.isNotEmpty(agencyId)) {
            upperAgencyid = agencyId;
        }
        String agecyFlag = "";
        // 查询指定机构
        // 查询归属机构不归并
        // 查询归属机构归并
        if (spec != null) {
            if ((StringUtils.isNotEmpty(agencyId)) && (!agencyId.equals(userAgencyId))) {
                agencytopId = agencyId;
            }
            upperAgencyid = userAgencyId;
            if (StringUtils.isNotEmpty(agencyId)) {
                upperAgencyid = agencyId;
                // agencyId = "";
            }
            if ("0".equals(spec.getLevelControl())) {
                upperAgencyid = "";
                agecyFlag = "";
                parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                if ("2".equals(queryFlag)) {
                    agencytopId = agencyId;
                    agencyId = "";
                }
            } else if ("1".equals(spec.getLevelControl())) {
                if (parentAgencyId.equals(ConstantUtils.CENTERCODE)) {
                    parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                    upperAgencyid = "";
                    agecyFlag = "";
                    if ("2".equals(queryFlag)) {
                        agencytopId = agencyId;
                        agencyId = "";
                    }
                } else if (agencyCount > 0) {
                    parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                    upperAgencyid = "";
                    agecyFlag = "";
                    if ("2".equals(queryFlag)) {
                        agencytopId = agencyId;
                        agencyId = "";
                    }
                } else {
                    parentAgencyId = "";
                    agecyFlag = agencyIdS;
                    upperAgencyid = "";
                    if ((StringUtils.isNotEmpty(agencyId)) && (!agencyId.equals(userAgencyId))) {
                        agencytopId = agencyId;
                        agencyId = "";
                    }
                }
            } else {
                agecyFlag = upperAgencyid;
                upperAgencyid = "";
                parentAgencyId = "";
            }
        } else {
            upperAgencyid = "";
            agecyFlag = "";
            if ("2".equals(queryFlag)) {
                agencytopId = agencyId;
                agencyId = "";
            }
        }

        if (StringUtils.isNotEmpty(startCode) && startCode.trim().length() > 12) {
            startCode = startCode.trim() + "0";
        }
        if (StringUtils.isNotEmpty(startCode) && endCode.trim().length() > 12) {
            endCode = endCode.trim() + "9";
        }
        int totalCount = this.terminalManageService.getTerminalManageAllCount(terminalCode, terminalTypeId, status, agencyId, systemId, agencyCode, userAgencyId, parentAgencyId, upperAgencyid,
                startCode, endCode, agecyFlag, agencytopId, activeDtStart, activeDtEnd, bindDtstart,bindDtend,isJoincash, isPay, havepsam).intValue();

        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == null) || (rows == "0") ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;

        List list = this.terminalManageService.getTerminalManageAllTb(terminalCode, terminalTypeId, status, agencyId, systemId, agencyCode, userAgencyId, start, end, parentAgencyId, upperAgencyid,
                startCode, endCode, agecyFlag, agencytopId, activeDtStart, activeDtEnd, bindDtstart,bindDtend,isJoincash, isPay, havepsam);

        String operateDetail = "查询条件为:终端编号：" + terminalCode + ",终端类型：" + terminalTypeId + "，终端状态 ：" + status;

        this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, "终端下发", "4", operateDetail);

        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    @RequestMapping(params = { "method=upload" })
    @ResponseBody
    public void upload(MultipartHttpServletRequest multipartRequest, HttpServletResponse response, HttpServletRequest req) {
        String ipAddress = req.getRemoteAddr();
        String userIdS = req.getSession().getAttribute("userId").toString();
        String agencyIdS = req.getSession().getAttribute("agencyId").toString();

        List list = new ArrayList();
        response.setContentType("text/html;charset=UTF-8");
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("upload_file");
        String teminalmanageRule = req.getParameter("teminalmanageRule");
        try {
            PrintWriter print = response.getWriter();
            InputStream in = file.getInputStream();
            String webRoot = getClass().getClassLoader().getResource("/").getPath();
            String realPath = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + file.getName();
            File rootFile = new File(realPath);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File ff = new File(realPath, file.getOriginalFilename());
            OutputStream out = new FileOutputStream(ff);
            FileCopyUtils.copy(in, out);
            req.getSession().setAttribute("upload_file", ff);
            String fileName = file.getOriginalFilename();
            String pathFileName = realPath + "/" + fileName;
            if (fileName.endsWith("txt")) {
                if ((ff.isFile()) && (ff.exists())) {
                    Map map1 = new HashMap();
                    map1.put("id", "-1");
                    map1.put("text", "---请选择---");
                    map1.put("selected", Boolean.valueOf(true));
                    list.add(map1);
                    req.getSession().setAttribute("namePath", pathFileName);
                    String encodinng = TxtEncodingUtils.codeString(pathFileName);
                    InputStreamReader read = new InputStreamReader(new FileInputStream(ff), encodinng);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    int flag = 0;

                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        String[] temp = lineTxt.split(teminalmanageRule);
                        if (flag == 0) {
                            for (int i = 0; i < temp.length; i++) {
                                Map map = new HashMap();
                                map.put("id", Integer.valueOf(i));
                                map.put("text", temp[i]);
                                list.add(map);
                            }
                        }
                        flag++;
                    }
                    read.close();
                    bufferedReader.close();
                }
            } else {
                Map map1 = new HashMap();
                map1.put("id", "-1");
                map1.put("text", "---请选择---");
                map1.put("selected", Boolean.valueOf(true));
                list.add(map1);
                Workbook work = Workbook.getWorkbook(new FileInputStream(ff));
                Sheet sheet = work.getSheet(0);
                int i = 0;
                if (i < sheet.getRows()) {
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        try {
                            Map map = new HashMap();
                            map.put("id", Integer.valueOf(j));
                            map.put("text", sheet.getCell(j, i).getContents());
                            list.add(map);
                        } catch (Exception localException1) {
                        }
                    }
                }
            }
            this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, "终端管理", "11", null);

            Gson gosn = new Gson();
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
        }
    }

    @RequestMapping(params = { "method=saveExcelData" })
    @ResponseBody
    public String save(@RequestParam("systemSource") String systemSource, @RequestParam("merchantCode") String merchantCode, @RequestParam("teminalmanageRule") String teminalmanageRule,
            @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("terminalCode") String terminalCode, HttpServletRequest req, HttpServletResponse res) throws IOException,
            BiffException {
        String agencyId = req.getSession().getAttribute("agencyId") == null ? "" : (String) req.getSession().getAttribute("agencyId");
        String userId = req.getSession().getAttribute("userId") == null ? "" : (String) req.getSession().getAttribute("userId");
        Integer terminal = Integer.valueOf(terminalCode);
        Integer merchant = Integer.valueOf(merchantCode);
        File file = (File) req.getSession().getAttribute("upload_file");

        int fail = 0;
        int sucess = 0;
        String failTxt = "";
        String terminalFailDesc = "";
        String createDt = CommonDate.getDate();
        String fileName = file.getName();
        if (fileName.endsWith("txt")) {
            if ((file.isFile()) && (file.exists())) {
                String pathFileName = (String) req.getSession().getAttribute("namePath");
                String encodinng = "utf-8";
                try {
                    encodinng = TxtEncodingUtils.codeString(pathFileName);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encodinng);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int flag = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] temp = lineTxt.split(teminalmanageRule);
                    if (flag != 0) {
                        TerminalManageBean terminalManageBean = SetTerminalBean(systemSource, temp[merchant.intValue()], temp[terminal.intValue()], agencyId, terminalTypeId, userId, createDt,
                                ConstantUtils.TERMINALNONACTIVATEDSTATUS.intValue());
                        try {
                            int k = this.terminalManageService.addTerminalManage(terminalManageBean);
                            if (k > 0) {
                                sucess++;
                            }
                        } catch (Exception e) {
                            fail++;
                            if (e.getCause() == null) {
                                terminalFailDesc = e.getMessage();
                            } else {
                                terminalFailDesc = e.getCause().getCause() == null ? e.getMessage() : e.getCause().getCause().toString();
                            }
                            failTxt = temp[terminal.intValue()] + "," + temp[merchant.intValue()] + "," + agencyId + "," + terminalTypeId + "," + userId + "," + createDt + "," + systemSource;
                            String terminalFailId = String.valueOf(this.terminalManageService.getTerminalFailId());
                            this.terminalManageService.addTerminalManageFail(terminalFailId, failTxt, terminalFailDesc, createDt);
                        }
                    }
                    flag++;
                }
            }
        } else {
            Workbook work = Workbook.getWorkbook(new FileInputStream(file));
            Sheet sheet = work.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) {
                if ((sheet.getCell(terminal.intValue(), i).getContents() != null) && (!"".equals(sheet.getCell(terminal.intValue(), i).getContents().toString().trim()))) {
                    TerminalManageBean terminalManageBean = SetTerminalBean(systemSource, sheet.getCell(merchant.intValue(), i).getContents(), sheet.getCell(terminal.intValue(), i).getContents(),
                            agencyId, terminalTypeId, userId, createDt, ConstantUtils.TERMINALNONACTIVATEDSTATUS.intValue());
                    try {
                        int k = this.terminalManageService.addTerminalManage(terminalManageBean);
                        if (k > 0)
                            sucess++;
                    } catch (Exception e) {
                        fail++;
                        if (e.getCause() == null)
                            terminalFailDesc = e.getMessage();
                        else {
                            terminalFailDesc = e.getCause().getCause() == null ? e.getMessage() : e.getCause().getCause().toString();
                        }
                        failTxt = sheet.getCell(terminal.intValue(), i).getContents() + "," + sheet.getCell(merchant.intValue(), i).getContents() + "," + agencyId + "," + terminalTypeId + ","
                                + userId + "," + createDt + "," + systemSource;
                        String terminalFailId = String.valueOf(this.terminalManageService.getTerminalFailId());
                        this.terminalManageService.addTerminalManageFail(terminalFailId, failTxt, terminalFailDesc, createDt);
                    }

                }

            }

        }

        Gson gson = new Gson();
        String temps = gson.toJson("成功：" + sucess + "条，失败：" + fail + "条");
        res.setContentType("text/xml;charset=UTF-8");
        res.setHeader("Cache-Control", "no-cache");

        res.getWriter().println(temps);

        return null;
    }

    @RequestMapping(params = { "method=rebackTerminal" })
    @ResponseBody
    public AjaxReturnInfo rebackTerminal(@RequestParam("terminalCodes") String terminalCodes, @RequestParam("status") String status, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
        int result = this.terminalManageService.updateTerminalStatus(terminalCodes, status, agencyId);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("操作失败");
        } else {
            ajaxinfo = AjaxReturnInfo.success("回拨成功");
            String[] Codes = terminalCodes.split(",");
            List beanList = new ArrayList();
            for (int i = 0; i < Codes.length; i++) {
                TerminalSerilBean serilBean = new TerminalSerilBean();
                serilBean.setAgencyId(agencyId);
                serilBean.setNewagencyId(agencyId);
                serilBean.setTerminalId(Codes[i].replace("'", ""));
                serilBean.setOldagencyId(agencyId);
                serilBean.setTerminalDesc("发起回拨");
                serilBean.setCreateId(agencyId);
                serilBean.setCreateDt(CommonDate.getDate());
                beanList.add(serilBean);
            }
            this.terminalManageService.addTeminalseril(beanList);
        }
        return ajaxinfo;
    }

    @RequestMapping(params = { "method=checkTerminal" })
    @ResponseBody
    public AjaxReturnInfo checkTerminal(@RequestParam("agencyId") String oldagencyId, @RequestParam("terminalCodes") String terminalCodes, @RequestParam("flag") boolean flag,
            HttpServletRequest request) {
        terminalCodes = terminalCodes.substring(0, terminalCodes.length() - 1);
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");

        String ipAddress = request.getRemoteAddr();
        String userIdS = request.getSession().getAttribute("userId").toString();
        String agencyIdS = request.getSession().getAttribute("agencyId").toString();
        String parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();

        int result = this.terminalManageService.checkTerminal(terminalCodes, agencyId, agencyIdS, flag);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("操作失败");
        } else {
            ajaxinfo = AjaxReturnInfo.success("操作成功");
            String userId = request.getSession().getAttribute("userId") == null ? "" : (String) request.getSession().getAttribute("userId");
            String[] Codes = terminalCodes.split(",");
            List beanList = new ArrayList();
            for (int i = 0; i < Codes.length; i++) {
                TerminalSerilBean serilBean = new TerminalSerilBean();
                serilBean.setAgencyId(agencyId);
                serilBean.setNewagencyId(agencyId);
                serilBean.setTerminalId(Codes[i].replace("'", ""));
                serilBean.setOldagencyId(oldagencyId);
                serilBean.setTerminalDesc("回拔");
                serilBean.setCreateId(userId);
                serilBean.setCreateDt(CommonDate.getDate());
                beanList.add(serilBean);
            }
            this.terminalManageService.addTeminalseril(beanList);

            String operateDetail = "回拨的终端code为" + terminalCodes;

            this.systemLogService.addLog(ipAddress, agencyId, userId, "终端回拨", "13", operateDetail);
        }

        return ajaxinfo;
    }

    @RequestMapping(params = { "method=checkTerminalfail" })
    @ResponseBody
    public AjaxReturnInfo checkTerminalfail(@RequestParam("agencyId") String oldagencyId, @RequestParam("terminalCodes") String terminalCodes, HttpServletRequest request) {
        terminalCodes = terminalCodes.substring(0, terminalCodes.length() - 1);
        int result = this.terminalManageService.checkTerminalfail(terminalCodes);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("操作失败");
        } else {
            ajaxinfo = AjaxReturnInfo.success("操作成功");
            String userId = request.getSession().getAttribute("userId") == null ? "" : (String) request.getSession().getAttribute("userId");
            String[] Codes = terminalCodes.split(",");
            List beanList = new ArrayList();
            for (int i = 0; i < Codes.length; i++) {
                TerminalSerilBean serilBean = new TerminalSerilBean();
                serilBean.setAgencyId(oldagencyId);
                serilBean.setNewagencyId(oldagencyId);
                serilBean.setTerminalId(Codes[i].replace("'", ""));
                serilBean.setOldagencyId(oldagencyId);
                serilBean.setTerminalDesc("未通过");
                serilBean.setCreateId(userId);
                serilBean.setCreateDt(CommonDate.getDate());
                beanList.add(serilBean);
            }
            this.terminalManageService.addTeminalseril(beanList);
        }
        return ajaxinfo;
    }

    @RequestMapping(params = { "method=updateTerminalIssued" })
    @ResponseBody
    public AjaxReturnInfo updateTerminalIssued(@RequestParam("agencyId") String agencyId, @RequestParam("merchantCodes") String merchantCodes, @RequestParam("Ids") String Ids,
            @RequestParam("flag") boolean flag, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute("ipAddress").toString();
        String userIdS = request.getSession().getAttribute("userId").toString();
        String agencyIdS = request.getSession().getAttribute("agencyId").toString();
        Ids = Ids.substring(0, Ids.length() - 1);
        AjaxReturnInfo ajaxinfo = null;
        int result = 0;
        String terminalFailDesc = "";

        String[] Idss = Ids.split(",");
        try {
            for (int i = 0; i < Idss.length; i++) {
                psmFeeService.queryCostRate(agencyId, Idss[i].replace("'", ""));
            }
            result = this.terminalManageService.updateTerminalExamine(agencyId, merchantCodes, Ids, flag);
            if (result == 0) {
                ajaxinfo = AjaxReturnInfo.failed("下发失败");
            } else {
                ajaxinfo = AjaxReturnInfo.success("成功:" + result + " 条，失败：" + (Idss.length - result));
                String oldagencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
                String userId = request.getSession().getAttribute("userId") == null ? "" : (String) request.getSession().getAttribute("userId");
                String[] ids = Ids.split(",");
                List beanList = new ArrayList();
                for (int i = 0; i < ids.length; i++) {
                    TerminalSerilBean serilBean = new TerminalSerilBean();
                    serilBean.setAgencyId(agencyId);
                    serilBean.setNewagencyId(agencyId);
                    serilBean.setTerminalId(ids[i].replace("'", ""));
                    serilBean.setOldagencyId(oldagencyId);
                    serilBean.setTerminalDesc("下发");
                    serilBean.setCreateId(userId);
                    serilBean.setCreateDt(CommonDate.getDate());
                    beanList.add(serilBean);
                }
                this.terminalManageService.addTeminalseril(beanList);

                TerminalChange change = new TerminalChange();
                change.setBeginTermid(ids[0].replace("'", "").substring(0, 15));
                change.setEndTermid(ids[ids.length - 1].replace("'", "").substring(0, 15));
                change.setBatchno(generateRefNo(userIdS));
                change.setOldAgencyid(agencyIdS);
                change.setNewAgencyid(agencyId);
                change.setChangeType(ConstantUtils.AGENCY_OPERNAMETERMINQU);
                change.setApplyTime(Format.time());
                change.setTermNums(result);
                change.setBatchState(BatchState.workFinish.getVal());
                terminalManageService.insertTerminalChange(change);
                String count = String.valueOf(result);
                // 查询下级机构信息
                AgencyBean agencyBean = agencyService.queryCertification(agencyId);
                // 查询上级机构信息
                AgencyBean agency = agencyService.queryCertification(agencyIdS);
                // 发送短信给下级
                this.terminalManageService.sendTerminalSMS(agencyBean.getCompanyPhone(), agency.getCompanyName(), agencyBean.getContactsName(), count, InterfaceNameConstans.VALUE_2);
                // 发送短信给上级
                this.terminalManageService.sendTerminalSMS(agency.getCompanyPhone(), agencyBean.getCompanyName(), agency.getContactsName(), count, InterfaceNameConstans.VALUE_1);

                String operateDetail = "下发终端的ID为" + Ids;
                this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, "终端下发", "12", operateDetail);
            }
        } catch (QTException e) {
            log.error("终端下发" + e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
        } catch (Exception e) {
            log.error("终端下发" + e.getMessage(), e);
            terminalFailDesc = e.getMessage();
            ajaxinfo = AjaxReturnInfo.failed(terminalFailDesc);
        }
        return ajaxinfo;
    }

    /**
     * 
     * 方法名： updateTerminalBatchIssued(终端批量下发).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月11日.<br/>
     * 创建时间：下午2:32:46.<br/>
     * 参数者异常：@param agencyId 参数者异常：@param startCode 参数者异常：@param endCode
     * 参数者异常：@param merchantCode 参数者异常：@param systemId 参数者异常：@param flag
     * 参数者异常：@param request 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    @RequestMapping(params = { "method=updateTerminalBatchIssued" })
    @ResponseBody
    public AjaxReturnInfo updateTerminalBatchIssued(@RequestParam("agencyId") String agencyId, @RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode,
            @RequestParam("merchantCode") String merchantCode, @RequestParam("systemId") String systemId, @RequestParam("flag") boolean flag, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute("ipAddress").toString();
        String userIdS = request.getSession().getAttribute("userId").toString();
        String agencyIdS = request.getSession().getAttribute("agencyId").toString();

        String parentAgencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");

        if ("".equals(startCode)) {
            startCode = "0";
        }
        if ("".equals(endCode)) {
            endCode = "0";
        }

        int result = 0;
        String terminalFailDesc = "";
        AjaxReturnInfo ajaxinfo = null;
        String prefix = null;
        TerminalChange change = new TerminalChange();
        if (startCode.length() >= 15) {
            change.setBeginTermid(startCode.substring(0, 15));
        } else {
            change.setBeginTermid(startCode);
        }
        if (endCode.length() >= 15) {
            change.setEndTermid(endCode.substring(0, 15));
        } else {
            change.setEndTermid(endCode);
        }

        if (startCode.length() == 15) {
            startCode = startCode + "0";
        }
        if (endCode.length() == 15) {
            endCode = endCode + "9";
        }
        try {
            psmFeeService.selectCostRate(agencyId, agencyIdS, systemId, startCode, endCode);
            int totalCount = this.terminalManageService.getTerminalBatchIssuedCount(systemId, parentAgencyId, startCode, endCode).intValue();
            this.terminalManageService.addTeminalserilAll(agencyId, startCode, endCode, agencyIdS, "下发", userIdS, systemId);
            result = this.terminalManageService.updateTerminalBatchIssued(agencyId, systemId, parentAgencyId, startCode, endCode, merchantCode, flag);
            if (result == 0) {
                ajaxinfo = AjaxReturnInfo.failed("下发失败");
            } else {
                change.setBatchno(generateRefNo(userIdS));
                change.setOldAgencyid(agencyIdS);
                change.setNewAgencyid(agencyId);
                change.setChangeType(ConstantUtils.AGENCY_OPERNAMETERMINQU);
                change.setApplyTime(Format.time());
                change.setTermNums(result);
                change.setBatchState(BatchState.workFinish.getVal());
                terminalManageService.insertTerminalChange(change);
                String count = String.valueOf(result);
                // 查询下级机构信息
                AgencyBean agencyBean = agencyService.queryCertification(agencyId);
                // 查询上级机构信息
                AgencyBean agency = agencyService.queryCertification(agencyIdS);
                // 发送短信给下级
                this.terminalManageService.sendTerminalSMS(agencyBean.getCompanyPhone(), agency.getCompanyName(), agencyBean.getContactsName(), count, InterfaceNameConstans.VALUE_2);
                // 发送短信给上级
                this.terminalManageService.sendTerminalSMS(agency.getCompanyPhone(), agencyBean.getCompanyName(), agency.getContactsName(), count, InterfaceNameConstans.VALUE_1);
                this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, "终端下发", "12", startCode + "|" + endCode);
                ajaxinfo = AjaxReturnInfo.success("成功:" + result + " 条，失败：" + (totalCount - result));
            }
        } catch (QTException e) {
            log.error("终端下发" + e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
        } catch (Exception e) {
            if (e.getCause() == null) {
                terminalFailDesc = e.getMessage();
            } else {
                terminalFailDesc = e.getCause().getCause() == null ? e.getMessage() : e.getCause().getCause().toString();
            }
            ajaxinfo = AjaxReturnInfo.failed(terminalFailDesc);
        }
        return ajaxinfo;
    }

    @RequestMapping(params = { "method=getTerminalBatchIssued" })
    @ResponseBody
    public Map<String, Object> getTerminalBatchIssued(@RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, @RequestParam("systemId") String systemId,
            HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");

        if ("".equals(startCode)) {
            startCode = "0";
        }
        if ("".equals(endCode)) {
            endCode = "0";
        }

        if (startCode.length() == 15) {
            startCode = startCode + "0";
        }
        if (endCode.length() == 15) {
            endCode = endCode + "9";
        }

        int totalCount = this.terminalManageService.getTerminalBatchIssuedCount(systemId, agencyId, startCode, endCode).intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalBatchIssuedTb(systemId, agencyId, startCode, endCode, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    private TerminalManageBean SetTerminalBean(String systemSource, String merchantCode, String terminalCode, String agencyId, String terminalTypeId, String userId, String createDt, int terminalStatus) {
        TerminalManageBean terminalManageBean = new TerminalManageBean();

        terminalManageBean.setOnlyCode(terminalCode + merchantCode);
        terminalManageBean.setTerminalCode(terminalCode);
        terminalManageBean.setMerchantCode(merchantCode);
        terminalManageBean.setAgencyId(agencyId);
        terminalManageBean.setTerminaltypeId(terminalTypeId);
        terminalManageBean.setTerminalStatus(Integer.valueOf(terminalStatus));
        terminalManageBean.setCreateId(userId);
        terminalManageBean.setCreateDt(createDt);
        terminalManageBean.setSystemSource(systemSource);
        return terminalManageBean;
    }

    @RequestMapping(params = { "method=getTerminalManageStatus" })
    @ResponseBody
    public Map<String, Object> getTerminalManageStatus(@RequestParam("terminalCode") String terminalCode, HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");

        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        String systemId = request.getSession().getAttribute("systemid").toString();

        List list = new ArrayList();

        SpecSystemBean spec = this.systemManageService.checkSpecSystem(systemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);

        String flag1 = "1";
        String flag2 = "";
        if (spec != null) {
            flag1 = "";
            flag2 = "2";
            if ("0".equals(spec.getLevelControl())) {
                flag1 = "1";
                flag2 = "";
            } else if ("1".equals(spec.getLevelControl())) {
                flag1 = "1";
                flag2 = "";
            } else if (agencyCount > 0) {
                flag1 = "1";
                flag2 = "";
            }
        }
        int totalCount = 0;
        if (StringUtils.isNotBlank(terminalCode)) {
            totalCount = this.terminalManageService.getTerminalStatusCount(terminalCode, agencyId, flag1, flag2);
            int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
            int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
            int start = (pagenumber - 1) * rownumber;
            int end = start + rownumber > totalCount ? totalCount : start + rownumber;
            list = this.terminalManageService.getTerminalStatus(terminalCode, agencyId, flag1, flag2, start, end);
        }

        String operateDetail = "查询条件终端code为" + terminalCode;

        this.systemLogService.addLog(ipAddress, agencyId, userId, "终端状态变更", "4", operateDetail);
        return AjaxReturnInfo.setTable(totalCount, list);
    }

    @RequestMapping(params = { "method=updateTerminalstatus" })
    @ResponseBody
    public AjaxReturnInfo updateTerminalstatus(@RequestParam("terminalCode") String terminalCode, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String agencyId = request.getSession().getAttribute("agencyId").toString();

        String status = "0";
        int result = this.terminalManageService.updateTerminalStatus(status, terminalCode, agencyId);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("操作失败");
        } else {
            String operateDetail = "审核通过的终端code为" + terminalCode;
            this.systemLogService.addLog(ipAddress, agencyId, userId, "终端状态变更", "1", operateDetail);
            ajaxinfo = AjaxReturnInfo.success("操作成功");
        }
        return ajaxinfo;
    }

    @RequestMapping(params = { "method=terminalExport" })
    public void terminalExport(HttpServletRequest request, HttpServletResponse response, @RequestParam("terminalCode") String terminalCode, @RequestParam("terminalTypeId") String terminalTypeId,
            @RequestParam("status") String status, @RequestParam("agencyId") String agencyId, @RequestParam("systemId") String systemId, @RequestParam("agencyCode") String agencyCode,
            @RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, @RequestParam("queryFlag") String queryFlag, @RequestParam("datestart") String activeDtStart,
            @RequestParam("bindDtstart") String bindDtstart, @RequestParam("bindDtend") String bindDtend, String isJoincash,  String isPay,String havepsam, @RequestParam("dateend") String activeDtEnd) throws IOException, RowsExceededException, WriteException {
        String ipAddress = request.getRemoteAddr();
        String userIdS = request.getSession().getAttribute("userId").toString();
        String agencyIdS = request.getSession().getAttribute("agencyId").toString();
        String userSystemId = request.getSession().getAttribute("systemid").toString();
        String userAgencyId = request.getSession().getAttribute("agencyId").toString();
        String parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
        String parentAgencyIds = request.getSession().getAttribute("parentagencyId").toString().trim();
        String agencytopId = userAgencyId;
        boolean isCenterUser = false;
        if ("-1".equals(systemId)) {
            systemId = "";
        }
        if ("-1".equals(status)) {
            status = null;
        }
        if ("-1".equals(terminalTypeId)) {
            terminalTypeId = null;
        }
        if ("-1".equals(agencyId)) {
            agencyId = "";
        }
        if (StringUtils.isEmpty(agencyId)) {
            queryFlag = "";
        }

        if (ConstantUtils.CENTERCODE.equals(userAgencyId)) {
            isCenterUser = true;
        }
        String agecyFlag = "";
        String upperAgencyid = userAgencyId;
        if (StringUtils.isNotEmpty(agencyId)) {
            upperAgencyid = agencyId;
        }
        SpecSystemBean spec = this.systemManageService.checkSpecSystem(userSystemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyIdS);
        if (spec != null) {

            if ((StringUtils.isNotEmpty(agencyId)) && (!agencyId.equals(userAgencyId))) {
                agencytopId = agencyId;
            }

            upperAgencyid = userAgencyId;

            if ("0".equals(spec.getLevelControl())) {
                upperAgencyid = "";
                agecyFlag = "";
                parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                if ("2".equals(queryFlag)) {
                    agencytopId = agencyId;
                    agencyId = "";
                }
            } else if ("1".equals(spec.getLevelControl())) {
                if (parentAgencyId.equals(ConstantUtils.CENTERCODE)) {
                    parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                    upperAgencyid = "";
                    agecyFlag = "";
                    if ("2".equals(queryFlag)) {
                        agencytopId = agencyId;
                        agencyId = "";
                    }
                } else if (agencyCount > 0) {
                    parentAgencyId = request.getSession().getAttribute("parentagencyId").toString().trim();
                    upperAgencyid = "";
                    agecyFlag = "";
                    if ("2".equals(queryFlag)) {
                        agencytopId = agencyId;
                        agencyId = "";
                    }
                } else {
                    agecyFlag = upperAgencyid;
                    upperAgencyid = "";
                    parentAgencyId = "";
                    if (StringUtils.isNotEmpty(agencyId) && (!agencyId.equals(userAgencyId))) {
                        agencyId = "";
                    }
                }
            } else {
                agecyFlag = upperAgencyid;
                upperAgencyid = "";
                parentAgencyId = "";
            }
        } else {
            upperAgencyid = "";
            agecyFlag = "";
            if ("2".equals(queryFlag)) {
                agencytopId = agencyId;
                agencyId = "";
            }
        }

        if (startCode.length() > 12)
            startCode = startCode + "0";
        if (endCode.length() > 12) {
            endCode = endCode + "9";
        }
        int totalCount = this.terminalManageService.getTerminalManageAllCount(terminalCode, terminalTypeId, status, agencyId, systemId, agencyCode, userAgencyId, parentAgencyId, upperAgencyid,
                startCode, endCode, agecyFlag, agencytopId, activeDtStart, activeDtEnd,bindDtstart,bindDtend, isJoincash, isPay, havepsam ).intValue();

        int start = 0;
        int end = totalCount;
        List list = this.terminalManageService.getTerminalManageAllTb(terminalCode, terminalTypeId, status, agencyId, systemId, agencyCode, userAgencyId, start, end, parentAgencyId, upperAgencyid,
                startCode, endCode, agecyFlag, agencytopId, activeDtStart, activeDtEnd,bindDtstart,bindDtend, isJoincash, isPay, havepsam);

        String webRoot = getClass().getClassLoader().getResource("/").getPath();
        String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
        File f = new File(urls);
        if (!f.exists()) {
            f.mkdirs();
        }

        String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + "终端下发明细.xls";
        File wj = new File(url);
        WritableWorkbook wwb = Workbook.createWorkbook(wj);
        int sheetNum = 0;
        WritableSheet ws = wwb.createSheet("terminal" + sheetNum, sheetNum);
        int i = isCenterUser ? 0 : 1;
        for (int j = 0; i < ConstantUtils.TERMINAL.length; j++) {
            ws.addCell(new Label(j, 0, ConstantUtils.TERMINAL[i]));

            i++;
        }

        for (int j = 0; j < list.size(); j++) {
            addCell(list, ws, j, isCenterUser);
            if (j / 60000 == sheetNum + 1) {
                sheetNum++;
                ws = null;
                ws = wwb.createSheet("agency" + sheetNum, sheetNum);
            }
        }
        wwb.write();
        wwb.close();
        try {
            this.systemLogService.addLog(ipAddress, agencyIdS, userIdS, "终端下发", "10", null);
            AgencyController agencyController = new AgencyController();
            agencyController.download(request, response, url, "终端下发明细.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCell(List<TerminalManageBean> list, WritableSheet ws, int i, boolean isCenterUser) throws WriteException, RowsExceededException {
        int clum = i + 1;
        int j = 0;
        TerminalManageBean termBean = (TerminalManageBean) list.get(i);
        if (isCenterUser) {
            ws.addCell(new Label(j++, clum, termBean.getSystemSource()));
        }
        ws.addCell(new Label(j++, clum, termBean.getTerminalCode()));
        ws.addCell(new Label(j++, clum, termBean.getTerminaltypeName()));
        ws.addCell(new Label(j++, clum, termBean.getAgencyName()));
        ws.addCell(new Label(j++, clum, termBean.getTerminalStatusStr()));
    }

    @RequestMapping(params = { "method=getTerminalView" })
    @ResponseBody
    public Map<String, Object> getTerminalView(@RequestParam("agencyId") String agencyId, @RequestParam("selAgencyId") String selAgencyId, @RequestParam("agencyIdOld") String agencyIdOld,
            @RequestParam("selAgencyIdOld") String selAgencyIdOld, @RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend, @RequestParam("opt") String opt, HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        String systemId = request.getSession().getAttribute("systemid").toString();
        String userAgencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");

        int totalCount = terminalManageService.getTerminalViewCount(agencyId, selAgencyId, agencyIdOld, selAgencyIdOld, startCode, endCode, datestart, dateend, opt, userAgencyId);

        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = terminalManageService.getTerminalView(agencyId, selAgencyId, agencyIdOld, selAgencyIdOld, startCode, endCode, datestart, dateend, opt, userAgencyId, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    /**
     * 查询核心数据库的终端信息
     * 
     * @param startCode
     * @param endCode
     * @param request
     * @return
     */
    @RequestMapping(params = { "method=getTerminalManageCore" })
    @ResponseBody
    public Map<String, Object> getTerminalManageCore(@RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int totalCount = this.terminalManageService.getTerminalCoreCount(startCode, endCode).intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalCore(startCode, endCode, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    /**
     * 查询的taCard终端信息
     * 
     * @param startCode
     * @param endCode
     * @param request
     * @return
     */
    @RequestMapping(params = { "method=getTaCardTerminalCore" })
    @ResponseBody
    public Map<String, Object> getTaCardTerminalCore(@RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int totalCount = this.terminalManageService.getTaCardTerminalCoreCount(startCode, endCode).intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTaCardTerminalCore(startCode, endCode, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    /**
     * 查询分润数据库的数据信息
     * 
     * @param startCode
     * @param endCode
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getAgencyFrun")
    @ResponseBody
    public Map<String, Object> getAgency(@RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, HttpServletRequest request) {
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int totalCount = this.terminalManageService.getTerminalFrunCount(startCode, endCode).intValue();

        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List list = this.terminalManageService.getTerminalFrun(startCode, endCode, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    @RequestMapping(params = { "method=terminalImport" })
    @ResponseBody
    public String terminalImport(@RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode, @RequestParam("isTaFlag") String isTaFlag,
            @RequestParam("systemSource") String systemSource, @RequestParam("terminalTypeId") String terminalTypeId, HttpServletRequest req, HttpServletResponse res) throws IOException {

        String agencyId = req.getSession().getAttribute("agencyId") == null ? "" : (String) req.getSession().getAttribute("agencyId");
        String userId = req.getSession().getAttribute("userId") == null ? "" : (String) req.getSession().getAttribute("userId");
        String createDt = CommonDate.getDate();

        int num = 0;
        if ("1".equals(isTaFlag)) {
            num = this.terminalManageService.addTaCardTerminalManage(startCode, endCode, systemSource, terminalTypeId, createDt, agencyId, userId);
        } else {
            num = this.terminalManageService.addTerminalManage(startCode, endCode, systemSource, terminalTypeId, createDt, agencyId, userId);
        }

        String str = num > 0 ? "导入成功，共" + num + "条" : "导入失败";
        Gson gson = new Gson();
        String temps = gson.toJson(str);
        res.setContentType("text/xml;charset=UTF-8");
        res.setHeader("Cache-Control", "no-cache");
        res.getWriter().println(temps);
        return null;
    }

    /**
     * 查询终端的交易
     * 
     * @param Ids
     * @param request
     * @return
     */
    @RequestMapping(params = { "method=terminalDeal" })
    @ResponseBody
    public AjaxReturnInfo terminalDeal(@RequestParam("Ids") String Ids, @RequestParam("flag") String flag, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        Ids = Ids.substring(0, Ids.length() - 1);
        AjaxReturnInfo ajaxInfo = AjaxReturnInfo.success("成功");
        String result = "1";
        StringBuffer sb = new StringBuffer();
        List<TerminalManageBean> list = terminalManageService.queryDealTermianl(agencyId, Ids, flag);
        if (list.isEmpty()) {
            result = "1";
        } else {
            result = "2";
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                TerminalManageBean terminalManageBean = (TerminalManageBean) iterator.next();
                sb.append(terminalManageBean.getOnlyCode()).append(",");
                terminalManageBean = null;
            }
        }
        ajaxInfo.add("flag", result);
        ajaxInfo.add("terminals", sb.toString());
        return ajaxInfo;
    }

    @RequestMapping(params = { "method=terminalsDeal" })
    @ResponseBody
    public AjaxReturnInfo terminalsDeal(@RequestParam("startTerminal") String startTerminal, @RequestParam("endTerminal") String endTerminal, @RequestParam("systemId") String systemId,
            @RequestParam("flag") String flag, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        AjaxReturnInfo ajaxInfo = AjaxReturnInfo.success("成功");
        String result = "1";
        StringBuffer sb = new StringBuffer();

        if ("".equals(startTerminal)) {
            startTerminal = "0";
        }
        if ("".equals(endTerminal)) {
            endTerminal = "0";
        }
        if (startTerminal.length() >= 15) {
            startTerminal = startTerminal + "0";
        }
        if (endTerminal.length() >= 15) {
            endTerminal = endTerminal + "9";
        }

        List<TerminalManageBean> list = terminalManageService.queryDealTermianl(agencyId, startTerminal, endTerminal, flag, systemId);
        if (list.isEmpty()) {
            result = "1";
        } else {
            result = "2";
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                TerminalManageBean terminalManageBean = (TerminalManageBean) iterator.next();
                sb.append(terminalManageBean.getOnlyCode()).append(",");
                terminalManageBean = null;
            }
        }
        ajaxInfo.add("flag", result);
        ajaxInfo.add("terminals", sb.toString());
        return ajaxInfo;
    }

    /**
     * 方法名： getTerminalManageStatusByAgencyId.<br/>
     * 方法作用:查询指定机构号的终端.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:02:51.<br/>
     * 参数者异常：@param request 参数者异常：@return .<br/>
     * 返回值： Map<String,Object>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = { "method=getTerminalManageStatusByAgencyId" })
    @ResponseBody
    public Map<String, Object> getTerminalManageStatusByAgencyId(HttpServletRequest request) {
        String[] strings = request.getParameterValues("agencyId[]");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        List<TerminalCountBean> terminalCountList = null;
        int totalCount;
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        if (strings == null) {
            totalCount = this.terminalManageService.getTerminalCount().intValue();
            int start = (pagenumber - 1) * rownumber;
            int end = start + rownumber > totalCount ? totalCount : start + rownumber;
            List<String> stringList = terminalManageService.getTerminalInfo(start, end);
            if (stringList != null && !stringList.isEmpty()) {
                StringBuffer sBuffer = new StringBuffer();
                for (int i = 0; i < stringList.size(); i++) {
                    sBuffer.append("'" + stringList.get(i) + "',");
                }
                sBuffer.deleteCharAt(sBuffer.length() - 1);
                terminalCountList = terminalManageService.getTerminalList(sBuffer.toString(), startDate, endDate);
            }
        } else {
            totalCount = strings.length;
            if ((pagenumber - 1) * rownumber < totalCount) {
                StringBuffer sBuffer = new StringBuffer();
                for (int i = (pagenumber - 1) * rownumber; i < (totalCount > pagenumber * rownumber ? pagenumber * rownumber : totalCount); i++) {
                    sBuffer.append("'" + strings[i] + "',");
                }
                sBuffer.deleteCharAt(sBuffer.length() - 1);
                terminalCountList = terminalManageService.getTerminalList(sBuffer.toString(), startDate, endDate);
            }

        }
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute("userId").toString();
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        String systemId = request.getSession().getAttribute("systemid").toString();
        this.systemLogService.addLog(ipAddress, agencyId, userId, "终端数量查询", "4", strings.toString());
        return AjaxReturnInfo.setTable(totalCount, terminalCountList);
    }

    /**
     * 方法名： terminalExport.<br/>
     * 方法作用:终端下载.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:04:37.<br/>
     * 参数者异常：@param request 参数者异常：@param response 参数者异常：@throws Exception .<br/>
     * 返回值： void.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = { "method=terminalCountExport" })
    public void terminalExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] agencyIdS = request.getParameter("agencyId").split(",");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (agencyIdS == null || agencyIdS.length == 0) {
            throw new Exception("必要参数丢失");
        }
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < agencyIdS.length; i++) {
            sBuffer.append("'" + agencyIdS[i] + "',");
        }
        sBuffer.deleteCharAt(sBuffer.length() - 1);
        List<TerminalCountBean> terminalCountList = terminalManageService.getTerminalList(sBuffer.toString(), startDate, endDate);
        String webRoot = getClass().getClassLoader().getResource("/").getPath();
        String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
        File f = new File(urls);
        if (!f.exists()) {
            f.mkdirs();
        }

        String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + "终端数量.xls";
        File wj = new File(url);
        WritableWorkbook wwb = Workbook.createWorkbook(wj);
        int sheetNum = 0;
        int i = 0;
        WritableSheet ws = wwb.createSheet("terminalCount" + sheetNum, sheetNum);
        for (int j = 0; i < ConstantUtils.TERMINALINFO.length; j++) {
            ws.addCell(new Label(j, 0, ConstantUtils.TERMINALINFO[i]));
            i++;
        }
        for (int j = 0; j < terminalCountList.size(); j++) {
            int row = 0;
            TerminalCountBean terminalCountBean = (TerminalCountBean) terminalCountList.get(j);
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getAgencyId()));
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getAgencyName()));
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getSelfCount()));
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getSelfCountActivity()));
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getChildCount()));
            ws.addCell(new Label(row++, j + 1, terminalCountBean.getChildCountActivity()));
            if (j / 60000 == sheetNum + 1) {
                sheetNum++;
                ws = null;
                ws = wwb.createSheet("terminalCount" + sheetNum, sheetNum);
            }
        }
        wwb.write();
        wwb.close();
        try {
            // this.systemLogService.addLog(ipAddress, agencyIdS, userIdS,
            // "终端下发", "10", null);
            AgencyController agencyController = new AgencyController();
            agencyController.download(request, response, url, "终端数量.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 方法名： queryPayterminalCount(终端绑定查询).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月5日.<br/>
     * 创建时间：下午2:12:26.<br/>
     * 参数者异常：@param systemId 参数者异常：@param startCode 参数者异常：@param endCode
     * 参数者异常：@param request 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    @RequestMapping(params = { "method=queryPayterminalCount" })
    @ResponseBody
    public AjaxReturnInfo queryPayterminalCount(@RequestParam("systemId") String systemId, @RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode,
            HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        AjaxReturnInfo ajaxReturnInfo = null;
        String start = startCode.trim().length() > 15 ? startCode.trim().substring(0, 15) : startCode.trim();
        String end = endCode.trim().length() > 15 ? endCode.trim().substring(0, 15) : endCode.trim();
        int count = this.terminalManageService.queryPayterminalCount(agencyId, systemId, start, end);
        if (count > 0) {
            String psamId = this.terminalManageService.queryMinPayterminal(agencyId, systemId, start, end);
            String result = "您指定的号段有" + count + "台瑞通宝无法下发，第一台不能下发的终端编号是" + psamId;
            ajaxReturnInfo = AjaxReturnInfo.failed(result);
        } else {
            ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.agajx.save.success"));
        }
        return ajaxReturnInfo;
    }

    /**
     * 
     * 【方法名】 : (来源系统变更查询). <br/>
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:29:22 .<br/>
     * 【参数】： .<br/>
     * 
     * @param systemId
     *            来源系统编号
     * @param startCode
     *            开始编号
     * @param endCode
     *            结束编号
     * @param request
     *            request
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 简思文 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=queryTerminalSystem" })
    @ResponseBody
    public Map<String, Object> queryTerminalSystem(@RequestParam("systemId") String systemId, @RequestParam("startCode") String startCode, @RequestParam("endCode") String endCode,
            HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        systemId = "-1".equals(systemId) ? null : systemId.trim();
        startCode = startCode.length() > 15 ? startCode.substring(0, 15) : startCode.trim();
        endCode = startCode.length() > 15 ? endCode.substring(0, 15) : endCode.trim();
        if (!agencyId.equals(ConstantUtils.CENTERCODE)) {
            return AjaxReturnInfo.setTable(null, null);
        }
        int count = this.terminalManageService.queryTerminalSystemCount(agencyId, systemId, startCode, endCode);
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > count ? count : start + rownumber;
        List<TerminalManageBean> terminalList = this.terminalManageService.queryTerminalSystem(agencyId, systemId, startCode, endCode, start, end);

        return AjaxReturnInfo.setTable(count, terminalList);
    }

    /**
     * 
     * 【方法名】 : (终端来源系统变更). <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:28:35 .<br/>
     * 【参数】： .<br/>
     * 
     * @param systemId
     *            来源系统编号
     * @param systemSource
     *            要变更到的来源系统
     * @param startCode
     *            开始编号
     * @param endCode
     *            结束编号
     * @param request
     *            request
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 简思文 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=updateSystemStatus" })
    @ResponseBody
    public AjaxReturnInfo updateSystemStatus(@RequestParam("systemId") String systemId, @RequestParam("systemSource") String systemSource, @RequestParam("startCode") String startCode,
            @RequestParam("endCode") String endCode, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        systemId = "-1".equals(systemId) ? "" : systemId.trim();
        startCode = startCode.length() > 15 ? startCode.substring(0, 15) : startCode.trim();
        endCode = startCode.length() > 15 ? endCode.substring(0, 15) : endCode.trim();
        AjaxReturnInfo ajaxReturnInfo = null;
        try {
            int count = this.terminalManageService.updateSystemStatus(agencyId, systemId, systemSource, startCode, endCode);
            if (count > 0) {
                ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));
            } else {
                ajaxReturnInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.updateSystemStatus.error"));
            }
        } catch (Exception e) {
            log.error("变更异常：" + systemSource);
            ajaxReturnInfo = AjaxReturnInfo.failed(e.getMessage());
        }
        return ajaxReturnInfo;
    }

    /**
     * 【方法名】 : saveTerminalCallBack(回拨). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:45:50 .<br/>
     * 【参数】： .<br/>
     * .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     * <p/>
     */
    @RequestMapping(params = { "method=saveTerminalCallBack" })
    @ResponseBody
    public AjaxReturnInfo saveTerminalCallBack(@RequestParam("terminalCodes") String terminalIds, @RequestParam("oldAgencyId") String oldAgencyId, HttpServletRequest request) {
        int result = this.terminalManageService.checkTerminalExist(terminalIds.substring(0, terminalIds.length() - 1), oldAgencyId);
        String[] terminalCodeStr = terminalIds.split(",");
        log.info("查询当前终端是否存在开始");
        if(result == 0){
            return AjaxReturnInfo.failed("count");
        }
        String userId = request.getSession().getAttribute("userId").toString();
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        this.terminalManageService.saveTerminalCallBack(terminalIds.substring(0, terminalIds.length() - 1), agencyId, userId, oldAgencyId);

        TerminalMsgBean beanLower = (TerminalMsgBean) this.terminalManageService.selectAgency(oldAgencyId);

        this.terminalManageService.sendMsg(beanLower.getCompanyPhone(), oldAgencyId, beanLower.getOnlineChannel(), terminalCodeStr.length);

        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));

        return ajaxReturnInfo;

    }

    /**
     * 
     * 【方法名】 : (查询当前用户类型). <br/>
     * 【作者】: 张翔宇 .<br/>
     * 【时间】： 2017年5月25日 上午11:00:46 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     *            .<br/>
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张翔宇 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=checkSuperAdmin" })
    @ResponseBody
    public AjaxReturnInfo checkSuperAdmin(HttpServletRequest request) {
        String roletypeId = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        return AjaxReturnInfo.success(roletypeId);
    }

    /**
     * 【方法名】 : 终端回拨审核记录表. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月17日 下午4:18:18 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=getTerminalBack" })
    @ResponseBody
    public Map<String, Object> getTerminalBack(@RequestParam("beginTerminalId") String beginTerminalId, @RequestParam("endTerminaId") String endTerminaId,
            @RequestParam("terminalTypeId") String terminalTypeId, @RequestParam("yearmonthdatestart") String yearmonthdatestart, @RequestParam("yearmonthdateend") String yearmonthdateend,
            @RequestParam("checkDateStart") String checkDateStart, @RequestParam("checkDateEnd") String checkDateEnd, HttpServletRequest request) {
        log.info("终端回拨审核");
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        if ("-1".equals(terminalTypeId)) {
            terminalTypeId = null;
        }
        TerminalBack terminalBack = new TerminalBack();
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        terminalBack.setAgencyId(agencyId);
        terminalBack.setTerminalTypeId(terminalTypeId);
        terminalBack.setBeginTerminalId(beginTerminalId);
        terminalBack.setEndTerminaId(endTerminaId);
        terminalBack.setCreateDateStart(yearmonthdatestart);
        terminalBack.setCreateDateEnd(yearmonthdateend);
        terminalBack.setCheckDateStart(checkDateStart);
        terminalBack.setCheckDateEnd(checkDateEnd);
        int totalCount = this.terminalManageService.getTerminalBackAllCount(terminalBack).intValue();
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber > totalCount ? totalCount : start + rownumber;
        List<TerminalBack> list = this.terminalManageService.getTerminalBackAll(terminalBack, start, end);
        return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
    }

    /**
     * 【方法名】 : 保存审核意见. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月17日 下午7:51:24 .<br/>
     * 【参数】： .<br/>
     * .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     * <p/>
     */
    @RequestMapping(params = { "method=saveTerminalSuggest" })
    @ResponseBody
    public AjaxReturnInfo saveTerminalSuggest(@RequestParam("batchNos") String batchNos, @RequestParam("oldAgencyId") String oldAgencyId, @RequestParam("reasonDesc") String reasonDesc,
            @RequestParam("beginTerminalIds") String beginTerminalIds, @RequestParam("endTerminalIds") String endTerminalIds, HttpServletRequest request) {
        log.info("保存回拨审核意见");
        TerminalBack terminalBack = new TerminalBack();
        String parentAgencyid = request.getSession().getAttribute("parentagencyId").toString().trim();
        terminalBack.setOldAgencyId(oldAgencyId);
        terminalBack.setCheckId(request.getSession().getAttribute("userId").toString());
        terminalBack.setCheckDt(CommonDate.getDate());
        terminalBack.setStatus(reasonDesc);
        terminalBack.setAgencyId(parentAgencyid);
        terminalBack.setEndTerminaId(endTerminalIds.substring(0, endTerminalIds.length() - 1));

        terminalBack.setBeginTerminalId(beginTerminalIds.substring(0, beginTerminalIds.length() - 1));
        int countAll = this.terminalManageService.countAllBack(terminalBack);

        int countOfSuccess = this.terminalManageService.saveTerminalSuggest(terminalBack, batchNos.substring(0, batchNos.length() - 1), beginTerminalIds.substring(0, beginTerminalIds.length() - 1),
                endTerminalIds.substring(0, endTerminalIds.length() - 1));

        if ("1".equals(reasonDesc) && 0 != countOfSuccess) {
            log.info("同意回拨终端且受影响的行数为：" + countOfSuccess);
            this.terminalManageService.addTeminalCheck(terminalBack, batchNos.substring(0, batchNos.length() - 1), beginTerminalIds.substring(0, beginTerminalIds.length() - 1));
        }
        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));
        ajaxReturnInfo.add("countOfSuccess", countOfSuccess);
        ajaxReturnInfo.add("countOfFail", countAll - countOfSuccess);
        return ajaxReturnInfo;
    }

    /**
     * 【方法名】 : 终端批量回拨. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月19日 下午2:34:08 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=saveTerminalBackMore" })
    @ResponseBody
    public AjaxReturnInfo getTerminalbackMore(@RequestParam("terminalCode") String terminalCode, @RequestParam("terminalCodeEnd") String terminalCodeEnd, @RequestParam("oldAgencyId") String agencyId,
            @RequestParam("terminaltypeId") String terminaltypeId, HttpServletRequest request) {
        AjaxReturnInfo ajaxReturnInfo = null;
        log.info("判断终端是否存在");
        int result = terminalManageService.checkTerminalExist(terminalCode, terminalCodeEnd, agencyId);
        if (result == 0) {
            return AjaxReturnInfo.failed("countStart");
        }
        log.info("终端批量回拨");
        String userId = request.getSession().getAttribute("userId").toString();
        String oldAgencyId = request.getSession().getAttribute("agencyId").toString();
        this.terminalManageService.saveTerminalBackMore(terminalCode, terminalCodeEnd, agencyId, oldAgencyId, userId, terminaltypeId);
        TerminalMsgBean beanLower = (TerminalMsgBean) this.terminalManageService.selectAgency(agencyId);
        TerminalBack terminalBack = new TerminalBack();
        terminalBack.setBeginTerminalId(terminalCode);
        terminalBack.setEndTerminaId(terminalCodeEnd);
        terminalBack.setAgencyId(agencyId);
        terminalBack.setOldAgencyId(agencyId);
        
        int backCount = this.terminalManageService.countAllBack(terminalBack);

        this.terminalManageService.sendMsg(beanLower.getCompanyPhone(), agencyId, oldAgencyId, backCount);

        ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));
        return ajaxReturnInfo;
    }

    /**
     * 【方法名】 : 转至批量回拨详情页. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 下午1:40:23 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     *            .<br/>
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=getListTerminalBack" })
    @ResponseBody
    public ModelAndView getListTerminalBack(HttpServletRequest request) {
        return new ModelAndView("terminalmanage/terminalBackExamine");
    }

    /**
     * 【方法名】 : (获取用户未审核终端回拨请求数量). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 下午2:25:04 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     *            .<br/>
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=getNoCheckTerminalBackCount" })
    @ResponseBody
    public AjaxReturnInfo getNoCheckTerminalBackCount(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute("agencyId").toString();
        int totalCount = this.terminalManageService.getNoCheckTerminalBackCount(agencyId).intValue();
        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));
        ajaxReturnInfo.add("unCheckCount", totalCount);
        return ajaxReturnInfo;
    }

    /**
     * 【方法名】 : (查询批次详情). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月3日 下午7:37:14 .<br/>
     * 【参数】： .<br/>
     * 
     * @param batchNo
     *            批次号
     * @param request
     *            HttpServletRequest
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = { "method=getTerminalChangeDetail" })
    @ResponseBody
    public Map<String, Object> getTerminalChangeDetail(@RequestParam("batchNo") String batchNo, HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            String userId = request.getSession().getAttribute("userId").toString();
            String agencyId = request.getSession().getAttribute("agencyId") == null ? "" : (String) request.getSession().getAttribute("agencyId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("batchNo", batchNo);
            map.put("agencyId", agencyId);
            String rows = request.getParameter("rows");
            String page = request.getParameter("page");
            int totalCount = this.terminalManageService.selectPageCountByParam(map);
            int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
            int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
            int start = (pagenumber - 1) * rownumber;
            int end = start + rownumber > totalCount ? totalCount : start + rownumber;
            map.put("start", start);
            map.put("end", end);
            this.systemLogService.addLog(ipAddress, agencyId, userId, "终端回拨详情", "4", batchNo);
            List<TerminalBack> list = terminalManageService.selectPageByParam(map);
            return AjaxReturnInfo.setTable(Integer.valueOf(totalCount), list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
