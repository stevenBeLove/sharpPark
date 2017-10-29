package com.compass.messagemanage.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ardu.jms.ems.exception.QTException;

import com.compass.agency.model.AgencyApprove;
import com.compass.agency.model.Payuser;
import com.compass.agency.service.AgencyApproveService;
import com.compass.agency.service.AgencyService;
import com.compass.messagemanage.model.MobileCheckCode;
import com.compass.messagemanage.service.MessageService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.Format;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author DamonYang
 * @description {@link MessageController} version 1.0
 * @date 2015年11月10日 下午3:27:36
 */
@Controller
@RequestMapping("/message/message.do")
public class MessageController {
	
	private final static Log log = LogFactory.getLog(MessageController.class);
	@Autowired
	@Qualifier("messageService")
	private MessageService messageService;
	
	@Autowired
  @Qualifier("agencyApproveService")
	private AgencyApproveService agencyApproveService;
	
	
	 @Autowired
	 @Qualifier("agencyService")
	 private AgencyService agencyService;
	
	/**
	 * 查询出所有的验证码
	 * 
	 * @param agencyId
	 * @param execDate
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getCheckCode")
	@ResponseBody
	public Map<String, Object> getCheckCode(@RequestParam(value="userName") String userName,
											@RequestParam(value="telephone") String telephone,
											@RequestParam(value="startDate") String startDate,
											@RequestParam(value="endDate") String endDate,
			HttpServletRequest request) {
		log.info("查询所有符合条件的验证码");
		MobileCheckCode mobileCheckCode=new MobileCheckCode();
		mobileCheckCode.setUserName(userName);
		mobileCheckCode.setMobileNo(telephone);
		mobileCheckCode.setStartTime(startDate==null?null:startDate.replace("-", ""));
		mobileCheckCode.setEndTime(endDate==null?null:endDate.replace("-", ""));
		mobileCheckCode.setBranchId((String)request.getSession().getAttribute(ConstantUtils.AGENCYID));
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		Integer count = messageService.getCheckCodeCount(mobileCheckCode);
		int pagenumber = Integer.parseInt((page == null || page.equals( "0" )) ? "1"
				: page);
		int rownumber = Integer.parseInt((rows.equals("0")|| rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > count ? count : start + rownumber;
		List<MobileCheckCode> list = messageService.getCheckCode(mobileCheckCode,start, end);
		mobileCheckCode=null;
		return AjaxReturnInfo.setTable(list == null ? 0 : count, list);
	}
//	/**
//	 * 生成验证码并发送到指定手机
//	 * @return
//	 */
//    @RequestMapping(params = "method=sendMessage")
//    @ResponseBody
//    public AjaxReturnInfo sendMessage(@RequestParam(value = "phone") String phone, HttpServletRequest request) {
//        AjaxReturnInfo ajaxinfo = null;
//        MobileCheckCode mobileCheckCode = null;
//        log.info("查询所有符合条件的验证码");
//        // 更新phone，使其不为空
//        phone = (phone == null || "".equals(phone)) ? (String) request.getSession().getAttribute(ConstantUtils.USERPHONE) : phone;
//        request.getSession().setAttribute(ConstantUtils.USERPHONE, phone);
//        String random = Format.getRandom();
//        try {
//            String smsMsg = "尊敬的用户您好,验证码为" + random + "十分钟有效，请勿泄露";// 短信内容
//            // 判断是否是瑞通宝用户
//            /*
//             * Payuser user = agencyApproveService.queryRtbClientUser(phone); if
//             * (user == null) { ajaxinfo =
//             * AjaxReturnInfo.failed("抱歉，不存在该瑞通宝账号，请确认后重试！"); } else if
//             * (agencyApproveService.queryUserVipResult(user.getCustomerid()) <
//             * 2) { ajaxinfo = AjaxReturnInfo.failed(
//             * "抱歉，未能成功绑定！由于您的瑞通宝信用等级过低，请先在瑞通宝客户端中完善信用资料，信用等级达到2后，再进行绑定！"); }
//             * else {
//             */
//            agencyApproveService.jmsSend(smsMsg, phone);
//            // MySendMessage.getInstance().toSend(smsMsg);
//            mobileCheckCode = new MobileCheckCode();
//            mobileCheckCode.setCheckCode(random);
//            mobileCheckCode.setUserId((String) request.getSession().getAttribute(ConstantUtils.USERID));
//            mobileCheckCode.setBranchId((String) request.getSession().getAttribute(ConstantUtils.AGENCYID));
//            mobileCheckCode.setMobileNo(phone);
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MINUTE, 10);
//            mobileCheckCode.setEffTime(new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime()));
//            messageService.addCheckCode(mobileCheckCode);
//            ajaxinfo = AjaxReturnInfo.success("验证码已发送，请注意查收");
//            mobileCheckCode = null;
//            // }
//        } catch (QTException e) {
//            ajaxinfo = AjaxReturnInfo.failed(e.getRespMsg());
//            log.error(e.getMessage(), e);
//        } catch (Exception e) {
//            ajaxinfo = AjaxReturnInfo.failed("验证码发送错误");
//            log.error(e.getMessage(), e);
//        }
//        return ajaxinfo;
//    }

    /**
     * 校验验证码是否正确
     * 
     * @return
     */
    @RequestMapping(params = "method=checkCode")
    @ResponseBody
    public AjaxReturnInfo checkCode(@RequestParam(value = "phone") String phone, @RequestParam(value = "checkCode") String checkCode, HttpServletRequest request) {
        try {
            String userId = request.getSession().getAttribute(ConstantUtils.USERID).toString();
            String agencyIdS = request.getSession().getAttribute(ConstantUtils.AGENCYID).toString();

            MobileCheckCode mobileCheckCode = new MobileCheckCode();
            mobileCheckCode.setCheckCode(checkCode);
            mobileCheckCode.setCheckFlag("0");
            mobileCheckCode.setEffTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            mobileCheckCode.setMobileNo(phone);
            mobileCheckCode.setUserId(userId);
            if (messageService.checkIfExi(mobileCheckCode)) {
                request.getSession().setAttribute(ConstantUtils.USERPHONE, "success");
                mobileCheckCode = null;
               /* Payuser user = agencyApproveService.queryRtbClientUser(phone);
                if(user!=null && user.getUserid().equals(phone)){
                    AgencyApprove approveUser = new AgencyApprove();
                    //机构编码
                    approveUser.setAgencyId(agencyIdS);
                    //用户Id
                    approveUser.setUserid(userId);
                    //手机号
                    approveUser.setMobileno(phone);
                    //认证时间
                    approveUser.setApprovedt(CommonDate.currentTimeWithFormat(CommonDate.YYYYMMDDHHMMSS));
                    agencyApproveService.insertApproveAgency(approveUser);
                    return  AjaxReturnInfo.success("激活成功！");
                }else{
                    return AjaxReturnInfo.failed("输入的手机号码和验证的手机号码不一致！");
                }*/
                return  AjaxReturnInfo.success("激活成功！");
            } else {
                mobileCheckCode = null;
                return AjaxReturnInfo.failed("验证码错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return AjaxReturnInfo.failed("验证出错");
        }
    }
	
}
