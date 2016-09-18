package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhRequestTemplates;
import com.everhomes.util.StringHelper;

public class RequestTemplates extends EhRequestTemplates {

	private static final long serialVersionUID = 6792060530008553932L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
