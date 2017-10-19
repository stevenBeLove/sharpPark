/** 
 * 包名: package com.compass.paramater.service; <br/> 
 * 添加时间: 2017年4月5日 下午5:41:07 <br/> 
 */
package com.compass.paramater.service;

import java.util.List;
import java.util.Map;

import com.compass.paramater.model.RtbParamter;
import com.compass.utils.AbstractService;

/** 
 * 类名: RtbParamterService <br/> 
 * 作用：瑞通宝参数操作<br/> 
 * 创建者: zhangyinghui <br/> 
 * 添加时间: 2017年4月5日 下午5:41:07 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class RtbParamterService extends AbstractService {

    
    /**
     * 【方法名】    : (根据map类型查询参数对象). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月5日 下午5:45:09 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String, Object>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public List<RtbParamter> getRtbParamter(Map<String, Object> map){
        return dao.queryForList("RTBPARAMTER.selectRtbParamterByMap", map);
    }
    
    /**
     * 【方法名】    : (查询参数信息). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月5日 下午5:53:53 .<br/>
     * 【参数】： .<br/>
     * @param paramater RtbParamter
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public RtbParamter getRtbParamter(RtbParamter paramater){
        return (RtbParamter) dao.queryForObject("RTBPARAMTER.selectRtbParamter", paramater);
    }
    
}

