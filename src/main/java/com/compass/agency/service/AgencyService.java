package com.compass.agency.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.ardu.jms.ems.exception.QTException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.compass.agency.model.AgencyBean;
import com.compass.agency.model.AgencySetup;
import com.compass.agency.model.AgentreeBean;
import com.compass.agency.model.TreeNodesBean;
import com.compass.constans.InterfaceNameConstans;
import com.compass.paramater.model.RtbParamter;
import com.compass.paramater.service.RtbParamterService;
import com.compass.userrole.model.UserAddInfo;
import com.compass.userrole.service.UserRoleService;
import com.compass.users.model.UserBean;
import com.compass.utils.AbstractService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.I18nUtils;
import com.imobpay.base.services.FrServer;

public class AgencyService extends AbstractService {
    
    /**
     * Dubbo分润服务
     */
    private FrServer frServer;
    
    /**
     * 瑞通宝参数操作service
     */
    private RtbParamterService rtbParamterService;
    
    /**
     * 用户service
     */
    private UserRoleService userRoleService;
    
    /**
     * 
     * 【方法名】    : (注入rtbParamterService). <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:26:27 .<br/>
     * 【参数】： .<br/>
     * @param rtbParamterService .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public void setRtbParamterService(RtbParamterService rtbParamterService) {
        this.rtbParamterService = rtbParamterService;
    }
    
    /**
     * 
     * 【方法名】    : (注入userRoleService). <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:26:58 .<br/>
     * 【参数】： .<br/>
     * @param userRoleService .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * 机构管理查询
     * 
     * @param agency
     *            n
     * @return
     */
    @SuppressWarnings("unchecked")
    /**
     * 获取机构信息
     */
    public List<AgencyBean> getAgency(AgencyBean agency) {

        return dao.queryForList("AGENCY.getAgency", agency);
    }

    /**
     * 查询机构的数量
     * 
     * @param agency
     * @return
     */
    public Integer getAgencyCount(AgencyBean agency) {
        return (Integer) dao.queryForObject("AGENCY.getAgencyCount", agency);
    }

    /**
     * 查询机构的数量
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getAgencyAll(AgencyBean agency) {
        return dao.queryForList("AGENCY.getAgencyAll", agency);
    }

    /**
     * 机构审核查询
     * 
     * @param agency
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getAgencyCheck(AgencyBean agency) {

        return dao.queryForList("AGENCY.getAgencyCheck", agency);
    }

    /**
     * 获取当前登录机构的所有下级机构
     * 
     * @param agencyId
     * @param systemId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgentreeBean> getAgencyTree(String agencyId, String systemId, String flag1, String flag2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        map.put("flag1", flag1);
        map.put("flag2", flag2);
        return dao.queryForList("AGENCY.getAgencytree", map);
    }

    /**
     * 【方法名】    : (添加机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月18日 上午10:44:38 .<br/>
     * 【参数】： .<br/>
     * @param agency AgencyBean
     * @return int
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int AddAgency(AgencyBean agency) throws Exception {
        saveAgencySetup(agency);
        return dao.insert("AGENCY.addAgency", agency);
    }
  
    /**
     * 修改机构
     * 
     * @param agency
     * @return
     */
    public int EditAgency(AgencyBean agency) {
        return dao.update("AGENCY.editAgency", agency);
    }

    /**
     * 删除机构（逻辑删除 0表示无效）
     * 
     * @param agencyIds
     * @return
     */
    public int delAgency(String agencyIds) {
        return dao.update("AGENCY.delAgency", agencyIds);
    }

    /**
     * 验证指定编号的机构是否存在
     * 
     * @param agencyId
     * @return
     */
    public Integer existAgency(String agencyId) {
        return (Integer) dao.queryForObject("AGENCY.existAgency", agencyId);
    }

    /**
     * 生成机构编号
     * 
     * @param agencyId
     * @return
     */
    public String getgencyId(String agencyId) {
        String agid = dao.queryForObject("AGENCY.getMaxagencyId", agencyId).toString().trim(); // 获得当前机构中最大的编号
        Integer z = Integer.parseInt(agid) + 1;
        String k = z.toString();
        System.out.println(k);
        if (k.length() == 1) {
            agid = "000" + z;
        } else if (k.length() == 2) {
            agid = "00" + z;
        } else if (k.length() == 3) {
            agid = "0" + z;
        } else {
            agid = "" + z;
        }
        return agid;
    }

    /**
     * 根据机构编号获得自己直接的子机构
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getChildAgencyList(String agencyId, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        return dao.queryForList("AGENCY.getChildAgencyList", map);
    }

    /**
     * 14.11.03 需求新增 获得最上级机构
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getSuperiorAgencyList(String agencyId, String systemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("systemId", systemId);
        return dao.queryForList("AGENCY.getSuperiorAgencyList", map);
    }

    /**
     * 修改机构
     * 
     * @param map
     * @return
     */
    public int checkAgency(Map<String, Object> map) {
        return dao.update("AGENCY.checkagency", map);
    }

    /**
     * 获取省信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getProvince() {
        return dao.queryForList("AGENCY.getProvince");
    }

    /**
     * 获取市信息
     * 
     * @param provinceId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getCity(String provinceId) {
        return dao.queryForList("AGENCY.getCity", provinceId);
    }

    @SuppressWarnings("unchecked")
    public List<AgencyBean> getArea(String provinceId) {
        return dao.queryForList("AGENCY.getArea", provinceId);
    }

    /**
     * 获取当前机构机构的直接子机构
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getChildAgency(String agencyId) {
        return dao.queryForList("AGENCY.getChilAgency", agencyId);
    }

    /**
     * 根据机构编号查询是否有终端
     * 
     * @param agencyId
     * @return
     */
    public int checkAgencyTerminal(String agencyId) {
        return (Integer) dao.queryForObject("AGENCY.checkAgencyTerminal", agencyId);
    }

    /**
     * 检查是否有子机构
     * 
     * @param agencyId
     * @return
     */
    public int checkHasChild(String agencyId) {
        // TODO Auto-generated method stub
        return (Integer) dao.queryForObject("AGENCY.checkHasChild", agencyId);
    }

    /**
     * 根据机构编号获得机构级别
     * 
     * @param startAgency
     * @param agencyId
     * @return
     */
    public String getAgencyLevel(String startAgency, String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startAgency", startAgency);
        map.put("agencyId", agencyId);
        return (String) dao.queryForObject("AGENCY.getAgencyLevel", map);
    }

    /**
     * 获取当前登录的机构并加载到tree里面
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAgencyone(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return dao.queryForList("AGENCY.getAgencyone", map);
    }

    /**
     * 递归更新机构的定制商归属机构编号
     * 
     * @param agencyId
     * @param vestBrand
     * @return
     */
    public int updateVestBrand(String agencyId, String vestBrand) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        map.put("vestBrand", vestBrand);
        return dao.update("AGENCY.updateVestBrand", map);
    }

    /**
     * 机构树数据
     * 
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TreeNodesBean> getTreeList(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyId", agencyId);
        return dao.queryForList("AGENCY.getTreeList", map);
    }

    public String getVestagencyByAgencyid(String agencyId) {
        Object vestagency = dao.queryForObject("AGENCY.getVestagencyByAgencyid", agencyId);
        if(vestagency!=null){
        	return (String)vestagency;
        }else{
        	return null;
        }
    }

    /**
     * 
     * 【方法名】 : 获取弹框时间. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月2日 上午9:54:21 .<br/>
     * 【参数】： .<br/>
     * 
     * @param username
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    public UserBean getAgencyObject(String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userid", username);
        return (UserBean) dao.queryForObject("AGENCY.getAgencyObject", map);
    }
    
    /**
     * 
     * 【方法名】    : (获取机构SystemId). <br/> 
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年12月2日 下午4:03:19 .<br/>
     * 【参数】： .<br/>
     * @param username
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: fuyu 修改描述： .<br/>
     * <p/>
     */
    public AgencyBean getAgencySystemId(String agencyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agency_id", agencyId);
        return (AgencyBean) dao.queryForObject("AGENCY.getAgencySystemId", map);
    }
    
   
    /**
     * 【方法名】    : (通过代理商ID查询代理商ID). <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月10日 下午5:08:14 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public AgencyBean getAgency(String agencyId){
    	return (AgencyBean) dao.queryForObject("AGENCY.getAgencyBean", agencyId);
    }
    /**
     * 【方法名】    : (更新我的机构信息). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年1月10日 下午5:35:34 .<br/>
     * 【参数】： .<br/>
     * @param bean
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int updateMyAgency(AgencyBean bean){
        return  dao.update("AGENCY.updateMyAgency", bean);
    }
    
    /**
     * 【方法名】    : (获取下拉框机构数量). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 上午10:45:07 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String,Object>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer getAgencyCombCount(Map<String,Object> map)throws Exception {
        return (Integer) dao.queryForObject("AGENCY.getAgencyCombCount", map);
    }

    /**
     * 【方法名】    : (获取下拉框机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 上午10:45:41 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String,Object>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getAgencyCombAll(Map<String,Object> map)throws Exception {
        return dao.queryForList("AGENCY.getAgencyComb", map);
    }
    
    
    /**
     * 【方法名】    : (获取下拉框机构数量). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 上午10:45:07 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String,Object>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public Integer getAgencyCombChildCount(Map<String,Object> map)throws Exception {
        return (Integer) dao.queryForObject("AGENCY.getAgencyCombChildCount", map);
    }

    /**
     * 【方法名】    : (获取下拉框机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月15日 上午10:45:41 .<br/>
     * 【参数】： .<br/>
     * @param map Map<String,Object>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @SuppressWarnings("unchecked")
    public List<AgencyBean> getAgencyCombChild(Map<String,Object> map)throws Exception {
        return dao.queryForList("AGENCY.getAgencyCombChild", map);
    }
    
    /**
     * 根据userId 获取 customerId
     */
    public String queryCustomerId(String userPhone)throws Exception {
        return (String) dao.queryForObject("AGENCY.queryCustomerId", userPhone);
    }
    
    public String queryUserPhone(String userId)throws Exception {
        return (String) dao.queryForObject("AGENCY.queryUserPhone", userId);
    }
    
    /**
     * 【方法名】    : (实名认证接口). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月29日 下午3:17:12 .<br/>
     * 【参数】： .<br/>
     * @param agency AgencyBean
     * @return .<br/>
     * @throws QTException
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public JSONObject checkNameCertPid(AgencyBean agency) throws QTException {
        JSONObject item = new JSONObject();
        if (StringUtils.isEmpty(agency.getContactsName())) {
            throw new QTException("法人姓名不能为空!");
        }
        if (StringUtils.isEmpty(agency.getUserpId())) {
            throw new QTException("证件号码不能为空!");
        }
        item.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.REAL_NAME_AUTH);
        item.put(InterfaceNameConstans.USERNAME, agency.getContactsName());
        item.put(InterfaceNameConstans.CERT_PID, agency.getUserpId());
        String execute = frServer.execute(item.toJSONString());
        return JSONObject.parseObject(execute);
    }
    
    /**
     * 【方法名】 : (查询实名认证信息). <br/>
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月10日 下午1:44:49 .<br/>
     * 【参数】： .<br/>
     * 
     * @param companyPhone
     *            String
     * @return AgencyBean
     *         <p>
     *         修改记录.<br/>
     *         修改人: yinghui zhang 修改描述： .<br/>
     *         <p/>
     */
    public AgencyBean getAgencyBeanWithMap(String companyPhone) {
        return (AgencyBean) dao.queryForObject("AGENCY.getAgencyBeanWithMap", companyPhone);
    }
    
    /***
     * 【方法名】    : (调用dubbo服务创建机构). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月31日 下午7:45:57 .<br/>
     * 【参数】： .<br/>
     * @param agency AgencyBean
     * @return JSONObject
     * @throws QTException .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public JSONObject addAgencyByDubbo(AgencyBean agency) throws QTException {
        JSONObject item = new JSONObject();
        item.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.JYM_OPEN_DOWN_AGENCY);
        //上级机构Id
        item.put(InterfaceNameConstans.AGENCY_ID, agency.getOnlinechannel());
        item.put(InterfaceNameConstans.AGENCY_NAME, agency.getCompanyName());
        //AppuserType
        item.put(InterfaceNameConstans.LOGIN_APP_USER_TYPE, "ruiyuanbao_ruitongbao");
        item.put(InterfaceNameConstans.SOMEONE_NAME, agency.getSomeoneName());
        item.put(InterfaceNameConstans.SOMEONE_PHONE, agency.getSomeonePhone());
        item.put(InterfaceNameConstans.PROVINCE_ID, agency.getProvinceId());
        item.put(InterfaceNameConstans.CITY_ID, agency.getCityId());
        item.put(InterfaceNameConstans.COMPANY_ADDR, agency.getCompanyAddr());
        String execute = frServer.execute(item.toJSONString());
        return JSONObject.parseObject(execute);
    }
    

    public void setFrServer(FrServer frServer) {
        this.frServer = frServer;
    }
    
    /**
     * 
     * 方法名： queryCertification(实名认证查询).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月30日.<br/>
     * 创建时间：下午3:16:11.<br/>
     * 参数者异常：@param agencyId
     * 参数者异常：@return
     * 参数者异常：@throws Exception .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public AgencyBean queryCertification(String agencyId)throws Exception{
        return (AgencyBean)dao.queryForObject("AGENCY.queryCertification", agencyId);
    }
    
    /**
     * 
     * 【方法名】    : (实名认证). <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:21:37 .<br/>
     * 【参数】： .<br/>
     * @param agency 机构实体类
     * @param userId 用户ID
     * @return
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public int updateCertification(AgencyBean agency, String userId) throws Exception{
        int num = userRoleService.queryUserAddInfo(userId);
        if (num > 0){
            userRoleService.updateUserAddInfo(userId);
        } else {
            saveUserInfo(userId);
        }
        return dao.update("AGENCY.updateCertification", agency);
        
    }
    
    /**
     * 方法名： getAgencyOnlinechannelCount(根据登录名查询是否是一级机构).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年3月31日.<br/>
     * 创建时间：下午2:27:39.<br/>
     * 参数者异常：@param loginName
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public int queryAgencyOnlinechannelCount(String loginName){
        return (Integer)dao.queryForObject("AGENCY.queryAgencyOnlinechannelCount", loginName);
    }
    
    /**
     * 【方法名】    : (发送短信). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月1日 下午7:16:58 .<br/>
     * 【参数】： .<br/>
     * @param agencyId 代理商Id
     * @param monileNo 手机号码
     * @param contactsName 法人名称
     * @return JSONObject
     * @throws QTException .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public JSONObject sendSMS(String agencyId, String monileNo, String contactsName) throws QTException {
        /**
         * 给下级级发送短信
         */
        JSONObject smsJson = new JSONObject();
        smsJson.put(InterfaceNameConstans.SERVERJYM, InterfaceNameConstans.JYM_SENDSMS);
        smsJson.put(InterfaceNameConstans.SMS_PARAM, InterfaceNameConstans.PAYFRSERVER_006);
        smsJson.put(InterfaceNameConstans.NOWDATE_UPPER, CommonDate.getNowDate());
        smsJson.put(InterfaceNameConstans.BRANCHID_UPPER, "00800625"); //分润宝瑞源宝机构号
        smsJson.put(InterfaceNameConstans.APPUSER_UPPER, "fenrunbao");

        JSONArray mobArrDown = new JSONArray();
        mobArrDown.add(monileNo);
        smsJson.put(InterfaceNameConstans.MOBILENO_UPPER, mobArrDown);
        smsJson.put(InterfaceNameConstans.CUSTOMERID_UPPER, new JSONArray());
        smsJson.put(InterfaceNameConstans.TIMETYPE, InterfaceNameConstans.VALUE_1);
        smsJson.put(InterfaceNameConstans.SOURCECHANNAL, InterfaceNameConstans.FR_SERVER);
        smsJson.put(InterfaceNameConstans.COMPANY_NAME, contactsName);
        smsJson.put(InterfaceNameConstans.AGENCY_ID, agencyId);
        frServer.execute(smsJson.toString());
        return smsJson;
    }
    
    /**
     *  
     * 方法名： queryCompanyNamelCount(查询机构名是否重复).<br/>
     * 创建者：简思文.<br/>
     * 创建日期：2017年4月8日.<br/>
     * 创建时间：下午2:46:08.<br/>
     * 参数者异常：@param companyName
     * 参数者异常：@return .<br/>
     * 其它内容： JDK 1.6 Rtb 1.0.<br/>
     */
    public int queryCompanyNamelCount(String companyName){
        return (Integer)dao.queryForObject("AGENCY.queryCompanyNamelCount", companyName);
    }
    
    /**
     * 【方法名】    :完善机构信息. <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月10日 下午8:33:32 .<br/>
     * 【参数】： .<br/>
     * @param agencyBean AgencyBean
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public int updateByPrimaryKey(AgencyBean agencyBean)throws Exception{
        return dao.update("AGENCY.updateByPrimaryKey", agencyBean);
    }
    
    /**
     * 【方法名】    : (添加机构注册信息). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年4月12日 下午9:16:41 .<br/>
     * 【参数】： .<br/>
     * @param agencyBean .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public void saveAgencySetup(AgencyBean agencyBean) throws Exception{
        AgencySetup setp = new AgencySetup();
        setp.setAgencyId(agencyBean.getAgency_id());
        setp.setSetDate(CommonDate.currentDateWithFormatStr(CommonDate.YYYYMMDD));
        setp.setSetTime(CommonDate.currentDateWithFormatStr(CommonDate.HHMMSS));
        setp.setRegisterUrl("");
        setp.setStatus(InterfaceNameConstans.VALUE_1);
        setp.setAgencymac("");
        setp.setIsNote(InterfaceNameConstans.VALUE_1);
        setp.setActTime(CommonDate.currentDateWithFormatStr(CommonDate.YYYYMMDDHHMMSS));
        setp.setIsNoteParent(InterfaceNameConstans.VALUE_1);
        setp.setIsNoteLogin(InterfaceNameConstans.VALUE_1);
        dao.insert("AGENCY.addAgencySetup", setp);
    }
    
    /**
     * 
     * 【方法名】    :用户附加信息表添加 (这里用一句话描述这个方法的作用). <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 上午10:21:09 .<br/>
     * 【参数】： .<br/>
     * @param userId 用户ID
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public void saveUserInfo(String userId)throws Exception{
        UserAddInfo info = new UserAddInfo();
        info.setHomeId(ConstantUtils.HOME_ID);
        info.setUserid(userId);
        info.setIsApprove(UserAddInfo.APPROVE);
        info.setUsercodeTime(CommonDate.currentTimeWithFormat(CommonDate.YYYYMMDDHHMMSS));
       
        //查询参数配置信息
        RtbParamter para = new RtbParamter();
        para.setStatus(InterfaceNameConstans.VALUE_1);
        para.setPmNo(InterfaceNameConstans.DAYS);
        RtbParamter findObj = rtbParamterService.getRtbParamter(para);
        if (findObj!=null){
            info.setDays(findObj.getPmValue().intValue());
        } else {
            info.setDays(InterfaceNameConstans.DAYS7);
        }
        userRoleService.insertUserAddInfo(info);
    }

}
