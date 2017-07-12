package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> parentId: 父id</li>
 *  <li> ownerType: 拥有者类型：现在是community</li>
 *  <li> ownerId: 拥有者ID</li>
 *  <li> destination: 输出终端 1：客户端 2：浏览器</li>
 * </ul>
 */
public class ListServiceAllianceCategoriesCommand {

	private Long parentId;
	
	private String ownerType;
	
	private Long ownerId;

	private Byte destination;

	public Byte getDestination() {
		return destination;
	}

	public void setDestination(Byte destination) {
		this.destination = destination;
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
