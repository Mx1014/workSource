package com.everhomes.forum;

import com.everhomes.server.schema.tables.pojos.EhForumAttachments;

public class Attachment extends EhForumAttachments {
    private static final long serialVersionUID = -4002357962961081080L;
    
    private String contentUrl;
    
    private Integer size;
    
    private String  metadata;
    
    public Attachment() {
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
    
}
