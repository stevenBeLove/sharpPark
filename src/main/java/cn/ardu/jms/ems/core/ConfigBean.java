package cn.ardu.jms.ems.core;


/**
 * 配置文件
 * 
 * @author teamsun.com.cn
 * 
 */
public class ConfigBean {

	private String	encoding;

	private String	config;

	private boolean	debug;

	/**
	  * 字符集编码
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 字符集编码
	 * 
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	/**
	* 是否处于调试模式
	 * 
	 * 处于调试模式下日志信息更丰富一些
	 * 
	 * @return
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * 设置调试模式
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
