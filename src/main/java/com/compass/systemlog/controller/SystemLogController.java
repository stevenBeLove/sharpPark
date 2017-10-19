package com.compass.systemlog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.systemlog.model.OperateTypeBean;
import com.compass.systemlog.model.SystemLogBean;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/systemlog/systemlog.do")
public class SystemLogController {
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService systemLogService;
    
    private static Logger       logger = LoggerFactory.getLogger(SystemLogController.class);

    /**
     * 获取当前机构下的交易明细
     * 
     * @param yearmonth
     * @param agencyId
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getLogDetail")
    @ResponseBody
    public Map<String, Object> getLogDetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart, @RequestParam(value = "yearmonthdateend") String yearmonthdateend,
            @RequestParam(value = "agencySelect") String agencySelect, @RequestParam(value = "operateMan") String operateMan, @RequestParam(value = "operateName") String operateName,
            @RequestParam(value = "operateType") String operateType, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getRemoteAddr();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        if ("-1".equals(operateType)) {
            operateType = null;
        }
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int totalCount = systemLogService.getSystemLogCount(yearmonthdatestart, yearmonthdateend, agencySelect, operateMan, operateName, operateType);

        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > totalCount ? totalCount : start + rownumber;
        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + yearmonthdatestart + yearmonthdateend + agencySelect;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMESYSLOG, ConstantUtils.OPERTYPESER, operateDetail);

        List<SystemLogBean> list = systemLogService.getSystemLogdetail(yearmonthdatestart, yearmonthdateend, agencySelect, operateMan, operateName, operateType, start, end);
        return AjaxReturnInfo.setTable(totalCount, list);
    }

    /**
     * 获取所有的操作类型
     * 
     * @param yearmonth
     * @param agencyId
     * @param request
     * @return
     */
    @RequestMapping(params = "method=getOperateType")
    @ResponseBody
    public List<Map<String, Object>> getOperateType(HttpServletRequest request) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        List<OperateTypeBean> operateTypeList = systemLogService.getOperateType();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "-1");
        map1.put("text", "请选择");
        map1.put("selected", true);
        list.add(map1);
        map1 = null;
        for (int i = 0; i < operateTypeList.size(); i++) {
            OperateTypeBean bean = operateTypeList.get(i);
            map1 = new HashMap<String, Object>();
            map1.put("id", bean.getOperateTypeId());
            map1.put("text", bean.getOperateType());
            list.add(map1);
            map1 = null;
        }
        return list;
    }

    /**
     * 
     * 【方法名】 :获取登录提示标识session. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月1日 上午9:46:45 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    @RequestMapping(params = "method=getOpenLoginShowFlag")
    @ResponseBody
    public String haveOrNotHave(HttpServletRequest request) {
        String flag = null;
        try {
            flag = request.getSession().getAttribute(ConstantUtils.LOGINSHOWFLAG).toString();
            if (!StringUtils.isEmpty(flag)) {
                return "1";
            } else {
                return "0";
            }
        } catch(NullPointerException e) { 
            logger.info( "发生空指针异常，登录提示标识SESSION值已被移除");
        }
        return flag;
    }

    /**
     * 
     * 【方法名】 : 去除登录提示标识session. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月1日 上午9:47:13 .<br/>
     * 【参数】： .<br/>
     * 
     * @param request
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: fuyu 修改描述： .<br/>
     *            <p/>
     */
    @RequestMapping(params = "method=getCloseLoginShowFlag")
    @ResponseBody
    public AjaxReturnInfo removeLoginShowFlag(HttpServletRequest request) {
        AjaxReturnInfo ajaxinfo = null;
        try {
            request.getSession().removeAttribute(ConstantUtils.LOGINSHOWFLAG);
            ajaxinfo = AjaxReturnInfo.success("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxinfo = AjaxReturnInfo.failed("修改失败");
        }
        return ajaxinfo;
    }
}