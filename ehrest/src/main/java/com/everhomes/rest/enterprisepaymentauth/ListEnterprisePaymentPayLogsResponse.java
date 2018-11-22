// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>payLogs: 支付记录 {@link com.everhomes.rest.enterprisepaymentauth.PaymentPayLogDTO}</li>
 * <li>nextPageOffset: 下一页</li>
 * </ul>
 */
public class ListEnterprisePaymentPayLogsResponse {
    private List<PaymentPayLogDTO> payLogs;
    private Integer nextPageOffset;

    public List<PaymentPayLogDTO> getPayLogs() {
        return payLogs;
    }

    public void setPayLogs(List<PaymentPayLogDTO> payLogs) {
        this.payLogs = payLogs;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
