package com.compass.agencyCost.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.agencyCost.model.AgencyCost;
import com.compass.agencyCost.model.AgencyCostLog;
import com.compass.common.FrConstants;
import com.compass.constans.InterfaceNameConstans;
import com.compass.paramater.model.RtbParamter;
import com.compass.paramater.service.RtbParamterService;
import com.compass.utils.AbstractService;
import com.imobpay.base.services.FrServer;

/**
 * <pre>
 * 【类型】: AgencyCostService <br/> 
 * 【作用】: 机构成本费率设置. <br/>  
 * 【时间】：2017年2月14日 下午4:31:26 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class AgencyCostService  extends AbstractService {

    
    /**
     * Dubbo分润服务
     */
    private FrServer frServer;
    
    
    /**
     * 参数查询
     */
    private RtbParamterService rtbParamterService;
    
    /**
     * 交易类型
     */
    private PayproductService payproductService;

    /**
     * 查询费率列表
     * @param map Map
     * @return List
     * @throws Exception 抛出异常
     */
    @SuppressWarnings("unchecked")
    public List<AgencyCost> getAllAgencyCostAll(Map<String, Object> map)throws Exception {
        return dao.queryForList("AgencyCost.selectAllAgencyCost", map);
    }
    
    
    /**
     * 查询费率的数量
     * @param map Map
     * @return Integer
     * @throws Exception 抛出异常
     */
    public Integer getAllAgencyCostCountGroup(Map<String, Object> map)throws Exception {
        return (Integer) dao.queryForObject("AgencyCost.selectAgencyCostCountGroup", map);
    }

    /**
     * 查询费率列表
     * @param map Map
     * @return List
     * @throws Exception 抛出异常
     */
    @SuppressWarnings("unchecked")
    public List<AgencyCost> getAllAgencyCostAllGroup(Map<String, Object> map)throws Exception {
        return dao.queryForList("AgencyCost.selectAllAgencyCostGroup", map);
    }
    
    /**
     * 【方法名】    : (查询机构使用中的分润成本). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午4:31:35 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 机构号
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<AgencyCost> queryIsUserAgencyCost(String agencyId) {
        return dao.queryForList("AgencyCost.selectisUseAgencyCost", agencyId);
    }
    
    /**
     * 【方法名】    : (根据AgencyId获取待生效费率记录). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午6:54:05 .<br/>
     * 【参数】： .<br/>
     * @param agencyid String
     * @return List<AgencyCost>.<br/>
     * @throws Exception 抛出异常
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<AgencyCost> queryRegCostDetail(String agencyid)throws Exception {
        return dao.queryForList("AgencyCost.queryRegCostDetail", agencyid);
    }
    
    
    /**
     * 【方法名】    : (根据AgencyId获取生效费率记录). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午6:54:05 .<br/>
     * 【参数】： .<br/>
     * @param agencyid String
     * @return List<AgencyCost>.<br/>
     * @throws Exception .<br/> 
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<AgencyCost> queryCostDetail(String agencyid)throws Exception {
        return dao.queryForList("AgencyCost.queryCostDetail", agencyid);
    }
    
    /**
     * 【方法名】    : (插入生效中的费率). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午9:07:11 .<br/>
     * 【参数】： .<br/>
     * @param cost AgencyCost对象
     * @return Integer
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer insertIsUserCost(AgencyCost cost)throws Exception {
        return dao.insert("AgencyCost.insertIsUser", cost);
    }
    
    /**
     * 【方法名】    : (根据机构Id删除生效中的费率). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午9:10:25 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 机构号
     * @return Integer
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer deleteIsUseWithAgencyId(String agencyId)throws Exception {
        return dao.delete("AgencyCost.deleteIsUseWithAgencyId", agencyId);
    }
    
    /**
     * 【方法名】    : (插入). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午9:24:45 .<br/>
     * 【参数】： .<br/>
     * @param costLog AgencyCostLog
     * @return 受影响的行数
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer insertCostLog(AgencyCostLog costLog)throws Exception {
        return dao.insert("AgencyCost.insertCostLog", costLog);
    }    
     
     /**
      * 【方法名】    : (查询机构分润成本费率). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月20日 下午2:30:26 .<br/>
      * 【参数】： .<br/>
      * @param map 参数集合
      * @return BigDecimal
      * @throws Exception .<br/>
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public BigDecimal queryCostRate(Map<String, Object> map) throws Exception {
        return (BigDecimal) dao.queryForObject("AgencyCost.queryCostRate", map);
    }
     
     /**
      * 【方法名】    : (查询机构成本固定值). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月20日 下午2:31:45 .<br/>
      * 【参数】： .<br/>
      * @param map 参数集合
      * @return BigDecimal
      * @throws Exception .<br/>
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public BigDecimal queryCostFix(Map<String, Object> map) throws Exception {
        return (BigDecimal) dao.queryForObject("AgencyCost.queryCostFix", map);
    }

     /**
      * 【方法名】    : (查询基础费率值). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月20日 下午3:27:36 .<br/>
      * 【参数】： .<br/>
      * @param pmNo 编码值
      * @return BigDecimal
      * @throws Exception .<br/>
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public BigDecimal queryCostRateBase(String pmNo) throws Exception {
        return (BigDecimal) dao.queryForObject("AgencyCost.queryCostRateBase", pmNo);
    }
     
     /**
      * 【方法名】    : (和上级设置的值进行验证). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月21日 下午1:40:28 .<br/>
      * 【参数】： .<br/>
      * @param cost AgencyCost
      * @param upperAgencyId .<br/>
      * @throws Exception .
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public void validateCompareUpper(AgencyCost cost, String upperAgencyId) throws Exception {
        // 必须比上一级分润成本费率高
        Map<String, Object> paramUpper = new HashMap<String, Object>();
        paramUpper.put("agencyId", upperAgencyId);
        paramUpper.put("businessType", cost.getBusinessType());
        BigDecimal upRate = queryCostRate(paramUpper);
        
        String bussType = payproductService.getPayproductDes(cost.getBusinessType());
        if (!StringUtils.isEmpty(upRate)) {
            if (cost.getCostRate().compareTo(upRate) == -1) {
                throw new QTException("设置的"+bussType+"比例数值需大于上级，请核对后重新填写");
            }
        } 

        BigDecimal upFix = queryCostFix(paramUpper);
        if (!StringUtils.isEmpty(upFix)) {
            if (cost.getCostFix().compareTo(upFix) == -1) {
                throw new QTException("设置的"+bussType+"固定值需大于上级，请核对后重新填写");
            }
        }

    }
     
     /**
      * 【方法名】    : (验证生效中的值). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月20日 下午2:56:17 .<br/>
      * 【参数】： .<br/>
      * @param cost 机构成本表实体类
      * @throws Exception .<br/>
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public void validateCostRate(AgencyCost cost) throws Exception {
        // 1、不能低于生效中的值
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("agencyId", cost.getAgencyId());
        param.put("businessType", cost.getBusinessType());
        String bussType = payproductService.getPayproductDes(cost.getBusinessType());
        // 查询出生效中的值
        BigDecimal rate = queryCostRate(param);
        BigDecimal fix = queryCostFix(param);
        if (rate != null) {
            // 生效值判断
            if (cost.getCostRate().compareTo(rate) == 1) {
                throw new QTException("本次配置的"+bussType+"比例数值高于现已生效的成本，平台仅支持向下调价，请核对后重新填写");
            }

        }
        if (fix != null) {
            // 生效值判断
            if (cost.getCostFix().compareTo(fix) == 1) {
                throw new QTException("本次配置的"+bussType+"固定值高于现已生效的成本，平台仅支持向下调价，请核对后重新填写");
            }

        }
        //验证极值情况
        queryParamateValidate(cost);
    }
     
    /**
     * 【方法名】    : 验证参数表极值 <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月6日 下午5:49:22 .<br/>
     * 【参数】： .<br/>
     * @param cost AgencyCost
     * @throws Exception 抛出异常.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    private void queryParamateValidate(AgencyCost cost) throws Exception {
        if (FrConstants.BUSINESSTYPE_3001.equals(cost.getBusinessType())) {
            // 闪付代理商成本费率最小值
            BigDecimal rateMin3001 = getRtbParamterByClassno("AGENCY001");
            if (cost.getCostRate().compareTo(rateMin3001) == -1) {
                throw new QTException("设置的闪付费率最小值小于底价限制，请核对后重新填写");
            }

            // 闪付代理商成本费率最大值
            BigDecimal rateMax3001 = getRtbParamterByClassno("AGENCY005");
            if (cost.getCostRate().compareTo(rateMax3001) == 1) {
                throw new QTException("设置的闪付费率超出底价限制，请核对后重新填写");
            }

            // 闪付代理商成本固定值最小值（分）
            BigDecimal fixMin3001 = getRtbParamterByClassno("AGENCY010");
            if (cost.getCostFix().compareTo(fixMin3001) == -1) {
                throw new QTException("设置的闪付固定值小于底价限制，请核对后重新填写");
            }

            // 闪付代理商成本固定值最大值（分）
            BigDecimal fixMax3001 = getRtbParamterByClassno("AGENCY009");
            if (cost.getCostFix().compareTo(fixMax3001) == 1) {
                throw new QTException("设置的闪付固定值超出底价限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3002.equals(cost.getBusinessType())) {
            // 普通到账成本费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("AGENCY002");
            if (cost.getCostRate().compareTo(rateMin) == -1) {
                throw new QTException("设置的普通到账费率最小值小于底价限制，请核对后重新填写");
            }

            // 普通到账成本费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("AGENCY006");
            if (cost.getCostRate().compareTo(rateMax) == 1) {
                throw new QTException("设置的普通到账费率超出底价限制，请核对后重新填写");
            }

            // 普通到账成本固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("AGENCY012");
            if (cost.getCostFix().compareTo(fixMin) == -1) {
                throw new QTException("设置的普通到账固定值小于底价限制，请核对后重新填写");
            }

            // 普通到账成本固定值最大值（分）
            BigDecimal fixMax = getRtbParamterByClassno("AGENCY011");
            if (cost.getCostFix().compareTo(fixMax) == 1) {
                throw new QTException("设置的普通到账固定值超出底价限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3003.equals(cost.getBusinessType())) {
            // 二维码微信代理商成本费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("AGENCY003");
            if (cost.getCostRate().compareTo(rateMin) == -1) {
                throw new QTException("设置的二维码微信最小值小于底价限制，请核对后重新填写");
            }

            // 二维码微信代理商成本费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("AGENCY007");
            if (cost.getCostRate().compareTo(rateMax) == 1) {
                throw new QTException("设置的二维码微信费率超出底价限制，请核对后重新填写");
            }

            // 二维码微信代理商成本固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("AGENCY014");
            if (cost.getCostFix().compareTo(fixMin) == -1) {
                throw new QTException("设置的二维码微信固定值小于底价限制，请核对后重新填写");
            }

            // 二维码微信代理商成本固定值最大值
            BigDecimal fixMax = getRtbParamterByClassno("AGENCY013");
            if (cost.getCostFix().compareTo(fixMax) == 1) {
                throw new QTException("设置的二维码微信固定值超出底价限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3004.equals(cost.getBusinessType())) {
            // 二维码支付宝代理商成本费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("AGENCY004");
            if (cost.getCostRate().compareTo(rateMin) == -1) {
                throw new QTException("设置的二维码支付宝最小值小于底价限制，请核对后重新填写");
            }

            // 二维码支付宝代理商成本费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("AGENCY008");
            if (cost.getCostRate().compareTo(rateMax) == 1) {
                throw new QTException("设置的二维码支付宝费率超出底价限制，请核对后重新填写");
            }

            // 二维码支付宝代理商成本固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("AGENCY016");
            if (cost.getCostFix().compareTo(fixMin) == -1) {
                throw new QTException("设置的二维码支付宝固定值小于底价限制，请核对后重新填写");
            }

            // 二维码支付宝代理商成本固定值最大值
            BigDecimal fixMax = getRtbParamterByClassno("AGENCY015");
            if (cost.getCostFix().compareTo(fixMax) == 1) {
                throw new QTException("设置的二维码支付宝固定值超出底价限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3006.equals(cost.getBusinessType())) {
         // 云闪付代理商成本费率最小值
            BigDecimal rateMin3006 = getRtbParamterByClassno("AGENCY018");
            if (cost.getCostRate().compareTo(rateMin3006) == -1) {
                throw new QTException("设置的云闪付最小值小于底价限制，请核对后重新填写");
            }

            // 云闪付代理商成本费率最大值
            BigDecimal rateMax3006 = getRtbParamterByClassno("AGENCY017");
            if (cost.getCostRate().compareTo(rateMax3006) == 1) {
                throw new QTException("设置的云闪付费率超出底价限制，请核对后重新填写");
            }

            // 云闪付代理商成本固定值最小值（分）
            BigDecimal fixMin3006 = getRtbParamterByClassno("AGENCY020");
            if (cost.getCostFix().compareTo(fixMin3006) == -1) {
                throw new QTException("设置的云闪付固定值小于底价限制，请核对后重新填写");
            }

            // 云闪付代理商成本固定值最大值（分）
            BigDecimal fixMax3006 = getRtbParamterByClassno("AGENCY019");
            if (cost.getCostFix().compareTo(fixMax3006) == 1) {
                throw new QTException("设置的云闪付固定值超出底价限制，请核对后重新填写");
            }
        }
    }
    
    /**
     * 【方法名】    : (查询参数配置). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月6日 下午5:23:32 .<br/>
     * 【参数】： .<br/>
     * @param pmNo 标识
     * @throws Exception 
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    private BigDecimal getRtbParamterByClassno(String pmNo) throws Exception{
        RtbParamter obj = new RtbParamter();
        obj.setPmNo(pmNo);
        RtbParamter rtbParamter = rtbParamterService.getRtbParamter(obj);
        if (rtbParamter == null) {
            throw new QTException("参数表未配置最大最小值!");
        }
        return rtbParamter.getPmValue();
    }
    
     /**
      * 【方法名】    : (查询成本费率对象). <br/> 
      * 【作者】: yinghui zhang .<br/>
      * 【时间】： 2017年2月21日 上午11:35:35 .<br/>
      * 【参数】： .<br/>
      * @param map Map<String, Object>
      * @return AgencyCost
      * @throws Exception .<br/>
      * <p>
      * 修改记录.<br/>
      * 修改人:  yinghui zhang 修改描述： .<br/>
      * <p/>
      */
    public AgencyCost queryAgencyCostObj(Map<String, Object> map) throws Exception {
        return (AgencyCost) dao.queryForObject("AgencyCost.queryAgencyCostObj", map);
    }
    
    /**
     * 【方法名】    : (更新agencyCost). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月22日 下午3:33:28 .<br/>
     * 【参数】： .<br/>
     * @param agencyCost AgencyCost
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int updateAgencyCost(AgencyCost agencyCost){
        return dao.update("AgencyCost.updateAgencyCost", agencyCost);
    }
    
    /**
     * 【方法名】    : (更新记录次数). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月24日 上午11:08:36 .<br/>
     * 【参数】： .<br/>
     * @return int
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int updateAgencyCostTimes()throws Exception{
        return dao.update("AgencyCost.updateAgencyCostTimes", null);
    }
    
    /**
     * 【方法名】    : (验证修改分润次数). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月30日 下午8:06:49 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @return JSONObject
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public JSONObject CheckSetProfitCost(String agencyId) throws Exception {
        JSONObject item = new JSONObject();
        item.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.SET_PROFITCOST_CHECK);
        item.put(InterfaceNameConstans.AGENCY_ID, agencyId);
        String execute = frServer.execute(item.toJSONString());
        return JSONObject.parseObject(execute);
    }
    
    /**
     * 
     * 【方法名】    : (dubbo服务注入). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年6月29日 下午3:20:27 .<br/>
     * 【参数】： .<br/>
     * @param frServer dubbo服务.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public void setFrServer(FrServer frServer) {
        this.frServer = frServer;
    }
    /**
     * 
     * 【方法名】    : (RtbParamterService注入). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年6月29日 下午3:21:23 .<br/>
     * 【参数】： .<br/>
     * @param rtbParamterService 参数查询service.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public void setRtbParamterService(RtbParamterService rtbParamterService) {
        this.rtbParamterService = rtbParamterService;
    }

    /**
     * 
     * 【方法名】    : (查询机构成本的费率值). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年6月29日 下午3:16:57 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 机构ID
     * @param businessType 产品类型编码
     * @return BigDecimal.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public BigDecimal queryCostRate(String agencyId, String businessType){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("businessType", businessType);
        return (BigDecimal) dao.queryForObject("AgencyCost.queryCostRate", map);
    }

    /**
     * 
     * 【方法名】    : (查询机构成本的固定值). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年6月29日 下午3:16:57 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 机构ID
     * @param businessType 产品类型编码
     * @return BigDecimal.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public BigDecimal queryCostFix(String agencyId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("businessType", businessType);
        return (BigDecimal) dao.queryForObject("AgencyCost.queryCostFix", map);
    }
    
    /**
     * 注入payproductService
     * @param payproductService 交易类型
     * 
     */
    public void setPayproductService(PayproductService payproductService) {
        this.payproductService = payproductService;
    } 
    
    
    
}
