package com.gaea.common.cache;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Created by chengpanwang on 4/22/16.
 */
public class RedisLock {

    private static final Logger             logger = LoggerFactory.getLogger(RedisLock.class);

    private long                            expire = 3000;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;
    @Resource
    private RedisTemplate                   redisTemplate;

    public boolean tryLock(String lock) {
        // 通过SETNX试图获取一个lock
        long value = System.currentTimeMillis() + expire + 1;
        Boolean res = valueOps.setIfAbsent(lock, String.valueOf(value));
        //SETNX成功，则成功获取一个锁
        if (res) {
            logger.debug("获取锁成功:{}", lock);
            return true;
        } else {
            logger.debug("获取锁失败:{}", lock);
        }

        //SETNX失败，说明锁仍然被其他对象保持，检查其是否已经超时
        long oldValue = Long.parseLong(valueOps.get(lock));

        // 超时了...
        if (oldValue < System.currentTimeMillis()) {
            //超时的情况
            String getValue = valueOps.getAndSet(lock, String.valueOf(value));
            // 获取锁成功
            if (Long.valueOf(getValue) == oldValue) {
                return true;
            }
        }

        return false;
    }

    //释放锁
    public void unLock(String lock) {
        long current = System.currentTimeMillis();
        // 避免删除非自己获取得到的锁
        if (current < Long.valueOf(valueOps.get(lock))) redisTemplate.delete(lock);
        logger.info("释放锁成功:{}", lock);
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
