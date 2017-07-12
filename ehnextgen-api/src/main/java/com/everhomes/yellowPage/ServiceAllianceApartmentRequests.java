package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApartmentRequests;
import com.everhomes.util.StringHelper;

public class ServiceAllianceApartmentRequests extends
		EhServiceAllianceApartmentRequests {

	private static final long serialVersionUID = 6486918659933481648L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
