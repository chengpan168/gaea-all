package com.gaea.dto;

import com.gaea.entity.Url;

/**
 * Created by tiantiea on 16/4/26.
 */
public class UrlDto extends Url {

    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
