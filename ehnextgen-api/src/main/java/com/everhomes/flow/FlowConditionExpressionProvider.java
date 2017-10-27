// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowConditionExpressionProvider {

	void createFlowConditionExpression(FlowConditionExpression flowConditionExpression);

	void updateFlowConditionExpression(FlowConditionExpression flowConditionExpression);

	FlowConditionExpression findById(Long id);

    void deleteFlowConditionExpression(Long flowMainId, Integer flowVersion);

    List<FlowConditionExpression> listFlowConditionExpression(Long conditionId);
}