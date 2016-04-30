package com.gaea.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 *
 */
public class FastJson {

    public static final Logger logger = LoggerFactory.getLogger(FastJson.class);

    public static SerializeConfig mapping = new SerializeConfig();

    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer(DEFAULT_DATE_FORMAT));
    }

    public static <T> String toJson(T t) {
        return JSON.toJSONString(t, mapping);
    }

    public static <T> T fromJson(String json,Class<T> t) {
        return (T) JSON.parseObject(json, t);
    }

    public static JSONObject fromJson(String json) {
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


    public static <T> List<T> jsonToList(String json,Class<T> t) {
        return (List<T>) JSON.parseArray(json, t);
    }

    public static JSONArray jsonToList(String json) {
        return JSON.parseArray(json);
    }

}
