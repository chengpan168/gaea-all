package com.gaea.client;

import java.util.Collection;
import java.util.Collections;

import org.apache.shiro.authz.AuthorizationInfo;

import com.gaea.client.tree.Tree;

/**
 * Created by chengpanwang on 4/14/16.
 */
public class GaeaUser {

    private String             name;
    private String             password;
    private boolean            owner;
    private Collection<String> roles;
    private Collection<String> urlPatterns;
    private Collection<String> permissions;
    private Tree               permissionTree;

    private AuthorizationInfo authorizationInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Collection<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }

    public AuthorizationInfo getAuthorizationInfo() {
        return authorizationInfo;
    }

    public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
    }

    public void resolvePermissionTree(Collection<String> permissions) {
        permissionTree = new Tree();
        permissionTree.resolve(permissions);
    }

    public Tree getPermissionTree() {
        return permissionTree;
    }

    public Collection<String> getPermissionChildren(String name) {

        if (permissionTree == null) {
            resolvePermissionTree(getPermissions());
        }

        return permissionTree.getChildren(name);
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
