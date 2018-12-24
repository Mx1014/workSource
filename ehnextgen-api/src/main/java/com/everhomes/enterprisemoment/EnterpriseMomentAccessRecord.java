// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentAccessRecords;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentAccessRecord extends EhEnterpriseMomentAccessRecords {
	
	private static final long serialVersionUID = -1683935744L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}