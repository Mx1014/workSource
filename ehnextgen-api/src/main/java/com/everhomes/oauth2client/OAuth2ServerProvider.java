package com.everhomes.oauth2client;

/**
 * Created by xq.tian on 2017/3/7.
 */
public interface OAuth2ServerProvider {

    /**
     * 拿OAuth2的服务信息
     */
    OAuth2Server findOAuth2ServerByVendor(String vendor);

    /**
     * 修改
     */
    void updateOauth2Server(OAuth2Server oAuth2Server);

    /**
     * 创建
     */
    void createOAuth2Server(OAuth2Server oAuth2Server);

    /**
     * get
     */
    OAuth2Server findOAuth2ServerById(Long id);
}
