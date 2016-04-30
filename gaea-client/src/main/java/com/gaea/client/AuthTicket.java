package com.gaea.client;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by chengpanwang on 4/14/16.
 */
public class AuthTicket implements AuthenticationToken {

    private String ticket;
    private String principal;
    private String credentials;

    public AuthTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
