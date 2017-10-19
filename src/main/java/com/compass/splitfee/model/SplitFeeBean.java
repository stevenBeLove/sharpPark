/**
 * 
 */
package com.compass.splitfee.model;

/**
 * @author wangLong 分润规则实体Bean
 * 
 */
public class SplitFeeBean {

	private String executeCode; // 执行编号
	private String startTime; // 开始时间
	private String endTime;// 结束时间
	private String executeTime;// 执行时间
	private String executeReult; // 执行结果
	private String executeDesc;// 执行描述

	public String getExecuteCode() {
		return executeCode;
	}

	public void setExecuteCode(String executeCode) {
		this.executeCode = executeCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getExecuteReult() {
		return executeReult;
	}

	public void setExecuteReult(String executeReult) {
		this.executeReult = executeReult;
	}

	public String getExecuteDesc() {
		return executeDesc;
	}

	public void setExecuteDesc(String executeDesc) {
		this.executeDesc = executeDesc;
	}

}
