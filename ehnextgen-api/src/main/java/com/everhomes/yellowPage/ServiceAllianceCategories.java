package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.util.StringHelper;

public class ServiceAllianceCategories extends EhServiceAllianceCategories {

	private static final long serialVersionUID = -7355170400034157715L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
