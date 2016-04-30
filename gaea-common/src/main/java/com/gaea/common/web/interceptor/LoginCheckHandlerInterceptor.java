package com.gaea.common.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gaea.common.result.AjaxResult;
import com.gaea.common.result.AjaxResultStatusEnum;
import com.gaea.common.web.xuser.XUser;
import com.gaea.common.web.xuser.XUserSession;
import com.gaea.common.http.HttpUtils;
import com.gaea.common.util.FastJson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by Fe on 15/6/25.
 */
public class LoginCheckHandlerInterceptor extends HandlerInterceptorAdapter {

    public static final Logger logger = LoggerFactory.getLogger(LoginCheckHandlerInterceptor.class);

    public static final String CHECK_LOGIN_TYPE_XML = "xml";
    public static final String CHECK_LOGIN_TYPE_ANNOTATION = "annotation";

    public static final String DEFAULT_CHECK_LOGIN_TYPE = CHECK_LOGIN_TYPE_XML;


    private List<String> unCheckUrlList = new ArrayList<String>();

    private List<String> unCheckPatternUrlList = new ArrayList<String>();

    private String loginUri;

    private String checkLoginType = DEFAULT_CHECK_LOGIN_TYPE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUri = request.getRequestURI();
        logger.info("check uri : {}", requestUri);
        if (unCheckUrlList.contains(requestUri) || patternMatch(requestUri)) {
            return true;
        } else {
            //TODO
            boolean hasLogin = hashLogin();
            //TODO 区分ajax请求和同步请求
            if (!hasLogin) {
                if (HttpUtils.isXhr(request)) {
                    printNoLoginResult(response);
                } else {
                    response.sendRedirect(loginUri + "?backUrl=" + requestUri + getParam(request));
                }
                return false;
            } else {
                return true;
            }
        }

    }


    private void printNoLoginResult(HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
            ajaxResult.setData(loginUri);
            writer.write(FastJson.toJson(ajaxResult));
        } catch (IOException e) {
            //TODO ignore
        } finally {
            if (writer != null)
                IOUtils.closeQuietly(writer);
        }

    }

    private String getParam(HttpServletRequest request) {
        String param = "";
        StringBuilder sb = new StringBuilder();
        Enumeration pNames=request.getParameterNames();
        while(pNames.hasMoreElements()){
            String name = (String) pNames.nextElement();
            sb.append(name).append("=").append(request.getParameter(name)).append("&");
        }
        if (!StringUtils.isEmpty(sb)) {
            param = "?" + sb.substring(0, sb.length() - 1);
        }
        return param;
    }

    /**
     * 判断是否已经登录
     */
    private boolean hashLogin() {
        XUser xUser = XUserSession.getCurrent().getXUser();
        return xUser.isSignedIn();
    }

    public List<String> getUnCheckUrlList() {
        return unCheckUrlList;
    }

    public void setUnCheckUrlList(List<String> unCheckUrlList) {
        this.unCheckUrlList = unCheckUrlList;
    }

    public List<String> getUnCheckPatternUrlList() {
        return unCheckPatternUrlList;
    }

    public void setUnCheckPatternUrlList(List<String> unCheckPatternUrlList) {
        this.unCheckPatternUrlList = unCheckPatternUrlList;
    }

    public boolean patternMatch(String requestUri) {
        boolean flag = false;
        for (String patternUri : unCheckPatternUrlList) {
            Pattern pattern = Pattern.compile(patternUri);
            Matcher matcher = pattern.matcher(requestUri);
            if (matcher.matches())  {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public String getLoginUri() {
        return loginUri;
    }

    public void setLoginUri(String loginUri) {
        this.loginUri = loginUri;
    }

    public String getCheckLoginType() {
        return checkLoginType;
    }

    public void setCheckLoginType(String checkLoginType) {
        this.checkLoginType = checkLoginType;
    }
}
