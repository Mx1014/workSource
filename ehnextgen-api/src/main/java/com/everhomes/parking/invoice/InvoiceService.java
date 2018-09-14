// @formatter:off
package com.everhomes.parking.invoice;

import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.invoice.*;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/6/21 10:56
 */
public interface InvoiceService {
    ListNotInvoicedOrdersResponse listNotInvoicedOrders(ListNotInvoicedOrdersCommand cmd);

    void notifyOrderInvoiced(NotifyOrderInvoicedCommand cmd);

    GetPayeeIdByOrderNoResponse getPayeeIdByOrderNo(GetPayeeIdByOrderNoCommand cmd);

    List<ParkingLotDTO> listAllParkingLots(ListAllParkingLotsCommand cmd);

	ParkingRechargeOrderDTO parkingRechargeOrdersByOrderNo(long orderNo);
}
