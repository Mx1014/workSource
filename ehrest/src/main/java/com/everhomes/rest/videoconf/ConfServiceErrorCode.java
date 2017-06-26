package com.everhomes.rest.videoconf;

public interface ConfServiceErrorCode {
	static final String SCOPE = "videoConf";
	
    static final int ERROR_INVALID_ACCOUNT=10000; 
    static final int ERROR_INVALID_CONF_ID=10001; 
    static final int ERROR_INVALID_USER_COUNT=10002; 
    static final int ERROR_INVALID_ACCOUNT_COUNT=10003; 
    static final int ERROR_INVALID_USER_ACCOUNT=10004; 
    static final int ERROR_INVALID_ASSIGN=10005; 
    static final int ZUOLIN_NAMESPACE_NAME=10006;
    static final int CONF_NOT_OPEN=10007; 
    static final int CONF_INVOICE_SUBJECT=10008; 
    static final int CONF_INVOICE_BODY=10009; 
    static final int ERROR_INVALID_ENTERPRISE=10010; 
    static final int CONF_ENTERPRISE_HAS_ACTIVE_ACCOUNT=10011; 
    static final int CONF_CATEGORY_NOT_FOUND =10012; 
    static final int CONF_CAN_NOT_TRIAL_MORE =10013; 
}
