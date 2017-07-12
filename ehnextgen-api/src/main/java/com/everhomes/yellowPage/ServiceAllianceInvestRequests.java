package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceInvestRequests;
import com.everhomes.util.StringHelper;

public class ServiceAllianceInvestRequests extends EhServiceAllianceInvestRequests{
    private static final long serialVersionUID = -6617815922370964258L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
