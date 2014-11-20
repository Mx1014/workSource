package com.everhomes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDiscover;
import com.everhomes.discover.RestMethod;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * 
 * Provides universal exception handling for all REST controllers
 * 
 * @author Kelven Yang
 *
 */
@RequireAuthentication(true)
public class ControllerBase {
    
    private static Map<String, RestMethod> s_restMethodMap = new HashMap<String, RestMethod>();  
    private static List<RestMethod> s_restMethodList = new ArrayList<RestMethod>();
    
    public ControllerBase() {
        for(RestMethod restMethod: RestDiscover.discover(this.getClass(), null)) {
            s_restMethodMap.put(restMethod.getUri(), restMethod);
            s_restMethodList.add(restMethod);
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleIOException(Exception ex) { 
        
        RestResponse restResponse;
        if(ex instanceof RuntimeErrorException) {
            RuntimeErrorException errorException = (RuntimeErrorException)ex;
            restResponse = new RestResponse(errorException.getErrorScope(), errorException.getErrorCode(), errorException.getMessage());
        } else {
            restResponse = new RestResponse(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, ex.toString());
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);        
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(StringHelper.toJsonString(restResponse), 
            responseHeaders, HttpStatus.OK);
        
        return responseEntity;
    }
    
    public static RestMethod getRestMethod(String uri) {
        return s_restMethodMap.get(uri);
    }
    
    public static List<RestMethod> getRestMethodList() {
        return s_restMethodList;
    }
}
