package com.gaea.admin.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gaea.common.query.Pagination;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.UserDto;
import com.gaea.mapper.query.UserQuery;
import com.gaea.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

@Controller
public class HomeController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/home.htm")
    public ModelAndView home(@RequestParam(value = "name", required = false) List<String> nameList, UserQuery query) {
        ModelAndView mv = new ModelAndView();

        return mv;
    }
}
