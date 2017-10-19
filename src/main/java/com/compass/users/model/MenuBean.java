package com.compass.users.model;


import java.io.Serializable;
/**
 * 
 * @author wangyuchao
 * 菜单实体类
 *
 */
public class MenuBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String menuId;//菜单编号
	private String menuName;//菜单名称
	private String menuUrl;//菜单超链接
	private String menuFather;//父菜单编号
	private String childCode;//是否有子菜单

	public String getChildCode() {
		return childCode;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}

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

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuFather() {
		return menuFather;
	}

	public void setMenuFather(String menuFather) {
		this.menuFather = menuFather;
	}

}
