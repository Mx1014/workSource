// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>删除充值费率
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），BOSIGAO {@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>rateToken: 费率ID，不同厂商有不同类型的ID</li>
 * </ul>
 */
public class DeleteParkingRechargeRateCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private String vendorName;
	@NotNull
    private String rateToken;
    
    public DeleteParkingRechargeRateCommand() {
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

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRateToken() {
        return rateToken;
    }

    public void setRateToken(String rateToken) {
        this.rateToken = rateToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
