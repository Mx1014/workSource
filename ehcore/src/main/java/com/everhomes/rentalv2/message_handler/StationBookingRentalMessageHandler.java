package com.everhomes.rentalv2.message_handler;

import com.everhomes.rentalv2.RentalMessageHandler;
import com.everhomes.rentalv2.RentalOrder;
import org.springframework.stereotype.Component;

@Component(RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX + "station_booking")
public class StationBookingRentalMessageHandler implements RentalMessageHandler{
    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void sendOrderOverTimeMessage(RentalOrder rentalBill) {

    }

    @Override
    public void sendRentalSuccessSms(RentalOrder order) {

    }

    @Override
    public void renewRentalOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void endReminderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void overTimeSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void completeOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void autoCancelOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void autoUpdateOrderSpaceSendMessage(RentalOrder rentalBill) {

    }
}
