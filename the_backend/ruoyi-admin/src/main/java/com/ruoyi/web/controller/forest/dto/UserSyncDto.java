package com.ruoyi.web.controller.forest.dto;

public class UserSyncDto {
    private String iv;
    private String userInfo;

    private String retcode;

    private String message;


    public UserSyncDto() {
    }


    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserSyncDto(String iv, String userInfo, String retcode, String message) {
        this.iv = iv;
        this.userInfo = userInfo;
        this.retcode = retcode;
        this.message = message;
    }

    public UserSyncDto(String iv, String userInfo) {
        this.iv = iv;
        this.userInfo = userInfo;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserSyncDto{" +
                "iv='" + iv + '\'' +
                ", userInfo=" + userInfo +
                ", retcode='" + retcode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
