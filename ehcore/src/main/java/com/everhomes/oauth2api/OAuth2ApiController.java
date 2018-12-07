package com.everhomes.oauth2api;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.oauth2.AccessToken;
import com.everhomes.oauth2.OAuth2AuthenticationType;
import com.everhomes.oauth2.OAuth2UserContext;
import com.everhomes.oauth2.RequireOAuth2Authentication;
import com.everhomes.oauthapi.OAuth2ApiService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserInfoDTO;
import com.everhomes.rest.user.ZhenZhiHuiUserDetailInfo;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    private OAuth2ApiService oAuth2ApiService;

    /**
     * <b>URL: /oauth2api/getUserInfo</b>
     * <p>给内部用的获取用户信息的接口</p>
     */
    @RequestMapping("getUserInfo")
    @RestReturn(value=UserInfo.class)
    public RestResponse getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        UserInfo info = this.oAuth2ApiService.getUserInfoForInternal(accessToken.getGrantorUid());
        return new RestResponse(info);
    }

    /**
     * <b>URL: /oauth2api/trd/userInfo</b>
     * <p>给第三方的获取用户信息</p>
     */
    @RequestMapping("trd/userInfo")
    @RestReturn(value=UserInfoDTO.class)
    public RestResponse userInfo() {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        UserInfoDTO info = this.oAuth2ApiService.getUserInfoForThird(accessToken.getGrantorUid());
        return new RestResponse(info);
    }

    /**
     * <b>URL: /oauth2api/getUserInfoForZhenZhiHui</b>
     * <p>给圳智慧用的获取用户信息的接口</p>
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getUserInfoForZhenZhiHui")
    @RestReturn(value=ZhenZhiHuiUserDetailInfo.class)
    public RestResponse getUserInfoForZhenZhiHui(HttpServletRequest request, HttpServletResponse response) {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        ZhenZhiHuiUserDetailInfo info = this.oAuth2ApiService.getUserInfoForZhenZhiHui(accessToken.getGrantorUid());
        return new RestResponse(info);
    }
    /**
     * <b>URL: /oauth2api/trd/authenticationInfo</b>
     * <p>用户认证信息</p>
     */
    @RequestMapping("trd/authenticationInfo")
    @RestReturn(value=OrganizationMemberDTO.class, collection = true)
    public RestResponse authenticationInfo() {
        AccessToken accessToken = OAuth2UserContext.current().getAccessToken();
        List<OrganizationMemberDTO> members = oAuth2ApiService.getAuthenticationInfo(accessToken.getGrantorUid());
        return new RestResponse(members);
    }
}
