package com.compass.pasmFee.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.compass.agencyCost.model.AgencyCost;
import com.compass.agencyCost.service.AgencyCostService;
import com.compass.pasmFee.model.PsmFee;
import com.compass.pasmFee.service.PsmFeeService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.terminalmanage.model.TerminalMsgBean;
import com.compass.terminalmanage.service.TerminalManageService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.NumberFormat;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.compass.utils.mvc.I18nUtils;

@Controller
@RequestMapping("/fee/psmfee.do")
public class PsmFeeController {
    private final Log            log = LogFactory.getLog(getClass());
	/**
	 * 系统日志service
	 */
	@Autowired
	@Qualifier("systemLogService")
	private SystemLogService systemLogService;
	
  @Autowired
  @Qualifier("terminalManageService")
  public TerminalManageService terminalManageService;
	
  /**
   * 分润成本设置
   */
  @Autowired
  @Qualifier("agencyCostService")
  private AgencyCostService   agencyCostService;

	/**
	 * 费率设置
	 */
	@Autowired
	@Qualifier("psmFeeService")
	private PsmFeeService psmFeeService;
	private static Logger logger = LoggerFactory
			.getLogger(PsmFeeController.class);

	/**
	 * 获取费率信息
	 * 
	 * @param agencyId
	 * @param companyName
	 * @param agencyStatus
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "method=getPsmFeeByParam")
	@ResponseBody
	public Map<String, Object> getPsmFeeByParam(
			@RequestParam(value = "agencyId") String agencyId,
			@RequestParam(value = "startPsamId") String startPsamId,
			@RequestParam(value = "endPsamId") String endPsamId,
			@RequestParam(value = "businesstype") String businesstype,
			@RequestParam(value = "termstatus") String termstatus,
			@RequestParam(value = "shopno") String shopno,
			@RequestParam(value = "fixFee") String fixFee,
			 @RequestParam(value = "terminalstatus") String terminalstatus,
			HttpServletRequest req) {
		// 获得ip
		String ipAddress = req.getRemoteAddr();
		String userId = req.getSession().getAttribute(ConstantUtils.USERID)
				.toString();
		String agencyIdS = req.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");

		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;

		Map<String, Object> map = new HashMap<String, Object>();

		if (org.apache.commons.lang.StringUtils.isEmpty(agencyId)) {
			agencyId = agencyIdS;
		}
		map.put("agencyId", agencyId);
		Integer count = psmFeeService.getAllPsmFeeCount(agencyId, startPsamId,
				endPsamId, businesstype, termstatus, shopno, fixFee,terminalstatus);
		int end = (start + rownumber) > count ? count : start + rownumber;
		map.put("start", start);
		map.put("end", end);
		map.put("startPsamId", startPsamId);
		map.put("endPsamId", endPsamId);
		map.put("businesstype", businesstype);
		map.put("termstatus", termstatus);
		map.put("shopno", shopno);
		map.put("fixFee", fixFee);

		List<PsmFee> list = psmFeeService.getPsmFeeAll(agencyId, startPsamId,
				endPsamId, businesstype, termstatus, shopno, fixFee, terminalstatus, start, end);
		
		// 记录查询日志
		String operateDetail = "查询条件为" + "startpsamid=" + startPsamId
				+ ",endpsamid=" + endPsamId + ",agencyId=" + agencyId
				+ ",businesstype=" + businesstype + ",termstatus=" + termstatus
				+ ",shopno=" + shopno + ",fixFee=" + fixFee+",treminalstatus"+terminalstatus;
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyIdS, userId,
				ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER,
				operateDetail);
		return AjaxReturnInfo.setTable(count, list);
	}

	/**
	 * 获取机构费率成本信息
	 * 
	 * @param agencyId
	 * @param companyName
	 * @param agencyStatus
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "method=getFeeByAgencyId")
	@ResponseBody
	public Map<String, Object> getFeeByAgencyId(
			@RequestParam(value = "agencyId") String agencyId,
			HttpServletRequest req) {
		// 获得ip
		String ipAddress = req.getRemoteAddr();
		String userId = req.getSession().getAttribute(ConstantUtils.USERID)
				.toString();
		String agencyIdS = req.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();

		Map<String, Object> map = new HashMap<String, Object>();
		if (org.apache.commons.lang.StringUtils.isEmpty(agencyId)) {
			agencyId = agencyIdS;
		}
		map.put("agencyId", agencyId);
		Integer count = psmFeeService.getFeeByAgencyIdCount(agencyId);
		
		String feeMin = psmFeeService.getParamterforFeeMin(agencyId);
		Integer fixMin = psmFeeService.getParamterforFixMin(agencyId);
		
		String feeMax = psmFeeService.getParamterforFeeMax(agencyId);
		Integer fixMax = psmFeeService.getParamterforFixMax(agencyId);

		List<PsmFee> list = psmFeeService.getFeeByAgencyId(agencyId);
		// 记录查询日志
		String operateDetail = "查询条件为" + "agencyId=" + agencyId + ",";
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyIdS, userId,
				ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER,
				operateDetail);
		return AjaxReturnInfo.setTableInteger(count, list, feeMin, fixMin, feeMax, fixMax);
	}

	/**
	 * 获取交易类型基本费率信息
	 * 
	 * @param agencyId
	 * @param companyName
	 * @param agencyStatus
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "method=getBusinessFeebyAgencyId")
	@ResponseBody
	public Map<String, Object> getPsmFeeByParam(
			@RequestParam(value = "agencyId") String agencyId,
			HttpServletRequest req) {
		// 获得ip
		String ipAddress = req.getRemoteAddr();
		String userId = req.getSession().getAttribute(ConstantUtils.USERID)
				.toString();
		String agencyIdS = req.getSession()
				.getAttribute(ConstantUtils.AGENCYID).toString();

		Map<String, Object> map = new HashMap<String, Object>();
		Integer count = 4;
		if (!ConstantUtils.CENTERCODE.equals(agencyIdS)) {
			if (StringUtils.isEmpty(agencyId)) {
				map.put("agencyId", agencyIdS);
			} else {
				map.put("agencyId", agencyId);
			}
		}
		List<PsmFee> list = psmFeeService.getBusnessFeebyAgencyId(agencyId);
		// 记录查询日志
		String operateDetail = "查询条件为" + "agencyId=" + agencyId + ",";
		// 添加进系统日志表 20141203
		systemLogService.addLog(ipAddress, agencyIdS, userId,
				ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER,
				operateDetail);
		return AjaxReturnInfo.setTable(count, list);
	}

	

    /**
     * 设置终端费率信息
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=setPsmFeebyPasmId")
    @ResponseBody
    public AjaxReturnInfo setPsmFeebyPasmId(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "startPSAMId") String startpsamid,
            @RequestParam(value = "endPSAMId") String endpsamid, @RequestParam(value = "shopno") String shopno, @RequestParam(value = "fixFee") String fixFee,
            HttpServletRequest req) {
        AjaxReturnInfo ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.execute.failed"));
        try {
            String ipAddress = req.getRemoteAddr();
            String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
            String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
            String userName = req.getSession().getAttribute(ConstantUtils.USERNAME).toString();

            Map<String, Object> map = new HashMap<String, Object>();
            if (org.apache.commons.lang.StringUtils.isEmpty(agencyId)) {
                agencyId = agencyIdS;
            }
            map.put("agencyId", agencyId);
            //插入终端费率历史表
            //psmFeeService.InsertPsamFeeHis(agencyId, startpsamid, endpsamid);
            int result = psmFeeService.updatePsmFeebyPsam(agencyId, startpsamid, endpsamid, shopno, fixFee, userName);
            ajaxInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.agajx.execute.success"));
            if (result == 0) {
                ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.execute.failed"));
            }
            // 记录查询日志
            String operateDetail = "更新条件为" + "startpsamid=" + startpsamid + ",endpsamid=" + endpsamid;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER, operateDetail);
        } catch (QTException e) {
            logger.error(e.getMessage(), e);
            ajaxInfo = AjaxReturnInfo.failed(e.getRespMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.error"));
        }
        return ajaxInfo;
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
    @RequestMapping(params ="method=psmFeeView")
    public ModelAndView forwardCostView(HttpServletRequest req) {
      Map<String, Object> paraMap = new HashMap<String, Object>();
      String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
    try {
          //本级生效中的费率
          List<AgencyCost>  costList = agencyCostService.queryIsUserAgencyCost(agencyIdS);
          for (AgencyCost agencyCost : costList) {
              agencyCost.setCostRate(agencyCost.getCostRate().multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN));
              agencyCost.setCostFix(agencyCost.getCostFix().divide(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN));
          }
          
          //闪付最大值
          BigDecimal feeMax = psmFeeService.getRtbParamterByClassno("TERM005").multiply(new BigDecimal("100"));
          //闪付固定最大值
          BigDecimal fixMax = psmFeeService.getRtbParamterByClassno("TERM009").divide(new BigDecimal("100"));
          
          //闪付最小值
          BigDecimal feeMin = psmFeeService.getRtbParamterByClassno("TERM001").multiply(new BigDecimal("100"));
          BigDecimal fixMin = psmFeeService.getRtbParamterByClassno("TERM010").divide(new BigDecimal("100"));
          
          paraMap.put("feeMax", NumberFormat.parseYuanFormat(feeMax.toString()));
          paraMap.put("fixMax", NumberFormat.parseYuanFormat(fixMax.toString()));
          
          paraMap.put("feeMin", NumberFormat.parseYuanFormat(feeMin.toString()));
          paraMap.put("fixMin", NumberFormat.parseYuanFormat(fixMin.toString()));
          paraMap.put("costList", costList);
          
          
    } catch (Exception e) {
        logger.error(e.getMessage(),e);
    }
      return new ModelAndView("/psmFee/psmFee",paraMap);
    }
    
    /**
     * 
     * 【方法名】    : 短信发送. <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月9日 下午4:21:17 .<br/>
     * 【参数】： .<br/>
     * @param agencyId
     * @param shopnop
     * @param fixFeep
     * @param shopno
     * @param fixFee
     * @param psamId
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: 张柯. 修改描述：创建新新件 .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=setPsmFeebyPasmIdUP")
    @ResponseBody
    public AjaxReturnInfo setPsmFeebyPasmIdUP(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "shopnop") String shopnop,
            @RequestParam(value = "fixFeep") String fixFeep, @RequestParam(value = "shopno") String shopno, @RequestParam(value = "fixFee") String fixFee,
            @RequestParam(value = "psamId") String psamId,HttpServletRequest req) {
        
        int count = this.terminalManageService.checkUserId(psamId);
            log.info("查询当前终端是否存在开始");
            AjaxReturnInfo ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.execute.failedUpdate"));
        try {
            String ipAddress = req.getRemoteAddr();
            String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
            String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
            String userName = req.getSession().getAttribute(ConstantUtils.USERNAME).toString();
            TerminalMsgBean beanLower= (TerminalMsgBean) this.terminalManageService.selectUserId(psamId);
            
            Map<String, Object> map = new HashMap<String, Object>();
            if (org.apache.commons.lang.StringUtils.isEmpty(agencyId)) {
                agencyId = agencyIdS;
            }
            map.put("agencyId", agencyId);
            //插入终端费率历史表
            //psmFeeService.InsertPsamFeeHis(agencyId, startpsamid, endpsamid);
            int result = psmFeeService.updatePsmFeebyPsamId(agencyId,shopno, fixFee,psamId, userName,shopnop, fixFeep);
           
            ajaxInfo = AjaxReturnInfo.success(I18nUtils.getResourceValue("message.agajx.execute.successUpdate"));
            if (result == 0 ) {
                ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.execute.failedUpdate"));
            }else{
                if(count == 0){
                    return AjaxReturnInfo.failed("该终端费率已修改，该终端未绑定用户");
                }else{
                this.terminalManageService.sendRateUpdateMsg(beanLower.getPhoneNum(), psamId);
            }
            }
            // 记录查询日志
            String operateDetail = "更新参数为" + "shopnop=" + shopnop + ",fixFeep=" + fixFeep +",shopnop"+shopnop+",fixFeep"+fixFeep;
            // 添加进系统日志表 20141203
            systemLogService.addLog(ipAddress, agencyIdS, userId, ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER, operateDetail);
        } catch (QTException e) {
            logger.error(e.getMessage(), e);
            ajaxInfo = AjaxReturnInfo.failed(e.getRespMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxInfo = AjaxReturnInfo.failed(I18nUtils.getResourceValue("message.agajx.system.error"));
        }
        return ajaxInfo;
    }
    /**
     * 
     * 【方法名】    : 查询终端所属机构. <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 张柯. .<br/>
     * 【时间】： 2017年8月9日 下午4:21:02 .<br/>
     * 【参数】： .<br/>
     * @param agencyId
     * @param psamId
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: 张柯. 修改描述：创建新新件 .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=selPsmFeebyPasmIdBranch")
    @ResponseBody
    public int selPsmFeebyPasmIdBranch(@RequestParam(value = "agencyId") String agencyId, @RequestParam(value = "psamId") String psamId,
            HttpServletRequest req) {
           String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
         
            //查询终端所属机构
            PsmFee result = psmFeeService.selPsmFeebyPasmIdBranch(psamId);
            String agencyId1 = result.getAgencyId();
            if (agencyId1.equals(agencyIdS)) {
                return 1;
            }else{
                return 0;
            }
           
    }
    /**
     * 获取交易类型基本费率信息
     * 
     * @param agencyId
     * @param companyName
     * @param agencyStatus
     * @param req
     * @return
     */
    @RequestMapping(params = "method=getBusinessFeebyAgencyIdUpdate")
    @ResponseBody
    public  Map<String, Object> getBusinessFeebyAgencyIdUpdate( @RequestParam(value = "agencyId") String agencyId,@RequestParam(value = "psamId") String psamId,
        HttpServletRequest req) {
      // 获得ip
      String ipAddress = req.getRemoteAddr();
      String userId = req.getSession().getAttribute(ConstantUtils.USERID)
          .toString();
      String agencyIdS = req.getSession()
          .getAttribute(ConstantUtils.AGENCYID).toString();
      agencyId = agencyIdS;
   
      List<PsmFee> list = (List<PsmFee>) psmFeeService.getBusnessFeebyAgIdPsId(agencyId, psamId);
      // 记录查询日志
      String operateDetail = "查询条件为" + "agencyId=" + agencyId + ",";
      // 添加进系统日志表 20141203
      systemLogService.addLog(ipAddress, agencyIdS, userId,
          ConstantUtils.OPERNAMEPSMFEE, ConstantUtils.OPERTYPESER,
          operateDetail);
      return AjaxReturnInfo.setTable(list.size(), list);
    }
    
}
