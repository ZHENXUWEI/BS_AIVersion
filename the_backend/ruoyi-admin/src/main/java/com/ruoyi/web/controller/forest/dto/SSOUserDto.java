package com.ruoyi.web.controller.forest.dto;

import java.util.Date;

public class SSOUserDto {

    private String accountactiveday;

    private String accountstatu;

    private String businessCategory;

    private String cn;

    private String customAttr;

    private String deptment;

    private String deptmentTitle;

    private String description;

    private String externalAuth;

    private String fixphone;

    private String gender;

    private String headship;

    private String mail;

    private String mobile;

    private String ncdeptTitle;

    private String ncdeptment;

    private String pwdexpirationtime;

    private String qqId;

    private Date timestamp;

    private String uid;

    private String userno;

    private String account;
    private String pwd;

    public SSOUserDto() {
    }

    @Override
    public String toString() {
        return "SSOUserDto{" +
                "accountactiveday='" + accountactiveday + '\'' +
                ", accountstatu='" + accountstatu + '\'' +
                ", businessCategory='" + businessCategory + '\'' +
                ", cn='" + cn + '\'' +
                ", customAttr='" + customAttr + '\'' +
                ", deptment='" + deptment + '\'' +
                ", deptmentTitle='" + deptmentTitle + '\'' +
                ", description='" + description + '\'' +
                ", externalAuth='" + externalAuth + '\'' +
                ", fixphone='" + fixphone + '\'' +
                ", gender='" + gender + '\'' +
                ", headship='" + headship + '\'' +
                ", mail='" + mail + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ncdeptTitle='" + ncdeptTitle + '\'' +
                ", ncdeptment='" + ncdeptment + '\'' +
                ", pwdexpirationtime='" + pwdexpirationtime + '\'' +
                ", qqId='" + qqId + '\'' +
                ", timestamp=" + timestamp +
                ", uid='" + uid + '\'' +
                ", userno='" + userno + '\'' +
                ", account='" + account + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public SSOUserDto(String accountactiveday, String accountstatu, String businessCategory, String cn, String customAttr, String deptment, String deptmentTitle, String description, String externalAuth, String fixphone, String gender, String headship, String mail, String mobile, String ncdeptTitle, String ncdeptment, String pwdexpirationtime, String qqId, Date timestamp, String uid, String userno, String account, String pwd) {
        this.accountactiveday = accountactiveday;
        this.accountstatu = accountstatu;
        this.businessCategory = businessCategory;
        this.cn = cn;
        this.customAttr = customAttr;
        this.deptment = deptment;
        this.deptmentTitle = deptmentTitle;
        this.description = description;
        this.externalAuth = externalAuth;
        this.fixphone = fixphone;
        this.gender = gender;
        this.headship = headship;
        this.mail = mail;
        this.mobile = mobile;
        this.ncdeptTitle = ncdeptTitle;
        this.ncdeptment = ncdeptment;
        this.pwdexpirationtime = pwdexpirationtime;
        this.qqId = qqId;
        this.timestamp = timestamp;
        this.uid = uid;
        this.userno = userno;
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public SSOUserDto(String accountactiveday, String accountstatu, String businessCategory, String cn, String customAttr, String deptment, String deptmentTitle, String description, String externalAuth, String fixphone, String gender, String headship, String mail, String mobile, String ncdeptTitle, String ncdeptment, String pwdexpirationtime, String qqId, Date timestamp, String uid, String userno) {
        this.accountactiveday = accountactiveday;
        this.accountstatu = accountstatu;
        this.businessCategory = businessCategory;
        this.cn = cn;
        this.customAttr = customAttr;
        this.deptment = deptment;
        this.deptmentTitle = deptmentTitle;
        this.description = description;
        this.externalAuth = externalAuth;
        this.fixphone = fixphone;
        this.gender = gender;
        this.headship = headship;
        this.mail = mail;
        this.mobile = mobile;
        this.ncdeptTitle = ncdeptTitle;
        this.ncdeptment = ncdeptment;
        this.pwdexpirationtime = pwdexpirationtime;
        this.qqId = qqId;
        this.timestamp = timestamp;
        this.uid = uid;
        this.userno = userno;
    }

    public String getAccountactiveday() {
        return accountactiveday;
    }

    public void setAccountactiveday(String accountactiveday) {
        this.accountactiveday = accountactiveday;
    }

    public String getAccountstatu() {
        return accountstatu;
    }

    public void setAccountstatu(String accountstatu) {
        this.accountstatu = accountstatu;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getCustomAttr() {
        return customAttr;
    }

    public void setCustomAttr(String customAttr) {
        this.customAttr = customAttr;
    }

    public String getDeptment() {
        return deptment;
    }

    public void setDeptment(String deptment) {
        this.deptment = deptment;
    }

    public String getDeptmentTitle() {
        return deptmentTitle;
    }

    public void setDeptmentTitle(String deptmentTitle) {
        this.deptmentTitle = deptmentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalAuth() {
        return externalAuth;
    }

    public void setExternalAuth(String externalAuth) {
        this.externalAuth = externalAuth;
    }

    public String getFixphone() {
        return fixphone;
    }

    public void setFixphone(String fixphone) {
        this.fixphone = fixphone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadship() {
        return headship;
    }

    public void setHeadship(String headship) {
        this.headship = headship;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNcdeptTitle() {
        return ncdeptTitle;
    }

    public void setNcdeptTitle(String ncdeptTitle) {
        this.ncdeptTitle = ncdeptTitle;
    }

    public String getNcdeptment() {
        return ncdeptment;
    }

    public void setNcdeptment(String ncdeptment) {
        this.ncdeptment = ncdeptment;
    }

    public String getPwdexpirationtime() {
        return pwdexpirationtime;
    }

    public void setPwdexpirationtime(String pwdexpirationtime) {
        this.pwdexpirationtime = pwdexpirationtime;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

}
