package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberWorkExperiences;
import com.everhomes.util.StringHelper;


public class OrganizationMemberWorkExperiences extends EhOrganizationMemberWorkExperiences {

    public OrganizationMemberWorkExperiences() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
