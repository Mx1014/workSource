// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationContacts;
import com.everhomes.util.StringHelper;

public class CommunityPmContact extends EhOrganizationContacts {
	private static final long serialVersionUID = 1003398644391254226L;

	public CommunityPmContact() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
