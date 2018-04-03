package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberInsurances;
import com.everhomes.util.StringHelper;


public class OrganizationMemberInsurances extends EhOrganizationMemberInsurances {

    public OrganizationMemberInsurances() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
