/**
 * 
 */
package com.compass.setting.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.compass.setting.model.SettingManageBean;
import com.compass.setting.service.SettingManageService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * @author wangLong
 * 文件提供系统
 */


@Controller
@RequestMapping("/setting/setting.do")
public class SettingManageController {

	@Autowired
	@Qualifier("settingManageService")
	private SettingManageService settingManageService;

	
	
	/**
	 * 获得文件提供系统列表
	 * @param systemCode
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getSettings")
	@ResponseBody
	public Map<String, Object> getSettings(
			@RequestParam(value = "systemCode") String systemCode,
			@RequestParam(value = "status") String status,
			HttpServletRequest request) {
		List<SettingManageBean> list = settingManageService.getSettings(systemCode,status);
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
				: page);
		int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
				: rows);
		int start = (pagenumber - 1) * rownumber;
		int end = (start + rownumber) > list.size() ? list.size() : start + rownumber;
		List<SettingManageBean> settingList=new ArrayList<SettingManageBean>();
		if (list != null && list.size() > 0) {
			for(int i=start;i<end;i++){
				settingList.add(list.get(i));
			}
		}
		settingList=null;
		return AjaxReturnInfo.setTable(list==null?0:list.size(), list);
	}
	
	/**
	 * 获得文件路径（下拉框使用）
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=getFilePath")
	@ResponseBody
	public List<Map<String,Object>> getFilePath(
			HttpServletRequest request) {
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> clist=new ArrayList<Map<String,Object>>();
			List<SettingManageBean> dtypeList = settingManageService.getFilePath();
			for(int i=0;i<dtypeList.size();i++){
				SettingManageBean bean = dtypeList.get(i);
				Map<String,Object> map1=new HashMap<String,Object>();
				map1.put("id", bean.getSettingId());
				map1.put("text", bean.getPath());
				Map<String,Object> map11=new HashMap<String ,Object>();
				
				map11.put("systemCode", bean.getSystemCode());
				map11.put("systemName", bean.getSystemName());
				map11.put("settingStatus", bean.getSettingStatus());
				map11.put("settingStatusStr", bean.getSettingStatusStr());
				map11.put("path", bean.getPath());
				map11.put("splitStr", bean.getSplitStr());
				map11.put("fileName", bean.getFileName());
			
				map11.put("startFlag", bean.getStartFlag());
				map11.put("startFlagStr", bean.getStartFlagStr());
				map11.put("dealId", bean.getDealId());
				map11.put("serialNumber", bean.getSerialNumber());
				map11.put("transaCount", bean.getTransaCount());
				map11.put("terminalCode", bean.getTerminalCode());
				map11.put("dealData", bean.getDealData());
				
				map11.put("dealTime", bean.getDealTime());
				map11.put("dealStatus", bean.getDealStatus());
				map11.put("dealtypeId", bean.getDealtypeId());
				map11.put("dealDesc", bean.getDealDesc());
				map11.put("dealRebackCode", bean.getDealRebackCode());
				map11.put("bankCardNumber", bean.getBankCardnumber());
				map11.put("cost", bean.getCost());
				map11.put("merchantCode", bean.getMerchantCode());
				
				map11.put("settingDesc", bean.getSettingDesc());
				map11.put("createrId", bean.getCreaterId());
				map11.put("createDate", bean.getCreateDate());
				map1.put("attributes", map11);
				list.add(map1);
				map1=null;
				map11=null;
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", ConstantUtils.COMBOXONEID);
			map.put("text", "请选择文件路径");
			map.put("checked", true);
			map.put("children", list);
			clist.add(map);
			map=null;
			return clist;
		}
	
	/**
	 * 添加文件提供系统管理
	 * @param path
	 * @param systemCode
	 * @param fileName
	 * @param startFlag
	 * @param splitStr
	 * @param dealId
	 * @param serialNumber
	 * @param transaCount
	 * @param terminalCode
	 * @param dealData
	 * @param dealTime
	 * @param dealStatus
	 * @param dealtypeId
	 * @param dealDesc
	 * @param dealRebackCode
	 * @param cost
	 * @param status
	 * @param settingDesc
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=addSetting")
	@ResponseBody
	public AjaxReturnInfo addSetting(
			@RequestParam(value="path")String path,
			@RequestParam(value="systemCode")String systemCode,
			@RequestParam(value="fileName")String fileName,
			@RequestParam(value="startFlag")String startFlag,
			@RequestParam(value="splitStr")String splitStr,
			@RequestParam(value="dealId")String dealId,
			@RequestParam(value="serialNumber")String serialNumber,
			@RequestParam(value="transaCount")String transaCount,
			@RequestParam(value="terminalCode")String terminalCode,
			@RequestParam(value="dealData")String dealData,
			@RequestParam(value="dealTime")String dealTime,
			@RequestParam(value="dealStatus")String dealStatus,
			@RequestParam(value="dealtypeId")String dealtypeId,
			@RequestParam(value="dealDesc")String dealDesc,
			@RequestParam(value="dealRebackCode")String dealRebackCode,
			@RequestParam(value="bankcardNumber")String bankcardNumber,
			@RequestParam(value="cost")String cost,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "settingDesc") String settingDesc,
			@RequestParam(value = "merchantCode") String merchantCode,
			HttpServletRequest request) {
		String createrId=request.getSession().getAttribute(ConstantUtils.USERID)==null?"":(String)request.getSession().getAttribute(ConstantUtils.USERID);
		SettingManageBean settingBean = new SettingManageBean();
		settingBean.setSystemCode(systemCode);
		settingBean.setPath(path);
		settingBean.setFileName(fileName);
		settingBean.setStartFlag(startFlag);
		settingBean.setSplitStr(splitStr);
		settingBean.setDealId(dealId);
		settingBean.setSerialNumber(serialNumber);
		settingBean.setTransaCount(transaCount);
		settingBean.setTerminalCode(terminalCode);
		settingBean.setDealData(dealData);
		settingBean.setDealTime(dealTime);
		settingBean.setDealStatus(dealStatus);
		settingBean.setDealtypeId(dealtypeId);
		settingBean.setDealDesc(dealDesc);
		settingBean.setDealRebackCode(dealRebackCode);
		settingBean.setBankCardnumber(bankcardNumber);
		settingBean.setCost(cost);
		settingBean.setSettingStatus(status);
		settingBean.setMerchantCode(merchantCode);
		settingBean.setSettingDesc(settingDesc);
		settingBean.setCreaterId(createrId);
		//String settingId=CommonTable.getTableId("r");
		String settingId=String.valueOf(settingManageService.getSettingId());
		String createDate=CommonDate.getDate();
		settingBean.setSettingId(settingId);
		settingBean.setCreateDate(createDate);
		int result = settingManageService.addSetting(settingBean);
		settingBean=null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("保存失败");
		} else {
			ajaxinfo = AjaxReturnInfo.success("保存成功");
		}
		return ajaxinfo;
	}

	/**
	 * 修改文件提供系统管理
	 * @param systemCode
	 * @param path
	 * @param fileName
	 * @param startFlag
	 * @param splitStr
	 * @param dealId
	 * @param serialNumber
	 * @param transaCount
	 * @param terminalCode
	 * @param dealData
	 * @param dealTime
	 * @param dealStatus
	 * @param dealtypeId
	 * @param dealDesc
	 * @param dealRebackCode
	 * @param cost
	 * @param status
	 * @param settingDesc
	 * @param settingId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=updateSetting")
	@ResponseBody
	public AjaxReturnInfo updateSetting(
			@RequestParam(value="systemCode")String systemCode,
			@RequestParam(value="path")String path,
			@RequestParam(value="fileName")String fileName,
			@RequestParam(value="startFlag")String startFlag,
			@RequestParam(value="splitStr")String splitStr,
			@RequestParam(value="dealId")String dealId,
			@RequestParam(value="serialNumber")String serialNumber,
			@RequestParam(value="transaCount")String transaCount,
			@RequestParam(value="terminalCode")String terminalCode,
			@RequestParam(value="dealData")String dealData,
			@RequestParam(value="dealTime")String dealTime,
			@RequestParam(value="dealStatus")String dealStatus,
			@RequestParam(value="dealtypeId")String dealtypeId,
			@RequestParam(value="dealDesc")String dealDesc,
			@RequestParam(value="dealRebackCode")String dealRebackCode,
			@RequestParam(value="bankcardNumber")String bankcardNumber,
			@RequestParam(value="cost")String cost,
			@RequestParam(value = "status") String status,
			@RequestParam(value = "settingDesc") String settingDesc,
			@RequestParam(value = "settingId") String settingId,
			@RequestParam(value = "merchantCode") String merchantCode,
			HttpServletRequest request) {
		SettingManageBean settingBean = new SettingManageBean();
		settingBean.setSettingId(settingId);
		settingBean.setSystemCode(systemCode);
		settingBean.setPath(path);
		settingBean.setFileName(fileName);
		settingBean.setStartFlag(startFlag);
		settingBean.setSplitStr(splitStr);
		settingBean.setDealId(dealId);
		settingBean.setSerialNumber(serialNumber);
		settingBean.setTransaCount(transaCount);
		settingBean.setTerminalCode(terminalCode);
		settingBean.setDealData(dealData);
		settingBean.setDealTime(dealTime);
		settingBean.setDealStatus(dealStatus);
		settingBean.setDealtypeId(dealtypeId);
		settingBean.setDealDesc(dealDesc);
		settingBean.setDealRebackCode(dealRebackCode);
		settingBean.setBankCardnumber(bankcardNumber);
		settingBean.setMerchantCode(merchantCode);
		settingBean.setCost(cost);
		settingBean.setSettingStatus(status);
		settingBean.setSettingDesc(settingDesc);
		int result = settingManageService.updateSetting(settingBean);
		settingBean=null;
		AjaxReturnInfo ajaxinfo = null;
		if (result == 0) {
			ajaxinfo = AjaxReturnInfo.failed("修改失败");
		} else {
			ajaxinfo = AjaxReturnInfo.success("修改成功");
		}
		return ajaxinfo;
	}

	
	/**
	 * 删除设置
	 * @param Ids
	 * @return
	 */
	@RequestMapping(params = "method=deleteSetting")
	@ResponseBody
	public AjaxReturnInfo deleteRole(@RequestParam(value = "Ids") String Ids) {
		AjaxReturnInfo ajaxinfo = null;
	
			int result = settingManageService.deleteSetting(Ids);
			if (result == 0) {
				ajaxinfo = AjaxReturnInfo.failed("删除失败");
			} else {
				ajaxinfo = AjaxReturnInfo.success("删除成功");
			}
		
		return ajaxinfo;
	}
	/**
	 * 上传文件到服务器指定路径
	 * @param multipartRequest
	 * @param response
	 * @param req
	 */
	@RequestMapping(params = "method=upload")
	@ResponseBody
	public void upload(MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response, HttpServletRequest req) {
		response.setContentType("text/html;charset=UTF-8");
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("upload_file");
		String filePath =null;
		File rootFile=null;
		try {
		    filePath= new String(req.getParameter("path").getBytes("ISO-8859-1"),"UTF-8");
			PrintWriter print = response.getWriter();
			InputStream in = file.getInputStream();
			req.getSession().setAttribute("file", filePath);

			rootFile = new File(filePath);
			if (!rootFile.exists()) {
				rootFile.mkdirs();
			}
			File ff = new File(filePath, file.getOriginalFilename());
			OutputStream out = new FileOutputStream(ff);
			FileCopyUtils.copy(in, out);
			req.getSession().setAttribute("upload_file", ff);
			
			out.close();
			in.close();
			print.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
		    filePath =null;
		    rootFile=null;
		}
	}
}
