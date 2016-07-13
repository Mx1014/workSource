package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalConfigAttachments;
import com.everhomes.util.StringHelper;

public class RentalConfigAttachment extends EhRentalConfigAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919890645954453788L;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
