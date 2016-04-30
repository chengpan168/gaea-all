package com.gaea.common.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.gaea.common.constant.Constant;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.result.AjaxResultStatusEnum;
import com.gaea.common.web.context.RequestContext;
import com.gaea.common.web.xuser.XUser;
import com.gaea.common.web.xuser.XUserSession;

/**
 * Created by Fe on 15/6/25.
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected AjaxResult newAjaxResult() {
        return new AjaxResult();
    }

    protected AjaxResult newAjaxResult(Object data) {
        return new AjaxResult(data);
    }

    protected ModelAndView newModelAndView() {
        return new ModelAndView();
    }

    protected ModelAndView newModelAndView(String viewName) {
        return new ModelAndView(viewName);
    }

    public XUser getXUser() {
        return XUserSession.getCurrent().getXUser();
    }

    @ExceptionHandler
    public void resolveException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        logger.error("handle exception:", e);
        String uri = request.getRequestURI();
        if (StringUtils.endsWith(uri, ".json")) {
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
            ServletOutputStream out = response.getOutputStream();
            IOUtils.write((JSON.toJSONString(ajaxResult).getBytes(Constant.DEFAULT_CHARSET)), out);
        } else {
            response.sendRedirect("/error/500.htm");
        }
    }

    public void write(String text) {
        try {
            PrintWriter writer = RequestContext.getResponse().getWriter();
            writer.write(text);

            IOUtils.closeQuietly(writer);
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
