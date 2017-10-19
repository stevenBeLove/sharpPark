package com.compass.dealmanager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.dealmanager.model.DealBean;
import com.compass.dealmanager.model.DealCountBean;
import com.compass.dealmanager.model.DealDeductBean;
import com.compass.dealmanager.model.DeductTypeBean;
import com.compass.dealmanager.model.OtherDeductBean;
import com.compass.dealmanager.model.ProfitBean;
import com.compass.dealmanager.model.RealDeductBean;
import com.compass.utils.AbstractService;

/**
 * 
 * @author gaoyang 交易管理
 * 
 */
public class DealManageService extends AbstractService {

	/**
	 * 获取交易信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getDealList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.queryForList("deal.getDealList", map);
	}

	/**
	 * 获取交易编号总数
	 * 
	 * @param systemId
	 * @return
	 */
	public Integer getDealListCount(String systemId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemId", systemId);
		return (Integer) dao.queryForObject("deal.getDealListCount", map);
	}

	/**
	 * 增加交易信息
	 * 
	 * @param dealBean
	 * @return
	 */
	public int addDeal(DealBean dealBean) {

		return dao.insert("deal.addDeal", dealBean);
	}

	/**
	 * 增加交易失败信息
	 * 
	 * @param dealBean
	 * @return
	 */
	public int addDealFail(String dealFailId, String failTxt, String dealFailDesc, String createDt) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dealFailId", dealFailId);
		map.put("failTxt", failTxt);
		map.put("dealFailDesc", dealFailDesc);
		map.put("createDt", createDt);
		return dao.insert("deal.addDealFail", map);
	}

	@SuppressWarnings("unchecked")
	public List<ProfitBean> getProfit(String yearmonth, String agencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonth", yearmonth);
		map.put("agencyId", agencyId);
		return dao.queryForList("deal.getProfitone", map);
	}

	/**
	 * 根据某个机构 所有类型的分润金额
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProfitBean> getDealByType(String yearmonth, String agencyId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonth", yearmonth);
		map.put("agencyId", agencyId);
		return dao.queryForList("deal.getDealByType", map);
	}

	/*
	 * public Integer getDealByTypeCount(String yearmonth,String agencyId) { //
	 * TODO Auto-generated method stub Map<String,Object> map=new
	 * HashMap<String,Object>(); map.put("yearmonth", yearmonth);
	 * map.put("agencyId", agencyId); return
	 * (Integer)dao.queryForObject("deal.getDealByTypeCount",map); }
	 */

	/**
	 * 获取当前机构下的交易总条数
	 * 
	 * @param yearmonthdatestart
	 *            开始日期
	 * @param yearmonthdateend
	 *            结束日期
	 * @param agencyId
	 *            登录用户机构编号
	 * @param systemId
	 *            来源系统
	 * @param agencySelect
	 *            选择查询机构编号
	 * @param serialNumber
	 *            流水号
	 * @param dealType
	 *            交易类型
	 * @param merchantCode
	 *            商户编号
	 * @param terminalcode
	 *            终端编号
	 * @param upperAgencyid
	 *            特殊机构查询标记（机构归并,不为空则进行归并，二级机构开始）
	 * @param parentAgencyid
	 *            上级机构编号
	 * @param agecyFlag
	 *            特殊机构查询标记（机构归并,若为不空只能看到直属机构 ，一级机构开始）
	 * @param topAgencyId
	 *            顶级机构
	 * @param moblieNo
	 *            手机号码
	 * @param customerName
	 *            客户姓名
	 * @return
	 */
	public DealCountBean getDealdetailCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName, String typeMergeFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getDealdetailCount", map);
	}

	/**
	 * 
	 * 获取当前机构下的交易明细
	 * 
	 * @param yearmonthdatestart
	 *            开始日期
	 * @param yearmonthdateend
	 *            结束日期
	 * @param agencyId
	 *            登录用户机构编号
	 * @param systemId
	 *            来源系统
	 * @param agencySelect
	 *            选择查询机构编号
	 * @param serialNumber
	 *            流水号
	 * @param dealType
	 *            交易类型
	 * @param merchantCode
	 *            商户编号
	 * @param start
	 *            开始行
	 * @param end
	 *            结束行
	 * @param terminalcode
	 *            终端编号
	 * @param upperAgencyid
	 *            特殊机构查询标记（机构归并,不为空则进行归并，二级机构开始）
	 * @param parentAgencyid
	 *            上级机构编号
	 * @param agecyFlag
	 *            特殊机构查询标记（机构归并,若为不空只能看到直属机构 ，一级机构开始）
	 * @param topAgencyId
	 *            顶级机构
	 * @param moblieNo
	 *            手机号码
	 * @param customerName
	 *            客户姓名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getDealdetail(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,
			String serialNumber, String dealType, String merchantCode, int start, int end, String terminalcode, String upperAgencyid,
			String parentAgencyid, String agecyFlag, String topAgencyId, String moblieNo, String customerName, String typeMergeFlag) {

		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("start", start);
		map.put("end", end);
		map.put("terminalcode", terminalcode);
		map.put("upperAgencyid", upperAgencyid);
		map.put("parentAgencyid", parentAgencyid);
		map.put("agecyFlag", agecyFlag);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return dao.queryForList("deal.getDealdetail", map);
	}

	/**
	 * 获取当前机构下的所有交易明细
	 * 
	 * @param yearmonthdate
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getDealdetailAll(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,
			String parentAgencyid, String upperAgencyid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("parentAgencyid", parentAgencyid);
		map.put("upperAgencyid", upperAgencyid);
		return dao.queryForList("deal.getDealdetailAll", map);
	}

	/**
	 * 得到交易类型详细的总条数
	 * 
	 * @param yearmonth
	 * @param agencyId
	 * @param dealtype
	 * @return
	 */
	public Integer getDealtypedetailCount(String yearmonth, String agencyId, String dealtype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonth", yearmonth);
		map.put("agencyId", agencyId);
		map.put("dealtype", dealtype);
		return (Integer) dao.queryForObject("deal.getDealtypedetailCount", map);
	}

	/**
	 * 查看当前机构的交易明细（限制条件:1 年月 2 机构 3 交易类型）
	 * 
	 * @param yearmonth
	 * @param agencyId
	 * @param dealtype
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getDealtypedetail(String yearmonth, String agencyId, String dealtype, int start, int end) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonth", yearmonth);
		map.put("agencyId", agencyId);
		map.put("dealtype", dealtype);
		map.put("start", start);
		map.put("end", end);
		return dao.queryForList("deal.getDealtypedetail", map);
	}

	@SuppressWarnings("unchecked")
	public List<DealBean> getDealtypedetailAll(String yearmonth, String agencyId, String dealtype) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonth", yearmonth);
		map.put("agencyId", agencyId);
		map.put("dealtype", dealtype);
		return dao.queryForList("deal.getDealtypedetailAll", map);
	}

	/**
	 * 获取终端失败信息序列号
	 * 
	 * @return
	 */
	public Integer getDealFailId() {
		return (Integer) dao.queryForObject("SEQUENCE.getDealFailId");
	}

	/**
	 * 获取交易统计报表总条数
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @return
	 */
	public DealCountBean getDealStatCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,String typeMergeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("typeMergeFlag",typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getDealStatCount", map);
	}

	/**
	 * 获取交易统计报表
	 * 
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param start
	 * @param end
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getDealStat(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,
			int start, int end ,String typeMergeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("start", start);
		map.put("end", end);
		map.put("typeMergeFlag", typeMergeFlag);
		return dao.queryForList("deal.getDealStat", map);
	}

	/**
	 * 获取交易扣款明细总数
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param serialNumber
	 * @param dealType
	 * @param terminalCode
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	public Integer getDealDeductCount(String datestart, String dateend, String dealFlag, String agencyId, String systemId, String agencySelect,
			String serialNumber, String dealType, String terminalCode, String upperAgencyId, String parentAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealFlag", dealFlag);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("terminalCode", terminalCode);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		return (Integer) dao.queryForObject("deal.getDealDeductCount", map);
	}

	/**
	 * 获取交易扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param serialNumber
	 * @param dealType
	 * @param start
	 * @param end
	 * @param terminalCode
	 * @param upperAgencyid
	 * @param parentAgencyid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealDeductBean> getDealDeduct(String datestart, String dateend, String dealFlag, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, int start, int end, String terminalCode, String upperAgencyId,
			String parentAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealFlag", dealFlag);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("start", start);
		map.put("end", end);
		map.put("terminalCode", terminalCode);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		return dao.queryForList("deal.getDealDeduct", map);
	}

	/**
	 * 获得扣款类型信息
	 * 
	 * @param deductClass
	 * @param deductTypeName
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeductTypeBean> getDeductType(String deductClass, String deductTypeName, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deductClass", deductClass);
		map.put("deductTypeName", deductTypeName);
		map.put("status", status);
		return dao.queryForList("deal.getDeductType", map);
	}

	/**
	 * 获取其他扣款明细总数
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	public Integer getOtherDeductCount(String datestart, String dateend, String dealFlag, String agencyId, String systemId, String agencySelect,
			String upperAgencyId, String parentAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealFlag", dealFlag);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		return (Integer) dao.queryForObject("deal.getOtherDeductCount", map);
	}

	/**
	 * 获取其他扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param start
	 * @param end
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OtherDeductBean> getOtherDeduct(String datestart, String dateend, String dealFlag, String agencyId, String systemId,
			String agencySelect, int start, int end, String upperAgencyId, String parentAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealFlag", dealFlag);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("start", start);
		map.put("end", end);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		return dao.queryForList("deal.getOtherDeduct", map);
	}

	/**
	 * 获取交易和其他扣款统计信息
	 * 
	 * @param datestart
	 * @param dateend
	 * @param dealFlag
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param start
	 * @param end
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RealDeductBean> getDeductStat(String datestart, String dateend, String dealFlag, String agencyId, String systemId,
			String agencySelect, String upperAgencyId, String parentAgencyId, String thisAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("dealFlag", dealFlag);
		map.put("agencySelect", agencySelect);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		map.put("thisAgencyId", thisAgencyId);
		return dao.queryForList("deal.getDeductStat", map);
	}

	/**
	 * 获取实际扣款明细
	 * 
	 * @param datestart
	 * @param dateend
	 * @param dealFlag
	 * @param agencyId
	 * @param systemId
	 * @param agencySelect
	 * @param upperAgencyId
	 * @param parentAgencyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RealDeductBean> getRealDeduct(String datestart, String dateend, String dealFlag, String real, String agencyId, String systemId,
			String agencySelect, String upperAgencyId, String parentAgencyId, String thisAgencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datestart", datestart);
		map.put("dateend", dateend);
		map.put("real", real);
		map.put("dealFlag", dealFlag);
		map.put("agencySelect", agencySelect);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("upperAgencyId", upperAgencyId);
		map.put("parentAgencyId", parentAgencyId);
		map.put("thisAgencyId", thisAgencyId);
		return dao.queryForList("deal.getRealDeduct", map);
	}

	/**
	 * 获取下一个扣款类型值（当前最大值加1）
	 * 
	 * @return
	 */
	public String getNextDeductType() {
		Integer maxType = (Integer) dao.queryForObject("deal.getNextDeductType");
		return String.valueOf(maxType + 1);
	}

	/**
	 * 生成其他扣款唯一编号
	 * 
	 * @return
	 */
	public Integer getOtherDeductId() {
		return (Integer) dao.queryForObject("SEQUENCE.getOtherDeductId");
	}

	/**
	 * 获取下一个扣实际扣款编号（当前最大值加1）
	 * 
	 * @return
	 */
	public Integer getRealDeductId() {
		return (Integer) dao.queryForObject("deal.getRealDeductId");
	}

	/**
	 * 交易扣款增加前检查
	 * 
	 * @param dealDate
	 * @param dealTime
	 * @param serialNumber
	 * @return
	 */
	public Integer checkDealDeductAdd(String dealDate, String dealTime, String serialNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dealDate", dealDate);
		map.put("dealTime", dealTime);
		map.put("serialNumber", serialNumber);
		return (Integer) dao.queryForObject("deal.checkDealDeductAdd", map);
	}

	/**
	 * 向扣款类型表中增加一条记录
	 * 
	 * @param deductTypeBean
	 * @return
	 */
	public int addDeductType(DeductTypeBean deductTypeBean) {
		int result = dao.insert("deal.addDeductType", deductTypeBean);
		return result;
	}

	/**
	 * 向交易扣款表中增加一条记录
	 * 
	 * @param dealDeductBean
	 * @return
	 */
	public int addDealDeduct(DealDeductBean dealDeductBean) {
		int result = dao.insert("deal.addDealDeduct", dealDeductBean);
		return result;
	}

	/**
	 * 向其他扣款表增加一条记录
	 * 
	 * @param otherDeductBean
	 * @return
	 */
	public int addOtherDeduct(OtherDeductBean otherDeductBean) {
		int result = dao.insert("deal.addOtherDeduct", otherDeductBean);
		return result;
	}

	/**
	 * 向实际扣款表增加一条记录
	 * 
	 * @param realDeductBean
	 * @return
	 */
	public int addRealDeduct(RealDeductBean realDeductBean) {
		int result = dao.insert("deal.addRealDeduct", realDeductBean);
		return result;
	}

	/**
	 * 更新扣款类型表
	 * 
	 * @param deductTypeBean
	 * @return
	 */
	public int updateDeductType(DeductTypeBean deductTypeBean) {
		int result = dao.update("deal.updateDeductType", deductTypeBean);
		return result;
	}

	/**
	 * 更新交易扣款表的一条记录
	 * 
	 * @param dealDeductBean
	 * @return
	 */
	public int updateDealDeduct(DealDeductBean dealDeductBean) {
		int result = dao.update("deal.updateDealDeduct", dealDeductBean);
		return result;
	}

	/**
	 * 更新其他扣款表的一条记录
	 * 
	 * @param otherDeductBean
	 * @return
	 */
	public int updateOtherDeduct(OtherDeductBean otherDeductBean) {
		int result = dao.update("deal.updateOtherDeduct", otherDeductBean);
		return result;
	}

	/**
	 * 更新实际扣款表的一条记录
	 * 
	 * @param realDeductBean
	 * @return
	 */
	public int updateRealDeduct(RealDeductBean realDeductBean) {
		int result = dao.update("deal.updateRealDeduct", realDeductBean);
		return result;
	}

	/**
	 * 删除扣款类型表的一条记录
	 * 
	 * @param deductClass
	 * @param deductType
	 * @return
	 */
	public int deleteDeductType(String deductClass, String deductType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deductClass", deductClass);
		map.put("deductType", deductType);
		return dao.delete("deal.deleteDeductType", map);
	}

	/**
	 * 删除交易扣款表的一条记录
	 * 
	 * @param dealDate
	 * @param dealTime
	 * @param serialNumber
	 * @return
	 */
	public int deleteDealDeduct(String dealDate, String dealTime, String serialNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dealDate", dealDate);
		map.put("dealTime", dealTime);
		map.put("serialNumber", serialNumber);
		return dao.delete("deal.deleteDealDeduct", map);
	}

	/**
	 * 删除其他扣款表中的多条记录
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteOtherDeducts(String ids) {
		int result = dao.delete("deal.deleteOtherDeducts", ids);
		return result;
	}

	/**
	 * 删除其他扣款表中的多条记录
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteRealDeducts(String ids) {
		int result = dao.delete("deal.deleteRealDeducts", ids);
		return result;
	}

	public DealCountBean getOldDealStatCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,String typeMergeFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getOldDealStatCount", map);
	}

	public List<DealBean> getOldDealStat(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,
			int start, int end,String typeMergeFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("start", start);
		map.put("end", end);
		map.put("typeMergeFlag", typeMergeFlag);
		return dao.queryForList("deal.getOldDealStat", map);
	}

	/**
	 * 获取当前机构下的历史交易总条数
	 * 
	 * @param yearmonthdatestart
	 *            开始日期
	 * @param yearmonthdateend
	 *            结束日期
	 * @param agencyId
	 *            登录用户机构编号
	 * @param systemId
	 *            来源系统
	 * @param agencySelect
	 *            选择查询机构编号
	 * @param serialNumber
	 *            流水号
	 * @param dealType
	 *            交易类型
	 * @param merchantCode
	 *            商户编号
	 * @param terminalcode
	 *            终端编号
	 * @param upperAgencyid
	 *            特殊机构查询标记（机构归并,不为空则进行归并，二级机构开始）
	 * @param parentAgencyid
	 *            上级机构编号
	 * @param agecyFlag
	 *            特殊机构查询标记（机构归并,若为不空只能看到直属机构 ，一级机构开始）
	 * @param topAgencyId
	 *            顶级机构
	 * @param moblieNo
	 *            手机号码
	 * @param customerName
	 *            客户姓名
	 * @return
	 */
	public DealCountBean getOldDealdetailCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName,String typeMergeFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getOldDealdetailCount", map);
	}

	/**
	 * 
	 * 获取当前机构下的历史交易明细
	 * 
	 * @param yearmonthdatestart
	 *            开始日期
	 * @param yearmonthdateend
	 *            结束日期
	 * @param agencyId
	 *            登录用户机构编号
	 * @param systemId
	 *            来源系统
	 * @param agencySelect
	 *            选择查询机构编号
	 * @param serialNumber
	 *            流水号
	 * @param dealType
	 *            交易类型
	 * @param merchantCode
	 *            商户编号
	 * @param start
	 *            开始行
	 * @param end
	 *            结束行
	 * @param terminalcode
	 *            终端编号
	 * @param upperAgencyid
	 *            特殊机构查询标记（机构归并,不为空则进行归并，二级机构开始）
	 * @param parentAgencyid
	 *            上级机构编号
	 * @param agecyFlag
	 *            特殊机构查询标记（机构归并,若为不空只能看到直属机构 ，一级机构开始）
	 * @param topAgencyId
	 *            顶级机构
	 * @param moblieNo
	 *            手机号码
	 * @param customerName
	 *            客户姓名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DealBean> getOldDealdetail(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId, String agencySelect,
			String serialNumber, String dealType, String merchantCode, int start, int end, String terminalcode, String upperAgencyid,
			String parentAgencyid, String agecyFlag, String topAgencyId, String moblieNo, String customerName,String typeMergeFlag) {

		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("start", start);
		map.put("end", end);
		map.put("terminalcode", terminalcode);
		map.put("upperAgencyid", upperAgencyid);
		map.put("parentAgencyid", parentAgencyid);
		map.put("agecyFlag", agecyFlag);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag",  typeMergeFlag);
		return dao.queryForList("deal.getOldDealdetail", map);
	}

	public int getAllowedMaxNum() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("maxNum", "ALLOWEDMAXNUM");
		List<String> listStrings = dao.queryForList("deal.getAllowedMaxNum", map);
		if (listStrings == null || listStrings.size() == 0) {
			return 200000;
		}
		return Integer.parseInt(listStrings.get(0));
	}
	
	public DealCountBean getDealdetailReceCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName, String typeMergeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getDealdetailReceCount", map);
	}


	public DealCountBean getDealdetailT0Count(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName, String typeMergeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getDealdetailT0Count", map);
	}

	public DealCountBean getOldDealdetailReceCount(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName,String typeMergeFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getOldDealdetailReceCount", map);
	}
	
	public DealCountBean getOldDealdetailT0Count(String yearmonthdatestart, String yearmonthdateend, String agencyId, String systemId,
			String agencySelect, String serialNumber, String dealType, String merchantCode, String terminalcode, String topAgencyId, String moblieNo,
			String customerName, String typeMergeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearmonthdatestart", yearmonthdatestart);
		map.put("yearmonthdateend", yearmonthdateend);
		map.put("agencyId", agencyId);
		map.put("systemId", systemId);
		map.put("agencySelect", agencySelect);
		map.put("serialNumber", serialNumber);
		map.put("dealType", dealType);
		map.put("merchantCode", merchantCode);
		map.put("terminalcode", terminalcode);
		map.put("topAgencyId", topAgencyId);
		map.put("moblieNo", moblieNo);
		map.put("customerName", customerName);
		map.put("typeMergeFlag", typeMergeFlag);
		return (DealCountBean) dao.queryForObject("deal.getOldDealdetailT0Count", map);
	}
}
