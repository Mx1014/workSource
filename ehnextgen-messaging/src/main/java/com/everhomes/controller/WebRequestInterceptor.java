// @formatter:off
package com.everhomes.controller;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServer;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.domain.Domain;
import com.everhomes.domain.DomainService;
import com.everhomes.messaging.MessagingKickoffService;
import com.everhomes.namespace.Namespace;
import com.everhomes.portal.PortalVersionUser;
import com.everhomes.portal.PortalVersionUserProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.user.*;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.tool.BlutoAccessor;
import com.everhomes.user.*;
import com.everhomes.user.sdk.SdkUserService;
import com.everhomes.util.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interceptor that checks REST API signatures
 *
 * @author Kelven Yang
 *
 */
public class WebRequestInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestInterceptor.class);

    private static final String ATTR_KEY_START_TICK = "EvhStartTick";
    private static final String ZUOLIN_APP_KEY = "zuolin.appKey";
    private static final String SIGN_APP_KEY = "sign.appKey";
    private static final String APP_KEY_NAME = "appKey";
    private static final int VERSION_UPPERBOUND = 4195330; // 区分4.1.2之前的版本,小于这个数字代表4.1.2以前的版本
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static final String DEFAULT_CLIENT_APP_KEY_API_STR =
            "/stat/event/postDevice,/pusher/registDevice,/user/syncActivity,/user/signupByAppKey,/user/signup,/user/logoff";

    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private MessagingKickoffService kickoffService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private DomainService domainService;

	@Autowired
    private PortalVersionUserProvider portalVersionUserProvider;
	
    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private SdkUserService sdkUserService;

    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    public WebRequestInterceptor() {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        String requestInfo = getRequestInfo(request, false);

        cleanupUserContext();

        Long startTick = (Long) request.getAttribute(ATTR_KEY_START_TICK);
        if (startTick != null) {
            long requestExecutionMs = System.currentTimeMillis() - startTick.longValue();
            if (requestExecutionMs > 1000)
                LOGGER.warn("Complete request {} in {} ms", requestInfo,
                        System.currentTimeMillis() - startTick.longValue());
            else
                LOGGER.debug("Complete request {} in {} ms", requestInfo,
                        System.currentTimeMillis() - startTick.longValue());
        } else {
            LOGGER.debug("Complete request: {}", requestInfo);
        }

        MDC.remove("seq");

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        WebRequestSequence.current().setupRequestSequence();
        MDC.put("seq", WebRequestSequence.current().getRequestSequence());
        MDC.put("uri", request.getRequestURI());
        MDC.put("ip", InetAddress.getLocalHost().getHostAddress());

        request.setAttribute(ATTR_KEY_START_TICK, System.currentTimeMillis());

//		LOGGER.debug("preHandle-begin");
//		Map<String,String[]> map = request.getParameterMap();
//		if(map!=null&&!map.isEmpty()){
//			for(String key:map.keySet()){
//				LOGGER.debug("preHandle-parameter."+key+"="+map.get(key)[0]);
//			}
//		}else
//			LOGGER.debug("preHandle-parameter is null");
        Map<String, String> userAgents = getUserAgent(request);
        try {
            // 由于服务器注册接口被攻击，从日志分析来看IP和手机号都不一样，但useragent并没有按标准的形式，故可以通过useragent来做限制，
            // 通过配置一黑名单，含黑名单关键字的useragent会被禁止掉 by lqs 20170516
            checkUserAgent(request.getRequestURI(), userAgents);

            //设置上下文的域空间信息
            setupNamespaceIdContext(userAgents, request.getParameterMap());

            setupVersionContext(userAgents);
            setupScheme(userAgents);
            setupDomain(request);

            //设置上下文信息
            setAppContext(request.getParameterMap());

            //加上频率控制
            frequencyControlHandler(request,handler);
            if (isProtected(handler)) {
                LoginToken token = userService.getLoginToken(request);
                // isValid转移到UserServiceImpl，使得其它地方也可以调（如第三方登录WebRequestWeixinInterceptor） by lqs 20160922
                if (!userService.isValid(token)) {
                    // 由于客户端已经在全部接口上加上签名，按原来的逻辑是先检验签名，若签名校验通过则不再往下执行而直接返回校验通过；
                    // 但如果用户是在被踢出状态，则会导致客户端仍然能够访问，但此时用户ID为0，会导致一部分接口出问题，
                    // 故把校验签名与校验踢出的代码换一下位置。 by lqs 20170816
//                    if (this.isInnerSignLogon(request)) {
//                        token = this.innerSignLogon(request, response);
//                        setupUserContext(token);
//                        MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
//                        return true;
//                    } else if (this.checkRequestSignature(request)) {
//                        setupUserContextForApp(UserContext.current().getCallerApp());
//                        //TODO Added by Janson
//                        if (null != UserContext.current().getUser()) {
//                            MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
//                        }
//                        return true;
//                    }
//
//                    //Kickoff state support
//                    if (kickoffService.isKickoff(UserContext.current().getNamespaceId(), token)) {
//                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
//                                UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//                    }
                    if (this.isInnerSignLogon(request)) {
                        LOGGER.debug("this is inner sign logon before");
                        token = this.innerSignLogon(request, response);
                        setupUserContext(token);
                        MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
                        LOGGER.debug("this is inner sign logon, UserContext.current(): {}", UserContext.current().getUser());
                        return true;
                    }

                    //Kickoff state support
                    // 把踢出校验这一步移到checkRequestSignature前面
                    if (kickoffService.isKickoff(UserContext.current().getNamespaceId(), token)) {
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                                UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
                    }

                    if (this.checkRequestSignature(request)) {
                        setupUserContextForApp(UserContext.current().getCallerApp());
                        //TODO Added by Janson
                        if (null != UserContext.current().getUser()) {
                            MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
                        }
                        LOGGER.debug("check request signature success, UserContext.current(): {}", UserContext.current().getUser());
                        return true;
                    }

                    //Update by Janson, when the request is using apiKey, we generate a 403 response to app.
                    String appKey = request.getParameter(APP_KEY_NAME);
                    if (null != appKey && (!appKey.isEmpty()) && !requireLogonToken(request)) {
                        LOGGER.info("appKey=" + appKey + " is Forbidden");
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                                UserServiceErrorCode.ERROR_FORBIDDEN, "Forbidden");
                    }
                    // authentication failed ,the httpcode should set 401
                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                            UserServiceErrorCode.ERROR_UNAUTHENTITICATION, "Authentication is required");
                } else {
                    setupUserContext(token);
                    MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
                    LOGGER.debug("token check success, setup user context: {}, uid={}", token, String.valueOf(UserContext.current().getUser().getId()));
                }

            } else {
                LoginToken token = userService.getLoginToken(request);
                if (token != null && userService.isValid(token)) {
                    setupUserContext(token);
                    
                    if(UserContext.current().getUser() != null) {
                        MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));    
                    }
                    
                } else {
                    setupAnnonymousUserContext();
                    MDC.put("uid", String.valueOf(UserContext.current().getUser().getId()));
                }
            }
            return true;
        } finally {
             if (LOGGER.isDebugEnabled()) {

                StringBuffer sb = new StringBuffer();
                sb.append("{");

                Enumeration<String> headers = request.getHeaderNames();
                int i = 0;
                while (headers.hasMoreElements()) {
                    String header = headers.nextElement();

                    if (i > 0)
                        sb.append(", ");
                    sb.append(header + ": " + request.getHeader(header));
                    i++;
                }
                sb.append("}");

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Pre handling request: {}, headers: {}", getRequestInfo(request, true), sb.toString());
                    LOGGER.debug("User Agents: {}", userAgents);
                }
            }
        }
    }

    // 转移到UserServiceImpl，使得其它地方也可以调（如第三方登录WebRequestWeixinInterceptor） by lqs 20160922
//	private boolean isValid(LoginToken token) {
//		if(token == null) {
//			User user = UserContext.current().getUser();
//			Long userId = -1L;
//			if(user != null) {
//				userId = user.getId();
//			}
////			It's ok when using signature
////			LOGGER.error("Invalid token, token={}, userId={}", token, userId);
//			return false;
//		}
//
//		return this.userService.isValidLoginToken(token);
//	}

    private void setupDomain(HttpServletRequest request){
        DomainDTO domain = domainService.getDomainInfo(null, request);
        UserContext context = UserContext.current();
        if(null != domain)
            context.setDomain(ConvertHelper.convert(domain, Domain.class));
    }

    private void setupScheme(Map<String, String> userAgents) {
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("Setup userAgents = {}", userAgents);
        // }
        UserContext context = UserContext.current();
        if(org.springframework.util.StringUtils.isEmpty(context.getVersion()) || context.getVersion().equals("0.0.0")){
            context.setScheme(userAgents.get("scheme"));
        }else{
            try {
                VersionRange versionRange = new VersionRange("[" + context.getVersion() + "," + context.getVersion() + ")");

                // if (LOGGER.isDebugEnabled()) {
                //     LOGGER.debug("setup scheme, version={}, versionUpperBound={}", context.getVersion(), versionRange.getUpperBound());
                // }
                if (versionRange.getUpperBound() < VERSION_UPPERBOUND) {
                    context.setScheme(HTTP);
                } else {
                    context.setScheme(userAgents.get("scheme"));
                }
            }catch(Exception ex) {
                context.setScheme(userAgents.get("scheme"));
            }
        }

    }

    private void setupVersionContext(Map<String, String> userAgents) {
        UserContext context = UserContext.current();
        context.setVersion("0.0.0");

        userAgents.forEach((k, v) -> {
            if (VersionRealmType.fromCode(k) != null) {
                context.setVersion(v);
                context.setVersionRealm(k);
                return;
            }
        });
    }

    private boolean isProtected(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireAuthentication authentication = handlerMethod.getMethod().getAnnotation(RequireAuthentication.class);
            if (authentication == null)
                authentication = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequireAuthentication.class);
            if (authentication != null)
                return authentication.value();
        }

        return false;
    }

//	private LoginToken getLoginToken(HttpServletRequest request) {
//		Map<String, String[]> paramMap = request.getParameterMap();
//		String loginTokenString = null;
//		for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//			String value = StringUtils.join(entry.getValue(), ",");
//			if(LOGGER.isTraceEnabled())
//				LOGGER.trace("HttpRequest param " + entry.getKey() + ": " + value);
//			if(entry.getKey().equals("token"))
//				loginTokenString = value;
//		}
//
//		if(loginTokenString == null) {
//			if(request.getCookies() != null) {
//				List<Cookie> matchedCookies = new ArrayList<>();
//
//				for(Cookie cookie : request.getCookies()) {
//					if(LOGGER.isTraceEnabled())
//						LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue() + ", path: " + cookie.getPath());
//
//					if(cookie.getName().equals("token")) {
//						matchedCookies.add(cookie);
//					}
//				}
//
//				if(matchedCookies.size() > 0)
//					loginTokenString = matchedCookies.get(matchedCookies.size() - 1).getValue();
//			}
//		}
//
//		if(loginTokenString != null)
//			try{
//				return WebTokenGenerator.getInstance().fromWebToken(loginTokenString, LoginToken.class);
//			} catch (Exception e) {
//				LOGGER.error("Invalid login token.tokenString={}",loginTokenString);
//				return null;
//			}
//
//		return null;
//	}

    private Map<String, String> getUserAgent(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty())
            userAgent = request.getHeader("user-agent");
        if (userAgent != null && userAgent.trim().length() > 0) {
            String[] segments = userAgent.trim().split(" ");
            for (String segment : segments) {
                if (segment != null && segment.trim().length() > 0) {
                    String[] values = segment.split("/");
                    if (values.length == 2) {
                        map.put(values[0].trim(), values[1].trim());
                    }
                }
            }
        }

        String xAgent = request.getHeader("X-Agent");
        if (xAgent == null || xAgent.isEmpty()) {
            xAgent = request.getHeader("x-agent");
        }
        if (xAgent != null && xAgent.trim().length() > 0) {
            String[] segments = xAgent.trim().split(" ");
            for (String segment : segments) {
                if (segment != null && segment.trim().length() > 0) {
                    String[] values = segment.split("/");
                    if (values.length == 2) {
                        map.put(values[0].trim(), values[1].trim());
                    }
                }
            }
        }
        // String scheme = request.getScheme();
        String scheme = request.getHeader("X-Forwarded-Scheme");
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("Strip the scheme from header, X-Forwarded-Scheme={}, scheme={}", scheme, request.getScheme());
        // }
        // 当请求没有过nginx的时候scheme为null，则需要根据数据库的content server配置项来决定scheme by sfyan 20170221
        if (scheme == null || scheme.isEmpty()) {
            try {
                ContentServer server = contentServerService.selectContentServer();
                Integer port = server.getPublicPort();
                // if (LOGGER.isDebugEnabled()) {
                //     LOGGER.debug("Final selectContentServer port = {}", port);
                // }
                if (80 == port || 443 == port) {
                    scheme = HTTPS;
                } else {
                    scheme = HTTP;
                }
            } catch (Exception e) {
                LOGGER.error("Get user agent. Failed to find content server", e);
                scheme = HTTP;
            }
        }
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("Final parsed schema = {}", scheme);
        // }
        map.put("scheme", scheme);
        return map;
    }

    private boolean checkRequestSignature(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap.get(APP_KEY_NAME) == null)
            return false;

        if (paramMap.get("signature") == null)
            return false;

        if (requireLogonToken(request)) {
            return false;
        }

        String appKey = getParamValue(paramMap, APP_KEY_NAME);
        App app = getAppByKey(appKey);
        if (app == null) {
            LOGGER.warn("Invalid app key: " + appKey);
            return false;
        }

        UserContext.current().setCallerApp(app);

        Map<String, String> mapForSignature = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if (!entry.getKey().equals("signature")) {
                mapForSignature.put(entry.getKey(), StringUtils.join(entry.getValue(), ','));
            }
        }

        String signature = getParamValue(paramMap, "signature");

        return SignatureHelper.verifySignature(mapForSignature, app.getSecretKey(), signature);
    }

    private App getAppByKey(String appKey) {
        // 给 SDK 调用提供的便捷方式
        if (AppConstants.APPKEY_SDK.equalsIgnoreCase(appKey)) {
            App app = new App();
            app.setAppKey(appKey);
            app.setSecretKey(Base64.getEncoder().encodeToString((new BlutoAccessor()).get(new TokenServiceSecretKey())));
            app.setName("SDK");
            app.setId(0L);
            return app;
        }
        return this.appProvider.findAppByKey(appKey);
    }

    private static String getParamValue(Map<String, String[]> paramMap, String paramName) {
        String[] values = paramMap.get(paramName);
        if (values != null)
            return StringUtils.join(values, ',');
        return null;
    }

    /**
     * 优先从 Header 的 userAgents 判断 namespaceId，若没有，则从请求信息确认
     * 请求内容如果有 namespaceId 这个值，则设置域空间 ID;
     * 如果已经从 userAgent 知道域空间 ID，则忽略
     * @param
     */
    private void setupNamespaceIdContext(Map<String, String> userAgents, Map<String, String[]> paramMap) {
        UserContext context = UserContext.current();
        context.setNamespaceId(Namespace.DEFAULT_NAMESPACE);

        if (userAgents.containsKey("ns")) {
            String ns = userAgents.get("ns");
            Integer namespaceId = Integer.valueOf(ns);
            context.setNamespaceId(namespaceId);
        } else {
            //主要应用场景是新的管理后台 by Janson
            String ns = getParamValue(paramMap, "namespaceId");
            if(ns != null && !ns.isEmpty()) {
                try {
                    Integer namespaceId = Integer.valueOf(ns);
                    context.setNamespaceId(namespaceId);
                } catch(NumberFormatException ex) {
                    LOGGER.warn("parse namespaceId failed", ex);
                }

            }

        }


    }


    /**
     * 标准版之后去除sceneToken设置上下文信息
     * @param
     */
    private void setAppContext(Map<String, String[]> paramMap) {

        UserContext context = UserContext.current();

        String[] sceneTokens = paramMap.get("sceneToken");

        if(sceneTokens != null && sceneTokens.length > 0){
            String sceneToken = sceneTokens[0];
            AppContext appContext = AppContextGenerator.fromWebToken(sceneToken);
            context.setAppContext(appContext);
        }
    }

    private void setupUserContext(LoginToken loginToken) {
        UserLogin login = this.userService.findLoginByToken(loginToken);
        UserContext context = UserContext.current();
        context.setLogin(login);

        User user = this.userProvider.findUserById(login.getUserId());
        context.setUser(user);

        PortalVersionUser portalVersionUserByUser = this.portalVersionUserProvider.findPortalVersionUserByUserId(login.getUserId());
        if(portalVersionUserByUser != null){
            context.setPreviewPortalVersionId(portalVersionUserByUser.getVersionId());
        }
    }

    private void setupAnnonymousUserContext() {
        UserContext context = UserContext.current();

        context.setLogin(User.ANNONYMOUS_LOGIN);

        User user = new User();
        user.setId(User.ANNONYMOUS_UID);
        context.setUser(user);
    }

    private void setupUserContextForApp(App app) {
        if (app.getAppKey().equalsIgnoreCase(AppConstants.APPKEY_BORDER)) {
            User user = this.userProvider.findUserById(User.ROOT_UID);
            UserContext.current().setUser(user);
        } else  {
            // 由于把发短信的相关接口加入到使用签名的行列来，此时由于使用了UserContext会引起空指针，
            // 故需要为这个场景加上UserContext， by lqs 20170629
            setupAnnonymousUserContext();
        }
    }

    private void cleanupUserContext() {
        UserContext.clear();
    }

    private static String getRequestInfo(HttpServletRequest request, boolean requestDetails) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getMethod()).append(" ");
        sb.append(request.getRequestURI());
        if (requestDetails) {
            Enumeration<String> e = request.getParameterNames();
            sb.append("{");
            int i = 0;
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String val = request.getParameter(name);

                if (val != null && !val.isEmpty()) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(name).append(": ").append(val);

                    i++;
                }
            }
            sb.append("}");
        }

        return sb.toString();
    }

    private boolean isInnerSignLogon(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String signAppKey = getParamValue(paramMap, "appKey");
        String osignAppKey = configurationProvider.getValue(SIGN_APP_KEY, "44952417-b120-4f41-885f-0c1110c6aece");
        return signAppKey != null && signAppKey.equals(osignAppKey) && !requireLogonToken(request);
    }

    private boolean requireLogonToken(HttpServletRequest request) {
        String appKey = getParamValue(request.getParameterMap(), "appKey");
        if (appKey != null) {
            String contextPath = request.getContextPath();
            String requestURI = request.getRequestURI();
            String uri = requestURI.replaceFirst(contextPath, "");

            String clientAppKeyApi = configurationProvider.getValue("client.appKeyApi", DEFAULT_CLIENT_APP_KEY_API_STR);
            Set<String> apiSet = Stream.of(clientAppKeyApi.split(",")).collect(Collectors.toSet());

            // appKey 不为空的时候，如果是客户端来的请求，除了特殊的几个，都是需要 logon token 的
            boolean needLogonToken = AppConstants.APPKEY_APP.equals(appKey) && !apiSet.contains(uri);

            // log start
            try {
                if (needLogonToken) {
                    String token = getParamValue(request.getParameterMap(), "token");
                    if (token == null) {
                        Cookie cookie = findCookieInRequest("token", request);
                        if (cookie != null) {
                            token = cookie.getValue();
                        }
                    }
                    LOGGER.debug("Need logon token, but they are invalid, requestURI={}, token={}", uri, token);
                }
            } catch (Exception e) {
                //
            }
            // log end

            return needLogonToken;
        }
        // 受保护的 API 里，没有 appKey 的请求都是需要 logon token 的
        return true;
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
            setCookieInResponse("token", tokenString, request, response);
            return logintoken;
        }
        return null;
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

    // 改为public方法，使得第三方帐号的登录也可以使用同一套代码，见UserServiceImpl.logonBythirdPartAccount by lqs 20160922
    public static void setCookieInResponse(String name, String value, HttpServletRequest request,
                                           HttpServletResponse response) {

        Cookie cookie = findCookieInRequest(name, request);
        if (cookie == null)
            cookie = new Cookie(name, value);
        else
            cookie.setValue(value);
        cookie.setPath("/");
        if (value == null || value.isEmpty())
            cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    //微信公众号的accessToken过期时间是7200秒，需要设置cookie小于7200。防止用户在coreserver处于登录状态而accessToken已过期，重新登录之后会刷新accessToken
    public static void setCookieInResponse(String name, String value, HttpServletRequest request,
                                           HttpServletResponse response, int age) {

        Cookie cookie = findCookieInRequest(name, request);
        if (cookie == null)
            cookie = new Cookie(name, value);
        else
            cookie.setValue(value);
        cookie.setPath("/");
        //微信
        cookie.setMaxAge(age);
        if (value == null || value.isEmpty())
            cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
        List<Cookie> matchedCookies = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    LOGGER.debug("Found matched cookie with name {} at value: {}, path: {}, version: {}", name,
                            cookie.getValue(), cookie.getPath(), cookie.getVersion());
                    matchedCookies.add(cookie);
                }
            }
        }

        if (matchedCookies.size() > 0)
            return matchedCookies.get(matchedCookies.size() - 1);
        return null;
    }
    
    private void checkUserAgent(String uri, Map<String, String> userAgents) {
        // 由于电商服务器、统一支付服务器、第三方服务器都可能不修改user-agent，从而会误杀；
        // 为了避免此情况，只对注册接口进行限制 by lqs 20170517
        if(uri == null || !uri.contains("/user/signup")) {
            return;
        }
        
        if(userAgents == null) {
            return;
        }
        
        try {
            String blacklist = configurationProvider.getValue("user.agent.blacklist", "");
            if(blacklist == null || blacklist.trim().length() == 0) {
                return;
            }
            
            String[] segments = blacklist.split(",");
            
            Iterator<Entry<String, String>> iterator = userAgents.entrySet().iterator();
            Entry<String, String> entry = null;
            String entryKey = null;
            String entryValue = null;
            while(iterator.hasNext()) {
                entry = iterator.next();
                entryKey = entry.getKey();
                entryValue = entry.getValue();
                for(String segment : segments) {
                    if((entryKey != null && entryKey.contains(segment)) || (entryValue != null && entryValue.contains(segment))) {
                        LOGGER.error("User agent is in blacklist, uri={}, userAgentKey={}, userAgentValue={}, blacklist={}", uri, entryKey, entryValue, blacklist);
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                                UserServiceErrorCode.ERROR_FORBIDDEN, "Forbidden");
                    }
                }
            }
        } catch (RuntimeErrorException e) {
            // 命中黑名单的异常则抛出去
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to check the user-agent in http/https header, userAgents={}", userAgents, e);
        }
    }

    private void frequencyControlHandler(HttpServletRequest request, Object handler) throws FrequencyControlException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            FrequencyControl control = handlerMethod.getMethod().getAnnotation(FrequencyControl.class);
            if (control != null) {
                String ip = request.getLocalAddr();
                String url = request.getRequestURL().toString();

                LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
                String[] paraNameArr = u.getParameterNames(handlerMethod.getMethod());

                String[] spelKey = control.key();

                Map<String, String[]> parameterMap = request.getParameterMap();


                StringBuffer keyBuffer = new StringBuffer();
                for (String s : spelKey) {
                    String attribute = s.split(paraNameArr[0]+".")[1];
                    if(!org.springframework.util.StringUtils.isEmpty(attribute)){
                        if(parameterMap.get(attribute) != null && parameterMap.get(attribute).length > 0){
                            keyBuffer.append(parameterMap.get(attribute)[0]);
                        }
                    }
                }

                String key = "req_limit_".concat(url).concat(ip).concat("?") + keyBuffer.toString();
                LOGGER.debug("current frequencyControl key" + key);

                Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
                RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
                Object redisCount = redisTemplate.opsForValue().get(key);
                if (redisCount == null) {
                    redisTemplate.opsForValue().set(key, "1", control.time(), TimeUnit.MILLISECONDS);
                } else {
                    redisTemplate.opsForValue().increment(key, 1);
                    if (Integer.valueOf(redisTemplate.opsForValue().get(key).toString()) > control.count()) {
                        LOGGER.info("user's IP[" + ip + "] post [" + url + "] more than [" + control.count() + "]");
                        throw new FrequencyControlException("user's IP[\" + ip + \"] post [\" + url + \"] more than [\" + control.count() + \"]");
                    }
                }
            }
        }

    }

}
