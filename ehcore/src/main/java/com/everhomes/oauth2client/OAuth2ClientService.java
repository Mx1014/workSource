// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.rest.oauth2client.*;

/**
 * Created by xq.tian on 2017/3/6.
 */
public interface OAuth2ClientService {

    /**
     * 根据参数获取重定向的url
     */
    String getRedirectUrl(String vendor, String serviceUrl);

    /**
     * 从第三方哪里获取accessToken
     */
    String getAccessToken(String vendor, String code);

    /**
     * 发送请求调用第三方接口
     */
    OAuth2ClientApiResponse api(String vendor, OAuth2ClientApiCommand cmd);

    /**
     * 根据id修改oauth2server
     */
    void updateOAuth2Server(UpdateOAuth2ServerCommand cmd);

    /**
     * 新建oauth2server
     */
    OAuth2ServerDTO createOAuth2Server(CreateOAuth2ServerCommand cmd);

    /**
     * 根据id获取oauth2server
     */
    OAuth2ServerDTO getOAuth2Server(GetOAuth2ServerCommand cmd);
}
