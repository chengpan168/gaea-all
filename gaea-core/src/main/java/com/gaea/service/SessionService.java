package com.gaea.service;

import com.gaea.dto.SessionDto;
import com.gaea.dto.UserDto;
import com.gaea.entity.GaeaSession;

import java.util.Map;

/**
 * Created by chengpanwang on 4/21/16.
 */
public interface SessionService {

    void put(String appName, GaeaSession session);

    void putNew(String appName, UserDto user);

    void clean(String appName, String ticket);

    GaeaSession get(String appName, String ticket);

    SessionDto sync(String appName, Map<String, Long> statusMap);
}
