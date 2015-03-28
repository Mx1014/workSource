// @formatter:off
package com.everhomes.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.LoginToken;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.user.UserServiceProvider;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

/**
 * Interceptor that checks REST API signatures
 * 
 * @author Kelven Yang
 *
 */
public class WebRequestInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestInterceptor.class);

    @Autowired
    private UserService userService;
   
    @Autowired
    private UserServiceProvider userProvider;
    
    @Autowired
    private AppProvider appProvider;
    
    public WebRequestInterceptor() {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("afterCompletion request : " + request.getRequestURI().toString());
        
        cleanupUserContext();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("postHandle request : " + request.getRequestURI().toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String uri = request.getRequestURI();

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("URI: " + uri + "handler: " + handler.getClass().getName());
        
        if(isProtected(handler)) {
            LoginToken token = getLoginToken(request);
            if(!isValid(token)) {
                if(UserContext.current().getCallerApp() != null)
                    return true;
                
                response.setContentType("application/json");
                RestResponse restResponse = new RestResponse(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Authentication is required");
                
                response.getWriter().write(StringHelper.toJsonString(restResponse));
                response.flushBuffer();
                return false;
            } else {
                setupUserContext(token);
            }
        }
        return true;
    }
    
    private boolean isValid(LoginToken token) {
        if(token == null)
            return false;
        
        return this.userService.isValidLoginToken(token);
    }
    
    private boolean isProtected(Object handler) {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            RequireAuthentication authentication = handlerMethod.getMethod().getAnnotation(RequireAuthentication.class);
            if(authentication == null)
                authentication = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequireAuthentication.class);
            if(authentication != null)
                return authentication.value();
        }
        
        return false;
    }
    
    private LoginToken getLoginToken(HttpServletRequest request) {
        if(!checkProtectedRequest(request))
            return null;
        
        Map<String, String[]> paramMap = request.getParameterMap();
        String loginTokenString = null;
        for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String value = StringUtils.join(entry.getValue(), ",");
            if(LOGGER.isTraceEnabled())
                LOGGER.trace("HttpRequest param " + entry.getKey() + ": " + value);
            if(entry.getKey().equals("token"))
                loginTokenString = value;
        }
        
        if(loginTokenString == null) {
            if(request.getCookies() != null) {
                for(Cookie cookie : request.getCookies()) {
                    if(LOGGER.isDebugEnabled())
                        LOGGER.debug("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue() + ", path: " + cookie.getPath());
                    
                    if(cookie.getName().equals("token")) {
                        loginTokenString = cookie.getValue();
                        break;
                    }
                }
            }
        }
 
        if(loginTokenString != null)
            return LoginToken.fromTokenString(loginTokenString);
        return null;
    }
    
    private boolean checkProtectedRequest(HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(LOGGER.isTraceEnabled())
                    LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue());
                
                if(cookie.getName().equals("token"))
                    return true;
            }
        }
        
        Map<String, String[]> paramMap = request.getParameterMap();
        if(paramMap.get("appkey") == null)
            return false;
        
        if(paramMap.get("signature") == null)
            return false;
        
        String appKey = getParamValue(paramMap, "appkey");
        App app = this.appProvider.findAppByKey(appKey);
        if(app == null) {
            LOGGER.warn("Invalid app key: " + appKey);
            return false;
        }
        
        UserContext.current().setCallerApp(app);
        
        Map<String, String> mapForSignature = new HashMap<String, String>();
        for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if(!entry.getKey().equals("signature")) {
                mapForSignature.put(entry.getKey(), StringUtils.join(entry.getValue(), ','));
            }
        }
        
        return SignatureHelper.verifySignature(mapForSignature, app.getSecretKey(), getParamValue(paramMap, "signature"));
    }
    
    private static String getParamValue(Map<String, String[]> paramMap, String paramName) {
        String[] values = paramMap.get(paramName);
        if(values != null)
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
    
    private void cleanupUserContext() {
        UserContext.clear();
    }
}
