package com.gaea.common.test;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by panwang.chengpw on 7/1/15.
 */

public class BaseTest extends TestCase {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected void log(Object object) {
        logger.info(object + "");
    }

}
