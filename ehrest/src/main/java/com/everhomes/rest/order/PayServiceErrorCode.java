// @formatter:off
package com.everhomes.rest.order;

public interface PayServiceErrorCode {
    String SCOPE = "pay";
    int ERROR_INVALID_USER_OWNER_TYPE=10000;
    int ERROR_PAYMENT_ACCOUNT_NO_FIND=10001;
    int ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND=10001;
    int ERROR_CREATE_FAIL=10001;
}
