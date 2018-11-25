// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>paymentStartDate: 开始时间</li>
 * <li>paymentEndDate: 结束时间</li>
 * <li>paymentSceneAppId: 支付应用场景id</li>
 * <li>keyWords: 员工姓名/电话</li>
 * <li>orderNo: 订单号</li>
 * </ul>
 */
public class ExportEnterprisePaymentPayLogsCommand {
    private Long organizationId;
    private Long paymentStartDate;
    private Long paymentEndDate;
    private Long paymentSceneAppId;
    private String keyWords;
    private String orderNo;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(Long paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public Long getPaymentEndDate() {
        return paymentEndDate;
    }

    public void setPaymentEndDate(Long paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }

    public Long getPaymentSceneAppId() {
        return paymentSceneAppId;
    }

    public void setPaymentSceneAppId(Long paymentSceneAppId) {
        this.paymentSceneAppId = paymentSceneAppId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
