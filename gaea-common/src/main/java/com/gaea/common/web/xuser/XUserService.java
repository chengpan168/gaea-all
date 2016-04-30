package com.gaea.common.web.xuser;

/**
 * 登录成功, 请执行@see XUserSessionManager.init(XUser) 方法
 *
 *
 * Created by panwang.chengpw on 6/29/15.
 */
public interface XUserService {

    /**
     * 通过用户名查找密码
     * @param name
     * @return
     */
    String getPass(String name);

    /**
     * 通过用户名查找用户id
     * @param name
     * @return
     */
    Long getUid(String name);

    /**
     * 实现此方法, 调用 XUserSession.refreshXUser(XUser);
     */
    void loginSuccess(XUser xUser);
}
