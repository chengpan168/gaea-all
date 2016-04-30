package com.gaea.common.web.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * Created by chengpanwang on 7/17/15.
 */
public class RequestContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("request context filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        long start = System.currentTimeMillis();
        RequestContext.init(request, response, null);

        logger.info("request start| seq:{}, uri:{}, param:{}", RequestContext.getSeq(), request.getRequestURI(), request.getParameterMap());
        logger.info("request seq:{}, refer:{}, cookie:{}", RequestContext.getSeq(), request.getHeader("Referer"), JSON.toJSONString(request.getCookies()));

        chain.doFilter(servletRequest, servletResponse);

        logger.info("request end| seq:{}, spend time:{} ms", RequestContext.getSeq(), (System.currentTimeMillis() - start));

        RequestContext.clear();

    }

    @Override
    public void destroy() {
        logger.info("request context filter destroy");
    }
}
