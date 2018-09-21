// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>companyName: 公司名称</li>
 *     <li>companyId: 公司ID</li>
 * </ul>
 */
public class UpdateUserCompanyCommand {
    private String companyName;

    private Long companyId;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
