package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroupMembers;
import com.everhomes.util.StringHelper;

public class EnterpriseContactGroupMember extends EhEnterpriseContactGroupMembers {
    /**
     * 
     */
    private static final long serialVersionUID = 8890532974674703695L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
