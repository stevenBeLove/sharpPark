/**
 * 
 */
package com.compass.role.model;

/**
 * 
 * @author wangyuchao
 * 角色实体类
 * 
 */
public class RoleBean {
	private String roleId;// 角色编号
	private String roleName;// 角色名称
	private String roleDesc;// 角色描述
	private String status;// 角色状态
	private String statusStr; // 角色状态描述
	private String createId;// 创建人编号
	private String createDt;// 创建时间
	private String modifyid;// 操作人编号
	private String modifydt;// 操作时间
	private String roletypeId;// 角色类型编号
	private String roletypeName;// 角色类型名称
	private String userCount;//用户数量
	private String menuCount;//菜单数量

	public String getMenuCount() {
		return menuCount;
	}

	public void setMenuCount(String menuCount) {
		this.menuCount = menuCount;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getRoletypeName() {
		return roletypeName;
	}

	public void setRoletypeName(String roletypeName) {
		this.roletypeName = roletypeName;
	}

	public String getRoletypeId() {
		return roletypeId;
	}

	public void setRoletypeId(String roletypeId) {
		this.roletypeId = roletypeId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
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

	public String getModifyid() {
		return modifyid;
	}

	public void setModifyid(String modifyid) {
		this.modifyid = modifyid;
	}

	public String getModifydt() {
		return modifydt;
	}

	public void setModifydt(String modifydt) {
		this.modifydt = modifydt;
	}

	public RoleBean() {
		super();
	}

}
