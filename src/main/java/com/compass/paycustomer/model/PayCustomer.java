/** 
 * 包名: package com.compass.paycustomer.model; <br/> 
 * 添加时间: 2017年4月18日 下午2:33:11 <br/> 
 */
package com.compass.paycustomer.model;
/** 
 * 类名: payCustomer <br/> 
 * 添加时间: 2017年4月18日 下午2:33:11 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class PayCustomer {
    
    /**
     * 用户编码
     */
    private String customerid;

    /**
     * 企业用户的法人名字
     */
    private String username;

    /**
     * 用户类型，00:个人 01:企业
     */
    private String customertype;

    /**
     * 中文名称
     */
    private String customername;

    /**
     * 证件类型，01:身份证
     */
    private String customerpidtype;

    /**
     * 证件号码
     */
    private String customerpid;

    /**
     * 是否认证，0:未认证 1:完善资料 2：认证未通过  3：认证通过
     */
    private String customertag;

    /**
     * 审核状态
     */
    private String checkrange;

    /**
     * 添加用户时间戳
     */
    private String timestamp;


    /**创建日期
     * 
     */
    private String customerregdate;

   /**
    * 所属机构
    */
    private String branchid;

    /**
     * 是否关注，Y:关注 N:不关注
     */
    private String attention;


    /**
     * 用户等级
     */
    private Long viplevel;

    /**
     * 变更日期
     */
    private String lastchangedate;

    /**
     * 用户权限，X0 冻结X天 40永久冻结
     */
    private String memo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 注册地址
     */
    private String customeraddr;

    /**
     * 商户营业执照
     */
    private String businesslicence;


    /**
     * 初审次数
     */
    private Long firstreject;

    /**
     * 复审次数
     */
    private Long finalreject;

    /**
     * 客户手机号信息
     */
    private String mobilenolocate;

    /**
     * 标识
     */
    private String remark;

    /**
     * 信用卡状态
     */
    private String credittag;

    /**
     * 身份证有效期开始
     */
    private String idcardvalidbegin;

    /**
     * 身份证有效期结束
     */
    private String idcardvalidend;

    /**
     * 信用卡有效期结束
     */
    private String creditvalid;

    /**
     * 0
     */
    private String auditflag;

    /**
     * 0无权限，1有权限
     */
    private String perflag;

    /**
     * 用户是否已经添加公共图片 0 未添加 1 已添加
     */
    private String shopstatus;

    /**
     * 最后一次重置密码
     */
    private String lastresetpassword;

    /**
     * 
     */
    private String featureflag;

    /**
     * 是否内部员工，1--是内部员工，null--不是内部员工
     */
    private String isEmployee;

    /**
     * 标记   0--未标记   1--已标记
     */
    private String signflag;

    /**
     * 标记原因
     */
    private String signreason;

    /**
     * 身份证性别
     */
    private String sex;

    /**
     * 身份证民族
     */
    private String familyname;

    /**
     * 身份证出生日期
     */
    private String birthday;

    /**
     * 身份证地址
     */
    private String address;

    /**
     * 
     */
    private String issuedepartment;

    /**
     * 
     */
    private String isgrab;

    /**
     * 
     */
    private String ifnewuser;

    /**
     * 
     */
    private String isFans;

    /**
     * 瑞通宝同步账户,该值为瑞钱包customerid
     */
    private String rtbCustomerid;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerpidtype() {
        return customerpidtype;
    }

    public void setCustomerpidtype(String customerpidtype) {
        this.customerpidtype = customerpidtype;
    }

    public String getCustomerpid() {
        return customerpid;
    }

    public void setCustomerpid(String customerpid) {
        this.customerpid = customerpid;
    }

    public String getCustomertag() {
        return customertag;
    }

    public void setCustomertag(String customertag) {
        this.customertag = customertag;
    }

    public String getCheckrange() {
        return checkrange;
    }

    public void setCheckrange(String checkrange) {
        this.checkrange = checkrange;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomerregdate() {
        return customerregdate;
    }

    public void setCustomerregdate(String customerregdate) {
        this.customerregdate = customerregdate;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public Long getViplevel() {
        return viplevel;
    }

    public void setViplevel(Long viplevel) {
        this.viplevel = viplevel;
    }

    public String getLastchangedate() {
        return lastchangedate;
    }

    public void setLastchangedate(String lastchangedate) {
        this.lastchangedate = lastchangedate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomeraddr() {
        return customeraddr;
    }

    public void setCustomeraddr(String customeraddr) {
        this.customeraddr = customeraddr;
    }

    public String getBusinesslicence() {
        return businesslicence;
    }

    public void setBusinesslicence(String businesslicence) {
        this.businesslicence = businesslicence;
    }

    public Long getFirstreject() {
        return firstreject;
    }

    public void setFirstreject(Long firstreject) {
        this.firstreject = firstreject;
    }

    public Long getFinalreject() {
        return finalreject;
    }

    public void setFinalreject(Long finalreject) {
        this.finalreject = finalreject;
    }

    public String getMobilenolocate() {
        return mobilenolocate;
    }

    public void setMobilenolocate(String mobilenolocate) {
        this.mobilenolocate = mobilenolocate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCredittag() {
        return credittag;
    }

    public void setCredittag(String credittag) {
        this.credittag = credittag;
    }

    public String getIdcardvalidbegin() {
        return idcardvalidbegin;
    }

    public void setIdcardvalidbegin(String idcardvalidbegin) {
        this.idcardvalidbegin = idcardvalidbegin;
    }

    public String getIdcardvalidend() {
        return idcardvalidend;
    }

    public void setIdcardvalidend(String idcardvalidend) {
        this.idcardvalidend = idcardvalidend;
    }

    public String getCreditvalid() {
        return creditvalid;
    }

    public void setCreditvalid(String creditvalid) {
        this.creditvalid = creditvalid;
    }

    public String getAuditflag() {
        return auditflag;
    }

    public void setAuditflag(String auditflag) {
        this.auditflag = auditflag;
    }

    public String getPerflag() {
        return perflag;
    }

    public void setPerflag(String perflag) {
        this.perflag = perflag;
    }

    public String getShopstatus() {
        return shopstatus;
    }

    public void setShopstatus(String shopstatus) {
        this.shopstatus = shopstatus;
    }

    public String getLastresetpassword() {
        return lastresetpassword;
    }

    public void setLastresetpassword(String lastresetpassword) {
        this.lastresetpassword = lastresetpassword;
    }

    public String getFeatureflag() {
        return featureflag;
    }

    public void setFeatureflag(String featureflag) {
        this.featureflag = featureflag;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getSignflag() {
        return signflag;
    }

    public void setSignflag(String signflag) {
        this.signflag = signflag;
    }

    public String getSignreason() {
        return signreason;
    }

    public void setSignreason(String signreason) {
        this.signreason = signreason;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIssuedepartment() {
        return issuedepartment;
    }

    public void setIssuedepartment(String issuedepartment) {
        this.issuedepartment = issuedepartment;
    }

    public String getIsgrab() {
        return isgrab;
    }

    public void setIsgrab(String isgrab) {
        this.isgrab = isgrab;
    }

    public String getIfnewuser() {
        return ifnewuser;
    }

    public void setIfnewuser(String ifnewuser) {
        this.ifnewuser = ifnewuser;
    }

    public String getIsFans() {
        return isFans;
    }

    public void setIsFans(String isFans) {
        this.isFans = isFans;
    }

    public String getRtbCustomerid() {
        return rtbCustomerid;
    }

    public void setRtbCustomerid(String rtbCustomerid) {
        this.rtbCustomerid = rtbCustomerid;
    }
    
}

