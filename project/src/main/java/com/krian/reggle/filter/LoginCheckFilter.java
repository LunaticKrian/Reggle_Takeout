package com.krian.reggle.filter;

import com.alibaba.fastjson.JSON;
import com.krian.reggle.common.BaseContext;
import com.krian.reggle.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登陆
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器（spring core工具类），支持使用通配符进行路径匹配：
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1.获取本次请求的URI：
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);

        // 2.创建可放行的URI的数组：
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/login",
                "/user/logout",
                "/user/sendMsg",
                "/backend/**",          // 后台静态资源
                "/front/**",            // 前台静态资源
                "/common/**"
        };

        // 3.判断请求路径是否需要处理：
        boolean checkResult = check(urls, requestURI);

        // 4.1 不需要处理，直接放行：
        if (checkResult) {
            log.info("本次请求 {} 不需要处理，方向通过！", requestURI);
            filterChain.doFilter(request, response);  // 放行请求
            return;
        }

        log.info("{} 请求开始处理！", requestURI);

        // 4.2 需要处理，判断用户是否已经登陆，如果登陆则放行：
        if (request.getSession().getAttribute("employee") != null) {  // PC浏览器登陆
            log.info("当前用户已登录！");
            filterChain.doFilter(request, response);  // 放行请求

            // 获取Session中的员工ID：
            Long empId = (long) request.getSession().getAttribute("employee");

            // 向ThreadLocal中添加数据员工ID：
            BaseContext.setCurrentId(empId);

        } else if (request.getSession().getAttribute("user") != null) {  // 移动端用户登陆
            log.info("当前用户已登录！");
            filterChain.doFilter(request, response);  // 放行请求

            // 获取Session中的员工ID：
            Long userId = (long) request.getSession().getAttribute("user");

            // 向ThreadLocal中添加数据员工ID：
            BaseContext.setCurrentId(userId);
        } else {
            log.info("当前用户未登录！");
            // 把JSON数据写给前端：
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }
    }

    /**
     * @param urls,requestURI
     * @return
     * @Func 路径匹配，检查当前的请求是否需要放行
     */
    private boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            // 使用匹配器，完成对路径的匹配：
            // boolean match = PATH_MATCHER.match(url, requestURI);
            // if (match) return true;

            // 简写：
            if (PATH_MATCHER.match(url, requestURI)) return true;
        }
        return false;
    }
}
