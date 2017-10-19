package com.compass.terminalmanage.model;

/**
 * 
 * @author gaoyang
 * 终端实体类
 */
public class TerminalManageBean {

	private String onlyCode;//终端唯一识别码(终端编号+来源系统编号+商户号）
	private String systemId;//来源系统编号
	private String systemSource;//来源系统名称
	private String merchantCode;//商户号
	private String terminalCode;//终端编号
	private String terminaltypeId;//终端类型编号
	private String terminaltypeName;//终端类型名称
	private String agencyId;// '所属机构编号'
	private String agencyName;//机构名称
	private Integer terminalStatus;//终端状态' 0.未激活1.已激活2.冻结3.回拨4.作废
	private String terminalStatusStr;
	private String terminalDesc;//终端描述
	private String openDate;//开通时间
	private String clientCode;//所属客户编码
	private String scrapDate;//报废时间
	private String createId;//创建人编号
	private String createDt;//创建时间
	private String activeDt;//激活时间 
	private String bindDt;//绑定时间
	private String isJoincash;//是否参加押金模式
	private String isPay; //是否已经支付押金
    /**
     * 绑定状态
     */
    private String  havePsam;

    /**
     * 描述：获取属性值.<br/>
     * 创建人：张柯. <br/>
     * 返回类型：@return bindDt .<br/>
     */
    public String getBindDt() {
        return bindDt;
    }

    /**
     * 创建人：张柯 <br/>
     * 创建时间：2017年7月26日 上午10:33:40 <br/>
     * 参数: @param  bindDt 设置值.  <br/>
     */
    public void setBindDt(String bindDt) {
        this.bindDt = bindDt;
    }

    /**
     * 
     * 方法名： getHavePsam.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月6日.<br/>
     * 创建时间：上午10:07:59.<br/>
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getHavePsam() {
        return havePsam;
    }

    /**
     * 
     * 方法名： setHavePsam.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月6日.<br/>
     * 创建时间：上午10:08:06.<br/>
     * 参数者异常：@param havePsam .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setHavePsam(String havePsam) {
        this.havePsam = havePsam;
    }

    public String getOnlyCode() {
		return onlyCode;
	}

	public void setOnlyCode(String onlyCode) {
		this.onlyCode = onlyCode;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getSystemSource() {
		return systemSource;
	}

	public void setSystemSource(String systemSource) {
		this.systemSource = systemSource;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getTerminaltypeId() {
		return terminaltypeId;
	}

	public void setTerminaltypeId(String terminaltypeId) {
		this.terminaltypeId = terminaltypeId;
	}

	public String getTerminaltypeName() {
		return terminaltypeName;
	}

	public void setTerminaltypeName(String terminaltypeName) {
		this.terminaltypeName = terminaltypeName;
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

	public Integer getTerminalStatus() {
		return terminalStatus;
	}

	public void setTerminalStatus(Integer terminalStatus) {
		this.terminalStatus = terminalStatus;
	}

	public String getTerminalStatusStr() {
		return terminalStatusStr;
	}

	public void setTerminalDesc(String terminalDesc) {
		this.terminalDesc = terminalDesc;
	}

	public String getTerminalDesc() {
		return terminalDesc;
	}

	public void setTerminalStatusStr(String terminalStatusStr) {
		this.terminalStatusStr = terminalStatusStr;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getScrapDate() {
		return scrapDate;
	}

	public void setScrapDate(String scrapDate) {
		this.scrapDate = scrapDate;
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

	public String getActiveDt() {
		return activeDt;
	}

	public void setActiveDt(String activeDt) {
		this.activeDt = activeDt;
	}

    public String getIsJoincash() {
        return isJoincash;
    }

    public void setIsJoincash(String isJoincash) {
        this.isJoincash = isJoincash;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

	
}
