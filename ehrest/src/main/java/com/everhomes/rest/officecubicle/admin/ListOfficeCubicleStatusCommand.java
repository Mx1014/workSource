package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
  * <li>namespaceId : namespaceId </li>
  * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 * <li>spaceId: 空间Id</li>
 * </ul>
 */
public class ListOfficeCubicleStatusCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long spaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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



	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
