package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>ownerType: 所属主体类型</li>
 *     <li>ownerId: 所属主体id</li>
 *     <li>categoryId: 多入口应用ID</li>
 * </ul>
 * Created by ying.xiong on 2017/10/26.
 */
public class ListDefaultChargingItemsCommand {
    private Integer namespaceId;
    private Long communityId;
    private String ownerType;
    private Long ownerId;
    private Long categoryId;//修复缺陷 #45399 【智富汇】【缴费管理】计价条款异常

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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
