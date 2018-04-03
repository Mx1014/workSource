package com.everhomes.rest.acl.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>commands: 用户信息(传递名称与手机号) {@link com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand}</li>
 * </ul>
 */
public class CreateOrganizationAdminsCommand {

    private String ownerType;

    private Long ownerId;

    private Long organizationId;

    @ItemType(CreateOrganizationAdminCommand.class)
    private List<CreateOrganizationAdminCommand> commands;

    public CreateOrganizationAdminsCommand() {
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<CreateOrganizationAdminCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<CreateOrganizationAdminCommand> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
