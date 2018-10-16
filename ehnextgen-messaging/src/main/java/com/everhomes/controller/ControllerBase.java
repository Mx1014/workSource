// @formatter:off
package com.everhomes.controller;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestMethod;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.*;

/**
 * 
 * Provides universal exception handling for all REST controllers
 * 
 * @author Kelven Yang
 *
 */
@RequireAuthentication(true)
public class ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerBase.class);
    
    private static Map<String, ExtendRestMethod> s_restMethodMap = new HashMap<>();
    private static final List<RestMethod> s_restMethodList = new ArrayList<>();
    private static volatile boolean s_sortRestMethodList = false;
    
    @Autowired
    private LocaleStringService localeService;
    
    public ControllerBase() {
        for(ExtendRestMethod restMethod: RestDiscover.discover(this.getClass(), null)) {
            s_restMethodMap.put(restMethod.getUri(), restMethod);
            s_restMethodList.add(restMethod);
            s_sortRestMethodList = true;
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleIOException(Exception ex) {
        LOGGER.error(String.format("Exception in processStat request [%s]: %s",
                WebRequestSequence.current().getRequestSequence(),
                ex.getMessage() != null ? ex.getMessage() : ""), ex);
        
        RestResponse restResponse;
        if(ex instanceof RuntimeErrorException) {
            RuntimeErrorException errorException = (RuntimeErrorException)ex;
            
            String localizedMessage = getLocalizedString(errorException.getErrorScope(), 
                    errorException.getErrorCode(), errorException.getMessage());
            restResponse = new RestResponse(errorException.getErrorScope(), errorException.getErrorCode(), localizedMessage);
            restResponse.setErrorDetails(String.format("Request [%s]: %s", WebRequestSequence.current().getRequestSequence(), ex.toString()));
        } else {
            String localizedMessage = getLocalizedString(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_GENERAL_EXCEPTION, ex.toString());
            restResponse = new RestResponse(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, localizedMessage);
            restResponse.setErrorDetails(String.format("Request [%s]: %s", WebRequestSequence.current().getRequestSequence(), ex.toString()));
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=utf-8");
        
        ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(restResponse, 
            responseHeaders, HttpStatus.OK);
        
        return responseEntity;
    }
    
    private String getLocalizedString(String scope, int code, String defaultText) {
        String locale = getLocale();
        return localeService.getLocalizedString(scope, String.valueOf(code), locale, defaultText); 
    }
    
    private String getLocale() {
        User user = UserContext.current().getUser();
        if(user != null && user.getLocale() != null)
            return user.getLocale();
        
        // 在某些情况下并没有用户，此时默认使用中文locale by lqs 20160825
        // return "en_US";
        return Locale.SIMPLIFIED_CHINESE.toString();
    }
    
    public static RestMethod getRestMethod(String uri) {
        return s_restMethodMap.get(uri);
    }
    
    public static List<RestMethod> getRestMethodList(String javadocRoot, String defaultSite) {
        if(s_sortRestMethodList) {
            synchronized(s_restMethodList) {
                s_restMethodList.sort(Comparator.comparing(RestMethod::getUri));
                s_restMethodList.forEach((m) -> m.setJavadocUrl(javadocRoot + "/" + m.getFullJavadocUrl(defaultSite)));
                s_sortRestMethodList = false;
            }
        }
        return s_restMethodList;
    }
    
    @InitBinder
    public void initListBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }
}
