package com.gaea.client;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.ThreadState;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * Created by chengpanwang on 4/14/16.
 */

public class GaeaFilter extends ShiroFilter {

    private static final Logger logger             = LoggerFactory.getLogger(GaeaFilter.class);

    private String              exclusionsName     = "exclusions";
    private String              loginUrlName       = "loginUrl";
    private String              deniedUrlName       = "deniedUrl";
    private String              logoutUrlName      = "logoutUrl";
    private String              localLogoutUrlName = "localLogoutUrl";
    private String              hostName           = "host";
    private String              appNameName        = "appName";
    private String              filterUrlName        = "filterUrl";

    private AntPathMatcher      antPathMatcher;
    private String              appName;
    private String              host               = Constants.HOST;
    private String              loginUrl           = Constants.LOGIN_URL;
    // 服务中心登出url
    private String              logoutUrl          = Constants.LOGOUT_URL;
    private String              deniedUrl          = Constants.DENIED_URL;
    private String              localLogoutUrl     = Constants.LOCAL_LOGOUT_URL;
    private boolean             filterUrl          = true;

    private Set<String>         exclusions;

    public void init() throws Exception {
        super.init();

        antPathMatcher = new AntPathMatcher();
        exclusions = resolveExclusions(filterConfig);

        String loginUrlParam = filterConfig.getInitParameter(loginUrlName);
        if (StringUtils.isNotBlank(loginUrlParam)) {
            this.loginUrl = loginUrlParam;
        }

        String logoutUrlParam = filterConfig.getInitParameter(logoutUrlName);
        if (StringUtils.isNotBlank(logoutUrlParam)) {
            this.logoutUrl = logoutUrlParam;
        }
        String localLogoutUrlParam = filterConfig.getInitParameter(localLogoutUrlName);
        if (StringUtils.isNotBlank(localLogoutUrlParam)) {
            this.localLogoutUrl = localLogoutUrlParam;
        }

        String hostParam = filterConfig.getInitParameter(hostName);
        if (StringUtils.isNotBlank(hostParam)) {
            this.host = hostParam;
        }
        String appNameParam = filterConfig.getInitParameter(appNameName);
        if (StringUtils.isNotBlank(appNameParam)) {
            this.appName = appNameParam;
        }
        String deniedUrlParam = filterConfig.getInitParameter(deniedUrlName);
        if (StringUtils.isNotBlank(deniedUrlParam)) {
            this.deniedUrl = deniedUrlParam;
        }
        String filterUrlParam = filterConfig.getInitParameter(filterUrlName);
        if (StringUtils.isNotBlank(filterUrlParam)) {
            this.filterUrl = BooleanUtils.toBoolean(filterUrlParam);
        }

        GaeaContext.init();
        GaeaContext.setAppName(appName);
        GaeaContext.setHost(host);
        GaeaContext.setLoginUrl(loginUrl);
        GaeaContext.setDeniedUrl(deniedUrl);
        GaeaContext.setLogoutUrl(logoutUrl);
        GaeaContext.setExclusions(exclusions);
        GaeaContext.setSecurityManager(WebUtils.getWebEnvironment(filterConfig.getServletContext()).getWebSecurityManager());

    }

    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, final FilterChain chain) throws ServletException,
                                                                                                                            IOException {


        GaeaContext.push(servletRequest, servletResponse);

        ThreadState threadState = null;
        try {
            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

            final Subject subject = createSubject(request, response);

            //手动关联到当前线程, 方便使用SecurityUtil.getSubject() 方法
            threadState = new SubjectThreadState(subject);
            threadState.bind();

            updateSessionLastAccessTime(request, response);
            execute(request, response, chain);

        } catch (Throwable e) {
            logger.error("", e);
        } finally {
            if (threadState != null) {
                threadState.clear();
            }
            GaeaContext.clear();
        }

    }

    private void execute(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = WebUtils.toHttp(servletResponse);
        HttpServletRequest request = WebUtils.toHttp(servletRequest);

        String uri = request.getRequestURI();

        // 如果是登出url
        if (handleLogout(uri)) {
            return;
        }

        if (isExclude(uri)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }


        // 如果没有验证通过, 重定向到登录页面
        boolean isLogin = authenticate();

        if (!isLogin) {
            logger.warn("没有登录信息, 重定向到登录页面");
            response.sendRedirect(buildUrl(GaeaContext.getLoginUrl()));
            return;
        }

        if (filterUrl && !isAccess(uri)) {
            logger.warn("没有权限, 重定向到无权限页面");
            response.sendRedirect(buildUrl(GaeaContext.getDeniedUrl()));
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
        return;
    }

    // 解析排除的url, 以逗号分隔
    private Set<String> resolveExclusions(FilterConfig filterConfig) {
        Set<String> exclusionsSet = Collections.emptySet();

        String exclusions = filterConfig.getInitParameter(exclusionsName);
        String[] exclusionsArr = StringUtils.split(exclusions, ",");

        if (ArrayUtils.isEmpty(exclusionsArr)) {
            return exclusionsSet;
        }


        exclusionsSet = Sets.newHashSet(StringUtils.stripAll(exclusionsArr));

        return exclusionsSet;
    }

    /**
     * 验证用户是登录
     * @return
     * @throws IOException
     */
    private boolean authenticate() throws IOException {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            return true;
        }

        // session 过期等, 从cookie取出ticket重新从服务器验证
        String ticket = GaeaHelper.getTicket();
        AuthTicket authTicket = new AuthTicket(ticket);

        try {
            logger.info("会话过期, 重新获取登录信息, ticket:{}", ticket);
            subject.login(authTicket);
            subject.getSession().setAttribute(Constants.TICKET_NAME, ticket);

            return true;
        } catch (AuthenticationException e) {
            logger.warn("获取用户信息失败", e);
        }
        return false;
    }

    private boolean isExclude(String uri) {

        for (String patter : GaeaContext.getExclusions()) {
            if (antPathMatcher.matches(patter, uri)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAccess(String uri) {

        GaeaUser userInfo = GaeaHelper.getUser();
        if (userInfo == null || userInfo.getUrlPatterns() == null) {
            return false;
        }
        Collection<String> urlPatterns = userInfo.getUrlPatterns();
        for (String pattern : urlPatterns) {
            if (antPathMatcher.matches(pattern, uri)) {
                return true;
            }
        }

        return false;
    }

    private boolean handleLogout(String uri) {
        if (antPathMatcher.matches(localLogoutUrl, uri)) {
            CookieHelper.removeCookie(Constants.COOKIE_SID);
            SecurityUtils.getSubject().getSession().stop();
            try {
                String url = buildLogoutUrl(logoutUrl);
                GaeaContext.getResponse().sendRedirect(url);
            } catch (IOException e) {
                logger.error("", e);
            }
            return true;
        }

        return false;
    }

    private String buildUrl(String url) {
        HttpServletRequest request = GaeaContext.getRequest();
        StringBuffer backUrl = request.getRequestURL().append("?");
        backUrl.append(StringUtils.trimToEmpty(request.getQueryString()));

        return url + "?backUrl=" + backUrl + "&appName=" + appName;
    }

    private String buildLogoutUrl(String url) {
        HttpServletRequest request = GaeaContext.getRequest();

        return url + "?backUrl=" + StringUtils.trimToEmpty(request.getHeader("Referer")) + "&appName=" + appName;
    }
}
