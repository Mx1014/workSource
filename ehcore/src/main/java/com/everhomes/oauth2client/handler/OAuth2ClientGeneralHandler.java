// @formatter:off
package com.everhomes.oauth2client.handler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.OAuth2ClientToken;
import com.everhomes.oauth2client.OAuth2Server;
import com.everhomes.rest.oauth2client.*;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by xq.tian on 2017/3/7.
 */
@Component(OAuth2ClientHandlerConstant.OAUTH2_HANDLER + OAuth2ClientHandlerConstant.HANDLER_GENERAL)
public class OAuth2ClientGeneralHandler implements OAuth2ClientHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientGeneralHandler.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String getAuthorizeUrl(OAuth2Server server) {
        return RestCallTemplate.queryStringBuilder()
                .var("client_id", server.getClientId())
                .var("client_secret", server.getClientSecret())
                .var("response_type", server.getResponseType())
                .var("redirect_uri", RestCallTemplate.urlEncode(getHomeUrl() + server.getRedirectUri()))
                .var("scope", server.getScope())
                .var("state", server.getState())
                .build(server.getAuthorizeUrl());
    }

    private String getHomeUrl() {
        return configurationProvider.getValue(ConfigConstants.HOME_URL, "");
    }

    @Override
    public OAuth2ClientApiResponse api(OAuth2Server server, OAuth2ClientToken token, OAuth2ClientApiCommand cmd) {

        String contentType = cmd.getContentType() != null ? cmd.getContentType() : MediaType.APPLICATION_FORM_URLENCODED_VALUE;

        HttpResponseEntity responseEntity = RestCallTemplate.url(cmd.getUrl())
                .body(cmd.getParam())
                .header("Authorization", "bearer " + token.getTokenString())
                .header("Content-Type", contentType)
                .method(cmd.getMethod())
                .respType(String.class)
                .errorHandler(r -> LOGGER.debug("oauth2 server response error, code={}", r.getStatusCode()))
                .execute();

        OAuth2ClientApiResponse response = new OAuth2ClientApiResponse();
        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setStatus(responseEntity.getStatusCode().value());
        response.setResponse(responseEntity.getBody().toString());

        return response;
    }

    /*@Override
    public String getServiceUrl(OAuth2Server server) {
        return server.getServiceUrl();
    }*/

    @Override
    public Tuple<OAuth2ClientToken, OAuth2ClientToken> getToken(OAuth2Server server, String code) {

        String body = RestCallTemplate.queryStringBuilder()
                .var("client_id", server.getClientId())
                .var("client_secret", server.getClientSecret())
                .var("grant_type", server.getGrantType())
                .var("redirect_uri", RestCallTemplate.urlEncode(getHomeUrl() + server.getRedirectUri()))
                .var("code", code)
                .build();

        HttpResponseEntity<OAuth2ResponseEntity> responseEntity = RestCallTemplate.url(server.getTokenUrl())
                .body(body)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .respType(OAuth2ResponseEntity.class)
                .errorHandler(r -> {
                    LOGGER.error("oauth2 server response error, code={}, server={}", r.getStatusCode(), server);
                    throw RuntimeErrorException.errorWith(OAuth2ClientServiceErrorCode.SCOPE,
                            OAuth2ClientServiceErrorCode.ERROR_OAUTH2_SERVER_RESPONSE_ERROR, "oauth2 server response error");
                })
                .post();

        OAuth2ResponseEntity token = responseEntity.getBody();

        return toTokenTuple(server, token);
    }

    @Override
    public Tuple<OAuth2ClientToken, OAuth2ClientToken> refreshToken(OAuth2Server server, OAuth2ClientToken refreshToken, OAuth2ClientToken accessToken) {
        String body = RestCallTemplate.queryStringBuilder()
                .var("grant_type", "refresh_token")
                .var("refresh_token", refreshToken.getTokenString())
                .build();

        HttpResponseEntity<OAuth2ResponseEntity> responseEntity = RestCallTemplate.url(server.getTokenUrl())
                .body(body)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("Authorization", "bearer " + accessToken.getTokenString())
                .respType(OAuth2ResponseEntity.class)
                .errorHandler(r -> {
                    LOGGER.error("oauth2 server response error, code={}, server={}", r.getStatusCode(), server);
                    throw RuntimeErrorException.errorWith(OAuth2ClientServiceErrorCode.SCOPE,
                            OAuth2ClientServiceErrorCode.ERROR_OAUTH2_SERVER_RESPONSE_ERROR, "oauth2 server response error");
                })
                .post();

        OAuth2ResponseEntity token = responseEntity.getBody();

        return toTokenTuple(server, token);
    }

    private Tuple<OAuth2ClientToken, OAuth2ClientToken> toTokenTuple(OAuth2Server server, OAuth2ResponseEntity token) {
        OAuth2ClientToken newAccessToken = new OAuth2ClientToken();
        newAccessToken.setExpirationTime(Timestamp.from(Instant.ofEpochMilli(Instant.now().toEpochMilli() + token.getExpires_in()*1000)));
        newAccessToken.setScope(token.getScope());
        newAccessToken.setTokenString(token.getAccess_token());
        newAccessToken.setType(OAuth2ClientTokenType.ACCESS_TOKEN.getCode());
        newAccessToken.setVendor(server.getVendor());

        OAuth2ClientToken newRefreshToken = new OAuth2ClientToken();
        newRefreshToken.setExpirationTime(Timestamp.from(Instant.ofEpochMilli(Instant.now().toEpochMilli() + token.getExpires_in()*1000)));
        newRefreshToken.setScope(token.getScope());
        newRefreshToken.setTokenString(token.getRefresh_token());
        newRefreshToken.setType(OAuth2ClientTokenType.REFRESH_TOKEN.getCode());
        newRefreshToken.setVendor(server.getVendor());

        return new Tuple<>(newAccessToken, newRefreshToken);
    }
}
