// @formatter:off
package com.everhomes.rest.comment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentType: 附件类型，{@link com.everhomes.rest.comment.ContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>contentUrl: 附件访问URL</li>
 * </ul>
 */
public class AttachmentDescriptor {
    private String contentType;

    private String contentUri;

    private String contentUrl;

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

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
