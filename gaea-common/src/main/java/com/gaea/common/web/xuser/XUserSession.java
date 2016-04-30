package com.gaea.common.web.xuser;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by panwang.chengpw on 5/8/15.
 */
public class XUserSession {

    private static final long serialVersionUID = -8889430465884234259L;
    private XUser               xUser;
    private Map<String, Object> attrs;
    private String              csrfToken;
    private static String       RANDOM_CODE_KEY = "_RANDOM_CODE_KEY";

    public XUserSession() {
        xUser = new XUser();
        attrs = Maps.newHashMap();
        csrfToken = RandomStringUtils.randomAlphanumeric(8);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void setAttr(String name, Object object) {
        attrs.put(name, object);
    }

    public <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public XUser getXUser() {
        return xUser;
    }

    /**
     * 返回当前用户, 永远不为空
     * @return
     */
    public static XUserSession getCurrent() {
         return XUserSessionManager.getCurrent();
    }

    /**
     * 初始化当前用户的session
     */
    public static void initXUserSession() {
        XUserSessionManager.getCurrent();
    }

    public static void refreshXUser(XUser xUser) {
        XUserSessionManager.refreshXUser(xUser);
    }

    public static void login(XUser xUser) {
        xUser.setIsSignedIn(true);
        refreshXUser(xUser);
    }

    public static void logout() {
        XUser xUser = getCurrent().getXUser();
        xUser.setIsSignedIn(false);
        xUser.setIsSavePass(false);

        refreshXUser(xUser);
    }

    public void setXUser(XUser xUser) {
        this.xUser = xUser;
    }

    public void setRandomCode(String randomCode) {
        setAttr(RANDOM_CODE_KEY, randomCode);
    }

    public void cleanRandomCode() {
        setAttr(RANDOM_CODE_KEY, null);
    }

    /**
     * 用户图片验证码
     * @return
     */
    public String getRandomCode() {
        return getAttr(RANDOM_CODE_KEY);
    }

    public void refresh() {

    }

}
