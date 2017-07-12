package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.ListFlowCommand;

public interface FlowProvider {

	Long createFlow(Flow obj);

	void updateFlow(Flow obj);

	void deleteFlow(Flow obj);

	Flow getFlowById(Long id);

	List<Flow> queryFlows(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	Flow findFlowByName(Integer namespaceId, Long moduleId, String moduleType,
			Long ownerId, String ownerType, String name);

	List<Flow> findFlowsByModule(ListingLocator locator, ListFlowCommand cmd);

	void flowMarkUpdated(Flow flow);

	Flow findSnapshotFlow(Long flowId, Integer flowVer);

	Flow getSnapshotFlowById(Long flowId);

	Flow getEnabledConfigFlow(Integer namespaceId, Long moduleId,
			String moduleType, Long ownerId, String ownerType);
	
}