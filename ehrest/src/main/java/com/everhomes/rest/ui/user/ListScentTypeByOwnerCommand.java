// @formatter:off
package com.everhomes.rest.ui.user;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *   <li>ownerType: 归属者类型 </li>
 *   <li>ownerId: 归属者id</li>
 * 	 <li>communityId: 小区id</li>
 * </ul>
 */
public class ListScentTypeByOwnerCommand {
	private String ownerType;
	private Long   ownerId;
	@NotNull
	private Long   communityId;
	
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
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
}
