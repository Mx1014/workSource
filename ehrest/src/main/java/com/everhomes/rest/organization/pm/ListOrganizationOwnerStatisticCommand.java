package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>livingStatus: 是否在户</li>
 *     <li>orgOwnerTypeIds: 业主类型id列表</li>
 *      <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 *      <li>ownerId: ownerId, communityId</li>
 * </ul>
 */
public class ListOrganizationOwnerStatisticCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    private Byte livingStatus;
    @ItemType(Long.class)
    private List<Long> orgOwnerTypeIds;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public List<Long> getOrgOwnerTypeIds() {
        return orgOwnerTypeIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setOrgOwnerTypeIds(List<Long> orgOwnerTypeIds) {
        this.orgOwnerTypeIds = orgOwnerTypeIds;
    }
}
