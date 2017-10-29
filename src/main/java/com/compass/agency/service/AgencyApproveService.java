package com.compass.agency.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyApprove;
import com.compass.agency.model.Payuser;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;
import com.compass.utils.PropertyPlaceholderConfigurerExt;
import com.compass.utils.PubTibcoSend;

/**
 * <pre>
 * 【类型】: AgencyApproveService <br/> 
 * 【作用】: 通过认证 <br/>  
 * 【时间】：2017年2月21日 下午4:40:33 <br/> 
 * 【作者】：yinghui zhang <br/> 
 * </pre>
 */
public class AgencyApproveService extends AbstractService {

    private final Log log = LogFactory.getLog(AgencyApproveService.class);
    /**
     * 【方法名】    : (查询是否已经通过认证). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月21日 下午5:02:44 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public AgencyApprove queryApproveAgency(String agencyId)throws Exception{
        return (AgencyApprove) dao.queryForObject("AgencyApprove.queryApproveAgency",agencyId);
    }
    
    /**
     * 【方法名】    : (插入认证表). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月21日 下午5:03:01 .<br/>
     * 【参数】： .<br/>
     * @param approve AgencyApprove
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer insertApproveAgency(AgencyApprove approve)throws Exception{
        return dao.insert("AgencyApprove.insertApproveAgency", approve);
    }
    
    /**
     * 【方法名】    : (发送短信验证码). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午1:22:38 .<br/>
     * 【参数】： .<br/>
     * @param content
     * @param mobileno
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
//    public JSONObject jmsSend(String content, String mobileno) throws Exception {
//        /** 组装发送报文 */
//        Map<String, Object> item = new HashMap<String, Object>();
//        item.put("P_TRANCODE", "DWSMS1001");
//        item.put("SMSTYPE", "1");
//        item.put("BRANCHID", "00800653");
//        item.put("TYPE", "1");
//        item.put("APPUSER", "ruitongbao");
//        item.put("CHANNEL", "瑞通宝综合管理系统");
//        item.put("CONTENT", content);//验证码【6668】
//        item.put("MOBILENO", mobileno);//mobileno
//        item.put("ORDERID", CommonDate.getDateStr());
//        Properties properties = PropertyPlaceholderConfigurerExt.getProperties();
//        if (StringUtils.isEmpty(properties.get("common.tibco.url"))) {
//            throw new QTException("验证失败！找不到队列链接[common.tibco.url]的配置信息！");
//        }
//        if (StringUtils.isEmpty(properties.get("common.tibco.user"))) {
//            throw new QTException("验证失败！找不到队列链接[common.tibco.user]的配置信息！");
//        }
//        if (StringUtils.isEmpty(properties.get("common.tibco.password"))) {
//            throw new QTException("验证失败！找不到队列链接[common.tibco.password]的配置信息！");
//        }
//        if (StringUtils.isEmpty(properties.get("common.tibco.encode"))) {
//            throw new QTException("验证失败！找不到队列链接[common.tibco.encode]的配置信息！");
//        }
//        if (StringUtils.isEmpty(properties.get("sms.tibco.sendtcp"))) {
//            throw new QTException("验证失败！找不到队列链接[sms.tibco.sendtcp]的配置信息！");
//        }
//        if (StringUtils.isEmpty(properties.get("sms.tibco.retcp"))) {
//            throw new QTException("验证失败！找不到队列链接[sms.tibco.retcp]的配置信息！");
//        }
//        String url = properties.get("common.tibco.url").toString();
//        String user = properties.get("common.tibco.user").toString();
//        String password = properties.get("common.tibco.password").toString();
//        String encode = properties.get("common.tibco.encode").toString();
//        String sendtcp = properties.get("sms.tibco.sendtcp").toString();
//        String retcp = properties.get("sms.tibco.retcp").toString();
//        JSONObject receiveJson = null;
//        int serverTimeOut = 30000;
//        try {
//            receiveJson = PubTibcoSend.sendCoreInfo(url, encode, user, password, "QueueConnectionFactory", sendtcp, retcp, serverTimeOut, item, false);
//        } catch (QTException e) {
//            log.error(e.getMessage(), e);
//            if (receiveJson == null) {
//                receiveJson = new JSONObject();
//            }
//            receiveJson.put("P_MSG_CODE", "99999");
//            receiveJson.put("P_MSG_TEXT", e.getRespMsg());
//        }
//        return receiveJson;
//    }
    
    /**
     * 【方法名】    : (查询客户端用户). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午3:00:32 .<br/>
     * 【参数】： .<br/>
     * @param mobile 登陆手机号
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Payuser queryRtbClientUser(String mobile)throws Exception{
        return (Payuser) dao.queryForObject("AgencyApprove.queryRtbClientUser",mobile);
    }
    
    /**
     * 【方法名】    : (查询信用等级). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午7:17:39 .<br/>
     * 【参数】： .<br/>
     * @param customerId 客户Id
     * @return 符合条件的数据数量
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer queryPaycustomerTag(String customerId)throws Exception{
        return (Integer) dao.queryForObject("AgencyApprove.queryPaycustomerTag",customerId);
    }
    /**
     * 【方法名】    : (查询信用等级). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午7:19:37 .<br/>
     * 【参数】： .<br/>
     * @param customerId 客户Id
     * @return 符合条件的数据数量
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer queryPrepAdvanceVip(String customerId)throws Exception{
        return (Integer) dao.queryForObject("AgencyApprove.queryPrepAdvanceVip",customerId);
    }
    
    /**
     * 【方法名】    : (查询信用等级). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午7:21:16 .<br/>
     * 【参数】： .<br/>
     * @param customerId 客户Id
     * @return 符合条件的数据数量
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer queryCsBindCard(String customerId)throws Exception{
        return (Integer) dao.queryForObject("AgencyApprove.queryCsBindCard",customerId);
    }
    
    /**
     * 【方法名】    : (查询信用等级). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月22日 下午7:30:02 .<br/>
     * 【参数】： .<br/>
     * @param customerId
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer queryUserVipResult(String customerId)throws Exception{
        int vipResult = 0;
        int cusCount = queryPaycustomerTag(customerId);
        if(cusCount >0){
            vipResult += 1; 
        }
        int vipCount = queryPrepAdvanceVip(customerId);
        if(vipCount > 0){
            vipResult += 1; 
        }
        int bindCount = queryCsBindCard(customerId);
        if(bindCount >0 ){
            vipResult += 1; 
        }
        return vipResult;
    }
    
    
}
