package com.compass.userrole.model;

/**
 * 
 * @author wangyuchao
 * 用户管理实体类
 *
 */
public class UserRoleBean {
	private String userId;//用户ID
	private String userName;//用户姓名
	private String loginName;//登录名称
	private String password;//登录密码
	private String email;//邮箱
	private String phone;//电话
	private String address;//地址
	private String comments;//备注 
	private String agencyId;//机构编号
	private String parentagencyId;//上级机构编号
	private String agencyName;//所属机构
	private String status;//状态1.有效 2.无效
	private String roleId;//角色编号
	private String createId;//操作人 
	private String createDt;//操作时间 
	private String statusStr;
	private String modifyId;//修改编号
	private String modifyDt;//修改时间
	private String usertypeId;//用户类型编号
	private String systemsource; //来源系统
	private String systemsourceName;  //来源系统名称
	
	private String agencyid; 
	private String parentagencyid;//上级机构编号
	private String roleName; //角色名
	private String isDtbUser;//是否为瑞推客用户
	private String dtbProfitFlag;//瑞推客用户是否分润标识
	
	private String isAdmin; //是否为管理员
	
	private String homeId; //首页登录类型
	
	private String isGrab; //是否抓取通讯录
	
	public static String GRAB = "1"; //1 抓取通讯录 
	
	public String getUsertypeId() {
		return usertypeId;
	}
	public void setUsertypeId(String usertypeId) {
		this.usertypeId = usertypeId;
	}
	public String getUsertypeName() {
		return usertypeName;
	}
	public void setUsertypeName(String usertypeName) {
		this.usertypeName = usertypeName;
	}
	private String usertypeName;
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getComment() {
		return comments;
	}
	public void setComment(String comment) {
		this.comments = comment;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public String getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(String modifyDt) {
		this.modifyDt = modifyDt;
	}
	public String getParentagencyId() {
		return parentagencyId;
	}
	public void setParentagencyId(String parentagencyId) {
		this.parentagencyId = parentagencyId;
	}
	public String getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(String agencyid) {
		this.agencyid = agencyid;
	}
	public String getSystemsource() {
		return systemsource;
	}
	public void setSystemsource(String systemsource) {
		this.systemsource = systemsource;
	}
	public String getSystemsourceName() {
		return systemsourceName;
	}
	public void setSystemsourceName(String systemsourceName) {
		this.systemsourceName = systemsourceName;
	}
	public String getParentagencyid() {
		return parentagencyid;
	}
	public void setParentagencyid(String parentagencyid) {
		this.parentagencyid = parentagencyid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
  public String getIsDtbUser() {
      return isDtbUser;
  }
  public void setIsDtbUser(String isDtbUser) {
      this.isDtbUser = isDtbUser;
  }
  public String getDtbProfitFlag() {
      return dtbProfitFlag;
  }
  public void setDtbProfitFlag(String dtbProfitFlag) {
      this.dtbProfitFlag = dtbProfitFlag;
  }
  public String getIsAdmin() {
      return isAdmin;
  }
  public void setIsAdmin(String isAdmin) {
      this.isAdmin = isAdmin;
  }
  public String getHomeId() {
      return homeId;
  }
  public void setHomeId(String homeId) {
      this.homeId = homeId;
  }
  public String getComments() {
      return comments;
  }
  public void setComments(String comments) {
      this.comments = comments;
  }
  public String getIsGrab() {
      return isGrab;
  }
  public void setIsGrab(String isGrab) {
      this.isGrab = isGrab;
  }
      	
	
}
