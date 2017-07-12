// @formatter:off
package com.everhomes.oauth2client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * Created by xq.tian on 2017/3/14.
 */
public class HttpResponseEntity<T> extends ResponseEntity<T> {
    public HttpResponseEntity(HttpStatus statusCode) {
        super(statusCode);
    }

    public HttpResponseEntity(T body, HttpStatus statusCode) {
        super(body, statusCode);
    }

    public HttpResponseEntity(MultiValueMap headers, HttpStatus statusCode) {
        super(headers, statusCode);
    }

    public HttpResponseEntity(T body, MultiValueMap headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }
}
