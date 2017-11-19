// @formatter:off
package com.everhomes.rest.order;

public interface PayServiceErrorCode {
    String SCOPE = "pay";
    int ERROR_INVALID_USER_OWNER=10000;
    int ERROR_PAYMENT_ACCOUNT_NO_FIND=10001;
    int ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND=10002;
    int ERROR_CREATE_FAIL=10003;
    int ERROR_REGISTER_USER_FAIL=10004;
    int ERROR_BIND_PHONE_FAIL=10005;
    int ERROR_SIGNATURE_VERIFY_FAIL=10006;
    int ERROR_REFUND_FAIL=10007;
    /** 可提现金额不足 */
    int ERROR_WITHDRAWABLE_AMOUNT_INSUFFICIENT  =10008;
    /** 希望提现的金额不合法（小于或等于0） */
    int ERROR_INVALID_WITHDRAW_AMOUNT  =10009;
}
