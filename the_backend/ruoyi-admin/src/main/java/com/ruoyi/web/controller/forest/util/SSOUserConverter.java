package com.ruoyi.web.controller.forest.util;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.web.controller.forest.dto.SSOUserDto;

public class SSOUserConverter {

    public static SysUser convertToSysUser(SSOUserDto dto) {
        SysUser user = new SysUser();
        user.setUserId(Long.valueOf(dto.getUid()));
        user.setUserName(dto.getCn());
        user.setEmail(dto.getMail());
        user.setPhonenumber(dto.getMobile());
        if ("男".equals(dto.getGender())) {
            user.setSex("0");
        } else if ("女".equals(dto.getGender())) {
            user.setSex("1");
        } else {
            user.setSex("2");
        }
        user.setAvatar(dto.getCustomAttr());
        user.setRemark(dto.getDescription());
        return user;
    }
}
