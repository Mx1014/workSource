package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.util.StringHelper;

public class YellowPage extends EhYellowPages {

	/**
	 * 
	 */
	private String posterUrl;
	private static final long serialVersionUID = -3549028327338435817L;
	private List<YellowPageAttachment> attachments =  new ArrayList<YellowPageAttachment>();
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<YellowPageAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<YellowPageAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
}
