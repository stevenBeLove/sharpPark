package com.compass.splitfee.model;

import java.util.HashMap;
import java.util.Map;

public class ResetBean {
	
	private String agencyId;           //计算机构编号
	private String agencyName;		   //计算机构名称
	private String parentAgencyId;	   //计算上级机构编号
	private String parentAgencyName;	   //计算上级机构编号
	private String startDate;		   //开始日期
	private String endDate;			   //线束日期
	private String status;			   //0:初始;1:计算中;2:计算完成;3:计算出错
	private String timestamp;		   //时间戳 间
	private String applyAgencyid;	   //申请 机构 
	private String applyAgencyname;	   //申请 机构 
	private String apply_date;		   //申请日期
	private String statusStr;
	
	private static Map<String,String> map=new HashMap<String, String>();
	
	static{
		map=new HashMap<String, String>();
		map.put("0", "初始");
		map.put("1", "计算中");
		map.put("2", "计算完成");
		map.put("3", "计算出错");
	}
	
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getParentAgencyId() {
		return parentAgencyId;
	}
	public void setParentAgencyId(String parentAgencyId) {
		this.parentAgencyId = parentAgencyId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}
	public String getStatusStr() {
		return map.get(this.status);
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getParentAgencyName() {
		return parentAgencyName;
	}
	public void setParentAgencyName(String parentAgencyName) {
		this.parentAgencyName = parentAgencyName;
	}
	public String getApplyAgencyid() {
		return applyAgencyid;
	}
	public void setApplyAgencyid(String applyAgencyid) {
		this.applyAgencyid = applyAgencyid;
	}
	public String getApplyAgencyname() {
		return applyAgencyname;
	}
	public void setApplyAgencyname(String applyAgencyname) {
		this.applyAgencyname = applyAgencyname;
	}
	 
	
}
