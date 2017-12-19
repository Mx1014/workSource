package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
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
