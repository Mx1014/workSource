//@formatter:off
package com.everhomes.rest.organization.pm;

/**
 * Created by Wentian Wang on 2018/6/12.
 */
/**
 *<ul>
 * <li>reservationId: id of reservation</li>
 *</ul>
 */
public class DeleteReservationCommand {
    private Long reservationId;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
