package com.gaea.common.web.view;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Created by chengpanwang on 3/18/16.
 */
public class VelocityToolboxBean {

    private static final Logger logger = LoggerFactory.getLogger(VelocityToolboxBean.class);

    protected Map<String, Object> tools;

    public VelocityToolboxBean() {
        tools = Maps.newHashMap();
        tools.put("stringTool", StringUtils.class);
        tools.put("collectionTool", CollectionUtils.class);
        tools.put("dateFormatTool", DateFormatTool.class);

    }

    public Map<String, Object> getTools() {
        return tools;
    }

    public void setTools(Map<String, String> tools) {
        if (MapUtils.isEmpty(tools)) {
            return;
        }

        for (Map.Entry<String, String> entry : tools.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            logger.info("载入velocty tool 类 : {}", entry.getValue());
            try {
                Class toolClass = ClassUtils.getClass(entry.getValue().toString());
                this.tools.put(entry.getKey(), toolClass);
            } catch (ClassNotFoundException e) {
                logger.error("没有找到velocty tool 类, 类名:{}", entry.getValue());

            }
        }
    }



}
