package com.gaea.common.cache;

/**
 * Created by panwang.chengpw on 7/4/15.
 */
public interface Cache {

    String get(String key);

    boolean set(String key, String value);

}
