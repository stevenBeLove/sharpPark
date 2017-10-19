/** 
 * 包名: package com.compass.agency.service; <br/> 
 * 添加时间: 2017年4月5日 下午5:56:15 <br/> 
 */
package com.compass.agency.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compass.constans.InterfaceNameConstans;
import com.compass.paramater.model.RtbParamter;
import com.compass.paramater.service.RtbParamterService;
import com.compass.utils.NumberFormat;

/** 
 * 类名: RtbParamterServiceJunit <br/> 
 * 创建者: zhangyinghui. <br/> 
 * 添加时间: 2017年4月5日 下午5:56:15 <br/> 
 * 版本： JDK 1.6 RtbPlatform 1.0
 */
public class RtbParamterServiceJunit {
    
//    static RtbParamterService   rtbParamterService;
//    
//    @BeforeClass
//    public static void setUpBeforeClass() {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
//        rtbParamterService = (RtbParamterService) ctx.getBean("rtbParamterService");
//    }
    
    @Test
    public void getParamter(){
        try {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/datasource.spring.xml");
            RtbParamterService rtbParamterService = (RtbParamterService) ctx.getBean("rtbParamterService");
            RtbParamter para = new RtbParamter();
            para.setStatus(InterfaceNameConstans.VALUE_1);
            para.setPmNo(InterfaceNameConstans.DAYS);
            RtbParamter findObj = rtbParamterService.getRtbParamter(para);
            System.out.println(findObj.getPmValue().intValue());
        } catch (BeansException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void test(){
        System.out.println(NumberFormat.parseYuanFormat("0.020"));
        
    }
    
}

