// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型</li>
 * <li>ownerId: 所属实体id</li>
 * <li>name: 名称</li>
 * <li>discription: 描述</li>
 * </ul>
 */
public class CreateOrganizationJobPositionCommand {

    private String ownerType;

    private Long ownerId;

    private String name;

    private String discription;


    public CreateOrganizationJobPositionCommand() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
