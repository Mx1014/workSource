package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberContracts;
import com.everhomes.util.StringHelper;


public class OrganizationMemberContracts extends EhOrganizationMemberContracts {

    public OrganizationMemberContracts() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
