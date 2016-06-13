package com.everhomes.rest.business;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : business's id</li>
 * 	<li>communityId :小区id，缺少小区id，推荐，是否收藏字段无效</li>
 * </ul>
 *
 */
public class FindBusinessByIdCommand {
	@NotNull
	private Long id;
	private Long communityId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
