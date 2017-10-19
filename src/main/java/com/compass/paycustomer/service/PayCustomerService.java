/** 
 * 包名: package com.compass.paycustomer.service; <br/> 
 * 添加时间: 2017年4月18日 下午2:55:01 <br/> 
 */
package com.compass.paycustomer.service;

import com.compass.paycustomer.model.PayCustomer;
import com.compass.utils.AbstractService;

/** 
 * 类名: PayCustomerService <br/> 
 * 作用：TODO(简单一句话描述)<br/> 
 * 方法：TODO(简单描述方法)<br/> 
 * 创建者: zyh12345. <br/> 
 * 添加时间: 2017年4月18日 下午2:55:01 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class PayCustomerService  extends AbstractService {

    /**
     * 【方法名】    : 查询用户是否已经实名认证过. <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月18日 下午3:51:15 .<br/>
     * 【参数】： .<br/>
     * @param mobile 手机号码
     * @return PayCustomer.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public PayCustomer selectPayCustomerByMobile(String mobile){
        return (PayCustomer) dao.queryForObject("PAYCUSTOMER.selectPayCustomerByMobile", mobile);
    }
}

