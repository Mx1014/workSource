package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>status: 是否开放 1：开放，0：关闭 {@link com.everhomes.rest.parking.ParkingSpaceStatus}</li>
 * </ul>
 */
public class UpdateParkingSpaceStatusCommand {

    private Long id;
    private Byte status;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
