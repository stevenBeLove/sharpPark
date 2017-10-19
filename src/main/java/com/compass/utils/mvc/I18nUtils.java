package com.compass.utils.mvc;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * <pre>
 * 【类型】: I18nUtils <br/> 
 * 【作用】: 系统工具类. <br/>  
 * 【时间】：2017年3月22日 下午2:56:44 <br/> 
 * 【作者】：yinghui zhang <br/>
 * </pre>
 */
public class I18nUtils {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(I18nUtils.class);


    /**
     * 【方法名】    : (获取Request). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月22日 下午3:11:56 .<br/>
     * 【参数】： .<br/>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 【方法名】    : (获取Session). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月22日 下午3:12:21 .<br/>
     * 【参数】： .<br/>
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static HttpSession getSession() {
        if (getRequest() == null) {
            return null;
        }
        return getRequest().getSession();
    }

    /**
     * 【方法名】    : (这里用一句话描述这个方法的作用). <br/> 
     * 【作者】: yinghui zhang .<br/>
     * 【时间】： 2017年3月22日 下午3:12:55 .<br/>
     * 【参数】： .<br/>
     * @param key 参数值
     * @param params String
     * @return .<br/>
     * <p>
     * 修改记录.<br/>
     * 修改人:  yinghui zhang 修改描述： .<br/>
     * <p/>
     */
    public static String getResourceValue(String key, String... params) {
        String value = "";
        String[] param = null;
        try {
            if (getRequest() == null) {
                WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
                value = context.getMessage(key, params, Locale.CHINA);
            } else {
                MessageSource messageSource = RequestContextUtils.getWebApplicationContext(getRequest(), getSession().getServletContext());
                if (params != null && params.length == 1 && StringUtils.isEmpty(params[0])) {
                    param = null;
                } else {
                    param = params;
                }
                value = messageSource.getMessage(key, param, Locale.CHINA);
            }
        } catch (Exception e) {
            logger.debug("Not found this i18n resource: " + key);
            return value;
        }
        return value;
    }
}
