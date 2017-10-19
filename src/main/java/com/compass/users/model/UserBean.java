/**
 * 
 */
package com.compass.users.model;

/**
 * @author wangyuchao
 * 
 * 用户实体类
 * 
 */
public class UserBean {
	private String userid;//用户编号
	private String username;//用户名称
	private String loginname;//登录名称
	private String password;//密码
	private String email;//用户邮箱
	private String phone;//用户电话
	private String address;//用户地址
	private String agencyId;//机构编号
	private String usertypeId;//用户类型编号
	private String parentagencyId;//父机构编号
	private String roletypeId;//角色类型编号
	private String bouncedTime;//弹框时间

	/**
     * 描述：获取属性值.<br/>
     * 创建人：fuyu <br/>
     * 返回类型：@return bouncedTime .<br/>
     */
    public String getBouncedTime() {
        return bouncedTime;
    }

    /**
     * 创建人：fuyu <br/>
     * 创建时间：2016年12月2日 上午10:00:08 <br/>
     * 参数: @param  bouncedTime 设置值.  <br/>
     */
    public void setBouncedTime(String bouncedTime) {
        this.bouncedTime = bouncedTime;
    }

    public String getRoletypeId() {
		return roletypeId;
	}

	public void setRoletypeId(String roletypeId) {
		this.roletypeId = roletypeId;
	}

	public String getParentagencyId() {
		return parentagencyId;
	}

	public void setParentagencyId(String parentagencyId) {
		this.parentagencyId = parentagencyId;
	}

	public String getUsertypeId() {
		return usertypeId;
	}

	public void setUsertypeId(String usertypeId) {
		this.usertypeId = usertypeId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
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

	
}
