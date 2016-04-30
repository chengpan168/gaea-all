package com.gaea.common.web.controller;

import com.gaea.common.http.RandomValidateCode;
import com.gaea.common.result.AjaxResult;
import com.gaea.common.result.AjaxResultStatusEnum;
import com.gaea.common.web.context.RequestContext;
import com.gaea.common.web.xuser.XUserSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by panwang.chengpw on 7/2/15.
 */
@Controller
@RequestMapping("/common")
public class RandomCodeController extends BaseUserController {

    private RandomValidateCode randomValidateCode = new RandomValidateCode();

    @RequestMapping("/randCode.htm")
    @ResponseBody
    public void randomCode() {
        randomValidateCode.randCode(RequestContext.getRequest(), RequestContext.getResponse());
        logger.info(XUserSession.getCurrent().getRandomCode());

    }

    @RequestMapping("/checkCode.json")
    @ResponseBody
    public AjaxResult checkCode(String reqCode){
        String innerCode=XUserSession.getCurrent().getRandomCode();
        AjaxResult ajaxResult=newAjaxResult();
        if(!StringUtils.equals(reqCode,innerCode )){
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
        }
        return ajaxResult;
    }
}
