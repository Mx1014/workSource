// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
  *<ul>
  *<li>nextPageAnchor : (Long)下一页锚点</li>
  *<li>invoicedOrderList : (List)未开票订单,{@link InvoicedOrderDTO}</li>
  *<li>count : (Long)总数</li>
  *</ul>
  */

public class ListNotInvoicedOrdersResponse {
    private Long nextPageAnchor;
    private List<InvoicedOrderDTO> invoicedOrderList;
    private Long count;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<InvoicedOrderDTO> getInvoicedOrderList() {
        return invoicedOrderList;
    }

    public void setInvoicedOrderList(List<InvoicedOrderDTO> invoicedOrderList) {
        this.invoicedOrderList = invoicedOrderList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
