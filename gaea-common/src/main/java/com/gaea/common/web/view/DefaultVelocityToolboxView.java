package com.gaea.common.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.velocity.VelocityView;

/**
 * Created by panwang.chengpw on 2016/3/3.
 */
public class DefaultVelocityToolboxView extends VelocityView {

    private static final Logger logger = LoggerFactory.getLogger(DefaultVelocityToolboxView.class);

    public DefaultVelocityToolboxView() {
        logger.info("初始化velocity tool box view ");
    }

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (getApplicationContext().containsLocalBean("velocityToolboxBean")) {
            VelocityToolboxBean velocityToolboxBean = (VelocityToolboxBean) getApplicationContext().getBean("velocityToolboxBean");
            model.putAll(velocityToolboxBean.getTools());

        }

        // Create a velocityContext instance.
        VelocityContext context = new VelocityContext(model);
        return context;
    }

}
