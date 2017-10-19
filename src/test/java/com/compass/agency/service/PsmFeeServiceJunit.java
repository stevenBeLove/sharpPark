/** 
 * 包名: package com.compass.agency.service; <br/> 
 * 添加时间: 2017年4月7日 下午4:05:26 <br/> 
 */
/**
 *  Project Name:Rtb
 *  File: TerminalManageServiceJunit.java
 *  Package Name:com.compass.agency.service
 *  <P>
 *  创建时间     创建者              修改记录
 *  2017年4月7日   简思文     Create
 *  </p>
 *  <p>File Description :  this Class dosome </p>
 *  Description:
 *  Copyright 2014-2015 QIANTUO FINANCE Services Co.,Ltd. All rights reserved.
 */
package com.compass.agency.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.pasmFee.service.PsmFeeService;
import com.compass.terminalmanage.service.TerminalManageService;

/** 
 * 类名: TerminalManageServiceJunit <br/> 
 * 作用：TODO(简单一句话描述)<br/> 
 * 方法：TODO(简单描述方法)<br/> 
 * 创建者: xinxiang. <br/> 
 * 添加时间: 2017年4月7日 下午4:05:26 <br/> 
 * 版本： JDK 1.6 Rtb 1.0
 */
/** 
 * 类名: TerminalManageServiceJunit <br/> 
 * 作用：TODO(简单一句话描述)<br/> 
 * 方法：TODO(简单描述方法)<br/> 
 * 创建者: 简思文. <br/> 
 * 添加时间: 2017年4月7日 下午4:05:26 <br/> 
 * 版本： 
 * @since JDK 1.6 Rtb 1.0
 */
public class PsmFeeServiceJunit {
    
    static PsmFeeService   psmFeeService;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
        psmFeeService = (PsmFeeService) ctx.getBean("psmFeeService");
    }
    
    @Test
    public void queryCostRate(){
        try {
            psmFeeService.selectCostRate("RTB30000001", "RTB30000000", null, "0", "0");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }
    
    public void test(){
        
    }

}

