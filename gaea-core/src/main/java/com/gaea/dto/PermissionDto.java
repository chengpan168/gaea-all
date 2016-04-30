package com.gaea.dto;

import com.gaea.entity.Permission;
import com.gaea.enums.StatusEnum;

/**
 * Created by tiantiea on 16/4/26.
 */
public class PermissionDto extends Permission {

    private String appName;

    private String statusName;

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
}
