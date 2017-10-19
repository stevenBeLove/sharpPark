package cn.ardu.jms.ems.impl;

import cn.ardu.jms.ems.core.ConfigBean;

/**
 * 配置信息
 * 
 * @author teamsun.com.cn
 * 
 */
public class JmsConfig extends ConfigBean implements Cloneable {

	private String tcpUrl;

	private String className;

	private boolean useJndi;

	private String provider;

	private String url;

	private String username;

	private String password;

	private String lookupName;

	private int reconnectTime;

	/**
	 * Ĭ�ϵķ��͵�ַ :ǰΪ�Ѻ���ƣ�����ΪĿ�ĵ�����
	 */
	private String default_destation_post_name = "";

	public String getDefault_destation_post_name() {
		return default_destation_post_name;
	}

	public void setDefault_destation_post_name(String defaultDestationPostName) {
		default_destation_post_name = defaultDestationPostName;
	}

	public String getDefault_destation_post_realpath() {
		return default_destation_post_realpath;
	}

	public void setDefault_destation_post_realpath(
			String defaultDestationPostRealpath) {
		default_destation_post_realpath = defaultDestationPostRealpath;
	}

	public String getDefault_destation_receive_name() {
		return default_destation_receive_name;
	}

	public void setDefault_destation_receive_name(
			String defaultDestationReceiveName) {
		default_destation_receive_name = defaultDestationReceiveName;
	}

	public String getDefault_destation_receive_realpath() {
		return default_destation_receive_realpath;
	}

	public void setDefault_destation_receive_realpath(
			String defaultDestationReceiveRealpath) {
		default_destation_receive_realpath = defaultDestationReceiveRealpath;
	}

	/**
	 * Ĭ�Ϸ��͵�ַ���Ѻ���ƶ�Ӧ��ʵ�ʵ�ַ
	 */
	private String default_destation_post_realpath = "";

	/**
	 * Ĭ�ϵĽ��յ�ַ :ǰΪ�Ѻ���� ����ΪĿ�ĵ�����
	 */
	private String default_destation_receive_name = "";

	/**
	 * ��Ĭ�ϵĽ��յ�ַ��Ӧ��ʵ�ʵ�ַ �˴���Ҫ��дESBʵ�ʵĶ������
	 */
	private String default_destation_receive_realpath = "";

	/**
	 * ���͵�ַ�� ǰΪ�Ѻ���ƣ�����ΪĿ�ĵ�����
	 */
	private String destations_post_name = "";

	/**
	 * ���͵�ַ��Ӧ��ʵ�ʵ�ַ
	 */
	private String destations_post_realpath = "";

	/**
	 * ���յ�ַ��ǰΪ�Ѻ���� ����ΪĿ�ĵ�����
	 */
	private String destations_receive_name = "";

	/**
	 * ���յ�ַ��Ӧ��ʵ�ʵ�ַ
	 */
	private String destations_receive_realpath = "";

	public String getDestations_receive_namesOfSplit(String regex, int index) {
		return destations_receive_name.split(regex)[index];
	}

	public String getDestations_receive_realpathsOfSplit(String regex, int index) {
		return destations_receive_realpath.split(regex)[index];
	}

	/**
	 * ��ȡJMS��Ӧ��
	 * 
	 * @return
	 */
	public String getProvider() {
		return provider;
	}

	public String getDestations_post_name() {
		return destations_post_name;
	}

	public void setDestations_post_name(String destationsPostName) {
		destations_post_name = destationsPostName;
	}

	public String getDestations_post_realpath() {
		return destations_post_realpath;
	}

	public void setDestations_post_realpath(String destationsPostRealpath) {
		destations_post_realpath = destationsPostRealpath;
	}

	public String getDestations_receive_name() {
		return destations_receive_name;
	}

	public void setDestations_receive_name(String destationsReceiveName) {
		destations_receive_name = destationsReceiveName;
	}

	public String getDestations_receive_realpath() {
		return destations_receive_realpath;
	}

	public void setDestations_receive_realpath(String destationsReceiveRealpath) {
		destations_receive_realpath = destationsReceiveRealpath;
	}

	/**
	 * ����JMS��Ӧ��
	 * 
	 * @param provider
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * JMS URL
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * JMS URL
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * �û���
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * �û���
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * ����
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * ����
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * JNDI ������
	 * 
	 * @return
	 */
	public String getLookupName() {
		return lookupName;
	}

	/**
	 * JNDI ������
	 * 
	 * @param lookupName
	 */
	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	/**
	 * �������Ӽ��ʱ��
	 * 
	 * @return
	 */
	public int getReconnectTime() {
		return reconnectTime;
	}

	/**
	 * �������Ӽ��ʱ��
	 * 
	 * @param reconnectTime
	 */
	public void setReconnectTime(int reconnectTime) {
		this.reconnectTime = reconnectTime;
	}

	public String getTcpUrl() {
		return tcpUrl;
	}

	public void setTcpUrl(String tcpUrl) {
		this.tcpUrl = tcpUrl;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isUseJndi() {
		return useJndi;
	}

	public void setUseJndi(boolean useJndi) {
		this.useJndi = useJndi;
	}

	public JmsConfig clone() {
		try {
			return (JmsConfig) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
