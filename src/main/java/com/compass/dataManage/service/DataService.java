package com.compass.dataManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.compass.dataManage.model.FrunFeeBean;
import com.compass.dataManage.model.ProcLogBean;
import com.compass.splitfee.model.ResetBean;
import com.compass.utils.AbstractService;

public class DataService extends AbstractService {
	private final Log log=LogFactory.getLog(getClass());
	
	/**
	 * 查询存储过程执行记录的数量
	 * @param agencyId 机构编号
	 * @param execDate  执行日期 
	 * @return
	 */
	public Integer getProcLogCount(String agencyId,String execDate){
		Map<String,String> map=new HashMap<String, String>();
		map.put("agencyId", agencyId);
		map.put("execDate", execDate);
		return  (Integer)dao.queryForObject("DATA.getProcLogCount",map);
	}

	/**
	 * 查询存储过程执行记录
	 * @param agencyId 机构编号
	 * @param execDate  执行日期 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProcLogBean> getProcLog(String agencyId,String execDate,int start,int end ){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("execDate", execDate);
		map.put("start", start);
		map.put("end", end);
		return  (List<ProcLogBean>)dao.queryForList("DATA.getProcLog",map);
	}

	/**
	 * 代理商分润计算
	 * @param startDate 开始计算日期
	 * @param endDate   结束计算日期
	 */
	public  void agencyFrCal(String startDate,String endDate){
		Map<String,String> map=new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		dao.queryForObject("DATA.agencyFrCal",map);
	}
	
	/**
	 * 统计重算申请的数量
	 * @param applyAgencyId
	 * @param applyDate
	 * @return
	 */
	public Integer getResetCount(String applyAgencyId,String applyDate,String loginAgencyId){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", applyAgencyId);
		map.put("applyDate", applyDate);
		map.put("loginAgencyId", loginAgencyId);
		return (Integer) dao.queryForObject("DATA.getResetCount", map);
	}
	
	/**
	 * 查询出重算申请的记录
	 * @param applyAgencyId
	 * @param applyDate
	 * @return
	 */
	public List<ResetBean>  getResetAll(String applyAgencyId,String applyDate,String loginAgencyId,int start,int end){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agencyId", applyAgencyId);
		map.put("applyDate", applyDate);
		map.put("start", start);
		map.put("end", end);
		map.put("loginAgencyId", loginAgencyId);
		return dao.queryForList("DATA.getReset", map);
	}
	
	/**
	 * 执行重算申请
	 */
	public void againCalReset(){
		dao.queryForObject("DATA.reset_again");
	}
	
	/**
	 * 导入交易记录
	 * @param execDate 导入日期
	 * @return
	 */
	public String  dealImp(String execDate){
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("execDate", execDate);
		dao.queryForObject("DATA.dealImp", map);
		return (String) map.get("result");
	}
	
	
	public int  getFeeCount(String systemid,String status,String dealtype,String branchid){
	    Map<String,Object> map=new HashMap<String, Object>();
        map.put("systemid", systemid);
        map.put("status", status);
        map.put("dealtype", dealtype);
        map.put("branchid", branchid);
	    return (Integer) dao.queryForObject("DATA.getfeecount", map);
	}
	

	@SuppressWarnings("unchecked")
    public List<FrunFeeBean> getFrunFee(String systemid,String status,String dealtype,String branchid,int start, int end){
	    Map<String,Object> map=new HashMap<String, Object>();
        map.put("systemid", systemid);
        map.put("status", status);
        map.put("dealtype", dealtype);
        map.put("branchid", branchid);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("DATA.getfrunfee", map);
	}
	
	
	
	public int updateFunFee(String fee,String systemid,String status,String prepbranch, String branchid,String feestatus,String dealtype,String shopno ){
	    log.info("费率修改");
        int result=0;
        Map<String,Object> map=new HashMap<String, Object>();
        try {
            map.put("fee", fee);
            map.put("systemid", systemid);
            map.put("status", status);
            map.put("prepbranch", prepbranch);
            System.out.println(branchid=="null");
            map.put("branchid", "null".equals(branchid)?"":branchid);
            map.put("feestatus",feestatus);
            map.put("dealtype", dealtype);
            map.put("shopno", shopno);
            result=(Integer) dao.update("DATA.updateFrunfee", map );
        } catch (Exception e) {
            log.error("终端激活处理异常",e);
            e.printStackTrace();
        }
	    return result;
	}
	
	public  List<FrunFeeBean> getbranch(String systemid){
	    Map<String,Object> map=new HashMap<String, Object>();
        map.put("systemid", systemid);
        return dao.queryForList("DATA.getbranch",map);
	}
	
	
	/**
	 * 一级代理商分润计算
	 * @param startDate 开始计算日期
	 * @param endDate   结束计算日期
	 */
	public  String agencyFristCal(String startDate,String endDate,String prepbranchid,String  branchid){
		log.info("一级代理商分润计算,时间："+startDate+"-"+endDate);
		Map<String,String> map=new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("prepbranchid", prepbranchid);
		map.put("branchid", branchid);
		String result=null;
		try {
			dao.queryForObject("DATA.firstCal", map);
			result=(String) map.get("result");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("一级代理商分润计算异常,时间："+startDate+"-"+endDate,e);
		} 
		return result;
	}

	/**
	 * 终端 激活处理理
	 * @return
	 */	
	public int updateTerminalActice(){
		log.info("终端激活处理");
		int result=0;
		try {
			result=(Integer) dao.update("DATA.terminalActive", null);
		} catch (Exception e) {
			log.error("终端激活处理异常",e);
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 获取顶级机构 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResetBean> getPrepBranchid(){
		return dao.queryForList("DATA.getPrepbranchid");
	}
	
 
	/**
	 * 获取品牌机构 
	 * @param prepbranchid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResetBean> getBranchid(String prepbranchid){
		return dao.queryForList("DATA.getBranchid",prepbranchid);
	}
	
}
