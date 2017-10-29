package com.compass.agency.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.AgentreeBean;
import com.compass.agency.model.SpecSystemBean;
import com.compass.agency.model.TreeNodesBean;
import com.compass.agency.service.AgencyApproveService;
import com.compass.agency.service.AgencyService;
import com.compass.authority.service.AuthorityService;
import com.compass.constans.InterfaceNameConstans;
import com.compass.paramater.model.RtbParamter;
import com.compass.paramater.service.RtbParamterService;
import com.compass.role.service.RoleService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.userrole.model.UserAddInfo;
import com.compass.userrole.model.UserRoleBean;
import com.compass.userrole.service.UserRoleService;
import com.compass.users.model.UserBean;
import com.compass.users.service.UsersService;
import com.compass.utils.CommonDate;
import com.compass.utils.CommonEnums.AgencyStatus;
import com.compass.utils.ConstantUtils;
import com.compass.utils.FormatChar;
import com.compass.utils.MD5;
import com.compass.utils.Tools;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.utils.mvc.I18nUtils;
import com.exception.QTException;

@Controller
@RequestMapping("/agency/agency.do")
public class AgencyController {
    @Autowired
    @Qualifier("agencyService")
    private AgencyService        agencyService;
    @Autowired
    @Qualifier("userRoleService")
    private UserRoleService      userRoleService;
    @Autowired
    @Qualifier("roleService")
    private RoleService          roleService;
    @Autowired
    @Qualifier("systemManageService")
    private SystemManageService  systemManageService;
    // 系统日志service
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService     systemLogService;
    /**
     * 配置参数
     */
    @Autowired
    @Qualifier("authorityService")
    private AuthorityService     authorityService;

    /**
     * 认证接口
     */
    @Autowired
    @Qualifier("agencyApproveService")
    private AgencyApproveService agencyApproveService;
    
    @Autowired
    @Qualifier("rtbParamterService")
    private RtbParamterService rtbParamterService;
    
    
//    @Autowired
//    @Qualifier("payCustomerService")
//    private PayCustomerService payCustomerService;
    
    
    @Autowired
    @Qualifier("usersService")
    private UsersService usersService;

    private static final Logger  LogPay = LoggerFactory.getLogger(AgencyController.class);

    /**
     * 获取机构信息
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getAgency")
    @ResponseBody
    public Map<String, Object> getAgency(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "companyName") String companyName,
            @RequestParam(value = "agencyStatus") String status, @RequestParam(value = "certificationStatus") String certificationStatus, @RequestParam(value = "provinceId") String provinceId, @RequestParam(value = "cityId") String cityId, HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getRemoteAddr();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        String systemId = req.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
        String roletypeid = req.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        String depagencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
        String parentagencyId = req.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
        String upperAgencyid = parentagencyId;
        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        // if(systemId.equals("HTXT")&&!parentagencyId.equals(ConstantUtils.CENTERCODE)){
        SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyIdS);
        if (null != spec) {
            if (parentagencyId.equals(ConstantUtils.CENTERCODE) && "1".equals(spec.getLevelControl())) {
                parentagencyId = "";
            } else if (agencyCount > 0 && "1".equals(spec.getLevelControl())) {
                parentagencyId = "";
            } else {
                upperAgencyid = "";
            }
        } else {
            parentagencyId = "";
        }

        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        AgencyBean agency = new AgencyBean();
        agency.setSystemId(systemId);
        agency.setDepagencyId(depagencyId);
        agency.setAgency_id(agencyId);
        agency.setCompanyName(companyName);
        agency.setStatus(status);
        agency.setAgency_status(certificationStatus);
        agency.setOnlinechannel(parentagencyId);
        agency.setUpperAgencyid(upperAgencyid);
        agency.setProvinceId(provinceId);
        agency.setCityId(cityId);

        Integer count = agencyService.getAgencyCount(agency);
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > count ? count : start + rownumber;
        agency.setStart(start);
        agency.setEnd(end);
        List<AgencyBean> list = agencyService.getAgencyAll(agency);
        if (list != null) {
            for (AgencyBean agencyBean : list) {
                if (!agencyIdS.equals(agencyBean.getAgency_id())) {
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getContactsName())) {
                        agencyBean.setContactsName(Tools.handleCustomerName(agencyBean.getContactsName()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyPhone())) {
                        agencyBean.setCompanyPhone(FormatChar.mosaic(agencyBean.getCompanyPhone()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyEmail())) {
                        agencyBean.setCompanyEmail(FormatChar.mosaic(agencyBean.getCompanyEmail()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getUserpId())) {
                        agencyBean.setUserpId(FormatChar.IDCard(agencyBean.getUserpId()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getSomeonePhone())) {
                        agencyBean.setSomeonePhone(FormatChar.mosaic(agencyBean.getSomeonePhone()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getSomeoneName())) {
                        agencyBean.setSomeoneName(Tools.handleCustomerName(agencyBean.getSomeoneName()));
                    }
                }
            }
        }

        /*
         * List<AgencyBean> menulist = new ArrayList<AgencyBean>(); int
         * pagenumber = Integer.parseInt((page == null || page == "0") ? "1" :
         * page); int rownumber = Integer.parseInt((rows == "0" || rows == null)
         * ? "20" : rows); int start = (pagenumber - 1) * rownumber; int end =
         * (start + rownumber) > list.size() ? list.size() : start + rownumber;
         * if (list != null && list.size() > 0) { for (int i = start; i < end;
         * i++) { menulist.add(list.get(i)); } }
         */

        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + companyName + status;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEAGENCY, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(count, list);
    }

    /**
     * 机构审核信息查询
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getAgencyCheck")
    @ResponseBody
    public Map<String, Object> getAgencyCheck(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "companyName") String companyName,
            @RequestParam(value = "agencyStatus") String agencyStatus, HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getRemoteAddr();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        String systemId = req.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
        String depagencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
        String roletypeid = req.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();

        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        AgencyBean agency = new AgencyBean();
        agency.setSystemId(systemId);
        agency.setDepagencyId(depagencyId);
        agency.setAgency_id(agencyId);
        agency.setCompanyName(companyName);
        agency.setAgency_status(agencyStatus);
        List<AgencyBean> list = agencyService.getAgencyCheck(agency);
        if (list != null) {
            for (AgencyBean agencyBean : list) {
                if (!agencyIdS.equals(agencyBean.getAgency_id())) {
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyPhone())) {
                        agencyBean.setCompanyPhone(FormatChar.mosaic(agencyBean.getCompanyPhone()));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyEmail())) {
                        agencyBean.setCompanyEmail(FormatChar.mosaic(agencyBean.getCompanyEmail()));
                    }

                }
            }
        }
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<AgencyBean> menulist = new ArrayList<AgencyBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                menulist.add(list.get(i));
            }
        }

        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + agencyId + companyName + agencyStatus;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEAGENCYCHECK, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), menulist);
    }

    /**
     * 获取当前机构下的所有机构
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getAgencyTree")
    @ResponseBody
    public List<Map<String, Object>> getAgencyTree(HttpServletRequest request, HttpServletResponse response) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String parentagencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
        String flag = request.getParameter("flag");
        String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        String flag1 = ""; // 不为空表示本机构及直属下级机构
        String flag2 = "2"; // 不为空表示本机构及下级所有机构
        SpecSystemBean check = systemManageService.checkSpecSystem(systemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);
        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        if (ConstantUtils.CENTERSYSTEMID.equals(systemId.trim())) {
            systemId = "";
        }
        /*
         * if(flag!=null){ systemId=""; agencyId=ConstantUtils.CENTERCODE;
         * parentagencyId="-1"; }
         */
        boolean flagAgency = false;
        // 特别来源系统处理，如果为特别来源系统并且是二级代理商或以下机构，则仅显示本机构和直属下级机构

        if (null != check) {
            flagAgency = true;
            flag1 = "1";
            flag2 = "";
            if ("0".equals(check.getLevelControl())) {
                flagAgency = false;
                flag1 = ""; // 不为空表示本机构及直属下级机构
                flag2 = "2"; // 不为空表示本机构及下级所有机构

            } else if ("1".equals(check.getLevelControl())) {
                if (parentagencyId.equals(ConstantUtils.CENTERCODE)) {
                    flagAgency = false;// true 则查询直属
                    flag1 = ""; // 不为空表示本机构及直属下级机构
                    flag2 = "2"; // 不为空表示本机构及下级所有机构
                } else if (agencyCount > 0) {
                    flagAgency = false;// true 则查询直属
                    flag1 = ""; // 不为空表示本机构及直属下级机构
                    flag2 = "2"; // 不为空表示本机构及下级所有机构
                } else {
                    flagAgency = true;// true 则查询直属
                    flag1 = "1"; // 不为空表示本机构及直属下级机构
                    flag2 = ""; // 不为空表示本机构及下级所有机构
                }
            }
            if ("profit".equals(flag)) {
                flagAgency = true;
                flag1 = "1";
                flag2 = "";
            }
        } else {

            if ("profit".equals(flag)) {
                flagAgency = true;
                flag1 = "1";
                flag2 = "";
            }
        }
        List<AgentreeBean> list = this.agencyService.getAgencyTree(agencyId, systemId, flag1, flag2);
        List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> clist = createAgencyTree(list, parentagencyId, systemId, agencyId, flagAgency, 1);
        map.put("id", "-1");
        map.put("text", "请选择机构");
        map.put("state", "open");
        map.put("children", clist);
        resultlist.add(map);

        return resultlist;
        // response.getWriter().print(jsonArray.toString());

    }

    @RequestMapping(params = "method=getAgencyTreebyStemp")
    @ResponseBody
    public List<Map<String, Object>> getAgencyTreebyStemp(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        List<Map<String, Object>> list = agencyService.getAgencyone(agencyId);
        List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", list.get(i).get("AGENCYID"));
            map.put("text", list.get(i).get("AGENCYNAME"));
            map.put("state", "open");
            clist.add(map);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // List<Map<String,Object>> clist=createAgencyTree(list,parentagencyId);

        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择机构");
        map.put("state", "open");
        map.put("children", clist);
        resultlist.add(map);

        return resultlist;
    }

    /**
     * 生成下拉树
     * 
     * @param agencylist
     * @param parentagencyId
     * @return
     */
    public List<Map<String, Object>> createAgencyTree(List<AgentreeBean> agencylist, String parentagencyId, String systemid, String agencyId, boolean flag, int num) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        num++; // 级别控制
        for (int i = 0; i < agencylist.size(); i++) {
            AgentreeBean agency = agencylist.get(i);
            if (!agency.getIsParent().equals(parentagencyId))
                continue;
            map = new HashMap<String, Object>();
            map.put("id", agency.getAgencyId());
            map.put("text", agency.getAgencyName());
            list.add(map);

            if (((num == 3) && (flag))) {
                continue;
            }

            if (((AgentreeBean) agencylist.get(i)).getIsChild().equals("1")) {
                map.put("state", "closed");
                map.put("children", createAgencyTree(agencylist, agency.getAgencyId(), systemid, agencyId, flag, num));
            }
        }
        return list;
    }

    /**
     * 添加机构
     * 
     * @param agencyid
     * @param agencyname
     * @param uppercompanyId
     * @param vestagencyId
     * @param contactsName
     * @param companyPhone
     * @param companyEmail
     * @param companyAddr
     * @param certificate
     * @param legalinfo
     * @param organizationCode
     * @param agreementcode
     * @param provinceId
     * @param cityId
     * @param agencystatus
     * @param accountBank
     * @param bankId
     * @param accountName
     * @param bankcode
     * @param request
     * @return
     * @throws Exception
     * @throws QTException
     */
    @RequestMapping(params = "method=addAgency")
    @ResponseBody
    public AjaxReturnInfo addAgency(
            @RequestParam(value = "systemId") String systemId,
            @RequestParam(value = "agencyid") String agencyid,
            @RequestParam(value = "agencyname") String agencyname,
            // @RequestParam(value = "agencybriefname") String agencybriefname,
            @RequestParam(value = "uppercompanyId") String uppercompanyId, @RequestParam(value = "vestagencyId") String vestagencyId, @RequestParam(value = "contactsName") String contactsName,
            @RequestParam(value = "companyPhone") String companyPhone, @RequestParam(value = "companyEmail") String companyEmail, @RequestParam(value = "companyAddr") String companyAddr,
            @RequestParam(value = "certificate") String certificate, @RequestParam(value = "legalinfo") String legalinfo, @RequestParam(value = "organizationCode") String organizationCode,
            @RequestParam(value = "agreementcode") String agreementcode, @RequestParam(value = "provinceId") String provinceId, @RequestParam(value = "cityId") String cityId,
            @RequestParam(value = "accountBank") String accountBank, @RequestParam(value = "bankId") String bankId, @RequestParam(value = "accountName") String accountName,
            @RequestParam(value = "bankcode") String bankcode, @RequestParam(value = "agencyType") String agencyType, String isDtbUser, String dtbProfitFlag, @RequestParam(value = "userpId") String userpId, String someoneName,String someonePhone, HttpServletRequest request){
        String createId = (request.getSession().getAttribute(ConstantUtils.USERID) == null) ? "" : request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String createDt = CommonDate.getDate();
        String sagid = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
        AjaxReturnInfo ajaxinfo;
        try {

            // /vestagencyId = agencyid.substring(0, 4) +
            // ConstantUtils.AGENCYSTARTVALUE;
            String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
            String sysId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();// 获取Session中的系统编号
            String agencystatus = ConstantUtils.AGENCY_PS; // 表示机构状态为审核通过

            // 获得ip
            String ipAddress = request.getRemoteAddr();
            String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
            String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

            /*
             * 不再限制层级 20141110 String level =
             * agencyService.getAgencyLevel(ConstantUtils.CENTERCODE, sagid);
             * if(level.equals("6")){ AjaxReturnInfo ajaxinfo=null;
             * ajaxinfo=AjaxReturnInfo.failed("已经是末机构，不能添加新机构！"); return
             * ajaxinfo; }
             */
            if (ConstantUtils.ROLETYPEOPERATOR.equals(roletypeid.trim())) {// 3
                // 表示该用户是操作员
                agencystatus = ConstantUtils.AGENCY_PD; // 表示机构状态为待审核
            }
            if (!ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {// 如果不为超级管理员的
                systemId = sysId;
            }
            int exit = 0;
            int result = 0;
            if (ConstantUtils.CENTERCODE.equals(sagid)) {
                agencyid = ConstantUtils.RTBPREFIX + agencyid + ConstantUtils.AGENCYSTARTVALUE;
                exit = agencyService.existAgency(agencyid);
            } else {
                agencyid = agencyid.substring(0, 4);
                String agid = agencyService.getgencyId(agencyid);
                agencyid = agencyid + agid;
            }
            
         // /解决手动切换机构的归属机构时，顶级机构vestagencyId不变的问题
            if (ConstantUtils.CENTERCODE.equals(sagid)) {
                vestagencyId = agencyid.substring(0, 7) + ConstantUtils.AGENCYSTARTVALUE;
            } else {
                if (agencyService.getVestagencyByAgencyid(sagid) == null) {
                    throw new QTException("归属机构不能为空!");
                } else {
                    vestagencyId = agencyService.getVestagencyByAgencyid(sagid);
                }

            }
            
            ajaxinfo = AjaxReturnInfo.failed("保存失败");
            agencyid = agencyid.toUpperCase();
            int count = agencyService.queryCompanyNamelCount(agencyname);
            if (count > 0) {
                ajaxinfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.companyName.failed"));
            } else {
                if (exit == 0) {
                    AgencyBean agency = new AgencyBean();
                    agency.setSystemId(systemId);
                    agency.setAgency_id(agencyid);
                    agency.setCompanyName(agencyname);
                    // agency.setCompanyBriefName(agencybriefname);
                    agency.setUpperAgencyid(uppercompanyId);
                    agency.setVestagencyId(vestagencyId);
                    agency.setContactsName(contactsName);
                    agency.setCompanyPhone(companyPhone);
                    agency.setCompanyEmail(companyEmail);
                    agency.setCompanyAddr(companyAddr);
                    agency.setCertificate(certificate);
                    agency.setLegal_info(legalinfo);
                    agency.setOrganizationCode(organizationCode);
                    agency.setAgreementcode(agreementcode);
                    agency.setProvinceId(provinceId);
                    agency.setCityId(cityId);
                    // 1:机构待审核  2:机构审核通过但未实名认证  3:机构审核通过且实名认证通过
                    if (ConstantUtils.CENTERCODE.equals(sagid)) {
                        agency.setAgency_status(AgencyStatus.realNamePass.getVal());
                    } else {
                        agency.setAgency_status(AgencyStatus.auditPass.getVal());
                    }
                    agency.setAccountBank(accountBank);
                    agency.setBankId(bankId);
                    agency.setAccountName(accountName);
                    agency.setBankcode(bankcode);
                    agency.setCreateId(createId);
                    agency.setCreateDt(createDt);
                    agency.setSomeoneName(someoneName);
                    agency.setSomeonePhone(someonePhone);
                    agency.setUserpId(userpId);
                    
                    // 定制商特别处理（登记定制商归属机构编号）20141117
                    if ("2".equals(agencyType)) {
                        agency.setVestBrandId(agencyid); // 如果是定制商则设置为自己的机构编号
                    } else {
                        // 代理商则取本级机构的定制商归属机构编号为新增机构的
                        AgencyBean parentAgency = new AgencyBean();
                        parentAgency.setAgency_id(sagid);
                        parentAgency.setSystemId(sysId);
                        List<AgencyBean> list = agencyService.getAgency(parentAgency);
                        if (list != null && list.size() > 0) {
                            agency.setVestBrandId(list.get(0).getVestBrandId());
                        }
                    }
                    // 添加操作详情 20141203
                    String operateDetail = "添加机构名为" + agencyname + ",ID为" + agencyid;
                    // 添加进系统日志表 20141203
                    systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEAGENCY, ConstantUtils.OPERTYPEADD, operateDetail);
                    // 如果是顶级机构添加，验证实名
                    if (ConstantUtils.CENTERCODE.equals(sagid)) {
                       
                        if (org.springframework.util.StringUtils.isEmpty(companyPhone)) {
                            throw new QTException(I18nUtils.getResourceValue("message.mobile.empty"));
                        }
                        
                        
//                        PayCustomer payCustomer = null;
//                        AgencyBean bean = agencyService.getAgencyBeanWithMap(companyPhone);
//                        if (payCustomer != null && InterfaceNameConstans.VALUE_3.equals(payCustomer.getCustomertag()) && InterfaceNameConstans.VALUE_5.equals(payCustomer.getCheckrange())) {
//                            agency.setUserpId(payCustomer.getCustomerpid());
//                            agency.setContactsName(payCustomer.getUsername());
//                            //查询Agency是否已经实名
//                        } else if (bean != null && bean.getAgency_status()!=null && bean.getAgency_status().equals(InterfaceNameConstans.VALUE_3)) {
//                            agency.setUserpId(bean.getUserpId());
//                            agency.setContactsName(bean.getContactsName());
//                        }  else {
//                            JSONObject jsonObj = null;
//                            if (!ConstantUtils.MSG_SUCCESS.equals(jsonObj.get(ConstantUtils.MSG_CODE))) {
//                                return AjaxReturnInfo.failed((String) jsonObj.get(ConstantUtils.MSG_TEXT));
//                            }
//                        }
                        agency.setAgency_status(AgencyStatus.realNamePass.getVal());
                        
                    } else {
                        agency.setAgency_status(AgencyStatus.auditPass.getVal());
                    }
                    result = agencyService.AddAgency(agency);

                    if (result > 0) {
                        String userId = userRoleService.getUserId().toString();
                        UserRoleBean user = new UserRoleBean();
                        user.setUserId(userId);
                        user.setUserName(agencyid);
                        user.setLoginName(agencyid);
                        user.setPassword(MD5.MD5(agencyid));
                        user.setEmail(companyEmail);
                        user.setPhone(companyPhone);
                        user.setStatus(ConstantUtils.SYSTEN_IV); // StatusParam.SYSTEN_IV=1
                        // 代表该用户有效
                        user.setAgencyId(agencyid);
                        user.setCreateId(createId);
                        user.setCreateDt(createDt);
                        user.setIsAdmin(ConstantUtils.SYSTEN_IV);//1为管理员
                        user.setHomeId(ConstantUtils.HOME_ID);
                        user.setIsGrab(UserRoleBean.GRAB);

                        // 用户瑞推客标识
                        user.setIsDtbUser(isDtbUser);
                        user.setDtbProfitFlag(dtbProfitFlag);
                        int z = userRoleService.getInsert(user);
                        
                        UserAddInfo info = new UserAddInfo();
                        info.setHomeId(ConstantUtils.HOME_ID);
                        info.setUserid(userId);
                        info.setIsApprove(UserAddInfo.APPROVE);
                        info.setUsercodeTime(CommonDate.currentTimeWithFormat(CommonDate.YYYYMMDDHHMMSS));
                       
                        //查询参数配置信息
                        RtbParamter para = new RtbParamter();
                        para.setStatus(InterfaceNameConstans.VALUE_1);
                        para.setPmNo(InterfaceNameConstans.DAYS);
                        RtbParamter findObj = rtbParamterService.getRtbParamter(para);
                        if (findObj!=null){
                            info.setDays(findObj.getPmValue().intValue());
                        } else {
                            info.setDays(InterfaceNameConstans.DAYS7);
                        }
                        //发送短信通知
//                        agencyService.sendSMS(agencyid, companyPhone, agency.getContactsName());
                        userRoleService.insertUserAddInfo(info);
                        // 添加操作详情 20141203
                        String operateDetail2 = "添加用户ID为" + userId + ",机构ID为" + agencyid;
                        // 添加进系统日志表 20141203
                        systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEUSERROLE, ConstantUtils.OPERTYPEADD, operateDetail2);
                        if (z > 0) {
                            List<Map<String, Object>> maplist = roleService.getRoleId(ConstantUtils.ROLETYPEADMIN);
                            if (maplist.size() > 0) {
                                String roleId = maplist.get(0).get("ROLEID").toString();
                                userRoleService.addUserRole(userId, roleId, createId, createDt);
                                // 添加操作详情 20141203
                                String operateDetail3 = "添加角色的用户ID为" + userId;
                                // 添加进系统日志表 20141203
                                systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEUSERROLE, ConstantUtils.OPERTYPEADD, operateDetail3);

                            }
                        }
                    }
                } else {
                    result = -1;
                }
                if (result > 0) {
                    ajaxinfo = AjaxReturnInfo.success("保存成功");
                } else if (result == -1) {
                    ajaxinfo = AjaxReturnInfo.failed("该机构标识已经被使用");
                } 
            }
        } catch (QTException e) {
            LogPay.error(e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
        } catch (DuplicateKeyException e) {
            LogPay.error(e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agencyId.repeat"));
        }  catch (Exception e) {
            LogPay.error(e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.busy"));
        }

        return ajaxinfo;
    }

    /**
     * 编辑机构
     * 
     * @param agencyid
     * @param agencyname
     * @param vestagencyId
     * @param contactsName
     * @param companyPhone
     * @param companyEmail
     * @param companyAddr
     * @param certificate
     * @param legalinfo
     * @param organizationCode
     * @param agreementcode
     * @param provinceId
     * @param cityId
     * @param agencystatus
     * @param accountBank
     * @param bankId
     * @param accountName
     * @param bankcode
     * @param request
     * @return
     */
    @RequestMapping(params = "method=editAgency")
    @ResponseBody
    public AjaxReturnInfo editAgency(
            @RequestParam(value = "systemId") String systemId,
            @RequestParam(value = "agencyid") String agencyid,
            @RequestParam(value = "agencyname") String agencyname,
            // s@RequestParam(value = "agencybriefname") String agencybriefname,
            @RequestParam(value = "vestagencyId") String vestagencyId, @RequestParam(value = "contactsName") String contactsName, @RequestParam(value = "companyPhone") String companyPhone,
            @RequestParam(value = "companyEmail") String companyEmail, @RequestParam(value = "companyAddr") String companyAddr, @RequestParam(value = "certificate") String certificate,
            @RequestParam(value = "legalinfo") String legalinfo, @RequestParam(value = "organizationCode") String organizationCode, @RequestParam(value = "agreementcode") String agreementcode,
            @RequestParam(value = "provinceId") String provinceId, @RequestParam(value = "cityId") String cityId, @RequestParam(value = "accountBank") String accountBank,
            @RequestParam(value = "bankId") String bankId, @RequestParam(value = "accountName") String accountName, @RequestParam(value = "bankcode") String bankcode,
            @RequestParam(value = "someoneName") String someoneName, @RequestParam(value = "someonePhone") String someonePhone,
            @RequestParam(value = "agencyType") String agencyType, @RequestParam(value = "agency_status") String agency_status, String isDtbUser, String dtbProfitFlag, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        String createId = (request.getSession().getAttribute(ConstantUtils.USERID) == null) ? "" : request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String createDt = CommonDate.getDate();
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        String sysId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();// 获取Session中的系统编号
        if (!ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {// 如果不为超级管理员的
            systemId = sysId;
        }
        AgencyBean agency = new AgencyBean();
        agency.setSystemId(systemId);
        agency.setAgency_id(agencyid);
        // 定制商特别处理，先查询当前机构的信息判断其机构类型 20141117
        boolean changed = false;
        String vestBrand = null;
        List<AgencyBean> list = agencyService.getAgency(agency);
        if (list != null && list.size() != 0) {
            vestBrand = list.get(0).getVestBrandId();
            if (StringUtils.isNotEmpty(vestBrand) && "1".equals(agencyType)) {
                changed = true;
                vestBrand = null;
            } else if (StringUtils.isEmpty(vestBrand) && "2".equals(agencyType)) {
                changed = true;
                vestBrand = agencyid;
            }
        }
        agency.setCompanyName(agencyname);
        // agency.setCompanyBriefName(agencybriefname);
        agency.setVestagencyId(vestagencyId);
        agency.setContactsName(contactsName);
        agency.setCompanyPhone(companyPhone);
        agency.setCompanyEmail(companyEmail);
        agency.setCompanyAddr(companyAddr);
        agency.setCertificate(certificate);
        agency.setLegal_info(legalinfo);
        agency.setOrganizationCode(organizationCode);
        agency.setAgreementcode(agreementcode);
        agency.setProvinceId(provinceId);
        agency.setCityId(cityId);
        agency.setSomeoneName(someoneName);
        agency.setSomeonePhone(someonePhone);
        // 不修改状态
        agency.setAccountBank(accountBank);
        agency.setBankId(bankId);
        agency.setAccountName(accountName);
        agency.setBankcode(bankcode);
        agency.setCreateId(createId);
        agency.setCreateDt(createDt);
        agency.setAgency_status(agency_status);
        int result = agencyService.EditAgency(agency);

        // 如果机构类型发生了变化，则更新本机构及下属机构定制商归属机构编号
        if (changed && result > 0) {
            agencyService.updateVestBrand(agencyid, vestBrand);
        }

        // 更新用户瑞推客标识
        if (result > 0) {
            // 更新瑞推客标识
            boolean tinyBussiness = (Boolean) request.getSession().getAttribute(ConstantUtils.TINYBUSSINESS);
            if (tinyBussiness) {
                userRoleService.updateUserisDtbUser(isDtbUser, agencyid);
            }
            // 更新瑞推客分润标识
            boolean auth = getIsDtbUserAuthority(request.getSession());
            if (tinyBussiness && auth) {
                userRoleService.updateUserDtbProfitFlag(dtbProfitFlag, agencyid);
            }
        }

        AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("修改失败");
        if (result > 0) {
            // 添加操作详情 20141203
            String operateDetail = "修改的机构名为" + agencyname + ",机构ID为" + agencyid;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAGENCY, ConstantUtils.OPERTYPEUPD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("修改成功");
        }
        return ajaxinfo;
    }

    /**
     * 删除机构支持批量删除
     * 
     * @param agencyIds
     * @return
     */
    @RequestMapping(params = "method=delAgency")
    @ResponseBody
    public AjaxReturnInfo delAgency(@RequestParam(value = "agencyIds") String agencyIds, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getRemoteAddr();
        String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("删除失败");
        String temp[] = agencyIds.split(",");
        int resultFlag = 0;
        int checkChildAgency = 0;
        for (int i = 0; i < temp.length; i++) {
            String agencyId = temp[i];
            int flag = agencyService.checkAgencyTerminal(agencyId);
            int tem = agencyService.checkHasChild(agencyId);
            resultFlag += flag;
            checkChildAgency += tem;
        }
        if (resultFlag == 0) {
            if (checkChildAgency == 0) {

                String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
                if (agencyIds.indexOf(agencyId) > -1) {
                    ajaxinfo = AjaxReturnInfo.success("当前登录的机构不能删除");
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("agencyid", agencyIds);
                    map.put("agencystatus", "3");
                    int result = agencyService.checkAgency(map);
                    if (result > 0) {
                        // userRoleService.deleteUserByAgencyId(agencyIds);
                        // 添加操作详情 20141203
                        String operateDetail = "删除的机构ID为" + agencyIds;
                        // 添加进系统日志表 20141203
                        systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEAGENCY, ConstantUtils.OPERTYPEDEL, operateDetail);
                        ajaxinfo = AjaxReturnInfo.success("删除成功");
                    }
                    map = null;
                }

            } else {
                ajaxinfo = AjaxReturnInfo.failed("机构下有子机构,不能删除!");
            }

        } else {
            ajaxinfo = AjaxReturnInfo.failed("机构下有终端,不能删除!");
        }
        return ajaxinfo;
    }

    /**
     * 根据机构编号获得自己直接的子机构
     * 
     * @param agencyId
     * @returngetAgencyTree
     */
    @RequestMapping(params = "method=getChildAgencyList")
    @ResponseBody
    public List<Map<String, Object>> getChildAgencyList(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        @SuppressWarnings("unused")
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * map.put("id", ConstantUtils.COMBOXONEID); map.put("text", "请选择机构");
         * map.put("selected", true); list.add(map);
         */
        List<AgencyBean> agencylist = agencyService.getChildAgencyList(agencyId, systemId);
        for (int i = 0; i < agencylist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", agencylist.get(i).getAgency_id());
            map1.put("text", agencylist.get(i).getCompanyName());
            list.add(map1);
        }
        return list;
    }

    /**
     * 14.11.03 新增方法 获得最上级机构
     * 
     * @param agencyId
     * @return
     */
    @RequestMapping(params = "method=getSuperiorAgencyList")
    @ResponseBody
    public List<Map<String, Object>> getSuperiorAgencyList(HttpServletRequest request) {
        // String agencyId
        // =request.getSession().getAttribute(ConstantUtils.AGENCYID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.AGENCYID);
        // 最上层机构代码
        String agencyId = ConstantUtils.CENTERCODE;
        String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择机构");
        map.put("selected", true);
        list.add(map);
        List<AgencyBean> agencylist = agencyService.getSuperiorAgencyList(agencyId, systemId);
        for (int i = 0; i < agencylist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", agencylist.get(i).getAgency_id());
            map1.put("text", agencylist.get(i).getCompanyName());
            list.add(map1);
        }
        return list;
    }

    @RequestMapping(params = "method=getChildAgencyListSin")
    @ResponseBody
    public List<Map<String, Object>> getChildAgencyListSin(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        String systemId = request.getParameter("systemId");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        @SuppressWarnings("unused")
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * map.put("id", ConstantUtils.COMBOXONEID); map.put("text", "请选择机构");
         * map.put("selected", true); list.add(map);
         */
        List<AgencyBean> agencylist = agencyService.getChildAgencyList(agencyId, systemId);
        for (int i = 0; i < agencylist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", agencylist.get(i).getAgency_id());
            map1.put("text", agencylist.get(i).getCompanyName());
            list.add(map1);
        }
        return list;
    }

    /**
     * 机构审核
     * 
     * @param agencyid
     * @param agencystatus
     * @param agencydesc
     * @return
     */
    @RequestMapping(params = "method=checkAgency")
    @ResponseBody
    public AjaxReturnInfo checkAgency(@RequestParam(value = "agencyid") String agencyid, @RequestParam(value = "agencystatus") String agencystatus,
            @RequestParam(value = "agencydesc") String agencydesc, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("审核失败");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyid", agencyid);
        map.put("agencystatus", agencystatus);
        map.put("agencydesc", agencydesc);
        int result = agencyService.checkAgency(map);
        if (result > 0) {
            // 添加操作详情 20141203
            String operateDetail = "审核的机构ID为" + agencyid;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEAGENCYCHECK, ConstantUtils.OPERTYPECHE, operateDetail);

            ajaxinfo = AjaxReturnInfo.success("审核成功");
        }
        return ajaxinfo;
    }

    /**
     * 获取所有的省
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getProvince")
    @ResponseBody
    public List<Map<String, Object>> getProvince(HttpServletRequest request) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择省");
        map.put("selected", true);
        list.add(map);
        List<AgencyBean> agencylist = agencyService.getProvince();
        for (int i = 0; i < agencylist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", agencylist.get(i).getProvinceId());
            map1.put("text", agencylist.get(i).getProvinceName());
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
    @RequestMapping(params = "method=getCity")
    @ResponseBody
    public List<Map<String, Object>> getCity(@RequestParam(value = "provinceId") String provinceId) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择市");
        map.put("selected", true);
        list.add(map);
        List<AgencyBean> agencylist = agencyService.getCity(provinceId);
        for (int i = 0; i < agencylist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", agencylist.get(i).getCityId());
            map1.put("text", agencylist.get(i).getCityName());
            list.add(map1);
        }
        return list;
    }

    @RequestMapping(params = "method=getChildAgency")
    @ResponseBody
    public List<Map<String, Object>> getChildAgency(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        List<Map<String, Object>> list = agencyService.getChildAgency(agencyId);
        List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择机构");
        map.put("selected", true);
        maplist.add(map);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", list.get(i).get("AGENCYID").toString());
            map1.put("text", list.get(i).get("AGENCYNAME").toString());
            maplist.add(map1);
        }
        return maplist;
    }

    /**
     * 
     * 方法名： agencyExport(机构导出).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午6:22:25.<br/>
     * 参数者异常：@param request
     * 参数者异常：@param agencyIds
     * 参数者异常：@param companyName
     * 参数者异常：@param status
     * 参数者异常：@param provinceId
     * 参数者异常：@param cityId
     * 参数者异常：@param response
     * 参数者异常：@throws IOException
     * 参数者异常：@throws RowsExceededException
     * 参数者异常：@throws WriteException .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    @RequestMapping(params = "method=agencyExport")
    public void agencyExport(HttpServletRequest request, @RequestParam(value = "agencyId") String agencyIds, @RequestParam(value = "companyName") String companyName,
            @RequestParam(value = "agencyStatus") String status, @RequestParam(value = "certificationStatus") String certificationStatus, @RequestParam(value = "provinceId") String provinceId, @RequestParam(value = "cityId") String cityId,
            HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
        String depagencyId = request.getSession().getAttribute("agencyId").toString().trim();

        String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString().trim();
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        String parentagencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim();
        String upperAgencyid = parentagencyId;
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        if (ConstantUtils.ROLETYPESUPERADMIN.equals(roletypeid.trim())) {
            systemId = "";
        }
        System.out.println(parentagencyId.equals(ConstantUtils.CENTERCODE));
        // if(systemId.equals("HTXT")&&!parentagencyId.equals(ConstantUtils.CENTERCODE)){
        SpecSystemBean spec = systemManageService.checkSpecSystem(systemId);
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, agencyId);

        if (spec != null) {
            if ("0".equals(spec.getLevelControl())) {
                parentagencyId = "";
            } else if ("1".equals(spec.getLevelControl()) && !parentagencyId.equals(ConstantUtils.CENTERCODE)) {
                if (agencyCount > 0) {
                    parentagencyId = "";
                } else {
                    upperAgencyid = "";
                }
            } else if ("2".equals(spec.getLevelControl())) {
                upperAgencyid = "";
            } else {
                parentagencyId = "";
            }
        } else {
            parentagencyId = "";
        }

        /*
         * } AgencyBean agency = new AgencyBean();
         * agency.setDepagencyId(depagencyId); agency.setAgency_id("");
         * agency.setCompanyName(""); agency.setOnlinechannel(parentagencyId);
         * agency.setUpperAgencyid(upperAgencyid);
         */

        AgencyBean agency = new AgencyBean();
        agency.setSystemId(systemId);
        agency.setDepagencyId(depagencyId);
        agency.setAgency_id(agencyIds);
        agency.setCompanyName(companyName);
        agency.setStatus(status);
        agency.setAgency_status(certificationStatus);
        agency.setOnlinechannel(parentagencyId);
        agency.setUpperAgencyid(upperAgencyid);
        agency.setProvinceId(provinceId);
        agency.setCityId(cityId);

        // agency.setAgency_status(ConstantUtils.AGENCY_PS); //
        // StatusParam.AGENCY_PS=2
        // 机构审核通过
        List<AgencyBean> list = agencyService.getAgency(agency);
        
        for (AgencyBean agencyBean : list) {
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getContactsName())) {
                agencyBean.setContactsName(Tools.handleCustomerName(agencyBean.getContactsName()));
            }
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyPhone())) {
                agencyBean.setCompanyPhone(FormatChar.mosaic(agencyBean.getCompanyPhone()));
            }
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getCompanyEmail())) {
                agencyBean.setCompanyEmail(FormatChar.mosaic(agencyBean.getCompanyEmail()));
            }
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getUserpId())) {
                agencyBean.setUserpId(FormatChar.IDCard(agencyBean.getUserpId()));
            }
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getSomeonePhone())) {
                agencyBean.setSomeonePhone(FormatChar.mosaic(agencyBean.getSomeonePhone()));
            }
            if (!org.springframework.util.StringUtils.isEmpty(agencyBean.getSomeoneName())) {
                agencyBean.setSomeoneName(Tools.handleCustomerName(agencyBean.getSomeoneName()));
            }
        }
        String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
        String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
        File f = new File(urls);
        if (!f.exists()) {
            f.mkdirs();
        }
        String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTAGENCY;
        File wj = new File(url);
        WritableWorkbook wwb = Workbook.createWorkbook(wj);
        int sheetNum = 0;
        WritableSheet ws = wwb.createSheet("agency" + sheetNum, sheetNum);
        if (agencyId.equals(ConstantUtils.CENTERCODE)) {
            for (int i = 0; i < ConstantUtils.AGENCY.length; i++) {
                ws.addCell(new Label(i, 0, ConstantUtils.AGENCY[i]));
            }

            for (int i = 0; i < list.size(); i++) {
                AddCell(list, ws, i);
                if (i / 60000 == sheetNum + 1) {
                    sheetNum++;
                    ws = null;
                    ws = wwb.createSheet("agency" + sheetNum, sheetNum);
                }
            }
        } else {
            for (int i = 0; i < ConstantUtils.AGENT.length; i++) {
                ws.addCell(new Label(i, 0, ConstantUtils.AGENT[i]));
            }

            for (int i = 0; i < list.size(); i++) {
                AddCellAgent(list, ws, i);
                if (i / 60000 == sheetNum + 1) {
                    sheetNum++;
                    ws = null;
                    ws = wwb.createSheet("agency" + sheetNum, sheetNum);
                }
            }
        }
        wwb.write();
        wwb.close();
        
        try {

            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAGENCYCHECK, ConstantUtils.OPERTYPEEXPO, null);
            download(request, response, url, ConstantUtils.EXPORTAGENCY);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 把agencyBean中的数据放入excel的”行“中
     * 
     * @param list
     * @param ws
     * @param i
     * @throws WriteException
     * @throws RowsExceededException
     */
    private void AddCell(List<AgencyBean> list, WritableSheet ws, int i) throws WriteException, RowsExceededException {
        int clum = i + 1;
        AgencyBean agencyBean = list.get(i);
        ws.addCell(new Label(0, clum, agencyBean.getAgency_id()));
        ws.addCell(new Label(1, clum, agencyBean.getCompanyName()));
        ws.addCell(new Label(2, clum, agencyBean.getVestagencyName()));
        ws.addCell(new Label(3, clum, agencyBean.getUppercompanyName()));
        ws.addCell(new Label(4, clum, agencyBean.getContactsName()));
        ws.addCell(new Label(5, clum, agencyBean.getUserpId()));
        ws.addCell(new Label(6, clum, agencyBean.getCompanyPhone()));
        ws.addCell(new Label(7, clum, agencyBean.getStatus()));
        ws.addCell(new Label(8, clum, agencyBean.getRegisterUrl()));
        ws.addCell(new Label(9, clum, agencyBean.getActTime()));
        ws.addCell(new Label(10, clum, agencyBean.getCompanyEmail()));
        ws.addCell(new Label(11, clum, agencyBean.getCompanyAddr()));
        ws.addCell(new Label(12, clum, agencyBean.getOrganizationCode()));
        ws.addCell(new Label(13, clum, agencyBean.getLegal_info()));
        ws.addCell(new Label(14, clum, agencyBean.getAgreementcode()));
        ws.addCell(new Label(15, clum, agencyBean.getProvinceName()));
        ws.addCell(new Label(16, clum, agencyBean.getCityName()));
        ws.addCell(new Label(17, clum, agencyBean.getAgencystatusSrc()));
        ws.addCell(new Label(18, clum, agencyBean.getAccountBank()));
        ws.addCell(new Label(19, clum, agencyBean.getBankId()));
        ws.addCell(new Label(20, clum, agencyBean.getAccountName()));
        ws.addCell(new Label(21, clum, agencyBean.getBankcode()));
        ws.addCell(new Label(22, clum, agencyBean.getCreateId()));
        ws.addCell(new Label(23, clum, agencyBean.getCreateDt()));
        ws.addCell(new Label(22, clum, agencyBean.getSomeoneName()));
        ws.addCell(new Label(23, clum, agencyBean.getSomeonePhone()));
    }
    
    /**
     * 
     * 方法名： AddCellAgent(代理商机构管理导出).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月1日.<br/>
     * 创建时间：下午4:32:41.<br/>
     * 参数者异常：@param list
     * 参数者异常：@param ws
     * 参数者异常：@param i
     * 参数者异常：@throws WriteException
     * 参数者异常：@throws RowsExceededException .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    private void AddCellAgent(List<AgencyBean> list, WritableSheet ws, int i) throws WriteException, RowsExceededException {
        int clum = i + 1;
        AgencyBean agencyBean = list.get(i);
        ws.addCell(new Label(0, clum, agencyBean.getAgency_id()));
        ws.addCell(new Label(1, clum, agencyBean.getCompanyName()));
        ws.addCell(new Label(2, clum, agencyBean.getSomeoneName()));
        ws.addCell(new Label(3, clum, agencyBean.getSomeonePhone()));
        ws.addCell(new Label(4, clum, agencyBean.getStatus()));
        ws.addCell(new Label(5, clum, agencyBean.getRegisterUrl()));
        ws.addCell(new Label(6, clum, agencyBean.getActTime()));
        ws.addCell(new Label(7, clum, agencyBean.getCompanyAddr()));
        ws.addCell(new Label(8, clum, agencyBean.getProvinceName()));
        ws.addCell(new Label(9, clum, agencyBean.getCityName()));
        ws.addCell(new Label(10, clum, agencyBean.getCreateId()));
        ws.addCell(new Label(11, clum, agencyBean.getCreateDt()));
    }

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

    @RequestMapping(params = "method=createAgencyTree")
    @ResponseBody
    public void createAgencyTree(HttpServletRequest request, HttpServletResponse response) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        @SuppressWarnings("unused")
        String parentagencyId = request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString();
        @SuppressWarnings("unused")
        String flag = request.getParameter("flag");
        @SuppressWarnings("unused")
        String systemId = request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
        @SuppressWarnings("unused")
        String roletypeid = request.getSession().getAttribute(ConstantUtils.ROLETYPEID).toString().trim();
        String id = request.getParameter("id");
        System.out.println(id);

        if (null != id && "" != id && !"null".equals(id)) {
            String zero = "";
            for (int i = 0; i < 8 - id.length(); i++) {
                zero += "0";
            }
            id = zero + id;
            agencyId = id;
        }
        List<TreeNodesBean> list = agencyService.getTreeList(agencyId);
        String json = JSON.toJSONString(list);
        System.out.println(json);
        PrintWriter out = null;
        response.setContentType("text/html; charset=utf-8");
        try {
            out = response.getWriter();
            out.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 
     * 方法名： getChildList.<br/>
     * 方法作用:动态加载机构.<br/>
     *
     * 创建者：Ferry Chen<br/>
     * 创建日期：2016年4月8日.<br/>
     * 创建时间：下午1:57:16.<br/>
     * 参数者异常：@param request 参数者异常：@param response 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：List<Map<String,Object>>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = "method=getChildList")
    @ResponseBody
    public List<Map<String, Object>> getChildList(HttpServletRequest request, HttpServletResponse response) {
        String agencyIdLogin = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String agencyId = request.getParameter("agencyId");
        List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
        if (StringUtils.isEmpty(agencyId)) {
            agencyId = agencyIdLogin;
            List<Map<String, Object>> agency = agencyService.getAgencyone(agencyId);
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != agency && agency.size() > 0) {
                map.put("id", agency.get(0).get("AGENCYID"));
                map.put("text", agency.get(0).get("AGENCYNAME"));
                List<AgencyBean> list = this.agencyService.getChildAgencyList(agencyId, null);
                if (list.size() > 0) {
                    map.put("isParent", true);
                } else {
                    map.put("isParent", false);
                }
                resultlist.add(map);
            }
            map = null;
        } else {
            List<AgencyBean> list = this.agencyService.getChildAgencyList(agencyId, null);
            for (AgencyBean treeBean : list) {
                Map<String, Object> mapNode = new HashMap<String, Object>();
                mapNode.put("id", treeBean.getAgency_id());
                mapNode.put("text", treeBean.getCompanyName());
                mapNode.put("isParent", treeBean.getIsParent().equals("0") ? false : true);
                resultlist.add(mapNode);
                mapNode = null;
            }
            list = null;
        }
        return resultlist;

    }

    /**
     * 
     * 【方法名】 : 获取机构弹框时间对象. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月2日 上午10:01:30 .<br/>
     * 【参数】： .<br/>
     * 
     * @param userBean
     *            UserBean
     * @param request
     *            HttpServletRequest
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=getAgencyObject")
    @ResponseBody
    public AjaxReturnInfo getAgencyObject(UserBean userBean, HttpServletRequest request) {
        AjaxReturnInfo ajaxinfo;
        try {
            String username = (String) request.getSession().getAttribute(ConstantUtils.USERNAME);
            String agencyId = (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
            AgencyBean bean = agencyService.getAgency(agencyId);
            if (username.equals(agencyId)) {
                if (!StringUtils.isEmpty(bean.getBouncedTime())) {
                    ajaxinfo = AjaxReturnInfo.success("该用户信息已做修改");
                } else {
                    ajaxinfo = AjaxReturnInfo.failed("弹框出时间不存在，该用户信息未做修改");
                }
            } else {
                ajaxinfo = AjaxReturnInfo.success("该用户无权限修改");
            }
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.success("该用户无权限修改");
        }
        return ajaxinfo;
    }

    /**
     * 
     * 【方法名】 :重新修改当前登录用户资料 <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月2日 上午11:58:23 .<br/>
     * 【参数】： .<br/>
     * 
     * @param renameCompanyName
     *            公司名称
     * @param companyPhone
     *            公司电话
     * @param companyEmail
     *            公司邮箱
     * @param contactsName
     *            联系人
     * @param request
     *            HttpServletRequest
     * @return AjaxReturnInfo对象.<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         修改人：zhangyinghui 2017-1-11
     *         <p/>
     */
    @RequestMapping(params = "method=updateAgency")
    @ResponseBody
    public AjaxReturnInfo updateAgency(@RequestParam(value = "renameCompanyName") String renameCompanyName, @RequestParam(value = "companyPhone") String companyPhone,
            @RequestParam(value = "companyEmail") String companyEmail, @RequestParam(value = "contactsName") String contactsName, HttpServletRequest request) {
        AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("修改失败");
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String createDt = CommonDate.getDate();
        AgencyBean agency = agencyService.getAgency(agencyId);
        if (agency != null) {
            agency.setRenameCompanyName(renameCompanyName);
            agency.setCompanyPhone(companyPhone);
            agency.setCompanyEmail(companyEmail);
            agency.setContactsName(contactsName);
            agency.setBouncedTime(createDt);
            try {
                int result = agencyService.updateMyAgency(agency);
                if (result > 0) {
                    // 修改用户手机号
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("phone", companyPhone);
                    map.put("userloginname", agencyId);
                    userRoleService.updateUserByMap(map);

                    ajaxinfo = AjaxReturnInfo.success("修改成功");
                }
            } catch (Exception e) {
                LogPay.error(e.getMessage(), e);
                ajaxinfo = AjaxReturnInfo.failed("修改失败");
            }
        }
        return ajaxinfo;
    }

    /**
     * 【方法名】 : (加载机构信息). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月11日 上午10:13:23 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     *            HttpServletRequest
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=getAgencyObj")
    @ResponseBody
    public AgencyBean getAgencyObj(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AgencyBean bean = null;
        String agencyId = (String) session.getAttribute(ConstantUtils.AGENCYID);
        String username = (String) session.getAttribute(ConstantUtils.USERNAME);
        if (agencyId.equals(username)) {
            bean = agencyService.getAgency(agencyId);
        }
        return bean;
    }

    /**
     * 【方法名】 : (是否有瑞推客设置权限). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月20日 下午1:43:13 .<br/>
     * 【参数】： .<br/>
     * 
     * @return AjaxReturnInfo 对象.<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=isDtbUserAuthority")
    @ResponseBody
    public AjaxReturnInfo isDtbUserAuthority(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AjaxReturnInfo ajaxinfo = AjaxReturnInfo.failed("无权限");
        boolean auth = getIsDtbUserAuthority(session);
        if (auth) {
            ajaxinfo = AjaxReturnInfo.success("有权限");
        }
        return ajaxinfo;
    }

    /**
     * 【方法名】 : (获取瑞推客提现标识). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月22日 下午6:38:47 .<br/>
     * 【参数】： .<br/>
     * 
     * @param session
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    private boolean getIsDtbUserAuthority(HttpSession session) {
        boolean auth = false;
        String userIds = authorityService.getParamObject("DtbUserAuthority");
        String[] userLoginNameArray = userIds.split(",");
        String loginName = (String) session.getAttribute(ConstantUtils.USERLOGINNAME);
        if (userLoginNameArray != null && userLoginNameArray.length > 0) {
            for (String uloginName : userLoginNameArray) {
                if (uloginName.equals(loginName)) {
                    auth = true;
                    break;
                }
            }
        }
        return auth;
    }

    /**
     * 【方法名】 : (获取瑞推客标识). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月22日 下午4:51:20 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     * @param systemId
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=getIsDtbUser")
    @ResponseBody
    public String getIsDtbUser(HttpServletRequest request, String agencyId) {
        String isDtbUser = "";
        UserRoleBean user = new UserRoleBean();
        user.setLoginName(agencyId);
        List<UserRoleBean> userRoleBeans = userRoleService.getUserRole(user);
        if (userRoleBeans != null && userRoleBeans.size() > 0) {
            isDtbUser = userRoleBeans.get(0).getIsDtbUser();
        }
        return isDtbUser;
    }

    /**
     * 【方法名】 : (获取瑞推客提现). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月22日 下午7:08:36 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     * @param agencyId
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=getDtbProfitFlag")
    @ResponseBody
    public String getDtbProfitFlag(HttpServletRequest request, String agencyId) {
        String dtbProfitFlag = "";
        UserRoleBean user = new UserRoleBean();
        user.setLoginName(agencyId);
        List<UserRoleBean> userRoleBeans = userRoleService.getUserRole(user);
        if (userRoleBeans != null && userRoleBeans.size() > 0) {
            dtbProfitFlag = userRoleBeans.get(0).getDtbProfitFlag();
        }
        return dtbProfitFlag;
    }

    /**
     * 获取所有下级机构信息
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getAgencyComb")
    @ResponseBody
    public Map<String, Object> getAgencyComb(String q, HttpServletRequest req) {
        // 获得ip
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "50" : rows);
        int start = (pagenumber - 1) * rownumber;
        Integer count;
        List<AgencyBean> list;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isEmpty(q)) {
                map.put("agency_id", q);
                map.put("companyName", q);
            }
            map.put("depagencyId", agencyIdS);
            map.put("ownAgencyId", agencyIdS);
            count = agencyService.getAgencyCombCount(map);
            int end = (start + rownumber) > count ? count : start + rownumber;
            map.put("start", start);
            map.put("end", end);
            list = agencyService.getAgencyCombAll(map);
            return AjaxReturnInfo.setTable(count, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogPay.error(e.getMessage(), e);
        }
        return null;

    }

    /**
     * 获取所有直属下级机构信息
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getAgencyCombChild")
    @ResponseBody
    public Map<String, Object> getAgencyCombChild(String q, HttpServletRequest req) {
        // 获得ip
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "50" : rows);
        int start = (pagenumber - 1) * rownumber;
        Integer count;
        List<AgencyBean> list;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isEmpty(q)) {
                map.put("agency_id", q);
                map.put("companyName", q);
            }
            map.put("depagencyId", agencyIdS);
            map.put("ownAgencyId", agencyIdS);
            count = agencyService.getAgencyCombChildCount(map);
            int end = (start + rownumber) > count ? count : start + rownumber;
            map.put("start", start);
            map.put("end", end);
            list = agencyService.getAgencyCombChild(map);
            return AjaxReturnInfo.setTable(count, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogPay.error(e.getMessage(), e);
        }
        return null;

    }
    
    /**
     * 
     * 【方法名】    : (实名认证). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月21日 上午10:29:16 .<br/>
     * 【参数】： .<br/>
     * @param companyName 企业名称
     * @param contactsName 法人姓名
     * @param userpId 身份证号
     * @param companyEmail 企业邮箱
     * @param companyPhone 企业电话
     * @param request request
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=updateCertification")
    @ResponseBody
    public AjaxReturnInfo updateCertification(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "contactsName") String contactsName,
            @RequestParam(value = "userpId") String userpId, @RequestParam(value = "companyEmail") String companyEmail, @RequestParam(value = "companyPhone") String companyPhone, HttpServletRequest request){
//        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.failed("message.auth.failed");
//        try {
//            String ipAddress = request.getRemoteAddr();
//            String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
//            String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
//            AgencyBean agency = new AgencyBean();
//            agency.setAgency_id(agencyId);
//            agency.setCompanyName(companyName);
//            agency.setCompanyEmail(companyEmail);
//            
////            PayCustomer payCustomer = payCustomerService.selectPayCustomerByMobile(companyPhone);
////            AgencyBean bean = agencyService.getAgencyBeanWithMap(companyPhone);
////            if (payCustomer != null && InterfaceNameConstans.VALUE_3.equals(payCustomer.getCustomertag()) && InterfaceNameConstans.VALUE_5.equals(payCustomer.getCheckrange())) {
////                agency.setUserpId(payCustomer.getCustomerpid());
////                agency.setContactsName(payCustomer.getUsername());
////                //查询Agency是否已经实名
////            } else if (bean != null && bean.getAgency_status() != null) {
////                if (bean.getAgency_status().equals(InterfaceNameConstans.VALUE_3)) {
////                    if (StringUtils.isNotEmpty(bean.getUserpId())) {
////                        agency.setUserpId(bean.getUserpId());
////                    }
////                    if (StringUtils.isNotEmpty(bean.getContactsName())) {
////                        agency.setContactsName(bean.getContactsName());
////                    }
////                    
////                    if (StringUtils.isNotEmpty(bean.getUserpId()) && StringUtils.isNotEmpty(bean.getContactsName())) {
////                        agency.setAgency_status(AgencyStatus.realNamePass.getVal());
////                    }
////                }
//            } else {
//                agency.setUserpId(userpId);
//                agency.setContactsName(contactsName);
//                JSONObject jsonObj = null;
//                if (!ConstantUtils.MSG_SUCCESS.equals(jsonObj.get(ConstantUtils.MSG_CODE))) {
//                    return AjaxReturnInfo.failed((String) jsonObj.get(ConstantUtils.MSG_TEXT));
//                }
//            }
//            int result = agencyService.updateCertification(agency, userId);
//            if (result > 0){
//                String operateDetail = "实名认证的机构为" + companyName + ",机构ID为" + agencyId;
//                // 添加进系统日志表 
//                systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.CERTIFICATION, ConstantUtils.OPERTYPEUPD, operateDetail);
//                ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.auth.success"));
//            }
//        } catch (QTException e) {
//            LogPay.error(e.getMessage(), e);
//            ajaxReturnInfo = AjaxReturnInfo.failed(e.getRespMsg()); 
//        } catch (Exception e) {
//            LogPay.error(e.getMessage(), e);
//            ajaxReturnInfo = AjaxReturnInfo.failed(e.getMessage()); 
//        }
//        return ajaxReturnInfo;
        return null;
    }
    
   /**
    * 【方法名】    : (调用Dubbo服务创建机构). <br/> 
    * 【作者】: yinghui zhang .<br/>
    * 【时间】： 2017年3月31日 下午8:19:57 .<br/>
    * 【参数】： .<br/>
    * @param agencyname 机构名
    * @param someoneName 联系人 
    * @param someonePhone 联系人电话
    * @param provinceId 省
    * @param cityId 市
    * @param companyAddr 公司地址
    * @param request HttpServletRequest
    * @return .<br/>
    * <p>
    * 修改记录.<br/>
    * 修改人:  yinghui zhang 修改描述： .<br/>
    * <p/>
    */
    @RequestMapping(params = "method=addAgencyWithDubbo")
    @ResponseBody
    public AjaxReturnInfo addAgencyWithDubbo(@RequestParam(value = "agencyname") String agencyname, @RequestParam(value = "someoneName") String someoneName,
            @RequestParam(value = "someonePhone") String someonePhone, @RequestParam(value = "provinceId") String provinceId, @RequestParam(value = "cityId") String cityId,
            @RequestParam(value = "companyAddr") String companyAddr, HttpServletRequest request) {
        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.save.success"));
        try {
            String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
            AgencyBean agency = new AgencyBean();
            agency.setOnlinechannel(agencyIdS);
            agency.setCompanyName(agencyname);
            agency.setCompanyAddr(companyAddr);
            agency.setProvinceId(provinceId);
            agency.setCityId(cityId);
            agency.setSomeoneName(someoneName);
            agency.setSomeonePhone(someonePhone);
//            JSONObject jsonObj = agencyService.addAgencyByDubbo(agency);
            JSONObject jsonObj = null;
            if (!ConstantUtils.MSG_SUCCESS.equals(jsonObj.get(ConstantUtils.MSG_CODE))) {
                throw new QTException((String) jsonObj.get(ConstantUtils.MSG_TEXT));
            } else {
                String message = "该下级机构已开设成功！系统已自动生成该机下级的机构信息，请将下发链接复制给下级进行激活，激活成功后即可登陆分润系统。"+(String)jsonObj.get(InterfaceNameConstans.RTB_REGISTER_URL);
                ajaxReturnInfo = AjaxReturnInfo.success(message);
            }
            
        } catch (QTException e) {
            LogPay.error(e.getMessage(), e);
            ajaxReturnInfo = AjaxReturnInfo.failed(e.getRespMsg());
        }
        return ajaxReturnInfo;

    }
    
    /**
     * 【方法名】    : (更新完善信息). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月10日 下午8:36:29 .<br/>
     * 【参数】： .<br/>
     * @param agencyBean AgencyBean
     * @param request HttpServletRequest
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=modifyAgencyBean")
    @ResponseBody
    public AjaxReturnInfo modifyAgencyBean(AgencyBean agencyBean, HttpServletRequest request){
        AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.agajx.update.success"));
        try {
            String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
            agencyBean.setAgency_id(agencyIdS);
            agencyService.updateByPrimaryKey(agencyBean);
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
            ajaxReturnInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.error"));
        }
        return ajaxReturnInfo;
    }
    
    /**
     * 【方法名】    : (激活机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月19日 上午11:12:37 .<br/>
     * 【参数】： .<br/>
     * @param phone 电话号码
     * @param request HttpServletRequest
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=modifyCompanyPhone")
    @ResponseBody
    public AjaxReturnInfo modifyCompanyPhone(String phone, HttpServletRequest request) {
//        try {
//            String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
//            String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
//            // 更新机构信息
//            AgencyBean agencyBean = new AgencyBean();
//            PayCustomer payCustomer = payCustomerService.selectPayCustomerByMobile(phone);
//            AgencyBean bean = agencyService.getAgencyBeanWithMap(phone);
//            if (payCustomer != null && InterfaceNameConstans.VALUE_3.equals(payCustomer.getCustomertag()) && InterfaceNameConstans.VALUE_5.equals(payCustomer.getCheckrange())) {
//                agencyBean.setUserpId(payCustomer.getCustomerpid());
//                agencyBean.setContactsName(payCustomer.getUsername());
//                agencyBean.setAgency_status(AgencyStatus.realNamePass.getVal());
//                // 查询Agency是否已经实名
//            } else if (bean != null && bean.getAgency_status() != null) {
//                if (bean.getAgency_status().equals(InterfaceNameConstans.VALUE_3)) {
//                    if (StringUtils.isNotEmpty(bean.getUserpId())) {
//                        agencyBean.setUserpId(bean.getUserpId());
//                    }
//                    if (StringUtils.isNotEmpty(bean.getContactsName())) {
//                        agencyBean.setContactsName(bean.getContactsName());
//                    }
//                    if (StringUtils.isNotEmpty(bean.getUserpId()) && StringUtils.isNotEmpty(bean.getContactsName())) {
//                        agencyBean.setAgency_status(AgencyStatus.realNamePass.getVal());
//                    }
//                    
//                }
//            } else {
//                agencyBean.setAgency_status(AgencyStatus.auditPass.getVal());
//            }
//            agencyBean.setAgency_id(agencyIdS);
//            agencyBean.setCompanyPhone(phone);
//            agencyBean.setSomeonePhone(phone);
//            agencyService.updateByPrimaryKey(agencyBean);
//
//            // 更新用户信息
//            UserBean user = new UserBean();
//            user.setUserid(userId);
//            user.setPhone(phone);
//            usersService.updateByPrimaryKey(user);
//
//            return AjaxReturnInfo.success(I18nUtils.getResourceValue("message.activate.success"));
//        } catch (Exception e) {
//            LogPay.error(e.getMessage(), e);
//            return AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.error"));
//        }
 return null;
    }
    
    
    /**
     * 【方法名】    : (验证手机号是否已经实名过). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月21日 下午9:43:37 .<br/>
     * 【参数】： .<br/>
     * @param phone String
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
//    private void getAgencyBeanWithPhone(AgencyBean agencyBean,String phone) {
//        PayCustomer payCustomer = payCustomerService.selectPayCustomerByMobile(phone);
//        AgencyBean bean = agencyService.getAgencyBeanWithMap(phone);
//        if (payCustomer != null && InterfaceNameConstans.VALUE_3.equals(payCustomer.getCustomertag()) && InterfaceNameConstans.VALUE_5.equals(payCustomer.getCheckrange())) {
//            agencyBean.setUserpId(payCustomer.getCustomerpid());
//            agencyBean.setContactsName(payCustomer.getUsername());
//            agencyBean.setAgency_status(AgencyStatus.realNamePass.getVal());
//            // 查询Agency是否已经实名
//        } else if (bean != null && bean.getAgency_status() != null) {
//            if (bean.getAgency_status().equals(InterfaceNameConstans.VALUE_3)) {
//                if (StringUtils.isNotEmpty(bean.getUserpId())) {
//                    agencyBean.setUserpId(bean.getUserpId());
//                }
//                if (StringUtils.isNotEmpty(bean.getContactsName())) {
//                    agencyBean.setContactsName(bean.getContactsName());
//                }
//                if (StringUtils.isNotEmpty(bean.getUserpId()) && StringUtils.isNotEmpty(bean.getContactsName())) {
//                    agencyBean.setAgency_status(AgencyStatus.realNamePass.getVal());
//                }
//
//            }
//        } else {
//            agencyBean.setAgency_status(AgencyStatus.auditPass.getVal());
//        }
//    }

}
