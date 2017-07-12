package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositionMaps;
import com.everhomes.util.StringHelper;

/**
 * Created by sfyan on 2016/11/7.
 */
public class OrganizationJobPositionMap extends EhOrganizationJobPositionMaps{

    public OrganizationJobPositionMap() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
