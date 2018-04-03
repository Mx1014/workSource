package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberEducations;
import com.everhomes.util.StringHelper;

public class OrganizationMemberEducations extends EhOrganizationMemberEducations{

    public OrganizationMemberEducations() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
