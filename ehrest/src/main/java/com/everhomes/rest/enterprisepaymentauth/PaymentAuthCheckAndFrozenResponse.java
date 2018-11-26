package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authFlag: 鉴权状态</li>
 * <li>statusCode: 状态码</li>
 * </ul>
 */
public class PaymentAuthCheckAndFrozenResponse {
    private Byte authFlag;
    private Integer statusCode;

    public PaymentAuthCheckAndFrozenResponse() {
        this.authFlag = NormalFlag.NO.getCode();
    }

    public PaymentAuthCheckAndFrozenResponse(Byte authFlag, Integer statusCode) {
        this.authFlag = authFlag;
        this.statusCode = statusCode;
    }

    public Byte getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(Byte authFlag) {
        this.authFlag = authFlag;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
