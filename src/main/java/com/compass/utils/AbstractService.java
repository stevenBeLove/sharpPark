package com.compass.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compass.utils.dao.Dao;

/**
 * @author wangLong
 * 基础Serivce
 */
public abstract class AbstractService {
  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  protected Dao dao;  //数据访问接口
  

  public void setDao(Dao dao) {
    this.dao = dao;
  }
}
