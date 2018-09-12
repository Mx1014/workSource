package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <p>用户须知详情信息</p>
 * <ul>
 * <li>parkingLotId : 停车场ID</li>
 * </ul>
 */
public class UserNoticeInvoiceCommand {
    private Long parkingLotId;


    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
