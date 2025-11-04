package com.ruoyi.web.controller.forest;

import com.dtflys.forest.annotation.*;
import com.ruoyi.web.controller.forest.dto.Authentication;
import com.ruoyi.web.controller.forest.dto.UserSyncDto;
import org.springframework.stereotype.Component;

import java.util.Map;


@BaseRequest(
        readTimeout = 20 * 3600,
        connectTimeout = 20 * 3600
)
public interface Oauth2 {
    @Post(
            url = "https://github.com/login/oauth/access_token?",
            headers = {
                    "Content-Type: application/x-www-form-urlencoded",
                    "Accept: */*"
            }
    )
    String getAccessTokenExample(@Body Map<String, Object> map);

    @Get(
            url = "http://localhost:8080/ssoserver/moc2/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}&code={code}&redirect_uri={redirectUri} "
    )
    Authentication getAccessToken(@Var("grantType") String grantType,
                                  @Var("clientId") String clientId,
                                  @Var("clientSecret") String clientSecret,
                                  @Var("code") String code,
                                  @Var("redirectUri") String redirectUri);


    @Get(
            url = "http://localhost:8080/ssoserver/moc2/me?access_token={accessToken}"
    )
    Authentication getOpenId(@Var("accessToken") String accessToken);

    @Get(
            url = "http://localhost:8080/ssoserver/user/info?access_token={accessToken}&oauth_consumer_key={oauthConsumerKey}&openid={openId}"
    )
    Authentication getUserInfo(@Var("accessToken") String accessToken,
                               @Var("oauthConsumerKey") String oauthConsumerKey,
                               @Var("openId") String openId);

    @Post(
            url = "ip"
    )
    UserSyncDto userSyncInfo();


}