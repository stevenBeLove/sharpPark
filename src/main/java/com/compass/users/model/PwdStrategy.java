/** 
 * 包名: package com.raising.system.modules.usermanager.vo; <br/> 
 * 添加时间: 2016年10月18日 下午2:04:40 <br/> 
 */
package com.compass.users.model;


/** 
 * 类名: PwdStrategyVO <br/> 
 * 作用：TODO(简单一句话描述)<br/> 
 * 方法：TODO(简单描述方法)<br/> 
 * 创建者: fuyu. <br/> 
 * 添加时间: 2016年10月18日 下午2:04:40 <br/> 
 * 版本： JDK 1.6 innerpm 1.0
 */
public class PwdStrategy   {

	/**
	 * 密码策略表对应的模型
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;            ///用户编号
	
	private String loginName;  ///系统登录名
	
	private String passWord;  ///系统旧密码
	
	private String oldPassWord; ///系统旧密码
	
	private String useSystem; ///系统旧密码
	
	private String createDate; ///系统旧密码
	
	private String createTime; ///创建时间
	
	private String status;   ///状态   1:有效 0：无效
	
	

/**
 * 
 * 方法名： getId.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:43:42.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：int.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public int getId() {
		return id;
	}

/**
 * 
 * 方法名： setId.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:43:48.<br/>
 * 参数者异常：@param id .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setId(int id) {
		this.id = id;
	}

/**
 * 
 * 方法名： getLoginName.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:43:59.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getLoginName() {
		return loginName;
	}

/**
 * 
 * 方法名： setLoginName.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:44:05.<br/>
 * 参数者异常：@param loginName .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

/**
 * 
 * 方法名： getPassWord.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:44:10.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getPassWord() {
		return passWord;
	}

/**
 * 
 * 方法名： setPassWord.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:44:15.<br/>
 * 参数者异常：@param passWord .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

/**
 * 
 * 方法名： getOldPassWord.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:44:19.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getOldPassWord() {
		return oldPassWord;
	}

/**
 * 
 * 方法名： setOldPassWord.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:44:28.<br/>
 * 参数者异常：@param oldPassWord .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}

/**
 * 
 * 方法名： getUseSystem.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:05.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getUseSystem() {
		return useSystem;
	}

/**
 * 
 * 方法名： setUseSystem.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:07.<br/>
 * 参数者异常：@param useSystem .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setUseSystem(String useSystem) {
		this.useSystem = useSystem;
	}

	
/**
 * 
 * 方法名： getCreateDate.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:09.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getCreateDate() {
		return createDate;
	}

/**
 * 
 * 方法名： setCreateDate.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:11.<br/>
 * 参数者异常：@param createDate .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

/**
 * 
 * 方法名： getCreateTime.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:13.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getCreateTime() {
		return createTime;
	}

/**
 * 
 * 方法名： setCreateTime.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:14.<br/>
 * 参数者异常：@param createTime .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

/**
 * 
 * 方法名： getStatus.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:18.<br/>
 * 参数者异常：@return .<br/>
 * 返回值： @return 返回结果：String.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public String getStatus() {
		return status;
	}

/**
 * 
 * 方法名： setStatus.<br/>
 * 适用条件:TODO(简单描述).<br/> 
 * 执行流程:TODO(简单描述).<br/> 
 * 注意事项:TODO(简单描述).<br/>
 * 方法作用:TODO(简单描述).<br/>
 *
 * 创建者：zhang jun<br/>
 * 创建日期：2016年11月2日.<br/>
 * 创建时间：下午4:45:20.<br/>
 * 参数者异常：@param status .<br/>
 * 返回值： @return 返回结果：void.<br/>
 * 其它内容： JDK 1.6 ProfitTerrace 1.0.<br/>
 */
	public void setStatus(String status) {
		this.status = status;
	}



}

