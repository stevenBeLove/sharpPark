package com.compass.dealmanager.model;

/**
 * 
 * @author machizhi
 * 扣款类型实体类
 * 
 */
public class DeductTypeBean {
	private String deductClass;			// 扣款类别
	private String deductName;			// 扣款名称
	private String deductType;				// 扣款类型
	private String status;						// 状态
	private String createId;					// 创建人编号
	private String createDt;					// 创建时间
	private String comments;				// 备注
	
	private String deductClassName;		// 扣款类别名称
	private String statusName;				// 状态名称
	
	public String getDeductClass() {
		return deductClass;
	}
	public void setDeductClass(String deductClass) {
		this.deductClass = deductClass;
	}
	public String getDeductName() {
		return deductName;
	}
	public void setDeductName(String deductName) {
		this.deductName = deductName;
	}
	public String getDeductType() {
		return deductType;
	}
	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDeductClassName() {
		return deductClassName;
	}
	public void setDeductClassName(String deductClassName) {
		this.deductClassName = deductClassName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
