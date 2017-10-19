package cn.ardu.jms.ems.msg;



/**
 * ������Ϣ����
 * 
 * ����Ϣ�޷����͵�ָ��Ŀ�ĵ�ʱ������ô˽ӿڡ�ע��<b>ֻ���������ͨѶ�������������ϵ��µ���Ϣ���ͲŻᴥ������Ϣ</b>
 * 
 * @author teamsun.com.cn
 * 
 */
public interface PostMessageFaultService {

	/**
	 * ʧ����Ϣ����
	 * 
	 * @param destation
	 *            ʧ����Ϣ���͵ĵ�ַ
	 * @param mail
	 *            ʧ�ܵ���Ϣ
	 */
	public void postMessageFault(Destination destation, Mail mail);

}
