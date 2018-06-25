// @formatter:off
package com.everhomes.parking.invoice;

import com.everhomes.rest.parking.invoice.*;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/6/21 10:56
 */
public interface InvoiceService {
    ListNotInvoicedOrdersResponse listNotInvoicedOrders(ListNotInvoicedOrdersCommand cmd);

    void notifyOrderInvoiced(NotifyOrderInvoicedCommand cmd);

    GetPayeeIdByOrderNoResponse getPayeeIdByOrderNo(GetPayeeIdByOrderNoCommand cmd);
}
