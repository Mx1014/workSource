//@formatter:off
package com.everhomes.rest.asset;

import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class CheckPaymentUserResponse {
    private Long id;
    private String ownerType;
    private String ownerId;
    private Byte paymentUserType;
    private Long payment_user_id;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getPaymentUserType() {
        return paymentUserType;
    }

    public void setPaymentUserType(Byte paymentUserType) {
        this.paymentUserType = paymentUserType;
    }

    public Long getPayment_user_id() {
        return payment_user_id;
    }

    public void setPayment_user_id(Long payment_user_id) {
        this.payment_user_id = payment_user_id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
