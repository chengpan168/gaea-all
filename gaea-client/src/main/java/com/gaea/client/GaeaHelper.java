package com.gaea.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 内部使用, 接入应用请使用GaeaContext
 * Created by chengpanwang on 4/15/16.
 */
public class GaeaHelper {

    private static final Logger logger = LoggerFactory.getLogger(GaeaHelper.class);

    /**
     * 获取登录的 ticket , 用来与服务中心验证用户状态和信息
     * @param request
     * @return
     */
    public static String getTicket() {
        return CookieHelper.getCookie(Constants.TICKET_COOKIE_NAME);
    }

    /**
     * 获取用户信息的open api
     * @return
     */
    public static String getInfoOpenApi() {
        return GaeaContext.getHost() + Constants.USER_INFO_PATH;
    }

    public static String getDetailOpenApi() {
        return GaeaContext.getHost() + Constants.USER_DETAIL_PATH;
    }

    public static String getSyncOpenApi() {
        return GaeaContext.getHost() + Constants.USER_SYNC_PATH;
    }
    /**
     * 往session 里塞东西
     * @param key
     * @param value
     */
    public static void toSession(String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            return;
        }

        getSession().setAttribute(key, value);
    }

    /**
     * 从session 里取东西
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T fromSession(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return (T) getSession().getAttribute(key);
    }

    public static Session getSession() {

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(true);
        if (session == null) {
            logger.error("没有取到session, 请检查配置, 有重大问题...");
        }

        return session;
    }

    public static GaeaUser getUser() {
        Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            return (GaeaUser) session.getAttribute(Constants.USER_SESSION_NAME);
        }
        return null;
    }

    public static void setUser(GaeaUser user) {
        Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            session.setAttribute(Constants.USER_SESSION_NAME, user);
        } else {
            logger.error("没有取到用户session, 检查应用配置");

        }
    }

}
