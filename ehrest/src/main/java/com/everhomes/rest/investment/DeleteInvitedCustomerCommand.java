package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>communityId: communityId</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>orgId: orgId</li>
 *     <li>deviceType: deviceType</li>
 * </ul>
 */
public class DeleteInvitedCustomerCommand {

    private Long id;

    private Long communityId;

    private Integer namespaceId;

    private Long orgId;

    private Byte deviceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
