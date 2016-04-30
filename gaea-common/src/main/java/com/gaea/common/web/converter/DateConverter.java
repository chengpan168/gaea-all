package com.gaea.common.web.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fe on 15/6/25.
 */
public class DateConverter implements Converter<String, Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormatSimple = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger log = LoggerFactory.getLogger(DateConverter.class);


    @Override
    public Date convert(String source) {

        Date result = null;
        dateFormat.setLenient(false);
        try {
            result = dateFormat.parse(source);
        } catch (ParseException e) {

        }
        if(result == null){
            try {
                result = dateFormatSimple.parse(source);
            } catch (ParseException e) {

            }
        }
        if(result == null){
            log.error("日期转换错误！");
        }
        return result;
    }
}
