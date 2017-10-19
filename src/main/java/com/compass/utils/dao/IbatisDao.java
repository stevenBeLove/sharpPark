package com.compass.utils.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author wangLong
 * 基于IBatis支持的Dao
 */
@SuppressWarnings("rawtypes")
public class IbatisDao extends SqlMapClientDaoSupport implements Dao {
  private int batchSize = 100;// 默认100,可以根据数据的特性调整此值(spring配置文件内)

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public Object queryForObject(String sql) {
    Assert.hasText(sql);
    return this.getSqlMapClientTemplate().queryForObject(sql);
  }

  public Object queryForObject(String sql, Object parameters) {
    Assert.hasText(sql);
    if (parameters == null) {
      return this.getSqlMapClientTemplate().queryForObject(sql);
    } else {
      return this.getSqlMapClientTemplate().queryForObject(sql, parameters);
    }
  }

  public Map queryForMap(String sql) {
    Assert.hasText(sql);
    return (Map) this.getSqlMapClientTemplate().queryForObject(sql);
  }

  public Map queryForMap(String sql, Object parameters) {
    Assert.hasText(sql);
    if (parameters == null) {
      return (Map) this.getSqlMapClientTemplate().queryForObject(sql);
    } else {
      return (Map) this.getSqlMapClientTemplate().queryForObject(sql, parameters);
    }
  }

  public List queryForList(String sql) {
    Assert.hasText(sql);
    return this.getSqlMapClientTemplate().queryForList(sql);
  }

  public List queryForList(String sql, Object parameters) {
    Assert.hasText(sql);
    if (parameters == null) {
      return this.getSqlMapClientTemplate().queryForList(sql);
    } else {
      return this.getSqlMapClientTemplate().queryForList(sql, parameters);
    }
  }

  public int insert(String sql, Object parameters) {
    return update(sql, parameters);
  }

  public int delete(String sql, Object parameters) {
    Assert.hasText(sql);
    if (parameters == null) {
      return this.getSqlMapClientTemplate().delete(sql);
    } else {
      return this.getSqlMapClientTemplate().delete(sql, parameters);
    }
  }

  public int update(String sql, Object parameters) {
    Assert.hasText(sql);
    if (parameters == null) {
      return this.getSqlMapClientTemplate().update(sql);
    } else {
      return this.getSqlMapClientTemplate().update(sql, parameters);
    }
  }

  public int execute(String sql, Object parameters) {
    return this.update(sql, parameters);
  }

  public int execute(String sql) {
    return this.update(sql, null);
  }

  @SuppressWarnings("unchecked")
public int batchUpdate(final String sql, final Collection<?> values) {
    if (values == null || values.size() == 0) {
      return 0;// 空集合不予处理
    }
    return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
        executor.startBatch();
        int i = 0, result = 0;
        for (Object value : values) {
          executor.update(sql, value);
          if (i++ > batchSize) {
            result += executor.executeBatch();
            i = 0;
          }
        }
        result += executor.executeBatch();
        return result;
      }
    });
  }
}
