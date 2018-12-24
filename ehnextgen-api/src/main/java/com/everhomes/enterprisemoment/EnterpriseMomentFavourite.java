// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentFavourites;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentFavourite extends EhEnterpriseMomentFavourites {

    private static final long serialVersionUID = 2041950236537152717L;

    public EnterpriseMomentFavourite() {
        super();
    }

    public EnterpriseMomentFavourite(Integer namespaceId, Long organizationId, Long enterpriseMomentId) {
        super();
        setNamespaceId(namespaceId);
        setOrganizationId(organizationId);
        setEnterpriseMomentId(enterpriseMomentId);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}