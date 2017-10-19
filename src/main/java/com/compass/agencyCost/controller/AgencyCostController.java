package com.compass.agencyCost.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.agencyCost.model.AgencyCost;
import com.compass.agencyCost.model.Payproduct;
import com.compass.agencyCost.service.AgencyCostService;
import com.compass.agencyCost.service.PayproductService;
import com.compass.common.FrConstants;
import com.compass.paramater.service.RtbParamterService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.utils.mvc.I18nUtils;

@Controller
@RequestMapping("/cost/agencyCost.do")
public class AgencyCostController {

    /**
     * 系统日志service
     */
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService   systemLogService;
    
    /**
     * 分润成本设置
     */
    @Autowired
    @Qualifier("agencyCostService")
    private AgencyCostService   agencyCostService;
    
    /**
     * 交易类型
     */
    @Autowired
    @Qualifier("payproductService")
    private PayproductService payproductService;
    
    /**
     * 参数配置
     */
    @Autowired
    @Qualifier("rtbParamterService")
    private RtbParamterService rtbParamterService;
    
    
    
    private static Logger   logger = LoggerFactory.getLogger(AgencyCostService.class);
    

    
    /**
     * 【方法名】    : (机构分润成本查询). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午3:26:40 .<br/>
     * 【参数】： .<br/>
     * @param agency_id 客户id
     * @param status 状态
     * @param req HttpServletRequest
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=getCostByGroup")
    @ResponseBody
    public Map<String, Object> getCostByGroup(String agency_id, String status, HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getRemoteAddr();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        String rows = req.getParameter("rows");
        String page = req.getParameter("page");

        int pagenumber = Integer.parseInt((page == null || page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        
        Integer count;
        List<AgencyCost> list;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isEmpty(agency_id)) {
                map.put("agency_id", agency_id);
            }
            map.put("onlinechannel", agencyIdS);
            map.put("status", status);
            count = agencyCostService.getAllAgencyCostCountGroup(map);
            int end = (start + rownumber) > count ? count : start + rownumber;
            map.put("start", start);
            map.put("end", end);
            list = agencyCostService.getAllAgencyCostAllGroup(map);
            //记录查询日志 
            String operateDetail = "查询条件为" + "status="+status+ ",";
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER, operateDetail);
            return AjaxReturnInfo.setTable(count, list);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    
    
    /**
     * 【方法名】    : (查询所有瑞通宝类型). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月16日 下午3:14:14 .<br/>
     * 【参数】： .<br/>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params ="method=agencyCostView")
    public ModelAndView forwardCostView(HttpServletRequest req) {
      Map<String, Object> paraMap = new HashMap<String, Object>();
      String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
    try {
          //本级生效中的费率
          List<AgencyCost>  costList = agencyCostService.queryIsUserAgencyCost(agencyIdS);
          for (AgencyCost agencyCost : costList) {
              agencyCost.setCostRate(agencyCost.getCostRate().multiply(new BigDecimal("100")).setScale(3,BigDecimal.ROUND_DOWN));
              agencyCost.setCostFix(agencyCost.getCostFix().divide(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN));
          }
          List<Payproduct> typeList = payproductService.getAllPayproduct();
          paraMap.put("rtbTypeList", typeList);
          paraMap.put("costList", costList);
    } catch (Exception e) {
        logger.error(e.getMessage(),e);
    }
      return new ModelAndView("/agencyCost/agencyCost",paraMap);
    }
    
    
    /**
     * 【方法名】    : (保存成本). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月16日 下午4:21:47 .<br/>
     * 【参数】： .<br/>
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params ="method=saveAgencyCost")
    @ResponseBody
    public AjaxReturnInfo saveAgencyCost(HttpServletRequest req){
        AjaxReturnInfo ajaxInfo  = AjaxReturnInfo.failed("保存失败！");
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        try {
            String agencyid = req.getParameter("agencyid");
           
            List<Payproduct> typeList = payproductService.getAllPayproduct();
            String operDt = CommonDate.currentDateWithFormatStr(CommonDate.YYYYMMDDHHMMSS);

            List<AgencyCost> agencyCostList = new ArrayList<AgencyCost>();
            //循环验证
            for (Payproduct payproduct : typeList) {
                AgencyCost cost = new AgencyCost();
                //代理商
                cost.setAgencyId(agencyid);
                //业务类型
                cost.setBusinessType(payproduct.getRtbType());
                String costRate = req.getParameter(payproduct.getRtbType()+"costRate");
                if(StringUtils.isEmpty(costRate)){
                    return ajaxInfo = AjaxReturnInfo.failed(payproduct.getProductdesc()+"不能为空!");
                }
                String costFix = req.getParameter(payproduct.getRtbType()+"costFix");
                if(StringUtils.isEmpty(costFix)){
                    return ajaxInfo = AjaxReturnInfo.failed(payproduct.getProductdesc()+"不能为空!");
                }
                BigDecimal rate = new BigDecimal(costRate);
                //百分比除以100
                BigDecimal div100 = new BigDecimal("100");
                //费率
                cost.setCostRate(rate.divide(div100));
                //单笔
                cost.setCostFix(new BigDecimal(costFix).multiply(new BigDecimal("100")));
                //创建人
                cost.setOper(userId);
                //创建日期
                cost.setOperdt(operDt);
                //和上级设置的值比较
                if (!ConstantUtils.CENTERCODE.equals(agencyIdS)){
                    agencyCostService.validateCompareUpper(cost, agencyIdS);
                }
                //验证机构生效中的费率
                agencyCostService.validateCostRate(cost);
                agencyCostList.add(cost);
            }
            //删除待生效记录
//          Map<String, Object> deleteMap = new HashMap<String, Object>();
//          deleteMap.put("agencyId", agencyid);
//          deleteMap.put("operdt", operDt);
//          agencyCostService.deleteRegWithAgencyId(deleteMap);
            //验证当月修改次数
            JSONObject jsonObj = agencyCostService.CheckSetProfitCost(agencyid);
            if (!ConstantUtils.MSG_SUCCESS.equals(jsonObj.get(ConstantUtils.MSG_CODE))) {
                throw new QTException((String) jsonObj.get(ConstantUtils.MSG_TEXT));
            }
            List<AgencyCost> isUserCostList = agencyCostService.queryIsUserAgencyCost(agencyid);
            if (isUserCostList == null || isUserCostList.size() == 0 ) {
                for (AgencyCost agencyCost : agencyCostList) {
                    //保存记录到生效表
                    agencyCostService.insertIsUserCost(agencyCost);
                }
            } else {
                for (AgencyCost agencyCost : agencyCostList) {
                    //更新成本
                    agencyCostService.updateAgencyCost(agencyCost);
                }
            }
            ajaxInfo=  AjaxReturnInfo.success(I18nUtils.getResourceValue("cost.message.update.sucess"));
        } catch (QTException e) {
            logger.error(e.getMessage(), e);
            ajaxInfo=  AjaxReturnInfo.failed(e.getRespMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxInfo=  AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.error")); 
        }
        return ajaxInfo;
    }
    
    
    
    /**
     * 【方法名】    : (查询分润费率设置详情). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午7:21:43 .<br/>
     * 【参数】： .<br/> 
     * @param agency_id 机构id 
     * @param status 状态
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=getCostDetail")
    @ResponseBody
    public Map<String, Object> getCostDetail(String agency_id, String status, HttpServletRequest req) {
        // 获得ip
        String ipAddress = req.getRemoteAddr();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

        List<AgencyCost> list;
        try {
            //待生效
            if(FrConstants.waitUse.equals(status)){
                list = agencyCostService.queryRegCostDetail(agency_id);
            }else{
                list = agencyCostService.queryCostDetail(agency_id);
            }
            for (AgencyCost agencyCost : list) {
                if(!StringUtils.isEmpty(agencyCost.getCostRate())){
                    agencyCost.setCostRate(agencyCost.getCostRate().multiply(new BigDecimal("100")).setScale(4,BigDecimal.ROUND_DOWN));
                    agencyCost.setCostFix(agencyCost.getCostFix().divide(new BigDecimal("100")));
                }
            }
            //记录查询日志 
            String operateDetail = "查询分润费率条件为" + "agencyId="+agencyIdS+ ",状态为="+status;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER, operateDetail);
            return AjaxReturnInfo.setTable(list.size(), list);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    
    /**
     * 【方法名】    : (查询是否重复设置). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月17日 下午8:35:36 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @param req HttpServletRequest
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params ="method=isRepeatSetCost")
    @ResponseBody
    public AjaxReturnInfo isRepeatSetCost(@RequestParam(value = "agencyId")String agencyId, HttpServletRequest req){
        AjaxReturnInfo ajaxInfo  = AjaxReturnInfo.success("可以设置！");
        try {
            List<AgencyCost> list = agencyCostService.queryRegCostDetail(agencyId);
            if(list!=null && list.size()>0){
                ajaxInfo = AjaxReturnInfo.failed("重复设置！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return  ajaxInfo;
    }
    
    /**
     * 
     * 【方法名】    : (查询是否有生效的模板). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月13日 下午4:33:38 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=isEffectCost")
    @ResponseBody
    public AjaxReturnInfo isEffectCost(@RequestParam(value = "agencyId") String agencyId) {
        AjaxReturnInfo ajaxInfo = AjaxReturnInfo.success("可以设置！");
        try {
            List<AgencyCost> isUserCostList = agencyCostService.queryIsUserAgencyCost(agencyId);
            if (isUserCostList != null && isUserCostList.size() > 0) {
                ajaxInfo = AjaxReturnInfo.failed("您选择的下级机构没有配置分润费率！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxInfo = AjaxReturnInfo.failed("分润模板出错！");
        }
        return ajaxInfo;
    }
    
    
}
