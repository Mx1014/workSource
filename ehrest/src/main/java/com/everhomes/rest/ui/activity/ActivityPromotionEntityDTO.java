// @formatter:off
package com.everhomes.rest.ui.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 活动id</li>
 *     <li>posterUrl: 封面图url</li>
 *     <li>subject: 标题</li>
 *     <li>tag: 标签</li>
 *     <li>description: 内容</li>
 *     <li>startTime: 开始时间</li>
 * </ul>
 */
public class ActivityPromotionEntityDTO {

    private Long id;
    private String posterUrl;
    private String subject;
    private String tag;
    private String description;
    private Long startTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
