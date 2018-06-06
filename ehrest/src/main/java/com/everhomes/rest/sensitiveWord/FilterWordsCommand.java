// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>appId: 应用originID</li>
 *     <li>creatorUid: 发布人的userId</li>
 *     <li>publishTime: 发布时间</li>
 *     <li>text: 需要检测的文本内容</li>
 * </ul>
 */
public class FilterWordsCommand {

    private Long namespaceId;
    private Long appId;
    private Long creatorUid;
    private String publishTime;
    private String text;

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
