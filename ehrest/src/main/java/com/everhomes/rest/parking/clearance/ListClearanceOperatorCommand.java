// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>parkingLotId: 停车场id</li>
 *     <li>operatorType: 用户类型 {@link com.everhomes.rest.parking.clearance.ParkingClearanceOperatorType}</li>
 *     <li>pageSize: 一页显示条数</li>
 *     <li>pageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListClearanceOperatorCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private Long parkingLotId;
    @EnumType(ParkingClearanceOperatorType.class)
    @NotNull private String operatorType;

    private Integer pageSize;
    private Long pageAnchor;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
