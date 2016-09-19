package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceAttachments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceAttachment extends EhServiceAllianceAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9109338455354726608L;
	
	private String contentUrl;
	
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
