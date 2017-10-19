package com.compass.agency.model;

/**
 * 
 * @author wangyuchao
 * 机构数实体类
 *
 */
public class AgentreeBean {
	private String  agencyId;     //机构编号
	private String agencyName;		//机构名称
	private String isParent;		//是否有父机构
	private String isChild;			//是否有子机构
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getIsChild() {
		return isChild;
	}
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}
	
}
