package com.everhomes.rest.organization.pm;

/**
 *  <ul>
 *      <li>ownerId: 业主id</li>
 *      <li>carId: 车辆id</li>
 *      <li>organizationId: 公司id</li>
 *  </ul>
 */
public class SetOrganizationOwnerAsCarPrimaryCommand {
    private Long ownerId;
    private Long carId;
    private Long organizationId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
