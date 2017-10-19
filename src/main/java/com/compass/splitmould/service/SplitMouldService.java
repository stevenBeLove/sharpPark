package com.compass.splitmould.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ardu.jms.ems.exception.QTException;

import com.compass.splitmould.model.SplitMouldBean;
import com.compass.splitmould.model.SplitOrgMouldBean;
import com.compass.splitmould.model.SplitTopMouldBean;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;

/**
 * 
 * @author liCheng 分润模板设置
 *
 */
public class SplitMouldService extends AbstractService {
	

    /**
     * 获得分润规则的Service方法
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getRules(String agencyId, String childagencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("childagencyId", childagencyId);
        return dao.queryForList("mould.getRules", map);
    }

    /**
     * 获得顶级分润模板的Service方法（根据String agencyId,String mouldName）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> getTopMoulds(String agencyId, String mouldName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        return dao.queryForList("mould.getTopMoulds", map);
    }

    /**
     * 获得顶级分润模板的Service方法（根据String agencyId,String mouldName）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> getTopMouldByAR(String agencyId, String ruleId)throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("ruleId", ruleId);
        return dao.queryForList("mould.getTopMouldByAR", map);
    }

    /**
     * 顶级机构获得模板分配信息Service方法（根据String agencyId,String mouldName）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> getDistrMoulds(String agencyId, String mouldName, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        map.put("status", status);
        return dao.queryForList("mould.getDistrMoulds", map);
    }

    /**
     * 顶级(上级)机构获得模板分配信息详情Service方法（根据String agencyId）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getDistrMouldsD(String ruleId, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("agencyId", agencyId);
        // 获得刚分配时候的名称常量
        map.put("noName", ConstantUtils.NOMOULDNAME);
        return dao.queryForList("mould.getDistrMouldsD", map);
    }

    /**
     * 上级机构获得可供选择的模板的方法（根据String agencyId）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> getSelectMoulds(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return dao.queryForList("mould.getSelectMoulds", map);
    }

    /**
     * 上级机构获得模板分配信息Service方法（根据String agencyId,String mouldName）
     * 
     * @return
     */
    /*
     * @SuppressWarnings("unchecked") public List<SplitMouldBean>
     * getUpperDistrMoulds(String agencyId,String mouldName) { Map<String,
     * Object> map = new HashMap<String, Object>(); map.put("agencyId",
     * agencyId); map.put("mouldName", mouldName); return
     * dao.queryForList("mould.getUpperDistrMoulds", map); }
     */

    /**
     * 添加顶级分润模板的Service方法
     * 
     * @return
     */

    public int addTopMould(SplitTopMouldBean splittopmouldBean) {
        // TODO Auto-generated method stub
        return dao.insert("mould.addTopMould", splittopmouldBean);
    }

    /**
     * 修改顶级分润模板的Service方法
     * 
     * @return
     */

    public int updateTopMould(SplitTopMouldBean splittopmouldBean) {
        // TODO Auto-generated method stub
        return dao.update("mould.updateTopMould", splittopmouldBean);
    }

    /**
     * 分配时，顶级分润模板，用新的代替旧的的Service方法
     * 
     * @return
     */

    public int updateTopMouldD(SplitTopMouldBean splittopmouldBean) {
        // TODO Auto-generated method stub
        return dao.update("mould.updateTopMouldD", splittopmouldBean);
    }

    /**
     * 上级机构更新已分配的模板的方法Service方法
     * 
     * @return
     */
    public void updateUpperDistrMould(String childAgencyId, String ruleId, String mouldName, String createDt, String createId)throws Exception {
        // 得到原来的模板
        List<SplitOrgMouldBean> beanList = getOrgMoulds(null, childAgencyId, ConstantUtils.NOMOULDNAME);
        for (int i = 0; i < beanList.size(); i++) {
            // 将原来的模板加到历史表
            addDisHisMould(beanList.get(i), createDt, createId);
        }
        // 删除原来的模板
        deleteOrgMould(beanList.get(0).getRuleId(), beanList.get(0).getAgencyId(), ConstantUtils.NOMOULDNAME);
    }

    /**
     * 删除顶级分润模板的Service方法(逻辑删除，置为无效)
     * 
     * @return
     */
    public Integer deleteTopMould(String ruleId, String AgencyId) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("agencyId", AgencyId);
        return dao.update("mould.delTopMould", map);
    }

    /**
     * 删除分润模板的Service方法
     * 
     * @return
     */
    public Integer deleteMould(String ruleId) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        return dao.delete("mould.delMould", map);
    }

    /**
     * 删除分润模板的Service方法
     * 
     * @return
     */
    public Integer deleteOrgMould(String ruleId, String agencyId, String mouldName) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        return dao.delete("mould.deleteOrgMould", map);
    }

    /**
     * 方法名： getMoulds.<br/>
     * 方法作用:获得分润模板的Service方法.<br/>
     * 创建者：zhangjun.<br/>
     * @param ruleId 规则号
     * @param mouldsDealTypeStr 模板中控制的交易类型
     * 创建日期：2016年11月10日.<br/>
     * 创建时间：下午3:07:11.<br/>
     * 返回值： @return 返回结果：List<SplitMouldBean>.<br/>
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getMoulds(String ruleId, String mouldsDealTypeStr)throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("mouldsDealTypeStr", mouldsDealTypeStr);
        return dao.queryForList("mould.getMoulds", map);
    }

    /**
     * 上级机构，分配模板时,用于显示的模板信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getMouldsD(String ruleId, String mouldName, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("mouldName", mouldName);
        map.put("agencyId", agencyId);
        return dao.queryForList("mould.getMouldsD", map);
    }

    /**
     * 上级机构，分配模板时,用于传递的模板信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getMouldsDT(String ruleId, String mouldName, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("mouldName", mouldName);
        map.put("agencyId", agencyId);
        return dao.queryForList("mould.getMouldsDT", map);
    }

    /**
     * 获得已经添加的机构分润模板的Service方法
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitOrgMouldBean> getAddedOrgMoulds(String agencyId, String mouldName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        return dao.queryForList("mould.getAddedOrgMoulds", map);
    }

    /**
     * 获得机构分润模板的Service方法
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitOrgMouldBean> getOrgMoulds(String ruleId, String agencyId, String mouldName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        return dao.queryForList("mould.getOrgMoulds", map);
    }

    /**
     * 添加分润规则方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int addRule(SplitMouldBean splitRuleBean) {
        // TODO Auto-generated method stub
        return dao.insert("mould.addRule", splitRuleBean);
    }

    public int updateRule(SplitMouldBean splitRuleBean) {
        return dao.update("mould.updateRule", splitRuleBean);
    }

    /**
     * 添加分润规则方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int addMould(SplitMouldBean splitMouldBean) {
        // TODO Auto-generated method stub
        return dao.insert("mould.addMould", splitMouldBean);
    }

    /**
     * 修改分润规则方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int updateMould(SplitMouldBean splitMouldBean) {
        // TODO Auto-generated method stub
        return dao.insert("mould.updateMould", splitMouldBean);
    }

    /**
     * 在上级机构分配时，添加机构分润规则的方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int addOrgMould(SplitMouldBean splitMouldBean, String agencyId, String createDt)throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("ruleId", splitMouldBean.getRuleId());
        map.put("dealType", splitMouldBean.getDealType());
        map.put("splittingMode", splitMouldBean.getSplittingMode());
        map.put("splittingRegionMode", splitMouldBean.getSplittingRegionMode());
        map.put("ruleBegin", splitMouldBean.getRuleBegin());
        map.put("ruleEnd", splitMouldBean.getRuleEnd());
        map.put("ruleNum", splitMouldBean.getRuleNum());
        map.put("createrId", splitMouldBean.getCreaterId());
        map.put("createDate", createDt);
        map.put("ruleRem", splitMouldBean.getRuleRem());
        map.put("ruleValue", splitMouldBean.getRuleValue());
        map.put("validityDate", splitMouldBean.getValidityDate());
        // 新分配的时候模板名称设为无名
        map.put("mouldName", ConstantUtils.NOMOULDNAME);
        return dao.insert("mould.addOrgMould", map);
    }

    /**
     * 当机构在模板设置时，添加机构分润历史表的方法
     * 
     * @param SplitOrgMouldBean
     * @return
     */
    public int addHisMould(SplitOrgMouldBean bean, String createDt, String createId, String agencyId, String validityDate, int value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("ruleId", bean.getRuleId());
        map.put("dealType", bean.getDealType());
        map.put("splittingMode", bean.getSplittingMode());
        map.put("splittingRegionMode", bean.getSplittingRegionMode());
        map.put("ruleBegin", bean.getRuleBegin());
        map.put("ruleEnd", bean.getRuleEnd());
        map.put("ruleNum", bean.getRuleNum());
        map.put("createrId", createId);
        map.put("createDate", bean.getCreateDate());
        map.put("ruleRem", bean.getRuleRem());
        map.put("mouldName", bean.getMouldName());
        // 把原来的值放进去
        map.put("ruleValue", value);
        map.put("validityDate", validityDate);
        // 赋一个失效日期
        map.put("invalidityDate", createDt);
        return dao.insert("mould.addHisMould", map);
    }

    /**
     * 分配时，当机构在用新模板替换时，把旧模板添加到机构分润历史表的方法
     * 
     * @param SplitOrgMouldBean
     * @return
     */
    public int addDisHisMould(SplitOrgMouldBean bean, String createDt, String createId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", bean.getAgencyId());
        map.put("ruleId", bean.getRuleId());
        map.put("dealType", bean.getDealType());
        map.put("splittingMode", bean.getSplittingMode());
        map.put("splittingRegionMode", bean.getSplittingRegionMode());
        map.put("ruleBegin", bean.getRuleBegin());
        map.put("ruleEnd", bean.getRuleEnd());
        map.put("ruleNum", bean.getRuleNum());
        map.put("createrId", createId);
        map.put("createDate", bean.getCreateDate());
        map.put("ruleRem", bean.getRuleRem());
        map.put("mouldName", bean.getMouldName());
        map.put("ruleValue", bean.getRuleValue());
        map.put("validityDate", bean.getValidityDate());
        // 赋一个失效日期
        map.put("invalidityDate", createDt);
        return dao.insert("mould.addHisMould", map);
    }

    /**
     * 当机构在分配查看时，添加机构分润历史表的方法
     * 
     * @param SplitOrgMouldBean
     * @return
     */
    public int addHisMouldD(SplitOrgMouldBean bean, String createDt, String createId, String agencyId, int value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("ruleId", bean.getRuleId());
        map.put("dealType", bean.getDealType());
        map.put("splittingMode", bean.getSplittingMode());
        map.put("splittingRegionMode", bean.getSplittingRegionMode());
        map.put("ruleBegin", bean.getRuleBegin());
        map.put("ruleEnd", bean.getRuleEnd());
        map.put("ruleNum", bean.getRuleNum());
        map.put("createrId", createId);
        map.put("createDate", bean.getCreateDate());
        map.put("ruleRem", bean.getRuleRem());
        map.put("mouldName", bean.getMouldName());
        // 把原来的值放进去
        map.put("ruleValue", value);
        // 赋一个失效日期
        map.put("invalidityDate", createDt);
        return dao.insert("mould.addHisMouldD", map);
    }

    /**
     * 更新机构分润规则的方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int updateOrgMouldData(SplitOrgMouldBean bean) {
        // 此处判断是不是按金额分润
        if (bean.getSplittingMode().equals(ConstantUtils.SPLITBYAMT)) {
            bean.setRuleValue(bean.getRuleValue() / 100);
        }
        return dao.update("mould.updateOrgMouldData", bean);
    }

    /**
     * 新增机构分润规则的方法
     * 
     * @param splitRuleBean
     * @return
     */
    public int addOrgMouldData(SplitOrgMouldBean bean) {
        // 此处判断是不是按金额分润
        if (bean.getSplittingMode().equals(ConstantUtils.SPLITBYAMT)) {
            bean.setRuleValue(bean.getRuleValue() / 100);
        }
        return dao.insert("mould.addOrgMouldData", bean);
    }

    /**
     * 上级机构根据机构编号获得自己属下的模板名称
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> getTopMouldList(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("noName", ConstantUtils.NOMOULDNAME);
        return dao.queryForList("mould.getTopMouldList", map);
    }

    /**
     * 顶级机构级机构根据机构编号获得自己属下的模板名称
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitTopMouldBean> topGetMouldList(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return dao.queryForList("mould.topGetMouldList", map);
    }

    /**
     * 得到机构模板报表中的规则值
     * 
     * @param
     * @return
     */
    public int getOrgValue(String ruleNum, String ruleId, String mouldName, String agencyId) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleNum", ruleNum);
        map.put("mouldName", mouldName);
        map.put("ruleId", ruleId);
        map.put("agencyId", agencyId);
        int value = (Integer) dao.queryForObject("mould.getOrgValue", map);
        return value;
    }

    /**
     * 得到机构模板报表中的生效日期
     * 
     * @param
     * @return
     */
    public String getOrgValidityDate(String ruleNum, String ruleID, String mouldName, String agencyId) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleNum", ruleNum);
        map.put("mouldName", mouldName);
        map.put("ruleID", ruleID);
        map.put("agencyId", agencyId);
        String value = (String) dao.queryForObject("mould.getOrgValidityDate", map);
        return value;
    }

    /**
     * 获取分润规则编号
     * 
     * @return
     */
    public Integer getRuleId() {
        return (Integer) dao.queryForObject("SEQUENCE.getRuleId");
    }

    /**
     * 删除分润规则
     * 
     * @param ruleIds
     * @return
     */
    public int deleteRules(String agencyId, String childAgencyId, String dealType, String validityData) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("childAgencyId", childAgencyId);
        map.put("dealType", dealType);
        map.put("validityData", validityData);
        return dao.delete("mould.deleteRules", map);
    }

    /**
     * 根据条件获取相应的规则编号
     * 
     * @param agencyId
     * @param childAgencyId
     * @param dealType
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> checkRule(String agencyId, String childAgencyId, String dealType, String validityData) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("childAgencyId", childAgencyId);
        map.put("dealType", dealType);
        map.put("validityData", validityData);
        return dao.queryForList("mould.checkrule", map);
    }

    /**
     * 获取交易规模区间
     * 
     * @param ruleIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getRulesByIds(String agencyId, String childAgencyId, String dealType) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("childAgencyId", childAgencyId);
        map.put("dealType", dealType);
        return dao.queryForList("mould.getRulesByIds", map);
    }

    /**
     * 获取单笔区间
     * 
     * @param ruleIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SplitMouldBean> getSingleDealSection(String ruleId) {
        // TODO Auto-generated method stub
        return dao.queryForList("mould.getSingleDealSection", ruleId);
    }

    /**
     * 根据交易规模区间删除单笔区间
     * 
     * @param ruleId
     */
    public int deleteSingleRules(String ruleId) {
        // TODO Auto-generated method stub
        return dao.delete("mould.deleteSingleRules", ruleId);
    }

    /**
     * 添加单笔区间
     * 
     * @param bean
     * @return
     */
    public int addSingleRule(SplitMouldBean bean) {
        // TODO Auto-generated method stub
        return dao.insert("mould.addSingleRule", bean);
    }

    public Integer getRulesCount(String agencyId, String childAgencyId, String dealType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("splitRegionMode", agencyId);
        map.put("childAgencyId", childAgencyId);
        map.put("dealType", dealType);

        // TODO Auto-generated method stub
        return (Integer) dao.queryForObject("mould.getRulesCount", map);
    }

    public Integer deleteRule(String ruleNum) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleNum", ruleNum);
        return dao.delete("mould.delRule", map);
    }

    /**
     * 将删除的机构模板传入历史表
     * 
     * @param ruleId
     * @param agencyId
     * @param mouldName
     */
    public void addDelRecords(String ruleId, String agencyId, String mouldName) {

        // 得到原值
        List<SplitOrgMouldBean> list = getOrgMoulds(ruleId, agencyId, mouldName);
        // 当前日期
        String createDt = CommonDate.getDate();
        for (int i = 0; i < list.size(); i++) {
            // 给记录赋一条失效日期
            list.get(i).setInvalidiayDate(createDt);
            // 添加进历史记录表
            addDelHis(list.get(i));
        }

    }

    /**
     * 添加历史表
     * 
     * @param SplitOrgMouldBean
     * @return
     */
    public int addDelHis(SplitOrgMouldBean bean) {
        return dao.insert("mould.addDelHis", bean);
    }

    /**
     * 获取模板序列号
     */
    public Integer getruleNum() {
        return (Integer) dao.queryForObject("SEQUENCE.getRuleNum");
    }

    /**
     * 判断子机构是否已经分配过某个模板
     */
    public Boolean checkDistr(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("agencyId", agencyId);
        map.put("noName", ConstantUtils.NOMOULDNAME);
        int num = (Integer) dao.queryForObject("mould.checkDistr", map);
        if (num != 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 判断是否已经将某模板分配给某个子系统
     */
    public Boolean checkDistrMould(String ruleId, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("ruleId", ruleId);
        map.put("noName", ConstantUtils.NOMOULDNAME);

        int num = (Integer) dao.queryForObject("mould.checkDistrMould", map);
        if (num != 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 判断顶级机构添加模板的时候是否重名
     */
    public Boolean checkTopMouldDup(String mouldName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mouldName", mouldName);
        int num = (Integer) dao.queryForObject("mould.checkTopMouldDup", map);
        if (num != 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 判断上级机构添加模板的时候是否重名
     */
    public Boolean checkOrgMouldDup(String agencyId, String mouldName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("mouldName", mouldName);
        int num = (Integer) dao.queryForObject("mould.checkOrgMouldDup", map);
        if (num != 0) {
            return false;
        } else {
            return true;
        }

    }
    
    /**
     * 根据条件查询上级分润设置比
     * @param map
     * @return
     */
    public Double queryMouldRulevalueUpper(Map<String,Object> map){
    	Double value = (Double) dao.queryForObject("mould.queryMouldRulevalueUpper", map);
    	if(value!=null){
    		return value;
    	}else{
    		return new Double("0");
    	}
    }
    

}
