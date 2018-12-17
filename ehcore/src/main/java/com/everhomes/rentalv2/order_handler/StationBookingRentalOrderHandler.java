package com.everhomes.rentalv2.order_handler;

import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalOrderHandler;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "station_booking")
public class StationBookingRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long time) {
        return null;
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {

    }

    @Override
    public void lockOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void releaseOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

    }

    @Override
    public void autoUpdateOrder(RentalOrder order) {

    }

    @Override
    public void checkOrderResourceStatus(RentalOrder order) {

    }

    @Override
    public void nearHalfHourBegin(RentalOrder order) {
        Long currTime = DateHelper.currentGMTTime().getTime();
        if (currTime >= order.getStartTime().getTime() ) {
            order.setStatus(SiteBillStatus.IN_USING.getCode());
            rentalv2Provider.updateRentalBill(order);
        }
    }

    @Override
    public Long getAccountId(RentalOrder order) {
        return null;
    }
}
