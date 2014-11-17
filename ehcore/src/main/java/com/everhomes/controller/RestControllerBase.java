package com.everhomes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.everhomes.constants.ErrorCodes;
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
public class RestControllerBase {
    
    public RestControllerBase() {
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
}
