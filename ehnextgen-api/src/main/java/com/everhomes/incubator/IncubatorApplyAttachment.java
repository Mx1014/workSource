package com.everhomes.incubator;

import com.everhomes.server.schema.tables.pojos.EhIncubatorApplies;
import com.everhomes.server.schema.tables.pojos.EhIncubatorApplyAttachments;
import com.everhomes.util.StringHelper;

public class IncubatorApplyAttachment extends EhIncubatorApplyAttachments{


	private static final long serialVersionUID = -5389479145974352925L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
