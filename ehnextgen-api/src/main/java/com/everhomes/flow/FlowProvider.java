package com.everhomes.flow;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.ListFlowCommand;

import java.util.List;

public interface FlowProvider {

	Long createFlow(Flow obj);

	void updateFlow(Flow obj);

	void deleteFlow(Flow obj);

	Flow getFlowById(Long id);

	List<Flow> queryFlows(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	Flow findFlowByName(Integer namespaceId, Long moduleId, String moduleType,
                        String projectType, Long projectId, Long ownerId, String ownerType, String name);

	List<Flow> findFlowsByModule(ListingLocator locator, ListFlowCommand cmd);

	void flowMarkUpdated(Flow flow);

	Flow findSnapshotFlow(Long flowId, Integer flowVer);

	Flow getSnapshotFlowById(Long flowId);

	Flow getEnabledConfigFlow(Integer namespaceId, String projectType, Long projectId, Long moduleId,
			String moduleType, Long ownerId, String ownerType);

    List<Flow> listConfigFlowByCond(Integer namespaceId, String moduleType, Long moduleId,
									String projectType, Long projectId, String ownerType, Long ownerId);
}