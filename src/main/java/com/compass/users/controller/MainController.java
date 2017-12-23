package com.compass.users.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.compass.agency.model.AgencyBean;
import com.compass.agency.service.AgencyApproveService;
import com.compass.agency.service.AgencyService;
import com.compass.authority.service.AuthorityService;
import com.compass.common.FrConstants;
import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.utils.CommonDate;
import com.compass.utils.CommonEnums.AgencyStatus;
import com.compass.utils.ConstantUtils;

@Controller
public class MainController {
   
    private static Logger       logger = LoggerFactory.getLogger(MainController.class);
    /**
     * 认证接口
     */
    @Autowired
    @Qualifier("agencyApproveService")
    private AgencyApproveService agencyApproveService;
    
    /**
     * 实名认证service
     */
    @Autowired
    @Qualifier("agencyService")
    private AgencyService agencyService;
    
    /**
     *配置参数
     */
    @Autowired
    @Qualifier("authorityService")
    private AuthorityService authorityService;
    
    @Autowired
    @Qualifier("parkService")
    private ParkService parkService;
    
    /**
     * 【方法名】    : (跳转到主页). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月21日 上午11:14:10 .<br/>
     * 【参数】： .<br/>
     * @param req
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping("/main.do")
    public ModelAndView forwardCostView(HttpServletRequest req) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
        String userLoginName = req.getSession().getAttribute(ConstantUtils.USERLOGINNAME).toString();
        try {
        	List<ParkBean> list = new ArrayList<ParkBean>();
            boolean isWhiteListUser = getIsWhiteListUser(req.getSession());
            paraMap.put("isWhiteListUser", isWhiteListUser);
            // 验证是否实名
            AgencyBean agencyBean = agencyService.getAgency(agencyId);
            boolean flag = false;
            boolean approve = true;
            if (agencyBean != null) {
                if (agencyBean.getSomeonePhone() == null || "".equals(agencyBean.getSomeonePhone())) {
                    // 和二期上线时间进行比较
                    if (CommonDate.compareDateTime(agencyBean.getCreateDt(), FrConstants.ONLINETIME) == -1) {
                        approve = false;
                    }
                }
                if (agencyBean.getAgency_status().equals(AgencyStatus.realNamePass.getVal())) {
                    flag = true;
                }
            }
            // 判断是否是管理员登陆
            if (agencyId.equals(userLoginName)) {
                paraMap.put("levelOne", true);
            } else {
                paraMap.put("levelOne", false);
            }
            String isAdmin = "";
            if(ConstantUtils.CENTERCODE.equals(agencyId)){
            	isAdmin = "1";
            	ParkBean parkBean = new ParkBean();
        		parkBean.setStart(0);
        		parkBean.setEnd(1000);
        		list = parkService.getParkAll(parkBean);
            }
            paraMap.put("isAdmin", isAdmin);
            paraMap.put("list", list);
            paraMap.put("agencyBean", agencyBean);
            paraMap.put("approve", approve);
            paraMap.put(ConstantUtils.AGENCY_CERTIFICATION, flag);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ModelAndView("main", paraMap);
    }

    /**
     * 【方法名】    : (查询是否为白名单用户). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月21日 下午5:23:32 .<br/>
     * 【参数】： .<br/>
     * @param session
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    private boolean getIsWhiteListUser(HttpSession session){
        boolean auth = false;
        String userIds = authorityService.getParamObject("WhiteListUser");
        if(userIds!=null && !"".equals(userIds)){
          String[] userLoginNameArray = userIds.split(",");
          String loginName = (String) session.getAttribute(ConstantUtils.USERLOGINNAME);
          if(userLoginNameArray!=null && userLoginNameArray.length > 0){
              for (String uloginName : userLoginNameArray) {
                  if(uloginName.equals(loginName)){
                      auth = true;
                      break;
                  }
              }
          }
        }
        return auth;
    }

}
