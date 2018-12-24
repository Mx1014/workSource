// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentAttachments;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentAttachment extends EhEnterpriseMomentAttachments {
	
	private static final long serialVersionUID = 5369419233028422854L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}