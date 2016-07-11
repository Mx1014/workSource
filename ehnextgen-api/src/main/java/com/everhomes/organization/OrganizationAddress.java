package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationAddresses;
import com.everhomes.util.StringHelper;

public class OrganizationAddress extends EhOrganizationAddresses {

    /**
     * 
     */
    private static final long serialVersionUID = -1085190896646182497L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
