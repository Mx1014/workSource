package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskAttachments;
import com.everhomes.util.StringHelper;

public class PmTaskAttachment extends EhPmTaskAttachments{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
