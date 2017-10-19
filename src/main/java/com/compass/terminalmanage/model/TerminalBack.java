/**
 *  <pre>	
 *  Project Name:RtbPlatform .</br>
 *  File: TerminalBack.java .</br>
 *  Package Name:com.compass.terminalmanage.model .</br>
 *  Date      Author       Changes .</br>
 *  2017年5月16日   zhangjianbao     Create  .</br>
 *  Description: .</br>
 *  Copyright 2014-2015 YINGXIANG FINANCE Services Co.,Ltd. All rights reserved..</br>
 *  <pre>	
 */
package com.compass.terminalmanage.model;

/**
 * <pre>
 * 【类型】: TerminalBack <br/> 
 * 【时间】：2017年5月16日 下午6:49:37 <br/> 
 * 【作者】：zhangjianbao <br/>
 * </pre>
 */
public class TerminalBack {

    /**
     * 终端编号集合
     */
    private String terminalIds;
    /**
     * 开始终端编号
     */
    private String beginTerminalId;
    /**
     * 所在机构编号
     */
    private String agencyId;
    /**
     * 原机构编码
     */
    private String oldAgencyId;
    /**
     * 终端类型
     */
    private String terminalTypeId;
    /**
     * 终端描述
     */
    private String terminalDesc;
    /**
     * 创建人编号
     */
    private String createId;
    /**
     * 创建时间
     */
    private String createDt;
    /**
     * 审核人编号
     */
    private String checkId;
    /**
     * 审核时间
     */
    private String checkDt;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 数据状态 0--回拨申请 , 1--回拨审核通过 2--回拨审核拒绝
     */
    private String status;
    /**
     * 结束终端编号
     */
    private String endTerminaId;
    /**
     * 审核理由
     */
    private String reason;
    /**
     * 终端类型名称
     */
    private String terminalTypeName;
    /**
     * 机构名称
     */
    private String agencyName;
    /**
     * 原机构名称
     */
    private String oldAgencyName;
    /**
     * 创建起始时间
     */
    private String createDateStart;
    /**
     * 创建结束时间
     */
    private String createDateEnd;
    /**
     * 审核开始时间
     */
    private String checkDateStart;
    /**
     * 审核结束时间
     */
    private String checkDateEnd;
    /**
     * 数据状态的值
     */
    private String statusDesc;
    /**
     * 回拨理由
     */
    private String backReason;
    /**
     * 终端编号
     */
    private String terminalCode;
    /**
     * 终端状态
     */
    private String terminalStatus;

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:15:24 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalIds() {
        return terminalIds;
    }

    /**
     * 
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:15:35 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalIds
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalIds(String terminalIds) {
        this.terminalIds = terminalIds;
    }

    /**
     * 【方法名】 : getBeginTerminalId. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:55:54 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getBeginTerminalId() {
        return beginTerminalId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:11 .<br/>
     * 【参数】： .<br/>
     * 
     * @param beginTerminalId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setBeginTerminalId(String beginTerminalId) {
        this.beginTerminalId = beginTerminalId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:20 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getAgencyId() {
        return agencyId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:27 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:34 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getOldAgencyId() {
        return oldAgencyId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:41 .<br/>
     * 【参数】： .<br/>
     * 
     * @param oldAgencyId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setOldAgencyId(String oldAgencyId) {
        this.oldAgencyId = oldAgencyId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:48 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalTypeId() {
        return terminalTypeId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:56:55 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalTypeId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalTypeId(String terminalTypeId) {
        this.terminalTypeId = terminalTypeId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalDesc() {
        return terminalDesc;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:08 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalDesc
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalDesc(String terminalDesc) {
        this.terminalDesc = terminalDesc;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:15 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:21 .<br/>
     * 【参数】： .<br/>
     * 
     * @param createId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:31 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCreateDt() {
        return createDt;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:37 .<br/>
     * 【参数】： .<br/>
     * 
     * @param createDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:43 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCheckId() {
        return checkId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:48 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:57:55 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCheckDt() {
        return checkDt;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCheckDt(String checkDt) {
        this.checkDt = checkDt;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public String getStatus() {
        return status;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public String getEndTerminaId() {
        return endTerminaId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setEndTerminaId(String endTerminaId) {
        this.endTerminaId = endTerminaId;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public String getReason() {
        return reason;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:58:01 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDt
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午10:59:14 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalTypeName() {
        return terminalTypeName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午10:59:26 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalTypeName
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalTypeName(String terminalTypeName) {
        this.terminalTypeName = terminalTypeName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:11:22 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getAgencyName() {
        return agencyName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:11:30 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyName
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:14:58 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getOldAgencyName() {
        return oldAgencyName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 上午11:15:05 .<br/>
     * 【参数】： .<br/>
     * 
     * @param oldAgencyName
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setOldAgencyName(String oldAgencyName) {
        this.oldAgencyName = oldAgencyName;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午3:59:48 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCreateDateStart() {
        return createDateStart;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午3:59:55 .<br/>
     * 【参数】： .<br/>
     * 
     * @param createDateStart
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:05 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCreateDateEnd() {
        return createDateEnd;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:12 .<br/>
     * 【参数】： .<br/>
     * 
     * @param createDateEnd
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:18 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCheckDateStart() {
        return checkDateStart;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:28 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDateStart
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCheckDateStart(String checkDateStart) {
        this.checkDateStart = checkDateStart;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:35 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getCheckDateEnd() {
        return checkDateEnd;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月18日 下午4:00:41 .<br/>
     * 【参数】： .<br/>
     * 
     * @param checkDateEnd
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setCheckDateEnd(String checkDateEnd) {
        this.checkDateEnd = checkDateEnd;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 上午11:18:35 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 上午11:18:41 .<br/>
     * 【参数】： .<br/>
     * 
     * @param statusDesc
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    /**
     * 【方法名】 : . <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 下午5:09:52 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getBackReason() {
        return backReason;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 下午5:09:59 .<br/>
     * 【参数】： .<br/>
     * 
     * @param backReason
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setBackReason(String backReason) {
        this.backReason = backReason;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月25日 上午10:41:48 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalCode() {
        return terminalCode;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月25日 上午10:41:54 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalCode
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月25日 上午10:42:00 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String getTerminalStatus() {
        return terminalStatus;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月25日 上午10:42:06 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalStatus
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void setTerminalStatus(String terminalStatus) {
        this.terminalStatus = terminalStatus;
    }

}
