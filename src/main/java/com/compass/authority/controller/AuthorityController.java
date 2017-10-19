package com.compass.authority.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.authority.model.AuthorityBean;
import com.compass.authority.model.ParamBean;
import com.compass.authority.service.AuthorityService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/authority/authority.do")
public class AuthorityController {
    @Autowired
    @Qualifier("authorityService")
    private AuthorityService authorityService;

    // 系统日志service
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService systemLogService;

    /**
     * 根据条件查询菜单
     * 
     * @param powerName
     * @param status
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getAuthority")
    @ResponseBody
    public Map<String, Object> getAuthority(@RequestParam(value = "powerName") String powerName, @RequestParam(value = "status") String status,
            HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        List<AuthorityBean> list = authorityService.getAuthority(powerName, status);
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
        List<AuthorityBean> menulist = new ArrayList<AuthorityBean>();
        if (list != null && list.size() > 0) {
            for (int i = start; i < end; i++) {
                menulist.add(list.get(i));
            }
        }
        // 添加操作详情 20141203
        String operateDetail = "查询条件为" + powerName + status;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(list == null ? 0 : list.size(), menulist);
    }

    /**
     * 添加菜单功能
     * 
     * @param powerName
     * @param Status
     * @param remark
     * @param parentId
     * @param menuDesc
     * @param request
     * @return
     */
    @RequestMapping(params = "method=addAuthority")
    @ResponseBody
    public AjaxReturnInfo addAuthority(@RequestParam(value = "powerName") String powerName, @RequestParam(value = "Status") String Status,
            @RequestParam(value = "comment") String comment, @RequestParam(value = "parentId") String parentId,
            @RequestParam(value = "menuDesc") String menuDesc, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        AuthorityBean ab = new AuthorityBean();
        String menuId = authorityService.getMenuId().toString();
        ab.setMenuName(powerName);
        ab.setMenuStatus(Status);
        ab.setMenuDesc(menuDesc);
        ab.setComment(comment);
        ab.setParentNodeId(parentId);
        ab.setMenuId(menuId);

        String createId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        ab.setCreateId(createId);
        ab.setCreateDt(CommonDate.getDate());
        int result = authorityService.add(ab);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("保存失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "添加菜单" + powerName;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEADD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("保存成功");
        }
        return ajaxinfo;
    }

    /**
     * 修改菜单功能
     * 
     * @param powerName
     * @param Status
     * @param remark
     * @param powerId
     * @param menuDesc
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(params = "method=updateAuthority")
    @ResponseBody
    public AjaxReturnInfo updateAuthority(@RequestParam(value = "powerName") String powerName, @RequestParam(value = "Status") String Status,
            @RequestParam(value = "comment") String comment, @RequestParam(value = "powerId") String powerId,
            @RequestParam(value = "menuDesc") String menuDesc, @RequestParam(value = "parentId") String parentId, HttpServletRequest request) {
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        AuthorityBean ab = new AuthorityBean();
        ab.setMenuName(powerName);
        ab.setMenuStatus(Status);
        ab.setComment(comment);
        ab.setMenuId(powerId);
        ab.setMenuDesc(menuDesc);
        ab.setParentNodeId(parentId);
        String createId = request.getSession().getAttribute(ConstantUtils.USERID) + "";
        ab.setOperator(createId);
        ab.setOperationTime(CommonDate.getDate());
        int result = authorityService.update(ab);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("修改失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "修改菜单" + powerName;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEUPD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("修改成功");
        }
        ab = null;
        return ajaxinfo;
    }

    /**
     * 删除菜单功能
     * 
     * @param Ids
     * @return
     */
    @RequestMapping(params = "method=deleteAuthority")
    @ResponseBody
    public AjaxReturnInfo deleteAuthority(@RequestParam(value = "Ids") String Ids, HttpServletRequest request) { // ids
                                                                                                                    // 为多个ID
                                                                                                                    // 中间用','隔开
        AjaxReturnInfo ajaxinfo = null;
        // 获得ip
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        int result = authorityService.delete(Ids);
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("删除失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "删除菜单Id为" + Ids;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEDEL, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("删除成功");
        }
        return ajaxinfo;
    }

    /**
     * 获得所有菜单并且生成树
     * 
     * @return
     */
    @RequestMapping(params = "method=getAuthTree")
    @ResponseBody
    public List<Map<String, Object>> getAuthTree() {
        List<AuthorityBean> list = authorityService.getAuTree();
        return createAuthTree(list, "-1");// -1为跟菜单的父节点，依据为数据库根菜单的 父菜单ID
    }

    /**
     * 遍历所有数据拼接生成树形结构需要的json数据
     * 
     * @param list
     * @param id
     * @return
     */
    public List<Map<String, Object>> createAuthTree(List<AuthorityBean> list, String id) {
        List<Map<String, Object>> aulist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = null;
            AuthorityBean at = list.get(i);
            if (id.equals(at.getParentNodeId())) {
                map = new HashMap<String, Object>();
                map.put("id", at.getMenuId());
                map.put("text", at.getMenuName());
                if ("0".equals(at.getChildCode())) {
                    map.put("state", "open");
                } else {
                    map.put("state", "open");
                    map.put("children", createAuthTree(list, at.getMenuId()));
                }
            }
            if (map != null)
                aulist.add(map);
        }
        return aulist;
    }

    /**
     * 获取根菜单中的所有直属节点，供下拉框使用
     * 
     * @return
     */
    @RequestMapping(params = "method=getParentMenu")
    @ResponseBody
    public List<Map<String, Object>> getParentMenu() {
        List<AuthorityBean> list = authorityService.getParentMenu();
        List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", ConstantUtils.COMBOXONEID);
        map1.put("text", "请选择上级菜单");
        map1.put("selected", true);
        maplist.add(map1);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", list.get(i).getMenuId());
            map.put("text", list.get(i).getMenuName());
            maplist.add(map);
        }
        return maplist;
    }

    /**
     * 方法名： getFrParam.<br/>
     * 方法作用:查询参数.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:15:05.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@param paramName
     * 参数者异常：@param req
     * 参数者异常：@return .<br/>
     * 返回值： ：Map<String,Object>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = "method=getFrParam")
    @ResponseBody
    public Map<String, Object> getFrParam(@RequestParam(value = "paramId") String paramId, @RequestParam(value = "paramName") String paramName,
            HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        int count = authorityService.getParamCount(paramId, paramName);
        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = (start + rownumber) > count ? count : start + rownumber;
        List<ParamBean> list = authorityService.getParam(paramId, paramName, start, end);
        String operateDetail = "查询条件为" + paramId + paramName;
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPESER, operateDetail);
        return AjaxReturnInfo.setTable(list == null ? 0 : count, list);
    }

    /**
     * 
     * 方法名： addParam.<br/>
     * 方法作用:新增参数.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:14:45.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@param paramName
     * 参数者异常：@param paramValue
     * 参数者异常：@param paramDes
     * 参数者异常：@param request
     * 参数者异常：@return .<br/>
     * 返回值： AjaxReturnInfo.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = "method=addParam")
    @ResponseBody
    public AjaxReturnInfo addParam(@RequestParam(value = "paramId") String paramId, @RequestParam(value = "paramName") String paramName,
            @RequestParam(value = "paramValue") String paramValue, @RequestParam(value = "paramDes") String paramDes, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        ParamBean paramBean = new ParamBean();
        paramBean.setParamDes(paramDes);
        paramBean.setParamId(paramId);
        paramBean.setParamName(paramName);
        paramBean.setParamValue(paramValue);
        int count = authorityService.getParam(paramId, null, 0, 10).size();
        AjaxReturnInfo ajaxinfo = null;
        if (count != 0) {
            ajaxinfo = AjaxReturnInfo.failed("该参数已经存在");
        } else {
            int result = authorityService.addParam(paramBean);
            if (result == 0) {
                ajaxinfo = AjaxReturnInfo.failed("保存失败");
            } else {
                String operateDetail = "添加参数" + paramId;
                systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEADD, operateDetail);
                ajaxinfo = AjaxReturnInfo.success("保存成功");
            }
        }

        return ajaxinfo;
    }

    /**
     * 
     * 方法名： updateParam.<br/>
     * 方法作用:更新参数.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:14:17.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@param paramName
     * 参数者异常：@param paramValue
     * 参数者异常：@param paramDes
     * 参数者异常：@param request
     * 参数者异常：@return .<br/>
     * 返回值： AjaxReturnInfo.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = "method=updateParam")
    @ResponseBody
    public AjaxReturnInfo updateParam(@RequestParam(value = "paramId") String paramId, @RequestParam(value = "paramName") String paramName,
            @RequestParam(value = "paramValue") String paramValue, @RequestParam(value = "paramDes") String paramDes, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        ParamBean paramBean = new ParamBean();
        paramBean.setParamDes(paramDes);
        paramBean.setParamId(paramId);
        paramBean.setParamName(paramName);
        paramBean.setParamValue(paramValue);
        int result = authorityService.updateParam(paramBean);
        AjaxReturnInfo ajaxinfo = null;
        if (result == 0) {
            ajaxinfo = AjaxReturnInfo.failed("更新失败");
        } else {
            // 添加操作详情 20141203
            String operateDetail = "更新参数" + paramId;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEADD, operateDetail);
            ajaxinfo = AjaxReturnInfo.success("更新成功");
        }
        return ajaxinfo;
    }

    /**
     * 
     * 方法名： deleteParam.<br/>
     * 方法作用:删除参数.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:13:51.<br/>
     * 参数者异常：@param ids
     * 参数者异常：@param request
     * 参数者异常：@return .<br/>
     * 返回值： AjaxReturnInfo.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    @RequestMapping(params = "method=deleteParam")
    @ResponseBody
    public AjaxReturnInfo deleteParam(@RequestParam(value = "ids") String ids, HttpServletRequest request) {
        String ipAddress = request.getSession().getAttribute(ConstantUtils.IPADDRESS).toString();
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        AjaxReturnInfo ajaxinfo = null;
        if (StringUtils.isEmpty(ids)) {
            ajaxinfo = AjaxReturnInfo.success("请选择参数ID");
            return ajaxinfo;
        }
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append("(");
        for (String str : ids.split(":")) {
            sbBuffer.append("'" + str + "',");
        }
        sbBuffer.deleteCharAt(sbBuffer.length() - 1);
        sbBuffer.append(")");
        int result = authorityService.deleteParam(sbBuffer.toString());

        String operateDetail = "删除参数" + sbBuffer.toString();
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEAUTHORITY, ConstantUtils.OPERTYPEADD, operateDetail);
        ajaxinfo = AjaxReturnInfo.success("删除成功");
        return ajaxinfo;
    }
}
