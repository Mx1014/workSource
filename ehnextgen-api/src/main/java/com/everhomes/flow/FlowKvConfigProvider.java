// @formatter:off
package com.everhomes.flow;

public interface FlowKvConfigProvider {

	void createFlowKvConfig(FlowKvConfig config);

	void updateFlowKvConfig(FlowKvConfig config);

	FlowKvConfig findById(Long id);

	FlowKvConfig findByKey(Integer namespaceId, String moduleType, Long moduleId,
						   String projectType, Long projectId, String ownerType, Long ownerId, String key);
}