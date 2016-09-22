package com.everhomes.wx;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

public class WebRequestWeixinInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestWeixinInterceptor.class);

    @Autowired
    private AppProvider appProvider;
    
    @Autowired
    private UserService userService;
    
    private final static String KEY_NAMESPACE = "ns";
    
    private final static String KEY_SOURCE_URL = "src_url";
    
    private final static String WX_AUTH_CALLBACK_URL = "/wx/authCallback";
    
    private final static String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final static String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Value("${server.contextPath:}")
    private String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURL().toString();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Pre handle weixin request, requestUrl={}", requestUrl);
        }
        
        LoginToken loginToken = userService.getLoginToken(request);
        if(!userService.isValid(loginToken)) {
            if(isWxAuthRequest(request)) {
                HttpSession session = request.getSession();
                Object ns = session.getAttribute(KEY_NAMESPACE);
                Integer namespaceId = parseNamespace(ns);
                
                // 如果是微信授权回调请求，则通过该请求来获取到用户信息并登录
                processUserInfo(namespaceId, request, response);
                
                // 登录成功则跳转到原来访问的链接
                String sourceUrl = (String) session.getAttribute(KEY_SOURCE_URL);

                response.sendRedirect(sourceUrl);
                return false; 
            } else {
                // 没有登录，则请求微信授权
                HttpSession session = request.getSession();
                session.setAttribute(KEY_SOURCE_URL, requestUrl);
                String sessionId = session.getId();
                String ns = request.getParameter(KEY_NAMESPACE);
                Integer namespaceId = parseNamespace(ns);
                session.setAttribute(KEY_NAMESPACE, namespaceId);
                String wxAuthCallbackUrl = configurationProvider.getValue(namespaceId, "wx.auth.callback.url", WX_AUTH_CALLBACK_URL);
                String redirectUri =  configurationProvider.getValue(namespaceId, "home.url", "") + contextPath + wxAuthCallbackUrl;

                String appId = "wx58052b72c7579016";
                String authorizeUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s"
                        + "&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", appId,
                        URLEncoder.encode(redirectUri, "UTF-8"), sessionId); 
                
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.info("Pre handle weixin request, requestUrl={}, authorizeUri={}", requestUrl, authorizeUri);
                }
                
                response.sendRedirect(authorizeUri);
                return false;
            }
        }
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Pre handle weixin request, loginToken={}", loginToken);
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Do nothing
    }
    
    private Integer parseNamespace(Object nsObj) {
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if(nsObj != null) {
            try {
                namespaceId = Integer.parseInt(nsObj.toString().trim());
            } catch (Exception e) {
                LOGGER.error("Failed to parse the namesapce, ns={}", nsObj, e);
            }
        }
        
        return namespaceId;
    }
    
    /**
     * 对于微信
     * @param request
     * @return
     */
    private boolean isWxAuthRequest(HttpServletRequest request) {
        String sourceUrl = request.getRequestURL().toString();
        if(sourceUrl != null && sourceUrl.contains(WX_AUTH_CALLBACK_URL)) {
            return true;
        } else {
            return false;
        }
    }
    
    private void processUserInfo(Integer namespaceId, HttpServletRequest request, HttpServletResponse response) {
        String appId = configurationProvider.getValue(namespaceId, "wx.offical.account.appid", "");
        String secret = configurationProvider.getValue(namespaceId, "wx.offical.account.secret", "");
        // 微信提供的临时code
        String code = request.getParameter("code");
        
        // 使用临时code从微信中换取accessToken
        String accessTokenUri = String.format(WX_ACCESS_TOKEN_URL, appId, secret, code);
        String accessTokenJson = httpGet(accessTokenUri);
        WxAccessTokenInfo accessToken = (WxAccessTokenInfo)StringHelper.fromJsonString(accessTokenJson, WxAccessTokenInfo.class);
        if (accessToken.getErrcode() != null) {
            LOGGER.error("Failed to get access token from webchat, appId={}, accessToken={}, accessTokenUri={}", 
                appId, accessTokenJson, accessTokenUri);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get access token from webchat");
        }
        
        // 获取用户信息
        String userInfoUri = String.format(WX_USER_INFO_URL, accessToken.getAccess_token(), accessToken.getOpenid());
        String userInfoJson = httpGet(userInfoUri);
        WxUserInfo userInfo = (WxUserInfo)StringHelper.fromJsonString(userInfoJson, WxUserInfo.class);
        if (userInfo.getErrcode()!=null) {
            LOGGER.error("Failed to get user information from webchat, appId={}, userInfo={}, userinfoUri={}", appId, userInfoJson, userInfoUri);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get user information from webchat");
        }

        User wxUser = new User();
        wxUser.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
        wxUser.setNamespaceUserType(NamespaceUserType.WX.getCode());
        wxUser.setNamespaceUserToken(userInfo.getOpenid());
        wxUser.setNickName(userInfo.getNickname());
        wxUser.setAvatar(userInfo.getHeadimgurl());
        // 0: undisclosured, 1: male, 2: female 微信的值恰好与左邻的定义是一致的
        UserGender gender = UserGender.fromCode(userInfo.getSex());
        wxUser.setGender(gender.getCode());
        
        userService.signupByThirdparkUser(wxUser, request);
        userService.logonBythirdPartUser(wxUser.getNamespaceId(), wxUser.getNamespaceUserType(), wxUser.getNamespaceUserToken(), request, response);
    }
    
    private String httpGet(String url) {
        CloseableHttpClient httpclient = null;
        
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpPost = new HttpGet(url);
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                        "Failed to get the http result");
            } else {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Get http result, url={}, result={}", url, result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the http result, url={}", url, e);
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if(httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return result;
    }
}
