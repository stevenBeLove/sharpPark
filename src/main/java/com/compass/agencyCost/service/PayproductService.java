package com.compass.agencyCost.service;

import java.util.List;

import com.compass.agencyCost.model.Payproduct;
import com.compass.utils.AbstractService;

/**
 * <pre>
 * 【类型】: PayproductService <br/> 
 * 【时间】：2017年2月15日 下午3:52:07 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class PayproductService  extends AbstractService {

    /**
     * 【方法名】    : (查询瑞通宝所有交易类型). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 下午4:05:01 .<br/>
     * 【参数】： .<br/>
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<Payproduct> getAllPayproduct()throws Exception{
        return dao.queryForList("AgencyCost.getAllPayproduct");
    }
    
    /**
     * 【方法名】    : (获取交易类型). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 下午4:51:56 .<br/>
     * 【参数】： .<br/>
     * @param rtbType
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public String getPayproductDes(String rtbType)throws Exception{
        return (String) dao.queryForObject("AgencyCost.getPayproductDesc", rtbType);
    }
    
}
