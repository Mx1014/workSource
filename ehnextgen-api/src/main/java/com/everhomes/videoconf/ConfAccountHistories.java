package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfAccountHistories;
import com.everhomes.util.StringHelper;

public class ConfAccountHistories extends EhConfAccountHistories {

	private static final long serialVersionUID = -354421727409981102L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
