package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.user.User;
import org.springframework.ui.Model;

import java.net.URI;

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
    ConfirmAuthorizationVO confirmAuthorization(Integer namespaceId, String identifier, String password, AuthorizationCommand cmd);
    URI confirmAuthorization(User user, AuthorizationCommand cmd);
    AccessToken grantAccessTokenFromAuthorizationCode(String code, String redirectUri, String clientId);

    void addAttribute(Model model, AuthorizationCommand cmd, App app);
}
