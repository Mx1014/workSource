package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>lockId: 锁id</li>
 * </ul>
 */
public class DownParkingSpaceLockCommand {
    private String lockId;
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
