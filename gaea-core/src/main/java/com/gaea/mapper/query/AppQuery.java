package com.gaea.mapper.query;

import com.gaea.common.query.QueryBase;

/**
 * Created by tiantiea on 16/4/21.
 */
public class AppQuery extends QueryBase {

    private String name;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
