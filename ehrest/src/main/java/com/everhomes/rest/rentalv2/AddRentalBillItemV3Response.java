package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>flowCaseUrl : payMode 为1线下支付的时候,需要url跳转工作流 </li>
 * <li>merchantOrderId : 统一订单id </li>
 * <li>merchantId : 商户id</li>
 * <li>payUrl : 统一订单跳转地址(客户端原生用)</li>
 * </ul>
 */
public class AddRentalBillItemV3Response {

    private String flowCaseUrl;
    private Long billId;
    private Long merchantOrderId;
    private Long merchantId;
    private String payUrl;

    public String getFlowCaseUrl() {
        return flowCaseUrl;
    }

    public void setFlowCaseUrl(String flowCaseUrl) {
        this.flowCaseUrl = flowCaseUrl;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
