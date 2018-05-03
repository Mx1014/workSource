// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>configVersion: (选填)配置版本</li>
 * </ul>
 */
public class GetUIConfigurationCommand extends BaseVisitorsysUICommand{
    private Long configVersion=0L;

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
