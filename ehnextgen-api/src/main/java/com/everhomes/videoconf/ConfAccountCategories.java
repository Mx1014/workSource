package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfAccountCategories;
import com.everhomes.util.StringHelper;

public class ConfAccountCategories  extends EhConfAccountCategories{

	private static final long serialVersionUID = 591019296244483828L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
