package com.gaea.dto;

import com.gaea.entity.Role;
import com.gaea.enums.StatusEnum;

/**
 * Created by tiantiea on 16/4/25.
 */
public class RoleDto extends Role {

    private String appName;

    private String statusName;

    private String pathName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStatusName() {
        StatusEnum statusEnum = StatusEnum.fromCode(getStatus());
        if (statusEnum == null) {
            return null;
        }
        return statusEnum.getName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
