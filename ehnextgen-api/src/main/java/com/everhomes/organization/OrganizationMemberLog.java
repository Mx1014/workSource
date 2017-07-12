package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberLogs;
import com.everhomes.util.StringHelper;

public class OrganizationMemberLog extends EhOrganizationMemberLogs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6394338961894648200L;



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
