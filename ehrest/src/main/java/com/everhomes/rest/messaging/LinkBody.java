package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

public class LinkBody {
    String title;
    String coverUrl;
    String content;
    String actionUrl;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
