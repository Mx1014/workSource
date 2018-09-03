package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomerAptitudeFlag;
import com.everhomes.util.StringHelper;

public class EnterpriseCustomerAptitudeFlag extends EhEnterpriseCustomerAptitudeFlag {


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
