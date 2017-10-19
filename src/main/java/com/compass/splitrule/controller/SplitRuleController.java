/**
 * 
 */
package com.compass.splitrule.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.splitrule.model.SplitRuleBean;
import com.compass.splitrule.service.SplitRuleService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author wangLong
 * 分润规则设置Controller
 */


@Controller
@RequestMapping("/splitrule/splitrule.do")
public class SplitRuleController {

	@Autowired
	@Qualifier("splitRuleService")
	private SplitRuleService splitRuleService;

	
	
	/**
	 * 获得规则列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getRules")
	@ResponseBody
	public Map<String, Object> getRules(
			@RequestParam(value = "childagencyId") String childagencyId,
			HttpServletRequest request) {
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.AGENCYID);
		List<SplitRuleBean> list = splitRuleService.getRules(agencyId,childagencyId);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1": page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20": rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<SplitRuleBean> ruleList=new ArrayList<SplitRuleBean>();
		if (list != null && list.size() > 0) {
			for(int i=start;i<end;i++){
				ruleList.add(list.get(i));
			}
		}
		ruleList=null;
		return AjaxReturnInfo.setTable(list==null?0:list.size(), list);
	}
	
	
	/**
	 * 获得规则区间列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getRulesByIds")
	@ResponseBody
	public Map<String, Object> getRulesByIds(
			@RequestParam(value = "childAgencyId") String childAgencyId,
			@RequestParam(value = "dealType") String dealType,
			HttpServletRequest request) {
		String agencyId =request.getSession().getAttribute(ConstantUtils.AGENCYID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.AGENCYID);
		List<SplitRuleBean> list = splitRuleService.getRulesByIds(agencyId,childAgencyId,dealType);
		int temp = ConstantUtils.RULECOUNT -list.size();
		for(int i=0; i<temp; i++){
			SplitRuleBean bean = new SplitRuleBean();
			list.add(bean);
			bean=null;
		}
		return AjaxReturnInfo.setTable(list==null?0:list.size(), list);
	}
	
	/**
	 * 获得单笔区间列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSingleDealSection")
	@ResponseBody
	public Map<String, Object> getSingleDealSection(
			@RequestParam(value = "splitMode") String splitMode,
			@RequestParam(value = "scale") String scale,
			@RequestParam(value = "ruleId") String ruleId,
			HttpServletRequest request) {
		List<SplitRuleBean> list = splitRuleService.getSingleDealSection(ruleId);
		List<SplitRuleBean> beanList = new ArrayList<SplitRuleBean>();
		for(SplitRuleBean bean:list){
			if("1".equals(splitMode)&&"1".equals(scale)){
				if(".".equals(bean.getRate().substring(0, 1))){
					bean.setRate("0"+bean.getRate());
				}
			}if("1".equals(splitMode)&&"2".equals(scale)){
				if(".".equals(bean.getRate().substring(0, 1))){
					bean.setRate("0"+bean.getRate());
				}
			}else{
				Double temp = Double.valueOf(bean.getRate());
				temp = temp*100;
				NumberFormat format = new DecimalFormat("####.####");
				bean.setRate(format.format(temp));
			}
			beanList.add(bean);
		}
		int temp = ConstantUtils.SINGLERULECOUNT -list.size();
		for(int i=0; i<temp; i++){
			SplitRuleBean bean = new SplitRuleBean();
			beanList.add(bean);
		}
		return AjaxReturnInfo.setTable(beanList==null?0:beanList.size(), beanList);
	}
	
	/**
	 * 添加分润规则
	 * @param childAgencyId
	 * @param dealType
	 * @param splittingMode
	 * @param scale
	 * @param scaleStartvalue
	 * @param scaleEndvalue
	 * @param singleDealStartvalue
	 * @param singleDealendvalue
	 * @param rate
	 * @param validityData
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addRule",method =RequestMethod.POST)
	@ResponseBody
	public AjaxReturnInfo addRule(
			@RequestBody String jsonStr,
			HttpServletRequest request) {
		Gson gson  = new Gson(); 
		List<SplitRuleBean> beanList = gson.fromJson(jsonStr,new TypeToken<List<SplitRuleBean>>(){}.getType());
		String createrId=request.getSession().getAttribute(ConstantUtils.USERID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.USERID);
		String agencyId = request.getSession().getAttribute(ConstantUtils.AGENCYID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.AGENCYID);
		/*String ruleIds=request.getParameter("ruleIds");*/
		String childAgencyId=request.getParameter("childAgencyId");
		String dealType = request.getParameter("dealType");
		String splittingMode=request.getParameter("splittingMode");
		String scale =request.getParameter("scale");
		String validityData =request.getParameter("validityData");
		String  flag=beanList.get(0).getRuleId();
		AjaxReturnInfo ajaxinfo = null;
		int result=0;
		if(flag==null || "".equals(flag) || "null".equals(flag)){
			//表示是添加
			Integer count=splitRuleService.getRulesCount(agencyId,childAgencyId,dealType);
			if(count>0){
				ajaxinfo = AjaxReturnInfo.failed("该子机构已经设置了分润方式");
				
				
			}else{
				result = SetBeanData(beanList, createrId, agencyId, childAgencyId,
						dealType, splittingMode, scale, validityData, result);
				ajaxinfo = AjaxReturnInfo.success("设置成功");
			}
			
		}else{
			String createDt = CommonDate.getDate();
			for(int i=0;i<beanList.size();i++){
				String scaleStartValue=beanList.get(i).getScaleStartValue();
				String ruleId=beanList.get(i).getRuleId();
				if(scaleStartValue!=null && !"".equals(scaleStartValue)){
					if(ruleId!=null && !"".equals(ruleId)){
						//做修改操作
						SplitRuleBean bean = beanList.get(i);
					
						if("".equals(bean.getScaleEndValue())||bean.getScaleEndValue()==null){
							bean.setScaleEndValue(ConstantUtils.MAXVALUE);
						}
						bean.setRuleId(ruleId);
						bean.setChildAgencyId(childAgencyId);
						bean.setAgencyId(agencyId);
						bean.setDealType(dealType);
						bean.setSplittingMode(splittingMode);
						bean.setScale(scale);
						bean.setValidityData(validityData);
						bean.setCreaterId(createrId);
						bean.setCreateDate(createDt);
						splitRuleService.updateRule(bean);
					}else{
						SplitRuleBean bean = beanList.get(i);
						String RuleId = splitRuleService.getRuleId().toString();
						if("".equals(bean.getScaleEndValue())||bean.getScaleEndValue()==null){
							bean.setScaleEndValue(ConstantUtils.MAXVALUE);
						}
						bean.setRuleId(RuleId);
						bean.setChildAgencyId(childAgencyId);
						bean.setAgencyId(agencyId);
						bean.setDealType(dealType);
						bean.setSplittingMode(splittingMode);
						bean.setScale(scale);
						bean.setValidityData(validityData);
						bean.setCreaterId(createrId);
						bean.setCreateDate(createDt);
						result = splitRuleService.addRule(bean);
					}
				}else{
					if(ruleId!=null && !"".equals(ruleId)){
						//做删除操作
						splitRuleService.deleteSingleRules(ruleId);
						splitRuleService.deleteRule(ruleId);
					}
				}	
			}	
			ajaxinfo = AjaxReturnInfo.success("设置成功");
		}
		
	
		return ajaxinfo;
	}


	/**
	 * 设置规则Bean数据
	 * @param beanList
	 * @param createrId
	 * @param agencyId
	 * @param childAgencyId
	 * @param dealType
	 * @param splittingMode
	 * @param scale
	 * @param validityData
	 * @param result
	 * @return
	 */
	private int SetBeanData(List<SplitRuleBean> beanList, String createrId,
			String agencyId, String childAgencyId, String dealType,
			String splittingMode, String scale, String validityData, int result) {
		String createDt = CommonDate.getDate();
		for( int i=0;i< beanList.size();i++){
			SplitRuleBean bean = beanList.get(i);
			if(bean.getScaleStartValue()!=null&&!"".equals(bean.getScaleStartValue())){
				String ruleId = splitRuleService.getRuleId().toString();
				if("".equals(bean.getScaleEndValue())||bean.getScaleEndValue()==null){
					bean.setScaleEndValue(ConstantUtils.MAXVALUE);
				}
				bean.setRuleId(ruleId);
				bean.setChildAgencyId(childAgencyId);
				bean.setAgencyId(agencyId);
				bean.setDealType(dealType);
				bean.setSplittingMode(splittingMode);
				bean.setScale(scale);
				bean.setValidityData(validityData);
				bean.setCreaterId(createrId);
				bean.setCreateDate(createDt);
				result = splitRuleService.addRule(bean);
			}
		}
		return result;
	}

	
	/**
	 * 添加单笔区间
	 * @param beanList
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addSingleRule",method =RequestMethod.POST)
	@ResponseBody
	public AjaxReturnInfo addSingleRule(
			@RequestBody String  jsonStr,
			HttpServletRequest request) {
		Gson gson  = new Gson(); 
		List<SplitRuleBean> beanList = gson.fromJson(jsonStr,new TypeToken<List<SplitRuleBean>>(){}.getType());
		String createrId=request.getSession().getAttribute(ConstantUtils.USERID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.USERID);
		String ruleId = request.getParameter("ruleId");
		String splitMode = request.getParameter("splitMode");
		String scale = request.getParameter("scale");
		if(ruleId==null||"".equals(ruleId)){
			ruleId="''";
		}
		splitRuleService.deleteSingleRules(ruleId);
		int flag = 0;
		for(int i=0;i<beanList.size();i++){
			SplitRuleBean bean = beanList.get(i);
			if(bean.getSingleDealStartValue()!=null&&!"".equals(bean.getSingleDealStartValue())){
				bean.setSingleRuleId(ruleId+"S");
				if("".equals(bean.getSingleDealEndValue())||bean.getSingleDealEndValue()==null){
					bean.setSingleDealEndValue(ConstantUtils.MAXVALUE);
				}
				if("1".equals(splitMode)&&"1".equals(scale)){
					
				}if("1".equals(splitMode)&&"2".equals(scale)){
					
				}else{
					double temp = Double.valueOf(bean.getRate());
					temp = temp/100;
					bean.setRate(String.valueOf(temp));
				}
				bean.setRuleId(ruleId);
				bean.setCreaterId(createrId);
				int result = splitRuleService.addSingleRule(bean);
				flag +=result;
			}
		}
		AjaxReturnInfo ajaxinfo = null;
		if(flag>0){
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		} else {
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}

	
	/**
	 * 删除分润规则
	 * @param Ids
	 * @return
	 */
	@RequestMapping(params = "method=deleteRules")
	@ResponseBody
	public AjaxReturnInfo deleteRules(
			@RequestParam(value = "childAgencyId") String childAgencyId,
			@RequestParam(value = "dealType") String dealType,
			@RequestParam(value = "validityData") String validityData,
			HttpServletRequest request) {
		AjaxReturnInfo ajaxinfo = null;
		String agencyId =  request.getSession().getAttribute(ConstantUtils.AGENCYID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.AGENCYID);
		int result = splitRuleService.deleteRules(agencyId,childAgencyId,dealType,validityData);
		List<SplitRuleBean>  list = splitRuleService.checkRule(agencyId, childAgencyId, dealType,validityData);
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("删除失败");
		} else {
			for (SplitRuleBean bean:list){
				String ruleId = bean.getRuleId();
				splitRuleService.deleteSingleRules(ruleId);
				
			}
			ajaxinfo = AjaxReturnInfo.success("删除成功");
		}
		
		return ajaxinfo;
	}
}
