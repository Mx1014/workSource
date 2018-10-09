package com.everhomes.oauth2;

public interface OAuth2Provider {
    void createAuthorizationCode(AuthorizationCode code);
    void updateAuthorizationCode(AuthorizationCode code);
    void deleteAuthorizationCode(AuthorizationCode code);
    void deleteAuthorizationCodeById(long id);
    AuthorizationCode findAuthorizationCodeById(long id);
    AuthorizationCode findAuthorizationCodeByCode(String code);

    void createAccessToken(AccessToken token);
    void updateAccessToken(AccessToken token);
    void deleteAccessToken(AccessToken token);
    void deleteAccessTokenById(long id);
    AccessToken findAccessTokenById(long id);
    AccessToken findAccessTokenByTokenString(String tokenString);

    AccessToken findAccessTokenByAppAndGrantorUid(Long appId, Long grantorUid);
}
