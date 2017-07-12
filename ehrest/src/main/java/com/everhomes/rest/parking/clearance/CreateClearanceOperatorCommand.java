// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>parkingLotId: 停车场id</li>
 *     <li>operatorType: 添加的用户类型 {@link com.everhomes.rest.parking.clearance.ParkingClearanceOperatorType}</li>
 *     <li>userIds: 添加的用户id列表</li>
 * </ul>
 */
public class CreateClearanceOperatorCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private Long parkingLotId;
    @EnumType(ParkingClearanceOperatorType.class)
    private String operatorType;
    @ItemType(Long.class)
    private List<Long> userIds;

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
