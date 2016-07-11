// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.util.StringHelper;

public class CommunityPmMember extends EhOrganizationMembers {
    private static final long serialVersionUID = -3043944773594056029L;

	public CommunityPmMember() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
