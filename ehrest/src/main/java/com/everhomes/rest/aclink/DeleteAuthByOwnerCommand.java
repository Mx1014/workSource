package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DeleteAuthByOwnerCommand {
	private Integer namespaceId;
    @NotNull
    private Long ownerId;    
    @NotNull
    private Byte ownerType;
    @NotNull
    private Long userId;
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Byte getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
