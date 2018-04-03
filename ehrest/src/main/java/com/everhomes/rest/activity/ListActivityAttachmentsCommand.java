package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>pageAnchor: 锚点 没有则不传</li>
 *     <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListActivityAttachmentsCommand {

    private Long activityId;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
