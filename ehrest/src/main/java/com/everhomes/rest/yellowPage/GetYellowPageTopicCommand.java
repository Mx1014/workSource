package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul> 
 * <li>ownerType:  </li>
 * <li>ownerId:  </li>
 * <li>type:  </li> 
 * </ul>
 */
public class GetYellowPageTopicCommand { 

	private String ownerType;
	
	@NotNull
	private Long ownerId;
	@NotNull
	private Byte type;
 
	 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
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
