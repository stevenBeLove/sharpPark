package cn.ardu.jms.ems.msg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象消息。
 * 
 * 所有使用本api发送、接收的消息均抽象为此类型。
 * 
 * @author teamsun.com.cn
 * 
 */
public class Mail {

	/**
	 * 消息内容编码格式 默认为配置文件中使用的格式；用户可以自行设置 mail.addStringProperty(...)
	 */
	public static final String			MAIL_ENCODING		= "MAIL_CONTENT_ENCODING";

	private long						timeStamp;

	private String						mailText			= "";

	private String						bodyName			= "";

	private Map<String, PropertyPair>	customerProperties	= new HashMap<String, PropertyPair>();

	public Mail() {

	}

	/**
	 * 增加字符串属性.<br>
	 * 
	 * @param name
	 * @param value
	 */
	public void addStringProperty(String name, String value) {
		PropertyPair pair = new PropertyPair();
		pair.setName(name);
		pair.setValue(value);

		this.customerProperties.put(name, pair);
	}

	/**
	 * 得到所有字符串属性名称的列表
	 * 
	 * @return
	 */
	public List<String> getStringPropertyNames() {
		List<String> list = new ArrayList<String>();

		for (PropertyPair pair : this.customerProperties.values()) {
			if (pair.getValue() instanceof String) {
				list.add(pair.getName());
			}
		}

		return list;
	}

	/**
	 * 根据名字得到字符串属性
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public String getStringProperty(String name) {
		PropertyPair pair = this.customerProperties.get(name);

		if (pair != null && pair.getValue() instanceof String) {
			return (String) pair.getValue();
		}

		return null;
	}

	public Mail(String text) {
		this.mailText = text;
	}

	public String getMailText() {
		return mailText;
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * 内部用于描述属性对的类
	 * 
	 * @author teamsun.com.cn
	 * 
	 */
	private static class PropertyPair {

		private String	name;

		private Object	value;

		/**
		 * 名字
		 * 
		 * @return
		 */
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 值
		 * 
		 * 
		 * @return
		 */
		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("属性名：" + name + " 属性值：" + value + "\n");
			return buffer.toString();
		}

	}

	public String getBodyName() {
		return bodyName;
	}

	public void setBodyName(String bodyName) {
		this.bodyName = bodyName;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("------- 信息内容 -------\n");
		buffer.append("  消息名称" + this.getBodyName() + "\n");
		buffer.append("  消息体：" + this.getMailText() + "\n");
		if (this.customerProperties.size() > 0) {
			buffer.append("  属性信息:\n");
			for (String name : this.customerProperties.keySet()) {
				buffer.append("  " + this.customerProperties.get(name));
			}
		} else {
			buffer.append("属性信息:空");
		}
		return buffer.toString();
	}
}
