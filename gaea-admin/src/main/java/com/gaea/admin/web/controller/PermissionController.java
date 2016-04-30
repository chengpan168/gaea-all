package com.gaea.admin.web.controller;

import com.gaea.common.query.Pagination;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.PermissionDto;
import com.gaea.entity.Permission;
import com.gaea.enums.StatusEnum;
import com.gaea.mapper.query.AppQuery;
import com.gaea.mapper.query.PermissionQuery;
import com.gaea.service.AppService;
import com.gaea.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by tiantiea on 16/4/26.
 */
@RequestMapping("/permission")
@Controller
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private AppService appService;


    @RequestMapping("/list.htm")
    public ModelAndView toList() {
        ModelAndView mv = newModelAndView("/permission/permissionList");
        mv.addObject("permissionStatus", StatusEnum.values());
        mv.addObject("apps", appService.listApp(null));
        return mv;
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public AjaxResult list(PermissionQuery query) {
        Pagination<PermissionDto> appPage = permissionService.listPermissionPage(query);
        return newAjaxResult(appPage);
    }


    @RequestMapping("/add.htm")
    public ModelAndView add() {
        ModelAndView mv = newModelAndView("/permission/permissionEdit");
        mv.addObject("permissionStatus", StatusEnum.values());
        AppQuery appQuery = new AppQuery();
        appQuery.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(appQuery));

        return mv;
    }

    @RequestMapping("/insert.json")
    @ResponseBody
    public AjaxResult insert(Permission permission) {
        // todo
        permission.setCreatorId(2L);
        permissionService.insert(permission);
        return newAjaxResult();
    }

    @RequestMapping("/edit.htm")
    public ModelAndView edit(Long id) {
        ModelAndView mv = newModelAndView("/permission/permissionEdit");

        mv.addObject("permission", permissionService.getById(id));

        mv.addObject("permissionStatus", StatusEnum.values());

        AppQuery query = new AppQuery();
        query.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(query));

        return mv;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public AjaxResult update(Permission permission) {
        permissionService.update(permission);
        return newAjaxResult();
    }

}
