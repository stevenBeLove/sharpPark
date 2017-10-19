package com.compass.splitfee.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.splitfee.model.SplitFeeBean;
import com.compass.splitfee.model.SplitFeeDealBean;
import com.compass.splitfee.model.SplitFeeDealType;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;

/**
 * 
 * @author wangLong
 * 分润规则设置
 *
 */
public class SplitFeeService extends AbstractService {

	/**
	 * 查看分润计算执行日志
	 * @param agencyId
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeBean> getSplitFeeLog(String agencyId,int start,int end) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("start", start);
		map.put("end", end);
		return dao.queryForList("fee.getSplitFeeLog",map);
	}

	
	/**
	 *  调用分润计算存储过程 
	 * @return
	 */
	public int splitFee(String reportData,String agencyId,String dealTypeId) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>(); 
		map.put("reportData", reportData);
		map.put("agencyId", agencyId);
		map.put("dealTypeId", dealTypeId);
		return dao.execute("fee.splitFee",map);
	}
	
	
	/**
	 * 获取分润计算的数据总个数
	 * @return
	 */
	public Integer getSplitFeeCount(String agencyId){
		return (Integer) dao.queryForObject("fee.getSplitFeeCount",agencyId);
	}

	/**
	 * 根据交易类型获取分润明细
	 * @param agencyId
	 * @param reportData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealType> splitFeeByAgencyDealType(String agencyId,String reportData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("reportData", reportData);
		return dao.queryForList("fee.splitFeeByAgencyDealType",map);
	}
	public Integer splitFeeByAgencyDealTypeCount(String agencyId,String reportData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agencyId", agencyId);
		map.put("reportData", reportData);
		return (Integer) dao.queryForObject("fee.splitFeeByAgencyDealTypeCount",map);
	}

	/**
	 * 分润结果确认
	 * @param bean
	 * @return
	 */
	public int splitFeeAffirm(SplitFeeDealType bean) {
		return dao.insert("fee.splitFeeAffirm", bean);
	}

	/**
	 * 查询当前机构对下级的分润
	 * @param yearmonth
	 * @param systemId 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealType> getChildSplitFee(//String yearmonth,
			String datestart, String dateend,
			String agencyId,String agencyIdf,String dealType, String systemId,Integer start,Integer end) {
		Map<String,Object> map = new HashMap<String, Object>(); 
		//map.put("yearmonth", yearmonth);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("agencyId", agencyId);
		map.put("agencyIdf", agencyIdf);
		map.put("dealType", dealType);
		//map.put("systemId", systemId);
		map.put("start", start);
		map.put("end", end);
		dao.queryForList("fee.getChildSplitFee", map);
		 List<SplitFeeDealType> list= ( List<SplitFeeDealType> ) map.get("result");   
		return list;    
	}
	/**
	 * 查询当前机构对下级的分润的历史数据
	 * @param yearmonth
	 * @param systemId 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealType> getChildHisSplitFee(//String yearmonth,
			String datestart, String dateend,
			String agencyId,String agencyIdf,String dealType, String systemId,Integer start,Integer end) {
		Map<String,Object> map = new HashMap<String, Object>(); 
		//map.put("yearmonth", yearmonth);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("agencyId", agencyId);
		map.put("agencyIdf", agencyIdf);
		map.put("dealType", dealType);
		//map.put("systemId", systemId);
		map.put("start", start);
		map.put("end", end);
		dao.queryForList("fee.getChildHisSplitFee", map);
		 List<SplitFeeDealType> list= ( List<SplitFeeDealType> ) map.get("result");   
		return list;    
	}
	public Integer getChildSplitFeeCount(//String yearmonth,
			String datestart, String dateend,
			String agencyId,String agencyIdf,String dealType, String systemId) {
		Map<String,Object> map = new HashMap<String, Object>(); 
		Integer returnid = null;   
		//map.put("yearmonth", yearmonth);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("agencyId", agencyId);
		map.put("agencyIdf", agencyIdf);
		map.put("dealType", dealType);
		//map.put("systemId", systemId);
		dao.queryForList("fee.getChildSplitFeeCount", map);
		// TODO Auto-generated method stub
		return  returnid = (Integer) map.get("result");
	}
	public Integer getChildHisSplitFeeCount(//String yearmonth,
			String datestart, String dateend,
			String agencyId,String agencyIdf,String dealType, String systemId) {
		Map<String,Object> map = new HashMap<String, Object>(); 
		Integer returnid = null;   
		//map.put("yearmonth", yearmonth);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("agencyId", agencyId);
		map.put("agencyIdf", agencyIdf);
		map.put("dealType", dealType);
		//map.put("systemId", systemId);
		dao.queryForList("fee.getChildHisSplitFeeCount", map);
		// TODO Auto-generated method stub
		return  returnid = (Integer) map.get("result");
	}
	
	/**
	 * 获取分润明细的总记录数
	 * @param agencyId
	 * @param yearmonth
	 * @param dealTypeId
	 * @param serialnumber
	 * @param terminalId
	 * @return
	 */
	public Integer getSplitFeeDetailCount(String agencyId, String yearmonth,
			String dealTypeId,String serialnumber,String terminalId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("yearmonth", yearmonth);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		return (Integer)dao.queryForObject("fee.getSplitFeeDetailCount",map);
	}

	/**
	 * 获取分润明细记录
	 * @param agencyId
	 * @param datestart
	 * @param dateend
	 * @param dealTypeId
	 * @param serialnumber
	 * @param terminalId
	 * @param startValue
	 * @param endValue
	 * @param levelCode
	 * @param loginAgencyId
	 * @param start
	 * @param end
	 * @param agencyOnly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealBean> getSplitFeeDetail(String agencyId,
			 String datestart,String dateend, String dealTypeId, String serialnumber,
			String terminalId,String startValue,String endValue,String levelCode,
			String flag,int start,int end) {	
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", levelCode);
		map.put("flag",flag);
		map.put("start", start);
		map.put("end", end);
		 dao.queryForList("fee.getSplitFeeDetail", map);
		  List<SplitFeeDealBean> list= ( List<SplitFeeDealBean> ) map.get("result");   
		return list;    
	}
	
	
	/**
	 * 根据交易类型和机构查看分润明细
	 * @param yearmonth
	 * @param agencyId
	 * @param dealTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealBean> splitFeeDetail(String yearmonth,
			String agencyId, String dealTypeId,int start,int end) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("yearmonth", yearmonth);
		map.put("dealTypeId", dealTypeId);
		map.put("start", start);
		map.put("end", end);
		dao.queryForList("fee.splitFeeDetail", map);
		  List<SplitFeeDealBean> list= ( List<SplitFeeDealBean> ) map.get("result");   
		return list;    
	}

	/**
	 * 
	 * @param agencyId
	 * @param typeid
	 * @param flag
	 * @return
	 */
	public int addLog(String agencyId, String typeid) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("typeId", typeid);
		return dao.insert("fee.addFlag", map);
	}


	public Integer getFlag(String agencyId) {
		// TODO Auto-generated method stub
		return (Integer) dao.queryForObject("fee.getFlag",agencyId);
	}
	
	public Integer delFlag(String agencyId,String typeId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("typeId", typeId);
		return dao.delete("fee.delFlag", map);
	}


	public Integer getSplitFeeDetailCount(String agencyId, String datestart,String dateend,
			String dealTypeId, String serialnumber, String terminalId,
			String startValue, String endValue, String level,String flag) {
		Integer returnid = null;   
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", level);
		map.put("flag",flag);
		map.put("result", returnid);
		dao.queryForObject("fee.getSplitFeeDetailCount", map);
		// TODO Auto-generated method stub
		return  returnid = (Integer) map.get("result");
	}


	public Integer splitFeeDetailCount(String yearmonth, String agencyId,
			String dealTypeId) {
		// TODO Auto-generated method stub
		Integer returnid=null;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("yearmonth", yearmonth);
		map.put("dealTypeId", dealTypeId);
		map.put("result", returnid);
		dao.queryForObject("fee.splitFeeDetailCount", map);
		return  returnid = (Integer) map.get("result");
	}

	

	
	public Integer getSplitFeeDetailAllCount(String agencyId, String datestart,String dateend,
			String dealTypeId, String serialnumber, String terminalId,
			String startValue, String endValue, String level,String loginAgencyId) {
		Integer returnid = null;   
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", level);
		map.put("loginAgencyId",loginAgencyId);
		map.put("result", returnid);
		dao.queryForObject("fee.getSplitFeeDetailAllCount", map);
		// TODO Auto-generated method stub
		return  returnid = (Integer) map.get("result");
	}
	
	/**
	 * 获取分润明细记录
	 * @param agencyId
	 * @param yearmonth
	 * @param dealTypeId
	 * @param serialnumber
	 * @param terminalId
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealBean> getSplitFeeDetailAll(String agencyId,
			 String datestart,String dateend, String dealTypeId, String serialnumber,
			String terminalId,String startValue,String endValue,String levelCode,String loginAgencyId,int start,int end) {
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", levelCode);
		map.put("loginAgencyId",loginAgencyId);
		map.put("start", start);
		map.put("end", end);
		 dao.queryForList("fee.getSplitFeeDetailAll", map);
		 List<SplitFeeDealBean> list= ( List<SplitFeeDealBean> ) map.get("result");   
		return list;    
	}
	
	
	public int  insertSplitFeeSum(String datestart,String dateend,String agencyId,String parentAgencyId,String applyAgencyId,String applyDate){
		Map<String,String> map=new HashMap<String, String>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("agencyId", agencyId);
		map.put("parentAgencyId", parentAgencyId);
		map.put("timestamp",CommonDate.getDateStr());
		map.put("applyAgencyId", applyAgencyId);
		map.put("applyDate", applyDate);
		return dao.insert("fee.splitFeeSum", map);
	}
	public Integer getHisSplitFeeDetailCount(String agencyId, String datestart,String dateend,
			String dealTypeId, String serialnumber, String terminalId,
			String startValue, String endValue, String level,String flag) {
		Integer returnid = null;   
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", level);
		map.put("flag",flag);
		map.put("result", returnid);
		dao.queryForObject("fee.getHisSplitFeeDetailCount", map);
		// TODO Auto-generated method stub
		return  returnid = (Integer) map.get("result");
	}
	/**
	 * 获取历史分润明细记录
	 * @param agencyId
	 * @param datestart
	 * @param dateend
	 * @param dealTypeId
	 * @param serialnumber
	 * @param terminalId
	 * @param startValue
	 * @param endValue
	 * @param levelCode
	 * @param loginAgencyId
	 * @param start
	 * @param end
	 * @param agencyOnly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SplitFeeDealBean> getHisSplitFeeDetail(String agencyId,
			 String datestart,String dateend, String dealTypeId, String serialnumber,
			String terminalId,String startValue,String endValue,String levelCode,
			String flag,int start,int end) {	
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("agencyId", agencyId);
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealTypeId", dealTypeId);
		map.put("serialnumber", serialnumber);
		map.put("terminalId", terminalId);
		map.put("startValue", startValue);
		map.put("endValue", endValue);
		map.put("levelCode", levelCode);
		map.put("flag",flag);
		map.put("start", start);
		map.put("end", end);
		 dao.queryForList("fee.getHisSplitFeeDetail", map);
		  List<SplitFeeDealBean> list= ( List<SplitFeeDealBean> ) map.get("result");   
		return list;    
	}
	
}
