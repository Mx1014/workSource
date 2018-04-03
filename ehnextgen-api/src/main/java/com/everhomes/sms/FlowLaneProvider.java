// @formatter:off
package com.everhomes.sms;

public interface FlowLaneProvider {

	void createFlowLane(FlowLane flowLane);

	void updateFlowLane(FlowLane flowLane);

	FlowLane findById(Long id);

}