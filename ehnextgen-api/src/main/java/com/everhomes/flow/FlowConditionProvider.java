// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowConditionProvider {

	void createFlowCondition(FlowCondition flowCondition);

	void updateFlowCondition(FlowCondition flowCondition);

	FlowCondition findById(Long id);

    void deleteFlowCondition(Long flowMainId, Integer flowVersion);

    List<FlowCondition> listFlowCondition(String belongEntity, Long belongTo);

    List<FlowCondition> listFlowCondition(Long flowMainId, Integer flowVersion);
}