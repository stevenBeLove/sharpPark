package com.compass.pasmFee.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compass.filter.ThreadLocalContext;
import com.compass.pasmFee.model.QueryTradeProfit;
import com.compass.pasmFee.model.TradeProfit;
import com.compass.utils.AbstractService;

public class QueryTradeProfitService  extends AbstractService {

    /**
     * 查询归属机构的明细的数量
     * @param 
     * @return
     */
    public Integer countNumber(Map<String, Object> map) {
        return (Integer) dao.queryForObject("QueryTradeProfit.selectAllQueryTradeProfitCount", map);
    }
    
    /**
     * 查询归属机构的明细的列表
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QueryTradeProfit> queryList(Map<String, Object> map) {
        return dao.queryForList("QueryTradeProfit.selectListQueryTradeProfit", map);
    }
    
    /**
     * 查询归属机构的交易总金额
     * @param 
     * @return
     */
    public String queryFee_AmountSum(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryFee_AmountSum", map);
    }
    
    /**
     * 查询归属机构的交易下级分润总金额
     * @param 
     * @return
     */
    public String queryNext_feeAmountSum(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryNext_feeAmountSum", map);
    }
    
    /**
     * 查询归属机构的交易分润差
     * @param 
     * @return
     */
    public String queryDifference_feeAmountSum(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryDifference_feeAmountSum", map);
    }
    
    
    
    
    
    
    /**
     * 查询当前机构的明细的数量
     * @param 
     * @return
     */
    public Integer countNowNumber(Map<String, Object> map) {
        return (Integer) dao.queryForObject("QueryTradeProfit.selectNowQueryTradeProfitCount", map);
    }

    /**
     * 查询当前机构的明细的列表
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QueryTradeProfit> queryNowList(Map<String, Object> map) {
        return dao.queryForList("QueryTradeProfit.selectNowListQueryTradeProfit", map);
    }
    
    /**
     * 查询当前机构的分润总金额
     * @param 
     * @return
     */
    public String queryFee_AmountSumNow(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryFee_AmountSumNow", map);
    }
    
    /**
     * 查询当前机构的交易下级分润总金额
     * @param 
     * @return
     */
    public String queryNext_feeAmountSumNow(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryNext_feeAmountSumNow", map);
    }
    
    /**
     * 查询当前机构的交易分润差
     * @param 
     * @return
     */
    public String queryDifference_feeAmountSumNow(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.queryDifference_feeAmountSumNow", map);
    }
    
    /**
     * 
     * 方法名： selectDealAmountSum.<br/>
     * 方法作用:查询交易总金额(简单描述).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月22日.<br/>
     * 创建时间：上午11:36:53.<br/>
     * 参数者异常：@param map 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：String.<br/>
     * 其它内容： JDK 1.6 RtbPlatform 1.0.<br/>
     */
    public String selectDealAmountSum(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.selectDealAmountSum", map);

    }

    /**
     * 
     * 方法名： selectPoundageAmountSum.<br/>
     * 方法作用:查询手续费总金额(简单描述).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月22日.<br/>
     * 创建时间：上午11:39:03.<br/>
     * 参数者异常：@param map 参数者异常：@return .<br/>
     * 返回值： @return 返回结果：String.<br/>
     * 其它内容： JDK 1.6 RtbPlatform 1.0.<br/>
     */
    public String selectPoundageAmountSum(Map<String, Object> map) {
        return (String) dao.queryForObject("QueryTradeProfit.selectPoundageAmountSum", map);

    }

    /**
     * 交易类型查询
     */
    @SuppressWarnings("unchecked")
    public List<QueryTradeProfit> getBusiness(String businessName) {
      Map<String,Object> map=new HashMap<String, Object>();
      map.put("businessName", businessName);
      return dao.queryForList("QueryTradeProfit.getBusiness",map);
    }
    
    /**
     * 【方法名】    : (分润明细). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月9日 上午11:15:48 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String, Object>
     * @return List<TradeProfit>
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<TradeProfit> selectPageTradeProfit(Map<String, Object> map)throws Exception{
        map.put("start" , ThreadLocalContext .getStartValue());
        map.put( "end", ThreadLocalContext. getEndValue());
        return dao.queryForList("QueryTradeProfit.selectPageTradeProfit",map);
    }
    
    /**
     * 【方法名】    : (导出分润明细). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月9日 上午11:15:48 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String, Object>
     * @return List<TradeProfit>
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<TradeProfit> exportPageTradeProfit(Map<String, Object> map)throws Exception{
        //map.put("start" , 0);
        //map.put( "end", 1000);
        return dao.queryForList("QueryTradeProfit.selectPageTradeProfitExport",map);
    }
  
    /**
     * 【方法名】    : (分页条数). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月9日 下午1:46:15 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String, Object>
     * @return Integer
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer selectPageCountTradeProfit(Map<String, Object> map)throws Exception{
        return (Integer) dao.queryForObject("QueryTradeProfit.selectPageCountTradeProfit",map);
    }
    
}
