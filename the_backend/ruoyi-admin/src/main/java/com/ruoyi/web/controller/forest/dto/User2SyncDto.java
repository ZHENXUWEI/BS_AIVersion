package com.ruoyi.web.controller.forest.dto;

public class User2SyncDto {
    private String id;
    private String uid;
    private String cn;
    private String deptment;
    private String password;
    private String mobile;
    private String fixphone;
    private String accountstatu;
    private String gender;
    private String mail;
    private String jobtitle;

    public User2SyncDto() {
    }

    public User2SyncDto(String id, String uid, String cn, String deptment, String password, String mobile, String fixphone, String accountstatu, String gender, String mail, String jobtitle) {
        this.id = id;
        this.uid = uid;
        this.cn = cn;
        this.deptment = deptment;
        this.password = password;
        this.mobile = mobile;
        this.fixphone = fixphone;
        this.accountstatu = accountstatu;
        this.gender = gender;
        this.mail = mail;
        this.jobtitle = jobtitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getDeptment() {
        return deptment;
    }

    public void setDeptment(String deptment) {
        this.deptment = deptment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFixphone() {
        return fixphone;
    }

    public void setFixphone(String fixphone) {
        this.fixphone = fixphone;
    }

    public String getAccountstatu() {
        return accountstatu;
    }

    public void setAccountstatu(String accountstatu) {
        this.accountstatu = accountstatu;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    @Override
    public String toString() {
        return "UserSyncDto{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", cn='" + cn + '\'' +
                ", deptment='" + deptment + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", fixphone='" + fixphone + '\'' +
                ", accountstatu='" + accountstatu + '\'' +
                ", gender='" + gender + '\'' +
                ", mail='" + mail + '\'' +
                ", jobtitle='" + jobtitle + '\'' +
                '}';
    }
}
