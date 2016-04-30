package com.gaea.admin.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.gaea.dto.AppDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaea.common.constant.Constant;
import com.gaea.common.query.Pagination;
import com.gaea.common.util.Converter;
import com.gaea.common.util.CryptoUtil;
import com.gaea.common.web.Cookies;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.UserDto;
import com.gaea.entity.App;
import com.gaea.mapper.query.AppQuery;
import com.gaea.service.AppService;
import com.gaea.service.SessionService;
import com.gaea.service.UserService;

/**
 * Created by chengpanwang on 4/18/16.
 */
@Controller
public class LoginController extends BaseController {

    String                 loginUrl = "redirect:/login.htm";

    @Resource
    private UserService    userService;
    @Resource
    private AppService     appService;
    @Resource
    private SessionService sessionService;

    @RequestMapping("/login.htm")
    public ModelAndView login(String appName, String backUrl, UserDto user, HttpServletResponse response) {
        ModelAndView mv = newModelAndView("/login");

        mv.addObject("appName", appName);
        mv.addObject("backUrl", backUrl);

        if (user == null) {
            user = new UserDto();
        }

        if (user.getStaffId() == null) {
            user.setStaffId(Converter.convert(Cookies.getCookie(Constant.COOKIE_NAME), Long.class));
            user.setPassword(CryptoUtil.decryptAES(Cookies.getCookie(Constant.COOKIE_PASS)));
        }

        if (user.getStaffId() == null || StringUtils.isBlank(user.getPassword())) {
            return mv;
        }

        if (!userService.login(user)) {
            return mv;
        }

        // 登录成功了, 肯定要加个session吧
        sessionService.putNew(appName, user);

        // 重定向到应用页面
        if (StringUtils.isNotBlank(backUrl)) {
            mv.setViewName(buildUrl(backUrl, user));

            return mv;
        }

        App app = appService.queryByName(appName);

        if (app != null) {
            mv.setViewName(buildUrl(app.getAppUrl(), user));
        } else {
            AppQuery query = new AppQuery();
            query.setPageSize(Integer.MAX_VALUE);
            Pagination<AppDto> apps = appService.listAppPage(query);

            mv.addObject("apps", apps.getData());
            mv.setViewName("/apps");
        }
        return mv;
    }

    @RequestMapping("/logoutAll.htm")
    public ModelAndView logout(String backUrl, String appName) {

        ModelAndView mv = newModelAndView();
        mv.addObject("backUrl", backUrl);

        // 退出了, 把session 清理掉, 不要再浪费内存了
        String ticket = Cookies.getCookie(Constant.COOKIE_TICKET);
        if (StringUtils.isNotBlank(ticket)) {
            sessionService.clean(appName, ticket);
        }

        Cookies.setCookie(Constant.COOKIE_NAME, StringUtils.EMPTY);
        Cookies.setCookie(Constant.COOKIE_PASS, StringUtils.EMPTY);
        Cookies.setCookie(Constant.COOKIE_TICKET, StringUtils.EMPTY);

        if (StringUtils.isNotBlank(backUrl)) {
            mv.setViewName("redirect:" + backUrl);

            return mv;
        }

        App app = appService.queryByName(appName);

        mv.setViewName("redirect:" + app.getAppUrl());
        return mv;

    }

    @RequestMapping("/denied.htm")
    public String denied(String backUrl, String appName) {

        return "/denied";

    }

    private String buildUrl(String url, UserDto user) {
        String param;
        if (url.contains("?")) {
            param = "&ticket=" + user.getTicket();
        } else {
            param = "?ticket=" + user.getTicket();
        }
        return "redirect:" + url + param;
    }
}
