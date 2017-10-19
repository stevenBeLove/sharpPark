/**
 * 
 */
package com.compass.authority.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.authority.model.AuthorityBean;
import com.compass.authority.model.ParamBean;
import com.compass.utils.AbstractService;

/**
 * @author wangYuChao
 * 
 */
public class AuthorityService extends AbstractService {
    /**
     * 获取菜单编号
     * 
     * @return
     */
    public Integer getMenuId() {
        return (Integer) dao.queryForObject("SEQUENCE.getMenuId");
    }

    /**
     * 根据条件查询菜单
     * 
     * @param powerName
     * @param status
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AuthorityBean> getAuthority(String powerName, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("menuName", powerName);
        map.put("menuStatus", status);
        return dao.queryForList("authority.getAuthority", map);
    }

    /**
     * 添加菜单方法
     * 
     * @param authorityBean
     * @return
     */
    public int add(AuthorityBean authorityBean) {
        return dao.insert("authority.addAuthority", authorityBean);
    }

    /**
     * 修改菜单方法
     * 
     * @param authorityBean
     * @return
     */
    public int update(AuthorityBean authorityBean) {
        return dao.update("authority.updateAuthority", authorityBean);
    }

    /**
     * 删除菜单方法
     * 
     * @param menuId
     * @return
     */
    public int delete(String menuId) {
        return dao.update("authority.deleteAuthority", menuId);
    }

    /**
     * 获得所有菜单，生成树使用
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AuthorityBean> getAuTree() {
        return dao.queryForList("authority.getAuthorityTree");
    }

    /**
     * 获得所有根菜单的直接子菜单
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AuthorityBean> getParentMenu() {
        return dao.queryForList("authority.getParentMenu");
    }

    /**
     * 
     * 方法名： getParamCount.<br/>
     * 方法作用:查询数量.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:17:55.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@param paramName
     * 参数者异常：@return .<br/>
     * 返回值： Integer.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public Integer getParamCount(String paramId, String paramName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("menuName", paramId);
        map.put("menuStatus", paramName);
        return (Integer) dao.queryForObject("authority.getParamCount", map);
    }

    /**
     * 
     * 方法名： getParam.<br/>
     * 方法作用:查询参数.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:16:30.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@param paramName
     * 参数者异常：@param start
     * 参数者异常：@param end
     * 参数者异常：@return .<br/>
     * 返回值： List<ParamBean>.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public List<ParamBean> getParam(String paramId, String paramName, int start, int end) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paramId", paramId);
        map.put("paramName", paramName);
        map.put("start", start);
        map.put("end", end);
        return dao.queryForList("authority.getParam", map);
    }

    /**
     * 方法名： addParam.<br/>
     * 方法作用:插入数据.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:16:36.<br/>
     * 参数者异常：@param paramBean
     * 参数者异常：@return .<br/>
     * 返回值： ：int.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public int addParam(ParamBean paramBean) {
        return dao.insert("authority.addParam", paramBean);
    }

    /**
     * 方法名： updateParam.<br/>
     * 方法作用:更新参数.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:16:39.<br/>
     * 参数者异常：@param paramBean
     * 参数者异常：@return .<br/>
     * 返回值： ：int.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public int updateParam(ParamBean paramBean) {
        return dao.update("authority.updateParam", paramBean);
    }

    /**
     * 
     * 方法名： deleteParam.<br/>
     * 方法作用:删除参数.<br/>
     *
     * 创建者：qiaoweizhen.<br/>
     * 创建日期：2016年3月17日.<br/>
     * 创建时间：下午4:16:43.<br/>
     * 参数者异常：@param paramId
     * 参数者异常：@return .<br/>
     * 返回值： ：int.<br/>
     * 其它内容： JDK 1.6 qtfr 1.0.<br/>
     */
    public int deleteParam(String paramId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paramId", paramId);
        return dao.delete("authority.deleteParam", map);
    }
    
    
    public String getParamObject(String paramId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paramId", paramId);
        ParamBean pb=  (ParamBean) dao.queryForObject("authority.getParamObject", map);
        return pb.getParamValue();
    }
    
    
    

}
