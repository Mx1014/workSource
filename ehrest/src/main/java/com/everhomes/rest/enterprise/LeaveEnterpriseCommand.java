package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 退出公司
 * <li>enterpriseId: 公司 ID</li>
 * </ul>
 * @author janson
 *
 */
public class LeaveEnterpriseCommand {
    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
