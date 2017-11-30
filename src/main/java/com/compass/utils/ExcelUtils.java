package com.compass.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.park.model.ParkBean;

public class ExcelUtils {
	
	private static final Logger  log = LoggerFactory.getLogger(ExcelUtils.class);
    
    public static Workbook createWorkbook(InputStream is,String excelFileName) throws IOException{
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        }else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    public static Sheet getSheet(Workbook workbook,int sheetIndex){
        return workbook.getSheetAt(0);        
    }
    
    public static Map<String,Object> importDataFromExcel(Object vo,InputStream is,String fileName,String title,List<Integer> nCell) throws Exception{
    	String retCode = "99";
    	String retMsg = "系统异常";
    	Map<String,Object> retMap = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();
        //创建工作簿
        Workbook workbook = createWorkbook(is, fileName);
        //创建工作表sheet
        Sheet sheet = getSheet(workbook, 0);
        //获取sheet中数据的行数
        int rows = sheet.getPhysicalNumberOfRows();
        //获取表头单元格个数
        int cells = sheet.getRow(0).getPhysicalNumberOfCells();
        StringBuffer sb = new StringBuffer();
        Row firstRow = sheet.getRow(0);
        for (int i = 0; i < cells; i++) {
        	Cell cell = firstRow.getCell(i);
        	 if (null == cell) {
                 cell = firstRow.createCell(i);
             }
             cell.setCellType(Cell.CELL_TYPE_STRING);
        	 sb.append(cell.getStringCellValue());
		}
        log.info("firstRow"+sb.toString());
        //利用反射，给JavaBean的属性进行赋值
        Field[] fields = vo.getClass().getDeclaredFields();
        if(title.equals(sb.toString())){
            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                int index = 0;
                while (index < cells) {
                    Cell cell = row.getCell(index);
                    if (null == cell) {
                        cell = row.createCell(index);
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    Object value = getCellData(cell);
                    if(nCell.contains(index)&&(value==null||"".equals(value))){
                    	retMap.put("retCode", "99");
                        retMap.put("retMsg", "excel中必填项有空值");
                        return retMap;
                    }
                    Field field = fields[index];
                    String fieldName = field.getName();
                    String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    Method setMethod = vo.getClass().getMethod(methodName, new Class[]{String.class});
                    setMethod.invoke(vo, new Object[]{value});
                    index++;
                }
                list.add(vo);
                vo = vo.getClass().getConstructor(new Class[]{}).newInstance(new Object[]{});//重新创建一个vo对象
            }
            retMap.put("list", list);
            retCode = "00";
            retMsg = "success";
        }else{
        	retMsg = "文件头不正确";
        }
        retMap.put("retCode", retCode);
        retMap.put("retMsg", retMsg);
        return retMap;
        
    }
    
    private static Object getCellData(Cell cell) {
        if(cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
            	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                return format.format(cell.getDateCellValue());
            } else {
            	cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
            }
        default:
            return null;
        }
    } 
    
    public static  boolean isHasValues(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        boolean flag = false;
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
            Method getMethod;
            try {
                getMethod = object.getClass().getMethod(methodName);
                Object obj = getMethod.invoke(object);
                if (null != obj && "".equals(obj)) {
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                log.error("",e);
            }
            
        }
        return flag;
        
    }
    
    public static <T> void exportDataToExcel(HttpServletResponse response,List<T> list,String[] headers,String fileName,String fileSuffix,ParkBean parkBean){
    	OutputStream os = null;
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(fileName);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        //生成一个样式
        HSSFCellStyle style = getCellStyle(workbook);
        //生成一个字体
        HSSFFont font = getFont(workbook);
        //把字体应用到当前样式
        style.setFont(font);
        
        //生成表格标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short)300);
        HSSFCell cell = null;
        
        for (int i = 0; i < headers.length; i++) {        
            cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //将数据放入sheet中
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i+1);
            T t = list.get(i);
            //利用反射，根据JavaBean属性的先后顺序，动态调用get方法得到属性的值
            Field[] fields = t.getClass().getDeclaredFields();
            try {
                for (int j = 0; j < fields.length; j++) {
                    cell = row.createCell(j);
                    Field field = fields[j];
                    String fieldName = field.getName();
                    String methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                    Method getMethod = t.getClass().getMethod(methodName,new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    if(null == value)
                        value ="";
                    cell.setCellValue(value.toString());
                }
            } catch (Exception e) {
                log.error("文件导出异常",e);
            }
        }
        try {
        	if(parkBean!=null){
        		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        		Date date = new Date();
        		int add_row = list.size()+10;
        		row = sheet.createRow(add_row+1);
        		row.createCell(0).setCellValue("导出日期：");
        		row.createCell(1).setCellValue(dateFormat.format(date));
        		row.createCell(2).setCellValue("导出时间：");
        		row.createCell(3).setCellValue(timeFormat.format(date));
        		row = sheet.createRow(add_row+2);
        		row.createCell(0).setCellValue("停车场基本信息：");
        		row = sheet.createRow(add_row+3);
        		row.createCell(0).setCellValue("停车场Id:");
        		row.createCell(1).setCellValue("停车场名称");
        		row = sheet.createRow(add_row+4);
        		row.createCell(0).setCellValue(parkBean.getOutParkingId());
        		row.createCell(1).setCellValue(parkBean.getParkingName());
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
    
    public void exportDataToExcel(List<Map<String, Object>> list,LinkedHashMap<String,Object> headers,String title,OutputStream os){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        //生成一个样式
        HSSFCellStyle style = getCellStyle(workbook);
        //生成一个字体
        HSSFFont font = getFont(workbook);
        //把字体应用到当前样式
        style.setFont(font);
        //生成表格标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short)300);
        HSSFCell cell = null;
        
        Set<String> keys = headers.keySet();
        int x = 0;
        for (String key : keys) {
        	x++;
        	cell = row.createCell(x);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString((String)headers.get(key));
            cell.setCellValue(text);
		}
        x=1;
        for (Map<String, Object> map : list) {
        	x++;
        	row = sheet.createRow(x);
        	int j = 0;
        	for (String key : keys) {
        		j++;
        		cell = row.createCell(j);
        		Object value = map.get(key);
        		if(null == value)
                    value ="";
                cell.setCellValue(value.toString());
			}
		}
        try {
            workbook.write(os);
        } catch (Exception e) {
        	log.error("",e);
        }finally{
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
            	log.error("",e);
            }
        }
        
    }
    
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.AQUA.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return style;
    }

    public static HSSFFont getFont(HSSFWorkbook workbook){
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }
    
    public boolean isIE(HttpServletRequest request){
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie")>0?true:false;    
    }
}