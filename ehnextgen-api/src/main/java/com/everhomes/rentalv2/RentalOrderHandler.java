package com.everhomes.rentalv2;

import java.math.BigDecimal;

/**
 * @author sw on 2018/1/9.
 */
public interface RentalOrderHandler {
    String RENTAL_ORDER_HANDLER_PREFIX = "RentalOrderHandler-";
    String DEFAULT = "default";

    public BigDecimal calculateRefundAmount(RentalOrder order, Long time);
}
