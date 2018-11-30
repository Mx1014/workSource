package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>resourceTypeId: 资源类型id</li>
 * </ul>
 */
public class StationBookingInstanceConfig {

    private Long resourceTypeId;
    private Byte currentProjectOnly;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Byte getCurrentProjectOnly() {
        return currentProjectOnly;
    }

    public void setCurrentProjectOnly(Byte currentProjectOnly) {
        this.currentProjectOnly = currentProjectOnly;
    }
}
