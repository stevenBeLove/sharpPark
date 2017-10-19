package com.compass.terminalmanage.model;

/**
 * 
 * @author wangyuchao
 * 终端流水实体类
 *
 */
public class TerminalSerilBean {
	private Integer serilNumber;// 流水编号
	private String agencyId;// 机构编号
	private String agencyName;// 机构名称
	private String newagencyId;// 新的机构编号
	private String newagencyName;// 新机构名称
	private String oldagencyId;// 原来机构编号
	private String oldagencyName;// 原来机构名称
	private String terminalId;// 终端编号
	private String terminaltypeId;// 终端类型编号
	private String terminalName;// 终端类型名称
	private String terminalDesc;// 终端操作描述
	private String createId;// '创建人编号'
	private String createDt;// '创建时间

	public Integer getSerilNumber() {
		return serilNumber;
	}

	public void setSerilNumber(Integer serilNumber) {
		this.serilNumber = serilNumber;
	}

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

	public String getNewagencyId() {
		return newagencyId;
	}

	public void setNewagencyId(String newagencyId) {
		this.newagencyId = newagencyId;
	}

	public String getNewagencyName() {
		return newagencyName;
	}

	public void setNewagencyName(String newagencyName) {
		this.newagencyName = newagencyName;
	}

	public String getOldagencyId() {
		return oldagencyId;
	}

	public void setOldagencyId(String oldagencyId) {
		this.oldagencyId = oldagencyId;
	}

	public String getOldagencyName() {
		return oldagencyName;
	}

	public void setOldagencyName(String oldagencyName) {
		this.oldagencyName = oldagencyName;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalDesc() {
		return terminalDesc;
	}

	public void setTerminalDesc(String terminalDesc) {
		this.terminalDesc = terminalDesc;
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

	public String getTerminaltypeId() {
		return terminaltypeId;
	}

	public void setTerminaltypeId(String terminaltypeId) {
		this.terminaltypeId = terminaltypeId;
	}

}
