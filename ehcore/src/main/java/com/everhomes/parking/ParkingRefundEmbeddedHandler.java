package com.everhomes.parking;

import com.everhomes.order.RefundEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component(RefundEmbeddedHandler.REFUND_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PARKING_CODE )
public class ParkingRefundEmbeddedHandler implements RefundEmbeddedHandler {

    @Autowired
    private ParkingProvider parkingProvider;

    @Override
    public void paySuccess(RefundCallbackCommand cmd) {
        ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderByOrderNo(Long.valueOf(cmd.getRefundOrderNo()));
        order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
        order.setRefundTime(new Timestamp(System.currentTimeMillis()));
        parkingProvider.updateParkingRechargeOrder(order);
    }
}
