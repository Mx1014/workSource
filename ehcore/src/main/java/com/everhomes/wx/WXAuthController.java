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

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.everhomes.rest.RestResponse;
import com.everhomes.user.*;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SimpleConvertHelper;
import com.everhomes.util.StringHelper;

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
public class WXAuthController {// extends ControllerBase
	private static final Logger LOGGER = LoggerFactory.getLogger(WXAuthController.class);
    
    private final static String KEY_NAMESPACE = "ns";
    private final static String KEY_CODE = "code";
    private final static String KEY_STATE = "state";
    
    /** 用于记录登录后要跳转的源链接 */
    private final static String KEY_SOURCE_URL = "src_url";
    
    /** 用于记录拿到code之后要跳转的链接（由于微信只允许配置一个回调域名，需要通过些配置来把code送到其它域名） */
    private final static String KEY_CODE_URL = "code_url";
    
    private final static String WX_AUTH_REQ_URL = "/wxauth/authReq";
    
    private final static String WX_AUTH_CALLBACK_URL = "/wxauth/authCallback";
    
    private final static String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final static String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    
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
    
    @PostConstruct
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
        LOGGER.info("Process weixin auth request, loginToken={}", loginToken);
        String sourceUrl = params.get(KEY_SOURCE_URL);
        redirectByWx(response, sourceUrl);
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(req calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
	}


    /**
     * <b>URL: /wxauth/checkAuth</b>
     * <p>检查是否已经登录，没有登录则发起授权。此接口与authReq接口的区别是此接口检查授权过后是直接返回的，而authReq是重定向到src_url的</p>
     */
    @RequestMapping("checkAuth")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse checkAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, String> params = getRequestParams(request);

        // 记录域空间
        String ns = params.get(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(ns);

        LoginToken loginToken = userService.getLoginToken(request);
        // 没有登录，则请求微信授权
        // 因为公众号有一对多的情况，需要增加检查域空间checkUserNamespaceId，当域空间不一致时用户登出，并且需要重新走授权登录流程。   add by yanjun 20170620
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
            //此处会重定向
            authReq(request, response);
        }

        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
	/**
	 * <b>URL: /wx/authCallback</b>
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
            redirectByWx(response, sourceUrl);
            
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(callback calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
	}

	private void checkRedirectUserIdentifier(HttpServletRequest request, HttpServletResponse response, Integer namespaceId, Map<String, String> params){
        LOGGER.info("checkUserIdentifier start");

        //检查Identifier数据或者手机是否存在，不存在则跳到手机绑定页面  add by yanjun 20170831
        User user = UserContext.current().getUser();
        if(user == null){
            LOGGER.error("checkUserIdentifier exception, it should be in logon status, but it is logoff");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to get usercontent");
        }
        UserIdentifier identifier = userService.getUserIdentifier(user.getId());
        if( identifier == null || identifier.getIdentifierToken() == null){
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
        }

        LOGGER.info("checkUserIdentifier success");

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

        String authorizeUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s"
                + "&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", appId,
                URLEncoder.encode(callbackUrl, "UTF-8"), sessionId);
        
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
    
    private void processUserInfo(Integer namespaceId, HttpServletRequest request, HttpServletResponse response) {
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
        String accessTokenJson = httpGet(accessTokenUri, accessTokenUriWithoutSecret);
        WxAccessTokenInfo accessToken = (WxAccessTokenInfo)StringHelper.fromJsonString(accessTokenJson, WxAccessTokenInfo.class);
        if (accessToken.getErrcode() != null) {
            LOGGER.error("Failed to get access token from webchat, namespaceId={}, appId={}, accessToken={}, accessTokenUri={}", 
                namespaceId, appId, accessTokenJson, accessTokenUriWithoutSecret);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get access token from webchat");
        }
        
        // 获取用户信息
        String userInfoUri = String.format(WX_USER_INFO_URL, accessToken.getAccess_token(), accessToken.getOpenid());
        String userInfoJson = httpGet(userInfoUri, userInfoUri);
        WxUserInfo userInfo = (WxUserInfo)StringHelper.fromJsonString(userInfoJson, WxUserInfo.class);
        if (userInfo.getErrcode()!=null) {
            LOGGER.error("Failed to get user information from webchat, namespaceId={}, appId={}, userInfo={}, userinfoUri={}", 
                namespaceId, appId, userInfoJson, userInfoUri);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get user information from webchat");
        }

        User wxUser = new User();
        wxUser.setNamespaceId(namespaceId);
        wxUser.setNamespaceUserType(NamespaceUserType.WX.getCode());
        wxUser.setNamespaceUserToken(userInfo.getOpenid());
        wxUser.setNickName(userInfo.getNickname());
        wxUser.setAvatar(userInfo.getHeadimgurl());
        // 0: undisclosured, 1: male, 2: female 微信的值恰好与左邻的定义是一致的
        UserGender gender = UserGender.fromCode(userInfo.getSex());
        wxUser.setGender(gender.getCode());
        
        userService.signupByThirdparkUser(wxUser, request);
        userService.logonBythirdPartUser(wxUser.getNamespaceId(), wxUser.getNamespaceUserType(), wxUser.getNamespaceUserToken(), request, response);

        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(userinfo calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
    }
    
    private String httpGet(String url, String safeUrl) {
        CloseableHttpClient httpclient = null;
        
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            response = httpclient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", safeUrl, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                        "Failed to get the http result");
            } else {
                HttpEntity resEntity = response.getEntity();
                String charset = getContentCharSet(resEntity);
                result = EntityUtils.toString(resEntity, charset);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Get http result, charset={}, url={}, result={}", charset, safeUrl, result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the http result, url={}", safeUrl, e);
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
    
    public static String getContentCharSet(final HttpEntity entity) throws ParseException {
        if (entity == null) {   
            throw new IllegalArgumentException("HTTP entity may not be null");   
        }   
        String charset = null;   
        if (entity.getContentType() != null) {    
            HeaderElement values[] = entity.getContentType().getElements();   
            if (values.length > 0) {   
                NameValuePair param = values[0].getParameterByName("charset" );   
                if (param != null) {   
                    charset = param.getValue();   
                }   
            }   
        }   
         
        if(charset == null || charset.length() == 0){  
            charset = "UTF-8";  
        }
        
        return charset;   
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
