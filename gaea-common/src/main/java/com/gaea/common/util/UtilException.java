package com.gaea.common.util;

/**
 * Created by panwang.chengpw on 5/7/15.
 */
public class UtilException extends RuntimeException {

    public UtilException(Exception e) {
        super(e);
    }

    public UtilException(String msg) {
        super(msg);
    }

    public UtilException(String msg, Exception e) {
        super(msg, e);
    }
}
