package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhResourceCategories;
import com.everhomes.util.StringHelper;

public class ResourceCategory extends EhResourceCategories {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
