// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>appkey: (必填)appkey</li>
 * <li>signature: (必填)签名</li>
 * <li>nonce: (必填)3位随机数</li>
 * <li>timestamp: (必填)当前时间戳</li>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>enterpriseId: (必填)公司id</li>
 * <li>locationId: (必填)办公地点id</li>
 * </ul>
 */
public class GetUIFormCommand extends BaseVisitorsysUICommand {
    private Long enterpriseId;
    private Long locationId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
