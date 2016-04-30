package com.gaea.common.test;

import javax.annotation.Resource;

import com.gaea.common.web.context.RequestContext;
import com.gaea.common.web.xuser.XUserSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by panwang.chengpw on 7/1/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:spring/spring-*.xml")
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BaseDBTest extends BaseTest {

    private static Logger        baseLogger = LoggerFactory.getLogger(BaseDBTest.class);
    @Resource
    protected ApplicationContext applicationContext;

    @BeforeClass
    public static void beforeClass() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();

        RequestContext.init(req, res, null);

        XUserSession.getCurrent().getXUser().setUid(1l);

        baseLogger.info("start test...");
    }

    @Test
    public void testSpring() {
        System.out.println(applicationContext);
    }

}
