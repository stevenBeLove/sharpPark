package com.compass.dealmanager.model;

import com.compass.utils.BaseModel;

/**
 * 
 * @author zhangjun 终端交易实体类
 * 
 */
public class TerminalDealBean extends BaseModel {
	/**
	 *
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;

	private String serialId;// id

	private String startDate;// 开始时间
	private String endDate;// 统计时间
	private String activateStandard;// 统计时间
	private String dealTypeId;// 交易类型
	private String startCode; // 起始编号
	private String endCode; // 结束编号
	private String agencyId;// 机构号
	private String agencyName;// 机构名称
	private String flagStatus;// 标记状态 ：0-未标记；1-已标记 2-已分配
	private String curAgencyId; // session 中获得的当前登录机构号

	private String terminalCode;// 终端编号
	private Double transAmount; // 交易金额 (单位：分)
	private String creatDt;// 记录时间
	private String flagOperator;// 标记操作人
	private String flagOperTime;// 标记时间

	private String terminalCodeStr;// 序列号字符串
	private String optFlag;// 操作标记
	private String idStr;
	
	private String  activeDate;  //激活时间
	private String  activeDateStart;  //激活时间
	private String  activeDateEnd;  //激活时间
	
    private String standardDate;    //达标时间
    private String standardAmount;  //达标金额
    private String customerId;      //客户编号
    private String flag; 
    
	
	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
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

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public Double getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}

	public String getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}

	public String getCreatDt() {
		return creatDt;
	}

	public void setCreatDt(String creatDt) {
		this.creatDt = creatDt;
	}

	public String getFlagOperator() {
		return flagOperator;
	}

	public void setFlagOperator(String flagOperator) {
		this.flagOperator = flagOperator;
	}

	public String getFlagOperTime() {
		return flagOperTime;
	}

	public void setFlagOperTime(String flagOperTime) {
		this.flagOperTime = flagOperTime;
	}

	/**
	 * 方法名： getTerminalCodeStr.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月25日.<br/>
	 * 创建时间：下午5:22:08.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getTerminalCodeStr() {
		return terminalCodeStr;
	}

	/**
	 * 方法名： setTerminalCodeStr.<br/>
	 * 参数： @param terminalCodeStr 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月25日.<br/>
	 * 创建时间：下午5:22:08.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setTerminalCodeStr(String terminalCodeStr) {
		this.terminalCodeStr = terminalCodeStr;
	}

	public String getOptFlag() {
		return optFlag;
	}

	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getActivateStandard() {
		return activateStandard;
	}

	public void setActivateStandard(String activateStandard) {
		this.activateStandard = activateStandard;
	}

	public String getDealTypeId() {
		return dealTypeId;
	}

	public void setDealTypeId(String dealTypeId) {
		this.dealTypeId = dealTypeId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStartCode() {
		return startCode;
	}

	public void setStartCode(String startCode) {
		this.startCode = startCode;
	}

	public String getEndCode() {
		return endCode;
	}

	public void setEndCode(String endCode) {
		this.endCode = endCode;
	}

	public String getCurAgencyId() {
		return curAgencyId;
	}

	public void setCurAgencyId(String curAgencyId) {
		this.curAgencyId = curAgencyId;
	}

	/**
	 * 方法名： getIdStr.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月25日.<br/>
	 * 创建时间：下午5:31:39.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getIdStr() {
		return idStr;
	}

	/**
	 * 方法名： setIdStr.<br/>
	 * 参数： @param idStr 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月25日.<br/>
	 * 创建时间：下午5:31:39.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	/** 
	 * 方法名： getActiveDate.<br/>
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:39:51.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getActiveDate() {
		return activeDate;
	}

	/** 
	 * 方法名： setActiveDate.<br/>
	 * 参数： @param activeDate 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:39:51.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	/** 
	 * 方法名： getActiveDateStart.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:48:37.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getActiveDateStart() {
		return activeDateStart;
	}

	/** 
	 * 方法名： setActiveDateStart.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 参数： @param activeDateStart 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:48:37.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setActiveDateStart(String activeDateStart) {
		this.activeDateStart = activeDateStart;
	}

	/** 
	 * 方法名： getActiveDateEnd.<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:48:37.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getActiveDateEnd() {
		return activeDateEnd;
	}

	/** 
	 * 方法名： setActiveDateEnd.<br/>
	 *
	 * 参数： @param activeDateEnd 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月29日.<br/>
	 * 创建时间：下午5:48:38.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setActiveDateEnd(String activeDateEnd) {
		this.activeDateEnd = activeDateEnd;
	}

	/** 
	 * 方法名： getStandardDate.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月3日.<br/>
	 * 创建时间：下午3:05:26.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getStandardDate() {
		return standardDate;
	}

	/** 
	 * 方法名： setStandardDate.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 参数： @param standardDate 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月3日.<br/>
	 * 创建时间：下午3:05:26.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setStandardDate(String standardDate) {
		this.standardDate = standardDate;
	}

	/** 
	 * 方法名： getStandardAmount.<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月3日.<br/>
	 * 创建时间：下午3:05:26.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getStandardAmount() {
		return standardAmount;
	}

	/** 
	 * 方法名： setStandardAmount.<br/>
	 *
	 * 参数： @param standardAmount 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月3日.<br/>
	 * 创建时间：下午3:05:26.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setStandardAmount(String standardAmount) {
		this.standardAmount = standardAmount;
	}

	/** 
	 * 方法名： getCustomerId.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月24日.<br/>
	 * 创建时间：下午2:46:16.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getCustomerId() {
		return customerId;
	}

	/** 
	 * 方法名： setCustomerId.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 参数： @param customerId 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月24日.<br/>
	 * 创建时间：下午2:46:16.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/** 
	 * 方法名： getFlag.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月24日.<br/>
	 * 创建时间：下午3:54:02.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getFlag() {
		return flag;
	}
	/** 
	 * 方法名： setFlag.<br/>
	 * 适用条件:TODO(简单描述).<br/> 
	 * 执行流程:TODO(简单描述).<br/> 
	 * 注意事项:TODO(简单描述).<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 参数： @param flag 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年5月24日.<br/>
	 * 创建时间：下午3:54:02.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
