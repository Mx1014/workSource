package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhAuthorizationRelations;
import com.everhomes.util.StringHelper;

public class AuthorizationRelation extends EhAuthorizationRelations {

	private static final long serialVersionUID = -1852518988310908484L;

	public AuthorizationRelation() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
