package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>cars: 车辆列表</li>
 * </ul>
 */
public class ListOrganizationOwnerCarResponse {

    private Long nextPageAnchor;
    @ItemType(OrganizationOwnerCarDTO.class)
    private List<OrganizationOwnerCarDTO> cars;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OrganizationOwnerCarDTO> getCars() {
        return cars;
    }

    public void setCars(List<OrganizationOwnerCarDTO> cars) {
        this.cars = cars;
    }
}
