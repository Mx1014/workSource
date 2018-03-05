package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : 公告ID</li>
 * <li>url : 公告预览URL</li>
 * </ul>
 */
public class EnterpriseNoticePreviewDTO {
    private Long id;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
