/**
 * 
 */
package com.compass.splitmould.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ardu.jms.ems.exception.QTException;

import com.compass.authority.service.AuthorityService;
import com.compass.common.FrConstants;
import com.compass.splitmould.model.SplitMouldBean;
import com.compass.splitmould.model.SplitOrgMouldBean;
import com.compass.splitmould.model.SplitTopMouldBean;
import com.compass.splitmould.service.SplitMouldService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author liCheng 分润模板设置Controller
 */

@Controller
@RequestMapping("/splitmould/splitmould.do")
public class SplitMouldController {

    @Autowired
    @Qualifier("splitMouldService")
    private SplitMouldService splitMouldService;

    // 系统日志service
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService  systemLogService;

    @Autowired
    @Qualifier("authorityService")
    private AuthorityService  authorityService;
    
    private static Logger       logger = LoggerFactory.getLogger(SplitMouldService.class);

    /**
     * 获得顶级模板列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getTopMoulds")
    @ResponseBody
    public Map<String, Object> getTopMoulds(@RequestParam(value = "mouldName") String mouldName, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        if (mouldName == null || "".equals(mouldName) || "-1".equals(mouldName)) {
            mouldName = "";
        }
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

        List<SplitTopMouldBean> list = splitMouldService.getTopMoulds(agencyId, mouldName);
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitTopMouldBean> ruleList = new ArrayList<SplitTopMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + mouldName;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 添加顶级分润规则
     * 
     * @param mouldName
     * @param ruleRem
     * @return
     */
    @RequestMapping(params = "method=addTopMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo addTopMould(@RequestParam(value = "mouldName") String mouldName, @RequestParam(value = "ruleRem") String ruleRem, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        int result = 99;
        // 判断是否重名
        if (splitMouldService.checkTopMouldDup(mouldName)) {
            // 如果不重名，则新增
            SplitTopMouldBean splitTopMouldBean = new SplitTopMouldBean();
            splitTopMouldBean.setAgencyId(agencyId);
            splitTopMouldBean.setRuleRem(ruleRem);
            splitTopMouldBean.setMouldName(mouldName);
            splitTopMouldBean.setRuleId(CommonDate.getDateStr());
            // 置为有效
            splitTopMouldBean.setStatus(ConstantUtils.TOPMOULD_V);
            result = splitMouldService.addTopMould(splitTopMouldBean);
            splitTopMouldBean = null;
        } else {
            // 如果重名
            result = 88;
        }
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("保存失败");
        } else if (result == 88) {
            ajaxinfo = AjaxReturnInfo.failed("对不起，模板名称重复，请改正后再试");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "添加顶级模板为" + mouldName;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEADD, operateDetail);

            ajaxinfo = AjaxReturnInfo.success("保存成功");
        }
        return ajaxinfo;
    }

    /**
     * 修改顶级分润规则
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=updateTopMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo updateTopMould(@RequestParam(value = "mouldName") String mouldName, @RequestParam(value = "ruleRem") String ruleRem, @RequestParam(value = "ruleId") String ruleId,
            HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        int result = 99;
        // 判断是否重名
        if (splitMouldService.checkTopMouldDup(mouldName)) {
            // 不重名的话则修改
            SplitTopMouldBean splitTopMouldBean = new SplitTopMouldBean();
            splitTopMouldBean.setRuleRem(ruleRem);
            splitTopMouldBean.setMouldName(mouldName);
            splitTopMouldBean.setRuleId(ruleId);
            result = splitMouldService.updateTopMould(splitTopMouldBean);
            splitTopMouldBean = null;
        } else {
            // 如果重名
            result = 88;
        }
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("更新失败");
        } else if (result == 88) {
            ajaxinfo = AjaxReturnInfo.failed("对不起，模板名称重复，请改正后再试");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "更新模板名称为" + mouldName;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEUPD, operateDetail);

            ajaxinfo = AjaxReturnInfo.success("更新成功");
        }
        return ajaxinfo;
    }

    /**
     * 删除顶级模板
     * 
     * @param Ids
     * @return
     */
    @RequestMapping(params = "method=deleteMould")
    @ResponseBody
    public AjaxReturnInfo deleteMould(@RequestParam(value = "ruleId") String ruleId, HttpServletRequest request) {
        AjaxReturnInfo ajaxinfo = null;
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        // 删除顶级模板
        int result1 = splitMouldService.deleteTopMould(ruleId, agencyId);
        // 删除分润模板
        int result2 = splitMouldService.deleteMould(ruleId);

        if (result1 == 0 || result2 == 0) {
            ajaxinfo = AjaxReturnInfo.failed("删除失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "删除模板为" + ruleId;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEDEL, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("删除成功");
        }

        return ajaxinfo;
    }

    /*
     * 删除机构分润模板
     * 
     * @param Ids
     * 
     * @return
     */
    @RequestMapping(params = "method=deleteOrgMould")
    @ResponseBody
    public AjaxReturnInfo deleteOrgMould(@RequestParam(value = "ruleId") String ruleId, @RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "mouldName") String mouldName,
            HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        AjaxReturnInfo ajaxinfo = null;
        // 先将要被删除的添加进添加进历史表
        splitMouldService.addDelRecords(ruleId, agencyId, mouldName);
        // 删除分润模板
        int result = splitMouldService.deleteOrgMould(ruleId, agencyId, mouldName);
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("删除失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "删除模板" + mouldName;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEORGSPLITMOULD, ConstantUtils.OPERTYPEDEL, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("删除成功");
        }

        return ajaxinfo;
    }

    /**
     * 获得规则列表
     * @param ruleId 规则号
     * @param request 请求
     * @return  返回模板
     */
    @RequestMapping(params = "method=getMoulds")
    @ResponseBody
    public Map<String, Object> getMoulds(
    // 获取传过来的ruleId
            @RequestParam(value = "ruleId") String ruleId, HttpServletRequest request)throws Exception {

        String mouldsdealTypeStr = "";
        String paramvalue = authorityService.getParamObject("MOULDSDEALTYPE");
        String array[] = paramvalue.split("\\|");
        for (int i = 0; i < array.length; i++) {
            String idtext[] = array[i].split(",");
            String tempvar = idtext[0] + ",";
            mouldsdealTypeStr += tempvar;
        }

        List<SplitMouldBean> list = splitMouldService.getMoulds(ruleId, mouldsdealTypeStr.substring(0, mouldsdealTypeStr.length() - 1));
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitMouldBean> ruleList = new ArrayList<SplitMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 上级机构，分配模板时，显示的模板信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getMouldsD")
    @ResponseBody
    public Map<String, Object> getMouldsD(
    // 获取传过来的ruleId
            @RequestParam(value = "ruleId") String ruleId, @RequestParam(value = "mouldName") String mouldName, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        List<SplitMouldBean> list = splitMouldService.getMouldsD(ruleId, mouldName, agencyId);
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitMouldBean> ruleList = new ArrayList<SplitMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 添加分润规则
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=addMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo addMould(@RequestParam(value = "splitRegionMode") String splitRegionMode, @RequestParam(value = "dealType") String dealType,
            @RequestParam(value = "splitMode") String splitMode, @RequestParam(value = "ruleBegin") int ruleBegin, @RequestParam(value = "ruleEnd") int ruleEnd,
            @RequestParam(value = "ruleRem") String ruleRem, @RequestParam(value = "ruleId") String ruleId, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);
        SplitMouldBean splitMouldBean = new SplitMouldBean();
        splitMouldBean.setDealType(dealType);
        splitMouldBean.setRuleBegin(ruleBegin);
        splitMouldBean.setRuleEnd(ruleEnd);
        splitMouldBean.setRuleId(ruleId);
        splitMouldBean.setSplittingRegionMode(splitRegionMode);
        splitMouldBean.setSplittingMode(splitMode);
        splitMouldBean.setRuleRem(ruleRem);

        splitMouldBean.setCreaterId(createrId);
        splitMouldBean.setCreateDate(CommonDate.getDate());
        // 加一个表示第几条记录的序列
        String ruleNum = String.valueOf(splitMouldService.getruleNum());
        splitMouldBean.setRuleNum(ruleNum);
        int result = splitMouldService.addMould(splitMouldBean);
        splitMouldBean = null;
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("保存失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "添加分润规则的ID为" + ruleId;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEADD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("保存成功");
        }
        return ajaxinfo;
    }

    /**
     * 修改分润规则
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=updateMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo updateMould(@RequestParam(value = "splitRegionModeUS") String splitRegionMode, @RequestParam(value = "dealTypeUS") String dealType,
            @RequestParam(value = "splitModeUS") String splitMode, @RequestParam(value = "ruleBeginUS") int ruleBegin, @RequestParam(value = "ruleEndUS") int ruleEnd,
            @RequestParam(value = "ruleRemUS") String ruleRem, @RequestParam(value = "ruleIdUS") String ruleId, @RequestParam(value = "ruleNumUS") String ruleNum, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);
        SplitMouldBean splitMouldBean = new SplitMouldBean();
        splitMouldBean.setDealType(dealType);
        splitMouldBean.setRuleBegin(ruleBegin);
        splitMouldBean.setRuleEnd(ruleEnd);
        splitMouldBean.setRuleId(ruleId);
        splitMouldBean.setSplittingRegionMode(splitRegionMode);
        splitMouldBean.setSplittingMode(splitMode);
        splitMouldBean.setRuleRem(ruleRem);
        splitMouldBean.setRuleNum(ruleNum);
        splitMouldBean.setCreaterId(createrId);
        splitMouldBean.setCreateDate(CommonDate.getDate());

        int result = splitMouldService.updateMould(splitMouldBean);
        splitMouldBean = null;
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("更新失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "更新分润规则" + ruleId;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEUPD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("更新成功");
        }
        return ajaxinfo;
    }

    /**
     * 删除分润规则
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=deleteRule", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo deleteRule(

    @RequestParam(value = "ruleNumUS") String ruleNum, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);
        SplitMouldBean splitMouldBean = new SplitMouldBean();

        splitMouldBean.setRuleNum(ruleNum);
        splitMouldBean.setCreaterId(createrId);
        splitMouldBean.setCreateDate(CommonDate.getDate());

        int result = splitMouldService.deleteRule(ruleNum);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("删除失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "删除的分润规则为" + ruleNum;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEDEL, operateDetail);

            ajaxinfo = AjaxReturnInfo.success("删除成功");
        }
        splitMouldBean = null;
        return ajaxinfo;
    }

    /**
     * 顶级机构使用的分配模板
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=distrMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo distrMould(@RequestParam(value = "childAgencyId") String childAgencyId, @RequestParam(value = "ruleId") String ruleId, HttpServletRequest request){
            // 获得ip
            AjaxReturnInfo ajaxinfo = null;
            try {
                String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
                String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
                String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
                int result = 99;
                String createDt = CommonDate.getDate();

                String mouldsdealTypeStr = "";
                String paramvalue = authorityService.getParamObject("MOULDSDEALTYPE");
                String array[] = paramvalue.split("\\|");
                for (int i = 0; i < array.length; i++) {
                    String idtext[] = array[i].split(",");
                    String tempvar = idtext[0] + ",";
                    mouldsdealTypeStr += tempvar;
                }

                // 判断此模板是否已经分配给过这个子系统
                if (splitMouldService.checkDistrMould(ruleId, childAgencyId)) {
                    // 根据自己的agencyId，和RuleId得到模板原来的信息
                    List<SplitTopMouldBean> list = splitMouldService.getTopMouldByAR(agencyId, ruleId);
                    // 把这条记录agencyId,换成子机构
                    SplitTopMouldBean splitTopMouldBean = list.get(0);
                    splitTopMouldBean.setAgencyId(childAgencyId);
                    // 添加进顶级模板表
                    result = splitMouldService.addTopMould(splitTopMouldBean);
                    // 将模板信息分配到机构分润表里面
                    List<SplitMouldBean> mouldlist = splitMouldService.getMoulds(ruleId, mouldsdealTypeStr.substring(0, mouldsdealTypeStr.length() - 1));
                    if (mouldlist != null && mouldlist.size() > 0) {
                        for (int i = 0; i < mouldlist.size(); i++) {
                            splitMouldService.addOrgMould(mouldlist.get(i), childAgencyId, createDt);
                        }
                    }
                } else {
                    // 已经分配过的话，就是替换原来的
                    // 根据自己的agencyId，和RuleId得到模板原来的信息
                    List<SplitTopMouldBean> list = splitMouldService.getTopMouldByAR(agencyId, ruleId);
                    // 把这条记录agencyId,换成子机构
                    SplitTopMouldBean splitTopMouldBean = list.get(0);
                    // 把模板的名字带进去
                    list.get(0).setMouldName(ConstantUtils.NOMOULDNAME);
                    splitTopMouldBean.setAgencyId(childAgencyId);
                    // 更新顶级模板表
                    // 把原来的模板放进机构分润历史表，并且在机构分润表里面删除
                    splitMouldService.updateUpperDistrMould(childAgencyId, ruleId, ConstantUtils.NOMOULDNAME, createDt, agencyId);
                    // 将模板信息分配到机构分润表里面
                    List<SplitMouldBean> mouldlist = splitMouldService.getMoulds(ruleId, mouldsdealTypeStr.substring(0, mouldsdealTypeStr.length() - 1));
                    if (mouldlist != null && mouldlist.size() > 0) {
                        for (int i = 0; i < mouldlist.size(); i++) {
                            splitMouldService.addOrgMould(mouldlist.get(i), childAgencyId, createDt);
                        }
                    }
                }
                ajaxinfo = null;
                if (result == 0) {
                    ajaxinfo = AjaxReturnInfo.failed("分配失败");
                } else if (result == 88) {
                    ajaxinfo = AjaxReturnInfo.success("分配失败，此子系统已经分配过这个模板");
                } else {
                    // 添加操作详情 20141203
                    String operateDetail = "分配给" + childAgencyId;
                    // 添加进系统日志表 20141203
                    systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEMOULDDIST, operateDetail);

                    ajaxinfo = AjaxReturnInfo.success("分配成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ajaxinfo = AjaxReturnInfo.failed(e.getMessage());
            }
        return ajaxinfo;
    }

    /**
     * 上级机构使用的分配模板
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=upperDistrMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo upperDistrMould(@RequestParam(value = "childAgencyId") String childAgencyId, @RequestParam(value = "ruleId") String ruleId,
            @RequestParam(value = "mouldName") String mouldName, HttpServletRequest request) {
        int result = 99;
        String createDt = CommonDate.getDate();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);

        AjaxReturnInfo ajaxinfo = null;
        try {
            // 判断是否已经分配过
            if (splitMouldService.checkDistr(childAgencyId)) {
                // 根据自己的agencyId，和RuleId得到模板原来的信息
                List<SplitTopMouldBean> list = splitMouldService.getTopMouldByAR(agencyId, ruleId);
                // 把这条记录agencyId,换成子机构
                SplitTopMouldBean splitTopMouldBean = list.get(0);
                // 把模板的名字带进去
                list.get(0).setMouldName(mouldName);
                splitTopMouldBean.setAgencyId(childAgencyId);
                // 添加进数据库
                result = splitMouldService.addTopMould(splitTopMouldBean);
                // 将模板信息分配到机构分润表里面
                List<SplitMouldBean> mouldlist = splitMouldService.getMouldsDT(ruleId, mouldName, agencyId);
                if (mouldlist != null && mouldlist.size() > 0) {
                    for (int i = 0; i < mouldlist.size(); i++) {
                        splitMouldService.addOrgMould(mouldlist.get(i), childAgencyId, createDt);
                    }
                }

            } else {
                // 已经分配过的话，就是替换原来的
                // 根据自己的agencyId，和RuleId得到模板原来的信息
                List<SplitTopMouldBean> list = splitMouldService.getTopMouldByAR(agencyId, ruleId);
                // 把这条记录agencyId,换成子机构
                SplitTopMouldBean splitTopMouldBean = list.get(0);
                // 把模板的名字带进去
                list.get(0).setMouldName(mouldName);
                splitTopMouldBean.setAgencyId(childAgencyId);
                // 更新顶级模板表
                result = splitMouldService.updateTopMouldD(splitTopMouldBean);
                // 把原来的模板放进机构分润历史表，并且在机构分润表里面删除
                splitMouldService.updateUpperDistrMould(childAgencyId, ruleId, mouldName, createDt, agencyId);
                // 在机构模板表里面新增新的
                List<SplitMouldBean> mouldlist = splitMouldService.getMouldsDT(ruleId, mouldName, agencyId);
                if (mouldlist != null && mouldlist.size() > 0) {
                    for (int i = 0; i < mouldlist.size(); i++) {
                        splitMouldService.addOrgMould(mouldlist.get(i), childAgencyId, createDt);
                    }
                }
                result = 88;
            }

            ajaxinfo = null;
            String status = "";
            if (result == 0) {
                ajaxinfo = AjaxReturnInfo.failed("分配失败");
                status = "分配失败";
            } else if (result == 88) {
                ajaxinfo = AjaxReturnInfo.success("已用新模板替换原有模板");
                status = "已用新模板替换原有模板";
            } else {
                AjaxReturnInfo.success("分配成功");
                status = "分配成功";
            }
            // 添加操作详情 20141203
            String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
            String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
            String operateDetail = "分配模板给：" + childAgencyId + "，分配状态：" + status;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEMOULDDISTVIEW, ConstantUtils.OPERTYPESER, operateDetail);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ajaxinfo;
    }

    /**
     * 顶级机构获得分配情况
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getDistrMoulds")
    @ResponseBody
    public Map<String, Object> getDistrMoulds(@RequestParam(value = "mouldName") String mouldName, @RequestParam(value = "status") String status, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        if (mouldName == null || "".equals(mouldName) || "-1".equals(mouldName)) {
            mouldName = "";
        }
        List<SplitTopMouldBean> list = splitMouldService.getDistrMoulds(agencyId, mouldName, status);
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitTopMouldBean> ruleList = new ArrayList<SplitTopMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }

        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + mouldName;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEMOULDDISTVIEW, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 顶级(上级)机构获得分配情况
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getDistrMouldsD")
    @ResponseBody
    public Map<String, Object> getDistrMouldsD(@RequestParam(value = "ruleId") String ruleId, @RequestParam(value = "agencyId") String agencyId, HttpServletRequest request) {

        List<SplitMouldBean> list = splitMouldService.getDistrMouldsD(ruleId, agencyId);
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitMouldBean> ruleList = new ArrayList<SplitMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 获取可供选择的模板
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getSelectMoulds")
    @ResponseBody
    public Map<String, Object> getSelectMoulds(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);

        List<SplitTopMouldBean> list = splitMouldService.getSelectMoulds(agencyId);
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitTopMouldBean> ruleList = new ArrayList<SplitTopMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
    }

    /**
     * 上级 用户获得分配给他的机构模板规则列表 获得修改的机构模板规则列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getOrgMoulds")
    @ResponseBody
    public Map<String, Object> getOrgMoulds(
    // 获取传过来的ruleId
            @RequestParam(value = "ruleId") String ruleId, @RequestParam(value = "mouldName") String mouldName, HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        // 判断如果ruleId为空，则说明是机构在添加
        if (ruleId == null || ruleId.equals("") || "-1".equals(ruleId)) {
            // 从顶级模板表里取它的rullId
            ruleId = splitMouldService.getTopMoulds(agencyId, null).get(0).getRuleId();
        }
        if (mouldName == null || "".equals(mouldName) || "-1".equals(mouldName)) {
            mouldName = "";
        }
        List<SplitOrgMouldBean> listTemp = splitMouldService.getOrgMoulds(ruleId, agencyId, mouldName);
        List<SplitOrgMouldBean> list = new ArrayList<SplitOrgMouldBean>();
        // 判断前台传过来的mouldName,如果没有值，说明要过滤掉已经启用的
        if (mouldName == null || mouldName.equals("")) {
            if (listTemp != null && listTemp.size() > 0) {
                for (int i = 0; i < listTemp.size(); i++) {
                    // 通过判断这条记录里面的mouldName是不是存在，来确定这个模板是不是增加过的
                    if (ConstantUtils.NOMOULDNAME.equals(listTemp.get(i).getMouldName())) {
                        list.add(listTemp.get(i));
                    }
                }
            }
        } else {
            // 判断前台传过来的mouldName,如果有值则不需要过滤
            if (listTemp != null && listTemp.size() > 0) {
                for (int i = 0; i < listTemp.size(); i++) {
                    list.add(listTemp.get(i));
                }
            }
        }

        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<SplitOrgMouldBean> ruleList = new ArrayList<SplitOrgMouldBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                ruleList.add(list.get(i));
            }
        }
        Map<String, Object> jsonMap = AjaxReturnInfo.setTable(list == null ? 0 : list.size(), ruleList);
        jsonMap.put("nowDate", new SimpleDateFormat("yyyy-MM-01").format(new Date()));
        return jsonMap;
    }

    /**
     * 获得已经添加过的机构模板列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getAddedOrgMoulds")
    @ResponseBody
    public Map<String, Object> getAddedOrgMoulds(
    // 获取传过来的ruleId
            @RequestParam(value = "mouldName") String mouldName, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        List<SplitOrgMouldBean> list = splitMouldService.getAddedOrgMoulds(agencyId, mouldName);
        // 过滤掉这条list里未设置的模板
        List<SplitOrgMouldBean> listTemp = new ArrayList<SplitOrgMouldBean>();
        for (int i = 0; i < list.size(); i++) {
            // 通过判断这条记录里面的mouldName是不是存在，来确定这个模板是不是增加过的
            if (!ConstantUtils.NOMOULDNAME.equals(list.get(i).getMouldName())) {
                listTemp.add(list.get(i));
            }
        }
        ;

        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "40" : rows);
        int start = (pagenumber - 1) * rownumber;

        List<SplitOrgMouldBean> ruleList = new ArrayList<SplitOrgMouldBean>();
        if (listTemp != null && listTemp.size() > 0) {
            int end = (start + rownumber) > listTemp.size() ? listTemp.size() : start + rownumber;
            for (int i = start; i < end; i++) {
                ruleList.add(listTemp.get(i));
            }
        }
        ;
        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + mouldName;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEORGSPLITMOULD, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(ruleList == null ? 0 : list.size(), ruleList);
    }

    /**
     * 添加机构分润模板规则
     * 
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */

    @RequestMapping(params = "method=addOrgMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo addOrgMould(
    // @RequestBody String jsonStr,
            @RequestParam(value = "validityDate") String validityDate, @RequestParam(value = "mouldName") String mouldName, @RequestParam(value = "jsonStr") String jsonStr, HttpServletRequest request)
            throws UnsupportedEncodingException {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();

        AjaxReturnInfo ajaxinfo = null;
        Gson gson = new Gson();
        int result = 0;
        List<SplitOrgMouldBean> beanList = gson.fromJson(jsonStr, new TypeToken<List<SplitOrgMouldBean>>() {
        }.getType());
        String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
        /*
         * String validityDate =request.getParameter("validityDate"); //转码
         * String mouldName =new
         * String(request.getParameter("mouldName").getBytes
         * ("ISO-8859-1"),"UTF-8");
         */
        try {
			// 判断是否有重名
			if (splitMouldService.checkOrgMouldDup(agencyId, mouldName)) {
			    // 不重名的话，保存进数据库
			    result = addOrgMouldData(beanList, validityDate, mouldName, createrId, agencyId, result);
			} else {
			    // 重名的话
			    result = 88;
			}

			if (result == 0) {
			    ajaxinfo = AjaxReturnInfo.failed("设置失败");
			} else if (result == 88) {
			    ajaxinfo = AjaxReturnInfo.failed("对不起，模板名称重复，请改正后再试");
			} else {
			    // 添加操作详情 20141203
			    String operateDetail = "添加模板" + mouldName;
			    // 添加进系统日志表 20141203
			    systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEORGSPLITMOULD, ConstantUtils.OPERTYPEADD, operateDetail);
			    ajaxinfo = AjaxReturnInfo.success("设置成功");
			}
		} catch (QTException e) {
			 ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
		}
        return ajaxinfo;

    }

    /**
     * 机构在机构模板设置时，对模板所做的修改
     * 
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */

    @RequestMapping(params = "method=updateOrgMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo updateOrgMould(
            // @RequestBody String jsonStr,
            @RequestParam(value = "validityDate") String validityDate, @RequestParam(value = "mouldName") String mouldName, @RequestParam(value = "ruleId") String ruleId,
            @RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "jsonStr") String jsonStr, HttpServletRequest request){
        // 获得ip
        AjaxReturnInfo ajaxinfo;
		try {
			String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
			String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
			String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

			ajaxinfo = null;
			Gson gson = new Gson();
			int result = 0;
			List<SplitOrgMouldBean> beanList = gson.fromJson(jsonStr, new TypeToken<List<SplitOrgMouldBean>>() {
			}.getType());
			String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);
			/*
			 * String validityDate =request.getParameter("validityDate"); //转码
			 * String mouldName =new
			 * String(request.getParameter("mouldName").getBytes
			 * ("ISO-8859-1"),"UTF-8"); String ruleId
			 * =request.getParameter("ruleId"); String agencyId
			 * =request.getParameter("agencyId");
			 */

			// 机构分润表，机构分润历史表更新
			result = updateOrgMouldData(beanList, createrId, agencyId, ruleId, mouldName, validityDate, result);

			if (result == 0) {
			    ajaxinfo = AjaxReturnInfo.failed("设置失败");
			} else {
			    // 添加操作详情 20141203
			    String operateDetail = "修改模板" + mouldName;
			    // 添加进系统日志表 20141203
			    systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEORGSPLITMOULD, ConstantUtils.OPERTYPEADD, operateDetail);
			    ajaxinfo = AjaxReturnInfo.success("设置成功");
			}
		}catch (QTException e) {
			ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
		}catch (Exception e) {
		  e.printStackTrace();  
	    ajaxinfo = AjaxReturnInfo.failed(e.getMessage());
	    }
        return ajaxinfo;

    }

    /**
     * 机构模板分配查看时，对模板所做的修改
     * 
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */

    @RequestMapping(params = "method=updateOrgMouldD", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo updateOrgMouldD(@RequestBody String jsonStr, HttpServletRequest request) throws UnsupportedEncodingException {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        AjaxReturnInfo ajaxinfo = null;
        Gson gson = new Gson();
        int result = 0;
        List<SplitOrgMouldBean> beanList = gson.fromJson(jsonStr, new TypeToken<List<SplitOrgMouldBean>>() {
        }.getType());
        String createrId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.USERID);

        // mouldName是分配之后的
        String mouldName = ConstantUtils.NOMOULDNAME;
        String ruleId = request.getParameter("ruleId");
        String agencyId = request.getParameter("agencyId");
        // String agencyId =
        // request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        try {
			// 机构分润表，机构分润历史表更新
			result = updateOrgMouldDataD(beanList, createrId, agencyId, ruleId, mouldName, result);
			 if (result == 0) {
		            ajaxinfo = AjaxReturnInfo.failed("设置失败");
		        } else {
		            // 添加操作详情 20141203
		            String operateDetail = "修改模板ID" + ruleId;
		            // 添加进系统日志表 20141203
		            systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMEORGMOULDDISTVIEW, ConstantUtils.OPERTYPEUPD, operateDetail);
		            ajaxinfo = AjaxReturnInfo.success("设置成功");
		        }
		} catch (QTException e) {
			 ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
		}catch (Exception e) {
		    e.printStackTrace();
	      ajaxinfo = AjaxReturnInfo.failed(e.getMessage());
	    }
        return ajaxinfo;
    }

    /**
     * 设置机构分润模板规则Bean数据
     * 
     * @param beanList
     * @param validityData
     * @param result
     * @return
     * @throws QTException 
     */
    private int addOrgMouldData(List<SplitOrgMouldBean> beanList, String validityDate, String mouldName, String createId, String agencyId, int result) throws QTException {
        String createDt = CommonDate.getDate();
        //zhangyinghui 修改分润模板比不能设置大于上级机构
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            //验证设置规则值是否大于上级机构
            validateMouldRulevalue(bean,agencyId);
        }
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            bean.setCreateDate(createDt);
            bean.setCreateId(createId);
            bean.setAgencyId(agencyId);
            bean.setValidityDate(validityDate);
            bean.setMouldName(mouldName);
            result = splitMouldService.addOrgMouldData(bean);
        }
        return result;
    }

    /**
     * 机构在机构模板设置时，更新机构分润模板规则Bean数据
     * 
     * @param beanList
     * @param validityData
     * @param result
     * @return
     * @throws UnsupportedEncodingException
     * @throws QTException 
     */
    private int updateOrgMouldData(List<SplitOrgMouldBean> beanList, String createId, String agencyId, String ruleId, String mouldName, String validateDate, int result)
            throws UnsupportedEncodingException, QTException {
        String createDt = CommonDate.getDate();
        //zhangyinghui 验证修改分润模板比不能设置大于上级机构
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            if (bean.getRuleValue() != splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId)
                    || !validateDate.equals(splitMouldService.getOrgValidityDate(bean.getRuleNum(), ruleId, mouldName, agencyId))) {
              validateMouldRulevalue(bean,agencyId);
            }
        }
        // 循环查找是否有改动的
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            // 得到数据库的原始数据,如果有效期或者值不相等，则更新机构模板表，并且将原值放入分润模板历史表
            if (bean.getRuleValue() != splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId)
                    || !validateDate.equals(splitMouldService.getOrgValidityDate(bean.getRuleNum(), ruleId, mouldName, agencyId))) {
            	// 取出原来的值和原来的生效日期
                int valueOld = splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId);
                String validityDateOld = splitMouldService.getOrgValidityDate(bean.getRuleNum(), ruleId, mouldName, agencyId);
                // 取出原数据，放入分润模板历史表
                splitMouldService.addHisMould(bean, createDt, createId, agencyId, validityDateOld, valueOld);

                // 将bean里的mouldName
                bean.setMouldName(mouldName);
                // 更新机构分润表,把日期赋上
                bean.setValidityDate(validateDate);
                //验证设置规则值是否大于上级机构
                result = splitMouldService.updateOrgMouldData(bean);
            } else {
                // 如果没有执行以上，说明没有修改给result赋值
                result = 1;
            }
        }
        return result;
    }

    /**
     * 机构在模板分配查看时，更新机构分润模板规则Bean数据
     * 
     * @param beanList
     * @param validityData
     * @param result
     * @return
     * @throws UnsupportedEncodingException
     */
    private int updateOrgMouldDataD(List<SplitOrgMouldBean> beanList, String createId, String agencyId, String ruleId, String mouldName, int result) throws Exception {
        String createDt = CommonDate.getDate();
        // zhangyinghui修改 循环查找是否有改动的,判断是否符合不能大于上级分润比设置规则
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            // 得到数据库的原始数据,如果有效期或者值不相等，判断设置规则
            if (bean.getRuleValue() != splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId)) {
                validateMouldRulevalue(bean, agencyId);
            }
        }
        // 循环查找是否有改动的
        for (int i = 0; i < beanList.size(); i++) {
            SplitOrgMouldBean bean = beanList.get(i);
            // 得到数据库的原始数据,如果有效期或者值不相等，则更新机构模板表，并且将原值放入分润模板历史表
            if (bean.getRuleValue() != splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId)) {
                // 取出原来的值
                int valueOld = splitMouldService.getOrgValue(bean.getRuleNum(), ruleId, mouldName, agencyId);
                // 取出原数据，放入分润模板历史表
                splitMouldService.addHisMouldD(bean, createDt, createId, agencyId, valueOld);
                result = splitMouldService.updateOrgMouldData(bean);
            } else {
                // 如果没有执行以上，说明没有修改给result赋值
                result = 1;
            }
        }

        return result;
    }

    /**
     * 
     * 【方法名】    : (验证规则值是否大于上级机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月13日 下午8:05:01 .<br/>
     * 【参数】： .<br/>
     * @param bean SplitOrgMouldBean
     * @param agencyId 机构ID
     * @throws QTException .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    private void validateMouldRulevalue(SplitOrgMouldBean bean,String agencyId)throws QTException{
    	Map<String,Object> map = new HashMap<String, Object>();
    	map.put("agencyId", agencyId);
    	logger.info(agencyId);
    	map.put("dealType", bean.getDealType());
    	logger.info(bean.getDealType());
    	map.put("splittingMode", bean.getSplittingMode());
    	logger.info(bean.getSplittingMode());
    	map.put("splittingRegionmode", bean.getSplittingRegionMode());
    	logger.info(bean.getSplittingRegionMode());
    	double mouldRule = bean.getRuleValue();
    	double mouldRuleUpper = splitMouldService.queryMouldRulevalueUpper(map).doubleValue();
    	//按金额
    	if(FrConstants.splittingModel.equals(bean.getSplittingMode())){
    	    mouldRuleUpper = mouldRuleUpper*100;
    	}
    	if( mouldRuleUpper != 0 && mouldRule > mouldRuleUpper){
    		throw new QTException("设置错误！规则值不能大于所属上级！");
    	}
    	
    }
    
    /**
     * 上级机构 根据agency，获得自己名下的模板
     * 
     * @param agencyId
     * @return
     */
    @RequestMapping(params = "method=getTopMouldList")
    @ResponseBody
    public List<Map<String, Object>> getTopMouldList(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择模板名称");
        map.put("selected", true);
        list.add(map);
        List<SplitTopMouldBean> beanlist = splitMouldService.getTopMouldList(agencyId);
        for (int i = 0; i < beanlist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", beanlist.get(i).getMouldName());
            map1.put("text", beanlist.get(i).getMouldName());
            list.add(map1);
        }

        return list;
    }

    /**
     * 顶级机构 根据agency，获得自己名下的模板
     * 
     * @param agencyId
     * @return
     */
    @RequestMapping(params = "method=topGetMouldList")
    @ResponseBody
    public List<Map<String, Object>> topGetMouldList(HttpServletRequest request) {
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? "" : (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ConstantUtils.COMBOXONEID);
        map.put("text", "请选择模板名称");
        map.put("selected", true);
        list.add(map);
        List<SplitTopMouldBean> beanlist = splitMouldService.topGetMouldList(agencyId);
        for (int i = 0; i < beanlist.size(); i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("id", beanlist.get(i).getMouldName());
            map1.put("text", beanlist.get(i).getMouldName());
            list.add(map1);
        }
        return list;
    }

    /**
     * 修改顶级分润规则状态
     * 
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping(params = "method=updateStatusTopMould", method = RequestMethod.POST)
    @ResponseBody
    public AjaxReturnInfo updateStatusTopMould(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "ruleId") String ruleId, @RequestParam(value = "status") String status,
            HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        AjaxReturnInfo ajaxinfo = null;
        ajaxinfo = AjaxReturnInfo.failed("更新失败");
        // 判断是否重名
        // 不重名的话则修改
        SplitTopMouldBean splitTopMouldBean = new SplitTopMouldBean();
        splitTopMouldBean.setAgencyId(agencyId);
        splitTopMouldBean.setStatus(Integer.parseInt(status));
        splitTopMouldBean.setRuleId(ruleId);
        splitMouldService.updateTopMould(splitTopMouldBean);
        splitTopMouldBean = null;
        // 添加操作详情 20141203
        String operateDetail = "修改模板状态为" + status;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESPLITMOULDMANAGE, ConstantUtils.OPERTYPEUPD, operateDetail);

        ajaxinfo = AjaxReturnInfo.success("更新成功");
        return ajaxinfo;
    }


    /**
     * 方法名： getActivateStandard.<br/>
     * 方法作用:参数配置分润模板中交易类型下拉列表.<br/>
     * 创建者：zhangjun.<br/>
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午3:15:17.<br/>
     * 参数者异常：@param request
     * 返回值： @return 返回结果：List<Map<String,Object>>.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    
    @RequestMapping(params = "method=getMouldsDealType")
    @ResponseBody
    public List<Map<String, Object>> getActivateStandard(HttpServletRequest request) {
        String flag = request.getParameter("flag");
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        String paramvalue = authorityService.getParamObject("MOULDSDEALTYPE");
        String array[] = paramvalue.split("\\|");
        Map<String, Object> map = null;
        // 如果参数为1 添加 ”请选择记录“
        if ("1".equals(flag)) {
            map = new HashMap<String, Object>();
            map.put("id", ConstantUtils.COMBOXONEID);
            map.put("text", "请选择交易类型");
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

}
