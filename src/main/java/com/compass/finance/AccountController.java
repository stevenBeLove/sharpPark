package com.compass.finance;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.utils.DateUtil;

@Controller
@RequestMapping("/finance/account.do")
public class AccountController {
	
	private static final Logger log = LoggerFactory
			.getLogger(AccountController.class);
	
	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;
	
	@RequestMapping(params = "method=makeAccount")
	@ResponseBody
	public void makeAccount(
			@RequestParam(value = "dateSet") String dateSet,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "payType") String payType,
			HttpServletRequest req,HttpServletResponse response) {
		log.info("dateSet:"+dateSet+",startDate:"+startDate+",endDate:"+endDate+",payType:"+payType);
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
			String outParkingId = "10180";
			ParkBean parkBean = parkService.getParkByOutParkingId(outParkingId);
			exportDataToExcel(response,bDate,eDate,parkBean,"对账报表", ".xls");
		} catch (Exception e) {
			log.error("数据导出异常",e);
		}
	}
	
	
	public static void exportDataToExcel(HttpServletResponse response,String startDate,String endDate,ParkBean parkBean,String fileName,String fileSuffix){
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
            row.createCell(0).setCellValue("对账单编号");
            row.createCell(1).setCellValue("SQB1680000756384-20170701-20170731");
            row = sheet.createRow(2);
            row.createCell(0).setCellValue("停车场id");
            row.createCell(1).setCellValue("停车场名称");
            row = sheet.createRow(3);
            row.createCell(0).setCellValue(parkBean.getOutParkingId());
            row.createCell(1).setCellValue(parkBean.getParkingName());
            row = sheet.createRow(4);
            row.createCell(0).setCellValue("收费汇总清单");
            row = sheet.createRow(5);
            row.createCell(0).setCellValue("收费类型");
            row.createCell(1).setCellValue("交易笔数");
            row.createCell(2).setCellValue("交易金额");
            row.createCell(3).setCellValue("退款笔数");
            row.createCell(4).setCellValue("退款金额");
            row.createCell(5).setCellValue("商户优惠");
            row.createCell(6).setCellValue("云停风行优惠");
            row.createCell(7).setCellValue("实收金额");
            row.createCell(8).setCellValue("手续费");
            row.createCell(9).setCellValue("结算金额");
            
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
}
