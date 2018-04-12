// @formatter:off
package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>id : 范围id</li>
 * <li>spaceId : 空间id</li>
 * <li>ownerType : EhCommunities 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 * </ul>
 *
 *  @author:dengs 2018年1月19日
 */
public class OfficeRangeDTO {
	private Long id;
	private Long spaceId;
	private String ownerType;
	private Long ownerId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
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
