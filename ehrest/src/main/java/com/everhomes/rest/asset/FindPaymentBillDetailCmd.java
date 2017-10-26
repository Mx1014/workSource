//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class FindPaymentBillDetailCmd {
    private String paymentOrderNum;

    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }

    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }
}
