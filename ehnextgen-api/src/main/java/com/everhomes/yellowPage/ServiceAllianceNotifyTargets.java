package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceNotifyTargets;
import com.everhomes.util.StringHelper;

public class ServiceAllianceNotifyTargets extends EhServiceAllianceNotifyTargets {

	private static final long serialVersionUID = -1554732502027183061L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
