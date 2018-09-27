// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId： 域空间ID</li>
 *     <li>version: 版本号</li>
 * </ul>
 */
public class ListPersonalCenterSettingsCommand {
    private Integer namespaceId;
    private Integer version;
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
