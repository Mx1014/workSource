// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>sourceRequestType: 请求来源，为空或者0:客户端 1:后台管理 {@link com.everhomes.rest.parking.ParkingSourceRequestType}</li>
 * </ul>
 */
public class ListParkingLotsCommand {
    /**
     * 小区类型
     */
    @NotNull
    private String ownerType;
    /**
     * 小区id
     */
    @NotNull
    private Long ownerId;

    private Byte sourceRequestType;

    public Byte getSourceRequestType() {
        return sourceRequestType;
    }

    public void setSourceRequestType(Byte sourceRequestType) {
        this.sourceRequestType = sourceRequestType;
    }

    public ListParkingLotsCommand() {
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
