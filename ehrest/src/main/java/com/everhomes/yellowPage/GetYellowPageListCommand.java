package com.everhomes.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class GetYellowPageListCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Byte type;

	private Long parentId;
	
	private Long pageAnchor;
    
	private Integer pageSize;
	
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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	 @Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    }
}
