//@formatter:off
package com.everhomes.order;

import java.util.Map;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public class PayMethod {

    //1-微信APP支付,8-支付宝扫码支付
    private Integer paymentType;
    private String paymentName;
    private String paymentLogo;
    private Map<String,String> paymentParams;
    private paymentExtendInfo paymentExtendInfo;

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentLogo() {
        return paymentLogo;
    }

    public void setPaymentLogo(String paymentLogo) {
        this.paymentLogo = paymentLogo;
    }

    public Map<String, String> getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(Map<String, String> paymentParams) {
        this.paymentParams = paymentParams;
    }

    public com.everhomes.order.paymentExtendInfo getPaymentExtendInfo() {
        return paymentExtendInfo;
    }

    public void setPaymentExtendInfo(com.everhomes.order.paymentExtendInfo paymentExtendInfo) {
        this.paymentExtendInfo = paymentExtendInfo;
    }

    public PayMethod() {
    }
}
