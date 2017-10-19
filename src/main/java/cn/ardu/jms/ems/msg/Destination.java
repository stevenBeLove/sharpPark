package cn.ardu.jms.ems.msg;

/**
 * 
 * ��ϢĿ�ĵ�
 * 
 * <p>
 * �����ֵ�ַ��Ϣ�����͵�ַ�����յ�ַ������<code>getDestationName()</code>Ϊ�Ѻ���ƣ�����ʹ�á�
 * <code>getDestationRealPath()</code>Ϊ��ַʵ����ơ� �˶����л����Ƿ�ΪĬ�ϵ�ַ����ַ����(���ͻ��߽���)
 * ����ϢӦ�������ļ��ṩ���������ڳ��������á�
 * </p>
 * 
 * @see gov.chinapost.ems.msg.adapter.Destination
 * @author teamsun.com.cn
 */
public class Destination {

	/**
	 * ��ַ������Ϣ
	 * 
	 * @author teamsun.com.cn
	 * 
	 */
	static public enum DestinationType {
		DESTINATION_POST, DESTINATION_RECEIVE
	}

	private String destationName;

	private String destationRealPath;

	private DestinationType destationType;

	private boolean defaultDestation;

	/**
	 * ������
	 */
	public Destination() {
	}

	/**
	 * ������
	 * 
	 * @param destationType
	 *            ��ַ����
	 */
	public Destination(DestinationType destationType) {
		super();
		this.destationType = destationType;
	}

	/**
	 * ������
	 * 
	 * @param destationName
	 *            �Ѻ����
	 * @param destationRealPath
	 *            ʵ�ʵ�ַ
	 * @param destationType
	 *            ��ַ���
	 */
	public Destination(String destationName, String destationRealPath,
			DestinationType destationType) {
		super();
		this.destationName = destationName;
		this.destationRealPath = destationRealPath;
		this.destationType = destationType;
	}

	/**
	 * ��ȡ�Ѻ����
	 * 
	 * @return �Ѻ����
	 */
	public String getDestationName() {
		return destationName;
	}

	/**
	 * �����Ѻ����
	 * 
	 * @param destationName
	 */
	public void setDestationName(String destationName) {
		this.destationName = destationName;
	}

	/**
	 * ��ȡʵ�ʵ�ַ
	 * 
	 * @return
	 */
	public String getDestationRealPath() {
		return destationRealPath;
	}

	/**
	 * ����ʵ�ʵ�ַ
	 * 
	 * @param destationRealPath
	 */
	public void setDestationRealPath(String destationRealPath) {
		this.destationRealPath = destationRealPath;
	}

	/**
	 * ��ȡ��ַ���
	 * 
	 * @return
	 */
	public DestinationType getDestationType() {
		return destationType;
	}

	/**
	 * ���õ�ַ���
	 * 
	 * @param destationType
	 */
	public void setDestationType(DestinationType destationType) {
		this.destationType = destationType;
	}

	/**
	 * �Ƿ�Ĭ�ϵ�ַ
	 * 
	 * @return ��ַ���
	 */
	public boolean isDefaultDestation() {
		return defaultDestation;
	}

	@Override
	public String toString() {
		return "�Ѻ����[" + this.destationName + "] ʵ�ʵ�ַ��["
				+ this.getDestationRealPath() + "] �շ����[" + this.destationType
				+ "] �Ƿ�Ĭ��:[" + this.isDefaultDestation() + "]";
	}

}
