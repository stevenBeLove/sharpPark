package cn.ardu.jms.ems.impl;

import java.util.Enumeration;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.ConnectionFaultException;
import cn.ardu.jms.ems.msg.Destination;
import cn.ardu.jms.ems.msg.Mail;
import cn.ardu.jms.ems.msg.ReceiveMessageService;


/**
 * 以jms方式实现的信息接收器
 * 
 * @author teamsun.com.cn
 * 
 */
public class MessageReceiver extends AbstractMessageHandler {

	private static final int		SLEEP_TIME	= 5000;

	private ReceiveMessageService	receiveMessageService;

	private Destination				destination;

	private Session					session;

	private MessageConsumer			msgConsumer;

	private boolean					started;

	private String					charset;
	
	public  Mail                    mail ; 
	
	static Logger log = Logger.getLogger(MessageReceiver.class);//��־����

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public void registerReceiveMessageService(Destination destation, final ReceiveMessageService receMsg,
			final String charset) {

		try {
			final javax.jms.Session session = this.getConnection().createSession(false, Session.CLIENT_ACKNOWLEDGE);
			this.session = session;
			javax.jms.MessageConsumer mc = session.createConsumer(getJmsDestation(session, destation));
			this.msgConsumer = mc;
			mc.setMessageListener(new MessageListener() {

				@SuppressWarnings("unchecked")
				public void onMessage(Message message) {
					try {
						if (message instanceof javax.jms.BytesMessage) {
							javax.jms.BytesMessage bm = (javax.jms.BytesMessage) message;
							byte[] bs = new byte[(int) bm.getBodyLength()];
							bm.readBytes(bs);
							Mail mail = new Mail();
							mail.setTimeStamp(message.getJMSTimestamp());

							for (Enumeration names = message.getPropertyNames(); names.hasMoreElements();) {
								String name = names.nextElement().toString();

								if (message.getStringProperty(name) != null) {
									mail.addStringProperty(name, message.getStringProperty(name));
								}
							}

							mail.setMailText(new String(bs, charset));
							if (receMsg.receiveMessage(mail)) {
								MessageReceiver.this .mail = mail;
								message.acknowledge();
							} else {
								Thread.sleep(SLEEP_TIME);
								session.recover();
							}
						} else {
							message.acknowledge();
						}
					} catch (JMSException e) {
						log.error("---" + e.getMessage());
						MessageReceiver.this.setStarted(false);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}

			});

			this.started = true;
			this.getConnection().start();
		} catch (JMSException e) {
			this.setStarted(false);
			log.error("目的地：" + destation + " 接收失败，等待重置\n" + e.getMessage());
		} finally {
		}

	}

	/**
	 * 客户接收程序
	 * 
	 * @return
	 */
	public ReceiveMessageService getReceiveMessageService() {
		return receiveMessageService;
	}

	/**
	 * 设置客户接收程序
	 * 
	 * @param receiveMessageService
	 */
	public void setReceiveMessageService(ReceiveMessageService receiveMessageService) {
		this.receiveMessageService = receiveMessageService;
	}

	/**
	 * 目的地
	 * 
	 * @return
	 */
	public Destination getDestination() {
		return destination;
	}

	/**
	 * 设置目的地
	 * 
	 * @param destation
	 */
	public void setDestination(Destination destation) {
		this.destination = destation;
	}

	/**
	 * 初始化
	 * 
	 * @throws ConnectionFaultException
	 */
	@Override
	public void init() throws ConnectionFaultException {
		this.connect();

		if (this.getConnection() != null) {
			try {
				this.getConnection().setExceptionListener(null);
				this.getConnection().setExceptionListener(new ExceptionListener() {

					public void onException(JMSException jmsException) {
						MessageReceiver.this.setStarted(false);
					}

				});
			} catch (JMSException e) {
				log.error("JMSException\t" + e.getMessage());
				throw new ConnectionFaultException();
			}

		}
	}

	/**
	 * 关闭
	 */
	@Override
	public void close() {

		if (this.msgConsumer != null) {
			try {
				this.msgConsumer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		if (this.session != null) {
			try {
				this.session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		if (this.getConnection() != null) {
			try {
				this.getConnection().stop();
				this.getConnection().close();
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 是否在运行中
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * 设置
	 * 
	 * @param started
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
