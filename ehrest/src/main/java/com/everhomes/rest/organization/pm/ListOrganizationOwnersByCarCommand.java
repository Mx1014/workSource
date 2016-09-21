
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>carId: 车辆id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class ListOrganizationOwnersByCarCommand {

    private Long carId;
    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
