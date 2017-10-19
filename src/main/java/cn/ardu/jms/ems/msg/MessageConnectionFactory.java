package cn.ardu.jms.ems.msg;

import java.util.Properties;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.NotSuppoertException;
import cn.ardu.jms.ems.impl.JmsConfig;
import cn.ardu.jms.ems.impl.JmsDestation;
import cn.ardu.jms.ems.impl.MessageConnectionJMS;
import cn.ardu.jms.ems.msg.Destination.DestinationType;

/**
 * 消息连接器工厂
 * 
 * <code>getMessageConnection()</code>方法返回根据配置文件生成的连接器
 * 
 * 
 * @author teamsun.com.cn
 * @see gov.chinapost.ems.msg.adapter.jms.impl.JmsConfig
 */
public class MessageConnectionFactory {

	/**
	 * �����ļ�config�ֶ�
	 */
	private static final String CONFIG = "CONFIG";

	/**
	 * �����ļ�encoding�ֶ�
	 */
	private static final String ENCODING = "ENCODING";
	
	static Logger log = Logger.getLogger(MessageConnectionFactory.class);//��־����


	/**
	 * ��������ļ����ض�Ӧ����Ϣ������
	 * 
	 * @TODO �쳣���� ��ʵ��ת�Ƶ��������
	 * @param prop
	 * @return ��Ϣ������
	 * 
	 */
	public static MessageConnection getMessageConnection(Properties prop) {

		String config = prop.getProperty(CONFIG);
		if (config.equals(MessageConnectionJMS.CONFIG_NAME)) {

			JmsConfig jmsConfig = new JmsConfig();
			jmsConfig.setEncoding(prop.getProperty(ENCODING));
			jmsConfig.setLookupName(prop
					.getProperty(MessageConnectionJMS.LOOKUPNAME));
			jmsConfig.setPassword(prop
					.getProperty(MessageConnectionJMS.PASSWORD));
			jmsConfig.setUrl(prop.getProperty(MessageConnectionJMS.URL));
			jmsConfig.setProvider(prop
					.getProperty(MessageConnectionJMS.PROVIDER));
			jmsConfig.setUsername(prop
					.getProperty(MessageConnectionJMS.USERNAME));
			jmsConfig.setReconnectTime(Integer.parseInt(prop
					.getProperty(MessageConnectionJMS.RECONNECT_TIME)));
			jmsConfig.setDebug(Boolean.parseBoolean(prop
					.getProperty(MessageConnectionJMS.DEBUG)));
			jmsConfig.setClassName(prop
					.getProperty(MessageConnectionJMS.CLASS_NAME));
			jmsConfig.setTcpUrl(prop.getProperty(MessageConnectionJMS.TCP_URL));
			jmsConfig.setUseJndi(Boolean.parseBoolean(prop
					.getProperty(MessageConnectionJMS.USE_JNDI)));

			MessageConnection msgConn = new MessageConnectionJMS();
			((MessageConnectionJMS) msgConn).setJmsConfig(jmsConfig);

			JmsDestation d_post = new JmsDestation();
			String d_post_s = prop
					.getProperty(MessageConnectionJMS.DEFAULT_DESTATION_POST_NAME);

			if (d_post_s != null && !"".equals(d_post_s)) {
				d_post.setDestationName(d_post_s.split("\\:")[0]);
				d_post
						.setDestationRealPath(prop
								.getProperty(MessageConnectionJMS.DEFAULT_DESTATION_POST_REALPATH));
				d_post.setDestationType(DestinationType.DESTINATION_POST);
				d_post.setTopic(d_post_s.split("\\:")[1].equals("topic"));

				msgConn.setDefaultDestination(d_post);
				log.info("Ĭ�Ϸ��͵�ַ��" + d_post);
			} else {
				log.debug("�����ļ���δ��Ĭ�ϵķ��͵�ַ");
			}

			JmsDestation d_recv = new JmsDestation();
			String d_recv_s = prop
					.getProperty(MessageConnectionJMS.DEFAULT_DESTATION_RECEIVE_NAME);

			if (d_recv_s != null && !"".equals(d_recv_s)) {
				d_recv.setDestationName(d_recv_s.split("\\:")[0]);
				d_recv.setTopic(d_recv_s.split("\\:")[1].equals("topic"));
				d_recv
						.setDestationRealPath(prop
								.getProperty(MessageConnectionJMS.DEFAULT_DESTATION_RECEIVE_REALPATH));
				d_recv.setDestationType(DestinationType.DESTINATION_RECEIVE);
				msgConn.setDefaultDestination(d_recv);
				log.info("Ĭ�Ͻ��յ�ַ��" + d_recv);
			} else {
				log.debug("�����ļ���δ��Ĭ�ϵĽ��յ�ַ");
			}
			registDestation(
					msgConn,
					prop.getProperty(MessageConnectionJMS.DESTATIONS_POST_NAME),
					prop
							.getProperty(MessageConnectionJMS.DESTATIONS_POST_REALPATH),
					DestinationType.DESTINATION_POST);

			registDestation(
					msgConn,
					prop
							.getProperty(MessageConnectionJMS.DESTATIONS_RECEIVE_NAME),
					prop
							.getProperty(MessageConnectionJMS.DESTATIONS_RECEIVE_REALPATH),
					DestinationType.DESTINATION_RECEIVE);

			if (jmsConfig.isDebug())
				((MessageConnectionJMS) msgConn).showAllDestations();
			return msgConn;
		} else {
			throw new NotSuppoertException("Ŀǰֻ�ṩJMS��Ϣ�շ�");
		}

	}
	
	/**
	 * ��������ļ����ض�Ӧ����Ϣ������
	 * 
	 * @TODO �쳣���� ��ʵ��ת�Ƶ��������
	 * @param jmsConfig
	 * @return ��Ϣ������
	 * 
	 */
	public static MessageConnection getMessageConnection(JmsConfig jmsConfig) {

		MessageConnection msgConn = new MessageConnectionJMS();
		((MessageConnectionJMS) msgConn).setJmsConfig(jmsConfig);

		JmsDestation d_post = new JmsDestation();
		String d_post_s = jmsConfig.getDefault_destation_post_name();

		if (d_post_s != null && !"".equals(d_post_s)) {
			d_post.setDestationName(d_post_s.split("\\:")[0]);
			d_post
					.setDestationRealPath(jmsConfig.getDefault_destation_post_realpath());
			d_post.setDestationType(DestinationType.DESTINATION_POST);
			d_post.setTopic(d_post_s.split("\\:")[1].equals("topic"));

			msgConn.setDefaultDestination(d_post);
			log.info("Ĭ�Ϸ��͵�ַ��" + d_post);
		} else {
			log.debug("�����ļ���δ��Ĭ�ϵķ��͵�ַ");
		}

		JmsDestation d_recv = new JmsDestation();
		String d_recv_s = jmsConfig.getDefault_destation_receive_name();

		if (d_recv_s != null && !"".equals(d_recv_s)) {
			d_recv.setDestationName(d_recv_s.split("\\:")[0]);
			d_recv.setTopic(d_recv_s.split("\\:")[1].equals("topic"));
			d_recv
					.setDestationRealPath(jmsConfig.getDefault_destation_receive_realpath());
			d_recv.setDestationType(DestinationType.DESTINATION_RECEIVE);
			msgConn.setDefaultDestination(d_recv);
			log.info("Ĭ�Ͻ��յ�ַ��" + d_recv);
		} else {
			log.debug("�����ļ���δ��Ĭ�ϵĽ��յ�ַ");
		}
		registDestation(msgConn,jmsConfig.getDestations_post_name(),jmsConfig.getDestations_post_realpath(),
				DestinationType.DESTINATION_POST);

		registDestation(
				msgConn,jmsConfig.getDestations_receive_name(),
				jmsConfig.getDestations_receive_realpath(),
				DestinationType.DESTINATION_RECEIVE);

		if (jmsConfig.isDebug())
			((MessageConnectionJMS) msgConn).showAllDestations();
		return msgConn;
	}

	private static void registDestation(MessageConnection msgConn, String name,
			String realPath, DestinationType dtype) {
		if (name == null || "".equals(name))
			return;
		int d = name.split("\\|").length;
		for (int i = 0; i < d; i++) {
			JmsDestation jd = new JmsDestation();
			jd.setDestationName(name.split("\\|")[i].split("\\:")[0]);
			jd.setTopic(name.split("\\|")[i].split("\\:")[1].equals("topic"));
			jd.setDestationRealPath(realPath.split("\\|")[i]);
			jd.setDestationType(dtype);
			msgConn.registerDestination(jd);
		}
	}

}
