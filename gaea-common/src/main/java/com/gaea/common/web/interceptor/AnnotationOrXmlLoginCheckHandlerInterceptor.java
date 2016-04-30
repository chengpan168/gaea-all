package com.gaea.common.web.interceptor;

import com.gaea.common.annotation.LoginCheckAnnotation;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.result.AjaxResultStatusEnum;
import com.gaea.common.web.xuser.XUser;
import com.gaea.common.web.xuser.XUserSession;
import com.gaea.common.http.HttpUtils;
import com.gaea.common.util.FastJson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fe on 15/8/15.
 */
public class AnnotationOrXmlLoginCheckHandlerInterceptor extends HandlerInterceptorAdapter {
    public static final Logger logger = LoggerFactory.getLogger(LoginCheckHandlerInterceptor.class);

    public static final String CHECK_LOGIN_TYPE_XML = "xml";
    public static final String CHECK_LOGIN_TYPE_ANNOTATION = "annotation";

    public static final String DEFAULT_CHECK_LOGIN_TYPE = CHECK_LOGIN_TYPE_XML;


    private List<String> checkUrlList = new ArrayList<String>();

    private List<String> checkPatternUrlList = new ArrayList<String>();

    private String loginUri;

    private String checkLoginType = DEFAULT_CHECK_LOGIN_TYPE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (checkLogin(request,handler)) {
            return true;
        } else {
            //TODO
            boolean hasLogin = hashLogin();
            //TODO 区分ajax请求和同步请求
            if (!hasLogin) {
                if (HttpUtils.isXhr(request)) {
                    printNoLoginResult(response);
                } else {
                    response.sendRedirect(loginUri + "?backUrl=" + request.getRequestURI());
                }
                return false;
            } else {
                return true;
            }
        }

    }

    private boolean checkLogin(HttpServletRequest request,Object handler) {
        String requestUri = request.getRequestURI();
        logger.info("check uri : {}", requestUri);
        if (checkLoginType.equals(CHECK_LOGIN_TYPE_XML)) {
            return !checkUrlList.contains(requestUri) || !patternMatch(requestUri);
        }

        if (checkLoginType.equals(CHECK_LOGIN_TYPE_ANNOTATION)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginCheckAnnotation loginCheckAnnotation = handlerMethod.getMethod().getAnnotation(LoginCheckAnnotation.class);

            if (loginCheckAnnotation == null) {
                return true;
            } else {
                return !loginCheckAnnotation.checked();
            }
        }
        return false;

    }

    private void printNoLoginResult(HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
            ajaxResult.setMessage("请先登录");
            ajaxResult.setData(loginUri);
            writer.write(FastJson.toJson(ajaxResult));
        } catch (IOException e) {
            //TODO ignore
        } finally {
            if (writer != null)
                IOUtils.closeQuietly(writer);
        }

    }

    /**
     * 判断是否已经登录
     */
    private boolean hashLogin() {
        XUser xUser = XUserSession.getCurrent().getXUser();
        return xUser.isSignedIn();
    }

    public List<String> getCheckUrlList() {
        return checkUrlList;
    }

    public void setCheckUrlList(List<String> checkUrlList) {
        this.checkUrlList = checkUrlList;
    }

    public List<String> getCheckPatternUrlList() {
        return checkPatternUrlList;
    }

    public void setCheckPatternUrlList(List<String> checkPatternUrlList) {
        this.checkPatternUrlList = checkPatternUrlList;
    }

    public boolean patternMatch(String requestUri) {
        boolean flag = false;
        for (String patternUri : checkPatternUrlList) {
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
