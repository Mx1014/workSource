package com.everhomes.rentalv2.order_handler;

import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "default")
public class DefaultRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private Rentalv2AccountProvider rentalv2AccountProvider;

    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long time) {
        BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, time);
        return refundAmount;
    }

    @Override
    public Long getAccountId(RentalOrder order) {
        //查特殊账户
        List<Rentalv2PayAccount> accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        //查通用账户
        accounts = this.rentalv2AccountProvider.listPayAccounts(null, order.getCommunityId(), RentalV2ResourceType.DEFAULT.getCode(),
                null, RuleSourceType.DEFAULT.getCode(), order.getResourceTypeId(), null, null);
        if (accounts != null && accounts.size()>0)
            return accounts.get(0).getAccountId();
        return null;
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
