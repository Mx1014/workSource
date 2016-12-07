package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowVariableProvider {

	Long createFlowVariable(FlowVariable obj);

	void updateFlowVariable(FlowVariable obj);

	void deleteFlowVariable(FlowVariable obj);

	FlowVariable getFlowVariableById(Long id);

	List<FlowVariable> queryFlowVariables(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowVariable> findVariables(Integer namespaceId, Long ownerId,
			String ownerType, Long moduleId, String moduleType, String name, String varType);

}
