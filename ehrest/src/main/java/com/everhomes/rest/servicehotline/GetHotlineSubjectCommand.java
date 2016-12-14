package com.everhomes.rest.servicehotline;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * 
 * <li>ownerType: 一般是community</li>  
 * <li>ownerId: ownerId</li>  
 * </ul>
 */
public class GetHotlineSubjectCommand { 

	private String ownerType;
	
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
	
}
