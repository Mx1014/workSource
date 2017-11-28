package com.everhomes.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.WebRequestSequence;
import com.everhomes.rest.oauth2.OAuth2ErrorResponse;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

@RequireAuthentication(false)
public class OAuth2ControllerBase extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ControllerBase.class);

    @Autowired
    protected OAuth2Service oAuth2Service;

    public OAuth2ControllerBase() {
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleIOException(Exception ex) {
        LOGGER.error(String.format("Exception in processStat request [%s]: %s",
                WebRequestSequence.current().getRequestSequence(),
                ex.getMessage() != null ? ex.getMessage() : ""), ex);

        OAuth2ErrorResponse errorResponse = new OAuth2ErrorResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        if(ex instanceof RuntimeErrorException) {
            RuntimeErrorException errorException = (RuntimeErrorException)ex;
            errorResponse.setError(this.oAuth2Service.getOAuth2AuthorizeError(errorException.getErrorCode()));
            switch(errorException.getErrorCode()) {
            case OAuth2ServiceErrorCode.ERROR_INVALID_CLIENT :
            case OAuth2ServiceErrorCode.ERROR_UNAUTHORIZED_CLIENT :
            case OAuth2ServiceErrorCode.ERROR_ACCESS_DENIED :
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;

            case OAuth2ServiceErrorCode.ERROR_SERVER_ERROR :
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;

            case OAuth2ServiceErrorCode.ERROR_SERVER_BUSY :
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
                break;

            case OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST :
            case OAuth2ServiceErrorCode.ERROR_UNSUPPORTED_RESPONSE_TYPE :
            case OAuth2ServiceErrorCode.ERROR_INVALID_SCOPE :
            case OAuth2ServiceErrorCode.ERROR_INVALID_GRANT :
            case OAuth2ServiceErrorCode.ERROR_UNSUPPORTED_GRANT_TYPE :
            default :
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            }
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse.setError(this.oAuth2Service.getOAuth2AuthorizeError(OAuth2ServiceErrorCode.ERROR_SERVER_ERROR));
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=utf-8");

        ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(errorResponse,
            responseHeaders, httpStatus);
        return responseEntity;
    }
}
