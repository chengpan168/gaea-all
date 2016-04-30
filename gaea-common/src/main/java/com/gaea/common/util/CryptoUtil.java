package com.gaea.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by panwang.chengpw on 6/29/15.
 */
public class CryptoUtil {

    private static Logger      logger          = LoggerFactory.getLogger(CryptoUtil.class);

    public static final String AES_KEY         = "2A8B3#E@0(#%!*5(_#`~^*)@%$@$!$^%*CA@#%DE8#258$(@&%46F";

    private static String      DEFAULT_CHARSET = "UTF-8";
    private static String      AES             = "AES";
    private static String      SECURE_TYPE     = "SHA1PRNG";

    public static String encryptAES(String content) {
        return encryptAES(content, AES_KEY);
    }

    /**
     * 加密, 顺序是 AES BASE65 URLEncoder
     * @param content
     * @param encryptKey
     * @return
     */
    public static String encryptAES(String content, String encryptKey) {
        if (StringUtils.isBlank(content)) {
            return StringUtils.EMPTY;
        }

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
            secureRandom.setSeed(encryptKey.getBytes(DEFAULT_CHARSET));
            keyGen.init(128, secureRandom);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
            byte[] data = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));

            return URLEncoder.encode(new String(Base64.encodeBase64(data), DEFAULT_CHARSET), DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error("AES encrypt error", e);
            throw new RuntimeException("AES encrypt error", e);
        }
    }

    public static String decryptAES(String content) {
        return decryptAES(content, AES_KEY);

    }

    public static String decryptAES(String content, String decryptKey) {
        if (StringUtils.isBlank(content)) {
            return StringUtils.EMPTY;
        }
        try {
            byte[] encryptBytes = Base64.decodeBase64(URLDecoder.decode(content, DEFAULT_CHARSET).getBytes(DEFAULT_CHARSET));
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_TYPE);
            secureRandom.setSeed(decryptKey.getBytes(DEFAULT_CHARSET));
            keyGen.init(128, secureRandom);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGen.generateKey().getEncoded(), AES));
            byte[] data = cipher.doFinal(encryptBytes);

            return new String(data, DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error("AES decrypt error", e);
            throw new RuntimeException("AES decrypt error", e);
        }

    }

    public static void main(String[] args) {
        String content = "e10adc3949ba59abbe56e057f20f883e";
        String key = "2A8B3#E@0(#%!*5(_#`~^*)@%$@$!$^%*CA@#%DE8#258$(@&%46F";

        String en = encryptAES(content, key);
        System.out.println(en);
        String de = decryptAES(en, key);
        System.out.println(de);
    }

}
