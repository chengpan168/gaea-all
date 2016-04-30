package com.gaea.common.web;

import org.apache.commons.lang3.StringEscapeUtils;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Created by panwang.chengpw on 6/27/15.
 */
public class JsonStringValueFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (value == null) {
            return value;
        }

        if (value instanceof String) {
            return StringEscapeUtils.escapeHtml4(String.valueOf(value));
        }
        return value;
    }
}
