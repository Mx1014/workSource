package com.everhomes.rentalv2.order_handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.parking.ParkingSpaceStatus;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
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
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    public BigDecimal getRefundAmount(RentalOrder order, Long now) {

        BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, now);

        return refundAmount;
    }

    @Override
    public void updateOrderResourceInfo(RentalOrder order) {

        updateOrderResourceInfo(order, false);
    }

    private void updateOrderResourceInfo(RentalOrder order, boolean needFree) {
        Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(order.getResourceType(),
                PriceRuleType.RESOURCE.getCode(), order.getRentalResourceId(), order.getRentalType());

        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);
        ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getRentalResourceId());

        List<String> spaces = rentalv2Provider.listOverTimeSpaces(parkingLot.getNamespaceId(), order.getResourceTypeId(),
                RentalV2ResourceType.VIP_PARKING.getCode(), parkingLot.getId());

        ParkingSpace parkingSpace;
        if (needFree) {
            parkingSpace = parkingProvider.getAnyFreeParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId());
        }else {
            parkingSpace = parkingProvider.getAnyParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId(), spaces);
        }


        if (null == parkingSpace) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid param rules");
        }

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
        //当是vip车位预约时设置车位编号 TODO: 改用 Rentalv2CustomField
        order.setStringTag1(parkingInfo.getSpaceNo());
        order.setStringTag2(parkingInfo.getPlateNumber());

        //更新停车位状态
        parkingSpace.setStatus(ParkingSpaceStatus.IN_USING.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void lockOrderResourceStatus(RentalOrder order) {
        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(order.getStringTag1());
        parkingSpace.setStatus(ParkingSpaceStatus.IN_USING.getCode());
        parkingProvider.updateParkingSpace(parkingSpace);
    }

    @Override
    public void releaseOrderResourceStatus(RentalOrder order) {
        ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(order.getStringTag1());
        parkingSpace.setStatus(ParkingSpaceStatus.OPEN.getCode());
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

    @Override
    public void autoUpdateOrder(RentalOrder order) {

        RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());

        ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getRentalResourceId());

        List<RentalOrder> overTimeOrders = rentalv2Provider.listOverTimeRentalOrders(order.getNamespaceId(),
                order.getResourceTypeId(), order.getResourceType(), order.getRentalResourceId(), order.getStringTag1());
        if (!overTimeOrders.isEmpty()) {
            ParkingSpace parkingSpace = parkingProvider.getAnyFreeParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                    parkingLot.getOwnerId(),parkingLot.getId());
            if (null != parkingSpace) {
                updateOrderResourceInfo(order, true);

                order.setStatus(SiteBillStatus.IN_USING.getCode());
                rentalv2Provider.updateRentalBill(order);

                handler.autoUpdateOrderSpaceSendMessage(order);
            }else {
                rentalCommonService.refundOrder(order, System.currentTimeMillis(), order.getPaidMoney());
                order.setStatus(SiteBillStatus.FAIL.getCode());
                rentalv2Provider.updateRentalBill(order);

                handler.autoCancelOrderSendMessage(order);
            }
        }else {
            order.setStatus(SiteBillStatus.IN_USING.getCode());
            rentalv2Provider.updateRentalBill(order);
        }
    }

    @Override
    public void checkOrderResourceStatus(RentalOrder order) {

    }
}
