// @formatter:off
package com.everhomes.oauth2client;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

/**
 * Created by xq.tian on 2017/3/14.
 */
public class HttpErrorEntity<T> extends HttpResponseEntity<T> {
    public HttpErrorEntity(HttpStatus statusCode) {
        super(statusCode);
    }

    public HttpErrorEntity(T body, HttpStatus statusCode) {
        super(body, statusCode);
    }

    public HttpErrorEntity(MultiValueMap headers, HttpStatus statusCode) {
        super(headers, statusCode);
    }

    public HttpErrorEntity(T body, MultiValueMap headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }
}
