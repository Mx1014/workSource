// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>announcementId: 公告ID</li>
 * </ul>
 */
public class DeleteAnnouncementCommand {
    private Long forumId;
    private Long announcementId;

    public DeleteAnnouncementCommand() {
    }


    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
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
