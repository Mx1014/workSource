//@formatter:off
package com.everhomes.rest.organization.pm;

/**
 * Created by Wentian Wang on 2018/6/13.
 */
/**
 *<ul>
 * <li>reservationId:预约id</li>
 *</ul>
 */
public class CancelReservationCommand {
    private Long reservationId;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
