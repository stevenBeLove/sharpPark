package com.compass.finance;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.compass.order.model.OrderPayBean;
import com.compass.order.service.OrderPayService;
import com.compass.park.model.ParkBean;
import com.compass.park.service.ParkService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.DateUtil;
import com.compass.utils.HttpClientUtil;
import com.compass.utils.IpUtils;
import com.compass.utils.Md5Util;
import com.compass.utils.PropertyPlaceholderConfigurerExt;
import com.compass.utils.mvc.AjaxReturnInfo;

/**
 * 交易流水
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/finance/transactionFlow.do")
public class TransactionFlowController {
	
	private static final Logger log = LoggerFactory
			.getLogger(TransactionFlowController.class);
	
	private static final String REFUND_URL = (String) PropertyPlaceholderConfigurerExt.getProperties().get("refund.url");
	
	private final String SIGN_KEY = (String) PropertyPlaceholderConfigurerExt.getProperties().get("parkPrice.sign");
	
	@Autowired
	@Qualifier("orderPayService")
	private OrderPayService orderPayService;
	
	@Autowired
	@Qualifier("parkService")
	private ParkService parkService;
	
	@RequestMapping(params = "method=getOrderPay")
	@ResponseBody
	public Map<String, Object> getOrderPay(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "payType") String payType,
			HttpServletRequest req) {
		Integer count = 0;
		List<OrderPayBean> list = new ArrayList<OrderPayBean>();
		try {
			log.info("carNumber:"+carNumber);
			String bDate = " 00:00:00";
			String eDate = " 23:59:59";
			String format = "yyyy-MM-dd HH:mm:ss";
			String today = DateUtil.toDay();
			OrderPayBean orderPayBean = new OrderPayBean();
			if(StringUtils.isNotBlank(carNumber)){
				orderPayBean.setCarNumber(carNumber);
			}
			if(StringUtils.isNotBlank(payType)){
				orderPayBean.setPayType(payType);
			}
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			startDate = StringUtils.isNotBlank(startDate)?startDate+bDate:today+bDate;
			endDate = StringUtils.isNotBlank(endDate)?endDate+eDate:today+eDate;
			String rows = req.getParameter("rows");
			String page = req.getParameter("page");
			orderPayBean.setOutParkingId(outParkingId);
			count = orderPayService.getOrderPayCount(orderPayBean);
			int pagenumber = Integer.parseInt((page == null || page == "0") ? "1"
					: page);
			int rownumber = Integer.parseInt((rows == "0" || rows == null) ? "20"
					: rows);
			int start = (pagenumber - 1) * rownumber;
			int end = (start + rownumber) > count ? count : start + rownumber;
			orderPayBean.setStart(start);
			orderPayBean.setEnd(end);
			orderPayBean.setStartDate(DateUtil.fromatDate(startDate, format));
			orderPayBean.setEndDate(DateUtil.fromatDate(endDate, format));
			count = orderPayService.getOrderPayCount(orderPayBean);
			list = orderPayService.getOrderPayAll(orderPayBean);
		} catch (Exception e) {
			log.error("getOrderPay---error",e);
		}
		return AjaxReturnInfo.setTable(count, list);
	}
	
	@RequestMapping(params = "method=transactionRefundMoney")
	@ResponseBody
	public AjaxReturnInfo transactionRefundMoney(
			@RequestParam(value = "refundMoney") String refundMoney,
			@RequestParam(value = "orderNo") String orderNo,
			HttpServletRequest req){
		try {
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			OrderPayBean orderPayBean = this.orderPayService.getOrderPayBeanByOrderNo(orderNo, outParkingId);
			if(orderPayBean!=null){
				Map postParam = new HashMap();
				postParam.put("tradeNO", orderNo);
				log.info("refundMoney:"+refundMoney);
				String signValue = Md5Util.sortMapByKey(postParam);
				String sign = Md5Util.getMd5(signValue+SIGN_KEY);
				postParam.put("sign", sign);
				log.info("refund-param:"+postParam);
				String ret = HttpClientUtil.sendPostUrl(REFUND_URL, postParam, Charset.forName("utf-8"), null);
				log.info(orderNo+"调用退款返回："+ret);
				if(StringUtils.isNotBlank(ret)){
					JSONObject jsonObject = JSONObject.parseObject(ret);
					String success =  jsonObject.getString("success");
					if("true".equals(success)){
						return AjaxReturnInfo.success("退款成功");
					}else{
						return AjaxReturnInfo.failed(jsonObject.getString("message"));
					}
				}
			}else{
				return AjaxReturnInfo.failed("订单不存在");
			}
		} catch (Exception e) {
			log.error("transactionRefundMoney---error",e);
		}
		return AjaxReturnInfo.failed("系统异常");
	}
	
	@RequestMapping(params = "method=makeTransactionFlow")
	@ResponseBody
	public void makeTransactionFlow(
			@RequestParam(value = "carNumber") String carNumber,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "payType") String payType,
			HttpServletRequest req,HttpServletResponse response) {
		try {
			String format = "yyyy-MM-dd HH:mm:ss";
			String bDate = " 00:00:00";
			String eDate = " 23:59:59";
			String changeParkId = (String) req.getSession().getAttribute("changeParkId");
			String outParkingId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString().trim();
			outParkingId = ConstantUtils.CENTERCODE.equals(outParkingId)?changeParkId:outParkingId;
			if(StringUtils.isNotBlank(startDate)){
				startDate = startDate+bDate;
			}
			if(StringUtils.isNotBlank(endDate)){
				endDate = endDate+eDate;
			}
			OrderPayBean orderPayBean = new OrderPayBean();
			orderPayBean.setPayType(payType);
			orderPayBean.setCarNumber(carNumber);
			orderPayBean.setStartDate(DateUtil.fromatDate(startDate, format));
			orderPayBean.setEndDate(DateUtil.fromatDate(endDate, format));
			orderPayBean.setOutParkingId(outParkingId);
			this.addLog(req, ConstantUtils.OPERNAMETRANSACTIONFLOW, ConstantUtils.OPERTYPEEXPO, "参数：outParkingId："+outParkingId+",bDate:"+bDate+",eDate:"+eDate+",payType:"+payType+",carNumber:"+carNumber);
			List<OrderPayBean> list = orderPayService.getOrderPayListByType(orderPayBean);
			ParkBean parkBean = new ParkBean();
			if(StringUtils.isNotBlank(outParkingId)){
				parkBean = parkService.getParkByOutParkingId(outParkingId);
			}
			exportDataToExcel(response,startDate,endDate,parkBean,list,"流水账目表", ".xls");
		} catch (Exception e) {
			log.error("数据导出异常",e);
		}
	}
	
	public static void exportDataToExcel(HttpServletResponse response,String startDate,String endDate,ParkBean parkBean,List<OrderPayBean> list,String fileName,String fileSuffix){
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
            row.createCell(0).setCellValue("车牌号码");
            row.createCell(1).setCellValue("车辆类型");
            row.createCell(2).setCellValue("收费金额");
            row.createCell(3).setCellValue("进场时间");
            row.createCell(4).setCellValue("出场时间");
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            if(list!=null&&!list.isEmpty()&&list.size()>0){
            	int i = 6;
            	for (OrderPayBean orderPayBean : list) {
            		i++;
            		row = sheet.createRow(i);
                    row.createCell(0).setCellValue(orderPayBean.getCarNumber());
                    String carType = orderPayBean.getCarType();
                    if("1".equals(carType)){
                    	carType = "小型车";
                    }else if("2".equals(carType)){
                    	carType = "中型车";
                    }else if("3".equals(carType)){
                    	carType = "大型车";
                    }else if("4".equals(carType)){
                    	carType = "摩托车";
                    }else{
                    	carType = "其他";
                    }
                    row.createCell(1).setCellValue(carType);
                    HSSFCell cell = row.createCell(2);
                    if(orderPayBean.getPayMoney()!=null){
                    	cell.setCellValue(new BigDecimal(orderPayBean.getPayMoney()).doubleValue());
                    }
                    cell.setCellStyle(cellStyle);
                    row.createCell(3).setCellValue(orderPayBean.getInTime());
                    row.createCell(4).setCellValue(orderPayBean.getOutTime());
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
