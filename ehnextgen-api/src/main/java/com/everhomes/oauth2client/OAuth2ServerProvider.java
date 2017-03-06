package com.everhomes.oauth2client;

/**
 * Created by xq.tian on 2017/3/7.
 */
public interface OAuth2ServerProvider {

    /**
     * 拿OAuth2的服务信息
     */
    OAuth2Server findOAuth2ServerByVendor(String vendor);
}
