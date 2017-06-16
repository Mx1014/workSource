package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.sql.Timestamp;

@Component
public class OAuth2ServiceImpl implements OAuth2Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);

    private static final long DEFAULT_CODE_EXPIRACTION_SECONDS = 600;   // 10 minutes
    private static final long DEFAULT_TOKEN_EXPIRACTION_SECONDS = 3600;  // 1 hour

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2Provider oAuth2Provider;

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public URI confirmAuthorization(String identifier, String password, AuthorizationCommand cmd) {
        User user = userService.logonDryrun(identifier, password);
        if(user == null)
            return null;

        return confirmAuthorization(user, cmd);
    }
   
    @Override
    public URI confirmAuthorization(User user, AuthorizationCommand cmd) {
        if (!"code".equals(cmd.getresponse_type())) {
            LOGGER.error("Only code response type is supported, response_type = {}", cmd.getresponse_type());
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE, OAuth2ServiceErrorCode.ERROR_UNSUPPORTED_RESPONSE_TYPE,
                    "Only code response type is supported");
        }

        AuthorizationCode code = new AuthorizationCode();
        code.setScope(cmd.getScope());
        code.setRedirectUri(cmd.getredirect_uri());
        code.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        code.setModifyTime(code.getCreateTime());
        code.setExpirationTime(new Timestamp(DateHelper.currentGMTTime().getTime() + getCodeExpirationSeconds() * 1000));
        code.setAppId(appIdFromClientId(cmd.getclient_id()));
        code.setGrantorUid(user.getId());
        oAuth2Provider.createAuthorizationCode(code);

        String redirectUrl = String.format("%s?code=%s&state=%s", cmd.getredirect_uri(), code.getCode(), cmd.getState());

        try {
            return new URI(redirectUrl);
        } catch(Exception e) {
            LOGGER.error("confirmAuthorization new URI error", e);
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE, OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST,
                    "Invalid redirect URI parameter");
        }
    }
    

    @Override
    public AccessToken grantAccessTokenFromAuthorizationCode(String code, String redirectUri, String clientId) {
        App app = this.appProvider.findAppByKey(clientId);
        if(app == null)
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
                    OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid clientapp Id");

        if(redirectUri == null || redirectUri.isEmpty())
            redirectUri = getDefaultRedirectUri(app.getId());

        AuthorizationCode authorizationCode = this.oAuth2Provider.findAuthorizationCodeByCode(code);
        if(authorizationCode == null)
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
                    OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid authorization code");

        if(authorizationCode.isExpired(DateHelper.currentGMTTime()))
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
                    OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid authorization code, code offer has expired");

        if(!authorizationCode.getRedirectUri().equals(redirectUri))
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
                    OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid redirect URI, it is different with the one that was originally granted");

        AccessToken accessToken = new AccessToken();
        accessToken.setGrantorUid(authorizationCode.getGrantorUid());
        accessToken.setScope(authorizationCode.getScope());
        accessToken.setType(OAuth2TokenType.ACCESS_TOKEN.getCode());
        accessToken.setAppId(app.getId());
        accessToken.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        accessToken.setExpirationTime(new Timestamp(DateHelper.currentGMTTime().getTime() + this.getAccessTokenExpirationSeconds()*1000));
        this.oAuth2Provider.createAccessToken(accessToken);

        // expire authorization code after a successful retrieval of it
        authorizationCode.setExpirationTime(accessToken.getCreateTime());
        this.oAuth2Provider.updateAuthorizationCode(authorizationCode);
        return accessToken;
    }

    @Override
    public String getDefaultRedirectUri(long appId) {
        // ??? TODO
        return null;
    }

    @Override
    public boolean validateRedirectUri(long appId, String redirectUri) {
        // ??? TODO
        return true;
    }

    @Override
    public String getOAuth2AuthorizeError(int errorCode) {
        switch(errorCode) {
        case OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST:
            return "invalid_request";

        case OAuth2ServiceErrorCode.ERROR_UNAUTHORIZED_CLIENT :
            return "unauthorized_client";

        case OAuth2ServiceErrorCode.ERROR_ACCESS_DENIED :
            return "access_denied";

        case OAuth2ServiceErrorCode.ERROR_UNSUPPORTED_RESPONSE_TYPE :
            return "unsupported_response_type";

        case OAuth2ServiceErrorCode.ERROR_INVALID_SCOPE :
            return "invalid_scope";

        case OAuth2ServiceErrorCode.ERROR_SERVER_ERROR :
            return "server_error";

        case OAuth2ServiceErrorCode.ERROR_SERVER_BUSY :
            return "temporarily_unavailable";

        case OAuth2ServiceErrorCode.ERROR_INVALID_CLIENT :
            return "invalid_client";

        case OAuth2ServiceErrorCode.ERROR_INVALID_GRANT :
            return "invalid_grant";

        case OAuth2ServiceErrorCode.ERROR_UNSUPPORTED_GRANT_TYPE :
            return "unsupported_grant_type";

        default :
            break;
        }
        return "server_error";
    }

    private long getCodeExpirationSeconds() {
        return this.configurationProvider.getLongValue("oauth2.code.expiration.seconds", DEFAULT_CODE_EXPIRACTION_SECONDS);
    }

    private long getAccessTokenExpirationSeconds() {
        return this.configurationProvider.getLongValue("oauth2.token.expiration.seconds", DEFAULT_TOKEN_EXPIRACTION_SECONDS);
    }

    private Long appIdFromClientId(String clientId) {
        App app = this.appProvider.findAppByKey(clientId);
        if(app != null)
            return app.getId();

        return null;
    }
}
