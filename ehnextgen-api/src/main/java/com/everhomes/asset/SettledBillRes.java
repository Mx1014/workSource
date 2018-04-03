//@formatter:off
package com.everhomes.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/12/15.
 */

public class SettledBillRes {
    private Long nextPageAnchor;
    @ItemType(PaymentBills.class)
    private List<PaymentBills> bills;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PaymentBills> getBills() {
        return bills;
    }

    public void setBills(List<PaymentBills> bills) {
        this.bills = bills;
    }
}
