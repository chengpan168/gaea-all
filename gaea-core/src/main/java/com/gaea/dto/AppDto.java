package com.gaea.dto;

import com.gaea.entity.App;
import com.gaea.enums.StatusEnum;

/**
 * Created by tiantiea on 16/4/27.
 */
public class AppDto extends App {

    private String ownerName;
    private String statusName;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatusName() {
        StatusEnum statusEnum = StatusEnum.fromCode(getStatus());
        if (statusEnum == null) {
            return null;
        }
        return statusEnum.getName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
