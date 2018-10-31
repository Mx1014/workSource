package com.everhomes.wx;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LogonCommandResponse;
import com.everhomes.rest.wx.*;
import com.everhomes.user.*;
import com.everhomes.util.*;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserGender;

/**
 * 由于要使拦截器工作，URL必须有serverContenxt(如evh），为了符合此规则，需要为微信申请授权及授权回调定义两个接口；
 * 配置了拦截器WebRequestWeixinInterceptor来拦截这两个接口，它的实现逻辑在拦截器中完成。而在controller定义
 * 这两个接口纯属是为了能够在日志中打印出符合标准 的日志，没有其它用途。
 * 
 * 没有特殊原因，不要在此controller增加其它接口。
 *
 */
@RestDoc(value = "WX Auth Controller", site = "core")
@RestController
@RequestMapping("/wxauth")
public class WXAuthController implements ApplicationListener<ContextRefreshedEvent> {// extends ControllerBase
	private static final Logger LOGGER = LoggerFactory.getLogger(WXAuthController.class);
    
    private final static String KEY_NAMESPACE = "ns";
    private final static String KEY_CODE = "code";
    private final static String KEY_STATE = "state";
    private final static String COMMUNITY = "community";
    
    /** 用于记录登录后要跳转的源链接 */
    private final static String KEY_SOURCE_URL = "src_url";

    /** 用于记录是否需要检查手机默认0-不检查，1-检查，检查结果是没有绑定的话跳到绑定手机页面 */
    private final static String BINDPHONE = "bindPhone";

    
    /** 用于记录拿到code之后要跳转的链接（由于微信只允许配置一个回调域名，需要通过些配置来把code送到其它域名） */
    private final static String KEY_CODE_URL = "code_url";
    
    private final static String WX_AUTH_REQ_URL = "/wxauth/authReq";
    
    private final static String WX_AUTH_CALLBACK_URL = "/wxauth/authCallback";
    
    private final static String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final static String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    private final static String WX_SCOPE_SNSAPI_USERINFO = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
    private final static String WX_SCOPE_SNSAPI_BASE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";

    @Autowired
    private UserService userService;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Value("${server.contextPath:}")
    private String contextPath;
    
    private CloseableHttpClient httpClient;
    
    private HttpContext httpClientContext;

    @Autowired
    private ContentServerService contentServerService;


    @Autowired
    private BorderProvider borderProvider;

    @Autowired
    private WeChatService wechatService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            openHttpClient();
        }
    }

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    private CloseableHttpClient openHttpClient() {
        if (isHttpClientOpen()) {
            return this.httpClient;
        }

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
        this.httpClientContext = HttpClientContext.create();

        if (this.httpClient == null) {
            throw new IllegalStateException("Unable to create HttpClient object");
        }
        
        return this.httpClient;
    }  
    
    private boolean isHttpClientOpen() {
        return this.httpClient != null;
    }
    
	/**
	 * <b>URL: /wxauth/authReq</b>
	 * <p>请求微信授权。</p>
	 */
	@RequestMapping("authReq")
	//@RestReturn(String.class)
	//@RequireAuthentication(false)
	public void authReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(req calculate), startTime={}", startTime);
        }
        
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        String requestUrl = request.getRequestURL().toString();
        Map<String, String> params = getRequestParams(request);
        
        // 记录域空间
        String ns = params.get(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(ns);

        // 记录需要接收code的URL
        String codeUrl = params.get(KEY_CODE_URL);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request, requestUrl={}, params={}", requestUrl, params);
        }
        
        // 如果配置了接收code的URL，代表接收到的code不属于本服务器域名的处理范畴，也就是不会在本服务器登录，
        // 此时需要向微信获取到code，并把code转给另外一台服务器
        if(codeUrl != null && codeUrl.trim().length() > 0) {
            sendAuthRequestToWeixin(namespaceId, sessionId, params, response);
            return;
        }
        
        LoginToken loginToken = userService.getLoginToken(request);
        // 没有登录，则请求微信授权
        // 因为公众号有一对多的情况，需要增加检查域空间checkUserNamespaceId，当域空间不一致时用户登出，并且需要重新走授权登录流程。   add by yanjun 20170620
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
            sendAuthRequestToWeixin(namespaceId, sessionId, params, response);
            return;
        }

        //检查Identifier数据或者手机是否存在，不存在则跳到手机绑定页面  add by yanjun 20170831
        checkRedirectUserIdentifier(request, response, namespaceId, params);

        // 登录成功则跳转到原来访问的链接
        String sourceUrl = params.get(KEY_SOURCE_URL);

        //将参数拼接到链接中传给页面 add by yanjun 20170918
        params.remove(KEY_SOURCE_URL);
        sourceUrl = appendParamToUrl(sourceUrl, params);

        redirectByWx(response, sourceUrl);
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(req calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
	}


    /**
     * <b>URL: /wxauth/checkAuth</b>
     * <p>检查用户是否已经登录，由于公众号是公用的，此处要带上ns</p>
     */
    @RequestMapping("checkAuth")
    @RestReturn(CheckAuthResponse.class)
    @RequireAuthentication(false)
    public RestResponse checkAuth(CheckAuthCommand cmd, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CheckAuthResponse checkAuthResponse = new CheckAuthResponse();

        LoginToken loginToken = userService.getLoginToken(request);
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(cmd.getNs())) {
            checkAuthResponse.setStatus((byte)0);
            WebRequestInterceptor.setCookieInResponse("token", "", request, response);
        }else {
            checkAuthResponse.setStatus((byte)1);
            String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
            checkAuthResponse.setLoginToken(tokenString);
            checkAuthResponse.setUid(loginToken.getUserId());
            checkAuthResponse.setContentServer(contentServerService.getContentServer());
        }

        RestResponse res = new RestResponse(checkAuthResponse);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
	/**
	 * <b>URL: /wxauth/authCallback</b>
	 * <p>微信授权后回调，回调包含了code，通过code可换取access_token，通过access_token可获取用户信息。</p>
	 */
	@RequestMapping("authCallback")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public void authCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback calculate), startTime={}", startTime);
        }
	    
        String requestUrl = request.getRequestURL().toString();
        Map<String, String> params = getRequestParams(request);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback), requestUrl={}, params={}", requestUrl, params);
        }
        
        String namespaceInReq = params.get(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(namespaceInReq);
        
        // 当配置了code_url时，说明从微信回调得到的code不是本服务器使用的，需要把code信息转给code_url对应的API；
        // 这是由于微信回调只支持配置一个域名，且是严格校验的（即子域名也是严格匹配的），为了能够使得支持多个域名的回调，
        // 需要在此配置一个中转，即在此接收到后再转给其它域名。
        String codeUrl = params.get(KEY_CODE_URL);
        if(codeUrl != null) {
            codeUrl = URLDecoder.decode(codeUrl, "UTF8");
            params.remove(KEY_CODE_URL);
            codeUrl = appendParamToUrl(codeUrl, params);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.info("Process weixin auth request(callback), redirect code to other domain, codeUrl={}", codeUrl);
            }
            
            redirectByWx(response, codeUrl);
        } else {
            LoginToken loginToken = userService.getLoginToken(request);

            // 因为公众号有一对多的情况，需要增加检查域空间checkUserNamespaceId方法，当域空间不一致时用户登出。   add by yanjun 20170620
            if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
                // 如果是微信授权回调请求，则通过该请求来获取到用户信息并登录
                processUserInfo(namespaceId, request, response);
            }

            //检查Identifier数据或者手机是否存在，不存在则跳到手机绑定页面  add by yanjun 20170831
            checkRedirectUserIdentifier(request, response, namespaceId, params);

            String sourceUrl = params.get(KEY_SOURCE_URL);
            //将参数拼接到链接中传给页面 add by yanjun 20170918
            params.remove(KEY_SOURCE_URL);
            sourceUrl = appendParamToUrl(sourceUrl, params);

            redirectByWx(response, sourceUrl);
            
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
	}

    /**
     * <b>URL: /wxauth/authCallbackByApp</b>
     * <p>微信授权后由APP调用，参数包含了code，通过code可换取access_token，通过access_token可获取用户信息。</p>
     */
    @RequestMapping("authCallbackByApp")
    @RestReturn(CheckWxAuthIsBindPhoneRestResponse.class)
    @RequireAuthentication(false)
    public CheckWxAuthIsBindPhoneRestResponse authCallbackByApp(WxAuthCallBackCommand cmd, HttpServletRequest request, HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback calculate), startTime={}", startTime);
        }

        String requestUrl = request.getRequestURL().toString();
        Map<String, String> params = getRequestParams(request);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback), requestUrl={}, params={}", requestUrl, params);
        }

        String namespaceInReq = params.get("namespaceId");
        Integer namespaceId = parseNamespace(namespaceInReq);

        CheckWxAuthIsBindPhoneResponse checkWxAuthIsBindPhoneResponse = processUserInfoV2(namespaceId, request, response, cmd);

        CheckWxAuthIsBindPhoneRestResponse checkWxAuthIsBindPhoneRestResponse = new CheckWxAuthIsBindPhoneRestResponse();
        checkWxAuthIsBindPhoneRestResponse.setResponse(checkWxAuthIsBindPhoneResponse);
        checkWxAuthIsBindPhoneRestResponse.setErrorCode(ErrorCodes.SUCCESS);
        checkWxAuthIsBindPhoneRestResponse.setErrorDescription("OK");
        return checkWxAuthIsBindPhoneRestResponse;
    }

    /**
     * <b>URL: /wxauth/redirect</b>
     * <p>统一用户重定向时，先重定向到这里，然后core server再重定向到目标url</p>
     */
    @RequestMapping("redirect")
    @RestReturn(String.class)
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = getRequestParams(request);

        String redirectUrl = params.get("callbackUrl");
        params.remove("callbackUrl");
        redirectUrl = URLDecoder.decode(redirectUrl, "UTF8");
        LOGGER.info("redirect params = {}, keys = {}", params, params.keySet());
        for (String key : params.keySet()) {
            Cookie cookie = new Cookie(key, params.get(key));
            cookie.setPath("/");
            cookie.setMaxAge(7000);
            response.addCookie(cookie);
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("redirect url to {}", redirectUrl);
        }
        redirectByWx(response,redirectUrl);
    }







    private List<String> listAllBorderAccessPoints() {
        List<Border> borders = this.borderProvider.listAllBorders();
        return borders.stream().map((Border border) -> {
            return String.format("%s:%d", border.getPublicAddress(), border.getPublicPort());
        }).collect(Collectors.toList());
    }
	private void checkRedirectUserIdentifier(HttpServletRequest request, HttpServletResponse response, Integer namespaceId, Map<String, String> params){
        LOGGER.info("checkUserIdentifier start");

        String bandphone = params.get(BINDPHONE);
        if(bandphone == null || Byte.valueOf(bandphone).byteValue() == 0){
            LOGGER.info("checkUserIdentifier do not need checkout bindphone");
            return;
        }

        //检查Identifier数据或者手机是否存在，不存在则跳到手机绑定页面  add by yanjun 20170831
        User user = UserContext.current().getUser();
        if(user == null){
            LOGGER.error("checkUserIdentifier exception, it should be in logon status, but it is logoff");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to get usercontent");
        }
        UserIdentifier identifier = userService.getUserIdentifier(user.getId());
        if( identifier == null || identifier.getIdentifierToken() == null){

            LOGGER.info("checkUserIdentifier fail user={},identifier={}", StringHelper.toJsonString(user), StringHelper.toJsonString(identifier));
            String homeUrl = configurationProvider.getValue(namespaceId, "home.url", "");
            String bindPhoneUrl = configurationProvider.getValue(namespaceId, WeChatConstant.WX_BIND_PHONE_URL, "");
            LOGGER.info("checkUserIdentifier fail redirect to bind phone Url, homeUrl={}, url={}", homeUrl, bindPhoneUrl);
            String url =homeUrl + bindPhoneUrl;
            try{
                url = appendParamToUrl(url, params);
            }catch (Exception ex){
                LOGGER.error("checkUserIdentifier exception, append param to url error");
            }

            redirectByWx(response, url);
        }else {
            LOGGER.info("checkUserIdentifier success");
        }

    }

    private CheckWxAuthIsBindPhoneResponse checkRedirectUserIdentifierV2(CheckWxAuthIsBindPhoneResponse response, User user){
        LOGGER.info("checkUserIdentifier start");

        //检查Identifier数据或者手机是否存在，不存在则跳到手机绑定页面  add by yanjun 20170831
        if(user == null){
            LOGGER.error("checkUserIdentifier exception, it should be in logon status, but it is logoff");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to get usercontent");
        }
        UserIdentifier identifier = userService.getUserIdentifier(user.getId());
        if( identifier == null || identifier.getIdentifierToken() == null){
            response.setBindType(WxAuthBindPhoneType.NOT_BIND.getCode());
        }else {
            LOGGER.info("checkUserIdentifier success");
            response.setBindType(WxAuthBindPhoneType.BIND.getCode());
            response.setIdentifierToken(identifier.getIdentifierToken());
            response.setRegionCode(identifier.getRegionCode());
        }
        return response;
    }

	private Map<String, String> getRequestParams(HttpServletRequest request) {
	    Map<String, String> params = new HashMap<String, String>();
	    Enumeration<String> em = request.getParameterNames();
	    while (em.hasMoreElements()) {
	       String name = em.nextElement();
	       String value = request.getParameter(name);
	       params.put(name, value);
	   }
	    
	    return params;
	}
	
	private String appendParamToUrl(String url, Map<String, String> params) throws Exception {
	    String baseUrl = null;
	    String hashUrl = "";
	    
	    // 若有#段，则先分离该段
	    int pos = url.indexOf('#');
	    if(pos != -1) {
	        baseUrl = url.substring(0, pos);
	        hashUrl = url.substring(pos);
	    } else {
	        baseUrl = url;
	    }

        // 如果hash有"?"，则hash可能带有参数，并且确定base不会有参数，如果base还有参数的话本身就是错误的（不考虑这种情况）。
        pos = hashUrl.indexOf('?');
        if(pos != -1) {
            String sourceParam = hashUrl.substring(pos);
            baseUrl = baseUrl + sourceParam;
            hashUrl = hashUrl.substring(0, pos);
        }
	    
	    // 若有问号，代表着有参数
	    pos = baseUrl.indexOf('?');
        if(pos == -1) {
            baseUrl = baseUrl + "?";
        } else {
            if(baseUrl.length() - 1 > pos) {
                baseUrl = baseUrl + "&";
            }
        }
        
        boolean isFirst = true;
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        while(iterator.hasNext()) {
            if(!isFirst) {
                baseUrl = baseUrl + "&";
            }
            Entry<String, String> entry = iterator.next();
            baseUrl += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF8");
            isFirst = false;
        }
        
        return baseUrl + hashUrl;
	}
	
	private void sendAuthRequestToWeixin(Integer namespaceId, String sessionId, Map<String, String> params, 
	        HttpServletResponse response) throws Exception {
        String wxAuthCallbackUrl = configurationProvider.getValue(namespaceId, "wx.auth.callback.url", WX_AUTH_CALLBACK_URL);
        String callbackUrl =  configurationProvider.getValue(namespaceId, "home.url", "") + contextPath + wxAuthCallbackUrl;
        callbackUrl = appendParamToUrl(callbackUrl, params);

        String appId = configurationProvider.getValue(namespaceId, WeChatConstant.WX_OFFICAL_ACCOUNT_APPID, "");

        String scope = params.get("scope");
        String authorizeUri;

        //静默授权、一般授权
        if(scope != null && scope.equals("snsapi_base")){
            authorizeUri  = String.format(WX_SCOPE_SNSAPI_BASE, appId, URLEncoder.encode(callbackUrl, "UTF-8"), sessionId);
        }else {
            authorizeUri  = String.format(WX_SCOPE_SNSAPI_USERINFO, appId, URLEncoder.encode(callbackUrl, "UTF-8"), sessionId);
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(send auth to weixin), authorizeUrl={}, callbackUrl={}", authorizeUri, callbackUrl);
        }
        
        response.sendRedirect(authorizeUri);
	}
	
	/**
	 * 除了授权的跳转外，由于微信不提供其它URL的302跳转，需要使用JS的方式进行跳转；
	 * @param response
	 * @param redirectUrl
	 */
	private void redirectByWx(HttpServletResponse response, String redirectUrl) {
	    if(LOGGER.isDebugEnabled()) {
	        LOGGER.debug("Process weixin auth request(redirect), redirectUrl={}", redirectUrl);
	    }
	    
	    response.setContentType("text/html; charset=utf8");  
	    PrintWriter out = null;
	    try {
	        User user = new User();
	        String title = localeStringService.getLocalizedString(OAuth2ServiceErrorCode.SCOPE, 
	            String.valueOf(OAuth2ServiceErrorCode.ERROR_REDIRECTING), user.getLocale(), "Redirecting...");
	        out = response.getWriter();
	        out.println("<!DOCTYPE html>"); 
	        out.println("<html lang=\"en\">"); 
	        out.println("<head>"); 
	        out.println("<meta charset=\"UTF-8\">"); 
	        out.println("<title>" + title + "</title>"); 
	        out.println("</head>"); 
	        out.println("<body>"); 
	        out.println("<script>"); 
	        out.println("location.href=\"" + redirectUrl + "\";"); 
	        out.println("</script>"); 
	        out.println("</body>"); 
	        out.println("</html>"); 
	    } catch(Exception e) {
	        LOGGER.error("Failed to print ouput by response, redirectUrl={}", redirectUrl, e);
	    } finally {
	        if(out != null) {
	            try {
	                out.close();
	            } catch(Exception e) {
	                LOGGER.error("Failed to close response writer, redirectUrl={}", redirectUrl, e);
	            }
	        }
	    }
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
    
    private UserLogin processUserInfo(Integer namespaceId, HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(userinfo calculate), startTime={}", startTime);
        }

        String appId = configurationProvider.getValue(namespaceId, WeChatConstant.WX_OFFICAL_ACCOUNT_APPID, "");
        String secret = configurationProvider.getValue(namespaceId, WeChatConstant.WX_OFFICAL_ACCOUNT_SECRET, "");

        // 微信提供的临时code
        String code = request.getParameter("code");
        
        // 使用临时code从微信中换取accessToken
        String accessTokenUri = String.format(WX_ACCESS_TOKEN_URL, appId, secret, code);
        String accessTokenUriWithoutSecret = accessTokenUri.replaceAll("secret=" + secret, "secret=****");
        String accessTokenJson = wechatService.httpGet(accessTokenUri, accessTokenUriWithoutSecret);
        WxAccessTokenInfo accessToken = (WxAccessTokenInfo)StringHelper.fromJsonString(accessTokenJson, WxAccessTokenInfo.class);
        if (accessToken.getErrcode() != null) {
            LOGGER.error("Failed to get access token from webchat, namespaceId={}, appId={}, accessToken={}, accessTokenUri={}", 
                namespaceId, appId, accessTokenJson, accessTokenUriWithoutSecret);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get access token from webchat");
        }

        User wxUser = new User();
        wxUser.setNamespaceId(namespaceId);
        wxUser.setNamespaceUserType(NamespaceUserType.WX.getCode());
        wxUser.setNamespaceUserToken(accessToken.getOpenid());

        String scope = request.getParameter("scope");
        //静默授权不用获取用户信息
        if(scope != null && scope.equals("snsapi_base")){
            wxUser.setNickName("微信用户");
            wxUser.setGender(UserGender.UNDISCLOSURED.getCode());
        }else {
            // 获取用户信息
            String userInfoUri = String.format(WX_USER_INFO_URL, accessToken.getAccess_token(), accessToken.getOpenid());
            String userInfoJson = wechatService.httpGet(userInfoUri, userInfoUri);
            WxUserInfo userInfo = (WxUserInfo)StringHelper.fromJsonString(userInfoJson, WxUserInfo.class);
            if (userInfo.getErrcode()!=null) {
                LOGGER.error("Failed to get user information from webchat, namespaceId={}, appId={}, userInfo={}, userinfoUri={}",
                        namespaceId, appId, userInfoJson, userInfoUri);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get user information from webchat");
            }
            wxUser.setNickName(userInfo.getNickname());
            wxUser.setAvatar(userInfo.getHeadimgurl());
            // 0: undisclosured, 1: male, 2: female 微信的值恰好与左邻的定义是一致的
            UserGender gender = UserGender.fromCode(userInfo.getSex());
            wxUser.setGender(gender.getCode());
        }
        
        userService.signupByThirdparkUser(wxUser, request);

        //signupByThirdparkUser里面已经设置过一次了但是setDefaultCommunity有个问题，但是有一个问题setDefaultCommunity限定了对应的namespaceResource只能是1。
        //add by yanjun 201805091940
        userService.setDefaultCommunityForWx(wxUser.getId(), namespaceId);

        UserLogin userLogin = userService.logonBythirdPartUser(wxUser.getNamespaceId(), wxUser.getNamespaceUserType(), wxUser.getNamespaceUserToken(), request, response);

        // 添加CurrentUser用于后期，检查identifi add by yanjun 20170926
        if(userLogin != null && userLogin.getUserId() != 0){
            wxUser.setId(userLogin.getUserId());
            UserContext.setCurrentUser(wxUser);
        }

        //by dengs,加个communityid参数，加到用户的eh_profiles中,2017.08.28
        String communityId = request.getParameter(COMMUNITY);
        if(wxUser.getId()!=null && communityId!=null){
        	try{
        		Long.valueOf(communityId);
        	}catch(Exception e){
        		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
        				"unknown community = " + communityId);
        	}
        	userService.updateUserCurrentCommunityToProfile(wxUser.getId(), Long.valueOf(communityId), wxUser.getNamespaceId());
        }

        LOGGER.info("UserContext user = {}", UserContext.current().getUser());

        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(userinfo calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
        return userLogin;
    }

    private CheckWxAuthIsBindPhoneResponse processUserInfoV2(Integer namespaceId, HttpServletRequest request, HttpServletResponse response, WxAuthCallBackCommand cmd) {
        CheckWxAuthIsBindPhoneResponse checkWxAuthIsBindPhoneResponse = new CheckWxAuthIsBindPhoneResponse();
        long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(userinfo calculate), startTime={}", startTime);
        }

        String appId = request.getParameter("appId");
        String secret = request.getParameter("secret");

        // 微信提供的临时code
        String code = request.getParameter("code");

        // 使用临时code从微信中换取accessToken
        String accessTokenUri = String.format(WX_ACCESS_TOKEN_URL, appId, secret, code);
        String accessTokenUriWithoutSecret = accessTokenUri.replaceAll("secret=" + secret, "secret=****");
        String accessTokenJson = wechatService.httpGet(accessTokenUri, accessTokenUriWithoutSecret);
        WxAccessTokenInfo accessToken = (WxAccessTokenInfo)StringHelper.fromJsonString(accessTokenJson, WxAccessTokenInfo.class);
        if (accessToken.getErrcode() != null) {
            LOGGER.error("Failed to get access token from webchat, namespaceId={}, appId={}, accessToken={}, accessTokenUri={}",
                    namespaceId, appId, accessTokenJson, accessTokenUriWithoutSecret);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to get access token from webchat");
        }

        User wxUser = new User();
        wxUser.setNamespaceId(namespaceId);
        wxUser.setNamespaceUserType(NamespaceUserType.WX.getCode());
        wxUser.setNamespaceUserToken(accessToken.getOpenid());

        String scope = request.getParameter("scope");
        //静默授权不用获取用户信息
        if(scope != null && scope.equals("snsapi_base")){
            wxUser.setNickName("微信用户");
            wxUser.setGender(UserGender.UNDISCLOSURED.getCode());
        }else {
            // 获取用户信息
            String userInfoUri = String.format(WX_USER_INFO_URL, accessToken.getAccess_token(), accessToken.getOpenid());
            String userInfoJson = wechatService.httpGet(userInfoUri, userInfoUri);
            WxUserInfo userInfo = (WxUserInfo)StringHelper.fromJsonString(userInfoJson, WxUserInfo.class);
            if (userInfo.getErrcode()!=null) {
                LOGGER.error("Failed to get user information from webchat, namespaceId={}, appId={}, userInfo={}, userinfoUri={}",
                        namespaceId, appId, userInfoJson, userInfoUri);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get user information from webchat");
            }
            wxUser.setNickName(userInfo.getNickname());
            wxUser.setAvatar(userInfo.getHeadimgurl());
            // 0: undisclosured, 1: male, 2: female 微信的值恰好与左邻的定义是一致的
            UserGender gender = UserGender.fromCode(userInfo.getSex());
            wxUser.setGender(gender.getCode());
        }

        wxUser = userService.signupByThirdparkUserByApp(wxUser, request);

        //signupByThirdparkUser里面已经设置过一次了但是setDefaultCommunity有个问题，但是有一个问题setDefaultCommunity限定了对应的namespaceResource只能是1。
        //add by yanjun 201805091940
        userService.setDefaultCommunityForWx(wxUser.getId(), namespaceId);
        checkRedirectUserIdentifierV2(checkWxAuthIsBindPhoneResponse,wxUser);
        if (checkWxAuthIsBindPhoneResponse.getBindType() == WxAuthBindPhoneType.BIND.getCode()) {
            UserLogin userLogin = userService.logonBythirdPartAppUser(wxUser.getNamespaceId(), wxUser.getNamespaceUserType(), wxUser.getNamespaceUserToken(), request, response);

            // 添加CurrentUser用于后期，检查identifi add by yanjun 20170926
            if(userLogin != null && userLogin.getUserId() != 0){
                wxUser.setId(userLogin.getUserId());
                UserContext.setCurrentUser(wxUser);
                LoginToken token = new LoginToken(userLogin.getUserId(), userLogin.getLoginId(), userLogin.getLoginInstanceNumber(), userLogin.getImpersonationId());
                String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
                WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response);
                // 当从园区版登录时，有指定的namespaceId，需要对这些用户进行特殊处理
                Integer ns = UserContext.getCurrentNamespaceId(namespaceId);
                if(ns != null) {
                    WebRequestInterceptor.setCookieInResponse("namespace_id", String.valueOf(ns), request, response);
                    userService.setDefaultCommunity(userLogin.getUserId(), ns);
                }
                checkWxAuthIsBindPhoneResponse.setUid(userLogin.getUserId());
                checkWxAuthIsBindPhoneResponse.setLoginToken(tokenString);
                checkWxAuthIsBindPhoneResponse.setAccessPoints(listAllBorderAccessPoints());
                checkWxAuthIsBindPhoneResponse.setContentServer(contentServerService.getContentServer());
            }
        }else {
            LOGGER.info("wxUser={}",wxUser);
            checkWxAuthIsBindPhoneResponse.setUid(wxUser.getId());
        }

        //by dengs,加个communityid参数，加到用户的eh_profiles中,2017.08.28
        String communityId = request.getParameter(COMMUNITY);
        if(wxUser.getId()!=null && communityId!=null){
            try{
                Long.valueOf(communityId);
            }catch(Exception e){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "unknown community = " + communityId);
            }
            userService.updateUserCurrentCommunityToProfile(wxUser.getId(), Long.valueOf(communityId), wxUser.getNamespaceId());
        }

        return checkWxAuthIsBindPhoneResponse;
    }

    
    @RequestMapping("cross")
    public void crossSiteRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = getRequestParams(request);
        String crossUrl = params.get("crossUrl");
        if(crossUrl != null && crossUrl.trim().length() > 0) {
            String targetUrl = URLDecoder.decode(crossUrl, "UTF-8");
            redirect(response, targetUrl);
        }
    }
    
    private void redirect(HttpServletResponse response, String targetUrl) {
        CloseableHttpClient client = openHttpClient();
        CloseableHttpResponse res = null;
        InputStream in = null;
        ServletOutputStream out = null;
        
        try {
            HttpGet post = new HttpGet(targetUrl);
            res = client.execute(post, this.httpClientContext);
            HttpEntity resEntity = res.getEntity();
            in = resEntity.getContent();
            out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = -1;
            long totalLength = 0;
            while((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                totalLength += len;
            }
            
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Redirect url, length={}, url={}", totalLength, targetUrl);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to redirect url, url={}", targetUrl, e);
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch(Exception e) {
                    LOGGER.error("Failed to close ServletOutputStream", e);
                }
            }
            
            if(in != null) {
                try {
                    in.close();
                } catch(Exception e) {
                    LOGGER.error("Failed to close target IputStream", e);
                }
            }
            
            if(res != null) {
                try {
                    res.close();
                } catch(Exception e) {
                    LOGGER.error("Failed to close CloseableHttpResponse", e);
                }
            }
        }
    }

    //检出当前登录用户域空间和需要登录的域空间是否相同，不同则登出    add by yanjun 20170620
    private boolean checkUserNamespaceId(Integer namespaceId){

	    Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
        LOGGER.info("checkUserNamespaceId, namespaceId={}, currentNamespaceId={}", namespaceId, currentNamespaceId);
        if(currentNamespaceId == null){
            return false;
        }

        if(namespaceId == null || namespaceId.intValue() != currentNamespaceId.intValue()){
            if(UserContext.current() != null){
                UserLogin login = UserContext.current().getLogin();
                if(login != null){
                    userService.logoff(login);
                    LOGGER.info("checkUserNamespaceId, userService.logoff");
                }
            }
            return false;
        }

        return true;


    }
}
