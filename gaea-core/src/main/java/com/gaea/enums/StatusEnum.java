package com.gaea.enums;

/**
 * Created by tiantiea on 16/4/21.
 */
public enum StatusEnum {

    enabled("enabled", "启用"), disabled("disabled", "禁用");

    private String code;
    private String name;

    private StatusEnum(String code, String name) {
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
    public static StatusEnum fromCode(String code) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;

    }

}
