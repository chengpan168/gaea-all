package com.gaea.client.tags;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

/**
 * Created by chengpanwang on 4/29/16.
 */
public class HasAnyPermissionTag extends PermissionTag {

    private static final String PERMISSION_NAMES_SEPARATOR = ",";

    @Override
    protected boolean showTagBody(String permissionNames) {

        Subject subject = getSubject();

        if (subject != null) {
            for (String permission : StringUtils.split(permissionNames, PERMISSION_NAMES_SEPARATOR)) {

                if (subject.isPermitted(StringUtils.trim(permission))) {
                    return true;
                }
            }
        }

        return false;
    }
}
