package com.everhomes.rest.oauth2;

public interface OAuth2ServiceErrorCode {
    static final String SCOPE = "oauth2";

    static final int ERROR_INVALID_REQUEST = 1;
    static final int ERROR_UNAUTHORIZED_CLIENT = 2;
    static final int ERROR_ACCESS_DENIED = 3;
    static final int ERROR_UNSUPPORTED_RESPONSE_TYPE = 4;
    static final int ERROR_INVALID_SCOPE = 5;
    static final int ERROR_SERVER_ERROR = 6;
    static final int ERROR_SERVER_BUSY = 7;
    static final int ERROR_INVALID_CLIENT = 8;
    static final int ERROR_INVALID_GRANT = 9;
    static final int ERROR_UNSUPPORTED_GRANT_TYPE = 10;
}
