//@formatter:off
package com.everhomes.organization.pm;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2018/6/13.
 */

public class ReservationInfo {
    private Timestamp endTime;
    private Long addressId;
    private Long reservationId;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
