package com.everhomes.wx;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
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
    
    @Value("${server.contextPath:}")
    private String contextPath;
    
	/**
	 * <b>URL: /wxauth/authReq</b>
	 * <p>请求微信授权。</p>
	 */
	@RequestMapping("authReq")
	//@RestReturn(String.class)
	//@RequireAuthentication(false)
	public void authReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        
        // 记录源始URL
        String sourceUrl = request.getParameter(KEY_SOURCE_URL);
        if(sourceUrl != null && sourceUrl.trim().length() > 0) {
            sourceUrl = sourceUrl.trim();
            session.setAttribute(KEY_SOURCE_URL, sourceUrl);
        }
        
        // 记录域空间
        String ns = request.getParameter(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(ns);
        session.setAttribute(KEY_NAMESPACE, namespaceId);

        // 记录需要接收code的URL
        String codeUrl = request.getParameter(KEY_CODE_URL);
        if(codeUrl != null && codeUrl.trim().length() > 0) {
            codeUrl = codeUrl.trim();
            session.setAttribute(KEY_CODE_URL, codeUrl);
        }
        String requestUrl = request.getRequestURL().toString();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request, ns={}, requestUrl={}, sourceUrl={}, codeUrl={}", 
                ns, requestUrl, sourceUrl, codeUrl);
        }
        
        // 如果配置了接收code的URL，代表接收到的code不属于本服务器域名的处理范畴，也就是不会在本服务器登录，
        // 此时需要向微信获取到code，并把code转给另外一台服务器
        if(codeUrl != null && codeUrl.trim().length() > 0) {
            sendAuthRequestToWeixin(namespaceId, sessionId, response);
            return;
        }
        
        LoginToken loginToken = userService.getLoginToken(request);
        // 没有登录，则请求微信授权
        if(!userService.isValid(loginToken)) {
            sendAuthRequestToWeixin(namespaceId, sessionId, response);
            return;
        }
        
        // 登录成功则跳转到原来访问的链接
        LOGGER.info("Process weixin auth request, loginToken={}, sourceUrl={}", loginToken, sourceUrl);
        redirectByWx(response, sourceUrl);
	}
	/**
	 * <b>URL: /wx/authCallback</b>
	 * <p>微信授权后回调API，逻辑由拦截器完成。定义此接口目的是为了打印日志。</p>
	 */
	@RequestMapping("authCallback")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public void wxRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
	    Object ns = session.getAttribute(KEY_NAMESPACE);
        Integer namespaceId = parseNamespace(ns);
        String sourceUrl = parseStr(session.getAttribute(KEY_SOURCE_URL));
        String sourceUrlInReq = request.getParameter(KEY_SOURCE_URL);
        if(sourceUrlInReq != null && sourceUrlInReq.trim().length() > 0) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.info("Process weixin auth request(callback), use source url in params, sourceUrlInReq={}, sourceUrlInSession={}", 
                    sourceUrlInReq, sourceUrl);
            }
            sourceUrl = sourceUrlInReq;
        }
        String codeUrl = parseStr(session.getAttribute(KEY_CODE_URL));
        if(codeUrl != null) {
            codeUrl = URLDecoder.decode(codeUrl, "UTF8");
            Map<String, String> params = new HashMap<String, String>();
            params.put(KEY_CODE, request.getParameter(KEY_CODE));
            params.put(KEY_STATE, request.getParameter(KEY_STATE));
            params.put(KEY_SOURCE_URL, request.getParameter(KEY_SOURCE_URL));
            codeUrl = appendParamToUrl(codeUrl, params);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.info("Process weixin auth request(callback), redirect code to other domain, codeUrl={}", codeUrl);
            }
            
            redirectByWx(response, codeUrl);
        } else {
            // 如果是微信授权回调请求，则通过该请求来获取到用户信息并登录
            processUserInfo(namespaceId, request, response);
            
            redirectByWx(response, sourceUrl);
        }
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
            baseUrl = entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF8");
        }
        
        return baseUrl + hashUrl;
	}
	
	private void sendAuthRequestToWeixin(Integer namespaceId, String sessionId, HttpServletResponse response) throws Exception {
        String wxAuthCallbackUrl = configurationProvider.getValue(namespaceId, "wx.auth.callback.url", WX_AUTH_CALLBACK_URL);
        String redirectUri =  configurationProvider.getValue(namespaceId, "home.url", "") + contextPath + wxAuthCallbackUrl;

        String appId = configurationProvider.getValue(namespaceId, "wx.offical.account.appid", "");
        String authorizeUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s"
                + "&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", appId,
                URLEncoder.encode(redirectUri, "UTF-8"), sessionId); 
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request, send auth to weixin, authorizeUrl={}", authorizeUri);
        }
        
        response.sendRedirect(authorizeUri);
	}
	
	/**
	 * 除了授权的跳转外，由于微信不提供其它URL的302跳转，需要使用JS的方式进行跳转；
	 * @param response
	 * @param redirectUrl
	 */
	private void redirectByWx(HttpServletResponse response, String redirectUrl) {
	    //response.sendRedirect(redirectUrl);
	    response.setContentType("text/html; charset=utf8");  
	    PrintWriter out = null;
	    try {
	        out = response.getWriter();
	        out.println("<!DOCTYPE html>"); 
	        out.println("<html lang=\"en\">"); 
	        out.println("<head>"); 
	        out.println("<meta charset=\"UTF-8\">"); 
	        out.println("<title>跳转中...</title>"); 
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
    
    private String parseStr(Object strObj) {
        if(strObj == null) {
            return null;
        } else {
            return strObj.toString();
        }
    }
    
    private void processUserInfo(Integer namespaceId, HttpServletRequest request, HttpServletResponse response) {
        String appId = configurationProvider.getValue(namespaceId, "wx.offical.account.appid", "");
        String secret = configurationProvider.getValue(namespaceId, "wx.offical.account.secret", "");
        // 微信提供的临时code
        String code = request.getParameter("code");
        
        // 使用临时code从微信中换取accessToken
        String accessTokenUri = String.format(WX_ACCESS_TOKEN_URL, appId, secret, code);
        String accessTokenUriWithoutSecret = accessTokenUri.replaceAll("secret=" + secret, "secret=****");
        String accessTokenJson = httpGet(accessTokenUri, accessTokenUriWithoutSecret);
        WxAccessTokenInfo accessToken = (WxAccessTokenInfo)StringHelper.fromJsonString(accessTokenJson, WxAccessTokenInfo.class);
        if (accessToken.getErrcode() != null) {
            LOGGER.error("Failed to get access token from webchat, appId={}, accessToken={}, accessTokenUri={}", 
                appId, accessTokenJson, accessTokenUriWithoutSecret);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                "Failed to get access token from webchat");
        }
        
        // 获取用户信息
        String userInfoUri = String.format(WX_USER_INFO_URL, accessToken.getAccess_token(), accessToken.getOpenid());
        String userInfoJson = httpGet(userInfoUri, userInfoUri);
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
    
    private String httpGet(String url, String safeUrl) {
        CloseableHttpClient httpclient = null;
        
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpPost = new HttpGet(url);
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", safeUrl, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                        "Failed to get the http result");
            } else {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Get http result, url={}, result={}", safeUrl, result);
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
}
