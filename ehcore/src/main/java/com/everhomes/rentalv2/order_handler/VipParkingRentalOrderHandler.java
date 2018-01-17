package com.everhomes.rentalv2.order_handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.parking.ParkingSpaceStatus;
import com.everhomes.rest.rentalv2.PriceRuleType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.VipParkingUseInfoDTO;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.ui.user.SceneType;
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
    @Autowired
    private Rentalv2PriceRuleProvider rentalv2PriceRuleProvider;

    @Override
    public BigDecimal calculateRefundAmount(RentalOrder order, Long now) {

        if (null != order.getRefundStrategy()) {
            if (order.getRefundStrategy() == RentalOrderStrategy.CUSTOM.getCode()) {
                RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
                        order.getResourceType(), order.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId());

                List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(order.getResourceType(), rule.getSourceType(),
                        rule.getId(), RentalOrderHandleType.REFUND.getCode());

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
            }else if (order.getRefundStrategy() == RentalOrderStrategy.FULL.getCode()) {
                return order.getPaidMoney();
            }
        }

        return order.getPaidMoney();
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {

        Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(order.getResourceType(),
                PriceRuleType.RESOURCE.getCode(), order.getRentalResourceId(), order.getRentalType());

        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);
        ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getRentalResourceId());

        ParkingSpace parkingSpace = parkingProvider.getAnyParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                parkingLot.getOwnerId(),parkingLot.getId());

        parkingInfo.setSpaceNo(parkingSpace.getSpaceNo());
        parkingInfo.setSpaceAddress(parkingSpace.getSpaceAddress());
        parkingInfo.setLockId(parkingSpace.getLockId());
        parkingInfo.setPriceStr(priceRule.getWorkdayPrice().toString() + "/半小时");

        if (null != order.getScene()) {
            if (SceneType.PM_ADMIN.getCode().equals(order.getScene())) {
                parkingInfo.setPriceStr(priceRule.getOrgMemberWorkdayPrice().toString() + "/半小时");

            } else if (!SceneType.ENTERPRISE.getCode().equals(order.getScene())) {
                parkingInfo.setPriceStr(priceRule.getApprovingUserWorkdayPrice().toString() + "/半小时");

            }else {
                parkingInfo.setPriceStr(priceRule.getWorkdayPrice().toString() + "/半小时");
            }
        }

        order.setCustomObject(JSONObject.toJSONString(parkingInfo));
        //当是vip车位预约时设置车位编号
        order.setStringTag1(parkingInfo.getSpaceNo());
        order.setStringTag2(parkingInfo.getPlateNumber());

        //更新停车位状态
        parkingSpace.setStatus(ParkingSpaceStatus.IN_USING.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void completeRentalOrder(RentalOrder order) {

        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);

        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(parkingInfo.getSpaceNo());
        //更新停车位状态
        parkingSpace.setStatus(ParkingSpaceStatus.OPEN.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }
}
