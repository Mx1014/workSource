package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.rest.user.DeviceIdentifierType;
import com.everhomes.rest.user.GetUserInfoByIdCommand;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.*;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.codec.binary.Base64;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

public class WebRequestOAuth2Interceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestOAuth2Interceptor.class);

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private OAuth2Provider oAuth2Provider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequireOAuth2Authentication authentication = getOAuth2Authentication(handler);
        if(authentication == null || authentication.value() == OAuth2AuthenticationType.NO_AUTHENTICATION) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod method = (HandlerMethod) handler;
                Method m = method.getMethod();
                RequestMapping annotation = m.getAnnotation(RequestMapping.class);
                if (annotation != null && annotation.value()[0].equals("authorize")) {
                    LoginToken token = this.innerSignLogon(request, response);
                    setupUserContext(token);
                    MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
                }
            }
            return true;
        }
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

    private LoginToken innerSignLogon(HttpServletRequest request, HttpServletResponse response) {
        //2016-07-29:modify by liujinwne,parameter name don't be signed.

        Map<String, String[]> paramMap = request.getParameterMap();
        String appKey = getParamValue(paramMap, "appKey");
        String signature = getParamValue(paramMap, "signature");
        String id = getParamValue(paramMap, "id");
        String randomNum = getParamValue(paramMap, "randomNum");
        String timeStamp = getParamValue(paramMap, "timeStamp");
        if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(signature) ||
                StringUtils.isEmpty(id) ||
                StringUtils.isEmpty(randomNum) || StringUtils.isEmpty(timeStamp)) {
            LOGGER.error("invalid parameter.appKey=" + appKey + ",signature=" + signature + ",id=" + id + ",randomNum=" + randomNum + ",timeStamp=" + timeStamp);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "invalid parameter.appKey or signature or id or randomNum or timeStamp is null.");
        }

        User user = UserContext.current().getUser();
        if (user == null || user.getId() == User.ANNONYMOUS_UID) {
            UserInfo userInfo = this.getBizUserInfo(signature, appKey, id, String.valueOf(randomNum), String.valueOf(timeStamp));
            String deviceIdentifier = DeviceIdentifierType.INNER_LOGIN.name();
            String pusherIdentify = null;
            UserLogin login = this.userService.innerLogin(userInfo.getNamespaceId(), userInfo.getId(), deviceIdentifier, pusherIdentify);
            LoginToken logintoken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), null);
            String tokenString = WebTokenGenerator.getInstance().toWebToken(logintoken);
            WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response);
            return logintoken;
        }
        return null;
    }

    private static String getParamValue(Map<String, String[]> paramMap, String paramName) {
        String[] values = paramMap.get(paramName);
        if (values != null)
            return StringUtils.join(values, ',');
        return null;
    }

    private void setupUserContext(LoginToken loginToken) {
        UserLogin login = this.userService.findLoginByToken(loginToken);
        UserContext context = UserContext.current();
        context.setLogin(login);

        User user = this.userProvider.findUserById(login.getUserId());
        context.setUser(user);
    }

    private UserInfo getBizUserInfo(String zlSignature, String zlAppKey, String id, String randomNum, String timeStamp) {
        //2016-07-29:modify by liujinwne,parameter name don't be signed.

        try {
            //20170307 modify by sw, through local service method invoked
            GetUserInfoByIdCommand cmd = new GetUserInfoByIdCommand();
            cmd.setId(Long.valueOf(id));
            cmd.setZlSignature(zlSignature);
            cmd.setZlAppKey(zlAppKey);
            cmd.setRandomNum(Integer.valueOf(randomNum));
            cmd.setTimeStamp(Long.valueOf(timeStamp));
            UserInfo userInfo = userService.getUserInfoById(cmd);
//            String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
//            String getUserInfoUri = homeUrl + "/evh/openapi/getUserInfoById";
//            String appKey = configurationProvider.getValue(ZUOLIN_APP_KEY, "f9392ce2-341b-40c1-9c2c-99c702215535");
//            App app = appProvider.findAppByKey(appKey);
//            if (app == null) {
//                LOGGER.error("app nou found.appKey=" + appKey);
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "app not found");
//            }
//
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("appKey", appKey);
//            params.put("zlAppKey", zlAppKey);
//            params.put("zlSignature", zlSignature);
//            params.put("id", id);
//            params.put("randomNum", randomNum);
//            params.put("timeStamp", timeStamp);
//            String signature = SignatureHelper.computeSignature(params, app.getSecretKey());
//
//            String param = String.format("%s?zlSignature=%s&zlAppKey=%s&id=%s&randomNum=%s&timeStamp=%s&appKey=%s&signature=%s",
//                    getUserInfoUri,
//                    URLEncoder.encode(zlSignature, "UTF-8"), zlAppKey,
//                    id,
//                    randomNum, timeStamp,
//                    appKey, URLEncoder.encode(signature, "UTF-8"));
//            Clients ci = new Clients();
//            String responseString = ci.restCall("GET", param, null, null, null);
//
//            LOGGER.error("getBizUserInfo the response of getUserInfoById , responseString={}", responseString);
//
//            Gson gson = new Gson();
//            CommonRestResponse<UserInfo> userInfoRestResponse = gson.fromJson(responseString, new TypeToken<CommonRestResponse<UserInfo>>() {
//            }.getType());
//            UserInfo userInfo = (UserInfo) userInfoRestResponse.getResponse();
            if (userInfo == null) {
                LOGGER.error("userInfo don't get.appKey=" + zlAppKey + ",signature=" + zlSignature + ",zlAppKey=" + zlAppKey + ",zlSignature=" + zlSignature + ",id=" + id + ",randomNum=" + randomNum + ",timeStamp=" + timeStamp);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "userInfo don't get.");
            }
            return userInfo;
        } catch (Exception e) {
            LOGGER.error("getUserInfo method error", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "getUserInfo method error.");
        }
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
