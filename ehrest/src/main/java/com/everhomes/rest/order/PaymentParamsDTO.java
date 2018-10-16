//@formatter:off
package com.everhomes.rest.order;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/9/7.
 */

public class PaymentParamsDTO {
    @NotNull
    private String payType;
    private String acct;
    private String vspCusid;
    private String paymentExtendInfoParamsJson;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getVspCusid() {
        return vspCusid;
    }

    public void setVspCusid(String vspCusid) {
        this.vspCusid = vspCusid;
    }

    public String getPaymentExtendInfoParamsJson() {
        return paymentExtendInfoParamsJson;
    }

    public void setPaymentExtendInfoParamsJson(String paymentExtendInfoParamsJson) {
        this.paymentExtendInfoParamsJson = paymentExtendInfoParamsJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
