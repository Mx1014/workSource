//@formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/8/11.
 */
/**
 *<ul>
 * <li>organizationId:企业id</li>
 *</ul>
 */
public class ListAddressIdsByOrganizationIdCommand {
    private Long organizationId;

    public ListAddressIdsByOrganizationIdCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
