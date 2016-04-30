package com.gaea.enums;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by chengpanwang on 1/24/16.
 */
public enum SexEnum {

    women("female", "女"), man("male", "男"), undefined("", StringUtils.EMPTY);

    private String code;
    private String name;

    private SexEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    /**
     * 从code转换成枚举 , 永远不返回null
     * @param code
     * @return
     */
    public static SexEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return undefined;
        }
        for (SexEnum sexEnum : SexEnum.values()) {
            if (StringUtils.equals(sexEnum.getCode(), code)) {
                return sexEnum;
            }
        }

        return undefined;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
