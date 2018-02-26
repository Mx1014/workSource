package com.everhomes.rentalv2.order_handler;

import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "default")
public class DefaultRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long time) {
        BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, time);
        return refundAmount;
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {
        //TODO:
    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

    }

    @Override
    public void lockOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void releaseOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void autoUpdateOrder(RentalOrder order) {

    }

    @Override
    public void checkOrderResourceStatus(RentalOrder order) {

    }
}
