// @formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetPmKeXingBillStatCommand {

    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
