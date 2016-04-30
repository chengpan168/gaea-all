package com.gaea.common.web.controller;

import com.gaea.common.web.xuser.XUser;
import com.gaea.common.web.xuser.XUserSession;

/**
 * Created by Fe on 15/6/26.
 */
public class BaseUserController extends BaseController {


    public void refreshXUser(XUser xUser) {
        XUserSession.getCurrent().setXUser(xUser);
    }
}

