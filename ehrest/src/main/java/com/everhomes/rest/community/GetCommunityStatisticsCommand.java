package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	   <li>nameSpaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 */
public class GetCommunityStatisticsCommand {
	
	private Integer nameSpaceId;
	private Long communityId;
	
	public Integer getNameSpaceId() {
		return nameSpaceId;
	}

	public void setNameSpaceId(Integer nameSpaceId) {
		this.nameSpaceId = nameSpaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
