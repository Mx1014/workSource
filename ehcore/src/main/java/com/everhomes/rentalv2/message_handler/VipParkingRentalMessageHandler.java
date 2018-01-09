package com.everhomes.rentalv2.message_handler;

import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.RentalMessageHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalOrderHandler;
import com.everhomes.rentalv2.Rentalv2Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalMessageHandler implements RentalMessageHandler {

    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {

    }
}
