package cn.ardu.jms.ems.impl;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.ConnectionFaultException;

/**
 * 连接器异常监听
 * 
 * 当连接发生异常或者信息发送失败时，此线程自动激活并尝试重连。
 * 
 * @author teamsun.com.cn
 * 
 */
public class ExceptionListenerThread extends Thread {

	private AbstractMessageHandler abstractMessageHandler;
	
	static Logger log = Logger.getLogger(ExceptionListenerThread.class);//��־����

	public void run() {
		log.info("start re-connect....");
		this.abstractMessageHandler.close();
		while (true) {
			try {
				Thread.sleep(abstractMessageHandler.getJmsConfig()
						.getReconnectTime());
			} catch (InterruptedException e) {
			}
			try {
				abstractMessageHandler.init();
			} catch (ConnectionFaultException e) {
				e.printStackTrace();
			}
			if (abstractMessageHandler.getConnection() != null) {
				break;
			}
		}

	}

	public AbstractMessageHandler getAbstractMessageHandler() {
		return abstractMessageHandler;
	}

	public void setAbstractMessageHandler(
			AbstractMessageHandler abstractMessageHandler) {
		this.abstractMessageHandler = abstractMessageHandler;
	}

}
