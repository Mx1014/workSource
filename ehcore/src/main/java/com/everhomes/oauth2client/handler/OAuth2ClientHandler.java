// @formatter:off
package com.everhomes.oauth2client.handler;

import com.everhomes.oauth2client.OAuth2ClientToken;
import com.everhomes.oauth2client.OAuth2Server;
import com.everhomes.rest.oauth2client.OAuth2ClientApiCommand;
import com.everhomes.rest.oauth2client.OAuth2ClientApiResponse;
import com.everhomes.util.Tuple;

/**
 * Created by xq.tian on 2017/3/7.
 */
public interface OAuth2ClientHandler {

    /**
     * 获取授权url
     */
    default String getAuthorizeUrl(OAuth2Server server) {
        return server.getAuthorizeUrl();
    }

    /**
     * 调用第三方的api
     */
    OAuth2ClientApiResponse api(OAuth2Server server, OAuth2ClientToken token, OAuth2ClientApiCommand cmd);

    /**
     * 返回业务页面
     */
    // String getServiceUrl(OAuth2Server server);

    /**
     * 根据授权code获取accessToken
     */
    Tuple<OAuth2ClientToken,OAuth2ClientToken> getToken(OAuth2Server server, String code);

    /**
     * 刷新token
     */
    Tuple<OAuth2ClientToken, OAuth2ClientToken> refreshToken(OAuth2Server server, OAuth2ClientToken refreshToken, OAuth2ClientToken accessToken);
}
