// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>parkingLotId: 停车场id</li>
 *     <li>actionType: icon的ActionType {@link com.everhomes.rest.launchpad.ActionType}</li>
 * </ul>
 */
public class CheckAuthorityCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private Byte actionType;
    private Long parkingLotId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getActionType() {
        return actionType;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
