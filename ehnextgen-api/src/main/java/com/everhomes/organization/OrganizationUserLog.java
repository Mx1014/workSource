package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationUserLogs;
import com.everhomes.util.StringHelper;

public class OrganizationUserLog extends EhOrganizationUserLogs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6394338961894648200L;



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
