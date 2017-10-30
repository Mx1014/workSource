// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>conditions: 如果branchDecider是按照条件判断则是条件列表 {@link com.everhomes.rest.flow.FlowConditionCommand}</li>
 * </ul>
 */
public class CreateFlowConditionCommand {

    @NotNull
    private Long flowId;

    @ItemType(FlowConditionCommand.class)
    private List<FlowConditionCommand> conditions;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public List<FlowConditionCommand> getConditions() {
        return conditions;
    }

    public void setConditions(List<FlowConditionCommand> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
