package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ConfigAttachments;
import com.everhomes.util.StringHelper;

public class RentalConfigAttachment extends EhRentalv2ConfigAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919890645954453788L;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
