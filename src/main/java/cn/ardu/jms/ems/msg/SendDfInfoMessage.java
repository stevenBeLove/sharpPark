package cn.ardu.jms.ems.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import cn.ardu.jms.ems.impl.JmsConfig;

public class SendDfInfoMessage {
	
static Logger log = Logger.getLogger(SendDfInfoMessage.class);
	
	private static String dfConfigFile = "resources/config_jms_df_passageway.properties";
	private static Properties sendDfProperties = null;

	static {
		InitSendDfProperties();
	}
	
	static boolean flag = false;

	JmsConfig jmsConfig = new JmsConfig();
	
	/**
	 * @Fields CONFIG 消息类型 JMS
	 */
	String CONFIG = "JMS";

	/**
	 * @Fields CONN_MODE 连接模式 TCP_ZL JNDI
	 */
	String CONN_MODE = "TCP_ZL";

	/**
	 * @Fields TCP_URL 连接地址
	 */
	String TCP_URL = "tcp://127.0.0.1:7222";

	/**
	 * 
	 */
	String LOOKUPNAME = "";

	/**
	 * @Fields JNDI地址
	 */
	String JNDI_URL = "";

	/**
	 * JNDI 工厂提供者
	 */
	String PROVIDER = "";

	/**
	 * @Fields USE_JNDI 是否使用JNDI
	 */
	boolean USE_JNDI = false;

	String CLASS_NAME = "com.tibco.tibjms.TibjmsConnectionFactory";

	/**
	 * 默认的发送地址 :前为友好名称，后面为目的地类型
	 */
	String DEFAULT_DESTATION_POST_NAME = "";

	/**
	 * 默认发送地址的友好名称对应的实际地址
	 */
	String DEFAULT_DESTATION_POST_REALPATH = "";

	/**
	 * 默认的接收地址 :前为友好名称 后面为目的地类型
	 */
	String DEFAULT_DESTATION_RECEIVE_NAME = "";

	/**
	 * 与默认的接收地址对应的实际地址 此处需要填写ESB实际的队列名称
	 */
	String DEFAULT_DESTATION_RECEIVE_REALPATH = "";

	/**
	 * 连接失败时重新连接间隔的时间 毫秒
	 */
	int RECONNECT_TIME = 0;

	/**
	 * 调试模式
	 */
	boolean DEBUG = true;

	/**
	 * 是否主动回复应答
	 */
	boolean IS_SEND_RESPONE = true;

	/**
	 * 发送地址： 前为友好名称，后面为目的地类型
	 */
	String DESTATIONS_POST_NAME = "";

	/**
	 * 发送地址对应的实际地址
	 */
	String DESTATIONS_POST_REALPATH = "";

	/**
	 * 接收地址：前为友好名称 后面为目的地类型
	 */
	String DESTATIONS_RECEIVE_NAME = "";

	/**
	 * 接收地址对应的实际地址
	 */
	String DESTATIONS_RECEIVE_REALPATH = "";

	/**
	 * @Fields 用户名
	 */
	String USER_NAME = "";

	/**
	 * @Fields 密码
	 */
	String PASSWORD = "";

	/**
	 * @Fields threads 线程数
	 */
	private int threads = 150;

	/**
	 * @Fields mode 连接模式 队列模式、主题模式
	 */
	private String MSG_MODE = "QUEUE";

	/**
	 * @Fields charset
	 */
	private String charset = "GBK";

	public void initDFQueueParame(String queueparam) {

		this.CONFIG = sendDfProperties.getProperty("CONFIG");

		this.TCP_URL = sendDfProperties.getProperty("TCP_URL");

		this.LOOKUPNAME = sendDfProperties.getProperty("LOOKUPNAME");

		this.JNDI_URL = sendDfProperties.getProperty("JNDI_URL");

		this.PROVIDER = sendDfProperties.getProperty("PROVIDER");
		
		this.CONN_MODE = sendDfProperties.getProperty("CONN_MODE", this.CONN_MODE);

		this.USER_NAME = sendDfProperties.getProperty("USER_NAME");

		this.PASSWORD = sendDfProperties.getProperty("PASSWORD");

		this.MSG_MODE = sendDfProperties.getProperty("MSG_MODE", this.MSG_MODE);

		this.charset = sendDfProperties.getProperty("charset",charset); // 编码格式
		this.IS_SEND_RESPONE = true;

		this.DEFAULT_DESTATION_POST_NAME = sendDfProperties.getProperty("DEFAULT_DESTATION_POST_NAME");

		this.DEFAULT_DESTATION_POST_REALPATH = queueparam;

		this.DEFAULT_DESTATION_RECEIVE_NAME = sendDfProperties.getProperty("DEFAULT_DESTATION_RECEIVE_NAME");

		this.DEFAULT_DESTATION_RECEIVE_REALPATH = sendDfProperties.getProperty("DEFAULT_DESTATION_RECEIVE_REALPATH");

		this.DESTATIONS_POST_NAME = sendDfProperties.getProperty("DESTATIONS_POST_NAME");

		this.DESTATIONS_POST_REALPATH = sendDfProperties.getProperty("DESTATIONS_POST_REALPATH");

		this.DESTATIONS_RECEIVE_NAME = sendDfProperties.getProperty("DESTATIONS_RECEIVE_NAME");

		this.DESTATIONS_RECEIVE_REALPATH = sendDfProperties.getProperty("DESTATIONS_RECEIVE_REALPATH");
		
		jmsConfig.setEncoding(this.charset);
		jmsConfig.setLookupName(this.LOOKUPNAME);
		jmsConfig.setPassword(this.PASSWORD);
		jmsConfig.setUrl(this.JNDI_URL);
		jmsConfig.setProvider(this.PROVIDER);
		jmsConfig.setUsername(this.USER_NAME);
		jmsConfig.setReconnectTime(this.RECONNECT_TIME);
		jmsConfig.setDebug(this.DEBUG);
		
		
		jmsConfig.setClassName(this.CLASS_NAME);
		jmsConfig.setTcpUrl(this.TCP_URL);
		jmsConfig.setUseJndi(this.USE_JNDI);

		jmsConfig
				.setDefault_destation_post_name(this.DEFAULT_DESTATION_POST_NAME);
		jmsConfig
				.setDefault_destation_post_realpath(this.DEFAULT_DESTATION_POST_REALPATH);

		jmsConfig
				.setDefault_destation_receive_name(this.DEFAULT_DESTATION_RECEIVE_NAME);
		jmsConfig
				.setDefault_destation_receive_realpath(this.DEFAULT_DESTATION_RECEIVE_REALPATH);

		jmsConfig.setDestations_post_name(this.DESTATIONS_POST_NAME);
		jmsConfig.setDestations_post_realpath(this.DESTATIONS_POST_REALPATH);

		jmsConfig.setDestations_receive_name(this.DESTATIONS_RECEIVE_NAME);
		jmsConfig
				.setDestations_receive_realpath(this.DESTATIONS_RECEIVE_REALPATH);

		flag = true; // 加载参数成功

	}
	
	/**
	 * 初始化代发配置属性
	 */
	private static void InitSendDfProperties() {
		InputStream is = null;
		try {
			is = SendDfInfoMessage.class.getClassLoader().getResourceAsStream(dfConfigFile);
			Properties prop = new Properties();
			prop.load(is);
			String default_destation_post_realpath = prop.getProperty("DEFAULT_DESTATION_POST_REALPATH");
			String default_destation_post_name = prop.getProperty("DEFAULT_DESTATION_POST_NAME");
			String default_destation_receive_realpath = prop.getProperty("DEFAULT_DESTATION_RECEIVE_REALPATH");
			String default_destation_receive_name = prop.getProperty("DEFAULT_DESTATION_RECEIVE_NAME");

			sendDfProperties = (Properties) prop.clone();
			sendDfProperties.put("DEFAULT_DESTATION_POST_REALPATH", default_destation_receive_realpath);
			sendDfProperties.put("DEFAULT_DESTATION_POST_NAME", default_destation_receive_name);
			sendDfProperties.put("DEFAULT_DESTATION_RECEIVE_REALPATH", default_destation_post_realpath);
			sendDfProperties.put("DEFAULT_DESTATION_RECEIVE_NAME", default_destation_post_name);

		} catch (IOException ioe) {
			log.error("读取代发jms配置失败", ioe);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void procDFContent(String message)throws Exception{
		MessageConnection msgConn = null;
		try {
			log.info("通道发送代发队列内容====>" + message);
			msgConn = MessageConnectionFactory.getMessageConnection(jmsConfig);
			Mail mail = new Mail(message);
			mail.addStringProperty("MAIL_CONTENT_ENCODING", this.charset);
			mail.addStringProperty("JMSCorrelationID", "DFPASSAGEWAY");
			msgConn.postMessage(mail);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			if (msgConn != null)
				msgConn.close();
		}
	}
}
