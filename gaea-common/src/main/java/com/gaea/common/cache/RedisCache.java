package com.gaea.common.cache;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;

/**
 * Created by panwang.chengpw on 7/4/15.
 */
public class RedisCache implements Cache {

    private static Logger          logger          = LoggerFactory.getLogger(RedisCache.class);

    private static String          DEFAULT_CHARSET = "UTF-8";

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    @Override
    public String get(String key) {
        if (StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        try {
            byte[] bytes = getJedis().get(key.getBytes(DEFAULT_CHARSET));
            if (bytes != null) {
                return new String(bytes, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }

    @Override
    public boolean set(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            getJedis().set(key.getBytes(DEFAULT_CHARSET), value.getBytes(DEFAULT_CHARSET));
            return true;
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;

    }

    /**
     * 自增
     * @param key key
     * @param expire 过期时间
     * @return
     */
    public long incr(String key, int expire) {
        getJedis().expire(key, expire);
        return getJedis().incr(key);

    }

    /**
     * 自减
     * @param key key
     * @param expire 过期时间
     * @return
     */
    public long decr(String key, int expire) {
        getJedis().expire(key, expire);
        return getJedis().decr(key);

    }

    /**
     * 自增
     * @param key key
     * @return
     */
    public long incr(String key) {
        return getJedis().incr(key);

    }

    public void hSet(String pKey, String sKey, String value) {
        if (StringUtils.isAnyBlank(pKey, sKey)) {
            return;
        }
        getJedis().hset(pKey, sKey, value);
    }

    public String hGet(String pKey, String sKey) {
        if (StringUtils.isAnyBlank(pKey, sKey)) {
            return StringUtils.EMPTY;
        }
        return getJedis().hget(pKey, sKey);
    }

    public Map<String, String> hGet(String pKey) {
        if (StringUtils.isBlank(pKey)) {
            return Collections.emptyMap();
        }

        return getJedis().hgetAll(pKey);
    }

    public Jedis getJedis() {
        return jedisConnectionFactory.getShardInfo().createResource();
    }
}
