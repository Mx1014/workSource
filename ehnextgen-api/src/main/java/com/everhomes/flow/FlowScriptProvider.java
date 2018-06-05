package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowScriptProvider {

	Long getNextId();

	void createFlowScriptWithId(FlowScript obj);

	void updateFlowScript(FlowScript obj);

	FlowScript getFlowScriptById(Long id);

	List<FlowScript> queryFlowScripts(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	FlowScript findNewestFlowScript(Long scriptMainId);

	FlowScript findByMainIdAndVersion(Long scriptMainId, Integer scriptVersion);

    FlowScript findById(Long id);

	List<FlowScript> listFlowScripts(
			Integer namespaceId, String ownerType, Long ownerId, String moduleType,
			Long moduleId, String scriptType, String keyword, int pageSize, ListingLocator locator);

    List<FlowScript> listByScriptMainId(Long scriptMainId);

	void updateFlowScripts(List<FlowScript> flowScripts);
}
