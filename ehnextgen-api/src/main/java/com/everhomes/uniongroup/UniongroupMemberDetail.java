// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.util.StringHelper;

public class UniongroupMemberDetail extends EhUniongroupMemberDetails {
	
	private static final long serialVersionUID = 8751516334864351356L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}