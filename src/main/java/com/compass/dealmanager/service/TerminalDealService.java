package com.compass.dealmanager.service;

import java.util.List;

import com.compass.dealmanager.model.TerminalDealBean;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;
 
/**
 * 类名: TerminalDealService <br/> 
 * 创建者: zhang jun <br/> 
 * 添加时间: 2016年4月12日 下午1:50:22 <br/> 
 * @since JDK 1.6 qtfr 1.0
 */
public class TerminalDealService extends AbstractService {

	/**
	 * 方法名： getViewTerminalDealCount.<br/>
	 * 方法作用:获取终端交易记录数.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:51:25.<br/>
	 * 返回值： @return 返回结果：Integer.<br/>
	 */
	public Integer getViewTerminalDealCount(TerminalDealBean terminalDeal) {
		   if("0".equals(terminalDeal.getFlagStatus())){
			   return (Integer) dao.queryForObject("terminaldeal.getTerDealCount", terminalDeal);
		   }
		   if(terminalDeal.getStartDate().equals(terminalDeal.getEndDate())){
		    	terminalDeal.setStartDate("");
		    	terminalDeal.setEndDate("");
		    }
		return (Integer) dao.queryForObject("terminaldeal.getViewTerminalDealCount", terminalDeal);
	}


	/**
	 * 方法名： getViewTermnalDealList.<br/>
	 * 方法作用:获取终端交易列表.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:52:59.<br/>
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalDealBean> getViewTermnalDealList(TerminalDealBean terminalDeal) {
	    if("0".equals(terminalDeal.getFlagStatus())){
	    	return dao.queryForList("terminaldeal.getTerDealList", terminalDeal);
	    }
	    if(terminalDeal.getStartDate().equals(terminalDeal.getEndDate())){
	    	terminalDeal.setStartDate("");
	    	terminalDeal.setEndDate("");
	    }
		return dao.queryForList("terminaldeal.getViewTermnalDealList", terminalDeal);
	}

	/**
	 * 方法名： updateFlagTerminalDealOne.<br/>
	 * 方法作用:更新一条终端交易记录标记状态.<br/>
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 */
	public int updateFlagTerminalDeal(TerminalDealBean  tempmodel) {
		tempmodel.setFlagOperTime(CommonDate.getDate());
		return dao.update("terminaldeal.updateFlagTerminal", tempmodel);

	}

	/**
	 * 方法名： insertPayTermJnls.<br/>
	 * 方法作用: 按时间区间插入终端交易记录.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月11日.<br/>
	 * 创建时间：下午4:47:44.<br/>
	 * @param lastmonthfirstdate
	 * @param lastmonthlastdate
	 */
	public int insertPayTermJnls(TerminalDealBean terminalDeal) {
		if("1".equals(terminalDeal.getFlag())){
			return   dao.insert("terminaldeal.insertTermDeal", terminalDeal);
		}else if("2".equals(terminalDeal.getFlag())){
			 return dao.insert("terminaldeal.insertOldTermDeal", terminalDeal);
		}
		return 0;		
	}
	
	/**
	 * 
	 * 方法名： delPayTermJnls.<br/>
	 * 方法作用:删除信息.<br/>
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月25日.<br/>
	 * 创建时间：下午5:34:03.<br/>
	 * 参数者异常：@param terminalDeal
	 * 参数者异常：@return .<br/>
	 * 返回值： @return 返回结果：int.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public int delPayTermJnls(TerminalDealBean terminalDeal) {
		return   dao.insert("terminaldeal.delTermDeal", terminalDeal);
	}
	
	
	public Integer getOldTerminalDealCount(TerminalDealBean terminalDeal) {
		   if("0".equals(terminalDeal.getFlagStatus())){
			   return (Integer) dao.queryForObject("terminaldeal.getOldTerDealCount", terminalDeal);
		   }
		   if(terminalDeal.getStartDate().equals(terminalDeal.getEndDate())){
		    	terminalDeal.setStartDate("");
		    	terminalDeal.setEndDate("");
		    }
		return (Integer) dao.queryForObject("terminaldeal.getViewTerminalDealCount", terminalDeal);
	}


	/**
	 * 方法名： getViewTermnalDealList.<br/>
	 * 方法作用:获取终端交易列表.<br/>
	 *
	 * 创建者：zhang jun<br/>
	 * 创建日期：2016年4月12日.<br/>
	 * 创建时间：下午1:52:59.<br/>
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalDealBean> getOldTermnalDealList(TerminalDealBean terminalDeal) {
	    if("0".equals(terminalDeal.getFlagStatus())){
	    	return dao.queryForList("terminaldeal.getOldTerDealList", terminalDeal);
	    }
	    if(terminalDeal.getStartDate().equals(terminalDeal.getEndDate())){
	    	terminalDeal.setStartDate("");
	    	terminalDeal.setEndDate("");
	    }
		return dao.queryForList("terminaldeal.getViewTermnalDealList", terminalDeal);
	}

}