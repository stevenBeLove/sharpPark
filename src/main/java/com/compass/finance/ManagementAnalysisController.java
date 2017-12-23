package com.compass.finance;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.order.model.ManagementAnalysisBean;
import com.compass.order.service.OrderPayService;
import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.DateUtil;
import com.compass.utils.IpUtils;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * 经营分析
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/managementAnalysis/managementAnalysis.do")
public class ManagementAnalysisController {
	
	private static final Logger log = LoggerFactory
			.getLogger(ManagementAnalysisController.class);
	
	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;
	
	@Autowired
	@Qualifier("orderPayService")
	private OrderPayService orderPayService;
	
	@RequestMapping(params = "method=getManagementAnalysis")
	@ResponseBody
	public Map<String, Object> getManagementAnalysis(
			@RequestParam(value = "dateSet") String dateSet,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			HttpServletRequest req,HttpServletResponse response) {
		Integer count = 0;
		List<ManagementAnalysisBean> list = new ArrayList<ManagementAnalysisBean>();
		log.info("dateSet:"+dateSet+",startDate:"+startDate+",endDate:"+endDate);
		try {
			String rows = req.getParameter("rows");
			String page = req.getParameter("page");
			String bDate = " 00:00:00";
			String eDate = " 23:59:59";
			if("1".equals(dateSet)){
				//昨天
				String yesterDay = DateUtil.yesterday();
				bDate = yesterDay+bDate;
				eDate = yesterDay+eDate;
			}else if("2".equals(dateSet)){
				//上周
				bDate = DateUtil.lastWeekFirst()+bDate;
				eDate = DateUtil.lastWeekEnd()+eDate;
			}else if("3".equals(dateSet)){
				//上月
				bDate = DateUtil.lastMonthFirst()+bDate;
				eDate = DateUtil.lastMonthEnd()+eDate;
			}else if("4".equals(dateSet)){
				//自定义
				bDate = startDate+bDate;
				eDate = endDate+eDate;
			}
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			//测试用
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			count = orderPayService.getManagementAnalysisCount(outParkingId,bDate, eDate);
			int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
					: page);
			int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
					: rows);
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > count ? count : start + rownumber;
			list = orderPayService.getManagementAnalysisList(outParkingId,bDate, eDate,start,end);
		} catch (Exception e) {
			log.error("查询经营分析异常",e);
		}
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=makeManagementAnalysis")
	@ResponseBody
	public void makeManagementAnalysis(
			@RequestParam(value = "dateSet") String dateSet,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			HttpServletRequest req,HttpServletResponse response) {
		log.info("dateSet:"+dateSet+",startDate:"+startDate+",endDate:"+endDate);
		try {
			String bDate = " 00:00:00";
			String eDate = " 23:59:59";
			if("1".equals(dateSet)){
				//昨天
				String yesterDay = DateUtil.yesterday();
				bDate = yesterDay+bDate;
				eDate = yesterDay+eDate;
			}else if("2".equals(dateSet)){
				//上周
				bDate = DateUtil.lastWeekFirst()+bDate;
				eDate = DateUtil.lastWeekEnd()+eDate;
			}else if("3".equals(dateSet)){
				//上月
				bDate = DateUtil.lastMonthFirst()+bDate;
				eDate = DateUtil.lastMonthEnd()+eDate;
			}else if("4".equals(dateSet)){
				//自定义
				bDate = startDate+bDate;
				eDate = endDate+eDate;
			}
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			//测试用
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			this.addLog(req, ConstantUtils.OPERNAMEMANAGEMENTANALYSIS, ConstantUtils.OPERTYPEEXPO, "参数：outParkingId："+outParkingId+",bDate:"+bDate+",eDate:"+eDate);
			ParkBean parkBean = new ParkBean();
			if(StringUtils.isNotBlank(outParkingId)){
				parkBean = parkService.getParkByOutParkingId(outParkingId);
			}
			List<ManagementAnalysisBean> list = orderPayService.getManagementAnalysisList(outParkingId,bDate, eDate,0,1000);
			exportDataToExcel(response,bDate,eDate,parkBean,list,"经营分析表", ".xls");
		} catch (Exception e) {
			log.error("数据导出异常",e);
		}
	}
	
	public static void exportDataToExcel(HttpServletResponse response,String startDate,String endDate,ParkBean parkBean,List<ManagementAnalysisBean> list,String fileName,String fileSuffix){
    	OutputStream os = null;
        try {
        	HSSFWorkbook workbook = new HSSFWorkbook();
            //生成一个表格
            HSSFSheet sheet = workbook.createSheet(fileName);
            //设置表格默认列宽15个字节
            sheet.setDefaultColumnWidth(15);
            //生成表格标题
            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("起始日期:");
            row.createCell(1).setCellValue("["+startDate+"]");
            row.createCell(2).setCellValue("终止日期:");
            row.createCell(3).setCellValue("["+endDate+"]");
            row = sheet.createRow(1);
            row.createCell(0).setCellValue("报表编号");
            row.createCell(1).setCellValue(System.currentTimeMillis()+"");
            row = sheet.createRow(2);
            row.createCell(0).setCellValue("停车场基本信息");
            row = sheet.createRow(3);
            row.createCell(0).setCellValue("停车场id");
            row.createCell(1).setCellValue("停车场名称");
            row = sheet.createRow(4);
            row.createCell(0).setCellValue(parkBean.getOutParkingId());
            row.createCell(1).setCellValue(parkBean.getParkingName());
            row = sheet.createRow(6);
            row.createCell(0).setCellValue("日期");
            row.createCell(1).setCellValue("预计临停总营收");
            row.createCell(2).setCellValue("预计临停现金收入");
            row.createCell(3).setCellValue("临停支付宝收入");
            row.createCell(4).setCellValue("临停微信收入");
            row.createCell(5).setCellValue("入库车次");
            row.createCell(6).setCellValue("出库车次");
            row.createCell(7).setCellValue("预计场内车辆");
            
            if(list!=null&&!list.isEmpty()&&list.size()>0){
            	int i = 6;
            	for (ManagementAnalysisBean managementAnalysisBean : list) {
            		i++;
            		row = sheet.createRow(i);
                    row.createCell(0).setCellValue(managementAnalysisBean.getDateStr());
                    row.createCell(1).setCellValue(managementAnalysisBean.getTempTotalAmount());
                    row.createCell(2).setCellValue(managementAnalysisBean.getTempAmount());
                    row.createCell(3).setCellValue(managementAnalysisBean.getTempAlipayAmount());
                    row.createCell(4).setCellValue(managementAnalysisBean.getTempWeiXinAmount());
                    row.createCell(5).setCellValue(managementAnalysisBean.getInTimeCount());
                    row.createCell(6).setCellValue(managementAnalysisBean.getOutTimeCount());
                    if(managementAnalysisBean.getExpectedVehicleCount()!=null){
                        row.createCell(7).setCellValue(managementAnalysisBean.getExpectedVehicleCount());
                    }
				}
            }
            
        	 fileName = fileName+System.currentTimeMillis();
        	 response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"),"ISO8859-1")+fileSuffix);
             response.setCharacterEncoding("UTF-8");
             response.setContentType("application/vnd.ms-excel");
             os = response.getOutputStream();
             workbook.write(os);
        } catch (Exception e) {
            log.error("写入excel异常",e);
        }finally{
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                log.error("关闭流异常",e);
            }
        }
    }
	
	@Autowired
    @Qualifier("systemLogService")
    private SystemLogService     systemLogService;
	
	public void addLog(HttpServletRequest req,String operName,String operType,String operateDetail){
		try {
			String ipAddress = IpUtils.getRemoteHost(req);
			String userId = req.getSession().getAttribute(ConstantUtils.USERID).toString();
			String agencyIdS = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
			systemLogService.addLog(ipAddress, agencyIdS, userId, operName, operType, operateDetail);
		} catch (Exception e) {
			log.error("insert--log---error",e);
		}
	}
}
