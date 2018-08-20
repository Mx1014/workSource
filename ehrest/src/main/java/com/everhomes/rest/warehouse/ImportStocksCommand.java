package com.everhomes.rest.warehouse;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 * <li>ownerId: 库存所属类型id</li>
 * <li>namespaceId: namespaceId</li>
 * <li>commmunityId: 园区id</li>
 * </ul>
 * Created by ding.jianmin on 2018/7/28.
 */
public class ImportStocksCommand {

	@NotNull
	private Long communityId;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Long ownerId;
	@NotNull
	private String ownerType;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
