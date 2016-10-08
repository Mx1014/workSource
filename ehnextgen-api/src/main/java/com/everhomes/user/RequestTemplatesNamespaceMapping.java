package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhRequestTemplatesNamespaceMapping;
import com.everhomes.util.StringHelper;

public class RequestTemplatesNamespaceMapping extends
		EhRequestTemplatesNamespaceMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3341353648315390604L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
