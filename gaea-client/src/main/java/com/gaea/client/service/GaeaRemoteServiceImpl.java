package com.gaea.client.service;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gaea.client.Constants;
import com.gaea.client.GaeaContext;
import com.gaea.client.GaeaHelper;
import com.gaea.client.GaeaUser;
import com.gaea.client.HttpClient;
import com.google.common.collect.Maps;

/**
 * Created by chengpanwang on 4/15/16.
 */
public class GaeaRemoteServiceImpl implements GaeaRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(GaeaRemoteServiceImpl.class);

    @Override
    public GaeaUser get(String ticket) {
        GaeaUser userInfo = HttpClient.getObject(GaeaHelper.getInfoOpenApi(), Collections.singletonMap(Constants.TICKET_NAME, ticket), GaeaUser.class);
        return userInfo;
    }

    @Override
    public GaeaUser getDetail(String ticket) {
        Map<String, String> param = Maps.newHashMapWithExpectedSize(2);
        param.put(Constants.APP_NAME, GaeaContext.getAppName());
        param.put(Constants.TICKET_NAME, ticket);

        GaeaUser userInfo = HttpClient.getObject(GaeaHelper.getDetailOpenApi(), param, GaeaUser.class);
        return userInfo;
    }

    @Override
    public void sync(Map<String, Long> sessionStatusMap) {
        logger.info("开始同步gaea服务器权限数据");
        logger.debug("ticket 数据:{}", sessionStatusMap);

        Map<String, String> param = Maps.newHashMap();
        param.put(Constants.APP_NAME, GaeaContext.getAppName());
        param.put(Constants.SESSION_STATUS, JSON.toJSONString(sessionStatusMap));
        String res = HttpClient.get(GaeaHelper.getSyncOpenApi(), param);

        logger.info("同步gaea服务器结果:{}", res);

    }
}
