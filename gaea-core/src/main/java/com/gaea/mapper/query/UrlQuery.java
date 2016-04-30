package com.gaea.mapper.query;

import com.gaea.common.query.QueryBase;

/**
 * Created by tiantiea on 16/4/26.
 */
public class UrlQuery extends QueryBase {

    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
