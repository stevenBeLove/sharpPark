package com.compass.pasmFee.model;


import java.text.DecimalFormat;

import com.compass.utils.NumberFormat;

/**
 * <pre>
 * 【类型】: PsmFee <br/> 
 * 【作用】: . <br/>  
 * 【时间】：2017年2月14日 上午10:22:18 <br/> 
 * 【作者】：yinghui zhang <br/>
 * </pre>
 */
public class PsmFee {
	/**
	 * 主键Id
	 */
	private String id;

	/**
	 * PASM卡号
	 */
	private String psamid;

	/**
	 * 代理商Id
	 */
	private String agencyId;

	/**
	 * 代理商名称
	 */
	private String agencyName;

	/**
	 * 产品类型编码
	 */
	private String businessType;

	/**
	 * 产品类型编码名称
	 */
	private String businessTypeName;

	/**
	 * 交易费率
	 */
	private String shopno;

	/**
	 * 交易固定费率
	 */
	private String fixFee;

	/**
	 * 创建时间
	 */
	private String operdt;

	/**
	 * 创建人
	 */
	private String oper;

	/**
	 * 创建人名字
	 */
	private String operUserName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 交易类型
	 */
	private String businessName;
	
	/** 交易類型 */
	private String transtype;

	
	/** 机构成本费率 */
	private String costrate;
	
	/** 机构成本单笔金额 */
	private String costfix;
	
	/** 终端状态 */
	private String psamstatus;
	/** 激活状态 */
	private String terminalStatus;
	 /** 激活状态 中文*/
	private String terminalStatusStr;

    /**
     * 描述：获取属性值.<br/>
     * 创建人：张柯. <br/>
     * 返回类型：@return terminalStatus .<br/>
     */
    public String getTerminalStatus() {
        return terminalStatus;
    }

    /**
     * 创建人：张柯 <br/>
     * 创建时间：2017年8月4日 上午11:15:41 <br/>
     * 参数: @param  terminalStatus 设置值.  <br/>
     */
    public void setTerminalStatus(String terminalStatus) {
        this.terminalStatus = terminalStatus;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：张柯. <br/>
     * 返回类型：@return terminalStatusStr .<br/>
     */
    public String getTerminalStatusStr() {
        return terminalStatusStr;
    }

    /**
     * 创建人：张柯 <br/>
     * 创建时间：2017年8月4日 上午11:15:41 <br/>
     * 参数: @param  terminalStatusStr 设置值.  <br/>
     */
    public void setTerminalStatusStr(String terminalStatusStr) {
        this.terminalStatusStr = terminalStatusStr;
    }

    public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getOperUserName() {
		return operUserName;
	}

	public void setOperUserName(String operUserName) {
		this.operUserName = operUserName;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getPsamid() {
		return psamid;
	}

	public void setPsamid(String psamid) {
		this.psamid = psamid == null ? null : psamid.trim();
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId == null ? null : agencyId.trim();
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType == null ? null : businessType.trim();
	}

	public String getShopno() {
	  return  NumberFormat.parseYuanFormat(shopno);
	}

	public void setShopno(String shopno) {
		/***
		 * 
		 * DecimalFormat 实例很重不适合在大数据的使用   在后期需要优化（  建议使用ThreadLocl 或者在数据量不大的情况下 使用同步块方法）
		 * 
		 */
		DecimalFormat decimalFormat = new DecimalFormat("######0.000");
		this.shopno = shopno == null ? null : decimalFormat.format(Double
				.parseDouble(shopno));
	}

	public String getFixFee() {
		return NumberFormat.parseYuanFormat(fixFee);
	}

	public void setFixFee(String fixFee) {
		this.fixFee = fixFee;
	}

	public String getOperdt() {
		return operdt;
	}

	public void setOperdt(String operdt) {
		this.operdt = operdt == null ? null : operdt.trim();
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper == null ? null : oper.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	
	public String getCostfix() {
		return costfix;
	}

	public void setCostfix(String costfix) {
		this.costfix = costfix;
	}

	public String getCostrate() {
		return costrate;
	}

	public void setCostrate(String costrate) {
	//	this.costrate = costrate;
		DecimalFormat decimalFormat = new DecimalFormat("######0.0000");
		this.costrate = costrate == null ? null : decimalFormat.format(Double
				.parseDouble(costrate));
	}

	public String getPsamstatus() {
		return psamstatus;
	}

	public void setPsamstatus(String psamstatus) {
		this.psamstatus = psamstatus;
	}
	
	
//
//	public static void main(String[] args) {
//		DecimalFormat decimalFormat = new DecimalFormat("######0.000");
//		String s = decimalFormat.format(Double.parseDouble(".21255"));
//		System.out.println(s);
//	}
}