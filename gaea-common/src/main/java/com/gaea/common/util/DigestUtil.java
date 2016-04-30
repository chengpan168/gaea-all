package com.gaea.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by panwang.chengpw on 5/7/15.
 */
public class DigestUtil {

    private static final String HMAC_SHA1 = "HMACSHA1";
    private static       Logger logger    = LoggerFactory.getLogger(DigestUtil.class);
    private static       String CHARSET   = "UTF-8";

    /**
     * 生成签名数据
     *
     * @param text 待加密的数据
     * @param key  加密使用的key
     * @return 生成MD5编码的字符串
     */
    public static String getSHA(String text, String key) {
        try {
            byte[] data = text.getBytes(CHARSET);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(CHARSET), HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            return DigestUtils.sha1Hex(rawHmac);
        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }


    public static String MD5(String value) {
        return DigestUtils.md5Hex(value);
    }

}
