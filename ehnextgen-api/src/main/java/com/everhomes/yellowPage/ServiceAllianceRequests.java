package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceRequests;
import com.everhomes.user.RequestAttachments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceRequests extends EhServiceAllianceRequests {

	private static final long serialVersionUID = -1646542975928177259L;
	
	@ItemType(RequestAttachments.class)
	private List<RequestAttachments> attachments;

	public List<RequestAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<RequestAttachments> attachments) {
		this.attachments = attachments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	 
}
