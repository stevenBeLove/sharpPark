package com.compass.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 * service factory
 * <p/>
 * 注意:
 * 仅供web或无法使用spring ioc注入spring bean的场景使用
 * 非web场景必须初始化ApplicationContext并加入service factory
 * 默认读取classpath下所有后缀为.spring.xml的配置文件
 *
 * @version 1.0.0
 */
public class ServiceFactory {
  private static final ServiceFactory _instance;

  static {
    _instance = new ServiceFactory();
  }

  public static ServiceFactory getInstance() {
    return _instance;
  }

  // spring application
  private ApplicationContext context = null;

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.context = applicationContext;
  }

  public ApplicationContext getApplicationContext() {
    if (this.context == null) {
      synchronized (this) {
        this.setApplicationContext(new ClassPathXmlApplicationContext("classpath*:**/*.spring.xml"));
      }
    }
    return this.context;
  }

  /**
   * 获得服务实例
   *
   * @param serviceName 服务名称
   * @param service     服务实现类
   * @return 服务实例
   */
  public <T> Object getService(String serviceName, Class<T> service) {
    if (StringUtils.hasText(serviceName)) {
      return  getApplicationContext().getBean(serviceName, service);
    }
    return null;
  }

  /**
   * 获得bean
   *
   * @param beanName bean名称
   * @return bean
   */
  public Object getBean(String beanName) {
    if (StringUtils.hasText(beanName)) {
      return getApplicationContext().getBean(beanName);
    }
    return null;
  }
}
