package com.everhomes.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>ownerType: EhOrganizations</li>
 * <li>ownerId: 会议所属分公司ID</li>
 * <li>name: 会议室名称</li>
 * <li>id: 当前会议室的ID</li>
 * </ul>
 */
public class CheckMeetingRoomNameExistCondition {
    private Integer namespaceId;
    private Long organizationId;
    private String ownerType;
    private Long ownerId;
    private String name;
    private Long id;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
