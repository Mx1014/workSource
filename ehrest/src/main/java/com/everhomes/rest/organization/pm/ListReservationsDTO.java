//@formatter:off
package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2018/6/12.
 */
/**
 *<ul>
 * <li>reservationId: id of reservation</li>
 * <li>addressName: 门牌名称</li>
 * <li>enterpriseCustomerName: 企业客户的名称，预订者的名字</li>
 * <li>startTime: 预定开始时间</li>
 * <li>endTime: 预定结束时间</li>
 * <li>createUname: 此条预定的创建者的时间</li>
 * <li>reservationStatus: 预定状态，1:无效；2：有效；3：删除的。参考{@link com.everhomes.rest.organization.pm.ReservationStatus}</li>
 *</ul>
 */
public class ListReservationsDTO {
    private Long reservationId;
    private String addressName;
    private String enterpriseCustomerName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String createUname;
    private Byte reservationStatus;
    private Long enterpriseCustomerId;
    private Long addressId;

    public Long getEnterpriseCustomerId() {
        return enterpriseCustomerId;
    }

    public void setEnterpriseCustomerId(Long enterpriseCustomerId) {
        this.enterpriseCustomerId = enterpriseCustomerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getCreateUname() {
        return createUname;
    }

    public void setCreateUname(String createUname) {
        this.createUname = createUname;
    }

    public Byte getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(Byte reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getEnterpriseCustomerName() {
        return enterpriseCustomerName;
    }

    public void setEnterpriseCustomerName(String enterpriseCustomerName) {
        this.enterpriseCustomerName = enterpriseCustomerName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
