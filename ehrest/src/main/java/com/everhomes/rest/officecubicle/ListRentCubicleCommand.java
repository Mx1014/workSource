package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId</li>
 * <li>ownerType : community </li>
 * <li>ownerId : communityId 园区id</li>
 * </ul>
 */
public class ListRentCubicleCommand { 
	private Integer namespaceId;
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



	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
