package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul> 查看单个公司的具体属性（标准版）
 * <li>organizationId: 域空间</li>
 * </ul>
 *
 */
public class FindEnterpriseDetailCommand {
    private Integer organizationId;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
