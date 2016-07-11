// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationBillItems;
import com.everhomes.util.StringHelper;

public class CommunityPmBillItem extends EhOrganizationBillItems {
    private static final long serialVersionUID = -4864277199003634058L;

	public CommunityPmBillItem() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
