//@formatter:off
package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>paymentType: 支付方式，//1-微信APP支付,8-支付宝扫码支付,9-微信公众号支付参考{@link com.everhomes.pay.order.PaymentType}</li>
 *     <li>paymentName: 支付方式名称</li>
 *     <li>paymentLogo: paymentLogo</li>
 *     <li>paymentParams: 参数 {@link com.everhomes.rest.order.PaymentParamsDTO}</li>
 *     <li>extendInfo: 扩展信息jsonstring {@link com.everhomes.rest.order.PaymentExtendInfo}</li>
 *     <li>validationType: 校验类型</li>
 * </ul>
 */
public class PayMethodDTO {

    //1-微信APP支付,8-支付宝扫码支付
    private Integer paymentType;
    private String paymentName;
    private String paymentLogo;
    private PaymentParamsDTO paymentParams;
    private String extendInfo;
    private Integer validationType;

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

    public PaymentParamsDTO getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(PaymentParamsDTO paymentParams) {
        this.paymentParams = paymentParams;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public PayMethodDTO() {
    }

    public Integer getValidationType() {
        return validationType;
    }

    public void setValidationType(Integer validationType) {
        this.validationType = validationType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
