package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhWebMenuScopes;
import com.everhomes.util.StringHelper;

public class WebMenuScope extends EhWebMenuScopes {
	
	private static final long serialVersionUID = -1852518988310908484L;

	public WebMenuScope() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
