package com.everhomes.rentalv2;

/**
 * @author sw on 2018/1/9.
 */
public interface RentalMessageHandler {
    String RENTAL_MESSAGE_HANDLER_PREFIX = "RentalMessageHandler-";
    String DEFAULT = "default";

    public void cancelOrderSendMessage(RentalOrder rentalBill);
}
