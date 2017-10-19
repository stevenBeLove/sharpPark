package com.compass.terminalmanage.model;
/**
 * 
 * <pre>
 * 【类型】: TerminalChange <br/> 
 * 【作用】: 回拨-下发批次. <br/>  
 * 【时间】：2017年2月8日 上午10:38:17 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class TerminalChange {
    /**
     * 批次号
     */
    private String batchno;

    /**
     * 开始终端号
     */
    private String beginTermid;

    /**
     * 结束终端号
     */
    private String endTermid;

    /**
     * 操作类型  1-下发  2-回拨
     */
    private String changeType;

    /**
     * 原机构编码
     */
    private String oldAgencyid;

    /**
     * 新机构号
     */
    private String newAgencyid;

    /**
     * 申请时间
     */
    private String applyTime;

    /**
     * 申请人
     */
    private String applyUserid;

    /**
     * 处理人
     */
    private String dealUserid;

    /**
     * 处理时间
     */
    private String dealTime;

    /**
     * 批次状态  0--申请   1--对方认可  2--对方驳回      3---处理完毕  4--处理异常
     */
    private String batchState;
    
    /**
     * 终端数量
     */
    private int termNums;
    
    
    public int getTermNums() {
        return termNums;
    }

    public void setTermNums(int termNums) {
        this.termNums = termNums;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getBeginTermid() {
        return beginTermid;
    }

    public void setBeginTermid(String beginTermid) {
        this.beginTermid = beginTermid;
    }

    public String getEndTermid() {
        return endTermid;
    }

    public void setEndTermid(String endTermid) {
        this.endTermid = endTermid;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getOldAgencyid() {
        return oldAgencyid;
    }

    public void setOldAgencyid(String oldAgencyid) {
        this.oldAgencyid = oldAgencyid;
    }

    public String getNewAgencyid() {
        return newAgencyid;
    }

    public void setNewAgencyid(String newAgencyid) {
        this.newAgencyid = newAgencyid;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyUserid() {
        return applyUserid;
    }

    public void setApplyUserid(String applyUserid) {
        this.applyUserid = applyUserid;
    }

    public String getDealUserid() {
        return dealUserid;
    }

    public void setDealUserid(String dealUserid) {
        this.dealUserid = dealUserid;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getBatchState() {
        return batchState;
    }

    public void setBatchState(String batchState) {
        this.batchState = batchState;
    }

    
}