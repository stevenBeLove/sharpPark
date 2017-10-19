package cn.ardu.jms.ems.msg;


import java.util.Map;

import cn.ardu.jms.ems.msg.Destination.DestinationType;


/**
 * ��Ϣ�������������͡�������Ϣ�����Ľӿڡ�
 * 
 * <p>
 * ��Ϣ���ͼ����վ�������ַ��ء�������Ϣ�����Ϊ���ĵ�ַ����mail��������Ϊ�����ĵ�ַ��������Ϣ����򴥷������¼��� �˽ӿڻ��ṩ��ַ��Ϣ�Ĳ��������ڸ�URL����Ϣ���������ԣ�ֻӦ��ʹ��һ��ʵ��
 * </p>
 * 
 * �û�����ʵ�ִ˽ӿڣ�ͨ��MessageConnectionFactory���ش˽ӿ�ʵ��
 * 
 * @author teamsun.com.cn
 * @see gov.chinapost.ems.msg.adapter.core.MessageConnectionFactory
 * @see gov.chinapost.ems.msg.adapter.PostMessageFaultService
 * @see gov.chinapost.ems.msg.adapter.ReceiveMessageService
 */
public interface MessageConnection {

	/**
	 * ��Ĭ�ϵ�ַ������Ϣ��
	 * 
	 * @param mail
	 *            ��Ϣ
	 */
	void postMessage(Mail mail);

	/**
	 * �����ַ������Ϣ
	 * 
	 * @param destation
	 *            ���ַ
	 * @param mail
	 *            ��Ϣ
	 */
	void postMessage(Destination destation, Mail mail);

	/**
	 * ��Ĭ�ϵ�ַ������Ϣ
	 * 
	 * @param postMsg
	 */
	void postMessage(PostMessageService postMsg);

	/**
	 * �����ַ������Ϣ
	 * 
	 * @param destation
	 * @param postMsg
	 */
	void postMessage(Destination destation, PostMessageService postMsg);

	/**
	 * ΪĬ�Ͻ��յ�ַע����Ϣ������
	 * 
	 * @param receMsg
	 * @see gov.chinapost.ems.msg.adapter.ReceiveMessageService
	 */
	void registerReceiveMessageService(ReceiveMessageService receMsg);

	/**
	 * Ϊ��Ľ��յ�ַע����Ϣ������
	 * 
	 * @param destation
	 * @param receMsg
	 * @see gov.chinapost.ems.msg.adapter.ReceiveMessageService
	 */
	void registerReceiveMessageService(Destination destation, ReceiveMessageService receMsg);

	/**
	 * Ϊ��Ľ��յ�ַע����Ϣ������ ��Ϣ����Ϊcharset
	 * 
	 * ���ǵ����ֶ��еı��뷽ʽ���ܲ�ͬ�������ļ��б����ķ�ʽ�������Ӵ˷���
	 * 
	 * @param destation
	 * @param receMsg
	 * @param charset
	 *            �ַ����
	 */
	void registerReceiveMessageService(Destination destation, ReceiveMessageService receMsg, String charset);

	/**
	 * ע�ᷢ��ʧ����Ϣ�Ĵ��������˴������������з���ʧ�ܵ���Ϣ��
	 * 
	 * @param faultService
	 * @param destation
	 * @see gov.chinapost.ems.msg.adapter.PostMessageFaultService
	 */
	void registerPostMessageFaultService(PostMessageFaultService faultService);

	/**
	 * ע���ַ
	 * 
	 * @param destation
	 * @return
	 */
	boolean registerDestination(Destination destation);

	/**
	 * ��ȡ���и��ַ���ĵ�ַ��Ϣ
	 * 
	 * @param destationType
	 * @return
	 */
	Map<String, Destination> getDestinations(DestinationType destationType);

	/**
	 * ����ƻ�ȡ��ַ
	 * 
	 * @param destationName
	 * @return
	 */
	Destination getDestinationByName(String destationName);

	/**
	 * ��ȡĬ�ϵĵ�ַ
	 * 
	 * @param destationType
	 * @return
	 */
	Destination getDefaultDestination(DestinationType destationType);

	/**
	 * ����Ĭ�ϵ�ַ
	 * 
	 * @param destation
	 */
	void setDefaultDestination(Destination destation);

	/**
	 * �ر�
	 */
	void close();

}
