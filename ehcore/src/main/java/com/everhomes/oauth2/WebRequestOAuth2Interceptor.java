package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class WebRequestOAuth2Interceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestOAuth2Interceptor.class);

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private OAuth2Provider oAuth2Provider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequireOAuth2Authentication authentication = getOAuth2Authentication(handler);
        if(authentication == null || authentication.value() == OAuth2AuthenticationType.NO_AUTHENTICATION)
            return true;

        switch(authentication.value()) {
            case ACCESS_TOKEN:
                return authenticateAccessToken(request, response);

            case CLIENT_AUTHENTICATION:
                return authenticateClient(request, response);

            default :
                assert(false);
                break;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        OAuth2UserContext.clear();
    }

    private boolean authenticateAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            if(header.equalsIgnoreCase("Authorization")) {
                String[] tokens = request.getHeader(header).trim().split(" ");
                if (tokens.length < 2 || !tokens[0].equalsIgnoreCase("Bearer")) {
                    break;
                }

                byte[] decodedToken = Base64.decodeBase64(tokens[1].trim());
                String tokenString = new String(decodedToken, "UTF-8");

                AccessToken accessToken = this.oAuth2Provider.findAccessTokenByTokenString(tokenString);
                if(accessToken != null) {
                    if(accessToken.isExpired(DateHelper.currentGMTTime())) {
                        LOGGER.error("Access token {} has expired", tokens[1]);
                        throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE, OAuth2ServiceErrorCode.ERROR_INVALID_CLIENT,
                                "Invalid access token");
                    }

                    OAuth2UserContext.current().setAuthenticationType(OAuth2AuthenticationType.ACCESS_TOKEN);
                    OAuth2UserContext.current().setAccessToken(accessToken);
                    return true;
                }
                break;
            }
        }

        LOGGER.error("invalid request, could not find access token");
        throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE, OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST,
                "Invalid request, could not find access token");
    }

    private boolean authenticateClient(HttpServletRequest request, HttpServletResponse response) {

        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            if(header.equalsIgnoreCase("Authorization")) {
                String[] tokens = request.getHeader(header).trim().split(" ");
                if (tokens.length < 2 || !tokens[0].equalsIgnoreCase("Basic")) {
                    break;
                }

                byte[] usenamePasswordCombo = Base64.decodeBase64(tokens[1]);
                if(usenamePasswordCombo != null) {
                    try {
                        String[] credentials = new String(usenamePasswordCombo, "UTF-8").split(":");
                        if(validateClient(credentials[0].trim(), credentials[1].trim()))
                            return true;

                    } catch(Exception e) {
                        assert(false);
                    }
                }

                break;
            }
        }

        response.addHeader("WWW-Authenticate", "Basic realm=\"OAuth2\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private boolean validateClient(String appKey, String appSecret) {
        App app = this.appProvider.findAppByKey(appKey);
        if(app != null && app.getSecretKey().equals(appSecret)) {
            LOGGER.info("App {} has been clientapp-authenticated");

            OAuth2UserContext.current().setClientApp(app);
            OAuth2UserContext.current().setAuthenticationType(OAuth2AuthenticationType.CLIENT_AUTHENTICATION);

            return true;
        }
        return false;
    }

    private RequireOAuth2Authentication getOAuth2Authentication(Object handler) {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            RequireOAuth2Authentication authentication = handlerMethod.getMethod().getAnnotation(RequireOAuth2Authentication.class);
            if(authentication == null)
                authentication = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequireOAuth2Authentication.class);

            return authentication;
        }
        return null;
    }
}
