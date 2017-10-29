package com.compass.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import cn.ardu.jms.ems.exception.QTException;
//
//import com.alibaba.fastjson.JSONObject;
//import com.compass.common.FrConstants;
//import com.imobpay.platform.pubfunc.EmptyChecker;
//import com.imobpay.platform.pubfunc.Tools;
//import com.tibco.tibjms.TibjmsQueueConnectionFactory;

/**
 * 
 * @author Lance.Wu
 * @since 2015年7月10日 上午9:52:24
 * 
 *        <pre>
 * 发送Tibco外部服务接口
 * </pre>
 */
public class PubTibcoSend {
    
//    private final static Log logger = LogFactory.getLog(PubTibcoSend.class);
//    /** 唯一码：六位 */
//    private static final String BODY_LOGNO = "CORRID";
//
//    /** TCP */
//    // private static final String TCPUSERCLS = "tcp://127.0.0.1:7222";
//    /** 参数 */
//    private static final String ENCODECLS = "UTF-8";
//    /** 参数 */
//    private static final String USERNAMECLS = "admin";
//    /** 参数 */
//    private static final String PASSWORDCLS = "";
//    /** 父级队列名 */
//    private static final String LOOKUPNAMECLS = "QueueConnectionFactory";
//    /** 默认读取服务器超时时间为：20钞 */
//    private static final int TIMEOUTCLS = 20000;
//
//    /** 默认不接收 */
//    private static final boolean ISRECFLAGCSS = false;
//
//    /** 发送队列 */
//    // private static final String SENDDESCNAMECLS = "";
//    /** 接收队列 */
//    // private static final String RECDESCnAMECLS = "";
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param encode
//     *            编码格式
//     * @param userName
//     *            用户名
//     * @param pwd
//     *            用户密码
//     * @param lookupName
//     *            父级队列名
//     * @param sendDescName
//     *            发送队列名
//     * @param recDescName
//     *            接收队列名
//     * @param serverTimeOut
//     *            服务器超时时间
//     * @param dataMap
//     *            发送队列数据
//     * @param isRecFalg
//     *            是否接收
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String encode, String userName, String pwd, String lookupName, String sendDescName, String recDescName, int serverTimeOut,
//            Map<String, Object> dataMap, Boolean isRecFalg) throws QTException {
//
//        TibjmsQueueConnectionFactory tqc = null;
//        QueueSession sendSession = null;
//        QueueSession recSession = null;
//        QueueConnection conn = null;
//
//        MessageProducer mp = null;
//        QueueReceiver receiver = null;
//
//        /** 发送数据异常 */
//        if (EmptyChecker.isEmpty(dataMap)) {
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "发送数据不为空");
//        }
//
//        String logno = Tools.getOnlyPK();
//        dataMap.put(BODY_LOGNO, logno);
//
//        JSONObject json = (JSONObject) JSONObject.toJSON(dataMap);
//
//        logger.info("发送地址为=====>>>" + url);
//        logger.info("发送队列=====>>>" + sendDescName);
//        logger.info("发送核心数据=====>>>" + json.toString());
//
//        try {
//            tqc = new TibjmsQueueConnectionFactory(url);
//            conn = tqc.createQueueConnection(userName, pwd);
//            conn.start();
//
//            /** 初始化数据-得到连接 */
//
//            sendSession = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//            Queue sendQ = sendSession.createQueue(sendDescName);
//            mp = sendSession.createProducer(sendQ);
//
//            BytesMessage message = sendSession.createBytesMessage();
//            message.setJMSCorrelationID(logno);
//            message.writeBytes(json.toString().getBytes(encode));
//
//            mp.send(message);
//
//            if (isRecFalg == Boolean.TRUE) {
//                recSession = conn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
//                Queue recQueue = recSession.createQueue(recDescName);
//                receiver = recSession.createReceiver(recQueue, "JMSCorrelationID='" + logno + "'");
//                BytesMessage receive = (javax.jms.BytesMessage) receiver.receive(serverTimeOut);
//
//                if (EmptyChecker.isEmpty(receive)) {
//                    throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//                }
//                byte[] bmessage = new byte[(int) receive.getBodyLength()];
//                receive.readBytes(bmessage);
//                receive.acknowledge();
//
//                String content = new String(bmessage, encode);
//                logger.info("接收核心队列数据=====[" + recDescName + "]>>>" + content);
//                return JSONObject.parseObject(content);
//
//            }
//            return null;
//        } catch (JMSException e) {
//            logger.error(e.getMessage() + "发送核心异常.", e);
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage() + "发送核心异常.", e);
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } finally {
//            try {
//                if (mp != null) {
//                    mp.close();
//                }
//                if (receiver != null) {
//                    receiver.close();
//                }
//                if (recSession != null) {
//                    recSession.close();
//                }
//                if (sendSession != null) {
//                    sendSession.close();
//                }
//                if (conn != null) {
//                    conn.stop();
//                    conn.close();
//                }
//            } catch (JMSException e) {
//                logger.error("参数异常:" + e.getMessage(), e);
//                throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "系统异常");
//            }
//        }
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param encode
//     *            编码格式
//     * @param userName
//     *            用户名
//     * @param pwd
//     *            用户密码
//     * @param lookupName
//     *            父级队列名
//     * @param sendDescName
//     *            发送队列名
//     * @param recDescName
//     *            接收队列名
//     * @param dataMap
//     *            发送队列数据
//     * @param isRecFalg
//     *            是否接收
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String encode, String userName, String pwd, String lookupName, String sendDescName, String recDescName,
//            Map<String, Object> dataMap, Boolean isRecFalg) throws QTException {
//        return sendCoreInfo(url, encode, userName, pwd, lookupName, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRecFalg);
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param encode
//     *            编码格式
//     * @param lookupName
//     *            父级队列名
//     * @param sendDescName
//     *            发送队列名
//     * @param recDescName
//     *            接收队列名
//     * @param dataMap
//     *            发送队列数据
//     * @param isRecFalg
//     *            是否接收
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String encode, String lookupName, String sendDescName, String recDescName, Map<String, Object> dataMap, Boolean isRecFalg)
//        throws QTException {
//        return sendCoreInfo(url, encode, USERNAMECLS, PASSWORDCLS, lookupName, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRecFalg);
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param encode
//     *            编码格式
//     * @param sendDescName
//     *            发送队列名
//     * @param recDescName
//     *            接收队列名
//     * @param dataMap
//     *            发送队列数据
//     * @param isRecFalg
//     *            是否接收
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String encode, String sendDescName, String recDescName, Map<String, Object> dataMap, Boolean isRecFalg) throws QTException {
//        return sendCoreInfo(url, encode, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRecFalg);
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param sendDescName
//     *            发送队列名
//     * @param recDescName
//     *            接收队列名
//     * @param dataMap
//     *            发送队列数据
//     * @param isRecFalg
//     *            是否接收
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String sendDescName, String recDescName, Map<String, Object> dataMap, Boolean isRecFalg) throws QTException {
//        return sendCoreInfo(url, ENCODECLS, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRecFalg);
//    }
//
//    /**
//     * 
//     * 方法名： sendCoreInfo.<br/>
//     *
//     * 创建者：Lance.Wu.<br/>
//     * 创建日期：2016年1月18日.<br/>
//     * 创建时间：上午10:13:45.<br/>
//     * 参数者异常：@param url .<br/>
//     * 参数者异常：@param sendDescName .<br/>
//     * 参数者异常：@param recDescName .<br/>
//     * 参数者异常：@param dataMap .<br/>
//     * 参数者异常：@param isRec .<br/>
//     * 参数者异常：@param isTest .<br/>
//     * 参数者异常：@throws QTException .<br/>
//     * 返回值： @return 返回结果：JSONObject.<br/>
//     * 其它内容： JDK 1.6 qtservices 1.0.<br/>
//     */
//    public static JSONObject sendCoreInfo(String url, String sendDescName, String recDescName, Map<String, Object> dataMap, boolean isRec, boolean isTest) throws QTException {
//
//        if (isTest) {
//            return sendCoreInfoTest(url, ENCODECLS, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRec);
//        } else {
//            return sendCoreInfo(url, ENCODECLS, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, recDescName, TIMEOUTCLS, dataMap, isRec);
//        }
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param sendDescName
//     *            发送队列名
//     * @param dataMap
//     *            发送队列数据
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String sendDescName, Map<String, Object> dataMap) throws QTException {
//        return sendCoreInfo(url, ENCODECLS, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, "", TIMEOUTCLS, dataMap, ISRECFLAGCSS);
//    }
//
//    /**
//     * @author Lance.Wu
//     * @since 2015年7月14日 上午10:56:06
//     * 
//     *        <pre>
//     * 发送银行核心队列
//     * </pre>
//     * @param url
//     *            访问地址
//     * @param sendDescName
//     *            发送队列名
//     * @param dataMap
//     *            发送队列数据
//     * @param test
//     *            测试类
//     * @return JSONObject 数据格式
//     * @throws QTException
//     *             卡服务异常
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfo(String url, String sendDescName, Map<String, Object> dataMap, Boolean test) throws QTException {
//        return sendCoreInfo(url, ENCODECLS, USERNAMECLS, PASSWORDCLS, LOOKUPNAMECLS, sendDescName, "", TIMEOUTCLS, dataMap, ISRECFLAGCSS);
//    }
//
//    /**
//     * 
//     * 方法名： sendCoreInfoTest.<br/>
//     *
//     * 创建者：Lance.Wu.<br/>
//     * 创建日期：2016年1月18日.<br/>
//     * 创建时间：上午10:13:10.<br/>
//     * 参数者异常：@param url .<br/>
//     * 参数者异常：@param encode .<br/>
//     * 参数者异常：@param userName .<br/>
//     * 参数者异常：@param pwd .<br/>
//     * 参数者异常：@param lookupName .<br/>
//     * 参数者异常：@param sendDescName .<br/>
//     * 参数者异常：@param recDescName .<br/>
//     * 参数者异常：@param serverTimeOut .<br/>
//     * 参数者异常：@param dataMap .<br/>
//     * 参数者异常：@param isRecFalg .<br/>
//     * 参数者异常：@throws QTException .<br/>
//     * 返回值： @return 返回结果：JSONObject.<br/>
//     * 其它内容： JDK 1.6 qtservices 1.0.<br/>
//     */
//    public static JSONObject sendCoreInfoTest(String url, String encode, String userName, String pwd, String lookupName, String sendDescName, String recDescName,
//            int serverTimeOut, Map<String, Object> dataMap, Boolean isRecFalg) throws QTException {
//
//        TibjmsQueueConnectionFactory tqc = null;
//        QueueSession sendSession = null;
//        QueueSession recSession = null;
//        QueueConnection conn = null;
//
//        MessageProducer mp = null;
//        QueueReceiver receiver = null;
//
//        /** 发送数据异常 */
//        if (EmptyChecker.isEmpty(dataMap)) {
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "发送数据不为空");
//        }
//
//        String logno = Tools.getOnlyPK();
//        dataMap.put(BODY_LOGNO, logno);
//
//        JSONObject json = (JSONObject) JSONObject.toJSON(dataMap);
//
//        System.out.println("发送地址为=====>>>" + url);
//        System.out.println("发送队列=====>>>" + sendDescName);
//        System.out.println("发送核心数据=====>>>" + json.toString());
//
//        try {
//            tqc = new TibjmsQueueConnectionFactory(url);
//            conn = tqc.createQueueConnection(userName, pwd);
//            conn.start();
//
//            /** 初始化数据-得到连接 */
//
//            sendSession = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//            Queue sendQ = sendSession.createQueue(sendDescName);
//            mp = sendSession.createProducer(sendQ);
//
//            BytesMessage message = sendSession.createBytesMessage();
//            message.setJMSCorrelationID(logno);
//            message.writeBytes(json.toString().getBytes(encode));
//
//            mp.send(message);
//
//            if (isRecFalg == Boolean.TRUE) {
//                recSession = conn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
//                Queue recQueue = recSession.createQueue(recDescName);
//                receiver = recSession.createReceiver(recQueue, "JMSCorrelationID='" + logno + "'");
//                BytesMessage receive = (javax.jms.BytesMessage) receiver.receive(serverTimeOut);
//
//                if (EmptyChecker.isEmpty(receive)) {
//                    throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//                }
//                byte[] bmessage = new byte[(int) receive.getBodyLength()];
//                receive.readBytes(bmessage);
//                receive.acknowledge();
//
//                String content = new String(bmessage, encode);
//                System.out.println("接收核心队列数据=====[" + recDescName + "]>>>" + content);
//                return JSONObject.parseObject(content);
//            }
//            return null;
//        } catch (JMSException e) {
//            System.err.println(e.getMessage() + "发送核心异常.");
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } catch (UnsupportedEncodingException e) {
//            System.err.println(e.getMessage() + "发送核心异常.");
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } finally {
//            try {
//                if (mp != null) {
//                    mp.close();
//                }
//                if (receiver != null) {
//                    receiver.close();
//                }
//                if (recSession != null) {
//                    recSession.close();
//                }
//                if (sendSession != null) {
//                    sendSession.close();
//                }
//                if (conn != null) {
//                    conn.stop();
//                    conn.close();
//                }
//            } catch (JMSException e) {
//                System.err.println("参数异常:" + e.getMessage());
//                throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "系统异常");
//            }
//        }
//    }
//
//    /**
//     * 
//     * 方法名： sendCoreInfotwo.<br/>
//     * 适用条件:<br/>
//     * 执行流程:<br/>
//     * 注意事项:<br/>
//     * 方法作用:发送对列数据改成string<br/>
//     *
//     * 创建者：黄强.<br/>
//     * 创建日期：2016年5月25日.<br/>
//     * 创建时间：上午11:29:22.<br/>
//     * 参数者异常：@param url .<br/>
//     * 参数者异常：@param encode .<br/>
//     * 参数者异常：@param userName .<br/>
//     * 参数者异常：@param pwd .<br/>
//     * 参数者异常：@param lookupName .<br/>
//     * 参数者异常：@param sendDescName .<br/>
//     * 参数者异常：@param recDescName .<br/>
//     * 参数者异常：@param serverTimeOut .<br/>
//     * 参数者异常：@param senddata .<br/>
//     * 参数者异常：@param isRecFalg .<br/>
//     * 参数者异常：@return .<br/>
//     * 参数者异常：@throws QTException .<br/>
//     * 其它内容： JDK 1.6 qtservices 1.0.<br/>
//     */
//    @SuppressWarnings("all")
//    public static JSONObject sendCoreInfotwo(String url, String encode, String userName, String pwd, String lookupName, String sendDescName, String recDescName, int serverTimeOut,
//            String senddata, Boolean isRecFalg) throws QTException {
//
//        TibjmsQueueConnectionFactory tqc = null;
//        QueueSession sendSession = null;
//        QueueSession recSession = null;
//        QueueConnection conn = null;
//
//        MessageProducer mp = null;
//        QueueReceiver receiver = null;
//
//        /** 发送数据异常 */
//        if (EmptyChecker.isEmpty(senddata)) {
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "发送数据不为空");
//        }
//
//        String logno = Tools.getOnlyPK();
//
//        logger.info("发送地址为=====>>>" + url);
//        logger.info("发送队列=====>>>" + sendDescName);
//        logger.info("发送核心数据=====>>>" + senddata);
//
//        try {
//            tqc = new TibjmsQueueConnectionFactory(url);
//            conn = tqc.createQueueConnection(userName, pwd);
//            conn.start();
//
//            /** 初始化数据-得到连接 */
//            sendSession = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//            Queue sendQ = sendSession.createQueue(sendDescName);
//            mp = sendSession.createProducer(sendQ);
//
//            BytesMessage message = sendSession.createBytesMessage();
//            message.setJMSCorrelationID(logno);
//            message.writeBytes(senddata.getBytes(encode));
//
//            mp.send(message);
//
//            if (isRecFalg == Boolean.TRUE) {
//                recSession = conn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
//                Queue recQueue = recSession.createQueue(recDescName);
//                receiver = recSession.createReceiver(recQueue, "JMSCorrelationID='" + logno + "'");
//                BytesMessage receive = (javax.jms.BytesMessage) receiver.receive(serverTimeOut);
//
//                if (EmptyChecker.isEmpty(receive)) {
//                    throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//                }
//                byte[] bmessage = new byte[(int) receive.getBodyLength()];
//                receive.readBytes(bmessage);
//                receive.acknowledge();
//
//                String content = new String(bmessage, encode);
//                logger.info("接收核心队列数据=====[" + recDescName + "]>>>" + content);
//                return JSONObject.parseObject(content);
//
//            }
//            return null;
//        } catch (JMSException e) {
//            logger.error(e.getMessage() + "发送核心异常.", e);
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage() + "发送核心异常.", e);
//            throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "请求超时,请稍后重试.");
//        } finally {
//            try {
//                if (mp != null) {
//                    mp.close();
//                }
//                if (receiver != null) {
//                    receiver.close();
//                }
//                if (recSession != null) {
//                    recSession.close();
//                }
//                if (sendSession != null) {
//                    sendSession.close();
//                }
//                if (conn != null) {
//                    conn.stop();
//                    conn.close();
//                }
//            } catch (JMSException e) {
//                logger.error("参数异常:" + e.getMessage(), e);
//                throw new QTException(FrConstants.RESP_CODE_15_ERR_HOST, "系统异常");
//            }
//        }
//    }

}
