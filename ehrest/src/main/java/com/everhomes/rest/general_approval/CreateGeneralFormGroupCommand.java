package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupName: 字段组名称</li>
 * <li>organizationId: 公司id</li>
 * <li>ownerId: ownerId</li>
 * <li>ownerType: ownerType</li>
 * <li>formOriginId: 表单id</li>
 * <li>formVersion: 表单版本</li>
 * </ul>
 */
public class CreateGeneralFormGroupCommand {

    private String groupName;

    private Long organizationId;

    private Long ownerId;

    private String ownerType;

    private Long formOriginId;

    private Long formVersion;

    public CreateGeneralFormGroupCommand() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
