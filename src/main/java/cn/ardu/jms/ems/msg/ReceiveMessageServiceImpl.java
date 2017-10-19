package cn.ardu.jms.ems.msg;

import org.apache.log4j.Logger;


public class ReceiveMessageServiceImpl implements ReceiveMessageService {
	
	static Logger log = Logger.getLogger(ReceiveMessageServiceImpl.class);//��־����

	private Mail mail;

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public boolean receiveMessage(Mail mail) {
		this.mail = mail;
		log.info("������Ϣ=========>" + mail.getMailText());
		return true;
	}

}
