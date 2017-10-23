// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>laneId: 泳道id</li>
 *     <li>displayName: 名称</li>
 *     <li>displayNameAbsort: 异常状态下的名称</li>
 *     <li>identifierNodeLevel: identifierNodeLevel</li>
 *     <li>identifierNodeId: identifierNodeId</li>
 * </ul>
 */
public class UpdateFlowLaneCommand {

    @NotNull
    private Long laneId;
    private String displayName;
    private String displayNameAbsort;
    private Integer identifierNodeLevel;
    private Long identifierNodeId;

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

    public Integer getIdentifierNodeLevel() {
        return identifierNodeLevel;
    }

    public void setIdentifierNodeLevel(Integer identifierNodeLevel) {
        this.identifierNodeLevel = identifierNodeLevel;
    }

    public Long getIdentifierNodeId() {
        return identifierNodeId;
    }

    public void setIdentifierNodeId(Long identifierNodeId) {
        this.identifierNodeId = identifierNodeId;
    }

    public String getDisplayNameAbsort() {
        return displayNameAbsort;
    }

    public void setDisplayNameAbsort(String displayNameAbsort) {
        this.displayNameAbsort = displayNameAbsort;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
