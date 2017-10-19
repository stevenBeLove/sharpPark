package com.compass.utils.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangLong
 * 错误处理
 */
public class ExceptionFilter implements Filter {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try{
      chain.doFilter(request, response);
    } catch(Exception exp) {
      exp.printStackTrace();
      logger.debug(exp.getMessage(),exp);
      logger.warn("Sorry,there is something wrong,please contact with IT! ", exp);
//      String message = exp.getCause()==null?exp.getMessage():exp.getCause().getCause().getMessage().toString();
//      String message = "程序异常,请联系相关技术人员!" ;
      String message = "Sorry,there is something wrong,please contact with IT!" ;
      String  msg =  new String(message.getBytes(),"UTF-8"); 
      AjaxReturnInfo ret = AjaxReturnInfo.failed(message);
//      ret.setException(exp);
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      PrintWriter writer = httpServletResponse.getWriter();
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(writer, ret);
      httpServletResponse.setCharacterEncoding("UTF-8");
      httpServletResponse.setContentType("application/json");
      msg=null;
      message=null;
      return;
    }
  }

  public void destroy() {
  }
}
