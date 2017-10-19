package com.compass.agency.model;

/**
 * 
 * @author wangyuchao 机构实体类
 *
 */
public class AgencyBean {
	private String systemId;// 来源系统编号
	private String systemName;// 来源系统名称
	private String agency_id; // 机构编号
	private String upperAgencyid; // 上级机构编号
	private String companyPhone; // 企业电话
	private String companyEmail; // 企业邮箱
	private String companyAddr; // 企业地址
	private String certificate; // 企业证书
	private String legal_info; // 法人信息
	private String organizationCode; // 组织机构代码
	private String agreementcode; // 合同编号
	private String provinceId; // 省编号
	private String cityId; // 城市编号
	private String vestagencyName; // 归属机构
	private String agency_status; // 实名状态
	private String accountBank; // 开户银行
	private String bankId; // 开户银行编码
	private String accountName; // 户名
	private String provinceName; // 省名称
	private String cityName; // 市名称
	private String onlinechannel; // 上级机构编码
	private String vestagencyId; // 归属机构编号
	private String vestBrandId; // 定制商归属机构编号
	private String agencystatusSrc; // 机构状态描述
	private String depagencyId; // 部门编号
	private String agencydesc; // 机构描述
	private String bankcode; // 账号
	private String contactsName; // 企业联系人名称
	private String companyName; // 企业名称
	private String companyBriefName; // 企业简称
	private String uppercompanyName; // 上级机构名称
	private String createId; // 创建人编号
	private String createDt; // 创建时间

	private String userCount;// 用户数量
	private String _parentId;
	private String agencyType; // 机构类型：1-代理商；2-定制商

	private Integer start;
	private Integer end;

	private String isParent;
	
	private String bouncedTime;//弹框时间
	
	/**
	 * 代理商修改公司名称
	 */
	private String renameCompanyName; 
	
	/**
	 * 是否瑞推客用户
	 */
	private String isDtbUser;
	
	/**
	 * 瑞推客是否分润
	 */
	private String dtbProfitFlag;
	
	/**
	 * 身份证号码
	 */
	private String userpId;
	
    /**
     * 激活状态
     */
    private String  status;

    /**
     * 注册url
     */
    private String  registerUrl;

    /**
     * 激活时间
     */
    private String  actTime;

    /**
     * 联系人电话
     */
    private String  someonePhone;

    /**
     * 联系人姓名
     */
    private String  someoneName;
    
    /**
     * 
     * 方法名： getSomeonePhone.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月30日.<br/>
     * 创建时间：下午5:00:18.<br/>
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getSomeonePhone() {
        return someonePhone;
    }
    
    /**
     * 
     * 方法名： setSomeonePhone.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月30日.<br/>
     * 创建时间：下午5:00:30.<br/>
     * 参数者异常：@param someonePhone .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setSomeonePhone(String someonePhone) {
        this.someonePhone = someonePhone;
    }
    
    /**
     * 
     * 方法名： getSomeoneName.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月30日.<br/>
     * 创建时间：下午5:00:39.<br/>
     * 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：String.<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getSomeoneName() {
        return someoneName;
    }
    
    /**
     * 
     * 方法名： setSomeoneName.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月30日.<br/>
     * 创建时间：下午5:00:46.<br/>
     * 参数者异常：@param someoneName .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setSomeoneName(String someoneName) {
        this.someoneName = someoneName;
    }
    /**
     * 
     * 方法名： getStatus.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:37:58.<br/>
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getStatus() {
        return status;
    }
    /**
     * 
     * 方法名： setStatus.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:38:06.<br/>
     * 参数者异常：@param status .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * 
     * 方法名： getRegisterUrl.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:38:11.<br/>
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getRegisterUrl() {
        return registerUrl;
    }
    
    /**
     * 
     * 方法名： setRegisterUrl.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:38:24.<br/>
     * 参数者异常：@param reisterUrl .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }
    
    /**
     * 
     * 方法名： getActTime.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:39:13.<br/>
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String getActTime() {
        return actTime;
    }
    
    /**
     * 
     * 方法名： setActTime.<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月29日.<br/>
     * 创建时间：下午2:39:05.<br/>
     * 参数者异常：@param actTime .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    /**
     * 描述：获取属性值.<br/>
     * 创建人：fuyu <br/>
     * 返回类型：@return bouncedTime .<br/>
     */
    public String getBouncedTime() {
        return bouncedTime;
    }

    /**
     * 创建人：fuyu <br/>
     * 创建时间：2016年12月1日 下午5:31:19 <br/>
     * 参数: @param  bouncedTime 设置值.  <br/>
     */
    public void setBouncedTime(String bouncedTime) {
        this.bouncedTime = bouncedTime;
    }

    public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAgencydesc() {
		return agencydesc;
	}

	public void setAgencydesc(String agencydesc) {
		this.agencydesc = agencydesc;
	}

	public String getDepagencyId() {
		return depagencyId;
	}

	public void setDepagencyId(String depagencyId) {
		this.depagencyId = depagencyId;
	}

	public String getAgencystatusSrc() {
		return agencystatusSrc;
	}

	public void setAgencystatusSrc(String agencystatusSrc) {
		this.agencystatusSrc = agencystatusSrc;
	}

	public String getVestagencyId() {
		return vestagencyId;
	}

	public void setVestagencyId(String vestagencyId) {
		this.vestagencyId = vestagencyId;
	}

	public String getVestagencyName() {
		return vestagencyName;
	}

	public void setVestagencyName(String vestagencyName) {
		this.vestagencyName = vestagencyName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUpperAgencyid() {
		return upperAgencyid;
	}

	public void setUpperAgencyid(String upperAgencyid) {
		this.upperAgencyid = upperAgencyid;
	}

	public String getUppercompanyName() {
		return uppercompanyName;
	}

	public void setUppercompanyName(String uppercompanyName) {
		this.uppercompanyName = uppercompanyName;
	}

	public String getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(String agency_id) {
		this.agency_id = agency_id;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getLegal_info() {
		return legal_info;
	}

	public void setLegal_info(String legal_info) {
		this.legal_info = legal_info;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getAgreementcode() {
		return agreementcode;
	}

	public void setAgreementcode(String agreementcode) {
		this.agreementcode = agreementcode;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOnlinechannel() {
		return onlinechannel;
	}

	public void setOnlinechannel(String onlinechannel) {
		this.onlinechannel = onlinechannel;
	}

	public String getAgency_status() {
		return agency_status;
	}

	public void setAgency_status(String agency_status) {
		this.agency_status = agency_status;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
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

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}

	public String getCompanyBriefName() {
		return companyBriefName;
	}

	public void setCompanyBriefName(String companyBriefName) {
		this.companyBriefName = companyBriefName;
	}

	public String getVestBrandId() {
		return vestBrandId;
	}

	public void setVestBrandId(String vestBrandId) {
		this.vestBrandId = vestBrandId;
	}

	public String getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	/** 
	 * 方法名： getIsParent.<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 返回值：@return 返回值 
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月26日.<br/>
	 * 创建时间：下午5:25:40.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getIsParent() {
		return isParent;
	}

	/** 
	 * 方法名： setIsParent.<br/>
	 * 方法作用:TODO(简单描述).<br/>
	 *
	 * 参数： @param isParent 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月26日.<br/>
	 * 创建时间：下午5:25:40.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getRenameCompanyName() {
		return renameCompanyName;
	}

	public void setRenameCompanyName(String renameCompanyName) {
		this.renameCompanyName = renameCompanyName;
	}

  public String getIsDtbUser() {
      return isDtbUser;
  }

  public void setIsDtbUser(String isDtbUser) {
      this.isDtbUser = isDtbUser;
  }

  public String getDtbProfitFlag() {
      return dtbProfitFlag;
  }
  
  public void setDtbProfitFlag(String dtbProfitFlag) {
      this.dtbProfitFlag = dtbProfitFlag;
  }

  public String getUserpId() {
      return userpId;
  }
  
  public void setUserpId(String userpId) {
      this.userpId = userpId;
  }

	
}
