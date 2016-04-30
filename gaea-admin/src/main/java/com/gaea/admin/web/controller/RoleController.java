package com.gaea.admin.web.controller;

import com.gaea.common.query.Pagination;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.RoleDto;
import com.gaea.entity.App;
import com.gaea.entity.Role;
import com.gaea.enums.StatusEnum;
import com.gaea.mapper.query.AppQuery;
import com.gaea.mapper.query.RoleQuery;
import com.gaea.service.AppService;
import com.gaea.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by tiantiea on 16/4/22.
 */
@RequestMapping("/role")
@Controller
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;
    @Resource
    private AppService appService;

    @RequestMapping("/list.htm")
    public ModelAndView toList() {
        ModelAndView mv = newModelAndView("/role/roleList");

        mv.addObject("roleStatus", StatusEnum.values());
        mv.addObject("apps", appService.listApp(null));

        return mv;
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public AjaxResult list(RoleQuery query) {
        Pagination<RoleDto> appPage = roleService.listRolePage(query);
        return newAjaxResult(appPage);
    }


    @RequestMapping("/add.htm")
    public ModelAndView add() {
        ModelAndView mv = newModelAndView("/role/roleEdit");
        mv.addObject("roleStatus", StatusEnum.values());
        AppQuery appQuery = new AppQuery();
        appQuery.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(appQuery));

        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("roles", roleService.listRole(roleQuery));
        return mv;
    }

    @RequestMapping("/insert.json")
    @ResponseBody
    public AjaxResult insert(Role role) {
        // todo
        role.setCreatorId(2L);
        roleService.insert(role);
        return newAjaxResult();
    }

    @RequestMapping("/edit.htm")
    public ModelAndView edit(Long id) {
        ModelAndView mv = newModelAndView("/role/roleEdit");

        mv.addObject("role", roleService.getById(id));

        mv.addObject("roleStatus", StatusEnum.values());

        AppQuery query = new AppQuery();
        query.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(query));

        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("roles", roleService.listRole(roleQuery));

        return mv;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public AjaxResult update(Role role) {
        roleService.update(role);
        return newAjaxResult();
    }

}
