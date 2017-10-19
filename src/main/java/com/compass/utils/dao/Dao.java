package com.compass.utils.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author wangLong
 * DAO 接口</p>
 * 提供数据存取能力
 */
@SuppressWarnings("rawtypes")
public interface Dao {
  /**
   * 查询对象
   *
   * @param sql sql语句或编号
   * @return 对象
   */
  Object queryForObject(String sql);

  /**
   * 查询对象
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时使用Map
   * @return 对象
   */
  Object queryForObject(String sql, Object parameters);

  /**
   * 查询对象
   *
   * @param sql sql语句或编号
   * @return 对象
   */

Map queryForMap(String sql);

  /**
   * 查询对象
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时使用Map
   * @return 对象
   */
  Map queryForMap(String sql, Object parameters);

  /**
   * 查询列表
   *
   * @param sql sql语句或编号
   * @return 查询结果列表
   */
  List queryForList(String sql);

  /**
   * 查询列表
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时使用Map
   * @return 查询结果列表
   */
  List queryForList(String sql, Object parameters);

  
  /**
   * 插入记录
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时使用Map
   * @return 影响的记录条数
   */
  int insert(String sql, Object parameters);

  /**
   * 删除记录
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时使用Map
   * @return 影响的记录条数
   */
  int delete(String sql, Object parameters);

  /**
   * 更新记录
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时可使用Map
   * @return 影响的记录条数
   */
  int update(String sql, Object parameters);

  /**
   * 执行SQL语句
   * <p/>
   * 通用的执行变更sql语句方法(可以是insert,update,delete)
   *
   * @param sql        sql语句或编号
   * @param parameters 参数对象，多参数时可使用Map
   * @return 影响的记录条数
   */
  int execute(String sql, Object parameters);

  /**
   * 执行SQL语句
   * <p/>
   * 通用的执行变更sql语句方法(可以是insert,update,delete)
   *
   * @param sql sql语句或编号
   * @return 影响的记录条数
   */
  int execute(String sql);

   /**
    * 批量保存数据
    *
    * @param sql    sql语句
    * @param values 批量参数
    * @return 此次批量影响的数据
    */
   int batchUpdate(String sql, Collection<?> values);

}
