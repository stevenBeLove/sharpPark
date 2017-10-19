/** 
 * 包名: package com.compass.utils; <br/> 
 * 添加时间: 2016年4月14日 下午3:28:34 <br/> 
 */
package com.compass.utils;

import java.io.Serializable;

/**
 * 类名: BaseModel <br/>
 * 作用：TODO(简单一句话描述)<br/>
 * 方法：TODO(简单描述方法)<br/>
 * 创建者: Administrator. <br/>
 * 添加时间: 2016年4月14日 下午3:28:34 <br/>
 * 版本： JDK 1.6 qtfr 1.0
 */
public class BaseModel implements Serializable {

	/**
	 * 
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 当前行数
	 */
	private String rows;
	/**
	 * 当前页数
	 */
	private String page;
	/**
	 * 查询页数
	 */
	private int pagenumber;
	/**
	 * 查询行数
	 */
	private int rownumber;
	/**
	 * 开始行数
	 */
	private int start;
	/**
	 * 结束行数
	 */
	private int end;

	
	
	
	
	/**
	 * 方法名： getRows.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getRows() {
		return rows;
	}

	/**
	 * 方法名： setRows.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 参数： @param rows 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * 方法名： getPage.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public String getPage() {
		return page;
	}

	/**
	 * 方法名： setPage.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 参数： @param page 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * 方法名： getPagenumber.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public int getPagenumber() {
		return Integer.parseInt((page == null) || (page == "0") ? "1" : page);
	}

	/**
	 * 方法名： setPagenumber.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 参数： @param pagenumber 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setPagenumber(int pagenumber) {
		this.pagenumber = pagenumber;
	}

	/**
	 * 方法名： getRownumber.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public int getRownumber() {
		return Integer.parseInt((rows == null) || (rows == "0") ? "20" : rows);
	}

	/**
	 * 方法名： setRownumber.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 参数： @param rownumber 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}

	/**
	 * 方法名： getStart.<br/>

	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 方法名： setStart.<br/>
	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 参数： @param start 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 方法名： getEnd.<br/>
	 * 执行流程:.<br/>
	 * 注意事项:.<br/>
	 * 方法作用:.<br/>
	 *
	 * 返回值：@return 返回值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * 方法名： setEnd.<br/>
	 * 方法作用:.<br/>
	 * 参数： @param end 设置值
	 *
	 * 创建者：Ferry Chen<br/>
	 * 创建日期：2016年4月14日.<br/>
	 * 创建时间：下午3:42:41.<br/>
	 * 其它内容： JDK 1.6 qtfr 1.0.<br/>
	 */
	public void setEnd(int end) {
		this.end = end;
	}

}
