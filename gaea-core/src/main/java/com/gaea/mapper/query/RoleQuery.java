package com.gaea.mapper.query;

import com.gaea.common.query.QueryBase;

/**
 * Created by tiantiea on 16/4/22.
 */
public class RoleQuery extends QueryBase {

    private String status;

    private Long   appId;
    private String appName;
    private Long   staffId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
