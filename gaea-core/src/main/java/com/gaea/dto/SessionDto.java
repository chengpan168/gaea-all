package com.gaea.dto;

import java.util.Collection;

/**
 * Created by chengpanwang on 4/22/16.
 */
public class SessionDto {
    private Collection<String> expireTickets;

    public Collection<String> getExpireTickets() {
        return expireTickets;
    }

    public void setExpireTickets(Collection<String> expireTickets) {
        this.expireTickets = expireTickets;
    }
}
