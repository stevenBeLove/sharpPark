/**
 * 
 */
package com.compass.authority.model;

/**
 * @author wangYuChao
 * 菜单实体类
 */
public class AuthorityBean {
	private String menuId; //菜单编号
	private String menuName;//菜单名称
	private String parentNodeId;//父菜单编号
	private String menuStatus;//菜单状态
	private String menuDesc;//菜单URL
	private String comments;//备注
	private String operator;//操作人
	private String operationTime;//操作时间
	private String status;//
	private String childCode;
	private String createId;
	private String createDt;
	private String modifyId;
	private String modifyDt;
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getParentNodeId() {
		return parentNodeId;
	}
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}
	public String getMenuStatus() {
		return menuStatus;
	}
	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}
	public String getMenuDesc() {
		return menuDesc;
	}
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}
	public String getComment() {
		return comments;
	}
	public void setComment(String comments) {
		this.comments = comments;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
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
	
	

}
