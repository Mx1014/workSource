package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhAuthorizations;
import com.everhomes.server.schema.tables.pojos.EhWebMenus;
import com.everhomes.util.StringHelper;

public class Authorization extends EhAuthorizations {

	private static final long serialVersionUID = -1852518988310908484L;

	public Authorization() {
    }

    public Authorization(String ownerType, Long ownerId, String identityType, String scope) {
        setOwnerType(ownerType);
        setOwnerId(ownerId);
        setIdentityType(identityType);
        setScope(scope);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
