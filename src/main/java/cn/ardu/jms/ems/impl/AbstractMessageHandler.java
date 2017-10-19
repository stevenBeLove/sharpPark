package cn.ardu.jms.ems.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.ConnectionFaultException;
import cn.ardu.jms.ems.msg.Destination;

/**
 * 抽象信息处理器
 * 
 * @author teamsun.com.cn
 */
public abstract class AbstractMessageHandler {

	private JmsConfig jmsConfig;

	private Connection connection;

	static Logger log = Logger.getLogger(AbstractMessageHandler.class);//��־����
	
	/**
	 * 建立连接
	 * 
	 * @throws JMSException
	 */
	public void connect() throws ConnectionFaultException {

		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				log.error(e.getMessage(), e);
			}
			connection = null;
		}
		if (jmsConfig.isUseJndi())
			connection = getConnectionUseJndi();
		else
			connection = getConnectionUseNative();
	}

	@SuppressWarnings("unchecked")
	private Connection getConnectionUseNative() throws ConnectionFaultException {
		log.info("ʹ使用Native方式获取连接");
		Connection conn = null;
		try {
			Constructor<ConnectionFactory> ctor = (Constructor<ConnectionFactory>) Class
					.forName(getJmsConfig().getClassName()).getConstructor(
							String.class);
			ConnectionFactory factory = ctor.newInstance(getJmsConfig()
					.getTcpUrl());

			// ConnectionFactory factory = new
			// com.tibco.tibjms.TibjmsConnectionFactory(
			// getJmsConfig().getTcpUrl());
			//
			conn = factory.createConnection(getJmsConfig().getUsername(),
					getJmsConfig().getPassword());

			return conn;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
			throw new ConnectionFaultException();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return conn;

	}

	private Connection getConnectionUseJndi() throws ConnectionFaultException {

		log.info("使用JNDI方式获取连接");
		Connection connection = null;
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("java.naming.factory.initial", getJmsConfig()
				.getProvider());
		hashtable.put("java.naming.provider.url", getJmsConfig().getUrl());
		hashtable.put("java.naming.security.principal", getJmsConfig()
				.getUsername());
		hashtable.put("java.naming.security.credentials", getJmsConfig()
				.getPassword());

		Context context = null;
		try {
			context = new InitialContext(hashtable);
			ConnectionFactory factory = (ConnectionFactory) context
					.lookup(getJmsConfig().getLookupName());
			connection = factory.createConnection(getJmsConfig().getUsername(),
					getJmsConfig().getPassword());
			return connection;
		} catch (NamingException e) {
			log.error("NamingException\t" + e.getMessage());
			throw new ConnectionFaultException();
		} catch (JMSException e) {
			log.error("JMSException\t" + e.getMessage());
			throw new ConnectionFaultException();
		}
	}

	public abstract void close();

	/**
	 * 获取实际的JMS Destation
	 * 
	 * @param session
	 *            jms session
	 * @param destation
	 * @see gov.chinapost.ems.msg.adapter.Destination �������ϢĿ�ĵ�
	 * @return
	 * @throws JMSException
	 */
	protected javax.jms.Destination getJmsDestation(Session session,
			Destination destation) throws JMSException {

		javax.jms.Destination jmsDest = null;
		if (((JmsDestation) destation).isTopic()) {
			jmsDest = session.createTopic(destation.getDestationRealPath());
		} else {
			jmsDest = session.createQueue(destation.getDestationRealPath());
		}
		return jmsDest;
	}

	/**
	 * 初始化
	 * 
	 * @throws ConnectionFaultException
	 */
	abstract public void init() throws ConnectionFaultException;

	public JmsConfig getJmsConfig() {
		return jmsConfig;
	}

	public void setJmsConfig(JmsConfig jmsConfig) {
		this.jmsConfig = jmsConfig;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
