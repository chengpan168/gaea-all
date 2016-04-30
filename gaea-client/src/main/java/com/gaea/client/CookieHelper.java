package com.gaea.client;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chengpanwang on 7/9/15.
 */
public class CookieHelper {

    public static String getCookie(String name) {
        HttpServletRequest request = GaeaContext.getRequest();

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

    public static void removeCookie(String name) {
            Cookie cookie = new Cookie(name, StringUtils.EMPTY);
            cookie.setPath("/");
            GaeaContext.getResponse().addCookie(cookie);
    }
}
