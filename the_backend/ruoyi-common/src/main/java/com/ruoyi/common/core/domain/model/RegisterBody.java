package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    private Long companyId;

    private Long userId;

    private String remark;

    private String nickname;

    private String phone;

    public String getPhone() {
        return phone;
    }


    @Override
    public String toString() {
        return "RegisterBody{" +
                "companyId=" + companyId +
                ", userId=" + userId +
                ", remark='" + remark + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
