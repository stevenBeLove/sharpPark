package com.compass.taskService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.compass.agencyCost.service.AgencyCostService;

/**
 * <pre>
 * 【类型】: AgencyCostTask <br/> 
 * 【作用】: 分润费率定时器. <br/>  
 * 【时间】：2017年2月17日 下午9:36:16 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
@Component
public class AgencyCostTask {

    /**
     * 分润成本设置
     */
    @Autowired
    @Qualifier("agencyCostService")
    private AgencyCostService   agencyCostService;
    
    private static Logger   logger = LoggerFactory.getLogger(AgencyCostTask.class);
    
  
    
    /**
     * 【方法名】    : (执行定时任务). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月19日 下午8:21:25 .<br/>
     * 【参数】： .<br/> .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    // 每天上午01:00触发
    //@Scheduled(cron = "0 15 00 * * ?")
/*    public void taskUserCostService(){
        try {
            List<String>  list = agencyCostService.getAllCostReg();
            if(list!=null && list.size()>0){
              for (String agencyId : list) {
                  //删除生效中成本费率
                  agencyCostService.deleteIsUseWithAgencyId(agencyId);
                  //复制待生效表中的数据到生效表
                  agencyCostService.insertReplica(agencyId);
                  //复制待生效表数据到日志表
                  agencyCostService.insertReplicaLog(agencyId);
              }
            }
            //清空临时表数据
            agencyCostService.deleteAgencyCostReg();
            logger.info("分润成本设置,定时任务执行完毕!");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    
    */
    
}
