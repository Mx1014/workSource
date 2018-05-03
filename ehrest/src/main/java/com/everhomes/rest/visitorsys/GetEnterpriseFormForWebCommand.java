// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.rest.visitorsys.ui.BaseVisitorsysUICommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>deviceType: (必填)设备类型，{@link VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>enterpriseId: (必填)公司id</li>
 * </ul>
 */
public class GetEnterpriseFormForWebCommand extends BaseVisitorsysUICommand {
    private Integer enterpriseId;

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
