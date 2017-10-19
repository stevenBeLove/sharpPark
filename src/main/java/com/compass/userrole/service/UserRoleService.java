package com.compass.userrole.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.userrole.model.UserAddInfo;
import com.compass.userrole.model.UserRoleBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author wangyuchao
 *
 */
public class UserRoleService extends AbstractService{
	
	private final Log log = LogFactory.getLog(getClass());
	/**
	 * 生成用户编号
	 * @return
	 */
	public Integer getUserId(){
		log.info("生成用户编号");
		return (Integer) dao.queryForObject("SEQUENCE.getUserId");
	}
	/**
	 *  用户查询统计
	 * @param userName  用户名
	 * @param status    用户状态
	 * @param agencyid  机构编码
	 * @param selAgencyId 选择机构编码 
	 * @param parentFlag 查询直属标记
	 * @param datestart	开始时间 
	 * @param dateend   结束时间 
	 * @param parentagencyId   上级机构编码 
	 * @return
	 */
	public Integer getUserRoleCount(String userName,String status,String agencyId,
			String selAgencyId,String parentFlag,String datestart,String dateend,
			String parentagencyId,String agencyid ){
		log.info("用户查询统计");
		Map<String,String> map= new HashMap<String,String>();
		map.put("userName", userName);
		map.put("status", status);
		map.put("agencyId", agencyId);
		map.put("selAgencyId", selAgencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("parentagencyid",parentFlag);
		map.put("parentagencyId", parentagencyId);
		map.put("agencyid", agencyid);
		return (Integer) dao.queryForObject("USERROLE.getUserRoleCount",map);
	}
	
	/**
	  *  用户查询
	 * @param userName  用户名
	 * @param status    用户状态
	 * @param agencyid  机构编码
	 * @param selAgencyId 选择机构编码 
	 * @param parentFlag 查询直属标记
	 * @param datestart	开始时间 
	 * @param dateend   结束时间 
	 * @param parentagencyId   上级机构编码 
	 * @param startNum  
	 * @param endNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserRoleBean> getUserRoleList(String userName,String status,String agencyId,
			String selAgencyId,String parentFlag,String datestart,String dateend,
			String parentagencyId,Integer startNum,Integer endNum,String agencyid){
		log.info("用户查询");
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("userName", userName);
		map.put("status", status);
		map.put("agencyId", agencyId);
		map.put("selAgencyId", selAgencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("parentagencyid",parentFlag);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		map.put("parentagencyId", parentagencyId);
		map.put("agencyid", agencyid);
		return (List<UserRoleBean> )dao.queryForList("USERROLE.getUserRoleList",map);
	}
 
	/**
	 * 用户查询
	 * @param user
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public List<UserRoleBean> getUserRole(UserRoleBean user){
		log.info("用户查询:"+user.getUserId());
		@SuppressWarnings("unchecked")
		List<UserRoleBean> list = dao.queryForList("USERROLE.inquire",user);
		return list;
	}
	/**
	 * 查询用户所拥有的权限
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserRoleBean> getRoleC(String userId){
		log.info(" 查询用户所拥有的权限,userID:"+userId);
		return dao.queryForList("USERROLE.getRolec", userId);
	}
	/**
	 * 添加用户
	 * @param userRoleBean
	 * @return
	 */
	public int getInsert(UserRoleBean userRoleBean){
		log.info("添加用户，userID:"+userRoleBean.getUserId()+",用户名："+userRoleBean.getUserName());
		int useradd = dao.insert("USERROLE.add", userRoleBean);
		return useradd;
	}
	
    /**
     * 【注意】: (添加附加关系表).<br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月5日 下午4:09:47 .<br/>
     * 【参数】： .<br/>
     * 
     * @param userAddInfo
     *            UserAddInfo
     * @return int.<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public int insertUserAddInfo(UserAddInfo userAddInfo) {
        int useradd = dao.insert("USERROLE.insertUserAddInfo", userAddInfo);
        return useradd;
    }
	/**
	 *修改用户
	 * @param userRoleBean
	 * @return
	 */
	public int getUpdate(UserRoleBean userRoleBean){
		log.info("修改用户，userID:"+userRoleBean.getLoginName()+",用户名："+userRoleBean.getUserName());
		int userupdate = dao.update("USERROLE.update", userRoleBean);
		return userupdate;
	}
	/**
	 * 删除用户
	 * @param userRoleBean
	 * @return
	 */
	public int getDelete(UserRoleBean userRoleBean){
		log.info("删除用户，userID:"+userRoleBean.getLoginName()+",用户名："+userRoleBean.getUserName());
		int userdelete = dao.delete("USERROLE.delete", userRoleBean);
		return userdelete;
	}
	/**
	 * 验证指定机构下是否有用户存在
	 * @param agencyId
	 * @return
	 */
	public int getUsersByagencyId(String agencyId){
		log.info("验证指定机构下是否有用户存在,机构号："+agencyId);
		return (Integer) dao.queryForObject("USERROLE.getUsersByagencyId", agencyId);
	}
	

	/**
	 * 为用户添加角色
	 * @param userId
	 * @param roleId
	 * @param createId
	 * @param createDt
	 * @return
	 */
	public int addUserRole(String userId, String roleId,String createId,String createDt) {
		// TODO Auto-generated method stub
		log.info("为用户添加角色,userId:"+userId);
		Map<String,String> map= new HashMap<String,String>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		map.put("createId", createId);
		map.put("createDt", createDt);
		
		return dao.insert("USERROLE.addUserRole",map);
	}
	/**
	 * 通过用户编号来删除用户的角色
	 * @param userId
	 * @return
	 */
	public int delRoleByUserId(String userId) {
		log.info("通过用户编号来删除用户的角色,userId:"+userId);
		// TODO Auto-generated method stub
		return dao.delete("USERROLE.delRoleByUserId", userId);
	}
	/**
	 * 获取用户所对应的数据
	 * @param userId
	 * @return
	 */
	public Integer getUserRelation(String userId){
		log.info("获取用户所对应的数据,userId:"+userId);
		return (Integer)dao.queryForObject("role.getUserRelation", userId);
	}
	/**
	 * 验证当前部门下的登录名称是否存在
	 * 修改为所有用户登录名称唯一
	 * @param loginname
	 * @param agencyId
	 * @return
	 */
	public Integer checkUserAdd(String loginname,String agencyId){
		log.info("验证当前部门下的登录名称是否存在,登录名:"+loginname+",机构编号"+agencyId);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("loginname", loginname);
		map.put("agencyId", agencyId);
		return (Integer) dao.queryForObject("USERROLE.checkUserAdd",map);
	}
	/**
	 * 检查同一机构下的用户是否重复
	 * 修改为所有用户登录名称唯一
	 * @param userId
	 * @param loginname
	 * @param agencyId
	 * @return
	 */
	public Integer checkUserEdit(String userId,String loginname,String agencyId){
		log.info("检查同一机构下的用户是否重复,登录名:"+loginname+",机构编号:"+agencyId+",userID"+userId);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("loginname", loginname);
		map.put("agencyId", agencyId);
		return (Integer) dao.queryForObject("USERROLE.checkUserEdit",map);
	}
	
	/**
	 * 根据机构编号删除用户
	 * @param agencyId
	 * @return
	 */
	public int deleteUserByAgencyId (String agencyId){
		log.info("根据机构编号删除用户,机构编号："+agencyId);
		return dao.update("USERROLE.deleteUserByAgencyId", agencyId);
	}
	
	
	public Integer getUserAndRoleCount(String userName,String agencyId,String roleid){
		log.info("用户角色查询统计");
		Map<String,String> map= new HashMap<String,String>();
		map.put("loginname", userName);
		map.put("agencyid", agencyId);
		map.put("roleid", roleid);
		return (Integer) dao.queryForObject("USERROLE.getUserAndRoleCount",map);
	}
	
	public List<UserRoleBean>  getUserAndRoleList(String userName,String agencyId,String roleid,int startNum,int endNum){
		log.info("用户角色查询统计");
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("loginname", userName);
		map.put("agencyid", agencyId);
		map.put("roleid", roleid);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return   (List<UserRoleBean>) dao.queryForList("USERROLE.getUserAndRoleList",map);
		 
	}
	
	public Integer updateUserRole(String userId,String roleId){
		Map<String,String> map=new HashMap<String, String>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		return dao.update("USERROLE.updateUserRole", map);
		
	}
	
	/**
	 * 【方法名】    : (更新推客标识). <br/> 
	 * 【作者】: yinghui zhang .<br/>
	 * 【时间】： 2017年1月22日 下午6:13:10 .<br/>
	 * 【参数】： .<br/>
	 * @param isDtbUser
	 * @param userloginname
	 * @return .<br/>
	 * <p>
	 * 修改记录.<br/>
	 * 修改人:  yinghui zhang 修改描述： .<br/>
	 * <p/>
	 */
	public Integer updateUserisDtbUser(String isDtbUser, String userloginname){
    Map<String,String> map=new HashMap<String, String>();
    map.put("isDtbUser", isDtbUser);
    map.put("userloginname", userloginname);
    return dao.update("USERROLE.updateUserisDtbUser", map);
	}
	
	
	 /**
   * 【方法名】    : (更新推客分润标识). <br/> 
   * 【作者】: yinghui zhang .<br/>
   * 【时间】： 2017年1月22日 下午6:13:10 .<br/>
   * 【参数】： .<br/>
   * @param isDtbUser
   * @param userloginname
   * @return .<br/>
   * <p>
   * 修改记录.<br/>
   * 修改人:  yinghui zhang 修改描述： .<br/>
   * <p/>
   */
  public Integer updateUserDtbProfitFlag(String dtbProfitFlag, String userloginname){
    Map<String,String> map=new HashMap<String, String>();
    map.put("dtbProfitFlag", dtbProfitFlag);
    map.put("userloginname", userloginname);
    return dao.update("USERROLE.updateUserDtbProfitFlag", map);
  }
  /**
   * 【方法名】    : (根据用户登录名更新). <br/> 
   * 【作者】: yinghui zhang .<br/>
   * 【时间】： 2017年1月23日 下午5:43:06 .<br/>
   * 【参数】： .<br/>
   * @param map
   * @return .<br/>
   * <p>
   * 修改记录.<br/>
   * 修改人:  yinghui zhang 修改描述： .<br/>
   * <p/>
   */
  public Integer updateUserByMap(Map<String, Object> map) {
      return dao.update("USERROLE.updateUserByMap", map);
  }
  
  /**
   * 
   * 【方法名】    : (用户附加信息表查询). <br/> 
   * 【作者】: 简思文 .<br/>
   * 【时间】： 2017年4月19日 上午10:25:32 .<br/>
   * 【参数】： .<br/>
   * @param userId
   * @return .<br/>
   * <p>
   * 修改记录.<br/>
   * 修改人:  简思文  修改描述： .<br/>
   * <p/>
   */
    public int queryUserAddInfo(String userId) {
        return (Integer) dao.queryForObject("USERROLE.queryUserAddInfo", userId);
    }
	
  /**
   * 
   * 【方法名】    : (修改用户附加信息). <br/> 
   * 【作者】: 简思文 .<br/>
   * 【时间】： 2017年4月19日 上午10:25:47 .<br/>
   * 【参数】： .<br/>
   * @param userId
   * @return .<br/>
   * <p>
   * 修改记录.<br/>
   * 修改人:  简思文  修改描述： .<br/>
   * <p/>
   */
    public int updateUserAddInfo(String userId) {
        return (Integer) dao.update("USERROLE.updateUserAddInfo", userId);
    }
}
