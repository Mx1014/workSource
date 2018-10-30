package com.everhomes.alipay;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayLogger;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.everhomes.aclink.AclinkConstant;
import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LogonCommandResponse;
import com.everhomes.rest.wx.*;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.wx.WeChatConstant;

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
 * 支付宝对接
 *
 */
@RestDoc(value = "Alipay Auth Controller", site = "core")
@RestController
@RequestMapping("/alipayauth")
public class AlipayAuthController implements ApplicationListener<ContextRefreshedEvent> {// extends ControllerBase
	private static final Logger LOGGER = LoggerFactory.getLogger(AlipayAuthController.class);
    
    private final static String KEY_NAMESPACE = "ns";
    private final static String KEY_STATE = "state";
    
    AlipayClient alipayClient;
    
    /** 用于记录登录后要跳转的源链接 */
    private final static String KEY_SOURCE_URL = "src_url";
        
    
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
    private ConfigurationProvider  configProvider;

    @Autowired
    private BorderProvider borderProvider;

    //@Autowired
    //private WeChatService wechatService;

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
	 * <b>URL: /alipayauth/authReq</b>
	 * <p>请求支付宝授权。</p>
	 */
	@RequestMapping("authReq")
	//@RestReturn(String.class)
	//@RequireAuthentication(false)
	public void authReq(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, String> params = getRequestParams(request);
        
        // 记录域空间
        String ns = params.get(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(ns);
        
        String state = request.getParameter(KEY_STATE);
        
        LoginToken loginToken = userService.getLoginToken(request);

        // 检查域空间checkUserNamespaceId，当域空间不一致时用户登出，并且需要重新走授权登录流程。
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
            sendAuthRequestToAlipay(namespaceId, params, response, state);
            return;
        }

        // 登录成功则跳转到原来访问的链接
        String sourceUrl = params.get(KEY_SOURCE_URL);

        //将参数拼接到链接中传给页面 
        params.remove(KEY_SOURCE_URL);
        sourceUrl = appendParamToUrl(sourceUrl, params);

        redirectByAlipay(response, sourceUrl);
	}


    /**
     * <b>URL: /alipayauth/checkAuth</b>
     * <p>检查用户是否已经登录</p>
     */
    @RequestMapping("checkAuth")
    @RestReturn(CheckAuthResponse.class)
    //@RequireAuthentication(false)
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
	 * <b>URL: /alipayauth/authCallback</b>
	 * <p>支付宝授权后回调，回调包含了code，通过code可换取access_token，通过access_token可获取用户信息。</p>
	 */
	@RequestMapping("authCallback")
	@RestReturn(String.class)
	//@RequireAuthentication(false)
	public void authCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    LOGGER.info("alipay call back start.");
        Map<String, String> params = getRequestParams(request);

        
        String namespaceInReq = params.get(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(namespaceInReq);
        
        LoginToken loginToken = userService.getLoginToken(request);

        // 检查域空间checkUserNamespaceId方法，当域空间不一致时用户登出
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
            // 如果是支付宝授权回调请求，则通过该请求来获取到用户信息并登录
            processUserInfo(namespaceId, request, response);
        }


        String sourceUrl = params.get(KEY_SOURCE_URL);
        //将参数拼接到链接中传给页面
        params.remove(KEY_SOURCE_URL);
        sourceUrl = appendParamToUrl(sourceUrl, params);

        redirectByAlipay(response, sourceUrl);
        LOGGER.info("alipay call back end.");
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
	
	private void sendAuthRequestToAlipay(Integer namespaceId, Map<String, String> params, 
	        HttpServletResponse response, String state) throws Exception {
        String callbackUrl =  configurationProvider.getValue(namespaceId, "home.url", "") + contextPath + "/alipayauth/authCallback";
        callbackUrl = appendParamToUrl(callbackUrl, params);

        String authorizeUri;
        authorizeUri  = URLEncoder.encode(callbackUrl, "UTF-8");
        String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016052301433288&scope=auth_user&redirect_uri=" + authorizeUri;
        LOGGER.info("authorizeUri = " + authorizeUri); 
        response.sendRedirect(url);
	}
	
	/**
	 * 除了授权的跳转外，由于微信不提供其它URL的302跳转，需要使用JS的方式进行跳转；
	 * @param response
	 * @param redirectUrl
	 */
	private void redirectByAlipay(HttpServletResponse response, String redirectUrl) {
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

        // 使用临时auth_code从支付宝中换取accessToken
        String auth_code = request.getParameter("auth_code");
        AlipaySystemOauthTokenRequest r = new AlipaySystemOauthTokenRequest();
        r.setCode(auth_code);
        r.setGrantType("authorization_code");
        AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
	    AlipayUserInfoShareResponse responseUser = new AlipayUserInfoShareResponse();
		try {
		      AlipaySystemOauthTokenResponse oauthTokenResponse = getAlipayClient().execute(r);
			responseUser = getAlipayClient().execute(requestUser, oauthTokenResponse.getAccessToken());
		} catch (Exception e) {
			LOGGER.error("使用authCode获取信息失败！", e);
            return null;
		}
        User alipayUser = new User();
        alipayUser.setNamespaceId(namespaceId);
        alipayUser.setNamespaceUserType(NamespaceUserType.ALIPAY.getCode());
        alipayUser.setNamespaceUserToken(responseUser.getUserId());
        alipayUser.setAvatar(responseUser.getAvatar());
        alipayUser.setNickName(responseUser.getNickName());
        //UserGender gender = UserGender.fromCode(parseGender(responseUser.getGender()));
        //alipayUser.setGender(gender.getCode());
        userService.signupByThirdparkUser(alipayUser, request);

        UserLogin userLogin = userService.logonBythirdPartUser(alipayUser.getNamespaceId(), alipayUser.getNamespaceUserType(), alipayUser.getNamespaceUserToken(), request, response);

        return userLogin;
    }

    private Byte parseGender (String gender){
    	if (gender.equals("f")){
    		return 2;
    	} else if (gender.equals("m")){
    		return 1;
    	}
    	return 0;
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
    
    private AlipayClient getAlipayClient() {
        if(alipayClient == null) {
            synchronized(this) {
                if(alipayClient == null) {
                    String URL = "https://openapi.alipay.com/gateway.do";
                    String APP_ID = "2016052301433288";

                    //TODO better for constant
                    String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1BNn3TgTF/92xg4UZraQJTOQbUJ8fIEpxsvvBQPVK0qWrwScXvosWpic6otceI0SESoEAhupEBcsckgEPawYd8LOajlh1OMMVmchOGWLW0Jmi6wEsLJmNX5C7plx5ZF2QOxCQdEQoLYB47WS3ny1/EhCI4SWgxmgciaaaGCpOPOKCxJnvm4vStdzpGFw4gVhHUTeienCLhH2UKO6tQWXgzTiGRrdts/kvxyrIxEQwlKHEnnd0hOig/BOVRDWQddap1WBl/4Wn3NUtTwKYJjfMEyEKlV/nKjKzSaVGXW8tC9RTHlIF1VoM7i3bv+upTrXfhJctmFw8cjxMnRBTv+lLAgMBAAECggEAeLsZnOW+LUiv1btARJYZN9K+uLEWz627uQWE/6mRcCv6LwmORzngjmSW+XauPv2Kryj5zR4ulNvAUffXGS9qTwHA6C5UNSp8gqWkgOJRl7BdJ9nRWGMOrsm0QFN+jcAw3wdlztKcuqdOblj2ublCoOOtf32amRZ7Tb1JDrFbVf4HNPhYIj/h0syzaKj7QLFlI+e+AMKP+qTxew1EvEmUPW3frlEsCJeF05bggltwtbbU/5ZlJiqCuqlL7hLVGbM+YMrSE12kkP1bifYsl4j5pnwDsAzEsaTaw+5e2GuTZNdn80Thrv645Qx2A1zUIX0JJGmjRMnHdwkkvUJ83nZOoQKBgQD6NGCFr3js3nH7FOku2kj5Ty4shQunaa0LT+PQl0bkIDDIovAQH6/puYHraUXsT9On4gJQVCmGeSCJDGWjuZj0FVZlZZGIPIOmC8ATRoQqRaOgR2aaRgUIA0mS1DRLywTGGrzwGQOslJ/uTYSiNjwPLGyMZG2hcI2EWhkO2y8qWQKBgQC5Njp9Tlf8zJyWd64SH8t4jkm131X41aCySAaRfZJMxBnfA3a8JQJfBpbX+1rDTtow9b3lMvvIpH1iUF7TjJ7Hf4KPS1nsy0huPG5SLt6j4jab8wGrn3kIkq+wYxU26zBRW8MgbGydFYFoKuVG8+FPV9E2SZmb2y0TFpYUOwP0QwKBgQCkC51RaXg2ja5buh0dG/+GfnS+ucinTjEn3ox2ogjX34c8tSAC4lO7QWa+S3qN8tTKGPP9aQEE6vv6/0bQrwpq1mab+pkNoueKAeBAgxZ6hMhAeS/7bHc2BrnBbCKRox2RKczA+xWenJ+zZd3VYQvFwxG0htAebiZLJauSQwFMmQKBgCxF7EFb4uiJOL03KqMmeor86F3TX0e2OU2krf1FWR2EmvKWb2GWLzTr7E8AxLd/N+UQOS56u3lA6MABmklTifkCYAFE7+AMz+maBH0cuxfN0WQB+No/qr0D4390j/Oq0MgB+WjcwjHinCZ8aoZFcgx3X4lsmo2JHQM9GO8JFG7lAoGAchQ4GSWCZM63OFEkaWByUfPUaxOQyNdcPFK196tpS4FjfnQwzYq1ls2IASisT2H1LPLnFjhjnv5ytLWYfT6lSn7lQix5+jK8XIsB/pb7pdVwmalohXaeRkIOOMrcQyzXWQUWEajPQjqtn6RA/e/xHw1stMypQ1GZ6e4Ohf5it0g=";
                    String FORMAT = "json";
                    String CHARSET = "UTF-8";
                    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAphmpO+yCACAtcE724TRl9n1WGKSeXN3iaXGR5s1L72MSS0hZdS6yQaM3ZCYCEUZT0d6awCnovXrNzudzOAZ7pFW+n1WU7tgIg/1n8lv99rgFCzjm8R2qOxeI6j7k3UsS8cUHLsRnj37dr+lQyTHMLGlOS0VBe4EqwfRgq7UQS++zINcwQI05orUuv38mKwR6Eth6BOL4E+1YiAmIS35YX2KawLmeYfnOVWZ9q2l6KKQ9faIVDTdw0C8uX6yYn9ltdiB9sZJJ2Ir/Uxf8q4mk74yRjNc32k+iOg7tBwpqJdvd0ktbdKjKxoNvXKwbZiNQaKw7NuH9ql9d9Kr2gXVYFQIDAQAB";
                    String SIGN_TYPE = "RSA2";
                    AlipayLogger.setNeedEnableLogger(true);
                    alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);    
                }               
            }
        }
        
        return alipayClient;
    }
}
