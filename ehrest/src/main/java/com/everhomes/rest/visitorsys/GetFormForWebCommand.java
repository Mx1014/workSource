// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.rest.visitorsys.ui.BaseVisitorsysUICommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>enterpriseId: (必填)公司id</li>
 * </ul>
 */
public class GetFormForWebCommand extends BaseVisitorsysCommand {
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
