package com.gaea.common.web.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.gaea.common.web.JsonStringValueFilter;
import com.gaea.common.web.context.RequestContext;

/**
 * 返回json时使用, 支持json , jsonp, 需配合RequestContext 使用, 注意
 *
 * Created by panwang.chengpw on 5/8/15.
 */
public class JsonMessageConverter extends FastJsonHttpMessageConverter {

    private static final Logger logger              = LoggerFactory.getLogger(JsonMessageConverter.class);
    private static String       CALLBACK_REGEXP     = "[^0-9a-zA-Z_\\.]";
    private static int          CALLBACK_MAX_LENGTH = 128;
    private static Pattern      PATTERN             = Pattern.compile(CALLBACK_REGEXP);
    private static String       JSON_HEADER_APPEND  = "\r\n\r\n";
    private static String       CHAR_SET            = "UTF-8";
    private static ValueFilter  filter              = new JsonStringValueFilter();

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        OutputStream out = outputMessage.getBody();
        String text;

        String callback = RequestContext.getStr("callback");
        RequestContext.getResponse().setCharacterEncoding(CHAR_SET);
        RequestContext.getResponse().setHeader("Cache-Control", "no-cache");

        if (StringUtils.isNotBlank(callback)) {
            callback = StringEscapeUtils.escapeHtml4(callbackFilter(callback));
            RequestContext.getResponse().setHeader("Content-Type", "application/javascript");
            text = JSON_HEADER_APPEND + callback + "(" + getJson(obj) + ");";
        } else {
            RequestContext.getResponse().setHeader("Content-Type", "application/json");
            text = JSON_HEADER_APPEND + getJson(obj);
        }

        logger.info("request seq:{}, response json: {}", RequestContext.getSeq(), text);

        byte[] bytes = text.getBytes(getCharset());
        out.write(bytes);
    }

    private String getJson(Object obj) {
        boolean isEscape = RequestContext.getBoolean("isEscape", true);
        if (isEscape) {
            return JSON.toJSONString(obj, filter, getFeatures());
        }
        return JSON.toJSONString(obj, getFeatures());
    }

    public static String callbackFilter(String callback) {
        if (StringUtils.isEmpty(callback)) {
            return StringUtils.EMPTY;
        }

        String filterCallback = callback;

        if (StringUtils.length(filterCallback) > CALLBACK_MAX_LENGTH) {
            filterCallback = filterCallback.substring(0, CALLBACK_MAX_LENGTH);
        }

        Matcher m = PATTERN.matcher(filterCallback);

        filterCallback = m.replaceAll("");

        if (!StringUtils.equals(callback, filterCallback)) {
            logger.error("callback was filter, callback:" + callback + ",filterCallcack:" + filterCallback);
        }

        return filterCallback;
    }
}
