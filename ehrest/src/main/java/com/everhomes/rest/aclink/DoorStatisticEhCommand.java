// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>moduleId：模块Id</li>
 * </ul>
 */
public class DoorStatisticEhCommand {
    private Long moduleId;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
