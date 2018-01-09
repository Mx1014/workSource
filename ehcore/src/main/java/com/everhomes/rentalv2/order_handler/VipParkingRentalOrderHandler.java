package com.everhomes.rentalv2.order_handler;

import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.admin.RentalDurationType;
import com.everhomes.rest.rentalv2.admin.RentalDurationUnit;
import com.everhomes.rest.rentalv2.admin.RentalOrderHandleType;
import com.everhomes.rest.rentalv2.admin.ResourceOrderRuleDTO;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalOrderHandler implements RentalOrderHandler {

    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    public BigDecimal calculateRefundAmount(RentalOrder order, Long now) {

        RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
                order.getResourceType(), order.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId());

        List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(rule.getSourceType(), rule.getId(),
                RentalOrderHandleType.REFUND.getCode());

        List<RentalOrderRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                .collect(Collectors.toList());
        List<RentalOrderRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                .collect(Collectors.toList());

        RentalOrderRule orderRule = null;

        Long startUseTime = order.getStartTime().getTime();

        long intervalTime = startUseTime - now;

        //处于时间外，查找最大的时间
        for (RentalOrderRule r: outerRules) {
            long duration = 0;

            if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                duration = (long)(r.getDuration() * 60 * 60 * 1000);
            }
            if (intervalTime > duration) {
                if (null == orderRule || r.getDuration() > orderRule.getDuration()) {
                    orderRule = r;
                }
            }
        }
        if (orderRule == null) {
            //处于时间内，查找最小的时间
            for (RentalOrderRule r: innerRules) {
                long duration = 0;

                if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                    duration = (long)(r.getDuration() * 60 * 60 * 1000);
                }
                if (intervalTime < duration) {
                    if (null == orderRule || r.getDuration() < orderRule.getDuration()) {
                        orderRule = r;
                    }
                }
            }
        }

        return order.getPaidMoney().multiply(new BigDecimal(orderRule.getFactor()))
                .divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }
}
