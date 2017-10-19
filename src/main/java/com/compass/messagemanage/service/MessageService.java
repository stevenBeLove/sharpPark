package com.compass.messagemanage.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.messagemanage.model.MobileCheckCode;
import com.compass.users.service.UsersService;
import com.compass.utils.AbstractService;

/**
 * @author DamonYang
 * @description {@link MessageService} version 1.0
 * @date 2015年11月10日 下午3:38:57
 */
public class MessageService extends AbstractService{
	private static Logger log = LoggerFactory.getLogger(MessageService.class);
	//得到所有满足条件的验证码的总数
	public int getCheckCodeCount(MobileCheckCode mobileCheckCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", mobileCheckCode.getUserName());
		map.put("userPhone", mobileCheckCode.getMobileNo());
		map.put("startTime", mobileCheckCode.getStartTime());
		map.put("endTime", mobileCheckCode.getEndTime());
		map.put("agencyId", mobileCheckCode.getBranchId());
		return Integer.parseInt(dao.queryForObject("message.getCheckCodeCount", map).toString());
	}

	public List<MobileCheckCode> getCheckCode(MobileCheckCode mobileCheckCode,
			int start, int end) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", mobileCheckCode.getUserName());
		map.put("userPhone", mobileCheckCode.getMobileNo());
		map.put("startTime", mobileCheckCode.getStartTime());
		map.put("endTime", mobileCheckCode.getEndTime());
		map.put("agencyId", mobileCheckCode.getBranchId());
		map.put("end", end);
		map.put("start", start);
		List<MobileCheckCode> mobileCheckCodeList=dao.queryForList("message.getCheckCode", map);
		return mobileCheckCodeList;
	}

	/**
	 * 插入手机验证码
	 * @param pwd
	 * @param userId
	 * @return
	 */
	public void addCheckCode(MobileCheckCode mobileCheckCode) {
		log.info("把指定用户的所有验证码改为已验证");
		Map<String,String> map = new HashMap<String, String>();
		map.put("userId", mobileCheckCode.getUserId());
		map.put("userPhone", mobileCheckCode.getMobileNo());
		map.put("branchId", mobileCheckCode.getBranchId());
		map.put("checkCode", mobileCheckCode.getCheckCode());
		map.put("effTime", mobileCheckCode.getEffTime());
		dao.update("message.updateAllToValid", map);
		dao.insert("message.insertCheckCode", map);
		
		
	}
	public boolean checkIfExi(MobileCheckCode mobileCheckCode) {
		log.info("验证是否存在");
		Map<String,String> map = new HashMap<String, String>();
		map.put("userId", mobileCheckCode.getUserId());
		map.put("userPhone", mobileCheckCode.getMobileNo());
		map.put("branchId", mobileCheckCode.getBranchId());
		map.put("checkCode", mobileCheckCode.getCheckCode());
		map.put("effTime", mobileCheckCode.getEffTime());
		int count=(Integer)dao.queryForObject("message.queryCount", map);
		if(count!=0){
			dao.update("message.updateAllToValid", map);
			dao.update("message.updatePhone", map);
			return true;
		}
		return false;
	}

}
