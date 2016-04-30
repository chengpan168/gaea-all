package com.gaea.dto;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gaea.entity.User;

/**
 * Created by chengpanwang on 4/15/16.
 */
public class UserDto extends User {

    private static Logger logger   = LoggerFactory.getLogger(UserDto.class);

    private Boolean       remember = true;
    private String        ticket;
    private String        rawPassword;
    private boolean       owner;
    private List<String>  roles;
    private List<String>  urlPatterns;
    private List<String>  permissions;

    public UserDto() {
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        if (StringUtils.isNotBlank(rawPassword)) {
            setPassword(DigestUtils.md5Hex(rawPassword));
        }

    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean remember) {
        if (remember == null) {
            this.remember = false;
            return;
        }

        this.remember = remember;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
