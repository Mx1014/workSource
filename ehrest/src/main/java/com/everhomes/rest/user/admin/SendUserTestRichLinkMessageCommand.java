package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

public class SendUserTestRichLinkMessageCommand {
    private String title;
    private String coverUri;
    private String coverUrl;
    private String content;
    private String actionUrl;
    private Integer targetNamespaceId;
    private String targetPhone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Integer getTargetNamespaceId() {
        return targetNamespaceId;
    }

    public void setTargetNamespaceId(Integer targetNamespaceId) {
        this.targetNamespaceId = targetNamespaceId;
    }

    public String getTargetPhone() {
        return targetPhone;
    }

    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
