package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li> parentId:父节点ID  </li>
 *  <li> ownerType: 拥有者类型：现在是comunity</li>
 *  <li> ownerId: 拥有者ID</li>
 * </ul>
 */
public class GetServiceAllianceEnterpriseDetailCommand {
	
	private Long id;
	
	private Long parentId;
	
	private String ownerType;
	
	private Long ownerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
