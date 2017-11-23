// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowConditionProvider {

	void createFlowCondition(FlowCondition flowCondition);

	void updateFlowCondition(FlowCondition flowCondition);

	FlowCondition findById(Long id);

    void deleteFlowCondition(Long flowMainId, Long flowNodeId, Integer flowVersion);

    List<FlowCondition> listFlowCondition(Long flowNodeId);

    List<FlowCondition> listFlowCondition(Long flowMainId, Integer flowVersion);
}