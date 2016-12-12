package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceSkipRule;
import com.everhomes.util.StringHelper;

public class ServiceAllianceSkipRule extends EhServiceAllianceSkipRule {


	private static final long serialVersionUID = 6637997540549144516L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
