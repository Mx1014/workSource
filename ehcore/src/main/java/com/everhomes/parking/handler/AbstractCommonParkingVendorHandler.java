package com.everhomes.parking.handler;

import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingNotificationTemplateCode;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingSupportRechargeStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sw on 2017/8/16.
 */
public abstract class AbstractCommonParkingVendorHandler implements ParkingVendorHandler {

    protected boolean checkExpireTime(ParkingLot parkingLot, long expireTime) {
        long now = System.currentTimeMillis();
        long cardReserveTime = 0;

        Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
        if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
            Integer cardReserveDay = parkingLot.getCardReserveDays();
            cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

        }

        return expireTime + cardReserveTime < now;
    }

    protected ParkingCardDTO convertCardInfo(ParkingLot parkingLot) {
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

        parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
        parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
        parkingCardDTO.setParkingLotId(parkingLot.getId());
        parkingCardDTO.setIsValid(true);//兼容历史app

        return parkingCardDTO;
    }

}
