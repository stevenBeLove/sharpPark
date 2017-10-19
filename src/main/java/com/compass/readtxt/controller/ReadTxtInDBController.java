package com.compass.readtxt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.compass.dealmanager.model.DealBean;
import com.compass.dealmanager.service.DealManageService;
import com.compass.setting.model.SettingManageBean;
import com.compass.setting.service.SettingManageService;
import com.compass.system.model.SystemManageBean;
import com.compass.system.service.SystemManageService;
import com.compass.terminalmanage.service.TerminalManageService;
import com.compass.utils.CommonDate;
import com.compass.utils.ConstantUtils;
import com.compass.utils.TxtEncodingUtils;

/**
 * @author wangLong
 * 
 */
public class ReadTxtInDBController {
	
	@Autowired
	@Qualifier("systemManageService")
	private SystemManageService systemManageService;
	
	@Autowired
	@Qualifier("settingManageService")
	private SettingManageService settingManageService;
	
	@Autowired
	@Qualifier("dealManageService")
	private DealManageService dealManageService;

	@Autowired
	@Qualifier("terminalManageService")
	public TerminalManageService terminalManageService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	public void saveTxtInDB() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		Date date = calendar.getTime();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateStr = format.format(date);
		String createDt = CommonDate.getDate();
		try {
			List<SystemManageBean> sysList =systemManageService.getSystems(null, ConstantUtils.SYSTEN_IV); // 获取所有的系统
			int fail = 0;
			int flag = 0;
			int k = 0;
			
			for (SystemManageBean sysBean : sysList) {
				String systemCode = sysBean.getSystemCode();
				List<SettingManageBean> setList =settingManageService.getSettings(sysBean.getSystemId(), ConstantUtils.SYSTEN_IV);// 根据系统编号获得所有导入文件的设置
				for (SettingManageBean setBean : setList) {
					String path = setBean.getPath().replace('\\', '/');
					String fileName = setBean.getFileName() + dateStr + ".txt"; // 文件名称拼接
					File file = new File(path + "/" + fileName); // 获取文件路径
					File cdir=new File(path+"/bak");
					if(!cdir.exists()){
						cdir.mkdir();
					}
					File cfile=new	File(path+"/bak/"+fileName);
					String startFlag = setBean.getStartFlag();
					String splitStr = setBean.getSplitStr();
					int tempFlag = 0;
					if (file.isFile() && file.exists()) { // 判断文件是否存在
						String encodinng = TxtEncodingUtils.codeString(path + "/" + fileName);
						InputStreamReader read = new InputStreamReader(
								new FileInputStream(file), encodinng);// 考虑到编码格式
						BufferedReader bufferedReader = new BufferedReader(read);
						String lineTxt = null;
						while ((lineTxt = bufferedReader.readLine()) != null) {
							String temp[] = lineTxt.split(splitStr);
							DealBean dealBean = null;
							
							if ("0".equals(startFlag)) {// 判断是否从第一行开始读,0：否 ，1：是
								tempFlag++;
								if(tempFlag!=1){
									dealBean = setBean(setBean, temp,systemCode,createDt);
								}else{
									continue;
								}	
							} else {
								dealBean =setBean(setBean, temp,systemCode,createDt);
							}
							
							try {
								k = dealManageService.addDeal(dealBean);
								if (k > 0) {
									flag++;

								} else {
									fail++;
								}

							} catch (Exception e) {
								// TODO: handle exception
								fail++;
							}finally{
								//bufferedReader.close();
							}
						}
						bufferedReader.close();
							terminalManageService.teminalActivating(createDt);
						
						copyFile(file,cfile);
						file.delete();
						log.info("成功导入：" + flag + "条; 失败：	" + fail
								+ "条");
					} else {
						log.info("找不到指定的文件");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		    format=null;
		}
		System.out.println("监听器执行");
	}
	
	
	/**
	 * 设置交易实体类中的数据
	 * @param setBean
	 * @param temp
	 * @param dealBean
	 */
	private DealBean setBean(SettingManageBean setBean, String[] temp,
			String systemCode ,String createDate) {
		DealBean dealBean = new DealBean();
		String transaCount = temp[Integer.valueOf(setBean.getTransaCount().trim())]; //交易金额
		String charge = temp[Integer.valueOf(setBean.getCost())];//交易成本
		String onlyCode = systemCode+ temp[Integer.valueOf(setBean.getDealId())]+temp[Integer.valueOf(setBean.getSerialNumber())]+temp[Integer.valueOf(setBean.getDealData())];
		dealBean.setSysSource(systemCode);
		dealBean.setOnlyCode(onlyCode);
		dealBean.setTerminal_OnlyCode(systemCode+temp[Integer.valueOf(setBean.getMerchantCode())]+temp[Integer.valueOf(setBean.getTerminalCode())]);
		dealBean.setDealId(temp[Integer.valueOf(setBean.getDealId())]);
		dealBean.setSerialNumber(temp[Integer.valueOf(setBean.getSerialNumber())]);
		
		if("".equals(transaCount) || transaCount == null){
			transaCount = "0";
		}
		if("".equals(charge) || charge == null){
			charge = "0";
		}
		dealBean.setTransacount(Double.valueOf(transaCount));
		//dealBean.setTranscost(null);//手续费
		dealBean.setTerminalId(temp[Integer.valueOf(setBean.getTerminalCode())]);
		dealBean.setDeal_data(temp[Integer.valueOf(setBean.getDealData())]);
		dealBean.setDeal_time(temp[Integer.valueOf(setBean.getDealTime())]);
		dealBean.setDeal_status(temp[Integer.valueOf(setBean.getDealStatus())]);
		dealBean.setDealtype_id(temp[Integer.valueOf(setBean.getDealtypeId())]);
		dealBean.setDealdesc(temp[Integer.valueOf(setBean.getDealDesc())]);
		dealBean.setDealrebackcode(temp[Integer.valueOf(setBean.getDealRebackCode())]);
		dealBean.setBankcardNumber(temp[Integer.valueOf(setBean.getBankCardnumber())]);
		dealBean.setMerchantCode(temp[Integer.valueOf(setBean.getMerchantCode())]);
		dealBean.setCost(null);//手续费
		dealBean.setCharge(Double.valueOf(charge));//成本
		dealBean.setCreateDt(createDate);
		return dealBean;
	}
	/**
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
}
