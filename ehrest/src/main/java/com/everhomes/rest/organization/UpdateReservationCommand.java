//@formatter:off
package com.everhomes.rest.organization;

/**
 * Created by Wentian Wang on 2018/6/12.
 */

import java.sql.Timestamp;

/**
 *<ul>
 * <li>reservationId: id of reservation</li>
 * <li>addressId: 门牌id</li>
 * <li>enterpriseCustomerId: 企业客户id</li>
 * <li>startTime: 开始时间, 时间戳，非精确选择的时间字段需要置为0，例如传来的时间最多精确到分钟，那么秒钟到0，如果有业务场景不满足再更改</li>
 * <li>endTime：结束时间</li>
 *</ul>
 */
public class UpdateReservationCommand {
    private Long reservationId;
    private Long addressId;
    private Long enterpriseCustomerId;
    private String startTime;
    private String endTime;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getEnterpriseCustomerId() {
        return enterpriseCustomerId;
    }

    public void setEnterpriseCustomerId(Long enterpriseCustomerId) {
        this.enterpriseCustomerId = enterpriseCustomerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
