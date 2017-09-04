// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>laneId: 泳道id</li>
 *     <li>displayName: 名称</li>
 * </ul>
 */
public class UpdateFlowLaneCommand {

    @NotNull private Long laneId;
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
