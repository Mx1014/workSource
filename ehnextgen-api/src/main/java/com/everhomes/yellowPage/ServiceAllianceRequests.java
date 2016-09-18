package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceRequests;
import com.everhomes.util.StringHelper;

public class ServiceAllianceRequests extends EhServiceAllianceRequests {

	private static final long serialVersionUID = -1646542975928177259L;

	 
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	 
}
