package com.everhomes.rentalv2.order_handler;

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

    @Override
    public BigDecimal calculateRefundAmount(RentalOrder order, Long time) {

        return order.getPaidMoney().multiply(new BigDecimal(order.getRefundRatio()))
                .divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {
        //TODO:
    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

    }
}
