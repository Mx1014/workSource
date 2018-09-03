package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentNowInfo;
import com.everhomes.util.StringHelper;

public class EnterpriseInvestmentNowInfo extends EhEnterpriseInvestmentNowInfo {

    private static final long serialVersionUID = -895067392560505252L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
