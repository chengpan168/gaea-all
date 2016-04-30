package com.gaea.common.aliyun.oss;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Fe on 15/7/3.
 */
public enum FileType {

    IMAGE("image"),ATTACHMENT("attachment");

    FileType(String type) {
        this.type = type;
    }

    private String type;

    public static FileType getFileType(String fileType) {
        if (StringUtils.startsWithIgnoreCase(fileType, "image")) {
            return FileType.IMAGE;
        }

        return FileType.ATTACHMENT;

    }
}
