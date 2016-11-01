package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *   <li>id:类型id</li>
 *   <li>displayName:显示名称</li>
 * </ul>
 */
public class OrganizationOwnerCarParkingTypeDTO {
    private Long   id;
    private String displayName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
