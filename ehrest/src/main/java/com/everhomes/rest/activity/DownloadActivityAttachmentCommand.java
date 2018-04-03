package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>attachmentId: 附件id</li>
 *     <li>activityId: 活动id</li>
 * </ul>
 */
public class DownloadActivityAttachmentCommand {

    private Long attachmentId;

    private Long activityId;

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
