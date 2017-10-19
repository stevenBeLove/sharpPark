package com.compass.utils;

import org.springframework.util.StringUtils;

/**
 * 
 * <pre>
 * 【类型】: Format <br/> 
 * 【作用】: 数据脱敏公共类 <br/>  
 * 【时间】：2016年11月11日 上午10:54:08 <br/> 
 * 【作者】：fuyu <br/>
 * </pre>
 */
public class FormatChar {

    /**
     * 
     * 【方法名】 :卡号加密. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年11月11日 上午10:54:27 .<br/>
     * 【参数】： .<br/>
     * 
     * @param cardId 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    public static String cardIdFormat(String cardId) {
        StringBuffer sb = new StringBuffer();
        sb.append(cardId.substring(0, 4));
        sb.append(" ");
        sb.append(cardId.substring(4, 6));
        sb.append("**");
        sb.append(" ");
        sb.append("**** ");
        sb.append(cardId.substring(cardId.length() - 4, cardId.length()));
        return sb.toString();
    }
    
    /**
     * 
     * 【方法名】    : 导出表格去除金额格式化. <br/> 
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/> 
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年11月18日 下午12:10:39 .<br/>
     * 【参数】： .<br/>
     * @param amount 
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: fuyu 修改描述： .<br/>
     * <p/>
     */
    public static String replaceMoney(String amount) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isEmpty(amount)) {
            return amount;
        } else {
            result.append(amount.replaceAll(",", ""));
        }
        return result.toString();
    }
    
    /**
     * 【方法名】    : (字段加密). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年2月23日 下午7:31:21 .<br/>
     * 【参数】： .<br/>
     * @param data
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String mosaic(String data) {
        if(data.indexOf("@")!=-1){
            String start = data.substring(0,3);
            String end = data.substring(data.indexOf("@"), data.length());
            return start+"****"+end;
        }else{
          if(data.length() > 4){
              String start = data.substring(0,3);
              String end = data.substring(data.length()-4, data.length());
              return start+"****"+end;
          }else{
              return "****";
          }
        }
        
    }
    
    /**
     * 
     * 【方法名】    : (身份证加密前二后二). <br/> 
     * 【作者】: 简思文 .<br/>
     * 【时间】： 2017年4月19日 下午3:39:46 .<br/>
     * 【参数】： .<br/>
     * @param data 接收参数
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  简思文  修改描述： .<br/>
     * <p/>
     */
    public static String IDCard(String data) {
        if (data.length() > 4) {
            String start = data.substring(0, 2);
            String end = data.substring(data.length() - 2, data.length());
            return start + "****" + end;
        } else {
            return "****";
        }
    }

    /**
     * 
     * 【方法名】 : 字段加密. <br/>
     * 【注意】: (这里描述这个方法的注意事项 – 可选).<br/>
     * 【作者】: fuyu .<br/>
     * 【时间】： 2016年11月11日 上午10:54:50 .<br/>
     * 【参数】： .<br/>
     * 
     * @param data 
     * @param type 
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: fuyu 修改描述： .<br/>
     *         <p/>
     */
    public static String mosaic(String data, String type) {
        /*
         * type1 姓名 只保留第一个字符 type2 手机号码 前三后四 type3 客户编号第一位以及后四为 type4 证件号码前十后四 中间四个* type5 第二交易帐号 根据不同情况判断 type6 终端编号 前四后五
         */
        String head;
        String end;
        int datasize;
        StringBuffer result = new StringBuffer();

        try {
            // 当数据为空直接返回
            if (StringUtils.isEmpty(data)) {
                return data;
            }
            datasize = data.length();
            if ("1".equals(type)) {
                result.append(data.substring(0, 1));
                for (int i = 1; i < datasize; i++) {
                    result.append("*");
                }
                return result.toString();
            }else if ("2".equals(type)) {
                if (datasize == 11) {
                    head = data.substring(0, 3);
                    end = data.substring(7);
                    return head + "****" + end;
                } else {
                    return "error";
                }
            }else if ("3".equals(type)) {
                if (datasize == 10) {
                    head = data.substring(0, 1);
                    end = data.substring(6);
                    return head + "*****" + end;
                } else {
                    return "error";
                }
            }else if ("4".equals(type)) {
                if (datasize == 18) {
                    head = data.substring(0, 3);
                    end = data.substring(14);
                    return head + "***********" + end;
                } else if (datasize == 15) {
                    head = data.substring(0, 3);
                    end = data.substring(11);
                    return head + "***********" + end;
                } else {
                    return "error";
                }
            }else if ("5".equals(type)) {
                if (datasize == 10) {
                    /*
                     * head = data.substring(0,1); end = data.substring(6); return head+"*****"+end;
                     */
                    return data;
                } else if (datasize == 11) {
                    head = data.substring(0, 3);
                    end = data.substring(7);
                    return head + "****" + end;
                } else if (datasize > 11) {
                    head = data.substring(0, 6);
                    end = data.substring(datasize - 4);
                    return head + "****" + end;
                } else {
                    return "error";
                }
            }else if ("6".equals(type)) {
                if (datasize > 10) {
                    head = data.substring(0, 4);
                    end = data.substring(-5);
                    return head + "***********" + end;
                } else {
                    return "error";
                }
            }else if("7".equals(type)){
                
            }
            return "";
        } finally {
            head = null;
            end = null;
            result = null;
        }
    }
}
