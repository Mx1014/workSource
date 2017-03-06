// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.rest.oauth2client.OAuth2ClientApiCommand;
import com.everhomes.rest.oauth2client.OAuth2ClientApiResponse;

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
}
