package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhYellowPageAttachments;
import com.everhomes.util.StringHelper;

public class  YellowPageAttachment extends EhYellowPageAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6670069780664774722L;
	private String contentUrl;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
 
}
