//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class ListPaymentBillResp {
    private Long offset;
    private Long limit;
    private List<PaymentBillResp> list;

    public ListPaymentBillResp() {

    }

    public ListPaymentBillResp(Long limit) {
        this.limit = limit;
    }

    public ListPaymentBillResp(Long offset, Long limit) {
        this.limit = limit;
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }
    public void setOffset(Long offset) {
        this.offset = offset;
    }
    public Long getLimit() {
        return limit;
    }
    public void setLimit(Long limit) {
        this.limit = limit;
    }
    public List<PaymentBillResp> getList() {
        return list;
    }
    public void setList(List<PaymentBillResp> list) {
        this.list = list;
    }
}
