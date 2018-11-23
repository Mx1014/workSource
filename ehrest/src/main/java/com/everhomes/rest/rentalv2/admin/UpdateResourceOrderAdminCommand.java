package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 资源id</li>
 * <li>defaultOrderId: 交换顺序id
 * </li>
 * </ul>
 */
public class UpdateResourceOrderAdminCommand {
    private String resourceType;
    private List<DefaultOrderDTO> defaultOrder;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<DefaultOrderDTO> getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(List<DefaultOrderDTO> defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
