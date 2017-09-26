package com.everhomes.rest.contentserver;

public interface ContentServerErrorCode {
    static final String SCOPE = "contentserver";
    static final int ERROR_INVALID_PRIVILLAGE = 10000;
    static final int ERROR_INVALID_SESSION = 10001;
    static final int ERROR_INVALID_SERVER = 10002;
    static final int ERROR_INVALID_USER = 10003;
    static final int ERROR_INVALID_ACTION = 10004;
    static final int ERROR_INVALID_PARAMS = 10005;
    static final int ERROR_INVALID_UUID = 10006;

}
