// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>announcementId: 公告ID</li>
 * <li>communityId: 用户当前所在的小区ID，如果没有则不填</li>
 * </ul>
 */
public class GetAnnouncementCommand {
    private Long announcementId;
    private Long communityId;

    public GetAnnouncementCommand() {
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
}
