package com.everhomes.organization.pmsy;

import com.everhomes.server.schema.tables.pojos.EhPmsyCommunities;
import com.everhomes.util.StringHelper;

public class PmsyCommunity extends EhPmsyCommunities {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
