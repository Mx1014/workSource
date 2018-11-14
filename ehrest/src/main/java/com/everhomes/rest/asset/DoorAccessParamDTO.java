package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * @author created by djm
 * 
 */
public class DoorAccessParamDTO {
	private Long id;
	private Long ownerId;
	private String ownerType;
	private Long orgId;
	private Long freezeDays;
    private Long unfreezeDays;
	private Long categoryId;
	
	public Long getFreezeDays() {
		return freezeDays;
	}
	public void setFreezeDays(Long freezeDays) {
		this.freezeDays = freezeDays;
	}
	public Long getUnfreezeDays() {
		return unfreezeDays;
	}
	public void setUnfreezeDays(Long unfreezeDays) {
		this.unfreezeDays = unfreezeDays;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
