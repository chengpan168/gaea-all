package com.gaea.mapper.query;

import com.gaea.common.query.QueryBase;

/**
 * Created by tiantiea on 16/4/26.
 */
public class PermissionQuery extends QueryBase {

    private Long   appId;

    private Long   staffId;

    private String appName;

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
