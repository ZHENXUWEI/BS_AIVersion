package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.company.domain.Company;
import com.ruoyi.web.controller.company.service.ICompanyService;
import com.ruoyi.web.controller.forest.Oauth2;
import com.ruoyi.web.controller.forest.dto.Authentication;
import com.ruoyi.web.controller.forest.dto.SSOUserDto;
import com.ruoyi.web.controller.forest.dto.UserSyncDto;
import com.ruoyi.web.controller.forest.util.SSOUserConverter;
import com.ruoyi.web.controller.forest.util.TripleDesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private Oauth2 oauth2;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private SysRegisterService sysRegisterService;

    @Autowired
    private ISysUserService sysUserService;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/sync")
    public Object sync(@RequestBody UserSyncDto userSyncDto) {
        System.out.println(userSyncDto);
        String userInfo = userSyncDto.getUserInfo();
        String iv = userSyncDto.getIv();
        if (userInfo.isEmpty()) {
            return AjaxResult.error("未提供userInfo参数");
        }
        if (iv.isEmpty()) {
            return AjaxResult.error("未提供iv参数");
        }
        System.out.println(userInfo);
        System.out.println(iv);
        Map<String, String> res = new HashMap<>();
        try {
            String json = TripleDesUtils.decode(userInfo, "0653216b6a36434db824b272", iv);
            JSONObject jsonObject = JSON.parseObject(json);
            System.out.println(jsonObject);
            String username = jsonObject.getString("uid");
            String password = jsonObject.getString("password");
            String nickname = jsonObject.getString("cn");
            String phone = jsonObject.getString("mobile");
//            String id = jsonObject.getString("id");
            SysUser user = sysUserService.selectUserByUserName(jsonObject.getString("uid"));
            if (user == null) {
                RegisterBody registerBody = new RegisterBody();
                registerBody.setUsername(username);
                registerBody.setPassword(password);
                registerBody.setNickname(nickname);
                registerBody.setPhone(phone);
//                registerBody.setUserId(Long.valueOf(id));
                registerBody.setRemark(userInfo);
                String registerMsg = sysRegisterService.register(registerBody);
                System.out.println(registerMsg);
                if (!"".equals(registerMsg)) {
                    res.put("message", "新增失败");
                    res.put("retcode", "10012");
                    return res;
                }
                res.put("message", "新增成功");
                res.put("retcode", "10011");
                return res;
            } else {
                SysUser sysUser = new SysUser();
                sysUser.setUserName(username);
                sysUser.setPassword(SecurityUtils.encryptPassword(password));
                sysUser.setNickName(nickname);
                sysUser.setPhonenumber(phone);
                sysUser.setRemark(userInfo);
                sysUser.setUserId(user.getUserId());
                try {
                    sysUserService.updateUser(sysUser);
                    res.put("message", "修改成功");
                    res.put("retcode", "10011");
                    return res;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    res.put("message", "修改失败");
                    res.put("retcode", "10012");
                    return res;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        res.put("message", "解析失败");
        res.put("retcode", "10012");
        return res;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        System.out.println(user);
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

//    /**
//     * 获取AccessToken
//     *
//     * @param code     授权码
//     * @param state    客户端的状态
//     * @param response 响应
//     */
//    @RequestMapping("/token")
//    public AjaxResult getAccessToken(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) {
//        try {
//            String redirectUri = URLEncoder.encode("http://172.19.14.40:5317", StandardCharsets.UTF_8);
//            Authentication auth = oauth2.getAccessToken("authorization_code", "zcfw", "47db0cbeb8c3696d4c5b91cfe6f85e9b", code, redirectUri);
//            if (!"0".equals(auth.getRet())) {
//                switch (auth.getCode()) {
//                    case "6081":
//                        throw new RuntimeException("client_id非法（不是服务端已存在的资源系统）");
//                    case "6082":
//                        throw new RuntimeException("当前登录用户没有此client_id权限");
//                    case "6600":
//                        throw new RuntimeException("未通过认证(手机端)");
//                    case "6900":
//                        throw new RuntimeException("请求参数格式错误，具体参见返回信息中的msg字段。");
//                    case "6905":
//                        throw new RuntimeException("redirect_uri无法解析出主域名。");
//                    default:
//                        throw new RuntimeException("未知错误");
//                }
//            }
//            return AjaxResult.success(Constants.TOKEN, auth.getAccessToken());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return AjaxResult.error("获取令牌失败");
//        }
//    }

    @RequestMapping("/openId")
    public AjaxResult getOpenId(@RequestParam("access_token") String accessToken, HttpServletResponse response) {
        Authentication auth = oauth2.getOpenId(accessToken);
        if (!"0".equals(auth.getRet())) {
            switch (auth.getCode()) {
                case "9094":
                    throw new RuntimeException("access_token不存在");
                case "9900":
                    throw new RuntimeException("请求参数格式错误，具体参见返回信息中的msg字段");
            }
        } else {
            return AjaxResult.success(auth);
        }
        return AjaxResult.error(auth.getMsg());
    }

//    @RequestMapping("/usrInfo")
//    public AjaxResult getUsrInfo(@RequestParam("openId") String openId, @RequestParam("access_token") String accessToken, HttpServletResponse response) {
//        Authentication auth = oauth2.getUserInfo(accessToken, "zcfw", openId);
//        if (!"0".equals(auth.getRet())) {
//            switch (auth.getCode()) {
//                case "1":
////                    throw new RuntimeException("请求参数格式错误，具体参见返回信息中的msg字段");
//                    return AjaxResult.error(auth.getMsg());
//                case "-24":
//                    throw new RuntimeException("当前用户无权访问此资源");
//                case "9094":
//                    throw new RuntimeException("access_token不存在");
//            }
//            return AjaxResult.error(auth.getMsg());
//        } else {
//            return AjaxResult.success(auth.getUserInfo());
//        }
//    }

    @RequestMapping("/token")
    @ResponseBody
    public AjaxResult loginBySSO(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) {
        try {
            //测试
            String redirectUri = URLEncoder.encode("http://localhost:8080/", StandardCharsets.UTF_8);
            //正式
//            String redirectUri = URLEncoder.encode("https://zcfw.hzsteel.com", StandardCharsets.UTF_8);
            Authentication accessTokenRes = oauth2.getAccessToken("authorization_code", "zcfw", "47db0cbeb8c3696d4c5b91cfe6f85e9b", code, redirectUri);
            System.out.println("accessTokenRes---->");
            System.out.println(accessTokenRes);
            if (!"0".equals(accessTokenRes.getRet())) {
                switch (accessTokenRes.getCode()) {
                    case "6081":
                        throw new RuntimeException("client_id非法（不是服务端已存在的资源系统）");
                    case "6082":
                        throw new RuntimeException("当前登录用户没有此client_id权限");
                    case "6600":
                        throw new RuntimeException("未通过认证(手机端)");
                    case "6900":
                        return AjaxResult.error(accessTokenRes.getMsg());
                    case "6905":
                        throw new RuntimeException("redirect_uri无法解析出主域名。");
                    default:
                        throw new RuntimeException("未知错误");
                }
            } else {
                Authentication openIdRes = oauth2.getOpenId(accessTokenRes.getAccessToken());
                System.out.println("openIdRes---->");
                System.out.println(openIdRes);
                if (!"0".equals(openIdRes.getRet())) {
                    switch (openIdRes.getCode()) {
                        case "9094":
                            throw new RuntimeException("access_token不存在");
                        case "9900":
                            return AjaxResult.error(openIdRes.getMsg());
                    }
                } else {
                    Authentication usrInfoRes = oauth2.getUserInfo(accessTokenRes.getAccessToken(), "zcfw", openIdRes.getOpenid());
                    System.out.println(usrInfoRes);
                    if (!"0".equals(usrInfoRes.getRet())) {
                        switch (usrInfoRes.getCode()) {
                            case "1":
                                return AjaxResult.error(usrInfoRes.getMsg());
                            case "-24":
                                throw new RuntimeException("当前用户无权访问此资源");
                            case "9094":
                                throw new RuntimeException("access_token不存在");
                        }
                        return AjaxResult.error(usrInfoRes.getMsg());
                    } else {
                        SSOUserDto userInfo = usrInfoRes.getUserInfo();
                        SysUser user = sysUserService.selectUserByUserName(userInfo.getUid());
//                        SysUser user = sysUserService.selectUserById(Long.valueOf(userInfo.getUid()));
                        AjaxResult ajax = AjaxResult.success();
                        List<Company> cbyNames = companyService.selectCompaniesByName(userInfo.getNcdeptTitle());
                        if (cbyNames.isEmpty()) {
                            Company company = new Company(userInfo.getNcdeptTitle());
                            companyService.insertCompany(company);
                        }

                        List<Company> companies = companyService.selectCompaniesByName(userInfo.getNcdeptTitle());
                        Company company = null;
                        if (!companies.isEmpty()) {
                            company = companies.get(0);
                        }
                        if (userInfo.getPwd() == null) {
                            userInfo.setPwd("123456789");
                        }
                        if (user != null) {
                            if (user.getCompanyId() == null && company != null) {
                                user.setCompanyId(Long.valueOf(company.getId()));
                                sysUserService.updateUser(user);
                            }
//                            String tk = loginService.login(userInfo.getUid(), userInfo.getPwd());
                            LoginUser loginUser = loginService.loginWithoutPassword(userInfo.getUid());
                            ajax.put(Constants.TOKEN, loginUser.getToken());
                            return ajax;
                        } else {
//                            SysUser sysUser = SSOUserConverter.convertToSysUser((SSOUserDto) usrInfoRes.getUserInfo());
                            RegisterBody registerBody = new RegisterBody();
                            registerBody.setUsername(userInfo.getUid());
                            registerBody.setPassword(userInfo.getPwd());
                            registerBody.setCompanyId(Long.valueOf(company.getId()));
                            registerBody.setNickname(userInfo.getCn());
                            sysRegisterService.register(registerBody);

                            String tk = loginService.login(registerBody.getUsername(), registerBody.getPassword());
                            ajax.put(Constants.TOKEN, tk);
                            return ajax;
                        }
                    }
                }
                return AjaxResult.error(openIdRes.getMsg());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AjaxResult.error("获取令牌失败");
        }
    }

    /**
     * 单点登录
     *
     * @param token 授权对象（含AccessToken）
     * @return token（本系统token）
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @RequestMapping("/oauth")
    public AjaxResult oauth(@RequestBody Authentication token) {
        if (!token.getCode().isEmpty()) {
            switch (token.getCode()) {
                case "7000":
                    throw new RuntimeException("APP同时在线用户数已达license上限（此错误码为APP对接时才有）");
                case "7001":
                    throw new RuntimeException("accessToken创建失败");
                case "7018":
                    throw new RuntimeException("client_secret非法（资源id和title不匹配）");
                case "7020":
                    throw new RuntimeException("code已过期");
                case "7081":
                    throw new RuntimeException("client_id非法（不是服务端已存在的资源系统）");
                case "7900":
                    throw new RuntimeException("请求参数格式错误，具体参见返回信息中的msg字段");
                case "7905":
                    throw new RuntimeException("redirect_uri无法解析出主域名");
            }
        }
        Authentication auth = oauth2.getOpenId(token.getAccessToken());
        if (!auth.getCode().isEmpty()) {
            switch (auth.getCode()) {
                case "9094":
                    return AjaxResult.error("access_token不存在");
                case "9900":
                    return AjaxResult.error("请求参数格式错误，具体参见返回信息中的msg字段");
            }
        }
        Company cbyId = companyService.selectCompanyByOpenid(auth.getOpenid());
        SysUser sysUser = sysUserService.selectUserByUserName(auth.getUsername());
        AjaxResult ajax = AjaxResult.success();
        if (cbyId == null) {
            Company cbyName = companyService.selectCompanyByName(auth.getUsername());
            RegisterBody registerBody = new RegisterBody();
            registerBody.setUsername(auth.getUsername());
            registerBody.setPassword("Long@123456");
            sysRegisterService.register(registerBody);
            if (cbyName == null) {
                Company company = new Company(auth.getUsername(), auth.getOpenid(), sysUser.getUserId());
                companyService.insertCompany(company);
            } else {
                Company company = new Company(cbyName.getId(), auth.getOpenid(), sysUser.getUserId());
                companyService.updateCompany(company);
            }
            String tk = loginService.login(registerBody.getUsername(), registerBody.getPassword());
            ajax.put(Constants.TOKEN, tk);
        } else {
            String tk = loginService.login(sysUser.getUserName(), sysUser.getPassword());
            ajax.put(Constants.TOKEN, tk);
        }
        return ajax;
    }

    /**
     * 将getAccessToken和oauth放在同个接口进行
     *
     * @param code     授权码
     * @param state    客户端状态
     * @param ret      状态码
     * @param response 响应
     * @return token
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @RequestMapping("/v2/authorize/{code}/{state}")
    public AjaxResult getAccessTokenV2(@PathVariable("code") String code, @PathVariable("state") String state, @RequestParam String ret, HttpServletResponse response) {

        if (!"0".equals(ret)) {
            switch (code) {
                case "6081":
                    return AjaxResult.error("client_id非法（不是服务端已存在的资源系统）");
                case "6082":
                    return AjaxResult.error("当前登录用户没有此client_id权限");
                case "6600":
                    return AjaxResult.error("未通过认证(手机端)");
                case "6900":
                    return AjaxResult.error("请求参数格式错误，具体参见返回信息中的msg字段。");
                case "6905":
                    return AjaxResult.error("redirect_uri无法解析出主域名。");
                default:
                    return AjaxResult.error("未知错误");
            }
        } else {
            Authentication token = oauth2.getAccessToken("authorization_code", "zcfw", "47db0cbeb8c3696d4c5b91cfe6f85e9b", code, "http://172.19.14.40:5317");
            if (!token.getCode().isEmpty()) {
                switch (token.getCode()) {
                    case "7000":
                        return AjaxResult.error("APP同时在线用户数已达license上限（此错误码为APP对接时才有）");
                    case "7001":
                        return AjaxResult.error("accessToken创建失败");
                    case "7018":
                        return AjaxResult.error("client_secret非法（资源id和title不匹配）");
                    case "7020":
                        return AjaxResult.error("code已过期");
                    case "7081":
                        return AjaxResult.error("client_id非法（不是服务端已存在的资源系统）");
                    case "7900":
                        return AjaxResult.error("请求参数格式错误，具体参见返回信息中的msg字段");
                    case "7905":
                        return AjaxResult.error("redirect_uri无法解析出主域名");
                }
            }
            Authentication auth = oauth2.getOpenId(token.getAccessToken());
            if (!auth.getCode().isEmpty()) {
                switch (auth.getCode()) {
                    case "9094":
                        return AjaxResult.error("access_token不存在");
                    case "9900":
                        return AjaxResult.error("请求参数格式错误，具体参见返回信息中的msg字段");
                }
            }
            Company cbyId = companyService.selectCompanyByOpenid(auth.getOpenid());
            SysUser sysUser = sysUserService.selectUserByUserName(auth.getUsername());
            AjaxResult ajax = AjaxResult.success();
            if (cbyId == null) {
                Company cbyName = companyService.selectCompanyByName(auth.getUsername());
                RegisterBody registerBody = new RegisterBody();
                registerBody.setUsername(auth.getUsername());
                registerBody.setPassword("Long@123456");
                sysRegisterService.register(registerBody);
                if (cbyName == null) {
                    Company company = new Company(auth.getUsername(), auth.getOpenid(), sysUser.getUserId());
                    companyService.insertCompany(company);
                } else {
                    Company company = new Company(cbyName.getId(), auth.getOpenid(), sysUser.getUserId());
                    companyService.updateCompany(company);
                }
                String tk = loginService.login(registerBody.getUsername(), registerBody.getPassword());
                ajax.put(Constants.TOKEN, tk);
            } else {
                String tk = loginService.login(sysUser.getUserName(), sysUser.getPassword());
                ajax.put(Constants.TOKEN, tk);
            }
            return ajax;
        }
    }

    @GetMapping("/oauth-login")
    public void redirectToOAuth(HttpServletResponse response) throws IOException {
        //测试
        String redirectUri = URLEncoder.encode("http://localhost:5173", StandardCharsets.UTF_8);

        //正式
        //String redirectUri = URLEncoder.encode("https://zcfw.hzsteel.com", StandardCharsets.UTF_8);
        String oauthUrl = "           "
                + "&client_id=zcfw"
                + "&redirect_uri=" + redirectUri
                + "&state=ok";
        response.sendRedirect(oauthUrl);
    }
}
