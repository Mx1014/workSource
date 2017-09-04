// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowLaneProvider {

	void createFlowLane(FlowLane flowLane);

	void updateFlowLane(FlowLane flowLane);

	FlowLane findById(Long id);

    List<FlowLane> listFlowLane(Long flowMainId, Integer flowVersion);

    void deleteFlowLane(List<Long> deleteIdList);

    void deleteFlowLane(Long flowMainId, Integer flowVersion, List<Long> retainIdList);
}