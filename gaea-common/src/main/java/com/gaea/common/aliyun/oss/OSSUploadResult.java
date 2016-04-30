package com.gaea.common.aliyun.oss;

public class OSSUploadResult {
    private String fileName;
    private String ossfileName;
    private String ossLocation;
    private String ossBucKet;

    private String url;

    public OSSUploadResult() {}

    public OSSUploadResult(String fileName, String ossfileName, String ossLocation, String ossBucKet, String url) {
        this.fileName = fileName;
        this.ossfileName = ossfileName;
        this.ossLocation = ossLocation;
        this.ossBucKet = ossBucKet;
        this.url = url;
    }

    public String getOssLocation() {
        return ossLocation;
    }

    public void setOssLocation(String ossLocation) {
        this.ossLocation = ossLocation;
    }

    public String getOssBucKet() {
        return ossBucKet;
    }

    public void setOssBucKet(String ossBucKet) {
        this.ossBucKet = ossBucKet;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOssfileName() {
        return ossfileName;
    }

    public void setOssfileName(String ossfileName) {
        this.ossfileName = ossfileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
