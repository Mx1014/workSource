package com.everhomes.oauth2api;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.oauth2.AccessToken;
import com.everhomes.oauth2.OAuth2AuthenticationType;
import com.everhomes.oauth2.OAuth2UserContext;
import com.everhomes.oauth2.RequireOAuth2Authentication;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller contains APIs that returns user information to third-party applications through
 * OAuth2 authorized access-token mechanism 
 *
 */
@RequireAuthentication(false)
@RequireOAuth2Authentication(OAuth2AuthenticationType.ACCESS_TOKEN)
@RestController
@RequestMapping("/oauth2api")
public class OAuth2ApiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ApiController.class);

    @Autowired
    private UserService userService;

    /**
     * <b>URL: /oauth2api/getUserInfo</b>
     * <p>给内部用的获取用户信息的接口</p>
     */
    @RequestMapping("getUserInfo")
    @RestReturn(value=UserInfo.class)
    public RestResponse getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        UserInfo info = this.userService.getUserSnapshotInfoWithPhone(accessToken.getGrantorUid());
        return new RestResponse(info);
    }

    /**
     * <b>URL: /oauth2api/trd/userInfo</b>
     * <p>给第三方的获取用户信息，把一些敏感信息去掉了</p>
     */
    @RequestMapping("trd/userInfo")
    @RestReturn(value=UserInfo.class)
    public RestResponse userInfo() {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        UserInfo info = this.userService.getUserSnapshotInfoWithPhone(accessToken.getGrantorUid());
        info = sensitiveClean(info);
        return new RestResponse(info);
    }

    private UserInfo sensitiveClean(UserInfo info) {
        // 把一些敏感信息去掉    add by xq.tian  2017/06/16
        UserInfo newInfo = new UserInfo();
        newInfo.setAvatarUrl(info.getAvatarUrl());
        newInfo.setNickName(info.getNickName());
        newInfo.setAccountName(info.getAccountName());
        return newInfo;
    }
}
