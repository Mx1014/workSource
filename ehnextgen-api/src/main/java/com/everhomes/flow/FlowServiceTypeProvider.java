// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowServiceTypeProvider {

	void createFlowServiceType(FlowServiceType flowServiceType);

	void updateFlowServiceType(FlowServiceType flowServiceType);

	FlowServiceType findById(Long id);

    <T> List<T> listFlowServiceType(Integer namespaceId, Class<T> clazz);
}