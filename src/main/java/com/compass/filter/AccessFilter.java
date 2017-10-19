package com.compass.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.compass.systemlog.service.SystemLogService;
import com.compass.utils.ConstantUtils;

/**
 * 用于检测用户是否登陆的过滤器，如果未登录，则重定向到指的登录页面 配置参数 checkSessionKey 需检查的在 Session 中保存的关键字 redirectURL 如果用户未登录，则重定向到指定的页面，URL不包括 ContextPath notCheckURLList 不做检查的URL列表，以分号分开，并且 URL 中不包括 ContextPath
 */
public class AccessFilter implements Filter {
    @Autowired
    @Qualifier("systemLogService")
    private SystemLogService systemLogService;
    protected FilterConfig   filterConfig    = null;
    private String           redirectURL     = null;
    private Set<String>      notCheckURLList = new HashSet<String>();
    private String           sessionKey      = null;

    public void destroy() {
        notCheckURLList.clear();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        //封装分页参数
        String rows = request.getParameter("rows");
        String page = request.getParameter("page");
        int pagenumber = Integer.parseInt((page == null) || (page == "0") ? "1" : page);
        int rownumber = Integer.parseInt((rows == "0") || (rows == null) ? "20" : rows);
        int start = (pagenumber - 1) * rownumber;
        int end = start + rownumber;
        ThreadLocalContext.setStartValue(start);
        ThreadLocalContext.setEndValue(end);
        
        
        HttpSession session = request.getSession();
        if (sessionKey == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if ((!checkRequestURIIntNotFilterList(request)) && session.getAttribute(sessionKey) == null) {
            response.sendRedirect(request.getContextPath() + redirectURL);
            return;
        } else {
            String path = request.getServletPath();
            String tempPath = "";
            List<String> urlList = (List<String>) request.getSession().getAttribute(ConstantUtils.MENULIST);
            if (path != null) {
                boolean flag = true;
                tempPath = path.substring(path.length() - 3, path.length());
                path = path.substring(1, path.length());
                if (checkRequestURIIntNotFilterList(request)) {
                    flag = false;
                }
                if (!"main.jsp".equals(path) && !".do".equals(tempPath)) {
                    if (urlList != null && urlList.size() > 0) {
                        for (String temp : urlList) {
                            if (path.equals(temp)) {
                                flag = false;
                            }
                        }
                    }
                } else {
                    flag = false;
                }
                if (flag) {
                    response.sendRedirect(request.getContextPath() + "/main.jsp");
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {
        String uri = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        String temp = request.getRequestURI();
        temp = temp.substring(request.getContextPath().length() + 1);
        return notCheckURLList.contains(uri);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        redirectURL = filterConfig.getInitParameter("redirectURL");
        sessionKey = filterConfig.getInitParameter("checkSessionKey");
        String notCheckURLListStr = filterConfig.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            String[] params = notCheckURLListStr.split(",");
            for (int i = 0; i < params.length; i++) {
                notCheckURLList.add(params[i].trim());
            }
        }
    }

}
