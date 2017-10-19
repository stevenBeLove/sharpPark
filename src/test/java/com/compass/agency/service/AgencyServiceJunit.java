/** 
 * 包名: package com.compass.agency.service; <br/> 
 * 添加时间: 2017年3月29日 下午4:10:34 <br/> 
 */
package com.compass.agency.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyBean;
import com.compass.utils.ConstantUtils;

/** 
 * 类名: AgencyServiceJunit <br/> 
 * 作用：unit测试类<br/> 
 * 创建者: zyh12345. <br/> 
 * 添加时间: 2017年3月29日 下午4:10:34 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class AgencyServiceJunit {

    static AgencyService   agencyService;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
        agencyService = (AgencyService) ctx.getBean("agencyService");
    }
    
    @Test
    public void checkNameCertPid(){
        try {
            AgencyBean agency = new AgencyBean();
            agency.setAgency_id("RTB11000003");
            agency.setContactsName("张英辉");
            agency.setCompanyPhone("18721115951");
            agency.setUserpId("410381198410013433");
            JSONObject jsonObj = agencyService.checkNameCertPid(agency);
            System.out.println(jsonObj.get(ConstantUtils.MSG_CODE));
            System.out.println(jsonObj.get(ConstantUtils.MSG_TEXT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void sendSMS(){
        try {
            AgencyBean agency = new AgencyBean();
            agency.setAgency_id("RTB11000003");
            agency.setContactsName("张英辉");
            agency.setCompanyPhone("17612152846");
            agency.setUserpId("410381198410013433");
            JSONObject jsonObj = agencyService.sendSMS("RTB99990000", "17612152846", "您好");
        } catch (QTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }
    
    

}

