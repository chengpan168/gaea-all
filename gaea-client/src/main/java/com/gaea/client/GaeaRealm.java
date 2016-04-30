package com.gaea.client;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.gaea.client.service.GaeaRemoteService;
import com.google.common.collect.Sets;

/**
 * Created by chengpanwang on 4/14/16.
 */
public class GaeaRealm extends AuthorizingRealm {

    private GaeaRemoteService gaeaRemoteService;

    public GaeaRealm() {
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        AuthTicket authTicket = (AuthTicket) token;
        String ticket = authTicket.getTicket();
        ((AuthTicket) token).setCredentials(ticket);

        if (StringUtils.isBlank(ticket)) {
            return null;
        }

        GaeaUser user = getGaeaRemoteService().getDetail(ticket);
        if (user == null) {
            return new SimpleAccount();
        }

        Set<Permission> permissions = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(user.getPermissions())) {
            permissions = Sets.newHashSet();
            for (String permission : user.getPermissions()) {
                permissions.add(new WildcardPermission(permission));
            }
        }

        SimpleAccount simpleAccount = new SimpleAccount(ticket, ticket, getName(), Sets.newHashSet(user.getRoles()), permissions);

        user.setAuthorizationInfo(simpleAccount);
        GaeaHelper.setUser(user);

        return simpleAccount;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return GaeaHelper.getUser().getAuthorizationInfo();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthTicket;
    }

    public void setGaeaRemoteService(GaeaRemoteService gaeaRemoteService) {
        this.gaeaRemoteService = gaeaRemoteService;
    }

    public GaeaRemoteService getGaeaRemoteService() {
        if (gaeaRemoteService == null) {
            gaeaRemoteService = GaeaContext.getGaeaRemoteService();
        }
        return gaeaRemoteService;
    }
}
