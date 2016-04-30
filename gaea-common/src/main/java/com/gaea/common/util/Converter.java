package com.gaea.common.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by panwang.chengpw on 1/20/15.
 */
public class Converter {

    /**
     * 类型转换, 使用 #ConvertUtils.convert 方法, 增加了泛型
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object obj, Class<T> clazz) {
        return convert(obj, clazz, null);
    }

    public static <T> T convert(Object obj, Class<T> clazz, T defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        T rs = null;
        try {
            rs = (T) ConvertUtils.convert(obj, clazz);
        } catch (Exception e) {
            //ignore
        }

        if (rs == null) {
            return defaultValue;
        }

        return rs;
    }

    /**
     * 转换为String , 如果为null 返回 空字符串
     * @param obj
     * @return
     */
    public static String convert(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        return ConvertUtils.convert(obj);
    }

}
