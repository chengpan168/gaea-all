package com.gaea.admin.web.controller.open;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gaea.dto.SessionDto;
import com.gaea.entity.GaeaSession;
import com.gaea.entity.User;
import com.gaea.service.SessionService;
import com.gaea.service.UserService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gaea.common.result.AjaxResult;
import com.gaea.common.web.controller.BaseController;
import com.gaea.dto.UserDto;

import javax.annotation.Resource;

/**
 * Created by chengpanwang on 4/15/16.
 */

@Controller
@RequestMapping("/open")
public class OpenApiController extends BaseController {


    @Resource
    private SessionService sessionService;
    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping("/user/info.json")
    public AjaxResult userInfo(String appName, String ticket) {
        AjaxResult ajaxResult = newAjaxResult();

        GaeaSession session = sessionService.get(appName, ticket);

        User user = userService.queryByStaffId(session.getStaffId());
        // 测试数据

        ajaxResult.setData(user);

        return ajaxResult;
    }


    @ResponseBody
    @RequestMapping("/user/detail.json")
    public AjaxResult userDetail(String appName, String ticket) {
        AjaxResult ajaxResult = new AjaxResult();

        GaeaSession session = sessionService.get(appName, ticket);

        if (session == null || session.getStaffId() == null) {
            return ajaxResult;
        }

        UserDto userDto = userService.queryDetail(appName, session.getStaffId());

        ajaxResult.setData(userDto);

        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/sync.json")
    public AjaxResult sync(String appName, String sessionStatus) {
        AjaxResult ajaxResult = newAjaxResult();

        logger.info("session 同步, appName:{}", appName);
        logger.debug("session 同步, 数据:{}", sessionStatus);


        if (StringUtils.isBlank(sessionStatus)) {
            return ajaxResult;
        }

        Map<String, Long> statusMap = JSON.parseObject(sessionStatus, Map.class);


        SessionDto sessionDto = sessionService.sync(appName, statusMap);

        return ajaxResult;
    }
}
