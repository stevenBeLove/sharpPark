package cn.ardu.jms.ems.msg;



/**
 * 
 * ��Ϣ������
 * 
 * ������ĵ�ַ����Ϣ����ʱ�ᴥ���˷���
 * 
 * @author teamsun.com.cn
 * 
 */
public interface ReceiveMessageService {
	
	

	/**
	 * 
	 * @param mail
	 *            �������Ϣ
	 * @return ����true��ʾ����Ϣ������� ����false����Ϣ�ᱻ�������˹�������Ϣ���͹���ʵ�֣�Ŀǰֻʹ����JMS���������HTTP�������Դ˷���ֵ����Ϣ���ᱻ������
	 */
	public boolean receiveMessage(Mail mail);

}
