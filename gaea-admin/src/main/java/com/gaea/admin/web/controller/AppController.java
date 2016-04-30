package com.gaea.admin.web.controller;

import com.gaea.common.query.Pagination;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.AppDto;
import com.gaea.entity.App;
import com.gaea.enums.StatusEnum;
import com.gaea.enums.UserStatusEnum;
import com.gaea.mapper.query.AppQuery;
import com.gaea.mapper.query.UserQuery;
import com.gaea.service.AppService;
import com.gaea.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by tiantiea on 16/4/21.
 */
@RequestMapping("/app")
@Controller
public class AppController extends BaseController {

    @Resource
    private AppService appService;
    @Resource
    private UserService userService;

    @RequestMapping("/list.htm")
    public ModelAndView toList() {

        ModelAndView mv = newModelAndView("/app/appList");
        return mv;
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public AjaxResult list(AppQuery query) {
        Pagination<AppDto> appPage = appService.listAppPage(query);
        return newAjaxResult(appPage);
    }

    @RequestMapping("/add.htm")
    public ModelAndView add() {
        ModelAndView mv = newModelAndView("/app/appEdit");
        mv.addObject("appStatus", StatusEnum.values());

        UserQuery query = new UserQuery();
        query.setStatus(UserStatusEnum.normal.getCode());
        mv.addObject("users", userService.listUser(query));
        return mv;
    }

    @RequestMapping("/insert.json")
    @ResponseBody
    public AjaxResult insert(App app) {
        // todo
        app.setCreatorId(2L);
        appService.insert(app);
        return newAjaxResult();
    }

    @RequestMapping("/edit.htm")
    public ModelAndView edit(Long id) {
        ModelAndView mv = newModelAndView("/app/appEdit");
        mv.addObject("appStatus", StatusEnum.values());
        mv.addObject("app", appService.getById(id));
        UserQuery query = new UserQuery();
        query.setStatus(UserStatusEnum.normal.getCode());
        mv.addObject("users", userService.listUser(query));
        return mv;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public AjaxResult update(App app) {
        appService.update(app);
        return newAjaxResult();
    }

    @RequestMapping("/queryByName.json")
    @ResponseBody
    public AjaxResult queryByName(String name) {
        return newAjaxResult(appService.queryByName(name));
    }
}
