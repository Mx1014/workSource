package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:项目id</li>
 * <li>ownerType:项目类型</li>
 * <li>spaceId:空间id</li>
 * <li>keyword:关键字</li>
 * </ul>
 */
public class GetStationForRoomCommand {
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long spaceId;
	private String keyword;
    

	



	public Long getSpaceId() {
		return spaceId;
	}




	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}




	public String getKeyword() {
		return keyword;
	}




	public void setKeyword(String keyword) {
		this.keyword = keyword;
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




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
