// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationAddressMappings;
import com.everhomes.util.StringHelper;

public class CommunityAddressMapping extends EhOrganizationAddressMappings {
    private static final long serialVersionUID = 6991821014883001951L;

	public CommunityAddressMapping() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
