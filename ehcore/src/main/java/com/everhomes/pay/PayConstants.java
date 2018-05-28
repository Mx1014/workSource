package com.everhomes.pay;

public interface PayConstants {

    String CORE_SERVER_URL = "core.server.url";

    String OAUTH2_REDIRECT_LOGON_URL = "evh/pay/oauth2/redirect/logon";
    String TOKEN_SERVICE_URL = "oauth2/token";
    String OAUTH2API_URL = "oauth2/api";
    String AUTHORIZE_SERVICE_URL = "oauth2/authorize";

    //constants stored in namespace table
    String APP_KEY = "pay_app_key";
    String APP_SECRET="pay_app_secret_key";
}
