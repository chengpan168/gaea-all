package com.gaea.client.service;

import com.gaea.client.GaeaUser;

import java.util.Map;

/**
 * Created by chengpanwang on 4/14/16.
 */
public interface GaeaRemoteService {

    GaeaUser get(String ticket);

    GaeaUser getDetail(String ticket);

    /**
     * 返回该应用, 最近有修改的在线用户的信息
     */
    void sync(Map<String, Long> sessionStatusMap);
}
