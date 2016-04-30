package com.gaea.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.gaea.common.constant.Constant;

/**
 * Created by chengpanwang on 4/22/16.
 */
public class GaeaSession {

    public GaeaSession() {
    }

    public GaeaSession(String ticket, Long staffId) {
        this.ticket = ticket;
        this.staffId = staffId;

        Date now = new Date();
        this.startTime = now;
        this.lastAccessTime = now;
    }

    @JSONField(name = "t")
    private String ticket;
    @JSONField(name = "sid")
    private Long   staffId;
    @JSONField(name = "uid")
    private Long   userId;
    @JSONField(name = "s", format = "yyyy-MM-dd HH:mm:ss")
    private Date   startTime;
    @JSONField(name = "l", format = "yyyy-MM-dd HH:mm:ss")
    private Date   lastAccessTime;

    public boolean isExpire() {
        //过期
        if ((System.currentTimeMillis() - lastAccessTime.getTime()) > Constant.SESSION_EXPIRE) {
            return true;
        }
        return false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
