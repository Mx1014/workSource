// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentComments;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentComment extends EhEnterpriseMomentComments {

    private static final long serialVersionUID = -8756283722778167435L;

    public EnterpriseMomentComment() {

    }

    public EnterpriseMomentComment(Integer namespaceId, Long organizationId, Long enterpriseMomentId) {
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