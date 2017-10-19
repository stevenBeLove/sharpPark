package com.compass.pasmFee.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ardu.jms.ems.exception.QTException;

import com.compass.agencyCost.service.AgencyCostService;
import com.compass.common.FrConstants;
import com.compass.paramater.model.RtbParamter;
import com.compass.paramater.service.RtbParamterService;
import com.compass.pasmFee.model.PsmFee;
import com.compass.utils.AbstractService;
import com.compass.utils.mvc.I18nUtils;

public class PsmFeeService extends AbstractService {

    /**
     * 参数查询
     */
    private RtbParamterService rtbParamterService;

    private AgencyCostService  agencyCostService;

    /**
     * 查询费率的数量
     * 
     * @param agency
     * @return
     */
    public Integer getAllPsmFeeCount(String agencyId, String startPsamId, String endPsamId, String businesstype, String termstatus, String shopno, String fixFee, String terminalstatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("startPsamId", startPsamId);
        map.put("endPsamId", endPsamId);
        map.put("businesstype", businesstype);
        map.put("termstatus", termstatus);
        map.put("shopno", shopno);
        map.put("fixFee", fixFee);
        map.put("terminalstatus", terminalstatus);
        return (Integer) dao.queryForObject("PsmFee.selectAllPsmFeeCount", map);
    }

    /**
     * 查询费率列表
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PsmFee> getPsmFeeAll(String agencyId, String startPsamId, String endPsamId, String businesstype, String termstatus, String shopno, String fixFee, String terminalstatus, int start,
            int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("startPsamId", startPsamId);
        map.put("endPsamId", endPsamId);
        map.put("businesstype", businesstype);
        map.put("termstatus", termstatus);
        map.put("shopno", shopno);
        map.put("fixFee", fixFee);
        map.put("start", start);
        map.put("end", end);
        map.put("terminalstatus", terminalstatus);
        return dao.queryForList("PsmFee.selectAllPsmFee", map);
    }

    /**
     * 查询交易类型基本费率列表
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PsmFee> getBusnessFeebyAgencyId(String agencyId) {
        return dao.queryForList("PsmFee.selectBusnessFeebyAgencyId");
    }

    /**
     * 查询交易类型基本费率列表数量
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public Integer getBusnessFeebyAgencyIdCount(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (Integer) dao.queryForObject("PsmFee.selectBusnessFeebyAgencyIdCount", map);
    }

    /**
     * 查询机构费率成本数量
     * 
     * @param agency
     * @return
     */
    public Integer getFeeByAgencyIdCount(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (Integer) dao.queryForObject("PsmFee.selectFeeByAgencyIdCount", map);
    }

    /**
     * 查询机构费率成本
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PsmFee> getFeeByAgencyId(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return dao.queryForList("PsmFee.selectFeeByAgencyId", map);
    }

    /**
     * 设置终端费率
     * 
     * @param agency
     * @return
     */
    public Integer updatePsmFeebyPsam(String agencyId, String startPSAMId, String endPSAMId, String shopno, String fixFee, String userName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("startPSAMId", startPSAMId);
        map.put("endPSAMId", endPSAMId);
        map.put("shopno", shopno);
        map.put("fixFee", fixFee);
        map.put("userName", userName);
        // 验证成本
        validateCostRate(agencyId, shopno, fixFee);
        // 验证最大最小值
        queryParamateValidate(FrConstants.BUSINESSTYPE_3001, shopno, fixFee);
        return dao.update("PsmFee.UpdatePsmFeebyPsam", map);
    }

    /**
     * 修改终端费率1闪电到账
     * 
     * @param agency
     * @return
     */
    public Integer updatePsmFeebyPsamUP(String agencyId, String shopno, String fixFee, String psamId, String userName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("psamId", psamId);
        map.put("shopno", shopno);
        map.put("fixFee", fixFee);
        map.put("userName", userName);
        // 验证成本
        validateCostRate(agencyId, shopno, fixFee);
        // 验证最大最小值
        queryParamateValidate(FrConstants.BUSINESSTYPE_3001, shopno, fixFee);
        return dao.update("PsmFee.UpdatePsmFeebyPsamUP", map);
    }

    /**
     * 修改终端费率2普通到账
     * 
     * @param agency
     * @return
     */
    public Integer updatePsmFeebyPsamUPT(String agencyId, String shopnop, String fixFeep, String psamId, String userName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("psamId", psamId);
        map.put("shopnop", shopnop);
        map.put("fixFeep", fixFeep);
        map.put("userName", userName);
        // 验证成本
        validateCostRateCommon(agencyId, shopnop, fixFeep);
        // 验证最大最小值
        queryParamateValidate(FrConstants.BUSINESSTYPE_3002, shopnop, fixFeep);
        return dao.update("PsmFee.UpdatePsmFeebyPsamUPT", map);
    }

    /**
     * 【方法名】 : (验证生效中的值). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月20日 下午2:56:17 .<br/>
     * 【参数】： .<br/>
     * 
     * @param cost
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public void validateCostRate(String agencyId, String shopno, String fixFee) throws Exception {
        // 1、不能低于生效中的值
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("agencyId", agencyId);
        param.put("businessType", FrConstants.BUSINESSTYPE_3001);

        // 查询出生效中的值
        BigDecimal rate = agencyCostService.queryCostRate(param);
        BigDecimal fix = agencyCostService.queryCostFix(param);

        if (rate != null) {
            // 生效值判断
            if (new BigDecimal(shopno).compareTo(rate.multiply(new BigDecimal("100"))) == -1) {
                throw new QTException("闪电到账的费率百分比低于成本设置，请核对后重新填写!");
            }

        }
        if (fix != null) {
            // 生效值判断
            if (new BigDecimal(fixFee).compareTo(fix.divide(new BigDecimal("100"))) == -1) {
                throw new QTException("闪电到账的费率固定值低于成本设置，请核对后重新填写!");
            }

        }
    }

    /**
     * 
     * 【方法名】 : 判断普通到账的成本. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月8日 下午3:31:46 .<br/>
     * 【参数】： .<br/>
     * 
     * @param agencyId
     * @param shopno
     * @param fixFee
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: 张柯. 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public void validateCostRateCommon(String agencyId, String shopnop, String fixFeep) throws Exception {
        // 1、不能低于生效中的值
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("agencyId", agencyId);
        param.put("businessType", FrConstants.BUSINESSTYPE_3002);

        // 查询出生效中的值
        BigDecimal rate = agencyCostService.queryCostRate(param);
        BigDecimal fix = agencyCostService.queryCostFix(param);

        if (rate != null) {
            // 生效值判断
            if (new BigDecimal(shopnop).compareTo(rate.multiply(new BigDecimal("100"))) == -1) {
                throw new QTException("普通到账的费率百分比低于成本设置，请核对后重新填写!");
            }

        }
        if (fix != null) {
            // 生效值判断
            if (new BigDecimal(fixFeep).compareTo(fix.divide(new BigDecimal("100"))) == -1) {
                throw new QTException("普通到账的费率固定值低于成本设置，请核对后重新填写!");
            }

        }
    }

    /**
     * 【方法名】 : 验证参数表极值 . <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月7日 下午2:03:10 .<br/>
     * 【参数】： .<br/>
     * 
     * @param businessType
     *            交易类型
     * @param shopno
     *            费率
     * @param fixFee
     *            固定值
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    private void queryParamateValidate(String businessType, String shopno, String fixFee) throws Exception {
        BigDecimal shopnoValue = new BigDecimal(shopno).divide(new BigDecimal("100"));
        BigDecimal fixFeeValue = new BigDecimal(fixFee).multiply(new BigDecimal("100"));
        if (FrConstants.BUSINESSTYPE_3001.equals(businessType)) {
            // 闪付终端费率费率最小值
            BigDecimal rateMin3001 = getRtbParamterByClassno("TERM001");
            if (shopnoValue.compareTo(rateMin3001) == -1) {
                throw new QTException("设置的闪付费率小于底价最小值限制，请核对后重新填写");
            }

            // 闪付终端费率费率最大值
            BigDecimal rateMax3001 = getRtbParamterByClassno("TERM005");
            if (shopnoValue.compareTo(rateMax3001) == 1) {
                throw new QTException("设置的闪付费率超出底价最大值限制，请核对后重新填写");
            }

            // 闪付终端费率固定值最小值（分）
            BigDecimal fixMin3001 = getRtbParamterByClassno("TERM010");
            if (fixFeeValue.compareTo(fixMin3001) == -1) {
                throw new QTException("设置的闪付固定值小于底价最小值限制，请核对后重新填写");
            }

            // 闪付终端费率固定值最大值（分）
            BigDecimal fixMax3001 = getRtbParamterByClassno("TERM009");
            if (fixFeeValue.compareTo(fixMax3001) == 1) {
                throw new QTException("设置的闪付固定值超出底价最大值限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3002.equals(businessType)) {
            // 普通到账终端费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("TERM002");
            if (shopnoValue.compareTo(rateMin) == -1) {
                throw new QTException("设置的普通到账费率小于底价最小值限制，请核对后重新填写");
            }

            // 普通到账终端费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("TERM006");
            if (shopnoValue.compareTo(rateMax) == 1) {
                throw new QTException("设置的普通到账费率超出底价最大值限制，请核对后重新填写");
            }

            // 普通到账成本固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("TERM012");
            if (fixFeeValue.compareTo(fixMin) == -1) {
                throw new QTException("设置的普通到账固定值小于底价最小值限制，请核对后重新填写");
            }

            // 普通到账成本固定值最大值（分）
            BigDecimal fixMax = getRtbParamterByClassno("TERM011");
            if (fixFeeValue.compareTo(fixMax) == 1) {
                throw new QTException("设置的普通到账固定值超出底价最大值限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3003.equals(businessType)) {
            // 二维码微信终端费率费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("TERM003");
            if (shopnoValue.compareTo(rateMin) == -1) {
                throw new QTException("设置的二维码微信最小值小于底价限制，请核对后重新填写");
            }

            // 二维码微信终端费率费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("TERM007");
            if (shopnoValue.compareTo(rateMax) == 1) {
                throw new QTException("设置的二维码微信费率超出底价限制，请核对后重新填写");
            }

            // 二维码微信终端费率固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("TERM014");
            if (fixFeeValue.compareTo(fixMin) == -1) {
                throw new QTException("设置的二维码微信固定值小于底价限制，请核对后重新填写");
            }

            // 二维码微信终端费率固定值最大值
            BigDecimal fixMax = getRtbParamterByClassno("TERM013");
            if (fixFeeValue.compareTo(fixMax) == 1) {
                throw new QTException("设置的二维码微信固定值超出底价限制，请核对后重新填写");
            }
        } else if (FrConstants.BUSINESSTYPE_3004.equals(businessType)) {
            // 二维码支付宝终端费率费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("TERM004");
            if (shopnoValue.compareTo(rateMin) == -1) {
                throw new QTException("设置的二维码支付宝最小值小于底价限制，请核对后重新填写");
            }

            // 二维码支付宝终端费率费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("TERM008");
            if (shopnoValue.compareTo(rateMax) == 1) {
                throw new QTException("设置的二维码支付宝费率超出底价限制，请核对后重新填写");
            }

            // 二维码支付宝终端费率固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("TERM016");
            if (fixFeeValue.compareTo(fixMin) == -1) {
                throw new QTException("设置的二维码支付宝固定值小于底价限制，请核对后重新填写!");
            }

            // 二维码支付宝终端费率固定值最大值
            BigDecimal fixMax = getRtbParamterByClassno("TERM015");
            if (fixFeeValue.compareTo(fixMax) == 1) {
                throw new QTException("设置的二维码支付宝固定值超出底价限制，请核对后重新填写!");
            }
        } else if (FrConstants.BUSINESSTYPE_3006.equals(businessType)) {
            // 云闪付终端费率费率最小值
            BigDecimal rateMin = getRtbParamterByClassno("TERM018");
            if (shopnoValue.compareTo(rateMin) == -1) {
                throw new QTException("设置的云闪付最小值小于底价限制，请核对后重新填写");
            }

            // 云闪付终端费率费率最大值
            BigDecimal rateMax = getRtbParamterByClassno("TERM017");
            if (shopnoValue.compareTo(rateMax) == 1) {
                throw new QTException("设置的云闪付费率超出底价限制，请核对后重新填写");
            }

            // 云闪付终端费率固定值最小值（分）
            BigDecimal fixMin = getRtbParamterByClassno("TERM020");
            if (fixFeeValue.compareTo(fixMin) == -1) {
                throw new QTException("设置的云闪付固定值小于底价限制，请核对后重新填写!");
            }

            // 云闪付终端费率固定值最大值
            BigDecimal fixMax = getRtbParamterByClassno("TERM019");
            if (fixFeeValue.compareTo(fixMax) == 1) {
                throw new QTException("设置的云闪付固定值超出底价限制，请核对后重新填写!");
            }
        }
    }

    /**
     * 【方法名】 : (查询参数配置). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月6日 下午5:23:32 .<br/>
     * 【参数】： .<br/>
     * 
     * @param classNo
     *            标识
     * @throws Exception
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public BigDecimal getRtbParamterByClassno(String pmNo) throws Exception {
        RtbParamter obj = new RtbParamter();
        obj.setPmNo(pmNo);
        RtbParamter rtbParamter = rtbParamterService.getRtbParamter(obj);
        if (rtbParamter == null) {
            throw new QTException("参数表未配置最大最小值!");
        }
        return rtbParamter.getPmValue();
    }

    /**
     * 插入终端费率历史表
     * 
     * @param agency
     * @return
     */
    public Integer InsertPsamFeeHis(String agencyId, String startPSAMId, String endPSAMId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("startPSAMId", startPSAMId);
        map.put("endPSAMId", endPSAMId);
        return dao.update("PsmFee.insertRtb_his_psam_fee", map);
    }

    /**
     * 获取终端费率最小值
     * 
     * @param agency
     * @return
     */
    public String getParamterforFeeMin(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (String) dao.queryForObject("PsmFee.selectParamterforFeeMin", map);
    }

    /**
     * 获取终端费率最小值
     * 
     * @param agency
     * @return
     */
    public Integer getParamterforFixMin(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (Integer) dao.queryForObject("PsmFee.selectParamterforFixMin", map);
    }

    /**
     * 获取终端费率最大值
     * 
     * @param agency
     * @return
     */
    public String getParamterforFeeMax(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (String) dao.queryForObject("PsmFee.selectParamterforFeeMax", map);
    }

    /**
     * 获取终端费率最大值
     * 
     * @param agency
     * @return
     */
    public Integer getParamterforFixMax(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return (Integer) dao.queryForObject("PsmFee.selectParamterforFixMax", map);
    }

    public void setRtbParamterService(RtbParamterService rtbParamterService) {
        this.rtbParamterService = rtbParamterService;
    }

    public void setAgencyCostService(AgencyCostService agencyCostService) {
        this.agencyCostService = agencyCostService;
    }

    /**
     * 
     * 方法名： selectPsamFeeFeerate(查询终端费率值).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午1:24:48.<br/>
     * 参数者异常：@param psamId 参数者异常：@param businessType 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public BigDecimal selectPsamFeeFeerate(String psamId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        map.put("businessType", businessType);
        return (BigDecimal) dao.queryForObject("PsmFee.selectPsamFeeFeerate", map);
    }

    /**
     * 
     * 方法名： selectPsamFeeFeerate(查询终端交易固定值).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午1:24:48.<br/>
     * 参数者异常：@param psamId 参数者异常：@param businessType 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public BigDecimal selectPsamFeeFixFee(String psamId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        map.put("businessType", businessType);
        return (BigDecimal) dao.queryForObject("PsmFee.selectPsamFeeFixFee", map);
    }

    /**
     * 
     * 方法名： selectAgencyFeerate(查询所属机构终端费率最小值(批量)).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午5:09:18.<br/>
     * 参数者异常：@param agencyIdS 参数者异常：@param systemSource 参数者异常：@param startPsamId 参数者异常：@param endPsamId 参数者异常：@param businessType 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public PsmFee selectAgencyFeerate(String agencyIdS, String systemSource, String startPsamId, String endPsamId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyIdS);
        map.put("systemSource", systemSource);
        map.put("startPsamId", startPsamId);
        map.put("endPsamId", endPsamId);
        map.put("businessType", businessType);
        return (PsmFee) dao.queryForObject("PsmFee.selectAgencyFeerate", map);
    }

    /**
     * 
     * 方法名： selectAgencyFixFee(查询所属机构终端固定值最小值(批量)).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午5:10:15.<br/>
     * 参数者异常：@param agencyIdS 参数者异常：@param systemSource 参数者异常：@param startPsamId 参数者异常：@param endPsamId 参数者异常：@param businessType 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public PsmFee selectAgencyFixFee(String agencyIdS, String systemSource, String startPsamId, String endPsamId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyIdS);
        map.put("systemSource", systemSource);
        map.put("startPsamId", startPsamId);
        map.put("endPsamId", endPsamId);
        map.put("businessType", businessType);
        return (PsmFee) dao.queryForObject("PsmFee.selectAgencyFixFee", map);
    }

    /**
     * 
     * 方法名： queryCostRate(普通下发终端比较下发机构费率).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午12:53:44.<br/>
     * 参数者异常：@param agencyId 参数者异常：@param psamId 参数者异常：@throws Exception .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void queryCostRate(String agencyId, String psamId) throws Exception {
        // 查询出机构的闪付值
        BigDecimal agencyRate3001 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3001);
        BigDecimal agencyFix3001 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3001);
        // 查询出机构的普通到账值
        BigDecimal agencyRate3002 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3002);
        BigDecimal agencyFix3002 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3002);
        // 查询出机构的二维码微信值
        BigDecimal agencyRate3003 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3003);
        BigDecimal agencyFix3003 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3003);
        // 查询出机构的二维码支付宝值
        BigDecimal agencyRate3004 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3004);
        BigDecimal agencyFix3004 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3004);
        // 判断机构成本是否为空
        if (agencyRate3001 == null || agencyRate3002 == null || agencyRate3003 == null || agencyRate3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryAgencyRate.error"));
        }
        if (agencyFix3001 == null || agencyFix3002 == null || agencyFix3003 == null || agencyFix3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryAgencyFixFee.error"));
        }

        // 查询终端闪付值
        BigDecimal pasmRate3001 = selectPsamFeeFeerate(psamId, FrConstants.BUSINESSTYPE_3001);
        BigDecimal pasmFix3001 = selectPsamFeeFixFee(psamId, FrConstants.BUSINESSTYPE_3001);
        // 查询终端普通到账值
        BigDecimal pasmRate3002 = this.selectPsamFeeFeerate(psamId, FrConstants.BUSINESSTYPE_3002);
        BigDecimal pasmFix3002 = this.selectPsamFeeFixFee(psamId, FrConstants.BUSINESSTYPE_3002);
        // 查询终端二维码微信值
        BigDecimal pasmRate3003 = this.selectPsamFeeFeerate(psamId, FrConstants.BUSINESSTYPE_3003);
        BigDecimal pasmFix3003 = this.selectPsamFeeFixFee(psamId, FrConstants.BUSINESSTYPE_3003);
        // 查询终端二维码支付宝值
        BigDecimal pasmRate3004 = this.selectPsamFeeFeerate(psamId, FrConstants.BUSINESSTYPE_3004);
        BigDecimal pasmFix3004 = this.selectPsamFeeFixFee(psamId, FrConstants.BUSINESSTYPE_3004);
        // 判断终端成本是否为空
        if (pasmRate3001 == null || pasmRate3002 == null || pasmRate3003 == null || pasmRate3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryFeerate.error"));
        }
        if (pasmFix3001 == null || pasmFix3002 == null || pasmFix3003 == null || pasmFix3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryFixFee.error"));
        }

        // 终端比较下发机构费率
        if (agencyRate3001 != null && pasmRate3001 != null) {
            // 生效值判断
            if (agencyRate3001.compareTo(pasmRate3001) == 1) {
                throw new QTException("终端" + psamId + "闪付费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3001 != null && pasmFix3001 != null) {
            // 生效值判断
            if (agencyFix3001.compareTo(pasmFix3001) == 1) {
                throw new QTException("终端" + psamId + "闪付固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyRate3002 != null && pasmRate3002 != null) {
            // 生效值判断
            if (agencyRate3002.compareTo(pasmRate3002) == 1) {
                throw new QTException("终端" + psamId + "普通到账费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3002 != null && pasmFix3002 != null) {
            // 生效值判断
            if (agencyFix3002.compareTo(pasmFix3002) == 1) {
                throw new QTException("终端" + psamId + "普通到账固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyRate3003 != null && pasmRate3003 != null) {
            // 生效值判断
            if (agencyRate3003.compareTo(pasmRate3003) == 1) {
                throw new QTException("终端" + psamId + "二维码微信费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3003 != null && pasmFix3003 != null) {
            // 生效值判断
            if (agencyFix3003.compareTo(pasmFix3003) == 1) {
                throw new QTException("终端" + psamId + "二维码微信固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyRate3004 != null && pasmRate3004 != null) {
            // 生效值判断
            if (agencyRate3004.compareTo(pasmRate3004) == 1) {
                throw new QTException("终端" + psamId + "二维码支付宝费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3004 != null && pasmFix3004 != null) {
            // 生效值判断
            if (agencyFix3004.compareTo(pasmFix3004) == 1) {
                throw new QTException("终端" + psamId + "二维码支付宝固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
    }

    /**
     * 
     * 方法名： selectCostRate(批量下发终端比较下发机构费率).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月10日.<br/>
     * 创建时间：下午5:31:11.<br/>
     * 参数者异常：@param agencyId 参数者异常：@param agencyIdS 参数者异常：@param systemSource 参数者异常：@param startPsamId 参数者异常：@param endPsamId 参数者异常：@throws Exception .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public void selectCostRate(String agencyId, String agencyIdS, String systemSource, String startPsamId, String endPsamId) throws Exception {
        // 查询出机构的闪付值
        BigDecimal agencyRate3001 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3001);
        BigDecimal agencyFix3001 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3001);
        // 查询出机构的普通到账值
        BigDecimal agencyRate3002 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3002);
        BigDecimal agencyFix3002 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3002);
        // 查询出机构的二维码微信值
        BigDecimal agencyRate3003 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3003);
        BigDecimal agencyFix3003 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3003);
        // 查询出机构的二维码支付宝值
        BigDecimal agencyRate3004 = agencyCostService.queryCostRate(agencyId, FrConstants.BUSINESSTYPE_3004);
        BigDecimal agencyFix3004 = agencyCostService.queryCostFix(agencyId, FrConstants.BUSINESSTYPE_3004);
        // 判断机构成本是否为空
        if (agencyRate3001 == null || agencyRate3002 == null || agencyRate3003 == null || agencyRate3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryAgencyRate.error"));
        }
        if (agencyFix3001 == null || agencyFix3002 == null || agencyFix3003 == null || agencyFix3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryAgencyFixFee.error"));
        }
        // 查询终端闪付值
        PsmFee pasmRate3001 = selectAgencyFeerate(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3001);
        PsmFee pasmFix3001 = selectAgencyFixFee(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3001);
        // 查询终端普通到账值
        PsmFee pasmRate3002 = selectAgencyFeerate(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3002);
        PsmFee pasmFix3002 = selectAgencyFixFee(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3002);
        // 查询终端二维码微信值
        PsmFee pasmRate3003 = selectAgencyFeerate(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3003);
        PsmFee pasmFix3003 = selectAgencyFixFee(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3003);
        // 查询终端二维码支付宝值
        PsmFee pasmRate3004 = selectAgencyFeerate(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3004);
        PsmFee pasmFix3004 = selectAgencyFixFee(agencyIdS, systemSource, startPsamId, endPsamId, FrConstants.BUSINESSTYPE_3004);
        // 判断终端成本是否为空
        if (pasmRate3001 == null || pasmRate3002 == null || pasmRate3003 == null || pasmRate3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryFeerate.error"));
        }
        if (pasmFix3001 == null || pasmFix3002 == null || pasmFix3003 == null || pasmFix3004 == null) {
            throw new QTException(I18nUtils.getResourceValue("message.queryFixFee.error"));
        }

        // 终端比较下发机构费率
        if (agencyRate3001 != null && pasmRate3001.getCostrate() != null) {
            // 生效值判断
            if (agencyRate3001.compareTo(new BigDecimal(pasmRate3001.getCostrate())) == 1) {
                throw new QTException("终端" + pasmRate3001.getPsamid() + "闪付费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3001 != null && pasmFix3001.getFixFee() != null) {
            // 生效值判断
            if (agencyFix3001.compareTo(new BigDecimal(pasmFix3001.getFixFee())) == 1) {
                throw new QTException("终端" + pasmFix3001.getPsamid() + "闪付固定值小于该机构成本设置，请核对后重新选择!");
            }
        }

        if (agencyRate3002 != null && pasmRate3002 != null) {
            // 生效值判断
            if (agencyRate3002.compareTo(new BigDecimal(pasmRate3002.getCostrate())) == 1) {
                throw new QTException("终端" + pasmRate3002.getPsamid() + "普通到账费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3002 != null && pasmFix3002.getFixFee() != null) {
            // 生效值判断
            if (agencyFix3002.compareTo(new BigDecimal(pasmFix3002.getFixFee())) == 1) {
                throw new QTException("终端" + pasmFix3002.getPsamid() + "普通到账固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyRate3003 != null && pasmRate3003.getCostrate() != null) {
            // 生效值判断
            if (agencyRate3003.compareTo(new BigDecimal(pasmRate3003.getCostrate())) == 1) {
                throw new QTException("终端" + pasmRate3003.getPsamid() + "二维码微信费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3003 != null && pasmFix3003.getFixFee() != null) {
            // 生效值判断
            if (agencyFix3003.compareTo(new BigDecimal(pasmFix3003.getFixFee())) == 1) {
                throw new QTException("终端" + pasmFix3003.getPsamid() + "二维码微信固定值小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyRate3004 != null && pasmRate3004.getCostrate() != null) {
            // 生效值判断
            if (agencyRate3004.compareTo(new BigDecimal(pasmRate3004.getCostrate())) == 1) {
                throw new QTException("终端" + pasmRate3004.getPsamid() + "二维码支付宝费率百分比小于该机构成本设置，请核对后重新选择!");
            }
        }
        if (agencyFix3004 != null && pasmFix3004.getFixFee() != null) {
            // 生效值判断
            if (agencyFix3004.compareTo(new BigDecimal(pasmFix3004.getFixFee())) == 1) {
                throw new QTException("终端" + pasmFix3004.getPsamid() + "二维码支付宝固定值小于该机构成本设置，请核对后重新选择!");
            }
        }

    }

    // 查询终端所属机构
    public PsmFee selPsmFeebyPasmIdBranch(String psamId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        return (PsmFee) dao.queryForObject("PsmFee.selectBranch", map);
    }

    // 查询终端所属机构
    public List<PsmFee> getBusnessFeebyAgIdPsId(String agencyId, String psamId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("psamId", psamId);
        map.put("agencyId", agencyId);
        return dao.queryForList("PsmFee.selectShopNoRate", map);
    }

    public int updatePsmFeebyPsamId(String agencyId, String shopno, String fixFee, String psamId, String userName, String shopnop, String fixFeep) throws Exception {
        int result = this.updatePsmFeebyPsamUP(agencyId, shopno, fixFee, psamId, userName);
        int results = this.updatePsmFeebyPsamUPT(agencyId, shopnop, fixFeep, psamId, userName);
        return result * results;
    }

}
