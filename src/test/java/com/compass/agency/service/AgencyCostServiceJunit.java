/** 
 * 包名: package com.compass.agency.service; <br/> 
 * 添加时间: 2017年3月29日 下午4:10:34 <br/> 
 */
package com.compass.agency.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyBean;
import com.compass.agencyCost.service.AgencyCostService;
import com.compass.utils.ConstantUtils;

/** 
 * 类名: AgencyServiceJunit <br/> 
 * 作用：unit测试类<br/> 
 * 创建者: zyh12345. <br/> 
 * 添加时间: 2017年3月29日 下午4:10:34 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class AgencyCostServiceJunit {

    static AgencyCostService  agencyCostService;
    
    //@BeforeClass
    public static void setUpBeforeClass() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
        agencyCostService = (AgencyCostService) ctx.getBean("agencyCostService");
    }
    
    @Test
    public void checkNameCertPid(){
        try {
            JSONObject jsonObj = agencyCostService.CheckSetProfitCost("RTB99990000");
            System.out.println(jsonObj.get(ConstantUtils.MSG_CODE));
            System.out.println(jsonObj.get(ConstantUtils.MSG_TEXT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void subStr(){
        String aa = "RTBabcdefg3432";
        System.out.println(aa.substring(3));
    }
    
    
}

