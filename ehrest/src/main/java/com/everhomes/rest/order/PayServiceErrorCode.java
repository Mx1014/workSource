// @formatter:off
package com.everhomes.rest.order;

public interface PayServiceErrorCode {
    String SCOPE = "pay";
    int ERROR_INVALID_USER_OWNER=10000;
    int ERROR_PAYMENT_ACCOUNT_NO_FIND=10001;
    int ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND=10001;
    int ERROR_CREATE_FAIL=10001;
    int ERROR_REGISTER_USER_FAIL=10001;
    int ERROR_BIND_PHONE_FAIL=10001;
    int ERROR_SIGNATURE_VERIFY_FAIL=10001;
}
