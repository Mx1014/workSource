package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhResourceCategoryAssignments;
import com.everhomes.util.StringHelper;

public class ResourceCategoryAssignment extends EhResourceCategoryAssignments{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
