package com.compass.terminalmanage.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.common.FrConstants;
import com.compass.constans.InterfaceNameConstans;
import com.compass.terminalmanage.model.TerminalBack;
import com.compass.terminalmanage.model.TerminalChange;
import com.compass.terminalmanage.model.TerminalCountBean;
import com.compass.terminalmanage.model.TerminalManageBean;
import com.compass.terminalmanage.model.TerminalSerilBean;
import com.compass.terminalmanage.model.TerminalViewBean;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.Format;
import com.imobpay.base.services.FrServer;

/**
 * 终端管理
 * 
 * @author gaoyang
 *
 */
public class TerminalManageService extends AbstractService {

    /**
     * Dubbo分润服务
     */
    private FrServer  frServer;
    /**
     * 日志
     */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * 获取终端信息总记录数（包括下发）
     * 
     * @param systemId
     * @return
     */
    public Integer getTerminalManageAllCount(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, String agencyCode, String userAgencyId,
            String parentAgencyid, String upperAgencyid, String startCode, String endCode, String agecyFlag, String agencytopId, String activeDtStart, String activeDtEnd, 
            String bindDtstart, String bindDtend, String isJoincash, String isPay, String havepsam) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("agencyCode", agencyCode);
        map.put("agencytopId", agencytopId);
        map.put("parentAgencyid", parentAgencyid);
        map.put("upperAgencyid", upperAgencyid);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("userAgencyId", userAgencyId);
        map.put("agecyFlag", agecyFlag);
        map.put("activeDtStart", activeDtStart);
        map.put("activeDtEnd", activeDtEnd);
        map.put("bindDtstart", bindDtstart);
        map.put("bindDtend", bindDtend);
        map.put("isJoincash", isJoincash);
        map.put("isPay", isPay);
        map.put("havepsam", havepsam); 
        
        return (Integer) dao.queryForObject("terminalmanage.getTerminalManageAllTbCount", map);
    }

    /**
     * 获取终端审核页面总记录数
     * 
     * @return
     */
    public Integer getTerminalManageCheckCount(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalManagecheckCount", map);
    }

    /**
     * 获取终端批量下发总记录
     * 
     * @param agencyId
     * @param prefix
     * @param startCode
     * @param endCode
     * @param lengthNumber
     * @param systemId
     * @return
     */
    public Integer getTerminalBatchIssuedCount(String systemId, String agencyId,// String
                                                                                // prefix,
            String startCodes, String endCodes
    // ,String lengthNumber,int subLengthNumber
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("systemId", systemId);
        map.put("agencyId", agencyId);
        // map.put("prefix", prefix);
        map.put("startCodes", startCodes);
        map.put("endCodes", endCodes);
        // map.put("lengthNumber",lengthNumber);
        // map.put("subLengthNumber",subLengthNumber);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalBatchIssuedCount", map);
    }

    /**
     * 获取终端回拨页面总记录数
     * 
     * @return
     */
    public Integer getTerminalManageBackCount(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, String terminalCodeStart, String terminalCodeEnd,
            String flagCTP, String agencytreeId, String datestart, String dateend) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("terminalCodeStart", terminalCodeStart);
        map.put("terminalCodeEnd", terminalCodeEnd);
        map.put("flagCTP", flagCTP);
        map.put("agencytreeId", agencytreeId);
        map.put("datestart", datestart);
        map.put("dateend", dateend);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalManagebackCount", map);
    }

    /**
     * 添加终端数据
     * 
     * @param terminalManageBean
     * @return
     */
    public int addTerminalManage(TerminalManageBean terminalManageBean) {

        return dao.insert("terminalmanage.addTerminalManage", terminalManageBean);
    }

    /**
     * 添加终端失败数据
     * 
     * @param terminalManageBean
     * @return
     */
    public int addTerminalManageFail(String terminalFailId, String failTxt, String terminalFailDesc, String createDt) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalFailId", terminalFailId);
        map.put("failTxt", failTxt);
        map.put("terminalFailDesc", terminalFailDesc);
        map.put("createDt", createDt);
        return dao.insert("terminalmanage.addTerminalManageFail", map);
    }

    /**
     * 获取终端数据(审核、下发)
     * 
     * @param terminalCode
     * @param terminalTypeId
     * @param status
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> getTerminalManageTb(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalmanage.getTerminalManageTb", map);
    }

    public Integer getTerminalManageTbCount(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        // map.put("parentAgencyid", parentAgencyid);
        // map.put("upperAgencyid", upperAgencyid);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalManageTbCount", map);
    }

    /**
     * 获取终端审核页面信息
     * 
     * @param terminalCode
     * @param terminalTypeId
     * @param status
     * @param agencyId
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> getTerminalManagecheck(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalmanage.getTerminalManagecheck", map);
    }

    /**
     * 回拔
     * 
     * @param terminalCode
     *            终端编号
     * @param terminalTypeId
     *            终端类型
     * @param status
     *            终端状态
     * @param agencyId
     *            机构编号
     * @param systemId
     *            来源系统
     * @param terminalCodeStart
     *            终端开始编号
     * @param terminalCodeEnd
     *            终端结束编号
     * @param flagCTP
     * @param start
     *            开始行数
     * @param end
     *            结束行数
     * @param agencytreeId
     *            选择机构编号
     * @param datestart
     *            下发开始日期
     * @param dateend
     *            下发结束日期
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> getTerminalManageback(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, String terminalCodeStart,
            String terminalCodeEnd, String flagCTP, int start, int end, String agencytreeId, String datestart, String dateend) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("terminalCodeStart", terminalCodeStart);
        map.put("terminalCodeEnd", terminalCodeEnd);
        map.put("flagCTP", flagCTP);
        map.put("start", start);
        map.put("end", end);
        map.put("agencytreeId", agencytreeId);
        map.put("datestart", datestart);
        map.put("dateend", dateend);
        return dao.queryForList("terminalmanage.getTerminalManageback", map);
    }

    /**
     * 获取终端数据
     * 
     * @param terminalCode
     * @param terminalTypeId
     * @param status
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> getTerminalManageAllTb(String terminalCode, String terminalTypeId, String status, String agencyId, String systemId, String agencyCode, String userAgencyId,
            int start, int end, String parentAgencyid, String uppweAgencyid, String startCode, String endCode, String agecyFlag, String agencytopId, String activeDtStart, String activeDtEnd,
            String bindDtstart, String bindDtend,String isJoincash, String isPay, String havepsam) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalTypeId", terminalTypeId);
        map.put("start", start);
        map.put("end", end);
        map.put("status", status);
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("agencyCode", agencyCode);
        map.put("agencytopId", agencytopId);
        map.put("parentAgencyid", parentAgencyid);
        map.put("uppweAgencyid", uppweAgencyid);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("agecyFlag", agecyFlag);
        map.put("userAgencyId", userAgencyId);
        map.put("activeDtStart", activeDtStart);
        map.put("activeDtEnd", activeDtEnd);
        map.put("bindDtstart", bindDtstart);
        map.put("bindDtend", bindDtend);
        map.put("isJoincash", isJoincash);
        map.put("isPay", isPay);
        map.put("havepsam", havepsam);
        return dao.queryForList("terminalmanage.getTerminalManageAllTb", map);
    }

    /**
     * 修改终端状态
     * 
     * @param terminalCode
     * @param status
     * @param agencyId
     * @return
     */
    public int updateTerminalStatus(String status, String terminalCodes, String agencyId) {
        log.info("修改终端状态:" + terminalCodes);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCodes);
        map.put("agencyId", agencyId);
        map.put("status", status);
        int result0 = 0;
        try {
            result0 = dao.update("terminalmanage.updateTerminalStatus", map);
        } catch (Exception e) {
            log.error("修改终端状态异常:" + terminalCodes, e);
            e.printStackTrace();
        }
        return result0;
    }

    /**
     * 终端审核通过
     * 
     * @param terminalCodes
     * @param status
     * @param agencyId
     * @return
     */
    public int checkTerminal(String terminalCodes, String agencyId, String parentAgencyId, boolean flag) {
        log.info("终端回拔:" + terminalCodes);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCodes);
        map.put("agencyId", agencyId);
        map.put("parentAgencyId", parentAgencyId);
        map.put("date", new SimpleDateFormat("yyyyMM").format(new Date()));
        int result0 = 0;
        int result1 = 1;
        try {
            result0 = dao.update("terminalmanage.checkTerminalpass", map);
            if (flag) {
                result1 = dao.update("terminalmanage.updatedealTerminal", map);
            }
        } catch (Exception e) {
            log.error("终端回拔:" + terminalCodes, e);
            e.printStackTrace();

        }
        return result0 * result1;
    }

    /**
     * 终端审核失败
     * 
     * @param terminalCodes
     * @param status
     * @param agencyId
     * @return
     */
    public int checkTerminalfail(String terminalCodes) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCodes);
        return dao.update("terminalmanage.checkTerminalfail", map);
    }

    /**
     * 终端下发
     * 
     * @param agencyId
     * @param terminalCode
     * @return
     */
    public int updateTerminalExamine(String agencyId, String merchantCodes, String terminalCode, boolean flag) {
        log.info("终端下发" + terminalCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("merchantCodes", merchantCodes);
        map.put("terminalCode", terminalCode);
        map.put("date", new SimpleDateFormat("yyyyMM").format(new Date()));
        int result1 = 0;
        int result2 = 1;
        try {
            result1 = dao.update("terminalmanage.updateTerminalExamine", map);
            if (flag) {
                result2 = dao.update("terminalmanage.updatedealTerminal", map);
            }
        } catch (Exception e) {
            log.error("终端下发异常" + terminalCode, e);
            e.printStackTrace();
        }
        return result1;
    }

    /**
     * 终端批量下发
     * 
     * @param agencyId
     * @param prefix
     * @param startCode
     * @param endCode
     * @param lengthNumber
     * @return
     */
    public int updateTerminalBatchIssued(String agencyId, String systemId, String parentAgencyId, String startCode, String endCode, String merchantCode, boolean flag) {
        log.info("终端批量下发");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("parentAgencyId", parentAgencyId);
        // map.put("prefix", prefix);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("systemId", systemId);
        // map.put("lengthNumber", lengthNumber);
        // map.put("subLengthNumber", subLengthNumber);
        map.put("merchantCode", merchantCode);
        map.put("date", new SimpleDateFormat("yyyyMM").format(new Date()));

        int result = 0;
        try {
            if (flag) {
                dao.update("terminalmanage.updateDealTerminalBatchIssued", map);
            }
            result = dao.update("terminalmanage.updateTerminalBatchIssued", map);

        } catch (Exception e) {
            log.error("终端批量下发", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取终端下发信息（批量下发）
     * 
     * @param agencyId
     * @param prefix
     * @param startCode
     * @param endCode
     * @param lengthNumber
     * @param systemId
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> getTerminalBatchIssuedTb(String systemId, String agencyId,// String
                                                                                              // prefix,
            String startCodes, String endCodes// ,String lengthNumber,int
                                              // subLengthNumber
            , int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("systemId", systemId);
        map.put("agencyId", agencyId);
        // map.put("prefix", prefix);
        map.put("startCodes", startCodes);
        map.put("endCodes", endCodes);
        // map.put("lengthNumber",lengthNumber);
        // map.put("subLengthNumber",subLengthNumber);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalmanage.getTerminalBatchIssuedTb", map);
    }

    /**
     * 批量插入终端信息
     * 
     * @param beanList
     * @return
     */
    public int addTeminalseril(List<TerminalSerilBean> beanList) {
        return dao.insert("terminalmanage.addserilTeminal", beanList);
    }

    /**
     * 激活终端
     * 
     * @param teminalIds
     * @return
     */
    public int teminalActivating(String createDt) {
        return dao.update("terminalmanage.teminalActivating", createDt);
    }

    /**
     * 获取终端失败信息序列号
     * 
     * @return
     */
    public Integer getTerminalFailId() {
        return (Integer) dao.queryForObject("SEQUENCE.getTerminalFailId");
    }

    /**
     * 获取要改状态的终端
     * 
     * @param terminalcode
     * @param status
     * @return
     */
    public List<TerminalManageBean> getTerminalStatus(String terminalcode, String agencyId, String flag1, String flag2, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalcode", terminalcode);
        map.put("agencyId", agencyId);
        map.put("flag1", flag1);
        map.put("flag2", flag2);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalmanage.getTerminalstatus", map);
    }

    /**
     * 获得总条数
     * 
     * @param terminalcode
     * @param agencyId
     * @param flag1
     * @param flag2
     * @param start
     * @param end
     * @return
     */
    public Integer getTerminalStatusCount(String terminalcode, String agencyId, String flag1, String flag2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalcode", terminalcode);
        map.put("agencyId", agencyId);
        map.put("flag1", flag1);
        map.put("flag2", flag2);
        Integer count = (Integer) dao.queryForObject("terminalmanage.getTerminalstatusCount", map);
        return count;
    }

    /**
     * 更改终端状态
     * 
     * @param status
     * @param terminalCode
     * @return
     */
    public int updateTerminalStatus(String status, String terminalCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("terminalCode", terminalCode);
        return dao.update("terminalmanage.updateTerminalStatus1", map);
    }

    /**
     * 批量插入终端信息
     * 
     * @param beanList
     * @return
     */
    public int addTeminalserilAll(String agencyId, String startCode, String endCode, String oldaAgencyId, String remark, String userId, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("oldaAgencyId", oldaAgencyId);
        map.put("remark", remark);
        map.put("userId", userId);
        map.put("systemId", systemId);
        map.put("CREATEDT", CommonDate.getDate());
        return dao.insert("terminalmanage.addserilTeminalAll", map);
    }

    public TerminalCountBean getTerminalCount(String agencyid, String datestart, String dateend) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyid", agencyid);
        map.put("datestart", datestart);
        map.put("dateend", dateend);
        return (TerminalCountBean) dao.queryForObject("terminalmanage.getAgencyTerminaicode", map);
    }

    /**
     * 查询终端下发回拔记录数量
     * 
     * @param agencyId
     * @param selAgencyId
     * @param agencyIdOld
     * @param selAgencyIdOld
     * @param startCode
     * @param endCode
     * @param datestart
     * @param dateend
     * @param opt
     * @param userAgencyId
     * @return
     */
    public Integer getTerminalViewCount(String agencyId, String selAgencyId, String agencyIdOld, String selAgencyIdOld, String startCode, String endCode, String datestart, String dateend, String opt,
            String userAgencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("selAgencyId", selAgencyId);
        map.put("agencyIdOld", agencyIdOld);
        map.put("selAgencyIdOld", selAgencyIdOld);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("datestart", datestart);
        map.put("dateend", dateend);
        map.put("opt", opt);
        map.put("userAgencyId", userAgencyId);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalViewCount", map);
    }

    /**
     * 查询终端下发回拔记录
     * 
     * @param agencyId
     * @param selAgencyId
     * @param agencyIdOld
     * @param selAgencyIdOld
     * @param startCode
     * @param endCode
     * @param datestart
     * @param dateend
     * @param opt
     * @param userAgencyId
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TerminalViewBean> getTerminalView(String agencyId, String selAgencyId, String agencyIdOld, String selAgencyIdOld, String startCode, String endCode, String datestart, String dateend,
            String opt, String userAgencyId, Integer start, Integer end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("selAgencyId", selAgencyId);
        map.put("agencyIdOld", agencyIdOld);
        map.put("selAgencyIdOld", selAgencyIdOld);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("datestart", datestart);
        map.put("dateend", dateend);
        map.put("opt", opt);
        map.put("userAgencyId", userAgencyId);
        map.put("start", start);
        map.put("end", end);
        return (List<TerminalViewBean>) dao.queryForList("terminalmanage.getTerminalView", map);
    }

    /**
     * 查询核心终端数量
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public Integer getTerminalCoreCount(String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalCoreCount", map);
    }

    /**
     * 查询核心终端信息
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public List<TerminalManageBean> getTerminalCore(String startCode, String endCode, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("start", start);
        map.put("end", end);
        return (List<TerminalManageBean>) dao.queryForList("terminalmanage.getTerminalCore", map);
    }

    /**
     * 查询核心taCard终端信息
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public List<TerminalManageBean> getTaCardTerminalCore(String startCode, String endCode, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("start", start);
        map.put("end", end);
        return (List<TerminalManageBean>) dao.queryForList("terminalmanage.getTaCardTerminalCore", map);
    }

    /**
     * 查询核心taCard终端数量
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public Integer getTaCardTerminalCoreCount(String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        return (Integer) dao.queryForObject("terminalmanage.getTaCardTerminalCoreCount", map);
    }

    /**
     * 查询分润终端数量
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public Integer getTerminalFrunCount(String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        return (Integer) dao.queryForObject("terminalmanage.getTerminalFrunCount", map);
    }

    /**
     * 查询分润终端信息
     * 
     * @param startCode
     * @param endCode
     * @return
     */
    public List<TerminalManageBean> getTerminalFrun(String startCode, String endCode, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("start", start);
        map.put("end", end);
        return (List<TerminalManageBean>) dao.queryForList("terminalmanage.getTerminalFrun", map);
    }

    public int addTerminalManage(String startCode, String endCode, String systemSource, String terminalTypeId, String createDt, String agencyId, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("systemSource", systemSource);
        map.put("terminalTypeId", terminalTypeId);
        map.put("createDt", createDt);
        map.put("agencyId", agencyId);
        map.put("userId", userId);
        return dao.insert("terminalmanage.addTerminalImport", map);
    }

    public int addTaCardTerminalManage(String startCode, String endCode, String systemSource, String terminalTypeId, String createDt, String agencyId, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("systemSource", systemSource);
        map.put("terminalTypeId", terminalTypeId);
        map.put("createDt", createDt);
        map.put("agencyId", agencyId);
        map.put("userId", userId);
        return dao.insert("terminalmanage.addTaCardTerminalImport", map);
    }

    /**
     * 按终端编号串查询有交易的终端
     * 
     * @param agencyId
     *            交易机构编号
     * @param terminalCode
     *            终端编号串
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> queryDealTermianl(String agencyId, String terminalCode, String flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        log.info("按终端编号串查询有交易的终端：" + terminalCode);
        map.put("agencyId", agencyId);
        map.put("terminalCode", terminalCode);
        map.put("up", "up".equals(flag) ? flag : null);
        map.put("down", "down".equals(flag) ? flag : null);
        map.put("date", new SimpleDateFormat("yyyyMM").format(new Date()));
        List<TerminalManageBean> list = null;
        try {
            list = dao.queryForList("terminalmanage.queryDealTerminal", map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按终端编号串查询有交易的终端异常", e);
        }
        return list;
    }

    /**
     * 
     * @param agencyId
     * @param terminalCode
     * @param flag
     * @return
     */
    public List<TerminalManageBean> queryDealTermianl(String agencyId, String startTerminalCode, String endTerminalCode, String flag, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        log.info("按终端编号串查询有交易的终端：" + startTerminalCode + "-" + endTerminalCode);
        map.put("agencyId", agencyId);
        map.put("startTerminalCode", startTerminalCode);
        map.put("endTerminalCode", endTerminalCode);
        map.put("systemId", systemId);
        map.put("up", "up".equals(flag) ? flag : null);
        map.put("down", "down".equals(flag) ? flag : null);
        map.put("date", new SimpleDateFormat("yyyyMM").format(new Date()));
        List<TerminalManageBean> list = null;
        try {
            list = dao.queryForList("terminalmanage.queryDealTerminals", map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按终端编号串查询有交易的终端异常", e);
        }
        return list;
    }

    public List<String> queryTerminalCode(String agencyId, String startTerminal, String endTerminal, String systemId) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("systemId", systemId);
        map.put("agencyId", agencyId);
        map.put("startCodes", startTerminal);
        map.put("endCodes", endTerminal);
        List<String> listForCode = null;
        listForCode = dao.queryForList("terminalmanage.queryTerminalCode", map);
        return listForCode;
    }

    public List<TerminalCountBean> selectTerminal(String string) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("string", string);
        map.put("guocheng", "SELECTTERMINAL_COUNT(?,?)");
        List<TerminalCountBean> listForCode = null;
        dao.queryForList("fee.SELECTTERMINAL_COUNT", map);
        listForCode = (List<TerminalCountBean>) map.get("result");

        return listForCode;
    }

    /**
     * 方法名： getTerminalCount.<br/>
     * 方法作用:得到机构总数.<br/>
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月8日.<br/>
     * 创建时间：下午3:19:23.<br/>
     * 参数者异常：@return .<br/>
     * 返回值： Integer.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public Integer getTerminalCount() {
        Integer countInteger = (Integer) dao.queryForObject("terminalmanage.queryAgencyCount");
        return countInteger;
    }

    /**
     * 方法名： getTerminalInfo.<br/>
     * 方法作用:根据分页得到相应的机构号.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月8日.<br/>
     * 创建时间：下午3:19:27.<br/>
     * 参数者异常：@param startNum 参数者异常：@param endNum 参数者异常：@return .<br/>
     * 返回值：List<String>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public List<String> getTerminalInfo(int startNum, int endNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startNum", startNum);
        map.put("endNum", endNum);
        List<String> stringList = dao.queryForList("terminalmanage.queryAgencyList", map);
        return stringList;
    }

    /**
     * 
     * 方法名： getTerminalList.<br/>
     * 方法作用:根据机构号得到相应的终端信息..<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:09:03.<br/>
     * 参数者异常：@param string 参数者异常：@param startDate 参数者异常：@param endDate
     * 参数者异常：@return .<br/>
     * 返回值： List<TerminalCountBean>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public List<TerminalCountBean> getTerminalList(String string, String startDate, String endDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", string);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        List<TerminalCountBean> stringList = dao.queryForList("terminalmanage.getTerminalList", map);
        return stringList;
    }

    /**
     * 【方法名】 : (添加回拨-下发批次). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月8日 上午10:51:31 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalChange
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public int insertTerminalChange(TerminalChange terminalChange) {
        return dao.insert("terminalChange.insertTerminalChange", terminalChange);
    }

    /**
     * 
     * 方法名： queryPayterminalCount(终端绑定查询).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月5日.<br/>
     * 创建时间：下午12:03:16.<br/>
     * 参数者异常：@param agencyId 参数者异常：@param systemId 参数者异常：@param startCode
     * 参数者异常：@param endCode 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public int queryPayterminalCount(String agencyId, String systemId, String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        return (Integer) dao.queryForObject("terminalmanage.queryPayterminalCount", map);
    }

    /**
     * 
     * 方法名： queryMinPayterminal(查询指定条件第一台绑定的终端).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月5日.<br/>
     * 创建时间：下午1:37:39.<br/>
     * 参数者异常：@param agencyId 参数者异常：@param systemId 参数者异常：@param startCode
     * 参数者异常：@param endCode 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public String queryMinPayterminal(String agencyId, String systemId, String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        return dao.queryForObject("terminalmanage.queryMinPayterminal", map).toString();
    }

    /**
     * 【方法名】 : (下发终端发送短信). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月5日 上午11:52:59 .<br/>
     * 【参数】： .<br/>
     * 
     * @param mobileNo
     *            手机号码
     * @param companyName
     *            公司名称
     * @param contactsname
     *            联系人姓名
     * @param count
     *            条数
     * @param sendType
     *            发送类型
     * @return JSONObject
     * @throws QTException .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public JSONObject sendTerminalSMS(String mobileNo, String companyName, String contactsname, String count, String sendType) throws QTException {
        JSONObject smsJson = new JSONObject();
        smsJson.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.JYM_SENDSMS);
        String datetime = CommonDate.currentTimeWithFormat(CommonDate.YYYYMMDDHHMMSS);
        String[] mobilearray = new String[] { mobileNo };
        String[] customerarray = new String[] { "" };
        if (InterfaceNameConstans.VALUE_1.equals(sendType)) {
            smsJson.put(InterfaceNameConstans.SMS_PARAM, InterfaceNameConstans.PAYFRSERVER_001);
            smsJson.put(InterfaceNameConstans.UPCONTACTSNAME, contactsname); // 上级
            smsJson.put(InterfaceNameConstans.DOWNCOMPAYNNAME, companyName); // 下级

        } else if (InterfaceNameConstans.VALUE_2.equals(sendType)) {
            smsJson.put(InterfaceNameConstans.SMS_PARAM, InterfaceNameConstans.PAYFRSERVER_002);
            smsJson.put(InterfaceNameConstans.DOWNCONTACTSNAME, contactsname); // 下级代理商名称
            smsJson.put(InterfaceNameConstans.UPCOMPAYNNAME, companyName); // 上级代理商名称
        }

        smsJson.put(InterfaceNameConstans.NOWDATE, datetime);
        smsJson.put(InterfaceNameConstans.BRANCHID_UPPER, InterfaceNameConstans.RTB_BRANCHID);
        smsJson.put(InterfaceNameConstans.APPUSER_UPPER, InterfaceNameConstans.APPUSER_UPPER);
        smsJson.put(InterfaceNameConstans.MOBILENO_UPPER, mobilearray);
        smsJson.put(InterfaceNameConstans.CUSTOMERID_UPPER, customerarray);
        smsJson.put(InterfaceNameConstans.TIMETYPE, InterfaceNameConstans.VALUE_1);
        smsJson.put(InterfaceNameConstans.SOURCECHANNAL, InterfaceNameConstans.PAYFRSERVER);
        smsJson.put(InterfaceNameConstans.COUNT, count);
        frServer.execute(smsJson.toString());
        return smsJson;
    }

    // JSONArray mobArrDown = new JSONArray();
    // mobArrDown.add(monileNo);
    // smsJson.put(InterfaceNameConstans.MOBILENO_UPPER, mobArrDown);
    // smsJson.put(InterfaceNameConstans.CUSTOMERID_UPPER, new JSONArray());
    // smsJson.put(InterfaceNameConstans.TIMETYPE,
    // InterfaceNameConstans.VALUE_1);
    // smsJson.put(InterfaceNameConstans.SOURCECHANNAL,
    // InterfaceNameConstans.FR_SERVER);
    // smsJson.put(InterfaceNameConstans.COMPANY_NAME, compayName);
    // smsJson.put(InterfaceNameConstans.AGENCY_ID, agencyId);

    public void setFrServer(FrServer frServer) {
        this.frServer = frServer;
    }

    /**
     * 
     * 【方法名】 : (来源系统变更查询总记录数). <br/>
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:16:39 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyId
     *            机构号
     * @param systemId
     *            来源系统编号
     * @param startCode
     *            开始编号
     * @param endCode
     *            结束编号
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 简思文 修改描述： .<br/>
     *         <p/>
     */
    public int queryTerminalSystemCount(String agencyId, String systemId, String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("terminalStatus", ConstantUtils.TERMINALNONACTIVATEDSTATUS);
        return (Integer) dao.queryForObject("terminalmanage.queryTerminalSystemCount", map);
    }

    /**
     * 
     * 【方法名】 : (来源系统变更查询). <br/>
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:18:26 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyId
     *            机构号
     * @param systemId
     *            来源系统编号
     * @param startCode
     *            开始编号
     * @param endCode
     *            结束编号
     * @param start
     *            分页开始
     * @param end
     *            分页结束
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 简思文 修改描述： .<br/>
     *         <p/>
     */
    @SuppressWarnings("unchecked")
    public List<TerminalManageBean> queryTerminalSystem(String agencyId, String systemId, String startCode, String endCode, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("terminalStatus", ConstantUtils.TERMINALNONACTIVATEDSTATUS);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalmanage.queryTerminalSystem", map);
    }

    /**
     * 
     * 【方法名】 : (终端来源系统变更). <br/>
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:19:17 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyId
     *            机构号
     * @param systemId
     *            来源系统编号
     * @param systemSource
     *            要变更到的来源系统
     * @param startCode
     *            开始编号
     * @param endCode
     *            结束编号
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 简思文 修改描述： .<br/>
     *         <p/>
     */
    public int updateSystemStatus(String agencyId, String systemId, String systemSource, String startCode, String endCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("systemSource", systemSource);
        map.put("startCode", startCode);
        map.put("endCode", endCode);
        map.put("terminalStatus", ConstantUtils.TERMINALNONACTIVATEDSTATUS);
        int result = dao.update("terminalmanage.updateSystemStatus", map);
        return result;
    }

    /**
     * 【方法名】 : (这里用一句话描述这个方法的作用). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月16日 下午6:59:51 .<br/>
     * 【参数】： .<br/>
     * .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     * <p/>
     */
    public void saveTerminalCallBack(String terminalIds, String agencyId, String userId, String oldAgencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalIds", terminalIds);
        map.put("agencyId", agencyId);
        map.put("oldAgencyId", oldAgencyId);
        map.put("createId", userId);
        map.put("createDt", CommonDate.getDate());
        dao.insert("terminalChange.saveTerminalCallBack", map);
    }

    /**
     * 【方法名】 : 获取回拨记录审核总数 <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月17日 下午4:30:59 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public Integer getTerminalBackAllCount(TerminalBack terminalBack) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTerminalId", terminalBack.getBeginTerminalId());
        map.put("endTerminaId", terminalBack.getEndTerminaId());
        map.put("terminalTypeId", terminalBack.getTerminalTypeId());
        map.put("yearmonthdatestart", terminalBack.getCreateDateStart());
        map.put("yearmonthdateend", terminalBack.getCreateDateEnd());
        map.put("checkDateStart", terminalBack.getCheckDateStart());
        map.put("checkDateEnd", terminalBack.getCheckDateEnd());
        map.put("agencyId", terminalBack.getAgencyId());
        return (Integer) dao.queryForObject("terminalChange.getTerminalBackAllCount", map);
    }

    /**
     * 【方法名】 : 获取回拨记录审核列表. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月17日 下午4:43:48 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    @SuppressWarnings("unchecked")
    public List<TerminalBack> getTerminalBackAll(TerminalBack terminalBack, int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTerminalId", terminalBack.getBeginTerminalId());
        map.put("endTerminaId", terminalBack.getEndTerminaId());
        map.put("terminalTypeId", terminalBack.getTerminalTypeId());
        map.put("yearmonthdatestart", terminalBack.getCreateDateStart());
        map.put("yearmonthdateend", terminalBack.getCreateDateEnd());
        map.put("checkDateStart", terminalBack.getCheckDateStart());
        map.put("checkDateEnd", terminalBack.getCheckDateEnd());
        map.put("agencyId", terminalBack.getAgencyId());
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("terminalChange.getTerminalBackAll", map);
    }

    /**
     * 【方法名】 : 保存回拨审核意见. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月19日 下午3:30:03 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalBack
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */

    public int saveTerminalSuggest(TerminalBack terminalBack, String batchNos, String beginTerminalIds, String endTerminalIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", terminalBack.getStatus());
        map.put("batchNos", batchNos);
        map.put("checkDt", terminalBack.getCheckDt());
        map.put("checkId", terminalBack.getCheckId());
        map.put("beginTerminalIds", beginTerminalIds);
        map.put("endTerminalIds", endTerminalIds);
        map.put("oldAgencyId", terminalBack.getOldAgencyId());
        map.put("agencyId", terminalBack.getAgencyId());
        dao.update("terminalChange.saveTerminalSuggest", map);
        // 当审核通过才修改终端的机构号
        Integer countOfSuccess = 0;
        if ("1".equals(terminalBack.getStatus())) {
            countOfSuccess = dao.update("terminalChange.updateTerminalSuggestStatus", map);
        }
        return countOfSuccess;
    }

    /**
     * 【方法名】 : 批量保存终端回拨. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月19日 下午3:31:10 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalCode
     * @param terminalCodeEnd
     * @param agencyId
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void saveTerminalBackMore(String terminalCode, String terminalCodeEnd, String agencyId, String oldAgencyId, String userId, String terminaltypeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalCode", terminalCode);
        map.put("terminalCodeEnd", terminalCodeEnd);
        map.put("agencyId", agencyId);
        map.put("oldAgencyId", oldAgencyId);
        map.put("createId", userId);
        map.put("terminaltypeId", terminaltypeId);
        map.put("createDt", CommonDate.getDate());
        dao.insert("terminalChange.saveTerminalBackMore", map);
    }

    /**
     * 【方法名】 : 获取为进行审核的终端数量. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月22日 下午1:53:25 .<br/>
     * 【参数】： .<br/>
     * 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public Integer getNoCheckTerminalBackCount(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (Integer) dao.queryForObject("terminalChange.getNoCheckTerminalBackCount", map);
    }

    /**
     * 【方法名】 : (分页条数). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年2月28日 下午2:12:08 .<br/>
     * 【参数】： .<br/>
     * 
     * @param map
     *            Map<String, Object>
     * @return int
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述： .<br/>
     *         <p/>
     */
    public Integer selectPageCountByParam(Map<String, Object> map) throws Exception {
        return (Integer) dao.queryForObject("terminalChange.selectPageCountByParam", map);
    }

    /**
     * 【方法名】 : (分页查询). <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年2月28日 下午2:10:22 .<br/>
     * 【参数】： .<br/>
     * 
     * @param map
     *            Map<String, Object>
     * @return List<TerminalChange>
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: zhangjianbao 修改描述： .<br/>
     *         <p/>
     */
    @SuppressWarnings("unchecked")
    public List<TerminalBack> selectPageByParam(Map<String, Object> map) throws Exception {
        return dao.queryForList("terminalChange.selectPageByParam", map);
    }

    /**
     * 【方法名】 : 审核通过终端插入到终端记录表. <br/>
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年5月25日 下午6:08:36 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalBack
     * @param batchNos
     * @param beginTerminalIds
     *            .<br/>
     *            <p>
     *            修改记录.<br/>
     *            修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     *            <p/>
     */
    public void addTeminalCheck(TerminalBack terminalBack, String batchNos, String beginTerminalIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchNos", batchNos);
        map.put("beginTerminalIds", beginTerminalIds);
        map.put("endTerminalIds", terminalBack.getEndTerminaId());
        map.put("agencyId", terminalBack.getAgencyId());
        map.put("oldAgencyId", terminalBack.getOldAgencyId());
        map.put("checkId", terminalBack.getCheckId());
        map.put("checkDt", terminalBack.getCheckDt());
        dao.insert("terminalChange.addTeminalCheck", map);
    }

    /*
     * 
     * 【方法名】 : 查询用户输入的终端是否存在. <br/> 【作者】: 张翔宇 .<br/> 【时间】： 2017年5月25日 上午9:20:59
     * .<br/> 【参数】： .<br/>
     * 
     * @param terminalCodeStart 开始终端
     * 
     * @param terminalCodeFinish 结束终端
     * 
     * @param agencyId 机构号
     * 
     * @return .<br/> <p> 修改记录.<br/> 修改人: 张翔宇 修改描述：创建新新件 .<br/> <p/>
     */
    public int checkTerminalExist(String terminalCodeStart, String terminalCodeFinish, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalcode", terminalCodeStart);
        map.put("agencyId", agencyId);
        log.debug("开始终端号：" + terminalCodeStart + ",数据库查询开始");
        return (Integer) dao.queryForObject("terminalmanage.checkTerminalExist", map);
//        map.put("terminalcode", terminalCodeFinish);
//        log.debug("结束终端号：" + terminalCodeFinish + ",数据库查询开始");
////        List listEnd = dao.queryForList("terminalmanage.checkTerminalExist", map);
//        Map<String, Object> count = new HashMap<String, Object>();
//        count.put("countStart", listStart.get(0));
////        count.put("countEnd", listEnd.get(0));
//        return count;
    }

    /**
     * 
     * 【方法名】 : (查询单个终端是否存在). <br/>
     * 【作者】: 张翔宇 .<br/>
     * 【时间】： 2017年5月25日 下午2:22:51 .<br/>
     * 【参数】： .<br/>
     * 
     * @param terminalCode
     *            终端标号
     * @param agencyId
     *            机构号
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张翔宇 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public int checkTerminalExist(String terminalCode, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalcode", terminalCode);
        map.put("agencyId", agencyId);
        log.debug("终端号：" + terminalCode + ",数据库查询开始");
        return (Integer) dao.queryForObject("terminalmanage.checkSingleTerminalExist", map);
    }

    /**
     * 
     * 【方法名】 : (根据appuser查询branchid). <br/>
     * 【作者】: 张翔宇 .<br/>
     * 【时间】： 2017年5月26日 下午6:56:12 .<br/>
     * 【参数】： .<br/>
     * 
     * @param appuser
     *            appuser
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张翔宇 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String selectBranchId(String appuser) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appuser", appuser);
        String branchId = (String) dao.queryForObject("terminalmanage.selectBranchId", map);
        return branchId;
    }

    /**
     * 
     * 【方法名】 : (根据下级机构号查询). <br/>
     * 【作者】: 张翔宇 .<br/>
     * 【时间】： 2017年5月26日 下午6:56:12 .<br/>
     * 【参数】： .<br/>
     * 
     * @param oldAgencyId
     *            下级机构号
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张翔宇 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public Object selectAgency(String oldAgencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agency_id", oldAgencyId);
        return dao.queryForObject("terminalmanage.selectAgency", map);
    }

    /**
     * 
     * 【方法名】 : (发送短信). <br/>
     * 【作者】: 张翔宇 .<br/>
     * 【时间】： 2017年5月26日 下午8:08:22 .<br/>
     * 【参数】： .<br/>
     * 
     * @param phoneNum
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张翔宇 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public String sendMsg(String phoneNum, String oldAgencyId, String onlineChannel, Integer backCount) {
        JSONObject json = new JSONObject();
        String[] mobileNo = { phoneNum };
        String[] customerId = { FrConstants.NULL };
        json.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.JYM_SENDSMS);
        json.put(FrConstants.SMSPARAM, FrConstants.RTBPLATFORM_01);
        json.put(FrConstants.NOWDATE, Format.time());
        json.put(FrConstants.APPUSER, FrConstants.RUITONGBAO);
        json.put(FrConstants.BRANCHID, FrConstants.BRANCHID_NUM);
        json.put(FrConstants.MOBILENO, mobileNo);
        json.put(FrConstants.CUSTOMERID, customerId);
        json.put(FrConstants.TIMETYPE, FrConstants.ONTIME);
        json.put(FrConstants.SOURCECHANNAL, FrConstants.RTBPLATFORM);
        json.put(FrConstants.ONLINECHANNEL, onlineChannel);
        json.put(FrConstants.AGENCYID, oldAgencyId);
        json.put(FrConstants.BACK_COUNT, String.valueOf(backCount));
        String obj = frServer.execute(json.toString());
      
        return obj;
    }
    /**
     * 【方法名】    : 终端回拨台数. <br/> 
     * 【作者】: zhangjianbao .<br/>
     * 【时间】： 2017年6月2日 下午12:10:21 .<br/>
     * 【参数】： .<br/>
     * @param terminalBack
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: zhangjianbao 修改描述：创建新新件 .<br/>
     * <p/>
     */
    public Integer countAllBack(TerminalBack terminalBack) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTerminalId", terminalBack.getBeginTerminalId());
        map.put("endTerminalId", terminalBack.getEndTerminaId());
        map.put("oldAgencyId", terminalBack.getOldAgencyId());
        return (Integer) dao.queryForObject("terminalChange.countAllBack", map);
    }
    
    
    
    /**
     * 
     * 【方法名】    : 终端费率修改短信通知 <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月9日 上午11:06:31 .<br/>
     * 【参数】： .<br/>
     * @param phoneNum
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: 张柯. 修改描述：创建新新件 .<br/>
     * <p/>
     */
    public String sendRateUpdateMsg(String phoneNum,String psamId) {
        
        JSONObject json = new JSONObject();
        String[] mobileNo = { phoneNum };
        String[] customerId = { FrConstants.NULL };
        json.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.JYM_SENDSMS);
        json.put(FrConstants.SMSPARAM, FrConstants.RTBPLATFORM_02);
        json.put(FrConstants.NOWDATE, Format.time());
        json.put(FrConstants.APPUSER, FrConstants.RUITONGBAO);
        json.put(FrConstants.BRANCHID, FrConstants.BRANCHID_NUM);
        json.put(FrConstants.MOBILENO, mobileNo);
        json.put(FrConstants.CUSTOMERID, customerId);
        json.put(FrConstants.PSAMID, psamId);
        json.put(FrConstants.TIMETYPE, FrConstants.ONTIME);
        json.put(FrConstants.SOURCECHANNAL, FrConstants.RTBPLATFORM);
  
        String obj = frServer.execute(json.toString());
       
        return obj;
    }
    
    /***
     * 
     * 【方法名】    : 查询修改终端联系人的手机号. <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月9日 上午11:08:33 .<br/>
     * 【参数】： .<br/>
     * @param oldAgencyId
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: 张柯. 修改描述：创建新新件 .<br/>
     * <p/>
     */
    public Object selectUserId(String psamId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        return dao.queryForObject("terminalmanage.selectUserId", map);
    }
    /**
     * 
     * 【方法名】    : 校验终端号用户 . <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月9日 上午11:13:22 .<br/>
     * 【参数】： .<br/>
     * @param psamId
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: 张柯. 修改描述：创建新新件 .<br/>
     * <p/>
     */
    public int checkUserId(String psamId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        log.debug("终端号：" + psamId + ",数据库查询开始");
        return (Integer) dao.queryForObject("terminalmanage.checkUserId", map);
    }
}
