// @formatter:off
package com.everhomes.rest.link;

import com.everhomes.util.StringHelper;

/**
 * <ul>用于消息的丰富LINK
 * <li>title: 标题 </li>
 * <li>coverUrl: 封面URL</li>
 * <li>content: 内容</li>
 * <li>actionUrl: 点击跳转URL（有可能是标准的HTTP跳转URL，也有可能是左邻的私有协议对应的跳转URL）</li>
 * </ul>
 */
public class RichLinkDTO{
	private String   title;
	private String   coverUrl;
	private String   content;
	private String   actionUrl;
	
	public RichLinkDTO() {
    }

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
