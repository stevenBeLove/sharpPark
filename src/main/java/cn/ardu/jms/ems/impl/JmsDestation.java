package cn.ardu.jms.ems.impl;

import cn.ardu.jms.ems.msg.Destination;


/**
 * 
 * @author teamsun.com.cn
 * 
 */
public class JmsDestation extends Destination {

	private boolean	topic;

	public boolean isTopic() {
		return topic;
	}

	public void setTopic(boolean topic) {
		this.topic = topic;
	}

}
