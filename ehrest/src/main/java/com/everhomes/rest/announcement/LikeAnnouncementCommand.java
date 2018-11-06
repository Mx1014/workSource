// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>announcementId: 公告ID</li>
 * </ul>
 */
public class LikeAnnouncementCommand {
    private Long announcementId;

    public LikeAnnouncementCommand() {
    }


    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
