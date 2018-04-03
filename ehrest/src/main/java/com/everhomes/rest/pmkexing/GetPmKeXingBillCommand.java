// @formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>dateStr: 选择的日期 (eg: 2016-08)</li>
 * </ul>
 */
public class GetPmKeXingBillCommand {

    private Long organizationId;
    private String dateStr;

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
