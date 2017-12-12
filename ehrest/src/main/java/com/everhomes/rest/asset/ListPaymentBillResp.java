//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class ListPaymentBillResp {
    private Long nextPageAnchor;
    private Long pageSize;
    @ItemType(PaymentBillResp.class)
    private List<PaymentBillResp> list;

    public ListPaymentBillResp() {

    }
    public ListPaymentBillResp(Long nextPageAnchor,Long pageSize) {
        this.nextPageAnchor = nextPageAnchor;
        this.pageSize = pageSize;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<PaymentBillResp> getList() {
        return list;
    }
    public void setList(List<PaymentBillResp> list) {
        this.list = list;
    }
}
