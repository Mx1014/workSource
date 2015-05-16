// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentType: 附件类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * </ul>
 */
public class AttachmentDescriptor {
    private String contentType;
    
    private String contentUri;
    
    public AttachmentDescriptor() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
