package com.gaea.common.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.UUID;

public class Randoms {

    public static String UUID() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static String nextSeq() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyMMddHHmmss.SS") + RandomUtils.nextLong(100000000l, 999999999l);
    }
}
