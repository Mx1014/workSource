package com.everhomes.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.user.UserServiceProvider;
import com.everhomes.util.StringHelper;

/**
 * Interceptor that checks REST API signatures
 * 
 * @author Kelven Yang
 *
 */
public class WebRequestApiKeyInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestApiKeyInterceptor.class);

    @Autowired
    private UserServiceProvider userServiceProvider;
    
    public WebRequestApiKeyInterceptor() {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("URI: " + uri);
        
        if(isProtectedUri(uri)) {
            LoginToken token = getLoginToken(request);
            if(!isValid(token)) {
                response.setContentType("application/json");
                RestResponse restResponse = new RestResponse(Response.SC_FORBIDDEN, "Authentication is required");
                response.setStatus(Response.SC_FORBIDDEN);
                
                response.getWriter().write(StringHelper.toJsonString(restResponse));
                response.flushBuffer();
                return false;
            }
        }
        return true;
    }
    
    private boolean isValid(LoginToken token) {
        if(token == null)
            return false;
        
        return this.userServiceProvider.isValidLoginToken(token);
    }
    
    private boolean isProtectedUri(String uri) {
        return !uri.equals("/user/logon");
    }
    
    private static LoginToken getLoginToken(HttpServletRequest request) {
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
            for(Cookie cookie : request.getCookies()) {
                if(LOGGER.isTraceEnabled())
                    LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue());
                
                if(cookie.getName().equals("token"))
                    loginTokenString = cookie.getValue();
            }
        }
 
        if(loginTokenString != null)
            return LoginToken.fromTokenString(loginTokenString);
        return null;
    }
}
