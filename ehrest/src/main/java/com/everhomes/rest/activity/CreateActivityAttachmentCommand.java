package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>name: 附件名</li>
 *     <li>contentType: 内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>contentUri: 内容uri</li>
 * </ul>
 */
public class CreateActivityAttachmentCommand {

    private Long activityId;

    private String name;

    private String contentType;

    private String contentUri;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
