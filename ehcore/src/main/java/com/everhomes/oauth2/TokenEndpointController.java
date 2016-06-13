package com.everhomes.oauth2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.rest.oauth2.OAuth2AccessTokenResponse;
import com.everhomes.util.DateHelper;

@RequireOAuth2Authentication(OAuth2AuthenticationType.CLIENT_AUTHENTICATION)
@RestController
@RequestMapping("/oauth2")
public class TokenEndpointController extends OAuth2ControllerBase {

    @RequestMapping("token")
    public ResponseEntity<Object> token(@RequestParam(value="grant_type", required = true) String grantType,
        @RequestParam(value="code", required = true) String code,
        @RequestParam(value="redirect_uri", required = true) String redirectUri,
        @RequestParam(value="client_id", required = true) String clientId) {

        AccessToken accessToken = this.oAuth2Service.grantAccessTokenFromAuthorizationCode(
            code, redirectUri, clientId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=utf-8");
        responseHeaders.set("Cache-Control", "no-store");
        responseHeaders.set("Pragma", "no-cache");

        OAuth2AccessTokenResponse tokenResponse = new OAuth2AccessTokenResponse();
        tokenResponse.setAccess_token(accessToken.getTokenString());
        tokenResponse.setToken_type("Bearer");

        long expireInSeconds = (accessToken.getExpirationTime().getTime() - DateHelper.currentGMTTime().getTime()) / 1000;
        tokenResponse.setExpires_in(expireInSeconds);

        ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(tokenResponse,
            responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
