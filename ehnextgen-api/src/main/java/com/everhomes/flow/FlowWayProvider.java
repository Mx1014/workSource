// @formatter:off
package com.everhomes.flow;

public interface FlowWayProvider {

	void createFlowWay(FlowWay flowWay);

	void updateFlowWay(FlowWay flowWay);

	FlowWay findById(Long id);

}