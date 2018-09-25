// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * 公告详情的actionData
 * <ul>
 * <li>bulletinId: 公告id</li>
 * <li>communityId: 项目id</li>
 * </ul>
 */
public class AnnouncementDetailActionData {
	private Long bulletinId;
	private Long communityId;

	public AnnouncementDetailActionData() {
		super();
	}

	public AnnouncementDetailActionData(Long bulletinId, Long communityId) {
		super();
		this.bulletinId = bulletinId;
		this.communityId = communityId;
	}

	public Long getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(Long bulletinId) {
		this.bulletinId = bulletinId;
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
	
	public String toUrlString(String url) {
		return url.replace("${bulletinId}", String.valueOf(bulletinId)).replace("${communityId}", String.valueOf(communityId));
	}
}
