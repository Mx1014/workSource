package com.everhomes.serviceconf;

import com.everhomes.server.schema.tables.pojos.EhCommunityServices;
import com.everhomes.util.StringHelper;


public class CommunityService extends EhCommunityServices{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CommunityService() {
		super();
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
