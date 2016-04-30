/**
 * 
 */
package com.gaea.service.helper;

/**
 * @author Nichole
 *
 *         2015年3月3日
 */
public class Authentication {

    private String authUrl;
    private String appId;
    private String appSecret;
    private String modifyPassUrl;
    private String privilegeUrl;
    private String privilegeRegId;
    private String areaPrivilegeUrl;
    private String shopPrivilegeUrl;

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getPrivilegeUrl() {
        return privilegeUrl;
    }

    public void setPrivilegeUrl(String privilegeUrl) {
        this.privilegeUrl = privilegeUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getModifyPassUrl() {
        return modifyPassUrl;
    }

    public void setModifyPassUrl(String modifyPassUrl) {
        this.modifyPassUrl = modifyPassUrl;
    }

    public String getPrivilegeRegId() {
        return privilegeRegId;
    }

    public void setPrivilegeRegId(String privilegeRegId) {
        this.privilegeRegId = privilegeRegId;
    }

    public String getAreaPrivilegeUrl() {
        return areaPrivilegeUrl;
    }

    public void setAreaPrivilegeUrl(String areaPrivilegeUrl) {
        this.areaPrivilegeUrl = areaPrivilegeUrl;
    }

    public String getShopPrivilegeUrl() {
        return shopPrivilegeUrl;
    }

    public void setShopPrivilegeUrl(String shopPrivilegeUrl) {
        this.shopPrivilegeUrl = shopPrivilegeUrl;
    }

    @Override
    public String toString() {
        return "Authentication [authUrl=" + authUrl + ", appId=" + appId + ", appSecret=" + appSecret + ", modifyPassUrl=" + modifyPassUrl
               + ", privilegeUrl=" + privilegeUrl + ", privilegeRegId=" + privilegeRegId + ", areaPrivilegeUrl=" + areaPrivilegeUrl
               + ", shopPrivilegeUrl=" + shopPrivilegeUrl + "]";
    }

}
