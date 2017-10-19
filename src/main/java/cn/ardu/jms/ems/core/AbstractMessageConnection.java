package cn.ardu.jms.ems.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.ardu.jms.ems.msg.Destination;
import cn.ardu.jms.ems.msg.MessageConnection;
import cn.ardu.jms.ems.msg.Destination.DestinationType;


/**
 * 抽象的消息联机器实现。实现了地址操作方法。
 * 
 * @author teamsun.com.cn
 * 
 */
abstract public class AbstractMessageConnection implements MessageConnection {


	private Map<String, Destination>	destations	= new HashMap<String, Destination>();
	
	static Logger log = Logger.getLogger(AbstractMessageConnection.class);//��־����

	public boolean registerDestination(Destination destation) {
		synchronized (destations) {
			if (this.destations.containsKey(destation.getDestationName()))
				return false;
			else {
				this.destations.put(destation.getDestationName(), destation);
				return true;
			}
		}

	}

	public Destination getDefaultDestination(DestinationType destationType) {
		for (Destination destation : this.destations.values()) {
			if (destation.getDestationType() == destationType && destation.isDefaultDestation())
				return destation;
		}
		return null;
	}

	public Destination getDestinationByName(String destationName) {
		return this.destations.get(destationName);
	}

	public Map<String, Destination> getDestinations(DestinationType destationType) {
		Map<String, Destination> maps = new HashMap<String, Destination>();
		synchronized (destations) {
			for (Destination destation : this.destations.values()) {
				if (destation.getDestationType() == destationType) {
					maps.put(destation.getDestationName(), destation);
				}
			}
		}
		return maps;
	}

	/**
	 * 显示所有地址信息
	 */
	public void showAllDestations() {
		for (Destination destation : this.destations.values()) {
			log.debug("地址信息：" + destation);
		}
	}

	/**
	 * 设置默认地ַ
	 */
	public void setDefaultDestination(Destination destation) {
		registerDestination(destation);
		synchronized (destations) {
			if (this.getDefaultDestination(destation.getDestationType()) == null
					&& this.destations.containsKey(destation.getDestationName())) {
				try {
					Field field = Destination.class.getDeclaredField("defaultDestation");
					field.setAccessible(true);
					field.set(destation, true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
