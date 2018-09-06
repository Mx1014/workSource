package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentTrackers;
import com.everhomes.util.StringHelper;

public class EnterpriseInvestmentTracker extends EhEnterpriseInvestmentTrackers {

    private static final long serialVersionUID = -3230993663167759962L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
