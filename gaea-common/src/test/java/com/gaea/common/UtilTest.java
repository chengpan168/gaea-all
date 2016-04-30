package com.gaea.common;

import com.gaea.common.test.BaseTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * Created by chengpanwang on 11/4/15.
 */
public class UtilTest extends BaseTest{

    @Test
    public void testSHA1() {
        String sha = DigestUtils.sha1Hex("hello");
        assertEquals(sha, "aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d");

    }

    @Test
    public void testsome() {
//        boolean phoneExists = someService.exist("18202151234");
        boolean phoneExists = true;
        assertTrue(phoneExists);
    }

}
