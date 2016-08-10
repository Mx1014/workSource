package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.util.StringHelper;

public class OfficeCubicleAttachment extends EhOfficeCubicleAttachments {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4624254823425561120L;

	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
