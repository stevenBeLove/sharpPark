package com.exception;

/**
 * 
 * ClassName: QTException <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2015年7月29日 下午4:03:34 <br/>
 * 
 * @author Lance.Wu
 * @version
 * @since JDK 1.6
 */
public class QTException extends Exception {

    /** 序号 */
    private static final long serialVersionUID = 1L;
    /** 流程码 */
    private int               respCode;
    /** 返回内容 */
    private String            respMsg;
    /** 返回码 */
    private String            errCode;

    /**
     * 
     * Creates a new instance of QTException. 
     *
     */
    public QTException() {
        this.respCode = 1;
        this.respMsg = "系统未知异常";
        this.errCode = "99";
    }
    
    /**
     * 
     * 【方法名】    : (这里用一句话描述这个方法的作用). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2016年11月10日 下午5:10:47 .<br/>
     * 【参数】： .<br/>
     * @param respMsg .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人: yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public QTException(String respMsg) {
        this.respCode = 1;
        this.respMsg = respMsg;
        this.errCode = "99";
    }

    /**
     * 
     * Creates a new instance of QTException. 
     * 
     * @param errCode errCode
     * @param respMsg respMsg
     */
    public QTException(String errCode, String respMsg) {
        super(respMsg);
        this.errCode = errCode;
        this.respMsg = respMsg;
    }

    /**
     * 
     * getRespCode:(得到返回码). <br/> 
     * @author Lance.Wu
     * @return
     * @return 返回结果：int
     * @since JDK 1.6 qtservices 1.0
     */
    public int getRespCode() {
        return respCode;
    }

    /**
     * 
     * setRespCode:(获取返回码). <br/> 
     * 
     * @author Lance.Wu
     * @param respCode respCode
     * @since JDK 1.6 qtservices 1.0
     */
    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    /**
     * 
     * getRespCode:(得到返回码). <br/> 
     * @author Lance.Wu
     * @return
     * @return 返回结果：int
     * @since JDK 1.6 qtservices 1.0
     */
    public String getRespMsg() {
        return respMsg;
    }

    /**
     * 
     * setRespCode:(获取返回码). <br/> 
     * 
     * @author Lance.Wu
     * @param respMsg respMsg
     * @since JDK 1.6 qtservices 1.0
     */
    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    /**
     * 
     * getRespCode:(得到返回码). <br/> 
     * @author Lance.Wu
     * @return
     * @return 返回结果：int
     * @since JDK 1.6 qtservices 1.0
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * 
     * setRespCode:(获取返回码). <br/> 
     * 
     * @author Lance.Wu
     * @param errCode errCode
     * @since JDK 1.6 qtservices 1.0
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
