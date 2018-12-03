package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remainAmount: 授权余额,金额*100</li>
 * <li>authFlag: 授权状态,0:无授权，1:已授权</li>
 * </ul>
 */
public class GetEmployeePaymentAuthStatusResponse {
    private Long remainAmount;
    private Byte authFlag;

    public GetEmployeePaymentAuthStatusResponse() {
        this.remainAmount = 0L;
        this.authFlag = NormalFlag.NO.getCode();
    }

    public Long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Long remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Byte getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(Byte authFlag) {
        this.authFlag = authFlag;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
