// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentMessages;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentMessage extends EhEnterpriseMomentMessages {

    private static final long serialVersionUID = -933435342103293094L;

    public EnterpriseMomentMessage() {
        super();
    }

    public EnterpriseMomentMessage(Integer namespaceId, Long organizationId, Long enterpriseMomentId, Byte messageType) {
        super();
        setNamespaceId(namespaceId);
        setOrganizationId(organizationId);
        setEnterpriseMomentId(enterpriseMomentId);
        setMessageType(messageType);
        setSourceDeleteFlag(TrueOrFalseFlag.FALSE.getCode());
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}