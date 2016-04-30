package com.gaea.common.web.xuser;

import com.gaea.common.constant.Constant;
import com.gaea.common.util.CryptoUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by panwang.chengpw on 5/8/15.
 */
public class XUser {

    /**
     * 用户名
     */
    private String  userName;
    /**
     * 名称
     */
    private String  name;
    /**
     * 密码
     */
    private String  password;
    /**
     * 用户id
     */
    private Long    uid;
    /**
     * 是否登录
     */
    private boolean isSignedIn = false;
    /**
     * 是否自动登录
     */
    private boolean isSavePass = false;

    public XUser() {
    }

    public XUser(Long uid, String userName, String password, boolean isSavePass) {
        this.isSavePass = isSavePass;
        this.password = password;
        this.uid = uid;
        this.userName = userName;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setIsSignedIn(boolean isSignedIn) {
        this.isSignedIn = isSignedIn;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSavePass() {
        return isSavePass;
    }

    public void setIsSavePass(boolean isSavePass) {
        this.isSavePass = isSavePass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecPassword() {
        if (StringUtils.isBlank(getPassword())) {
            return StringUtils.EMPTY;
        }

        return CryptoUtil.encryptAES(getPassword());
    }
}
