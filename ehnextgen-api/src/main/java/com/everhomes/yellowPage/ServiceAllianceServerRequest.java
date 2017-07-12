package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceGolfRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceServerRequests;
import com.everhomes.util.StringHelper;

public class ServiceAllianceServerRequest extends EhServiceAllianceServerRequests{
    private static final long serialVersionUID = -6617815922370964258L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
