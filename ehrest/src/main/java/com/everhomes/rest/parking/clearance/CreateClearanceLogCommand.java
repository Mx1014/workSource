// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>parkingLotId: 停车场id</li>
 *     <li>plateNumber: 车牌号</li>
 *     <li>clearanceTime: 放行时间</li>
 *     <li>remarks: 备注</li>
 * </ul>
 */
public class CreateClearanceLogCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private Long parkingLotId;
    @NotNull @Size(max = 32)
    private String plateNumber;
    @NotNull private Long clearanceTime;
    private String remarks;

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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getClearanceTime() {
        return clearanceTime;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setClearanceTime(Long clearanceTime) {
        this.clearanceTime = clearanceTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
