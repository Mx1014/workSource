package com.everhomes.parking;

import com.everhomes.order.RefundEmbeddedHandler;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ParkingRefundEmbeddedHandler implements RefundEmbeddedHandler {

    @Autowired
    private ParkingProvider parkingProvider;

    @Override
    public void paySuccess(RefundCallbackCommand cmd) {
        ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(Long.valueOf(cmd.getRefundOrderNo()));
        order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
        parkingProvider.updateParkingRechargeOrder(order);
    }
}
