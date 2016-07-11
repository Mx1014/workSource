package com.everhomes.oauth2;

import java.net.URI;

import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.user.User;

public interface OAuth2Service {

    //
    // Implementation helpers
    //
    String getDefaultRedirectUri(long appId);
    boolean validateRedirectUri(long appId, String redirectUri);

    // convert errorCode into RFC 6749 defined error name
    String getOAuth2AuthorizeError(int errorCode);

    //
    // Service methods
    //
    URI confirmAuthorization(String identifier, String password, AuthorizationCommand cmd);
    URI confirmAuthorization(User user, AuthorizationCommand cmd);
    AccessToken grantAccessTokenFromAuthorizationCode(String code, String redirectUri, String clientId);
}
