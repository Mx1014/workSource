// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>companyId：管理公司Id</li>
 * <li>companyName：管理公司名字</li>
 * </ul>
 */
public class AddDoorManagementCommand {
    private Long companyId;
    private String companyName;

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

