// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.rest.visitorsys.ui.BaseVisitorsysUICommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>enterpriseId: (必填)公司id</li>
 * <li>locationId: (必填)办公地点id</li>
 * </ul>
 */
public class GetFormCommand extends BaseVisitorsysCommand {
    private Long enterpriseId;
    private Long locationId;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

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
