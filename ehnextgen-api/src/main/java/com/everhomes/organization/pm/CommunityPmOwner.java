// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationOwners;
import com.everhomes.util.StringHelper;

public class CommunityPmOwner extends EhOrganizationOwners {
	private static final long serialVersionUID = 1003398644391254226L;

	public CommunityPmOwner() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
