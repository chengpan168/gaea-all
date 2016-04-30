package com.gaea.client;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * Created by chengpanwang on 4/19/16.
 */
public class GaeaSecurityManager extends DefaultWebSecurityManager {

    public GaeaSecurityManager() {
        super();
    }

    @Override
    protected void rememberMeSuccessfulLogin(AuthenticationToken token, AuthenticationInfo info, Subject subject) {

    }
}
