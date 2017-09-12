// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCommentAttachments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceCommentAttachment extends EhServiceAllianceCommentAttachments {
	
	private static final long serialVersionUID = 7733126232900067580L;

    
    private String contentUrl;
    
    private Integer size;
    
    private String  metadata;
    
    public ServiceAllianceCommentAttachment() {
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}