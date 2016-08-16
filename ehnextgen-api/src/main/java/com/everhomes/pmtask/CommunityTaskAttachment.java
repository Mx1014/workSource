package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhCommunityTaskAttachments;
import com.everhomes.util.StringHelper;

public class CommunityTaskAttachment extends EhCommunityTaskAttachments{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
