package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunityRequests;
import com.everhomes.util.StringHelper;

public class OrganizationCommunityRequest extends EhOrganizationCommunityRequests {
    /**
     * 
     */
    private static final long serialVersionUID = 1819638678632899486L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
