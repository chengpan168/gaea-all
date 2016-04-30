package com.gaea.mapper.query;

import com.gaea.common.query.QueryBase;

/**
 * Created by chengpan on 2016/3/4.
 */
public class UserQuery extends QueryBase {

    private String name;
    private Long   id;
    private String status;
    private Long staffId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
