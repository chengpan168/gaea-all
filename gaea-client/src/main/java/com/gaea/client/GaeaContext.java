package com.gaea.client;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.mgt.WebSecurityManager;

import com.gaea.client.service.GaeaRemoteService;
import com.gaea.client.service.GaeaRemoteServiceImpl;

/**
 * Created by chengpanwang on 4/14/16.
 */

/**
 * gaea 的上下文, 存储 服务器host  登录接口等等
 */
public class GaeaContext {

    private static String                           appName;
    private static String                           host            = Constants.HOST;
    private static String                           loginUrl        = Constants.LOGIN_URL;
    private static String                           deniedUrl       = Constants.DENIED_URL;
    private static String                           logoutUrl       = Constants.LOGOUT_URL;
    private static Collection<String>               exclusions      = Collections.emptySet();

    private static ThreadLocal<HttpServletRequest>  requestContext  = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<HttpServletResponse>();

    private static WebSecurityManager               securityManager;

    private static GaeaRemoteService                gaeaRemoteService;

    public static void init() {
        gaeaRemoteService = new GaeaRemoteServiceImpl();
        new GaeaHeartbeat(gaeaRemoteService).start();
    }

    public static void push(ServletRequest request, ServletResponse response) {
        setRequest((HttpServletRequest) request);
        setResponse((HttpServletResponse) response);
    }

    public static void clear() {
        requestContext.remove();
        responseContext.remove();
    }

    public static GaeaUser getUser() {
        return GaeaHelper.getUser();
    }

    /**
     * 获取子权限
     * @param name  city:30120
     * @return
     */
    public static Collection<String> getPermissions(String name) {
        if (StringUtils.isBlank(name)) {
            getUser().getPermissions();
        }

        return GaeaHelper.getUser().getPermissionChildren(name);
    }

    public static HttpServletRequest getRequest() {
        return requestContext.get();
    }

    public static HttpServletResponse getResponse() {
        return responseContext.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestContext.set(request);
    }

    public static void setResponse(HttpServletResponse response) {
        responseContext.set(response);
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        GaeaContext.appName = appName;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        GaeaContext.host = host;
    }

    public static String getLoginUrl() {
        return loginUrl;
    }

    public static void setLoginUrl(String loginUrl) {
        GaeaContext.loginUrl = loginUrl;
    }

    public static Collection<String> getExclusions() {
        return exclusions;
    }

    public static void setExclusions(Collection<String> exclusions) {
        GaeaContext.exclusions = exclusions;
    }

    public static WebSecurityManager getSecurityManager() {
        return securityManager;
    }

    public static void setSecurityManager(WebSecurityManager securityManager) {
        GaeaContext.securityManager = securityManager;
    }

    public static GaeaRemoteService getGaeaRemoteService() {
        return gaeaRemoteService;
    }

    public static void setGaeaRemoteService(GaeaRemoteService gaeaRemoteService) {
        GaeaContext.gaeaRemoteService = gaeaRemoteService;
    }

    public static String getLogoutUrl() {
        return logoutUrl;
    }

    public static void setLogoutUrl(String logoutUrl) {
        GaeaContext.logoutUrl = logoutUrl;
    }

    public static String getDeniedUrl() {
        return deniedUrl;
    }

    public static void setDeniedUrl(String deniedUrl) {
        GaeaContext.deniedUrl = deniedUrl;
    }
}
