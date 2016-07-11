// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunities;
import com.everhomes.util.StringHelper;

public class OrganizationCommunity extends EhOrganizationCommunities {
	private static final long serialVersionUID = 883648459124016666L;

	public OrganizationCommunity() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
