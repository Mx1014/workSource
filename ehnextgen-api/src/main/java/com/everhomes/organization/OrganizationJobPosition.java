package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositions;
import com.everhomes.util.StringHelper;

public class OrganizationJobPosition extends EhOrganizationJobPositions{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
