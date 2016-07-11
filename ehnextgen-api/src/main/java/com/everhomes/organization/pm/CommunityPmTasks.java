// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationTasks;
import com.everhomes.util.StringHelper;

public class CommunityPmTasks extends EhOrganizationTasks {
	private static final long serialVersionUID = -1852518988310908484L;

	public CommunityPmTasks() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
