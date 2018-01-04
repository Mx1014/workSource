package com.everhomes.rest.servicehotline;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * 
 * <li>ownerType: 一般是community</li>  
 * <li>ownerId: ownerId</li>
 * <li>namespaceId: 域空间Id</li>
 * </ul>
 */
public class GetHotlineSubjectCommand { 

	private String ownerType;
	private Integer namespaceId;
	@NotNull
	private Long ownerId; 
 
	 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
