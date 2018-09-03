package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentDemand;
import com.everhomes.util.StringHelper;

public class EnterpriseInvestmentDemand extends EhEnterpriseInvestmentDemand {

    private static final long serialVersionUID = 7590840615951433155L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
