package com.compass.pasmFee.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.constans.FieldConstans;
import com.compass.pasmFee.model.QueryTradeProfit;
import com.compass.pasmFee.model.TradeProfit;
import com.compass.pasmFee.service.QueryTradeProfitService;
import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;
import com.compass.utils.FormatChar;
import com.compass.utils.NumberFormat;
import com.compass.utils.mvc.AjaxReturnInfo;

@Controller
@RequestMapping("/fee/queryTradeProfit.do")
public class queryTradeProfitController {

    /**  
     * 系统日志service
     */
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService   systemLogService;
    
    /**
     *  查询
     */
    @Autowired
    @Qualifier("queryTradeProfitService")
    private QueryTradeProfitService   queryTradeProfitService;
    
    private static Logger    logger = LoggerFactory.getLogger(queryTradeProfitController.class);
    
    /**
     * 【方法名】    : (交易及分润明细查询). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月14日 下午4:42:45 .<br/>
     * 【参数】： .<br/>
     * @param yearmonthdatestart 开始日期
     * @param yearmonthdateend 结束日期
     * @param pageAgencyId 代理商 
     * @param queryFlag 查询类型 
     * @param businessName 交易类型
     * @param logNo 流水号
     * @param psamId 终端号
     * @param req HttpServletRequest
     * @return Map<String, Object>.<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=queryTradeProfit")
    @ResponseBody
    public Map<String, Object> queryTradeProfit(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart, @RequestParam(value = "yearmonthdateend") String yearmonthdateend,
            @RequestParam(value = "agency_Id") String pageAgencyId, @RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "business_name") String businessName,
            @RequestParam(value = "logNo") String logNo, @RequestParam(value = "psamId") String psamId, @RequestParam(value = "mobileNo") String mobileNo, HttpServletRequest req) {
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("yearmonthdatestart", yearmonthdatestart);
        map.put("yearmonthdateend", yearmonthdateend);
        map.put("business_name", businessName);
        map.put("logNo", logNo);
        map.put("mobileNo", mobileNo);
        if (!StringUtils.isEmpty(psamId)) {
            if (psamId.length() > 15) {
                psamId = psamId.substring(0, 15);
            }
        }
        map.put("psamId", psamId);
        map.put("orderByClause", " LOCALDATE desc, LOCALTIME desc ");
        // 是顶级机构登录
        if (FieldConstans.QUERYFLAG1.equals(queryFlag)) {
            if (StringUtils.isEmpty(pageAgencyId)){
                map.put(FieldConstans.SELFAGENCYID, agencyId);
            } else {
                map.put(FieldConstans.SELFAGENCYID, "");
            }
        } else {
          //顶级机构登录
            if (ConstantUtils.CENTERCODE.equals(agencyId)){
                if (StringUtils.isEmpty(pageAgencyId)) {
                    map.put(FieldConstans.ADMINAGENCYID, agencyId);
                } else {
                    map.put(FieldConstans.ADMINAGENCYID, agencyId);
                    map.put(FieldConstans.ADMIN_PAGE_AGENCYID, pageAgencyId);
                }
            } else {//非顶级机构登录
                if (StringUtils.isEmpty(pageAgencyId)) {
                    map.put(FieldConstans.SUPERAGENCYID, agencyId);
                } else {
                    map.put(FieldConstans.SUPERAGENCYID, agencyId);
                    map.put(FieldConstans.PAGEAGENCYID, pageAgencyId);
                }
            }
        }
        Integer count = 0;
        String feeAmountSum = "0";
        String nextFeeAmountSum = "0";
        String differenceFeeAmountSum = "0";
        String dealAmountSum = "0";
        String poundageAmountSum = "0";
        List<TradeProfit> list = null;
        try {
            feeAmountSum = queryTradeProfitService.queryFee_AmountSumNow(map);
            nextFeeAmountSum = queryTradeProfitService.queryNext_feeAmountSumNow(map);
            differenceFeeAmountSum = queryTradeProfitService.queryDifference_feeAmountSumNow(map);
            dealAmountSum = queryTradeProfitService.selectDealAmountSum(map);
            poundageAmountSum = queryTradeProfitService.selectPoundageAmountSum(map);
            list = queryTradeProfitService.selectPageTradeProfit(map);
            count = queryTradeProfitService.selectPageCountTradeProfit(map);
            if (list != null) {
                for (TradeProfit tradeProfit : list) {
                    if (!org.springframework.util.StringUtils.isEmpty(tradeProfit.getMobileNo())) {
                        tradeProfit.setMobileNo(FormatChar.mosaic(tradeProfit.getMobileNo()));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return AjaxReturnInfo.setTableInt(count, list, NumberFormat.parse2NumberFormat(feeAmountSum, NumberFormat.DEVIDE100), NumberFormat.parse2NumberFormat(nextFeeAmountSum, NumberFormat.DEVIDE100),
                NumberFormat.parse2NumberFormat(differenceFeeAmountSum, NumberFormat.DEVIDE100), NumberFormat.parse2NumberFormat(dealAmountSum, NumberFormat.DEVIDE100),
                NumberFormat.parse2NumberFormat(poundageAmountSum, NumberFormat.DEVIDE100));
    }
    
    /*
     * 交易类型查询
     */
    
    @RequestMapping(params = "method=getBusiness")
    @ResponseBody
    public List<Map<String, Object>> getBusiness(HttpServletRequest request) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String businessName = "瑞通宝";
        List<QueryTradeProfit> business = queryTradeProfitService.getBusiness(businessName);
        map.put("id", "");
        map.put("text", "请选择交易类型");
        map.put("selected", true);
        list.add(map);
        map = null;
        for (int i = 0; i < business.size(); i++) {
            QueryTradeProfit bean = business.get(i);
          Map<String, Object> map1 = new HashMap<String, Object>();
          map1.put("id", bean.getBusiness_type());
          map1.put("text", bean.getBusiness_Name());
          list.add(map1);
        }
      return list;
    }
    
    /**
     * 【方法名】    : (导出交易及分润明细查询). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月14日 下午4:42:45 .<br/>
     * 【参数】： .<br/>
     * @param yearmonthdatestart 开始日期
     * @param yearmonthdateend 结束日期
     * @param pageAgencyId 代理商 
     * @param queryFlag 查询类型 
     * @param businessName 交易类型
     * @param logNo 流水号
     * @param psamId 终端号
     * @param mobileNo 手机号
     * @param req HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException 异常
     * @throws RowsExceededException 异常
     * @throws WriteException 异常
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    @RequestMapping(params = "method=exportDetail")
    public void exportDetail(@RequestParam(value = "yearmonthdatestart") String yearmonthdatestart, @RequestParam(value = "yearmonthdateend") String yearmonthdateend,
            @RequestParam(value = "agency_Id") String pageAgencyId, @RequestParam(value = "queryFlag") String queryFlag, @RequestParam(value = "business_name") String businessName,
            @RequestParam(value = "logNo") String logNo, @RequestParam(value = "psamId") String psamId, @RequestParam(value = "mobileNo") String mobileNo, HttpServletRequest req, HttpServletResponse response) throws IOException,
            RowsExceededException, WriteException {
        String agencyId = req.getSession().getAttribute(ConstantUtils.AGENCYID).toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("yearmonthdatestart", yearmonthdatestart);
        map.put("yearmonthdateend", yearmonthdateend);
        map.put("business_name", businessName);
        map.put("logNo", logNo);
        map.put("mobileNo", mobileNo);
        if (!StringUtils.isEmpty(psamId)) {
            if (psamId.length() > 15) {
                psamId = psamId.substring(0, 15);
            }
        }
        map.put("psamId", psamId);
        map.put("orderByClause", " LOCALDATE desc, LOCALTIME desc ");
        // 是顶级机构登录
        if (FieldConstans.QUERYFLAG1.equals(queryFlag)) {
            if (StringUtils.isEmpty(pageAgencyId)){
                map.put(FieldConstans.SELFAGENCYID, agencyId);
            } else {
                map.put(FieldConstans.SELFAGENCYID, "");
            }
        } else {
          //顶级机构登录
            if (ConstantUtils.CENTERCODE.equals(agencyId)){
                if (StringUtils.isEmpty(pageAgencyId)) {
                    map.put(FieldConstans.ADMINAGENCYID, agencyId);
                } else {
                    map.put(FieldConstans.ADMINAGENCYID, agencyId);
                    map.put(FieldConstans.ADMIN_PAGE_AGENCYID, pageAgencyId);
                }
            } else {//非顶级机构登录
                if (StringUtils.isEmpty(pageAgencyId)) {
                    map.put(FieldConstans.SUPERAGENCYID, agencyId);
                } else {
                    map.put(FieldConstans.SUPERAGENCYID, agencyId);
                    map.put(FieldConstans.PAGEAGENCYID, pageAgencyId);
                }
            }
        }
        List<TradeProfit> list = null;
        try {
            list = queryTradeProfitService.exportPageTradeProfit(map);
            for (TradeProfit tradeProfit : list) {
                if (!org.springframework.util.StringUtils.isEmpty(tradeProfit.getMobileNo())) {
                    tradeProfit.setMobileNo(FormatChar.mosaic(tradeProfit.getMobileNo()));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
        String urls = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel";
        File f = new File(urls);
        if (!f.exists()) {
            f.mkdirs();
        }
        String url = webRoot.substring(0, webRoot.indexOf("WEB-INF")) + "excel/" + ConstantUtils.EXPORTTRADEPROFIT;
        File wj = new File(url);
        WritableWorkbook wwb = Workbook.createWorkbook(wj);
        int sheetNum = 0;
        WritableSheet ws = wwb.createSheet("tradeprofit" + sheetNum, sheetNum);
        
        for (int i = 0; i < ConstantUtils.TRADEPROFIT.length; i++) {
            ws.addCell(new Label(i, 0, ConstantUtils.TRADEPROFIT[i]));
        }
        for (int i = 0; i < list.size(); i++) {
            AddCell(list, ws, i);
            if (i / 60000 == sheetNum + 1) {
                sheetNum++;
                ws = null;
                ws = wwb.createSheet("tradeprofit" + sheetNum, sheetNum);
            }
        }
        wwb.write();
        wwb.close();
        try {
            download(req, response, url, ConstantUtils.EXPORTTRADEPROFIT);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 【方法名】    : (下载). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月14日 下午10:06:50 .<br/>
     * 【参数】： .<br/>
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param path 路径
     * @param fileName 文件名
     * @throws Exception .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public void download(HttpServletRequest request, HttpServletResponse response, String path, String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        fileName = new String(fileName.getBytes(), "iso_8859_1");
        try {
            File f = new File(path);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }
    /**
     * 把agencyBean中的数据放入excel的行
     * @param list List<TradeProfit>
     * @param ws WritableSheet
     * @param i 列
     * @throws WriteException 异常信息
     * @throws RowsExceededException 异常信息
     */
    private void AddCell(List<TradeProfit> list, WritableSheet ws, int i) throws WriteException, RowsExceededException {
        int clum = i + 1;
        TradeProfit queryTradeProfit = list.get(i);
        ws.addCell(new Label(0, clum, queryTradeProfit.getAgencyName()));
        ws.addCell(new Label(1, clum, queryTradeProfit.getMobileNo()));
        ws.addCell(new Label(2, clum, queryTradeProfit.getLocallogno()));
        ws.addCell(new Label(3, clum, queryTradeProfit.getPsamid()));
        ws.addCell(new Label(4, clum, queryTradeProfit.getBusinessName()));
        ws.addCell(new Label(5, clum, queryTradeProfit.getLocaldate()));
        ws.addCell(new Label(6, clum, queryTradeProfit.getLocaltime()));
        ws.addCell(new Label(7, clum, queryTradeProfit.getAmount()));
        ws.addCell(new Label(8, clum, queryTradeProfit.getRtbfee()));
        ws.addCell(new Label(9, clum, queryTradeProfit.getFeerate()));
        ws.addCell(new Label(10, clum, queryTradeProfit.getFixFee()));
        ws.addCell(new Label(11, clum, queryTradeProfit.getCostRate()));
        ws.addCell(new Label(12, clum, queryTradeProfit.getCostFix()));
        ws.addCell(new Label(13, clum, queryTradeProfit.getChildCostRate()));
        ws.addCell(new Label(14, clum, queryTradeProfit.getChildCostFix()));
        ws.addCell(new Label(15, clum, queryTradeProfit.getChildAmount()));
        ws.addCell(new Label(16, clum, queryTradeProfit.getSumAmount()));
        ws.addCell(new Label(17, clum, queryTradeProfit.getFrAmount()));
    }
    
}
