package com.gaea.common.web.xuser;

import javax.servlet.http.HttpSession;

import com.gaea.common.constant.Constant;
import com.gaea.common.context.SpringContext;
import com.gaea.common.web.Cookies;
import com.gaea.common.web.context.RequestContext;
import com.gaea.common.util.CryptoUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XUser 的全部实现, 要使用此方法需要实现XUserService 接口, 以断定用户合法性
 * 登录成功, 请执行@see init(XUser) 方法
 *
 * Created by panwang.chengpw on 5/11/15.
 */
public class XUserSessionManager {

    private static final Logger logger            = LoggerFactory.getLogger(XUserSessionManager.class);

    private static String       XUSER_SESSION_KEY = "xuser_session";
    private static String       CSRF_TOKEN_KEY    = "csrf_token";
    private static String       XUSER_NAME_KEY    = "xuser_n";
    private static String       XUSER_PASS_KEY    = "xuser_p";

    private static XUserService xUserService ;

    private XUserSessionManager() {

    }

    /**
     * 取得当前用户XUserSession ,以后可以替换为redis等
     * @return 当前用户
     */
    private static XUserSession getXUserSession() {
        XUserSession xUserSession;
        HttpSession httpSession = RequestContext.getSession();
        if (httpSession != null) {
            xUserSession = (XUserSession) httpSession.getAttribute(XUSER_SESSION_KEY);
        } else {
            logger.error("http session is null , please check web app");
            throw new RuntimeException("http session is null , please check web app");
        }

        return xUserSession;
    }

    /**
     * 存储当前用户XUserSession ,以后可以替换为redis等
     * @param xUserSession 当前用户sesseion
     */
    private static void setXUserSession(XUserSession xUserSession) {
        HttpSession httpSession = RequestContext.getSession();
        if (httpSession != null) {
            httpSession.setAttribute(XUSER_SESSION_KEY, xUserSession);
        } else {
            logger.error("http session is null , please check web app");
            throw new RuntimeException("http session is null , please check web app");
        }
    }

    public static XUserSession getCurrent() {
        XUserSession xUserSession = getXUserSession();

        if (xUserSession == null) {
            xUserSession = initXUserSession();
        }

        return xUserSession;
    }

    public static XUserSession initXUserSession() {



        XUserSession xUserSession = new XUserSession();
        XUser xUser = xUserSession.getXUser();

        String name = Cookies.getCookie(XUSER_NAME_KEY);
        if (StringUtils.isNotBlank(name)) {
            xUser.setUserName(name);

            // 自动登录
            String password = Cookies.getCookie(XUSER_PASS_KEY);
            if (StringUtils.isNotBlank(password)) {
                password = CryptoUtil.decryptAES(password);
                if (StringUtils.equals(password, getXUserService().getPass(name))) {
                    xUser.setIsSignedIn(true);
                    xUser.setPassword(password);
                    xUser.setUid(getXUserService().getUid(name));
                }
            }
        }

        xUserSession.setXUser(xUser);

        setXUserSession(xUserSession);

        setCookies(xUserSession);

        return xUserSession;

    }

    public static void setCookies(XUserSession xUserSession) {
        Cookies.setCookie(XUSER_NAME_KEY, xUserSession.getXUser().getUserName());
        Cookies.setCookie(CSRF_TOKEN_KEY, XUserSession.getCurrent().getCsrfToken());
        if (xUserSession.getXUser().getIsSavePass()) {
            Cookies.setCookie(XUSER_PASS_KEY, xUserSession.getXUser().getSecPassword());
        }
    }

    /**
     * 登录成功时执行此方法, 标识已经登录
     * @param xuser 当前登录用户
     */
    public static void refreshXUser(XUser xuser) {
        if (xuser == null) {
            logger.error("init xuser , but xuser is null");
            return;
        }

        XUserSession xUserSession = XUserSession.getCurrent();
        xUserSession.setXUser(xuser);
        setCookies(xUserSession);
    }


    public static XUserService getXUserService() {
        if (xUserService == null) {
            xUserService = SpringContext.getBean("xUserService");
        }

        return xUserService;
    }
}
