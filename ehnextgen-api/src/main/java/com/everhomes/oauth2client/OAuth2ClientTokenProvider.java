package com.everhomes.oauth2client;

import com.everhomes.rest.oauth2client.OAuth2ClientTokenType;

/**
 * Created by xq.tian on 2017/3/7.
 */
public interface OAuth2ClientTokenProvider {

    void createToken(OAuth2ClientToken token);

    OAuth2ClientToken findLastTokenByUserAndVendor(Long userId, String vendor, OAuth2ClientTokenType tokenType);
}
