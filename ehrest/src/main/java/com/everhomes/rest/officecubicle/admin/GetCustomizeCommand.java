package com.everhomes.rest.officecubicle.admin;


/**
 * <ul>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目</li>
 * </ul>
 */
public class GetCustomizeCommand {
    private String ownerType;
    private Long ownerId;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
