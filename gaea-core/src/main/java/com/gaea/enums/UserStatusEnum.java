package com.gaea.enums;

/**
 * Created by tiantiea on 16/4/20.
 */
public enum UserStatusEnum {

    normal("normal", "正常"), holiday("holiday", "休假"), dimission("dimission", "离职");

    private String code;
    private String name;

    private UserStatusEnum(String code, String name) {
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
    public static UserStatusEnum fromCode(String code) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.getCode().equals(code)) {
                return userStatusEnum;
            }
        }
        return null;

    }

}
