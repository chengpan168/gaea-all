package com.gaea.client;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;

/**
 * Created by chengpanwang on 4/15/16.
 */
public class GaeaUtils {

    public static boolean hasRole(String role) {
        return SecurityUtils.getSubject().hasRole(role);
    }

    public static boolean[] hasRoles(List<String> roleIdentifiers) {
        return SecurityUtils.getSubject().hasRoles(roleIdentifiers);
    }

    public static boolean hasAllRoles(Collection<String> roleIdentifiers) {
        return SecurityUtils.getSubject().hasAllRoles(roleIdentifiers);
    }

    public static boolean isPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    public static boolean[] isPermitted(String... permissions) {
        return SecurityUtils.getSubject().isPermitted(permissions);
    }

    public static boolean isPermittedAll(String... permissions) {
        return SecurityUtils.getSubject().isPermittedAll(permissions);
    }

}
