/**
 * 
 */
package com.gaea.service.helper;

/**
 * @author Nichole
 *
 *         2015年2月28日
 */
public class Member {

    private String id;

    private String userid;
    private String username;

    private String password;
    private String token;
    private String createtime;
    private String expires;
    private String appid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String toString() {
        return "Member [id=" + id + ", userid=" + userid + ", username=" + username + ", password=" + password + ", token=" + token + ", createtime="
               + createtime + ", expires=" + expires + ", appid=" + appid + "]";
    }

}
