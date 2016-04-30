package com.gaea.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;
import com.gaea.common.cache.RedisLock;
import com.gaea.common.constant.Constant;
import com.gaea.dto.SessionDto;
import com.gaea.dto.UserDto;
import com.gaea.entity.GaeaSession;
import com.gaea.service.SessionService;
import com.google.common.collect.Sets;

/**
 * Created by chengpanwang on 4/21/16.
 */
public class SessionServiceImpl implements SessionService {

    private static final Logger                    logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOps;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String>        valueOps;
    @Resource
    private RedisLock                              redisLock;

    @Override
    public void clean(String appName, String ticket) {
        hashOps.delete(appName, ticket);
    }

    @Override
    public void putNew(String appName, UserDto user) {
        String ticket = user.getTicket();
        GaeaSession session = new GaeaSession(ticket, user.getStaffId());
        session.setUserId(user.getId());
        put(appName, session);
    }

    @Override
    public void put(String appName, GaeaSession session) {
        if (session == null || StringUtils.isAnyBlank(appName, session.getTicket())) {
            return;
        }

        String json = JSON.toJSONString(session);
        hashOps.put(appName, session.getTicket(), json);
    }

    @Override
    public GaeaSession get(String appName, String ticket) {
        if (StringUtils.isAnyBlank(appName, ticket)) {
            return null;
        }

        String json = (String) hashOps.get(appName, ticket);

        GaeaSession session = JSON.parseObject(json, GaeaSession.class);

        // 过期了, 删掉吧
        if (session == null || session.isExpire()) {
            hashOps.delete(appName, ticket);
            return null;
        }

        return session;
    }

    /**
     * 刷新session 最后访问时间
     * @param appName
     * @param ticket
     * @return
     */
    public GaeaSession touch(String appName, String ticket) {
        GaeaSession gaeaSession = get(appName, ticket);
        gaeaSession.setLastAccessTime(new Date());
        put(appName, gaeaSession);
        return gaeaSession;
    }

    @Override
    public SessionDto sync(String appName, Map<String, Long> statusMap) {

        String lock = buildLockName(appName);
        SessionDto sessionDto = new SessionDto();
        try {

            int times = 1;
            while (times < 4) {
                logger.info("尝试获取锁:{}, 次数:{}", lock, times);
                if (!redisLock.tryLock(lock)) {
                    TimeUnit.SECONDS.sleep(2 * times);
                } else {
                    break;
                }

                times++;
            }

            if (MapUtils.isEmpty(statusMap)) {
                return sessionDto;
            }

            //过期或者失效的ticket
            Set<String> expireTickets = Sets.newHashSet();

            Map<String, String> storeSessionMap = hashOps.entries(appName);
            if (MapUtils.isEmpty(storeSessionMap)) {
                return sessionDto;
            }

            // 清理过期的session
            clear(appName, storeSessionMap);

            for (Map.Entry<String, Long> entry : statusMap.entrySet()) {
                String ticket = entry.getKey();

                // 如果从redis 没有取出session  那就过期了
                GaeaSession storeSession = fromJson(MapUtils.getString(storeSessionMap, ticket));
                if (storeSession == null) {
                    expireTickets.add(ticket);
                    continue;
                }

                try {
                    // storeSession 正常  刷新最后访问时间
                    storeSession.getLastAccessTime().setTime(MapUtils.getLongValue(statusMap, storeSession.getTicket(), System.currentTimeMillis()));
                    put(appName, storeSession);
                } catch (Exception e) {
                    logger.error("更新redis session最后访问时间错误", e);
                }

            }

            sessionDto.setExpireTickets(expireTickets);
        } catch (Exception e) {

        } finally {
            redisLock.unLock(lock);
        }
        return sessionDto;
    }

    /**
     * 清理过期的 session
     * @param appName
     * @param storeSessionMap
     */
    public void clear(String appName, Map<String, String> storeSessionMap) {
        long timeMillis = System.currentTimeMillis();
        for (Map.Entry<String, String> entry : storeSessionMap.entrySet()) {
            String ticket = entry.getKey();
            GaeaSession storeSession = fromJson(entry.getValue());
            // storeSession 过期
            if ((timeMillis - storeSession.getLastAccessTime().getTime()) > Constant.SESSION_EXPIRE) {
                hashOps.delete(appName, ticket);
                storeSessionMap.remove(ticket);
                continue;
            }

        }
    }

    private GaeaSession fromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, GaeaSession.class);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    private String buildLockName(String appName) {
        return appName + "_sync_lock";
    }
}
