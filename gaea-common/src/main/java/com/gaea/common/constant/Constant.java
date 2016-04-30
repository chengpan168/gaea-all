package com.gaea.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * Created by panwang.chengpw on 6/29/15.
 */
public class Constant {

    public static final String       COOKIE_TICKET   = "ticket";
    public static final String       COOKIE_NAME     = "g_name";
    public static final String       COOKIE_PASS     = "g_pass";

    public static final String       DEFAULT_CHARSET = "UTF-8";

    // session 过期时间 2小时 单位ms
    public static final Long         SESSION_EXPIRE  = 1 * 60 * 60 * 1000L;

    public static final List<String> ROOT_URI_PATTER = Arrays.asList("/**");
    public static final List<String> ROOT_PERMISSION = Arrays.asList("*");

}
