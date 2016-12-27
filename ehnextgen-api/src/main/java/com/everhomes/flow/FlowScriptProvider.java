package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowScriptProvider {

	Long createFlowScript(FlowScript obj);

	void updateFlowScript(FlowScript obj);

	void deleteFlowScript(FlowScript obj);

	FlowScript getFlowScriptById(Long id);

	List<FlowScript> queryFlowScripts(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowScript> findFlowScriptByModuleId(Long moduleId, String moduleType);

}
