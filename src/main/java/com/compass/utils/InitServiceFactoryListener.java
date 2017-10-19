package com.compass.utils;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 初始化 Service 工厂
 */
public class InitServiceFactoryListener extends ContextLoaderListener {
  private final   Log logger = LogFactory.getLog(getClass());

  public void contextInitialized(ServletContextEvent event) {
    String contextPath = event.getServletContext().getContextPath();
    ContextRootUtils.setContextRoot(contextPath);

    logger.info("初始化平台...");
    super.contextInitialized(event);
    // 初始化Support中Spring的CTX
    ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
    // 将spring的BeanFactory注册给ServiceFactory
    ServiceFactory.getInstance().setApplicationContext(ctx);
    logger.info("初始化完成...");
  }
}
