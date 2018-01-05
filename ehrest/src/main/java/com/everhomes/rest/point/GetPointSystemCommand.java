package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>systemId: 配置id</li>
 * </ul>
 */
public class GetPointSystemCommand {

    @NotNull
    private Long systemId;

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
