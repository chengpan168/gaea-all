package com.gaea.common.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gaea.common.annotation.CsrfTokenAnnotation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gaea.common.cache.RedisCache;
import com.gaea.common.http.HttpUtils;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.result.AjaxResultStatusEnum;
import com.gaea.common.util.FastJson;
import com.gaea.common.web.xuser.XUserSession;

/**
 * Created by panwang.chengpw on 1/22/15.
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {

    private Logger        logger     = LoggerFactory.getLogger(CsrfInterceptor.class);
    private static String CSRF_TOKEN = "csrfToken";
    private boolean       debugMode  = true;
    private List<String>  excludePath;
    @Resource
    private RedisCache    redisCache;

    private String        prefix     = "token_";
    private int           maxTimes   = 4;
    private int           expire     = 60 * 60 * 12;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("");
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CsrfTokenAnnotation csrfTokenAnnotation = handlerMethod.getMethod().getAnnotation(CsrfTokenAnnotation.class);
        if (csrfTokenAnnotation != null && csrfTokenAnnotation.checked()) {
            boolean isPass = checkCsrfToken(request);
            if (!isPass) {
                if (HttpUtils.isXhr(request)) {
                    printAuthError(response);
                } else {
                    response.sendRedirect("/home.htm");
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private boolean checkCsrfToken(HttpServletRequest request) {
        return StringUtils.equals(request.getParameter(CSRF_TOKEN), XUserSession.getCurrent().getCsrfToken());
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private void printAuthError(HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR.ERROR);
            ajaxResult.setMessage("非法请求");
            writer.write(FastJson.toJson(ajaxResult));
        } catch (IOException e) {
            //TODO ignore
        } finally {
            if (writer != null) IOUtils.closeQuietly(writer);
        }

    }

    private String buildKey(String phone) {
        return prefix + phone;
    }
}
