// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowConditionExpressionProvider {

	void createFlowConditionExpression(FlowConditionExpression flowConditionExpression);

	void updateFlowConditionExpression(FlowConditionExpression flowConditionExpression);

	FlowConditionExpression findById(Long id);

    void deleteFlowConditionExpression(Long flowMainId, Integer flowVersion);

    List<FlowConditionExpression> listFlowConditionExpression(Long flowMainId, Integer flowVersion, Long conditionId);

    void updateFlowConditionExpressions(List<FlowConditionExpression> expressions);

    List<FlowConditionExpression> listFlowConditionExpressionByFlow(Long flowMainId, Integer flowVersion);

    void deleteFlowConditionExpressionByCondition(Long conditionId);
}