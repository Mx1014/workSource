// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>configVersion: (选填)配置版本</li>
 * </ul>
 */
public class GetConfigurationForWebCommand extends BaseVisitorsysCommand{
    private Long configVersion;

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
