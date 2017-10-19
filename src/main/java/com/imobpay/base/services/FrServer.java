/**
 *  <pre>	
 *  Project Name:ServerFramework .</br>
 *  File: UserServer.java .</br>
 *  Package Name:com.imobpay.base.services .</br>
 *  Date      Author       Changes .</br>
 *  2016年5月30日   Lance.Wu     Create  .</br>
 *  Description: .</br>
 *  Copyright 2014-2015 YINGXIANG FINANCE Services Co.,Ltd. All rights reserved..</br>
 *  <pre>	
 */
package com.imobpay.base.services;

/** 
 * 
 * <pre>
 * 【类型】: FrServer <br/> 
 * 【作用】: Dubbo分润服务接口. <br/>  
 * 【时间】：2016年12月12日 上午10:56:38 <br/> 
 * 【作者】：HuaiYu.Wen <br/> 
 * </pre>
 */
public interface FrServer{
    /**
     * 
     * execute:定义dubbo接口. <br/> 
     * 
     * @author madman
     * @param json json格式的字符串
     * @return
     * @return 返回结果：String
     * @since JDK 1.6 PayIFramework 1.0
     */
    String execute(String json);
}