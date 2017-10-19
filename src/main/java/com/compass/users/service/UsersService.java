/**
 * 
 */
package com.compass.users.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.userrole.model.UserRoleBean;
import com.compass.users.model.MenuBean;
import com.compass.users.model.PwdStrategy;
import com.compass.users.model.UserBean;
import com.compass.utils.AbstractService;

/**
 * @author wangyuchao
 * 
 */
public class UsersService extends AbstractService {
	private static Logger log = LoggerFactory.getLogger(UsersService.class);

	
	/**
	 * 验证用户登录
	 * @param loginname
	 * @param password
	 * @param agencyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserBean> checkLogin(String loginname, String password,String agencyId,String systemId) {
		log.info(loginname+":验证用户登录");
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginname", loginname);
		map.put("password", password);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		return dao.queryForList("login.checkLogin", map);
	
		
	}
	
	/**
	 * 获取主菜单
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MenuBean> getMenunew(String userid,String menuTreeBase){
		log.info("获取主菜单");
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("menuTreeBase", menuTreeBase);
		return dao.queryForList("MENU.getmenusnew", map);
	}
	
	/**
	 * 获取用户原始密码
	 * @param userId
	 * @return
	 */
	public String getUserLoginPwd(String userId){
		log.info("用户名："+userId+":获取用户原始密码");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return (String) dao.queryForObject("MENU.getUserLoginPwd", map);
	}
	
	/**
	 * 修改用户密码
	 * @param pwd
	 * @param userId
	 * @return
	 */
	public int updateUserLoginPwd(String pwd,String userId){
		log.info("用户名："+userId+":修改用户密码");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pwd", pwd);
		map.put("userId", userId);
		return dao.update("MENU.updateUserLoginPwd", map);
	}




	public void clearPhone(UserRoleBean bean) {

		log.info("把手机号清空");
		Map<String,String> map = new HashMap<String, String>();
		map.put("userId", bean.getUserId());
		map.put("userPhone",null);
		dao.update("login.updatePhone", map);	
	}

	
	
	
	    /**
	     * 方法名： selectPwdStrategyAndUser.<br/>
	     * 方法作用:关联查询密码策略表及用户密码表查询该用户是否已在密码策略表中.<br/>
	     * 创建者：zhang jun<br/>
	     * 创建日期：2016年11月2日.<br/>
	     * 创建时间：下午5:50:41.<br/>
	     * 参数者异常：@param pwdStrategy
	     * 返回值： @return 返回结果：int.<br/>
	     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
	     */
    public int selectPwdStrategyAndUser(PwdStrategy pwdStrategy) {
        log.info("用户策略表中");
        Map<String, String> map= new HashMap<String, String>();
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("useSystem", pwdStrategy.getUseSystem());     
        return (Integer) dao.queryForObject("login.selectPwdStrategyAndUser", map);
        
    }
   
    /**
     * 
     * 方法名： insertPwdStrategy.<br/>
     * 方法作用:登录的用户尚未在密码策略表中那么初始化插入一条过期用户记录.<br/>
     * 创建者：zhang jun<br/>
     * 创建日期：2016年11月2日.<br/>
     * 创建时间：下午5:50:41.<br/>
     * 参数者异常：@param pwdStrategy
     * 返回值： @return 返回结果：int.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    public int insertPwdStrategy(PwdStrategy pwdStrategy) {
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", pwdStrategy.getId());
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("passWord", pwdStrategy.getPassWord());
        map.put("oldPassWord", pwdStrategy.getOldPassWord());
        map.put("createDate", pwdStrategy.getCreateDate());
        map.put("createTime", pwdStrategy.getCreateTime());
        map.put("useSystem", pwdStrategy.getUseSystem());
        map.put("status", pwdStrategy.getStatus());
        return dao.insert("login.insertPwdStrategy", map);
        
    }

    
    /**
     * 
     * 方法名： selectPwdStrategy.<br/>
     * 方法作用:查询密码策略表中该系统此用户下获取时间用.<br/>
     * 创建者：zhang jun<br/>
     * 创建日期：2016年11月2日.<br/>
     * 创建时间：下午5:50:41.<br/>
     * 参数者异常：@param pwdStrategy
     * 返回值： @return 返回结果：int.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    @SuppressWarnings("unchecked")
    public List<PwdStrategy>  selectPwdStrategy(PwdStrategy pwdStrategy) {
        Map<String, String> map= new HashMap<String, String>();
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("useSystem", pwdStrategy.getUseSystem());
        return dao.queryForList("login.selectPwdStrategy", map);
    }
    
   
    
    /**
     * 
     * 方法名： selectPwdStrategyCount.<br/>
     * 方法作用:查询用户是否存在于密码策略表中.<br/>
     * 创建者：zhang jun<br/>
     * 创建日期：2016年11月3日.<br/>
     * 创建时间：下午12:56:42.<br/>
     * 参数者异常：@param pwdStrategy
     * 返回值： @return 返回结果：int.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    public int selectPwdStrategyCount(PwdStrategy pwdStrategy) {
        Map<String, String> map= new HashMap<String, String>();
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("useSystem", pwdStrategy.getUseSystem());
        return (Integer) dao.queryForObject("login.selectPwdStrategyCount", map);
    }
	
    
    

    /**
     * 方法名： getTablePwdStrategyId.<br/>
     * 方法作用:获取下一个策略表 下一个序列编号.<br/>
     * 创建者：zhangjun.<br/>
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午1:56:58.<br/>
     * 返回值： @return 返回结果：Integer.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    public Integer getTablePwdStrategyId() {
        return (Integer) dao.queryForObject("login.getTablePwdStrategyId");
    }
    
    /**
     * 修改密码策略表的用户密码
     * @param pwdStrategy  密码策略表模型
     * @return 修改成功
     */
    public int updatePwdStrategy(PwdStrategy pwdStrategy) {
        log.info("用户："+pwdStrategy.getLoginName()+ ":修改密码策略表的用户密码");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("passWord", pwdStrategy.getPassWord());
        map.put("oldPassWord", pwdStrategy.getOldPassWord());
        map.put("useSystem", pwdStrategy.getUseSystem());
        map.put("createDate", pwdStrategy.getCreateDate());
        map.put("createTime", pwdStrategy.getCreateTime());      
        return dao.update("login.updatePwdStrategy", map);
      
    }

    
    /**
     * 方法名： updateResetPwdStrategy.<br/>
     * 方法作用:密码重置时修改密码策略表的用户密码.<br/>
     * 创建者：zhangjun.<br/>
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午2:39:47.<br/>
     * 参数者异常：@param pwdStrategy
     * 返回值： @return 返回结果：int.<br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    public int updateResetPwdStrategy(PwdStrategy pwdStrategy) {
        log.info("用户："+pwdStrategy.getLoginName()+ ":密码重置时修改密码策略表的用户密码");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginName", pwdStrategy.getLoginName());
        map.put("passWord", pwdStrategy.getPassWord());
        map.put("oldPassWord", pwdStrategy.getOldPassWord());
        map.put("useSystem", pwdStrategy.getUseSystem());
        map.put("createDate", pwdStrategy.getCreateDate());
        map.put("createTime", pwdStrategy.getCreateTime());      
        return dao.update("login.updatePwdStrategy", map);
    } 
       
    
    /**
     * 
     * 方法名： selectPwdStrategyByUserId.<br/>
     * 方法作用:修改密码时查找此用户.<br/>
     * 创建者：zhang jun<br/>
     * 创建日期：2016年11月3日.<br/>
     * 创建时间：下午12:58:23.<br/>
     * 参数者异常：@param userId
     * 返回值： @return   返回结果：List<UserBean>. <br/>
     * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
     */
    @SuppressWarnings("unchecked")
    public List<UserBean> selectUserLoginByUserId(String userId) {
        Map<String, String> map = new HashMap<String, String>(); 
        map.put("userId", userId);      
        return dao.queryForList("login.selectUserLoginByUserId", map);
    }
	
    /**
     * 【方法名】    : (更新用户信息). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月19日 上午11:54:13 .<br/>
     * 【参数】： .<br/>
     * @param userBean
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int updateByPrimaryKey(UserBean userBean)throws Exception{
        return dao.update("login.updateByPrimaryKey", userBean);
    }

    
	
}
