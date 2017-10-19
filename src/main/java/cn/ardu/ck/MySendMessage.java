package cn.ardu.ck;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.impl.JmsConfig;
import cn.ardu.jms.ems.msg.Mail;
import cn.ardu.jms.ems.msg.MessageConnection;
import cn.ardu.jms.ems.msg.MessageConnectionFactory;

public class MySendMessage {
	/**
	 * @param args
	 */
	static Logger log = Logger.getLogger(MySendMessage.class);// 日志对象

	static Properties p = null;
    static String jmsConfigFile = "/home/weblogic/etc/RtbPlatform/config_jms.properties";

	static JmsConfig jmsConfig = new JmsConfig();

	private static MySendMessage sm = null;

	public static MySendMessage getInstance() {
		if (null == sm)
			sm = new MySendMessage();

		sm.init();
		return sm;
	}

	private void init() {
		  InitJmsProperties();
	    jmsConfig.setEncoding(p.getProperty("ENCODING"));
			jmsConfig.setLookupName(p.getProperty("LOOKUPNAME"));
			jmsConfig.setPassword(p.getProperty("PASSWORD"));
			jmsConfig.setUrl(p.getProperty("URL"));
			jmsConfig
					.setProvider(p.getProperty("PROVIDER"));
			jmsConfig.setUsername(p.getProperty("USERNAME"));
			jmsConfig.setReconnectTime(Integer.parseInt(p.getProperty("RECONNECT_TIME")));
			jmsConfig.setDebug("true".equals(p.getProperty("DEBUG"))?true:false);
			jmsConfig.setClassName(p.getProperty("CLASS_NAME"));
			jmsConfig.setTcpUrl(p.getProperty("TCP_URL"));
			jmsConfig.setUseJndi("false".equals(p.getProperty("USE_JNDI"))?false:true);

			jmsConfig.setDefault_destation_post_name(p.getProperty("DEFAULT_DESTATION_POST_NAME"));
			jmsConfig.setDefault_destation_post_realpath(p.getProperty("DEFAULT_DESTATION_POST_REALPATH"));

			jmsConfig.setDefault_destation_receive_name(p.getProperty("DEFAULT_DESTATION_RECEIVE_NAME"));
			jmsConfig.setDefault_destation_receive_realpath(p.getProperty("DEFAULT_DESTATION_RECEIVE_REALPATH"));

			jmsConfig.setDestations_post_name(p.getProperty("DESTATIONS_POST_NAME"));
			jmsConfig.setDestations_post_realpath(p.getProperty("DESTATIONS_POST_REALPATH"));

			jmsConfig.setDestations_receive_name(p.getProperty("DESTATIONS_RECEIVE_NAME"));
			jmsConfig.setDestations_receive_realpath(p.getProperty("DESTATIONS_RECEIVE_REALPATH"));
			p=null;
	}

	public static void toSend(String msg) {
		MessageConnection msgConn = null;
		try{
			log.info("短信内容：" + msg);
			msgConn = MessageConnectionFactory
							.getMessageConnection(jmsConfig);
			Mail mail = new Mail(msg);
/*			mail.addStringProperty(Mail.MAIL_ENCODING, "GBK");
			mail.addStringProperty("JMSCorrelationID", "Zc19_Send_SMS");*/
		      mail.addStringProperty(Mail.MAIL_ENCODING, "GBK");
		      mail.addStringProperty("JMSCorrelationID", "Zc19_Send_SMS");
			msgConn.postMessage(mail);
		}catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			if (msgConn != null) {
				msgConn.close();
			}
		}
	}

	/**
	 * 初始化代发配置属性
	 */
	private static void InitJmsProperties() {
		InputStream is = null;
		try {
			is = new FileInputStream(jmsConfigFile);
			p = new Properties();
			p.load(is);

		} catch (IOException ioe) {
			log.error("读取代发jms配置失败", ioe);
		} finally {
			if (is != null) {
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public static void main(String[] args) {
		String msg = "20141222|182024|059259|18721115951|交易回执:您向尾号7225的用户付款5000.00元成功,请联系收款人查询收款|00030566";
		MySendMessage.getInstance().toSend(msg);
	}
}
