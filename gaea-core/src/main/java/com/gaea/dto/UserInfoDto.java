package com.gaea.dto;

import com.gaea.entity.User;
import com.gaea.enums.UserStatusEnum;

/**
 * Created by tiantiea on 16/4/27.
 */
public class UserInfoDto extends User {

    private String statusName;


    public String getStatusName() {
        UserStatusEnum statusEnum = UserStatusEnum.fromCode(getStatus());
        if (statusEnum == null) {
            return null;
        }
        return statusEnum.getName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
