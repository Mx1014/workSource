package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderAttachments;
import com.everhomes.util.StringHelper;

public class RentalOrderAttachment extends EhRentalv2OrderAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7299200251846138759L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
