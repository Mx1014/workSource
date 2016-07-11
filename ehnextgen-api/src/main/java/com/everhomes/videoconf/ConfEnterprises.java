package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfEnterprises;
import com.everhomes.util.StringHelper;

public class ConfEnterprises extends EhConfEnterprises {

	private static final long serialVersionUID = 528166544448823363L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
