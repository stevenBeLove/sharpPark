package cn.ardu.jms.ems.impl;



import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.ConnectionFaultException;
import cn.ardu.jms.ems.msg.Destination;
import cn.ardu.jms.ems.msg.Mail;
import cn.ardu.jms.ems.msg.MessageConnectionFactory;
import cn.ardu.jms.ems.msg.PostMessageFaultService;

/**
 * 消息发送器
 * 
 * @author teamsun.com.cn
 * 
 */
public class MessageSender extends AbstractMessageHandler {

	private PostMessageFaultService	postMessageFaultService;

	private ExceptionListenerThread	elt;

	static Logger log = Logger.getLogger(MessageConnectionFactory.class);//��־����
	
	/**
	 * 将Mail转换为javax.jms.Message进行传送，此处使用其中的javax.jms.BytesMessage实现。
	 * 
	 * @param destation
	 * @param mail
	 */
	public void postMessage(Destination destination, Mail mail) {
		javax.jms.Session session = null;
		MessageProducer mp = null;
		try {
			if (getJmsConfig().isDebug())
				log.info("connectoin:" + getConnection() + " " + this);
			session = this.getConnection().createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			javax.jms.BytesMessage bm = session.createBytesMessage();

			for (String name : mail.getStringPropertyNames()) {
				if (name.equals(Mail.MAIL_ENCODING))
					continue;
				bm.setStringProperty(name, mail.getStringProperty(name));
			}
			if(mail.getStringProperty("JMSCorrelationID")!=null){ // ������������Ϣcorrelationidֵ�������ø�ֵ��Ϊ������ϢΨһ��ֵ
				bm.setJMSCorrelationID(mail.getStringProperty("JMSCorrelationID"));
			}

			String encoding = mail.getStringProperty(Mail.MAIL_ENCODING);
		    if (encoding == null) {
				encoding = getJmsConfig().getEncoding();
				log.info("信息体编码使用配置编码  Encoding=" + encoding);
				bm.writeBytes(mail.getMailText().getBytes(encoding));
			} else if (encoding.equals("")) {
				log.info("信息体编码使用默认编码");
				bm.writeBytes(mail.getMailText().getBytes());
			} else {
				log.info("信息体编码 Encoding=" + encoding);
				bm.writeBytes(mail.getMailText().getBytes(encoding));
			}

			mp = session.createProducer(this.getJmsDestation(session, destination));
			mp.send(bm);

			if (getJmsConfig().isDebug())
				log.info("信息发送地址：" + destination + "\n信息发送成功:\n" + mail);
			else
				log.info("信息发送地址：" + destination + " 信息发送成功");
		} catch (JMSException e) {
			log.error("信息发送失败:" + e.getMessage() + " " + e.getLocalizedMessage());
			if (this.postMessageFaultService != null)
				this.postMessageFaultService.postMessageFault(destination, mail);
			startReconnect();
		} catch (Exception e) {
			log.error("信息发送失败 原因：" + e.getMessage());
			e.printStackTrace();
			if (this.postMessageFaultService != null)
				this.postMessageFaultService.postMessageFault(destination, mail);
		}finally {
			if (null != mp)
				try {
					mp.close();
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
		}
	}

	/**
	 * 设置连接失败的监听程序
	 */
	public void setMessageConnectExceptionListener() {
		try {
			if (this.getConnection() != null)
				this.getConnection().setExceptionListener(new ExceptionListener() {

					public void onException(JMSException jmsException) {
						log.info("JMS连接异常:" + jmsException.getMessage());
						if (!elt.isAlive())
							elt.start();

					}

				});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ConnectionFaultException {
		elt = new ExceptionListenerThread();
		elt.setAbstractMessageHandler(MessageSender.this);
		elt.setDaemon(true);

		this.connect();

		this.setMessageConnectExceptionListener();
	}

	@Override
	public void close() {
		if (this.getConnection() != null) {
			try {
				this.getConnection().close();
			} catch (JMSException e) {
				log.error("连接关闭异常");
			}
			this.setConnection(null);

		}
	}

	public void startReconnect() {
		if (getJmsConfig().isDebug())
			log.info("激活接收器监听机制，当无法正常接收时会重新建立连接");
		if (elt == null) {
			elt = new ExceptionListenerThread();
			elt.setAbstractMessageHandler(MessageSender.this);
			elt.setDaemon(true);
		}
		if (!elt.isAlive())
			elt.start();
	}

	public PostMessageFaultService getPostMessageFaultService() {
		return postMessageFaultService;
	}

	public void setPostMessageFaultService(PostMessageFaultService postMessageFaultService) {
		this.postMessageFaultService = postMessageFaultService;
	}

}
