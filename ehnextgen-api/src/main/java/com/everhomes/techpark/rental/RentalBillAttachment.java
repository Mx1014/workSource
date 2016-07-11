package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalBillAttachments;
import com.everhomes.util.StringHelper;

public class RentalBillAttachment extends EhRentalBillAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7299200251846138759L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
