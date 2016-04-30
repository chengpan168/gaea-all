package com.gaea.admin.web.controller;

import com.gaea.common.query.Pagination;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.UrlDto;
import com.gaea.entity.Url;
import com.gaea.enums.StatusEnum;
import com.gaea.mapper.query.AppQuery;
import com.gaea.mapper.query.UrlQuery;
import com.gaea.service.AppService;
import com.gaea.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by tiantiea on 16/4/26.
 */
@RequestMapping("/url")
@Controller
public class UrlController extends BaseController {
    
    @Resource
    private UrlService urlService;
    @Resource
    private AppService appService;


    @RequestMapping("/list.htm")
    public ModelAndView toList() {
        ModelAndView mv = newModelAndView("/url/urlList");
        mv.addObject("urlStatus", StatusEnum.values());
        return mv;
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public AjaxResult list(UrlQuery query) {
        Pagination<UrlDto> appPage = urlService.listUrlPage(query);
        return newAjaxResult(appPage);
    }


    @RequestMapping("/add.htm")
    public ModelAndView add() {
        ModelAndView mv = newModelAndView("/url/urlEdit");
        mv.addObject("urlStatus", StatusEnum.values());
        AppQuery appQuery = new AppQuery();
        appQuery.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(appQuery));

        return mv;
    }

    @RequestMapping("/insert.json")
    @ResponseBody
    public AjaxResult insert(Url url) {
        // todo
        url.setCreatorId(2L);
        urlService.insert(url);
        return newAjaxResult();
    }

    @RequestMapping("/edit.htm")
    public ModelAndView edit(Long id) {
        ModelAndView mv = newModelAndView("/url/urlEdit");

        mv.addObject("url", urlService.getById(id));

        mv.addObject("urlStatus", StatusEnum.values());

        AppQuery query = new AppQuery();
        query.setStatus(StatusEnum.enabled.getCode());
        mv.addObject("apps", appService.listApp(query));

        return mv;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public AjaxResult update(Url url) {
        urlService.update(url);
        return newAjaxResult();
    }

}
