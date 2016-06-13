package com.everhomes.rest.yellowPage;


import com.everhomes.util.StringHelper;

public class GetYellowPageDetailCommand { 
	private Byte type;
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
	
}
