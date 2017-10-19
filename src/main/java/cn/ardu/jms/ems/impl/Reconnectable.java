package cn.ardu.jms.ems.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.exception.ConnectionFaultException;



/**
 * 接收程序监听器
 * 
 * 当有接收程序发生失败时，此监听程序会自动修复
 * 
 * @author teamsun.com.cn
 * 
 */
public class Reconnectable extends Thread {

	private static final int SLEEP_TIME = 10000;

	private List<MessageReceiver> messageReceivers = new ArrayList<MessageReceiver>();
	
	
	static Logger log = Logger.getLogger(Reconnectable.class);//��־����

	public void setMessageReceivers(List<MessageReceiver> messageReceivers) {
		this.messageReceivers = messageReceivers;
	}

	private long delay;

	private boolean close = false;

	public void addMessageReceiver(MessageReceiver msgRecv) {
		this.messageReceivers.add(msgRecv);
	}

	@Override
	public void run() {

		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}

		while (!close) {
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage(), e1);
			}

			for (MessageReceiver messageReceiver : this.messageReceivers) {
				if (!messageReceiver.isStarted()) {
					try {
						messageReceiver.init();
						// for re-connect
						if (messageReceiver.getDestination() != null
								&& messageReceiver.getReceiveMessageService() != null)
							messageReceiver.registerReceiveMessageService(
									messageReceiver.getDestination(),
									messageReceiver.getReceiveMessageService(),
									messageReceiver.getCharset());
						messageReceiver.setStarted(true);
						log.info(messageReceiver.getDestination()
								.getDestationName()
								+ "-重建连接成功.");
					} catch (ConnectionFaultException e) {
						log.warn(messageReceiver.getDestination()
								.getDestationName()
								+ "-重建连接异常:" + e.getMessage());
					} catch (Exception e) {
						log.warn("重连失败 继续尝试");
					}

				}
			}

		}

		this.stopReceive();

	}

	public void stopReceive() {
		for (MessageReceiver messageReceiver : this.messageReceivers) {
			messageReceiver.close();
		}
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
}