package com.compass.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.agencyCost.model.Payproduct;
import com.compass.agencyCost.service.PayproductService;

public class InitDate {

    private static Logger   logger = LoggerFactory.getLogger(InitDate.class);
    
    public static List<Payproduct> payProductList;
    
    
    private PayproductService payproductService;
    
    public void init(){
        try {
            payProductList = payproductService.getAllPayproduct();
        } catch (Exception e) {
            logger.error(e.getMessage() ,e);
        }
    }
    
    
    /**
     * 【方法名】    : (获取交易类型). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 下午4:15:25 .<br/>
     * 【参数】： .<br/>
     * @param typeId
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String getProductdesc(String typeId){
        for (Payproduct payproduct : payProductList) {
            if(payproduct.getRtbType().equals(typeId)){
                return payproduct.getProductdesc();
            }
        }
        return "未知类型";
    }


    public void setPayproductService(PayproductService payproductService) {
        this.payproductService = payproductService;
    }
    
    
}
