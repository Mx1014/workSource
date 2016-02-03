package com.everhomes.rest.business;

/**
 * <ul>
 * 	<li>communityId:小区id</li>
 * 	<li>categoryId:分类id</li>
 * </ul>
 *
 */
public class ListBusinessByCommonityIdCommand {
	private Long communityId;
	private Long categoryId;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
