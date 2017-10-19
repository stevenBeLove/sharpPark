/**
 * 
 */
package com.compass.users.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.SpecSystemBean;
import com.compass.agency.service.AgencyService;
import com.compass.authority.service.AuthorityService;
import com.compass.role.service.RoleService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.model.SystemLogBean;
import com.compass.systemlog.service.SystemLogService;
import com.compass.userrole.model.UserRoleBean;
import com.compass.userrole.service.UserRoleService;
import com.compass.users.model.MenuBean;
import com.compass.users.model.PwdStrategy;
import com.compass.users.model.UserBean;
import com.compass.users.service.UsersService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.MD5;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangyuchao
 * 
 */
@Controller
@RequestMapping("/users/users.do")
public class UsersController {

    private static Logger       log = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    @Qualifier("usersService")
    private UsersService        usersService;

    @Autowired
    @Qualifier("roleService")
    private RoleService         roleService;

    @Autowired
    @Qualifier("userRoleService")
    private UserRoleService     userRoleService;

    @Autowired
    @Qualifier("agencyService")
    private AgencyService       agencyService;

    @Autowired
    @Qualifier("systemManageService")
    private SystemManageService systemManageService;

    // 系统日志service
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService    systemLogService;

    @Autowired
    @Qualifier("authorityService")
    private AuthorityService    authorityService;

    /**
     * 用户登录
     * 
     * @param loginname
     * @param password
     *            // * @param agencyId
     * @param request
     *            请求参数
     * @return 返回参数
     * @throws ParserConfigurationException
     *             转换配置异常
     * @throws SAXException
     *             异常
     * @throws IOException
     *             异常
     */
    @RequestMapping(params = "method=checkLogin")
    @ResponseBody
    public AjaxReturnInfo checkLogin(SystemLogBean systemLogBean, @RequestParam(value = "loginname") String loginname, @RequestParam(value = "password") String password,
    // @RequestParam(value = "agencyId") String agencyId,
    // @RequestParam(value = "systemId") String systemId,
            HttpServletRequest request) throws ParserConfigurationException, SAXException, IOException {
        AjaxReturnInfo ajaxinfo = null;
        try {
			log.info("用户登录 : [" + loginname + ", " + password + "]");
			// 获得ip
			String ipAddress = getRemoteHost(request);

			// 获得角色id
			String userId = request.getSession().getAttribute(ConstantUtils.USERID) == null ? "" : request.getSession().getAttribute(ConstantUtils.USERID).toString();
			userId = "";
			if ("".equals(userId)) {
			    // 根据用户登录名查询数据库获取来源系统和机构编号
			    UserRoleBean user = new UserRoleBean();
			    if(!ConstantUtils.ADMIN.equals(loginname)){
			        if(loginname.toUpperCase().startsWith(ConstantUtils.RTBPREFIX)){
			            loginname = ConstantUtils.RTBPREFIX + loginname.substring(3);
		          }
			    }
			    user.setLoginName(loginname);
			    List<UserRoleBean> userRoleBeans = userRoleService.getUserRole(user);

			    if (userRoleBeans != null && userRoleBeans.size() != 0) {
			        String userPwd = userRoleBeans.get(0).getPassword();
			        String agencyIdS = userRoleBeans.get(0).getAgencyid();
			        String userIdS = userRoleBeans.get(0).getUserId();
			        if (userPwd == null || !userPwd.equals(password)) {
			            log.info(loginname + "密码不正确");
			            // 添加操作详情 20141203
			            String operateDetail = "密码不正确" + password;
			            // 添加进系统日志表 20141203
			            systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGONFAIL, operateDetail);
			            ajaxinfo = AjaxReturnInfo.failed("请检验用户名，密码是否正确");
			            return ajaxinfo;
			        }

			        String systemId = null;
			        String companyBriefName = null;

			        // ibatis字段不区分大小写，而UserRoleBean中agencyId和agencyid
			        // agencyId = userRoleBeans.get(0).getAgencyId();

			        AgencyBean agency = new AgencyBean();
			        agency.setAgency_id(agencyIdS);
			        List<AgencyBean> agencyBeans = agencyService.getAgency(agency);
			        
			        if (agencyBeans != null && agencyBeans.size() != 0) {
			            systemId = agencyBeans.get(0).getSystemId();
			            companyBriefName = agencyBeans.get(0).getCompanyBriefName();
			            // 如果企业简称未登记，则取系统编号
			            if (companyBriefName == null) {
			                companyBriefName = systemId;
			            }
			            List<UserBean> list = usersService.checkLogin(loginname, password, agencyIdS, systemId);
			            if (list ==null ||list.size() == 0) {
			                log.info(loginname + "用户不存在");
			                // 添加操作详情 20141203
			                String operateDetail = "用户不存在";
			                log.info("operateDetail");
			                // 添加进系统日志表 20141203
			                systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGONFAIL, operateDetail);
			                ajaxinfo = AjaxReturnInfo.failed("请检验用户名，密码是否正确");
			            } else {
			                log.info(loginname + ":登录成功");
			                log.info(list.get(0).getUsername() + ":登录成功");
			                
			                SystemLogBean systemLogBeanVO = systemLogService.getloginPrompt(list.get(0).getUsername());
			                
			                if(systemLogBeanVO!=null){
			                	 request.getSession().setAttribute("systemLogIpAddress", systemLogBeanVO.getIpAddress());
					             request.getSession().setAttribute("perateTime", systemLogBeanVO.getOperateTime());
			                }
			               
			                
			                // 添加进系统日志表 20141203
			                systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGON, null);
			                // 会话属性中增加企业简称 20141102
			                // 会话属性中增加ip地址 20141203
			                SetSessionData(systemId, request, list, companyBriefName, ipAddress, agencyBeans.get(0).getVestagencyId(), loginname);
			                /* 添加用户登录密码策略代码 */
			                // 获取用户登录密码正则表达式
			                String pwdRegularity = authorityService.getParamObject("PWDREGULARITY");
			                request.getSession().setAttribute("pwdRegularity", pwdRegularity);

			                PwdStrategy pwdStrategy = new PwdStrategy();
			                pwdStrategy.setLoginName(loginname);

			                String path = request.getContextPath();
			                String subStr = path.substring(1);
			                pwdStrategy.setUseSystem(subStr);
			                pwdStrategy.setStatus("1"); // 设置默认状态为1有效
			                // /关联查询策略表和用户密码表
			                // / 如果策略表中无此系统的此用户 则默认插入策略表一条此用户的过期时间记录
			                int pwdUserCount = usersService.selectPwdStrategyAndUser(pwdStrategy);
			                if (pwdUserCount < 1) {
			                    // 获取并设置当前登录用户密码
			                    Integer idnub = usersService.getTablePwdStrategyId();
			                    pwdStrategy.setId(idnub);
			                    pwdStrategy.setPassWord(password);
			                    pwdStrategy.setOldPassWord(password);
			                    pwdStrategy.setCreateDate(CommonDate.getNowDate());
			                    pwdStrategy.setCreateTime("000000");
			                    usersService.insertPwdStrategy(pwdStrategy);
			                }

			                List<PwdStrategy> pwdStrategysModelList = usersService.selectPwdStrategy(pwdStrategy);
			                String createDateTime = pwdStrategysModelList.get(0).getCreateDate();
			                // i==1超过有效期
			                int i = CommonDate.compareDate(getDate(), createDateTime);
			                // /查询密码策略表
			                int count = usersService.selectPwdStrategyCount(pwdStrategy);
			                int num = agencyService.queryAgencyOnlinechannelCount(loginname);

			                if (password.equals(MD5.MD5(loginname)) && num > 0) {
			                    request.getSession().setAttribute("initpwd", "1");
			                } else if (count == 0 || (count > 0 && i == 1)) {
			                    log.info("策略表中此用户的个数count:" + count + ",( i==1超过有效期)比较日期 i:" + i + "弹框");
			                    request.getSession().setAttribute("initpwd", "2");
			                }

			                ajaxinfo = AjaxReturnInfo.success("登录成功");
			            }
			        } else {
			            // 添加操作详情 20141203
			            String operateDetail = "机构不存在";
			            log.info(loginname + "机构不存在");
			            // 添加进系统日志表 20141203
			            systemLogService.addLog(ipAddress, agencyIdS, userIdS, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGONFAIL, operateDetail);
			            ajaxinfo = AjaxReturnInfo.failed("请检验用户名，密码是否正确");
			        }

			    } else {
			        log.info(loginname + "未找到用户，登录名为");
			        // 添加操作详情 20141203
			        String operateDetail = "未找到用户，登录名为" + loginname;
			        // 添加进系统日志表 20141203
			        systemLogService.addLog(ipAddress, null, null, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGONFAIL, operateDetail);
			        ajaxinfo = AjaxReturnInfo.failed("请检验用户名，密码是否正确");
			    }

			} else {
			    log.info(loginname + "本地不能同时登录多个用户!");
			    ajaxinfo = AjaxReturnInfo.failed("本地不能同时登录多个用户!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			ajaxinfo = AjaxReturnInfo.failed("登录异常，请联系相关技术人员!");
		}
        return ajaxinfo;
    }
    
    /**
     * 【方法名】    : (IP Address ). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月11日 上午11:14:47 .<br/>
     * 【参数】： .<br/>
     * @param request
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    private String getRemoteHost(javax.servlet.http.HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

    //
    /**
     * 方法名： getDate.<br/>
     * 方法作用:获取减去参数后的日期作用在于判断其当前日期是否在有效期内.<br/>
     * 创建者：zhangjun.<br/>
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午2:31:47.<br/>
     * 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：String.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    public String getDate() {
        String pwdRegularity = authorityService.getParamObject("VALIDTIME");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 格式化对象
        Calendar calendar = Calendar.getInstance(); // 日历对象
        int month = calendar.get(Calendar.MONTH); // 获得当前月份
        calendar.set(Calendar.MONTH, month - Integer.parseInt(pwdRegularity)); // 设置上一个月的月份
        Date strDateTo = calendar.getTime();
        return sdf.format(strDateTo);
    }

    /***
     * 方法名： SetSessionData.<br/>
     * 方法作用:登录初始化设置session参量.<br/>
     * 创建者：zhangjun.<br/>
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午2:33:13.<br/>
     * 参数者异常：@param systemId 参数者异常：@param request 参数者异常：@param list 参数者异常：@param companyBriefName 参数者异常：@param ipAddress .<br/>
     * 返回值： @return 返回结果：void.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    private void SetSessionData(String systemId, HttpServletRequest request, List<UserBean> list, String companyBriefName, String ipAddress,String vestagencyid,String loginName) {
        log.info("设置SESSION参数");
        HttpSession session = request.getSession();
        session.setAttribute(ConstantUtils.USERID, list.get(0).getUserid());
        session.setAttribute(ConstantUtils.ROLETYPEID, list.get(0).getRoletypeId());
        session.setAttribute(ConstantUtils.USERNAME, list.get(0).getUsername());
        session.setAttribute(ConstantUtils.AGENCYID, list.get(0).getAgencyId());
        session.setAttribute(ConstantUtils.PARENTAGENCYID, list.get(0).getParentagencyId());
        session.setAttribute(ConstantUtils.SYSTEMID, systemId);
        // 会话属性中增加企业简称 20141102
        session.setAttribute(ConstantUtils.COMPANYBRIEFNAME, companyBriefName);
        session.setAttribute(ConstantUtils.USERPHONE, list.get(0).getPhone());
        session.setAttribute(ConstantUtils.LOGINSHOWFLAG, "1");
        // 会话属性中增加ip地址 20141203
        session.setAttribute(ConstantUtils.IPADDRESS, ipAddress);
        
        //归属机构为小微商
        if((!StringUtils.isEmpty(vestagencyid)) && "01000000".equals(vestagencyid)){
            session.setAttribute(ConstantUtils.TINYBUSSINESS,true);
        }else{
            session.setAttribute(ConstantUtils.TINYBUSSINESS,false);
        }
        if(ConstantUtils.CENTERCODE.equals(list.get(0).getUserid())){
            session.setAttribute(ConstantUtils.TINYBUSSINESS,true);
        }
        
        //登录名
        session.setAttribute(ConstantUtils.USERLOGINNAME, loginName);
        
        log.info("查询是否显示归属机构 ");
        SpecSystemBean spec = systemManageService.checkOnlineSystem(systemId);
        String onlineControl = "0";
        if (null == spec) {
            log.info(" 显示归属机构 ");
            onlineControl = "1";
        } else {
            if (null == spec.getOnlineControl()) {
                log.info(" 显示归属机构 ");
                onlineControl = "1";
            } else {
                log.info(" 归属机构： " + spec.getOnlineControl());
                onlineControl = spec.getOnlineControl();
            }
        }
        session.setAttribute(ConstantUtils.ONLINEFLAG, onlineControl);
        String agencySpec = "0";
        //
        Integer agencyCount = systemManageService.checkSpecAgency(systemId, list.get(0).getAgencyId());
        if (agencyCount > 0) {
            agencySpec = "1";
        }
        log.info(list.get(0).getUsername() + ":");
        session.setAttribute(ConstantUtils.AGENCYFLAG, agencySpec);
        spec = null;
        // 区分是否显示机构查询选项
        spec = systemManageService.checkSpecSystem(systemId);
        Boolean agencyQueryFlag = true;
        if (null != spec) {
            if ("1".equals(spec.getLevelControl()) && !ConstantUtils.CENTERCODE.equals(list.get(0).getParentagencyId())) {
                agencyQueryFlag = false;
            } else if ("2".equals(spec.getLevelControl())) {
                agencyQueryFlag = false;
            }
            if (agencyCount > 0) {
                agencyQueryFlag = true;
            }
        }
        log.info(list.get(0).getUsername() + "：显示机构查询选项:" + agencyQueryFlag);
        session.setAttribute(ConstantUtils.AGENCYQUERYFLAG, agencyQueryFlag);
    }

    /**
     * 修改用户密码
     * 
     * @param newPwd
     *            新密码
     * @param oldPwd
     *            旧密码
     * @param request
     *            请求
     * @return 返回值
     */
    @RequestMapping(params = "method=updateUserPwd")
    @ResponseBody
    public AjaxReturnInfo updateUserPwd(@RequestParam(value = "newPwd") String newPwd, @RequestParam(value = "oldPwd") String oldPwd, HttpServletRequest request) {

        AjaxReturnInfo ajaxinfo = null;
        String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        log.info(agencyId + ":修改密码");
        String pwdStr = usersService.getUserLoginPwd(userId);
        List<UserBean> userModelList = usersService.selectUserLoginByUserId(userId);

        // 获得ip
        String ipAddress = request.getRemoteAddr();

        if (oldPwd.trim().equals(pwdStr.trim())) {

            // //获取登录的用户名称 重置密码的公共方法
            PwdStrategy pwdStrategy = new PwdStrategy();
            pwdStrategy.setLoginName(userModelList.get(0).getLoginname());

            // /设置要更新的密码策略表中的新旧密码
            pwdStrategy.setPassWord(newPwd);
            pwdStrategy.setOldPassWord(oldPwd);

            // 获取系统名称
            String path = request.getContextPath();
            String subStr = path.substring(1);
            pwdStrategy.setUseSystem(subStr);

            // 获取当前日期时间
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
            String str = format.format(date);
            String createDate = str.substring(0, str.indexOf(" "));
            String createTime = str.substring(str.indexOf(" ") + 1);
            pwdStrategy.setCreateDate(createDate);
            pwdStrategy.setCreateTime(createTime);
            // /更新密码策略表密码
            int pwdStrategyNub = usersService.updatePwdStrategy(pwdStrategy);
            int index = usersService.updateUserLoginPwd(newPwd, userId);

            if ((index * pwdStrategyNub) == 0) {
                log.info(agencyId + ":修改失败");
                ajaxinfo = AjaxReturnInfo.failed("修改失败");
            } else {
                log.info(agencyId + ":修改成功");
                // 添加操作详情 20141203
                String operateDetail = "修改密码为" + newPwd;
                // 添加进系统日志表 20141203
                systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPEUPD, operateDetail);
                request.getSession().setAttribute("initpwd", "0");
                ajaxinfo = AjaxReturnInfo.success("修改成功");
            }
        } else {
            log.info(agencyId + ":原始密码不正确！");
            ajaxinfo = AjaxReturnInfo.failed("原始密码不正确！");
        }
        return ajaxinfo;

    }

    /**
     * 用户退出，销毁session
     * 
     * @param req
     *            请求
     * @param resp
     *            返回
     * @return 返回值
     */
    @RequestMapping(params = "method=destroySession")
    @ResponseBody
    public Map<String, Object> destroySession(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("des", "销毁");
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        log.info(agencyId + ":用户退出");
        // 获得ip
        String ipAddress = req.getRemoteAddr();
        // 添加进系统日志表 20141203
        systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMELOGON, ConstantUtils.OPERTYPELOGOFF, null);
        session.invalidate();
        return map;
    }

    /**
     * 获取用户所对应的菜单权限
     * 
     * @param request
     *            请求
     * @return 返回
     */
    @RequestMapping(params = "method=getMenuTree")
    @ResponseBody
    public List<Map<String, Object>> getMenuTree(HttpServletRequest request) {
        String userid = request.getSession().getAttribute(ConstantUtils.USERID).toString();
        List<MenuBean> menulist = usersService.getMenunew(userid, ConstantUtils.MENUTREEBASE);
        List<String> urlList = new ArrayList<String>();
        log.info(userid + ":获取用户所对应的菜单权限");
        for (MenuBean bean : menulist) {
            urlList.add(bean.getMenuUrl());
        }
        request.getSession().setAttribute(ConstantUtils.MENULIST, urlList);
        return createMenuTree(menulist, ConstantUtils.MENUTREEBASE);
    }

    /**
     * 生成用户所对应的菜单树
     * 
     * @param list
     * @param id
     * @return 返回参数
     */
    @RequestMapping(params = "method=getMenusetTree")
    @ResponseBody
    public List<Map<String, Object>> createMenuTree(List<MenuBean> list, String id) {
        List<Map<String, Object>> aulist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = null;
            MenuBean at = list.get(i);
            if (id.equals(at.getMenuFather())) {
                map = new HashMap<String, Object>();
                map.put("id", at.getMenuId());
                map.put("text", at.getMenuName());
                if ("0".equals(at.getChildCode())) {
                    map.put("state", "open");
                } else {
                    map.put("state", "open");
                    map.put("children", createMenuTree(list, at.getMenuId()));
                    List<Map<String, Object>> strlist = new ArrayList<Map<String, Object>>();
                    Map<String, Object> z = new HashMap<String, Object>();
                    z.put("url", at.getMenuUrl());
                    strlist.add(z);
                    map.put("attributes", z);
                }

            }
            if (map != null)
                aulist.add(map);
        }
        return aulist;
    }
    

}
