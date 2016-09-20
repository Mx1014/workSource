package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAttachments;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/9/2.
 */
public class OrganizationOwnerAttachment extends EhOrganizationOwnerAttachments {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
