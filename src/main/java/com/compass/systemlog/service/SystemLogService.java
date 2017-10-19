package com.compass.systemlog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.systemlog.model.OperateTypeBean;
import com.compass.systemlog.model.SystemLogBean;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;

public class SystemLogService extends AbstractService {

    /**
     * 添加系统日志
     * 
     * @param map
     * 
     * @return
     */
    public int addLog(String ipAddress, String operateAgency, String operateId, String operateName, String operateType, String operateDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取当前时间CommonDate.getDate()
        map.put("operateTime", CommonDate.getDate());
        // 获取日志编号
        map.put("logId", String.valueOf(getLogId()));
        map.put("ipAddress", ipAddress);

        if (null != operateAgency) {
            map.put("operateAgency", operateAgency);
        } else {
            map.put("operateAgency", null);
        }

        if (null != operateId) {
            map.put("operateId", operateId);
        } else {
            map.put("operateId", null);
        }

        map.put("operateName", operateName);

        if (null != operateType) {
            map.put("operateType", operateType);
        } else {
            map.put("operateType", null);
        }
        if (null != operateDetail) {
            map.put("operateDetail", operateDetail);
        } else {
            map.put("operateDetail", null);
        }
        return dao.insert("systemlog.addLog", map);
    }

    /**
     * 获取系统日志序列号
     */
    public Integer getLogId() {
        return (Integer) dao.queryForObject("SEQUENCE.getLogId");
    }

    /**
     * 获取系统日志总条数
     * 
     * @param yearmonth
     * @param agencyId
     * @param agencySelect
     * @return
     */
    public int getSystemLogCount(String yearmonthdatestart, String yearmonthdateend, String agencySelect, String operateMan, String operateName, String operateType) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("yearmonthdatestart", yearmonthdatestart);
        map.put("yearmonthdateend", yearmonthdateend);
        map.put("agencySelect", agencySelect);
        map.put("operateMan", operateMan);
        map.put("operateType", operateType);
        map.put("operateName", operateName);

        return (Integer) dao.queryForObject("systemlog.getSystemLogCount", map);
    }

    /**
     * 获取系统日志明细
     * 
     * @param yearmonth
     * @param agencyId
     * @param agencySelect
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SystemLogBean> getSystemLogdetail(String yearmonthdatestart, String yearmonthdateend, String agencySelect, String operateMan, String operateName, String operateType, int start, int end) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("yearmonthdatestart", yearmonthdatestart);
        map.put("yearmonthdateend", yearmonthdateend);
        map.put("agencySelect", agencySelect);
        map.put("operateMan", operateMan);
        map.put("operateName", operateName);
        map.put("operateType", operateType);

        map.put("start", start);
        map.put("end", end);

        return dao.queryForList("systemlog.getSystemLogDetail", map);
    }

    /**
     * 获取所有的操作类型
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<OperateTypeBean> getOperateType() {

        return dao.queryForList("systemlog.getOperateType", null);
    }

    /**
     * 
     * 【方法名】 :查询登陆日志表对象. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年11月30日 下午1:40:14 .<br/>
     * 【参数】： .<br/>
     * 
     * @param userId
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    public SystemLogBean getloginPrompt(String operateMan) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operateMan", operateMan);
        return (SystemLogBean) dao.queryForObject("systemlog.getloginPrompt", map);
    }

}
