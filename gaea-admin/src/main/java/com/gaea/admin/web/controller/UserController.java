package com.gaea.admin.web.controller;

import java.util.Arrays;

import javax.annotation.Resource;

import com.gaea.common.query.Pagination;
import com.gaea.dto.UserDto;
import com.gaea.dto.UserInfoDto;
import com.gaea.entity.User;
import com.gaea.enums.SexEnum;
import com.gaea.enums.UserStatusEnum;
import com.gaea.mapper.query.UserQuery;
import com.gaea.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;

@RequestMapping("/user")
@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/list.htm")
    public ModelAndView toList() {

        ModelAndView mv = newModelAndView("/user/userList");
        return mv;
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public AjaxResult list(UserQuery userQuery) {
        Pagination<UserInfoDto> userPage = userService.listUserPage(userQuery);
        return newAjaxResult(userPage);
    }

    @RequestMapping("/add.htm")
    public ModelAndView add() {
        ModelAndView mv = newModelAndView("/user/userEdit");
        mv.addObject("userStatus", UserStatusEnum.values());
        return mv;
    }

    @RequestMapping("/insert.json")
    @ResponseBody
    public AjaxResult insert(User user) {
        userService.insert(user);
        return newAjaxResult();
    }

    @RequestMapping("/edit.htm")
    public ModelAndView edit(Long id) {
        ModelAndView mv = newModelAndView("/user/userEdit");
        mv.addObject("userStatus", UserStatusEnum.values());
        mv.addObject("user", userService.getById(id));
        return mv;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public AjaxResult update(User user) {
        user.setStaffId(null);
        userService.update(user);
        return newAjaxResult();
    }

    @RequestMapping("/queryByStaffId.json")
    @ResponseBody
    public AjaxResult queryByStaffId(Long staffId) {
        return newAjaxResult(userService.queryByStaffId(staffId));
    }
}
