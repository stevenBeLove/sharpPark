package com.compass.userrole.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.SpecSystemBean;
import com.compass.agency.service.AgencyService;
import com.compass.role.service.RoleService;
import com.compass.system.service.SystemManageService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.userrole.model.UserRoleBean;
import com.compass.userrole.service.UserRoleService;
import com.compass.users.model.PwdStrategy;
import com.compass.users.model.UserBean;
import com.compass.users.service.UsersService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.FormatChar;
import com.compass.utils.mvc.AjaxReturnInfo;
/**
 * 
 * @author Ferry Chen
 *
 */
@Controller
@RequestMapping("/userrole/userrole.do")
public class UserRoleController {
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier("userRoleService")
    private UserRoleService userRoleService;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;
	@Autowired
	@Qualifier("usersService")
	private UsersService usersService;
	@Autowired
	@Qualifier("agencyService")
	private AgencyService agencyService;
	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;
	//系统日志service
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService; 
		
	/**
	 * 用户查询	
	 * @param userName
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=inquire")
	@ResponseBody
	public Map<String,Object> getUsers(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "agencyid") String agencyid,
			@RequestParam(value="selAgencyId") String selAgencyId,
			@RequestParam(value="datestart") String datestart,
			@RequestParam(value="dateend")  String dateend,
			HttpServletRequest request){
		log.info("用户查询");
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? ""
				: (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
		//上级机构 /
		String  parentagencyId=request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString(); //上级机构编号
		//来源系统
		String systemid=(String) request.getSession().getAttribute(ConstantUtils.SYSTEMID) ;
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		
		if("-1".equals(selAgencyId)){
			selAgencyId=null;
		}
		
		String parentFlag=""; //若不为空则为直属上级
		if(agencyId.equals(ConstantUtils.CENTERCODE)){
			parentagencyId=agencyId;
		}else{
			//
			//if(!parentagencyId.equals(ConstantUtils.CENTERCODE) && !systemid.equals("HTXT") ){
			
			//查看是否为特别系统（仅显示本机构及直属机构）
			SpecSystemBean spec=systemManageService.checkSpecSystem(systemid);
			//查看来源系统下的机构是否为特别系统
			Integer agencyCount=systemManageService.checkSpecAgency(systemid, agencyId);
			
			if(null!= spec){
				parentFlag="1";
				parentagencyId="";
				if("0".equals(spec.getLevelControl())){
					parentagencyId=agencyId;
					parentFlag="";
				}else if("1".equals(spec.getLevelControl())&&parentagencyId.equals(ConstantUtils.CENTERCODE)){
					parentagencyId=agencyId;
					parentFlag="";
				} else if(agencyCount>0&&parentagencyId.equals(ConstantUtils.CENTERCODE)){
					parentagencyId=agencyId;
					parentFlag="";
				} 
			
			}else {
				parentagencyId=agencyId;
				parentFlag="";
			}
		}	
		
		Integer count=userRoleService.getUserRoleCount(userName, status,agencyId, selAgencyId,
				parentFlag, datestart, dateend,parentagencyId,agencyid);
		
/*		
		UserRoleBean user=new UserRoleBean();
		user.setUserName(userName);
		user.setStatus(status);
		user.setAgencyId(agencyId);
		user.setParentagencyId(parentagencyId);
		user.setAgencyid(agencyid);
		user.setParentagencyid(parentFlag);
		List<UserRoleBean> list = userRoleService.getUserRole(user);
		*/
		String page =request.getParameter("page");
		String rows = request.getParameter("rows");
		// 当前页
		int intPage = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		// 每页显示条数
		int number = Integer.parseInt((rows == null || rows == "0") ? "50"
				: rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int start = (intPage - 1) * number;
		int end = (start + number) > count ? count : start + number;
		List<UserRoleBean> beanList =userRoleService.getUserRoleList(userName, status, agencyId, selAgencyId, 
				parentFlag, datestart, dateend, parentagencyId, start, end,agencyid);
		if(beanList!=null){
		    for (UserRoleBean userRoleBean : beanList) {
		        if(!userRoleBean.getUserId().equals(userId)){
		            if(!StringUtils.isEmpty(userRoleBean.getEmail())){
		                userRoleBean.setEmail(FormatChar.mosaic(userRoleBean.getEmail()));
		            }
		            if(!StringUtils.isEmpty(userRoleBean.getPhone())){
		                userRoleBean.setPhone(FormatChar.mosaic(userRoleBean.getPhone()));
		            }
		        }
		       
        }
		}
		
		//添加操作详情 20141203
		String operateDetail = "查询条件为" + userName + status + agencyid;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEUSERROLE,ConstantUtils.OPERTYPESER,
		operateDetail);

		return AjaxReturnInfo.setTable(beanList==null?0:count, beanList);

	}
	/**
	 * 用户添加
	 * @param userName
	 * @param loginName
	 * @param password
	 * @param email
	 * @param phone
	 * @param status
	 * @param roleId
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=add")
	@ResponseBody
	public AjaxReturnInfo useradd(@RequestParam(value = "userName") String userName,
									@RequestParam(value = "loginName") String loginName,
									@RequestParam(value = "password") String password,
									@RequestParam(value = "email") String email,
									String phone,
									@RequestParam(value = "status") String status,
									@RequestParam(value = "agencyId") String agencyId,
									HttpServletRequest request){
			log.info("用户添加:" + userName );
			String createId=request.getSession().getAttribute(ConstantUtils.USERID)+"";
			agencyId=request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
			String userId=userRoleService.getUserId().toString();
			String createDt=CommonDate.getDate();
			//获得ip
			String ipAddress = request.getRemoteAddr();
			String userIdS = request.getSession().getAttribute(ConstantUtils.USERID).toString();
			String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
			String systemid=request.getSession().getAttribute(ConstantUtils.SYSTEMID).toString();
			
			UserRoleBean userRoleBean = new UserRoleBean();
			userRoleBean.setUserId(userId);
			userRoleBean.setUserName(userName);
			userRoleBean.setLoginName(ConstantUtils.RTBPREFIX+loginName);
			userRoleBean.setPassword(password);
			userRoleBean.setEmail(email);
			userRoleBean.setPhone(phone);
			userRoleBean.setStatus(status);
			userRoleBean.setAgencyId(agencyId);
			userRoleBean.setCreateId(createId);
			userRoleBean.setCreateDt(createDt);
			List<Map<String,Object>> maplist=roleService.getRoleId(ConstantUtils.ROLETYPEOPERATOR);
			AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");
			//检查用户的登录名称是否存在 
			int checkuser=userRoleService.checkUserAdd(loginName, agencyId);
			if(checkuser==0){
				String roleId=maplist.get(0).get("ROLEID").toString();
				//默认添加的用户为操作作员 ,，通联民富作特殊处理 
				roleId=	"TLMF".equals(systemid)?ConstantUtils.ROLETYPEOPERATOR_TLMF: roleId;
				int resule = userRoleService.getInsert(userRoleBean);
				int resule1= userRoleService.addUserRole(userId,roleId,createId,createDt);
				if(resule*resule1==0){
					log.info("保存失败");
					ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
				}
				log.info("保存完成");
			}else{
				log.info("登录名已经被占用!");
				ajaxReturnInfo = AjaxReturnInfo.failed("登录名已经被占用!");
			}
			
			//添加操作详情 20141203
			String operateDetail = "添加用户名为" + userName +",ID为"+ userId ;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyIdS,userIdS,ConstantUtils.OPERNAMEUSERROLE,ConstantUtils.OPERTYPEADD,
			operateDetail);
			return ajaxReturnInfo;
	}
	/**
	 * 修改用户
	 * @param userId
	 * @param userName
	 * @param loginName
	 * @param password
	 * @param email
	 * @param phone
	 * @param status
	 * @param roleId
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=update")
	@ResponseBody
	public AjaxReturnInfo userupdate(
									@RequestParam(value = "userId") String userId,
									@RequestParam(value = "userName") String userName,
									@RequestParam(value = "loginName") String loginName,
									@RequestParam(value = "password") String password,
									@RequestParam(value = "email") String email,
									@RequestParam(value = "phone") String phone,
									@RequestParam(value = "status") String status,
									@RequestParam(value = "agencyId") String agencyId,
									HttpServletRequest request){
					log.info("修改用户,用户名："+loginName+"，机构"+agencyId+",userId:"+userId);
					//获得ip
					String ipAddress = request.getRemoteAddr();
					
					UserRoleBean userRoleBean = new UserRoleBean();
					String modifyId=request.getSession().getAttribute(ConstantUtils.USERID)+"";
					String modifyDt=CommonDate.getDate();
					userRoleBean.setUserId(userId);
					userRoleBean.setUserName(userName);
					userRoleBean.setLoginName(loginName);
					userRoleBean.setPassword(password);
					userRoleBean.setEmail(email);
					userRoleBean.setPhone(phone);
					userRoleBean.setModifyId(modifyId);
					userRoleBean.setModifyDt(modifyDt);
					userRoleBean.setStatus(status);
					int checkuser=userRoleService.checkUserEdit(userId,loginName, agencyId);
					AjaxReturnInfo ajaxReturnInfo=null;
					if(checkuser==0){
			            int resule = userRoleService.getUpdate(userRoleBean);
			            //添加操作详情 20141203
						String operateDetail = "修改的用户名为" + userName + ",ID为" + userId ;
						// 添加进系统日志表 20141203
						systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEUSERROLE,ConstantUtils.OPERTYPEUPD,
						operateDetail);
			            ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");		
			            
			            //同步代理商表中的用户信息
			            AgencyBean agency = agencyService.getAgency(loginName);
			            if(!StringUtils.isEmpty(agency)){
			            	agency.setCompanyPhone(phone);
				            agencyService.updateMyAgency(agency);
			            }
			            			            
						if(resule==0){
						ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
						}
					}else{
						ajaxReturnInfo = AjaxReturnInfo.failed("登录名已经被占用!");
					}
					
			
			
			return ajaxReturnInfo;
	}
	/**
	 * 验证指定机构下是否存在用户
	 * @param agencyId
	 * @return
	 */
	@RequestMapping(params ="method=getUsersByagencyId")
	@ResponseBody
	public AjaxReturnInfo getUsersByagencyId(@RequestParam(value = "agencyId") String agencyId){
		return null;
		
	}
	/**
	 * 删除用户，支持批量删除
	 * @param userIds
	 * @return
	 */
	@RequestMapping(params ="method=delete")
	@ResponseBody
	public AjaxReturnInfo userdelete(@RequestParam(value = "userIds") String userIds,
			HttpServletRequest request){
		log.info("删除用户，支持批量删除,userId为"+userIds);
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
		AjaxReturnInfo ajaxReturnInfo = AjaxReturnInfo.failed("删除失败");		
		int resule = 0; 
		userIds=userIds.substring(0, userIds.length()-1);
		String []tt=userIds.split(",");
		for(int i=0;i<tt.length;i++){
			UserRoleBean userRoleBean = new UserRoleBean();
			userRoleBean.setUserId(tt[i]);		
			//int status = 0;
			userRoleBean.setStatus("0");
			resule+= userRoleService.getDelete(userRoleBean);
		}
		
		if(resule==tt.length){
			 //添加操作详情 20141203
			String operateDetail = "删除的用户的ID为" + userIds;
			// 添加进系统日志表 20141203
			systemLogService.addLog(ipAddress,agencyId,userId,ConstantUtils.OPERNAMEUSERROLE,ConstantUtils.OPERTYPEDEL,
			operateDetail);
			log.info("删除成功,userId为"+userIds);
			ajaxReturnInfo = AjaxReturnInfo.success("删除成功!");
		}else{
			log.info("删除失败,userId为"+userIds);
		}
	
		return ajaxReturnInfo;
	
		
	}
	/**
	 * 查看用户所拥有的权限
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=getRoleC")
	@ResponseBody
	public List<Map<String,Object>> getRoleC(@RequestParam(value = "userId") String userId,
			HttpServletRequest request){
		log.info("查看用户所拥有的权限");
		List<UserRoleBean> list=userRoleService.getRoleC(userId);
		List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();
		for(UserRoleBean obj:list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("userId", obj.getUserId());
			map.put("roleId", obj.getRoleId());
			listmap.add(map);
		}
		return listmap;
	}

    /**
     * 重置用户密码(超级管理员和管理员拥有该权限)
     * 
     * @param userId  用户编号
     * @param userPwd  登录密码
     * @param request  请求
     * @return  返回
     */
    @RequestMapping(params = "method=resetPwd")
    @ResponseBody
    public AjaxReturnInfo resetPwd(@RequestParam(value = "userId") String userId, @RequestParam(value = "userPwd") String userPwd, HttpServletRequest request) {
        log.info(" 重置用户密码,UserID" + userId);
        AjaxReturnInfo ajaxReturnInfo = null;

        // 获得ip
        String ipAddress = request.getRemoteAddr();
        String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        int result = usersService.updateUserLoginPwd(userPwd, userId);

        List<UserBean> userModelList = usersService.selectUserLoginByUserId(userId);
        PwdStrategy pwdStrategy = new PwdStrategy();
        pwdStrategy.setLoginName(userModelList.get(0).getLoginname());
        // /设置要更新的密码策略表中的新旧密码
        pwdStrategy.setPassWord(userPwd);
        pwdStrategy.setOldPassWord(userModelList.get(0).getPassword());

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

        // /若果密码策略表中存在此用户则更新密码策略表密码
        int pwdUserCount = usersService.selectPwdStrategyAndUser(pwdStrategy);
        if (pwdUserCount >= 1) {
            int pwdStrategyNub = usersService.updateResetPwdStrategy(pwdStrategy);
            if ((result * pwdStrategyNub) == 0) {
                log.info(" 密码重置失败,UserID" + userId);
                ajaxReturnInfo = AjaxReturnInfo.failed("密码重置失败！");
            } else {
                log.info(" 密码重置成功,UserID" + userId);
                // 添加操作详情 20141203
                String operateDetail = "用户" + userId;
                // 添加进系统日志表 20141203
                systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEUSERROLE, ConstantUtils.OPERTYPERESETPSW, operateDetail);

                ajaxReturnInfo = AjaxReturnInfo.success("密码已重置！");
            }

        } else {
            if (result == 0) {
                log.info(" 密码重置失败,UserID" + userId);
                ajaxReturnInfo = AjaxReturnInfo.failed("密码重置失败！");
            } else {
                log.info(" 密码重置成功,UserID" + userId);
                String operateDetail = "用户" + userId;
                systemLogService.addLog(ipAddress, agencyId, userId, ConstantUtils.OPERNAMEUSERROLE, ConstantUtils.OPERTYPERESETPSW, operateDetail);
                ajaxReturnInfo = AjaxReturnInfo.success("密码已重置！");
            }

        }

        return ajaxReturnInfo;

    }
	
	
	/**
	 * 用户角色查询	
	 * @param userName
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=getUserRole")
	@ResponseBody
	public Map<String,Object> getUserRole(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "roleid") String roleid,
			@RequestParam(value = "agencyid") String agencyid,
			HttpServletRequest request){
		log.info("用户查询");
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID) == null ? ""
				: (String) request.getSession().getAttribute(ConstantUtils.AGENCYID);
		//上级机构 /
		String  parentagencyId=request.getSession().getAttribute(ConstantUtils.PARENTAGENCYID).toString(); //上级机构编号
		//来源系统
		String systemid=(String) request.getSession().getAttribute(ConstantUtils.SYSTEMID) ;
		//获得ip
		String ipAddress = request.getRemoteAddr();
		String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
		
		if(!ConstantUtils.CENTERCODE.equals(agencyId)){
			return null;
		}
		if("-1".equals(agencyid)){
			agencyid="";
		}
		if("-1".equals(roleid)){
			roleid="";
		}
		
		Integer count=userRoleService.getUserAndRoleCount(userName, agencyid, roleid);
		 
		String page =request.getParameter("page");
		String rows = request.getParameter("rows");
		// 当前页
		int intPage = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		// 每页显示条数
		int number = Integer.parseInt((rows == null || rows == "0") ? "50"
				: rows);
		// 每页的开始记录 第一页为1 第二页为number +1
		int start = (intPage - 1) * number;
		int end = (start + number) > count ? count : start + number;
		List<UserRoleBean> beanList =userRoleService.getUserAndRoleList(userName, agencyid, roleid,start,end);
	 
		
		return AjaxReturnInfo.setTable(beanList==null?0:count, beanList);

	}
	
	
	/**
	 * 修改用户
	 * @param userId
	 * @param userName
	 * @param loginName
	 * @param password
	 * @param email
	 * @param phone
	 * @param status
	 * @param roleId
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=updateUserRole")
	@ResponseBody
	public AjaxReturnInfo userRole(
									@RequestParam(value = "userId") String userId,
									@RequestParam(value = "roleId") String roleId,
									HttpServletRequest request){
					log.info("修改用户,roleId："+roleId+",userId:"+userId);
					AjaxReturnInfo ajaxReturnInfo=null;
					ajaxReturnInfo = AjaxReturnInfo.failed("保存失败!");
					userRoleService.updateUserRole(userId, roleId);
					ajaxReturnInfo = AjaxReturnInfo.success("保存成功!");		
			return ajaxReturnInfo;
	}
	/**
	 * 清空用户手机号
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping(params ="method=clearPhone")
	@ResponseBody
	public AjaxReturnInfo clearPhone(
			@RequestParam(value = "userId") String userId,HttpServletRequest request){
		AjaxReturnInfo ajaxReturnInfo = null;
		UserRoleBean bean= new UserRoleBean();
		bean.setUserId(userId);
		try{
			usersService.clearPhone(bean);
			ajaxReturnInfo=AjaxReturnInfo.success("更新成功");
		}catch (Exception e) {
			ajaxReturnInfo=AjaxReturnInfo.failed("重置失败");
		}
		bean=null;
		return ajaxReturnInfo;
	}
	
}
