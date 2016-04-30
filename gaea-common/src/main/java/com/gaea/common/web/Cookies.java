package com.gaea.common.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.gaea.common.web.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chengpanwang on 7/9/15.
 */
public class Cookies {

    private static Logger logger = LoggerFactory.getLogger(Cookies.class);

    private static String BASE_DOMAIN;
    private static int    EXPIRE = 24 * 30 * 24 * 60 * 60;

    public static void setCookie(String name, String value) {
        setCookie(name, value, EXPIRE);
    }

    public static void setCookie(String name, String value, int expire) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(getBaseDomain());
        cookie.setPath("/");
        cookie.setMaxAge(expire);
        cookie.setHttpOnly(true);
        RequestContext.getResponse().addCookie(cookie);
    }

    public static String getCookie(String name) {
        HttpServletRequest request = RequestContext.getRequest();
        if (request == null) {
            return StringUtils.EMPTY;
        }

        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return StringUtils.EMPTY;
        }

        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), name)) {
                return cookie.getValue();
            }
        }

        return StringUtils.EMPTY;
    }

    private static String getBaseDomain() {
        if (BASE_DOMAIN != null) {
            return BASE_DOMAIN;
        } else {
            InputStream inputStream = null;
            try {
                inputStream = Cookies.class.getResourceAsStream("/gaea-common.properties");
                Properties properties = new Properties();
                properties.load(inputStream);
                BASE_DOMAIN = properties.getProperty("base.domain");
                if (BASE_DOMAIN == null) {
                    BASE_DOMAIN = StringUtils.EMPTY;
                }
                logger.info("domain:" + BASE_DOMAIN);
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }

        return BASE_DOMAIN;
    }
}
