package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleScopes;
import com.everhomes.util.StringHelper;

public class ServiceModuleScope extends EhServiceModuleScopes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
