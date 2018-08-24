// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * 公告详情的actionData
 * <ul>
 * <li>announcementId: 公告id</li>
 * <li>communityId: 项目id</li>
 * </ul>
 */
public class AnnouncementDetailActionData {
	private Long announcementId;
	private Long communityId;

	public AnnouncementDetailActionData() {
		super();
	}

	public AnnouncementDetailActionData(Long announcementId, Long communityId) {
		super();
		this.announcementId = announcementId;
		this.communityId = communityId;
	}

	public Long getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Long announcementId) {
		this.announcementId = announcementId;
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
		return url.replace("${announcementId}", String.valueOf(announcementId)).replace("${communityId}", String.valueOf(communityId));
	}
}
