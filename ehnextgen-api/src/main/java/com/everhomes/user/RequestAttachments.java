package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhRequestAttachments;
import com.everhomes.util.StringHelper;

public class RequestAttachments extends EhRequestAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6497910330464633309L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
