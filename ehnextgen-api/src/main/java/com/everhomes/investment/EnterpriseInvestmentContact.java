package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentContact;
import com.everhomes.util.StringHelper;

public class EnterpriseInvestmentContact extends EhEnterpriseInvestmentContact {


    private static final long serialVersionUID = 1063324609381535170L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
