// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowPredefinedParamProvider {

	void createFlowPredefinedParam(FlowPredefinedParam flowPredefinedParam);

	void updateFlowPredefinedParam(FlowPredefinedParam flowPredefinedParam);

	FlowPredefinedParam findById(Long id);

    List<FlowPredefinedParam> listPredefinedParam(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId, String entityType);
}