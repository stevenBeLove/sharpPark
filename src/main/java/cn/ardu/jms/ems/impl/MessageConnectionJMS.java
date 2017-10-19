package cn.ardu.jms.ems.impl;



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.core.AbstractMessageConnection;
import cn.ardu.jms.ems.exception.ConnectionFaultException;
import cn.ardu.jms.ems.msg.Destination;
import cn.ardu.jms.ems.msg.Mail;
import cn.ardu.jms.ems.msg.PostMessageFaultService;
import cn.ardu.jms.ems.msg.PostMessageService;
import cn.ardu.jms.ems.msg.ReceiveMessageService;
import cn.ardu.jms.ems.msg.Destination.DestinationType;

/**
 * 以JMS方式实现的信息连接器
 * 
 * 对于同一个URL的JMS服务器，所有发送的信息使用一个JMS Connection，每个接收器独立使用一个JMS Connection。
 * 
 * @author teamsun.com.cn
 * 
 */
public class MessageConnectionJMS extends AbstractMessageConnection {

	private static final Lock		lock	= new java.util.concurrent.locks.ReentrantLock();

	private ReentrantReadWriteLock	rwLock	= new ReentrantReadWriteLock();
	/**
	 * jms 消息发送器
	 */
	private MessageSender			messageSender;

	/**
	  * jms 配置信息
	 */
	private JmsConfig				jmsConfig;

	/**
	 * jms 信息接收的连接监控
	 */
	private Reconnectable			reconnectable;

	private PostMessageFaultService	faultService;
	
	static Logger log = Logger.getLogger(MessageConnectionJMS.class);//��־����

	public void postMessage(Mail mail) {
		postMessage(getDefaultDestination(DestinationType.DESTINATION_POST), mail);
	}

	/**
	 * 未经测试
	 * 
	 * @param destation
	 * @param mail
	 */
	public void postMessageEx(Destination destation, Mail mail) {
		rwLock.readLock().lock();
		if (null == messageSender || null == messageSender.getConnection()) {
			rwLock.readLock().unlock(); // unlock first to obtain
			// writelock
			rwLock.writeLock().lock();
			if (null == messageSender || null == messageSender.getConnection()) {
				messageSender = new MessageSender();
				messageSender.setJmsConfig(jmsConfig);
				try {
					messageSender.init();
				} catch (ConnectionFaultException e) {
					log.error("发送信息初始化失败 启动重新连接：" + e.getMessage());
					messageSender.startReconnect();
				}

				if (this.faultService != null)
					messageSender.setPostMessageFaultService(this.faultService);
			}
			rwLock.readLock().lock();
			rwLock.writeLock().unlock();
		}
		messageSender.postMessage(destation, mail);
		rwLock.readLock().unlock();
	}

	/**
	 * 
	 */
	public void postMessage(Destination destation, Mail mail) {
		if (null == messageSender || null == messageSender.getConnection()) {
			lock.lock();
			try {
				if (null == messageSender || null == messageSender.getConnection()) {
					messageSender = new MessageSender();
					messageSender.setJmsConfig(jmsConfig);
					try {
						messageSender.init();
					} catch (ConnectionFaultException e) {
						log.error("发送信息初始化失败 启动重新连接：" + e.getMessage());
						messageSender.startReconnect();
					}

					if (this.faultService != null)
						messageSender.setPostMessageFaultService(this.faultService);
				}

			}
			finally {
				lock.unlock();
			}
		}
		messageSender.postMessage(destation, mail);
	}

	public void registerPostMessageFaultService(PostMessageFaultService faultService) {
		this.faultService = faultService;
		if (messageSender != null)
			messageSender.setPostMessageFaultService(faultService);
	}

	public void postMessage(PostMessageService postMsg) {
		postMessage(getDefaultDestination(DestinationType.DESTINATION_POST), postMsg);
	}

	public void postMessage(Destination destation, PostMessageService postMsg) {
		Mail text = postMsg.postMessage();
		postMessage(destation, text);
	}

	public void registerReceiveMessageService(ReceiveMessageService receMsg) {
		registerReceiveMessageService(getDefaultDestination(DestinationType.DESTINATION_RECEIVE), receMsg);
	}

	public void registerReceiveMessageService(Destination destination, final ReceiveMessageService receMsg,
			final String charset) {

		if (this.reconnectable == null) {
			synchronized (this) {
				if (this.reconnectable == null) {
					reconnectable = new Reconnectable();
					reconnectable.setDelay(this.getJmsConfig().getReconnectTime());
					reconnectable.setDaemon(false);
					reconnectable.start();
					log.info("��������������");
				}
			}
		}

		MessageReceiver msgRecv = new MessageReceiver();
		msgRecv.setJmsConfig(jmsConfig);
		msgRecv.setReceiveMessageService(receMsg);
		msgRecv.setDestination(destination);
		msgRecv.setCharset(charset);

		this.reconnectable.addMessageReceiver(msgRecv);

		try {
			msgRecv.init();
		} catch (ConnectionFaultException e) {
			e.printStackTrace();
		}
		log.info("��ʼ����Ŀ�ĵأ�[" + destination + "]");
		msgRecv.registerReceiveMessageService(destination, receMsg, charset);
	}

	public void registerReceiveMessageService(Destination destination, final ReceiveMessageService receMsg) {
		registerReceiveMessageService(destination, receMsg, getJmsConfig().getEncoding());
	}

	public void close() {
		log.info("�ر���������");
		if (this.reconnectable != null)
			this.reconnectable.setClose(true);
		if (this.messageSender != null)
			this.messageSender.close();
	}

	public void connect() {
	}

	public JmsConfig getJmsConfig() {
		return jmsConfig;
	}

	public void setJmsConfig(JmsConfig jmsConfig) {
		this.jmsConfig = jmsConfig;
	}

	public static final String	CONFIG_NAME							= "JMS";

	public static final String	PROVIDER							= "PROVIDER";

	public static final String	URL									= "URL";

	public static final String	PASSWORD							= "PASSWORD";

	public static final String	USERNAME							= "USERNAME";

	public static final String	DEFAULT_DESTATION_POST_NAME			= "DEFAULT_DESTATION_POST_NAME";

	public static final String	DEFAULT_DESTATION_POST_REALPATH		= "DEFAULT_DESTATION_POST_REALPATH";

	public static final String	DEFAULT_DESTATION_RECEIVE_NAME		= "DEFAULT_DESTATION_RECEIVE_NAME";

	public static final String	DEFAULT_DESTATION_RECEIVE_REALPATH	= "DEFAULT_DESTATION_RECEIVE_REALPATH";

	public static final String	DESTATIONS_POST_NAME				= "DESTATIONS_POST_NAME";

	public static final String	DESTATIONS_POST_REALPATH			= "DESTATIONS_POST_REALPATH";

	public static final String	DESTATIONS_RECEIVE_NAME				= "DESTATIONS_RECEIVE_NAME";

	public static final String	DESTATIONS_RECEIVE_REALPATH			= "DESTATIONS_RECEIVE_REALPATH";

	public static final String	RECONNECT_TIME						= "RECONNECT_TIME";

	public static final String	LOOKUPNAME							= "LOOKUPNAME";

	public static final String	DEBUG								= "DEBUG";

	public static final String	USE_JNDI							= "USE_JNDI";

	public static final String	CLASS_NAME							= "CLASS_NAME";

	public static final String	TCP_URL								= "TCP_URL";

}
